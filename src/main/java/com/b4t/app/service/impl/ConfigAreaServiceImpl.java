package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigArea_;
import com.b4t.app.domain.ConfigScreen;
import com.b4t.app.repository.ConfigMapChartLinksRepository;
import com.b4t.app.repository.ConfigScreenRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.domain.ConfigArea;
import com.b4t.app.repository.ConfigAreaRepository;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.ConfigAreaMapper;
import com.b4t.app.service.mapper.ConfigScreenMapper;
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
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigArea}.
 */
@Service
@Transactional
public class ConfigAreaServiceImpl implements ConfigAreaService {

    private final Logger log = LoggerFactory.getLogger(ConfigAreaServiceImpl.class);

    private final ConfigAreaRepository configAreaRepository;

    private final ConfigAreaMapper configAreaMapper;

    private final ConfigMapChartLinksRepository configMapChartLinksRepository;

    private final ConfigMapChartAreaService configMapChartAreaService;

    private final ConfigMapScreenAreaService configMapScreenAreaService;

    private final ConfigMapGroupChartAreaService configMapGroupChartAreaService;

    private final ConfigScreenRepository configScreenRepository;

    private final ConfigChartService configChartService;

    private final EntityManager entityManager;
    private final ConfigScreenMapper configScreenMapper;

    public ConfigAreaServiceImpl(
        ConfigAreaRepository configAreaRepository,
        ConfigAreaMapper configAreaMapper,
        ConfigMapChartLinksRepository configMapChartLinksRepository, ConfigMapChartAreaService configMapChartAreaService,
        ConfigMapGroupChartAreaService configMapGroupChartAreaService,
        ConfigScreenRepository configScreenRepository,
        ConfigChartService configChartService,
        ConfigMapScreenAreaService configMapScreenAreaService,
        EntityManager entityManager,
        ConfigScreenMapper configScreenMapper) {
        this.configAreaRepository = configAreaRepository;
        this.configAreaMapper = configAreaMapper;
        this.configMapChartLinksRepository = configMapChartLinksRepository;
        this.configMapChartAreaService = configMapChartAreaService;
        this.configMapScreenAreaService = configMapScreenAreaService;
        this.configMapGroupChartAreaService = configMapGroupChartAreaService;
        this.configScreenRepository = configScreenRepository;
        this.configChartService = configChartService;
        this.entityManager = entityManager;
        this.configScreenMapper = configScreenMapper;
    }

//    @Autowired
//    ConfigScreenService configScreenService;

    /**
     * Save a configArea.
     *
     * @param configAreaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigAreaDTO save(ConfigAreaDTO configAreaDTO) {
        log.debug("Request to save ConfigArea : {}", configAreaDTO);
        ConfigArea configArea = configAreaMapper.toEntity(configAreaDTO);
        configArea.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configArea.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configArea = configAreaRepository.save(configArea);
        return configAreaMapper.toDto(configArea);
    }

    @Override
    public List<ConfigAreaDTO> saveAll(List<ConfigAreaDTO> configAreaDTOS) {
        if (DataUtil.isNullOrEmpty(configAreaDTOS)) return new ArrayList<>();
        log.debug("Request to save all ConfigArea : {}", configAreaDTOS.stream().map(ConfigAreaDTO::getId).collect(Collectors.toList()).toString());
        configAreaDTOS = configAreaDTOS.stream().peek(item -> {
            item.setStatus(Constants.STATUS_ACTIVE);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigArea> rs = configAreaRepository.saveAll(configAreaMapper.toEntity(configAreaDTOS));
        return configAreaMapper.toDto(rs);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConfigAreaDTO> findAll(String keyword, Long[] screenIds, Long status, Pageable pageable) {
        log.debug("Request to get page ConfigMapChartArea");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigArea> criteria = cb.createQuery(ConfigArea.class);
        Root<ConfigArea> root = criteria.from(ConfigArea.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(
                cb.or(
                    cb.like(cb.lower(root.get(ConfigArea_.AREA_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get(ConfigArea_.AREA_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
                )
            );
        }
        if (screenIds != null && !DataUtil.isNullOrEmpty(screenIds)) {
            Expression<Long> inExpression = root.get(ConfigArea_.SCREEN_ID);
            predicates.add(inExpression.in(screenIds));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigArea_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<ConfigArea> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigArea> rootCount = countQuery.from(ConfigArea.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigAreaDTO> rsDTOs = rs.stream().map(configAreaMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigAreaDTO> findByScreenIds(Long[] screenIds, Long groupChartId) {
        List<ConfigAreaDTO> rs = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(screenIds)) {
            rs = configAreaMapper.toDto(configAreaRepository.findAllByScreenIdInAndStatus(screenIds, Constants.STATUS_ACTIVE));

            Long[] areaIds = rs.stream().map(ConfigAreaDTO::getId).toArray(Long[]::new);
            Long[] groupChartIds = groupChartId != null ? new Long[]{groupChartId} : null;
            List<ConfigMapChartAreaDTO> mapCharts = configMapChartAreaService.findAll(null, areaIds, groupChartIds, Constants.STATUS_ACTIVE);
            List<ConfigMapScreenAreaDTO> mapScreens = configMapScreenAreaService.findAll(null, areaIds, Constants.STATUS_ACTIVE);
            List<Long> chartIds = mapCharts.stream()
                .filter(m -> m.getChartId() != null).map(ConfigMapChartAreaDTO::getChartId)
                .collect(Collectors.toList());
            List<ConfigChartDTO> charts = configChartService.findByIds(chartIds);
            List<Long> nextToScreenIds = mapCharts.stream().filter(m -> m.getScreenIdNextto() != null)
                .map(ConfigMapChartAreaDTO::getScreenIdNextto)
                .collect(Collectors.toList());
            List<ConfigScreen> nextToScreens = configScreenRepository.findAllById(nextToScreenIds);
            List<ConfigMapGroupChartAreaDTO> mapGroupCharts = configMapGroupChartAreaService.findAll(groupChartIds, areaIds, Constants.STATUS_ACTIVE);

            List<Long> listScreenIds = mapScreens.stream()
                .filter(m -> m.getScreenId() != null).map(ConfigMapScreenAreaDTO::getScreenId)
                .collect(Collectors.toList());
            List<ConfigScreenDTO> listScreens = configScreenRepository.findByIdInAndStatus(listScreenIds, Constants.STATUS_ACTIVE)
                .stream().map(configScreenMapper::toDto).collect(Collectors.toList());
            rs.forEach(item -> {
                List<ConfigMapChartAreaDTO> itemMapCharts = mapCharts.stream()
                    .filter(mc -> item.getId().equals(mc.getAreaId()))
                    .peek(mc -> {
                        if (mc.getScreenIdNextto() != null) {
                            Optional<ConfigScreen> nextToScreen = nextToScreens.stream().filter(s -> s.getId().equals(mc.getScreenIdNextto())).findFirst();
                            if (!nextToScreen.isPresent() || Constants.STATUS_DISABLED.equals(nextToScreen.get().getStatus())) {
                                mc.setScreenIdNextto(null);
                            }
                        }
                        mc.setLinksChart(configMapChartLinksRepository.getListChartMapIdAndScreenId(mc.getChartId(), screenIds[0]));
                        Optional<ConfigChartDTO> chart = charts.stream().filter(c -> c.getId().equals(mc.getChartId())).findFirst();
                        chart.ifPresent(configChartDTO -> {
                            mc.setChartName(configChartDTO.getChartName());
                            mc.setTypeChart(configChartDTO.getTypeChart());
                        });
                    })
                    .collect(Collectors.toList());

                List<ConfigMapScreenAreaDTO> itemMapScreens = mapScreens.stream()
                    .filter(ms -> item.getId().equals(ms.getAreaId()))
                    .peek(ms -> {
                        Optional<ConfigScreenDTO> screen = listScreens.stream().filter(s -> s.getId().equals(ms.getScreenId())).findFirst();
                        screen.ifPresent(ConfigScreenDTO -> {
                            ms.setScreenName(ConfigScreenDTO.getScreenName());
                        });
                    })
                    .collect(Collectors.toList());
                item.setMapCharts(itemMapCharts);
                item.setMapScreens(itemMapScreens);
               /*
               Optional<ConfigProfileDTO> profileDTO = configProfileService.findOne(item.get());
                if (profileDTO.isPresent()) {
                    configScreenDTO.get().setProfileType(profileDTO.get().getType());
                }
                */
                List<ConfigMapGroupChartAreaDTO> itemMapGroupCharts =
                    mapGroupCharts.stream()
                        .filter(mgc -> item.getId().equals(mgc.getAreaId()))
                        .collect(Collectors.toList());
                item.setMapGroupCharts(itemMapGroupCharts);
                Optional<ConfigMapGroupChartAreaDTO> mapGroupChart =
                    mapGroupCharts.stream()
                        .filter(mgc -> item.getId().equals(mgc.getAreaId()))
                        .findFirst();
                if (groupChartId != null) {
                    mapGroupChart.ifPresent(configMapGroupChartAreaDTO -> item.setPositionJson(configMapGroupChartAreaDTO.getPositionJson()));
                }
            });
        }
        return rs;
    }

    /**
     * Get one configArea by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigAreaDTO> findOne(Long id) {
        log.debug("Request to get ConfigArea : {}", id);
        return configAreaRepository.findById(id)
            .map(configAreaMapper::toDto);
    }


    @Override
    public List<ConfigAreaDTO> getByCodes(String[] codes) {
        List<ConfigArea> areas = configAreaRepository
            .findByAreaCodeInAndStatus(codes, Constants.STATUS_ACTIVE);
        return configAreaMapper.toDto(areas);
    }

    /**
     * Delete the configArea by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigArea : {}", id);
        Optional<ConfigAreaDTO> areaOpt = findOne(id);
        areaOpt.ifPresent(this::delete);
    }

    /**
     * Delete the configScreen.
     *
     * @param dto dto.
     */
    @Override
    public void delete(ConfigAreaDTO dto) {
        if (dto == null) return;
        log.debug("Request to delete ConfigArea : {}", dto.getId());
        ConfigArea entity = configAreaMapper.toEntity(dto);
        // delete map
        configMapChartAreaService.delete(new Long[]{dto.getId()}, null);
        // delete area
        entity.setStatus(Constants.STATUS_DISABLED);
        entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configAreaRepository.save(entity);
    }

    @Override
    public void delete(List<ConfigAreaDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        dtos = dtos.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());

        Long[] ids = dtos.stream().map(ConfigAreaDTO::getId).toArray(Long[]::new);
        //delete map
        if (!DataUtil.isNullOrEmpty(ids)) {
            configMapChartAreaService.delete(ids, null);
        }
        //delete entities
        configAreaRepository.saveAll(configAreaMapper.toEntity(dtos));
    }

}
