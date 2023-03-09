package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.ConfigMapChartLinksRepository;
import com.b4t.app.repository.ConfigScreenRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.ConfigMapChartLinksMapper;
import com.b4t.app.service.mapper.ConfigScreenMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigScreen}.
 */
@Service
@Transactional
public class ConfigScreenServiceImpl implements ConfigScreenService {

    private final Logger log = LoggerFactory.getLogger(ConfigScreenServiceImpl.class);

    private final ConfigScreenRepository configScreenRepository;
    private final ConfigMapChartLinksRepository configMapChartLinksRepository;
    private final ConfigMapChartLinksService configMapChartLinksService;

    private final ConfigScreenMapper configScreenMapper;

    private final ConfigAreaService configAreaService;

    private final ConfigMapChartAreaService configMapChartAreaService;

    private final ConfigMapScreenAreaService configMapScreenAreaService;

    private final ConfigMapGroupChartAreaService configMapGroupChartAreaService;

    private final CommonService commonService;

    private final EntityManager entityManager;

    public ConfigScreenServiceImpl(
        ConfigScreenRepository configScreenRepository,
        ConfigMapChartLinksRepository configMapChartLinksRepository, ConfigMapChartLinksService configMapChartLinksService, ConfigAreaService configAreaService, ConfigScreenMapper configScreenMapper,
        ConfigMapChartAreaService configMapChartAreaService,
        CommonService commonService,
        ConfigMapGroupChartAreaService configMapGroupChartAreaService,
        EntityManager entityManager,
        ConfigMapScreenAreaService configMapScreenAreaService) {
        this.configScreenRepository = configScreenRepository;
        this.configMapChartLinksRepository = configMapChartLinksRepository;
        this.configMapChartLinksService = configMapChartLinksService;
        this.configAreaService = configAreaService;
        this.configScreenMapper = configScreenMapper;
        this.configMapChartAreaService = configMapChartAreaService;
        this.configMapGroupChartAreaService = configMapGroupChartAreaService;
        this.commonService = commonService;
        this.entityManager = entityManager;
        this.configMapScreenAreaService = configMapScreenAreaService;
    }

    /**
     * Save a configScreen.
     *
     * @param configScreenDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigScreenDTO save(ConfigScreenDTO configScreenDTO) {
        log.debug("Request to save ConfigScreen : {}", configScreenDTO);
        ConfigScreen configScreen = configScreenMapper.toEntity(configScreenDTO);
        configScreen.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configScreen.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configScreen = configScreenRepository.save(configScreen);
        ConfigScreenDTO result = configScreenMapper.toDto(configScreen);

        List<ConfigAreaDTO> existedArea = configAreaService.findByScreenIds(new Long[]{configScreen.getId()}, null);
        List<Long> existedAreaIds = existedArea.stream().map(ConfigAreaDTO::getId).collect(Collectors.toList());

        if (!DataUtil.isNullOrEmpty(configScreenDTO.getConfigAreaDTOs())) {
            List<ConfigAreaDTO> areas = configScreenDTO.getConfigAreaDTOs();
            List<ConfigAreaDTO> needSaveArea = areas.stream()
                .filter(i -> i.getId() == null || existedAreaIds.contains(i.getId())).collect(Collectors.toList());
            List<Long> needUpdateIds = needSaveArea.stream().filter(i -> i.getId() != null).map(ConfigAreaDTO::getId).collect(Collectors.toList());
            List<ConfigAreaDTO> needDeleteArea = existedArea.stream().filter(i -> !needUpdateIds.contains(i.getId())).collect(Collectors.toList());
            ConfigScreen finalConfigScreen = configScreen;
            if (!DataUtil.isNullOrEmpty(needDeleteArea)) {
                Long[] deleteAreaIds = needDeleteArea.stream().map(ConfigAreaDTO::getId).toArray(Long[]::new);
                configMapChartAreaService.delete(deleteAreaIds, null);
                configAreaService.delete(needDeleteArea);
            }
            if (!DataUtil.isNullOrEmpty(needSaveArea)) {
                needSaveArea = needSaveArea.stream().peek(i -> {
                    i.setScreenId(finalConfigScreen.getId());
                    i.setStatus(Constants.STATUS_ACTIVE);
                    i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
                    i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
                    List<ConfigMapChartAreaDTO> mapChart = i.getMapCharts();
                    List<ConfigMapScreenAreaDTO> mapScreen = i.getMapScreens();
                    i = configAreaService.save(i);
                    Long newAreaId = i.getId();
                    if (!DataUtil.isNullOrEmpty(mapChart)) {
                        mapChart = mapChart.stream().peek(m -> m.setAreaId(newAreaId)).collect(Collectors.toList());
                        i.setMapCharts(mapChart);
                    }
                    if (!DataUtil.isNullOrEmpty(mapScreen)) {
                        mapScreen = mapScreen.stream().peek(m -> m.setAreaId(newAreaId)).collect(Collectors.toList());
                        i.setMapScreens(mapScreen);
                    }
                }).collect(Collectors.toList());

                List<Long> lstAreaId = needSaveArea.stream().filter(e->e.getId() != null).map(ConfigAreaDTO::getId).collect(Collectors.toList());
                Long[] newAreaIds = null;
                if (!DataUtil.isNullOrEmpty(lstAreaId)) {
                    newAreaIds = lstAreaId.toArray(new Long[0]);
                }
                if (!DataUtil.isNullOrEmpty(newAreaIds)) {
                    configMapChartAreaService.delete(newAreaIds, null);
                }
                if (!DataUtil.isNullOrEmpty(newAreaIds)) {
                    configMapScreenAreaService.delete(newAreaIds, null);
                }
                List<ConfigMapChartAreaDTO> mapCharts = needSaveArea.stream().map(ConfigAreaDTO::getMapCharts).flatMap(List::stream).collect(Collectors.toList());
                if (!DataUtil.isNullOrEmpty(mapCharts)) {
                    configMapChartAreaService.saveAll(mapCharts);
                    configMapChartLinksRepository.deleteAllByScreenId(finalConfigScreen.getId());
                    mapCharts.stream().filter(i -> !DataUtil.isNullOrEmpty(i.getLinksChart())).forEach(bean -> {
                        List<ConfigMapChartLinks> lstSave = createConfigMapChartLink(bean, finalConfigScreen);
                        configMapChartLinksRepository.saveAll(lstSave);
                    });
                }
                List<ConfigMapScreenAreaDTO> mapScreens = needSaveArea.stream().map(ConfigAreaDTO::getMapScreens).flatMap(List::stream).collect(Collectors.toList());
                if (!DataUtil.isNullOrEmpty(mapScreens)) {
                    configMapScreenAreaService.saveAll(mapScreens);
                }
            }
            result.setConfigAreaDTOs(needSaveArea);
        }
        return result;
    }

    List<ConfigMapChartLinks> createConfigMapChartLink(ConfigMapChartAreaDTO bean, ConfigScreen finalConfigScreen) {
        List<ConfigMapChartLinks> lstSave = new ArrayList<>();
        bean.getLinksChart().forEach(item -> {
            ConfigMapChartLinks configMapChartLinks = new ConfigMapChartLinks();
            configMapChartLinks.setChartMapId(bean.getChartId());
            configMapChartLinks.setScreenId(finalConfigScreen.getId());
            configMapChartLinks.setChartLinkId(item);
            configMapChartLinks.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            configMapChartLinks.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            lstSave.add(configMapChartLinks);
        });
        return lstSave;
    }

    @Override
    public List<ConfigScreenDTO> saveAll(List<ConfigScreenDTO> configScreenDTOS) {
        if (DataUtil.isNullOrEmpty(configScreenDTOS)) return new ArrayList<>();
        configScreenDTOS = configScreenDTOS.stream().peek(item -> {
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigScreen> entities = configScreenMapper.toEntity(configScreenDTOS);
        return configScreenMapper.toDto(configScreenRepository.saveAll(entities));
    }

    /**
     * Get all the configScreens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigScreenDTO> findAll(String keyword, Long[] profileIds, Long[] menuIds, Long[] menuItemIds, Long isDefault, Long status, Pageable pageable) {
        log.debug("Request to get page ConfigMapChartArea");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigScreen> criteria = cb.createQuery(ConfigScreen.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigScreen> rootCount = countQuery.from(ConfigScreen.class);
        Root<ConfigScreen> root = criteria.from(ConfigScreen.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate likePredicate = cb.or(
                cb.like(cb.lower(root.get(ConfigScreen_.SCREEN_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(ConfigScreen_.SCREEN_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(likePredicate);
            countPredicates.add(likePredicate);
        }
        if (!DataUtil.isNullOrEmpty(profileIds)) {
            predicates.add(root.get(ConfigScreen_.PROFILE_ID).in(profileIds));
            countPredicates.add(rootCount.get(ConfigScreen_.PROFILE_ID).in(profileIds));
        }
        if (!DataUtil.isNullOrEmpty(menuItemIds)) {
            predicates.add(root.get(ConfigScreen_.MENU_ITEM_ID).in(menuItemIds));
            countPredicates.add(rootCount.get(ConfigScreen_.MENU_ITEM_ID).in(menuItemIds));
        }
        if (!DataUtil.isNullOrEmpty(menuIds)) {
            Root<ConfigMenuItem> menuItem = criteria.from(ConfigMenuItem.class);
            predicates.add(cb.equal(menuItem.get(ConfigMenuItem_.ID), root.get(ConfigScreen_.MENU_ITEM_ID)));
            if (status == null || Constants.STATUS_ACTIVE.equals(status)) {
                predicates.add(cb.equal(menuItem.get(ConfigMenuItem_.STATUS), Constants.STATUS_ACTIVE));
            }
            predicates.add(menuItem.get(ConfigMenuItem_.MENU_ID).in(menuIds));

            Root<ConfigMenuItem> menuItemCount = countQuery.from(ConfigMenuItem.class);
            countPredicates.add(cb.equal(menuItemCount.get(ConfigMenuItem_.ID), rootCount.get(ConfigScreen_.MENU_ITEM_ID)));
            if (status == null || Constants.STATUS_ACTIVE.equals(status)) {
                countPredicates.add(cb.equal(menuItemCount.get(ConfigMenuItem_.STATUS), Constants.STATUS_ACTIVE));
            }
            countPredicates.add(menuItemCount.get(ConfigMenuItem_.MENU_ID).in(menuIds));
        }
        if (isDefault != null) {
            predicates.add(cb.equal(root.get(ConfigScreen_.IS_DEFAULT), isDefault));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigScreen_.STATUS), status));
        countPredicates.add(cb.equal(rootCount.get(ConfigScreen_.STATUS), status));

        criteria.select(root).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : sort) {
                if (ConfigScreen_.ORDER_INDEX.equals(order.getProperty()) && order.isAscending()) {
                    Expression exp = cb.coalesce(root.get(ConfigScreen_.ORDER_INDEX), Long.MAX_VALUE);
                    orders.add(cb.asc(exp));
                } else if (order.isDescending()) {
                    orders.add(cb.desc(root.get(order.getProperty())));
                } else {
                    orders.add(cb.asc(root.get(order.getProperty())));
                }
            }
            criteria.orderBy(orders);
        }
        List<ConfigScreen> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        countQuery.select(cb.count(rootCount)).where(cb.and(countPredicates.toArray(new Predicate[countPredicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigScreenDTO> rsDTOs = rs.stream().map(configScreenMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigScreenDTO> findAll(String keyword, Long[] profileIds, Long[] menuIds, Long[] menuItemIds, Long isDefault, Long status) {
        log.debug("Request to get list ConfigMapChartArea");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigScreen> criteria = cb.createQuery(ConfigScreen.class);
        Root<ConfigScreen> root = criteria.from(ConfigScreen.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(
                cb.or(
                    cb.like(cb.lower(root.get(ConfigScreen_.SCREEN_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get(ConfigScreen_.SCREEN_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
                )
            );
        }
        if (!DataUtil.isNullOrEmpty(profileIds)) {
            Expression<Long> inExpression = root.get(ConfigScreen_.PROFILE_ID);
            predicates.add(inExpression.in(profileIds));
        }
        if (!DataUtil.isNullOrEmpty(menuItemIds)) {
            Expression<Long> inExpression = root.get(ConfigScreen_.MENU_ITEM_ID);
            predicates.add(inExpression.in(menuItemIds));
        }
        if (isDefault != null) {
            predicates.add(cb.equal(root.get(ConfigScreen_.IS_DEFAULT), isDefault));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigScreen_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Expression exp = cb.coalesce(root.get(ConfigScreen_.ORDER_INDEX), Long.MAX_VALUE);
        criteria.orderBy(cb.asc(exp), cb.desc(root.get(ConfigScreen_.ID)));
        List<ConfigScreen> rs = entityManager.createQuery(criteria).getResultList();
        return configScreenMapper.toDto(rs);
    }

    /**
     * Get one configScreen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigScreenDTO> findOne(Long id) {
        log.debug("Request to get ConfigScreen : {}", id);
        return configScreenRepository.findById(id)
            .map(configScreenMapper::toDto);
    }

    @Override
    public Optional<ConfigScreenDTO> findByCode(String code) {
        return configScreenRepository
            .findFirstByScreenCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(configScreenMapper::toDto);
    }

    @Override
    public List<ConfigScreenDTO> findByIds(List<Long> ids) {
        return configScreenMapper.toDto(configScreenRepository.findByIdInAndStatus(ids, Constants.STATUS_ACTIVE));
    }

    /**
     * Delete the configScreen by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigScreen : {}", id);
        Optional<ConfigScreenDTO> entityOpt = findOne(id);
        entityOpt.ifPresent(this::delete);
    }

    /**
     * Delete the configScreen.
     *
     * @param dto dto.
     */
    @Override
    public void delete(ConfigScreenDTO dto) {
        if (dto == null) return;
        log.debug("Request to delete ConfigArea : {}", dto.getId());
        ConfigScreen entity = configScreenMapper.toEntity(dto);
        entity.setStatus(Constants.STATUS_DISABLED);
        entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configScreenRepository.save(entity);

        List<ConfigAreaDTO> areas = configAreaService.findByScreenIds(new Long[]{dto.getId()}, null);
        Long[] areaIds = areas.stream().map(ConfigAreaDTO::getId).toArray(Long[]::new);

        configAreaService.delete(areas);
        if (!DataUtil.isNullOrEmpty(areaIds)) {
            configMapChartAreaService.delete(areaIds, null);
            configMapGroupChartAreaService.delete(areaIds, null);
            configMapScreenAreaService.delete(areaIds, null);
        }
    }

    @Override
    public void delete(List<ConfigScreenDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        dtos = dtos.stream().peek(item -> {
            item.setStatus(Constants.STATUS_DISABLED);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigScreen> entities = configScreenMapper.toEntity(dtos);
        configScreenRepository.saveAll(entities);

        Long[] screenIds = dtos.stream().map(ConfigScreenDTO::getId).toArray(Long[]::new);
        configMapScreenAreaService.delete(null, screenIds);
        List<ConfigAreaDTO> areas = configAreaService.findByScreenIds(screenIds, null);
        if (!DataUtil.isNullOrEmpty(areas)) {
            Long[] areaIds = areas.stream().map(ConfigAreaDTO::getId).toArray(Long[]::new);
            configAreaService.delete(areas);
            if (!DataUtil.isNullOrEmpty(areaIds)) {
                configMapChartAreaService.delete(areaIds, null);
                configMapGroupChartAreaService.delete(areaIds, null);
                configMapScreenAreaService.delete(areaIds, null);
            }
        }
    }

    @Override
    public ConfigScreenDTO copy(Long id) {
        ConfigScreenDTO newConfigScreenDTO = findOne(id).get();
        String copyScreenCodePrefix = newConfigScreenDTO.getScreenCode() + Constants.COPY_SUFFIX_CODE;
        Integer copyScreenIdx = commonService.getMaxCode("config_screen", "screen_code", copyScreenCodePrefix);
        copyScreenIdx += 1;

        newConfigScreenDTO.setId(null);
        newConfigScreenDTO.setScreenCode(copyScreenCodePrefix + copyScreenIdx);
        String newName = String.format(Constants.COPY_FORMAT_NAME, newConfigScreenDTO.getScreenName(), copyScreenIdx);
        newConfigScreenDTO.setScreenName(newName);

        ConfigScreen cloneScreen = configScreenMapper.toEntity(newConfigScreenDTO);
        cloneScreen.setIsDefault(Constants.SCREEN_NORMAL);
        cloneScreen.setMenuItemId(null);
        cloneScreen.setStatus(Constants.STATUS_ACTIVE);
        cloneScreen.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        cloneScreen.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        cloneScreen = configScreenRepository.save(cloneScreen);

        Map<String, Long> mapOldIdAndNewCodes = new HashMap<>();
        List<ConfigAreaDTO> areas = configAreaService.findByScreenIds(new Long[]{id}, null);
        if (DataUtil.isNullOrEmpty(areas)) return configScreenMapper.toDto(cloneScreen);
        ConfigScreen finalCloneScreen = cloneScreen;
        areas = areas.stream().peek(a -> {
            a.setScreenId(finalCloneScreen.getId());
            String copyCodePreffix = a.getAreaCode() + Constants.COPY_SUFFIX_CODE;
            Integer copyIdx = commonService.getMaxCode("config_area", "area_code", copyCodePreffix);
            copyIdx += 1;
            a.setAreaCode(copyCodePreffix + copyIdx);
            a.setAreaName(String.format(Constants.COPY_FORMAT_NAME, a.getAreaName(), copyIdx));
            mapOldIdAndNewCodes.put(a.getAreaCode(), a.getId());
            a.setId(null);
        }).collect(Collectors.toList());
        areas = configAreaService.saveAll(areas);
        Map<Long, Long> mapAreaIds = areas.stream()
            .collect(Collectors.toMap(e -> mapOldIdAndNewCodes.get(e.getAreaCode()), ConfigAreaDTO::getId));

        Long[] areaIds = new Long[]{};
        areaIds = mapAreaIds.keySet().toArray(areaIds);
        if (!DataUtil.isNullOrEmpty(areaIds)) {
            List<ConfigMapChartAreaDTO> mapChartAreas = configMapChartAreaService.findAll(null, areaIds, null, Constants.STATUS_ACTIVE);
            mapChartAreas = mapChartAreas.stream().peek(mca -> {
                mca.setId(null);
                mca.setAreaId(mapAreaIds.get(mca.getAreaId()));
            }).collect(Collectors.toList());
            configMapChartAreaService.saveAll(mapChartAreas);

            List<ConfigMapGroupChartAreaDTO> mapGroupChartAreas = configMapGroupChartAreaService.findAll(null, areaIds, Constants.STATUS_ACTIVE);
            mapGroupChartAreas = mapGroupChartAreas.stream().peek(mgca -> {
                mgca.setId(null);
                mgca.setAreaId(mapAreaIds.get(mgca.getAreaId()));
            }).collect(Collectors.toList());
            configMapGroupChartAreaService.saveAll(mapGroupChartAreas);

            List<ConfigMapScreenAreaDTO> mapScreenAreas = configMapScreenAreaService.findAll(null, areaIds, Constants.STATUS_ACTIVE);
            mapScreenAreas = mapScreenAreas.stream().peek(msa -> {
                msa.setId(null);
                msa.setAreaId(mapAreaIds.get(msa.getAreaId()));
            }).collect(Collectors.toList());
            configMapScreenAreaService.saveAll(mapScreenAreas);
        }
        Long newScreenId = cloneScreen.getId();
        List<ConfigMapChartLinksDTO> lstMapChartLink = configMapChartLinksService.findAllByScreenId(id).stream().peek(e -> {
            e.setId(null);
            e.setScreenId(newScreenId);
        }).collect(Collectors.toList());
        if (!DataUtil.isNullOrEmpty(lstMapChartLink)){
            configMapChartLinksService.saveAll(lstMapChartLink);
        }

        return configScreenMapper.toDto(cloneScreen);
    }

    public List<ConfigScreenDTO> findSreenWithProfile(Long profileId) {
        return configScreenRepository.findAllByProfileIdAndStatus(profileId, Constants.STATUS_ACTIVE).stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    public List<ConfigScreenDTO> findSreenWithProfileAndParent(Long profileId, Long parentId) {
        return configScreenRepository.findAllByProfileIdAndParentIdAndIsDefaultNotAndStatus(profileId, parentId, Arrays.asList(Constants.SCREEN_SLIDE), Constants.STATUS_ACTIVE).stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    public List<ConfigScreenDTO> findScreenRoot(Long profileId) {
        return configScreenRepository.findAllByProfileIdAndParentIdIsNullAndIsDefaultNotInAndStatusOrderByOrderIndex(profileId, Arrays.asList(Constants.SCREEN_SLIDE, Constants.SCREEN_DETAIL_CHILD), Constants.STATUS_ACTIVE).stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    public List<ConfigScreenDTO> findScreenHome(Long profileId) {
        return configScreenRepository.findeScreenHome(profileId, Constants.STATUS_ACTIVE).stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    public List<ConfigScreenDTO> findAllByParentIdAndStatus(Long parentId) {
        return configScreenRepository.findAllByParentIdAndStatus(parentId, Constants.STATUS_ACTIVE).stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    public List<ConfigScreenDTO> findTabsForScreen(Long profileId, Long currentScreenId, Long parentId) {
        List<ConfigScreenDTO> results = configScreenRepository.findAllByProfileIdAndParentIdAndIsDefaultNotAndStatus(profileId, currentScreenId, Arrays.asList(Constants.SCREEN_SLIDE, Constants.SCREEN_DETAIL_CHILD), Constants.STATUS_ACTIVE).
            stream().map(configScreenMapper::toDto).
            collect(Collectors.toList());
//        ConfigScreenDTO configScreenCurent = configScreenRepository.findById(currentScreenId).map(configScreenMapper::toDto).get();
        if (DataUtil.isNullOrEmpty(results)) {
            results = configScreenRepository.findAllByProfileIdAndParentIdAndIsDefaultNotAndStatus(profileId, parentId, Arrays.asList(Constants.SCREEN_SLIDE, Constants.SCREEN_DETAIL_CHILD), Constants.STATUS_ACTIVE).
                stream().
                map(configScreenMapper::toDto).
                collect(Collectors.toList());
//            configScreenCurent = configScreenRepository.findById(parentId).map(configScreenMapper::toDto).get();
        }
//        ConfigScreenDTO confingScreenHome = findScreenHome(profileId).get(0);
//        results.add(0, confingScreenHome);
//        results.add(1, configScreenCurent);
        return results;
    }

    @Override
    public List<ConfigScreenDTO> findAllByKeywordAndScreenType(String keyword, Long screenType) {
        return configScreenRepository.findAllByKeywordAndScreenType(DataUtil.makeLikeQuery(keyword), screenType)
            .stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConfigScreenDTO> findAllScreenType(List<Long> screenType) {
        return configScreenRepository.findAllScreenType(screenType)
            .stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConfigScreenDTO> findAllScreen() {
        return configScreenRepository.findAll()
            .stream().map(configScreenMapper::toDto).collect(Collectors.toList());
    }
}
