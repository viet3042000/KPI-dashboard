package com.b4t.app.service.impl;


import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.ConfigMapScreenAreaRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapScreenAreaService;
import com.b4t.app.service.dto.ConfigMapChartAreaDTO;
import com.b4t.app.service.dto.ConfigMapScreenAreaDTO;
import com.b4t.app.service.mapper.ConfigMapChartAreaMapper;
import com.b4t.app.service.mapper.ConfigMapScreenAreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConfigMapScreenAreaServiceImpl implements ConfigMapScreenAreaService {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartAreaServiceImpl.class);

    private final ConfigMapScreenAreaRepository configMapScreenAreaRepository;

    private final ConfigMapScreenAreaMapper configMapScreenAreaMapper;

    private final EntityManager entityManager;

    public ConfigMapScreenAreaServiceImpl(ConfigMapScreenAreaRepository configMapScreenAreaRepository, ConfigMapScreenAreaMapper configMapScreenAreaMapper, EntityManager entityManager) {
        this.configMapScreenAreaRepository = configMapScreenAreaRepository;
        this.configMapScreenAreaMapper = configMapScreenAreaMapper;
        this.entityManager = entityManager;
    }

    @Override
    public List<ConfigMapScreenAreaDTO> saveAll(List<ConfigMapScreenAreaDTO> configMapScreenAreaDTOS) {
        if (DataUtil.isNullOrEmpty(configMapScreenAreaDTOS)) return new ArrayList<>();
        configMapScreenAreaDTOS = configMapScreenAreaDTOS.stream().peek(item -> {
            item.setStatus(Constants.STATUS_ACTIVE);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigMapScreenArea> entities = configMapScreenAreaMapper.toEntity(configMapScreenAreaDTOS);
        entities = configMapScreenAreaRepository.saveAll(entities);
        return configMapScreenAreaMapper.toDto(entities);
    }

    @Override
    public List<ConfigMapScreenAreaDTO> findAll(Long[] screenIds, Long[] areaIds, Long status) {
        log.debug("Request to get all ConfigMapScreenArea");
//
//
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<ConfigMapScreenArea> criteria = cb.createQuery(ConfigMapScreenArea.class);
//        Root<ConfigMapScreenArea> root = criteria.from(ConfigMapScreenArea.class);
//        Root<ConfigScreen> screen = criteria.from(ConfigScreen.class);
//        Root<ConfigArea> area = criteria.from(ConfigArea.class);
//        List<Predicate> predicates = new ArrayList<>();
//        if (!DataUtil.isNullOrEmpty(screenIds)) {
//            Expression<Long> inExpression = root.get(ConfigMapScreenArea_.SCREEN_ID);
//            predicates.add(inExpression.in(screenIds));
//        }
//        if (!DataUtil.isNullOrEmpty(areaIds)) {
//            Expression<Long> inExpression = root.get(ConfigMapScreenArea_.AREA_ID);
//            predicates.add(inExpression.in(areaIds));
//        }
//        status = status != null ? status : Constants.STATUS_ACTIVE;
//        predicates.add(cb.equal(root.get(ConfigMapScreenArea_.STATUS), status));
//        predicates.add(cb.equal(root.get(ConfigMapScreenArea_.SCREEN_ID), screen.get(ConfigScreen_.ID)));
//        predicates.add(cb.equal(root.get(ConfigMapScreenArea_.AREA_ID), area.get(ConfigArea_.ID)));
//        predicates.add(cb.equal(screen.get(ConfigScreen_.STATUS), Constants.STATUS_ACTIVE));
//        predicates.add(cb.equal(area.get(ConfigArea_.STATUS), Constants.STATUS_ACTIVE));
//
//        criteria.select(root).distinct(true);
//        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        List<ConfigMapScreenArea> rs = configMapScreenAreaRepository.getByAreaIdsAndScreenIdActive(screenIds, areaIds, status);
        return configMapScreenAreaMapper.toDto(rs);
    }

    @Override
    public void delete(Long[] areaIds, Long[] screenIds) {
        if (DataUtil.isNullOrEmpty(areaIds) && DataUtil.isNullOrEmpty(screenIds)) return;
        List<ConfigMapScreenAreaDTO> dtos = findAll(screenIds, areaIds, null);
        delete(dtos);
    }

    @Override
    public void delete(List<ConfigMapScreenAreaDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        dtos = dtos.stream().peek(item -> {
            item.setStatus(Constants.STATUS_DISABLED);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        configMapScreenAreaRepository.saveAll(configMapScreenAreaMapper.toEntity(dtos));
    }
}
