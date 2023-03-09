package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.repository.ConfigProfileRepository;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.ConfigProfileMapper;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigProfile}.
 */
@Service
@Transactional
public class ConfigProfileServiceImpl implements ConfigProfileService {

    private final Logger log = LoggerFactory.getLogger(ConfigProfileServiceImpl.class);

    private final ConfigProfileRepository configProfileRepository;

    private final ConfigProfileMapper configProfileMapper;

    private final EntityManager entityManager;

    @Autowired
    private ConfigScreenService configScreenService;

    @Autowired
    private ConfigAreaService configAreaService;

    @Autowired
    private ConfigMapChartAreaService configMapChartAreaService;

    @Autowired
    private ConfigMapGroupChartAreaService configMapGroupChartAreaService;

    @Autowired
    private CommonService commonService;

    public ConfigProfileServiceImpl(ConfigProfileRepository configProfileRepository, ConfigProfileMapper configProfileMapper, EntityManager entityManager) {
        this.configProfileRepository = configProfileRepository;
        this.configProfileMapper = configProfileMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a configProfile.
     *
     * @param configProfileDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigProfileDTO save(ConfigProfileDTO configProfileDTO) {
        log.debug("Request to save ConfigProfile : {}", configProfileDTO);
        ConfigProfile configProfile = configProfileMapper.toEntity(configProfileDTO);
        configProfile.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configProfile.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configProfile = configProfileRepository.save(configProfile);
        return configProfileMapper.toDto(configProfile);
    }

    /**
     * Get all the configProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigProfileDTO> findAll(String keyword, Boolean hasScreenOnly, Long isDefault, Long status, Pageable pageable) {
        log.debug("Request to get all ConfigProfiles");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigProfile> criteria = cb.createQuery(ConfigProfile.class);
        Root<ConfigProfile> root = criteria.from(ConfigProfile.class);

        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<ConfigProfile> rootCnt = countCrit.from(ConfigProfile.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesCount = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate predicate = cb.or(
                cb.like(cb.lower(root.get(ConfigProfile_.PROFILE_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(ConfigProfile_.PROFILE_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(predicate);
            predicatesCount.add(predicate);
        }
        if (isDefault != null) {
            predicates.add(cb.equal(root.get(ConfigProfile_.IS_DEFAULT), isDefault));
            predicatesCount.add(cb.equal(rootCnt.get(ConfigProfile_.IS_DEFAULT), isDefault));
        }
        if (hasScreenOnly != null && hasScreenOnly) {
            Root<ConfigScreen> screen = criteria.from(ConfigScreen.class);
            predicates.add(cb.equal(root.get(ConfigProfile_.ID), screen.get(ConfigScreen_.PROFILE_ID)));
            criteria.select(root).distinct(true);

            Root<ConfigScreen> screenCnt = countCrit.from(ConfigScreen.class);
            predicatesCount.add(cb.equal(rootCnt.get(ConfigProfile_.ID), screenCnt.get(ConfigScreen_.PROFILE_ID)));
            countCrit.select(cb.countDistinct(rootCnt));
        } else {
            criteria.select(root);
            countCrit.select(cb.count(rootCnt));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigProfile_.STATUS), status));
        predicatesCount.add(cb.equal(rootCnt.get(ConfigProfile_.STATUS), status));

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : sort) {
                if (ConfigProfile_.ORDER_INDEX.equals(order.getProperty()) && order.isAscending()) {
                    Expression exp = cb.coalesce(root.get(ConfigProfile_.ORDER_INDEX), Long.MAX_VALUE);
                    orders.add(cb.asc(exp));
                } else if (order.isDescending()) {
                    orders.add(cb.desc(root.get(order.getProperty())));
                } else {
                    orders.add(cb.asc(root.get(order.getProperty())));
                }
            }
            criteria.orderBy(orders);
        }
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        List<ConfigProfile> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
        countCrit.where(cb.and(predicatesCount.toArray(new Predicate[predicatesCount.size()])));
        Long count = entityManager.createQuery(countCrit).getSingleResult();
        List<ConfigProfileDTO> rsDTOs = rs.stream().map(configProfileMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigProfileDTO> findAll(String keyword, Boolean hasScreenOnly, Long isDefault, Long status) {
        log.debug("Request to get all ConfigProfiles");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigProfile> criteria = cb.createQuery(ConfigProfile.class);
        Root<ConfigProfile> root = criteria.from(ConfigProfile.class);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate predicate = cb.or(
                cb.like(cb.lower(root.get(ConfigProfile_.PROFILE_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(ConfigProfile_.PROFILE_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(predicate);
        }
        if (isDefault != null) {
            predicates.add(cb.equal(root.get(ConfigProfile_.IS_DEFAULT), isDefault));
        }
        if (hasScreenOnly != null && hasScreenOnly) {
            Root<ConfigScreen> screen = criteria.from(ConfigScreen.class);
            predicates.add(cb.equal(root.get(ConfigProfile_.ID), screen.get(ConfigScreen_.PROFILE_ID)));
            criteria.select(root).distinct(true);
        } else {
            criteria.select(root);
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigProfile_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(cb.asc(cb.coalesce(root.get(ConfigProfile_.ORDER_INDEX), Long.MAX_VALUE)));

        List<ConfigProfile> rs = entityManager.createQuery(criteria).getResultList();
        return configProfileMapper.toDto(rs);
    }

    @Override
    public Optional<ConfigProfileDTO> findByCode(String code) {
        log.debug("Request to get ConfigProfile : {}", code);
        return configProfileRepository
            .findFirstByProfileCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(configProfileMapper::toDto);
    }

    /**
     * Get one configProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigProfileDTO> findOne(Long id) {
        log.debug("Request to get ConfigProfile : {}", id);
        return configProfileRepository.findById(id)
            .map(configProfileMapper::toDto);
    }

    @Override
    public List<ConfigProfileDTO> findByIds(List<Long> ids) {
        return configProfileMapper.toDto(configProfileRepository.findAllById(ids));
    }

    /**
     * Delete the configProfile by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigProfile : {}", id);
        Optional<ConfigProfile> entityOpt = configProfileRepository.findById(id);

        if (entityOpt.isPresent()) {
            ConfigProfile entity = entityOpt.get();
            entity.setStatus(Constants.STATUS_DISABLED);
            entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            configProfileRepository.save(entity);

            List<ConfigScreenDTO> screens = configScreenService.findAll(null, new Long[]{id}, null, null, null, null);
            if (!DataUtil.isNullOrEmpty(screens)) {
                configScreenService.delete(screens);
            }
        }
    }

    @Override
    public void deleteAll(List<ConfigProfileDTO> dtos) {
        List<ConfigProfile> entities = dtos.stream().map(d -> {
            ConfigProfile e = configProfileMapper.toEntity(d);
            e.setStatus(Constants.STATUS_DISABLED);
            e.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            e.setUpdateUser(SecurityUtils.getCurrentUserLogin().orElse(null));
            return e;
        }).collect(Collectors.toList());
        configProfileRepository.saveAll(entities);

        Long[] profileIds = dtos.stream().map(ConfigProfileDTO::getId).toArray(Long[]::new);
        List<ConfigScreenDTO> screens = configScreenService.findAll(null, profileIds, null, null, null, null);
        if (!DataUtil.isNullOrEmpty(screens)) {
            configScreenService.delete(screens);
        }
    }

    @Override
    public ConfigProfileDTO copy(Long cloneId) {
        ConfigProfileDTO newConfigProfileDTO = findOne(cloneId).get();
        String copyProfileCodePrefix = newConfigProfileDTO.getProfileCode() + Constants.COPY_SUFFIX_CODE;
        Integer copyProfileIdx = commonService.getMaxCode("config_profile", "profile_code", copyProfileCodePrefix);
        copyProfileIdx += 1;

        newConfigProfileDTO.setId(null);
        newConfigProfileDTO.setProfileCode(copyProfileCodePrefix + copyProfileIdx);
        String newName = String.format(Constants.COPY_FORMAT_NAME, newConfigProfileDTO.getProfileName(), copyProfileIdx);
        newConfigProfileDTO.setProfileName(newName);

        ConfigProfile cloneProfile = configProfileMapper.toEntity(newConfigProfileDTO);
        cloneProfile.setIsDefault(Constants.PROFILE_NORMAL);
        cloneProfile.setStatus(Constants.STATUS_ACTIVE);
        cloneProfile.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        cloneProfile.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        cloneProfile = configProfileRepository.save(cloneProfile);

        // copy screen
        List<ConfigScreenDTO> screens = configScreenService.findAll(null, new Long[]{cloneId}, null, null, null, Constants.STATUS_ACTIVE);
        if (DataUtil.isNullOrEmpty(screens)) return configProfileMapper.toDto(cloneProfile);
        ConfigProfile finalCloneProfile = cloneProfile;

        Map<Long, Long> mapScreenIds = new HashMap<>();
        screens.stream().peek(s -> {
            Long oldId = s.getId();
            s.setProfileId(finalCloneProfile.getId());
            String copyCodePreffix = s.getScreenCode() + Constants.COPY_SUFFIX_CODE;
            Integer copyIdx = commonService.getMaxCode("config_screen", "screen_code", copyCodePreffix);
            copyIdx += 1;
            s.setScreenCode(copyCodePreffix + copyIdx);
            s.setScreenName(String.format(Constants.COPY_FORMAT_NAME, s.getScreenName(), copyIdx));
            s.setId(null);
            s = configScreenService.save(s);
            mapScreenIds.put(oldId, s.getId());
        }).collect(Collectors.toList());

        Long[] screenIds = new Long[]{};
        screenIds = mapScreenIds.keySet().toArray(screenIds);

        Map<Long, Long> mapAreaIds = new HashMap<>();
        List<ConfigAreaDTO> areas = configAreaService.findByScreenIds(screenIds, null);
        if (DataUtil.isNullOrEmpty(areas)) return configProfileMapper.toDto(cloneProfile);
        areas.stream().peek(a -> {
            Long oldId = a.getId();
            a.setScreenId(mapScreenIds.get(a.getScreenId()));
            String copyCodePreffix = a.getAreaCode() + Constants.COPY_SUFFIX_CODE;
            Integer copyIdx = commonService.getMaxCode("config_area", "area_code", copyCodePreffix);
            copyIdx += 1;
            a.setAreaCode(copyCodePreffix + copyIdx);
            a.setAreaName(String.format(Constants.COPY_FORMAT_NAME, a.getAreaName(), copyIdx));
            a.setId(null);
            a = configAreaService.save(a);
            mapAreaIds.put(oldId, a.getId());
        }).collect(Collectors.toList());

        Long[] areaIds = new Long[]{};
        areaIds = mapAreaIds.keySet().toArray(areaIds);
        List<ConfigMapChartAreaDTO> mapChartAreas = configMapChartAreaService.findAll(null, areaIds, null, Constants.STATUS_ACTIVE);
        mapChartAreas = mapChartAreas.stream().peek(mca -> {
            mca.setId(null);
            mca.setAreaId(mapAreaIds.get(mca.getAreaId()));
            if (mca.getScreenIdNextto() != null && mapScreenIds.containsKey(mca.getScreenIdNextto())) {
                mca.setScreenIdNextto(mapScreenIds.get(mca.getScreenIdNextto()));
            }
        }).collect(Collectors.toList());
        configMapChartAreaService.saveAll(mapChartAreas);

        List<ConfigMapGroupChartAreaDTO> mapGroupChartAreas = configMapGroupChartAreaService.findAll(null, areaIds, Constants.STATUS_ACTIVE);
        mapGroupChartAreas = mapGroupChartAreas.stream().peek(mgca -> {
            mgca.setId(null);
            mgca.setAreaId(mapAreaIds.get(mgca.getAreaId()));
        }).collect(Collectors.toList());
        configMapGroupChartAreaService.saveAll(mapGroupChartAreas);
        return configProfileMapper.toDto(cloneProfile);
    }
}
