package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapChartMenuItemService;
import com.b4t.app.service.ConfigMenuItemService;
import com.b4t.app.service.dto.ConfigChartDTO;
import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;
import com.b4t.app.service.dto.ConfigMenuItemDTO;
import com.b4t.app.service.mapper.ConfigChartMapper;
import com.b4t.app.service.mapper.ConfigMenuItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigMenuItem}.
 */
@Service
@Transactional
public class ConfigMenuItemServiceImpl implements ConfigMenuItemService {

    private final Logger log = LoggerFactory.getLogger(ConfigMenuItemServiceImpl.class);

    private final ConfigMenuItemRepository configMenuItemRepository;

    private final ConfigMenuItemMapper configMenuItemMapper;

    private final ConfigChartMapper configChartMapper;

    private final EntityManager entityManager;

    @Autowired
    private ConfigMapChartMenuItemRepository configMapChartMenuItemRepository;

    @Autowired
    private ConfigMenuRepository configMenuRepository;

    @Autowired
    private ConfigMapChartMenuItemService configMapChartMenuItemService;

    @Autowired
    private ConfigChartRepository configChartRepository;
    @Autowired
    private CatItemRepository catItemRepository;

    public ConfigMenuItemServiceImpl(ConfigMenuItemRepository configMenuItemRepository, ConfigMenuItemMapper configMenuItemMapper, ConfigChartMapper configChartMapper, EntityManager entityManager) {
        this.configMenuItemRepository = configMenuItemRepository;
        this.configMenuItemMapper = configMenuItemMapper;
        this.configChartMapper = configChartMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a configMenuItem.
     *
     * @param configMenuItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMenuItemDTO save(ConfigMenuItemDTO configMenuItemDTO) {
        log.debug("Request to save ConfigMenuItem : {}", configMenuItemDTO);
        ConfigMenuItem configMenuItem = configMenuItemMapper.toEntity(configMenuItemDTO);
        configMenuItem.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configMenuItem.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMenuItem = configMenuItemRepository.save(configMenuItem);
        ConfigMenuItemDTO rs = configMenuItemMapper.toDto(configMenuItem);
        configMapChartMenuItemService.delete(new Long[]{rs.getId()}, null);
        if (!DataUtil.isNullOrEmpty(configMenuItemDTO.getMapCharts())) {
            List<ConfigMapChartMenuItemDTO> mapCharts = configMenuItemDTO.getMapCharts();
            AtomicInteger count = new AtomicInteger(1);
            mapCharts = mapCharts.stream().peek(item -> {
                item.setMenuItemId(rs.getId());
                item.setOrderIndex(count.longValue());
                item.setIsMain(Constants.STATUS_ACTIVE);
                count.getAndIncrement();
            }).collect(Collectors.toList());
            rs.setMapCharts(configMapChartMenuItemService.saveAll(mapCharts));
        }
        return rs;
    }


    @Override
    public void preSave(ConfigMenuItemDTO configMenuItemDTO) {
        if (!DataUtil.isNullOrEmpty(configMenuItemDTO.getConfigCharts())) {
            List<ConfigMapChartMenuItemDTO> lstMap = new ArrayList<>();
            configMenuItemDTO.getConfigCharts().forEach(bean -> {
                ConfigMapChartMenuItemDTO configMapChartMenuItemDTO = new ConfigMapChartMenuItemDTO();
                configMapChartMenuItemDTO.setChartId(bean.getId());
                configMapChartMenuItemDTO.setMenuItemId(configMenuItemDTO.getId());
                configMapChartMenuItemDTO.setStatus(Constants.STATUS_ACTIVE);
                configMapChartMenuItemDTO.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
                configMapChartMenuItemDTO.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
                ;
                lstMap.add(configMapChartMenuItemDTO);
            });
            configMenuItemDTO.setMapCharts(lstMap);
        }
    }

    @Override
    public List<ConfigMenuItemDTO> saveAll(List<ConfigMenuItemDTO> configMenuItemDTOS) {
        log.debug("Request to save all ConfigMenuItem : {}", configMenuItemDTOS.stream().map(item -> item.getId()).collect(Collectors.toList()).toString());
        List<ConfigMenuItemDTO> rs = new ArrayList<>();
        configMenuItemDTOS.stream().peek(item -> {
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            rs.add(save(item));
        }).collect(Collectors.toList());
        return rs;
    }

    /**
     * Get all the configMenuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMenuItemDTO> findAll(String keyword, Long[] menuIds, Long isDefault, Long status, Pageable pageable) {
        log.debug("Request to get all to page ConfigMenuItems");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<ConfigMenuItem> criteria = cb.createQuery(ConfigMenuItem.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigMenuItem> root = criteria.from(ConfigMenuItem.class);
        Root<ConfigMenuItem> rootCount = countQuery.from(ConfigMenuItem.class);

        createQueryFindAll(keyword, menuIds, isDefault, status, pageable, cb, criteria, root, predicates);
        List<ConfigMenuItem> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigMenuItemDTO> rsDTOs = rs.stream().map(configMenuItemMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMenuItemDTO> findAllRelate(String keyword, Long[] menuIds, Long isDefault, Long status, Pageable pageable) {
        log.debug("Request to get all ConfigMenuItems");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMenuItem> criteria = cb.createQuery(ConfigMenuItem.class);
        Root<ConfigMenuItem> root = criteria.from(ConfigMenuItem.class);
        List<Predicate> predicates = new ArrayList<>();
        createQueryFindAll(keyword, menuIds, isDefault, status, pageable, cb, criteria, root, predicates);
        List<ConfigMenuItem> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        List<ConfigMenuItemDTO> rsDTOs = rs.stream().map(configMenuItemMapper::toDto).collect(Collectors.toList());
        updateMenuItems(rsDTOs);
        Root<ConfigMenuItem> rootCount = countQuery.from(ConfigMenuItem.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    private void updateMenuItems(List<ConfigMenuItemDTO> configMenuItemDTOS) {
        if (DataUtil.isNullOrEmpty(configMenuItemDTOS)) return;
        Map<Long, List<ConfigChartDTO>> mapMenuAndChart = getMapMenuItem(configMenuItemDTOS);
        List<ConfigMenu> listConfigMenu = configMenuRepository.findAllByStatus(Constants.STATUS_ACTIVE);
        Map<Long, String> mapMenuIdMenuName = listConfigMenu.stream().collect(Collectors.toMap(ConfigMenu::getId, ConfigMenu::getMenuName));
        Map<Long, String> mapMenuAndDomainName = getMapMenuAndDomainName(listConfigMenu);
        configMenuItemDTOS.forEach(bean -> {
            bean.setMenuName(mapMenuIdMenuName.get(bean.getMenuId()));
            if (mapMenuAndChart != null && mapMenuAndChart.get(bean.getId()) != null) {
                bean.setConfigCharts(mapMenuAndChart.get(bean.getId()));
                List<String> chartNames = mapMenuAndChart.get(bean.getId()).stream().map(ConfigChartDTO::getChartName).collect(Collectors.toList());
                if (!DataUtil.isNullOrEmpty(chartNames)) {
                    String charts = String.join(", ", chartNames);
                    bean.setChartNames(charts);
                }
                if (mapMenuAndDomainName != null) {
                    bean.setDomainName(mapMenuAndDomainName.get(bean.getMenuId()));
                }
            }
        });
    }

    private Map<Long, String> getMapMenuAndDomainName(List<ConfigMenu> listConfigMenu) {
        List<String> lstCode = listConfigMenu.stream().map(ConfigMenu::getDomainCode).collect(Collectors.toList());
        List<CatItem> lstCatItems = catItemRepository.findAllByItemCodeInAndStatus(lstCode, Constants.STATUS_ACTIVE);
        if (lstCatItems == null) return null;
        Map<Long, String> mapResults = new HashMap<>();
        listConfigMenu.stream().forEach(bean -> {
            lstCatItems.stream().forEach(cat -> {
                if (bean.getDomainCode().equalsIgnoreCase(cat.getItemCode())) {
                    mapResults.put(bean.getId(), cat.getItemName());
                }
            });
        });
        return mapResults;
    }

    private Map<Long, List<ConfigChartDTO>> getMapMenuItem(List<ConfigMenuItemDTO> configMenuItemDTOS) {
        List<Long> menuItemIds = configMenuItemDTOS.stream().map(ConfigMenuItemDTO::getId).collect(Collectors.toList());
        List<ConfigMapChartMenuItem> mapChartMenuItems = configMapChartMenuItemRepository.findAllByMenuItemIdInAndStatus(menuItemIds, Constants.STATUS_ACTIVE);
        List<Long> lstChartIds = mapChartMenuItems.stream().map(ConfigMapChartMenuItem::getChartId).collect(Collectors.toList());
        List<ConfigChartDTO> lstCharts = configChartRepository.findAllById(lstChartIds).stream().map(configChartMapper::toDto).collect(Collectors.toList());
        if (DataUtil.isNullOrEmpty(lstCharts)) return null;
        Map<Long, List<ConfigChartDTO>> mapMenuItemChart = new HashMap<>();
        mapChartMenuItems.forEach(bean -> {
            List<ConfigChartDTO> listChart = new ArrayList<>();
            lstCharts.forEach(chartBean -> {
                if (bean.getChartId().equals(chartBean.getId())) {
                    if (mapMenuItemChart.get(bean.getMenuItemId()) == null) {
                        listChart.add(chartBean);
                        mapMenuItemChart.put(bean.getMenuItemId(), listChart);
                    } else {
                        mapMenuItemChart.get(bean.getMenuItemId()).add(chartBean);
                    }
                }
            });
        });
        return mapMenuItemChart;
    }

    private void createQueryFindAll(String keyword, Long[] menuIds, Long isDefault, Long status, Pageable pageable, CriteriaBuilder cb, CriteriaQuery<ConfigMenuItem> criteria, Root<ConfigMenuItem> root, List<Predicate> predicates) {
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(
                cb.or(
                    cb.like(cb.lower(root.get(ConfigMenuItem_.MENU_ITEM_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get(ConfigMenuItem_.MENU_ITEM_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
                )
            );
        }
        if (menuIds != null && !DataUtil.isNullOrEmpty(menuIds)) {
            Expression<Long> inExpression = root.get(ConfigMenuItem_.MENU_ID);
            predicates.add(inExpression.in(menuIds));
        }
        if (isDefault != null) {
            predicates.add(cb.equal(root.get(ConfigMenuItem_.IS_DEFAULT), isDefault));
        }

        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigMenuItem_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigMenuItemDTO> findAll(String keyword, Long[] menuIds, Long isDefault, Long status) {
        log.debug("Request to get all ConfigMenuItems");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMenuItem> criteria = cb.createQuery(ConfigMenuItem.class);
        Root<ConfigMenuItem> root = criteria.from(ConfigMenuItem.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(
                cb.or(
                    cb.like(cb.lower(root.get("menuItemCode")), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get("menuItemName")), keyword, Constants.DEFAULT_ESCAPE_CHAR)
                )
            );
        }
        if (menuIds != null && !DataUtil.isNullOrEmpty(menuIds)) {
            Expression<Long> inExpression = root.get("menuId");
            predicates.add(inExpression.in(menuIds));
        }
        if (isDefault != null) {
            predicates.add(cb.equal(root.get("isDefault"), isDefault));
        }

        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get("status"), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(cb.desc(root.get("id")));

        List<ConfigMenuItem> rs = entityManager.createQuery(criteria).getResultList();
        List<ConfigMenuItemDTO> rsDTOs = rs.stream().map(configMenuItemMapper::toDto).collect(Collectors.toList());
        return rsDTOs;
    }

    @Override
    public List<ConfigMenuItemDTO> findByIds(List<Long> ids) {
        return configMenuItemMapper.toDto(configMenuItemRepository.findAllById(ids));
    }

    /**
     * Get one configMenuItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMenuItemDTO> findOne(Long id) {
        log.debug("Request to get ConfigMenuItem : {}", id);
        return configMenuItemRepository.findById(id)
            .map(configMenuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMenuItemDTO> findOneAllInfo(Long id) {
        log.debug("Request to get ConfigMenuItem : {}", id);
        Optional<ConfigMenuItemDTO> menuItemOpt = configMenuItemRepository.findById(id).map(configMenuItemMapper::toDto);
        if (menuItemOpt.isPresent()) {
            List<ConfigMenuItemDTO> lstMenu = Arrays.asList(menuItemOpt.get());
            updateMenuItems(lstMenu);
            Optional<ConfigMenuItemDTO> configMenuItem = Optional.of(lstMenu.get(0));
            return configMenuItem;
        }
        return null;
    }

    @Override
    public Optional<ConfigMenuItemDTO> findByCode(String code) {
        return configMenuItemRepository.findFirstByMenuItemCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(configMenuItemMapper::toDto);
    }

    /**
     * Delete the configMenuItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMenuItem : {}", id);
        //configMenuItemRepository.deleteById(id);
        Optional<ConfigMenuItemDTO> menuItem = findOne(id);
        menuItem.ifPresent(this::delete);
    }

    /**
     * Delete the configScreen.
     *
     * @param dto dto.
     */
    @Override
    public void delete(ConfigMenuItemDTO dto) {
        if (dto == null) return;
        log.debug("Request to delete ConfigMenuItem : {}", dto.getId());
        ConfigMenuItem entity = configMenuItemMapper.toEntity(dto);
        // delete map
        configMapChartMenuItemService.delete(new Long[]{dto.getId()}, null);
        // delete menu item
        entity.setStatus(Constants.STATUS_DISABLED);
        entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMenuItemRepository.save(entity);
    }

    @Override
    public void delete(List<ConfigMenuItemDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        dtos = dtos.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());

        Long[] ids = dtos.stream().map(ConfigMenuItemDTO::getId).toArray(Long[]::new);
        if (DataUtil.isNullOrEmpty(ids)) return;
        //delete map
        configMapChartMenuItemService.delete(ids, null);
        //delete entities
        configMenuItemRepository.saveAll(configMenuItemMapper.toEntity(dtos));
    }

}
