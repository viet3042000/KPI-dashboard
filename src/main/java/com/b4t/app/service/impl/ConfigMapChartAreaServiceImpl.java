package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.ConfigMapChartAreaRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapChartAreaService;
import com.b4t.app.service.dto.ConfigMapChartAreaDTO;
import com.b4t.app.service.mapper.ConfigMapChartAreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigMapChartArea}.
 */
@Service
@Transactional
public class ConfigMapChartAreaServiceImpl implements ConfigMapChartAreaService {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartAreaServiceImpl.class);

    private final ConfigMapChartAreaRepository configMapChartAreaRepository;

    private final ConfigMapChartAreaMapper configMapChartAreaMapper;

    private final EntityManager entityManager;

    public ConfigMapChartAreaServiceImpl(ConfigMapChartAreaRepository configMapChartAreaRepository, ConfigMapChartAreaMapper configMapChartAreaMapper, EntityManager entityManager) {
        this.configMapChartAreaRepository = configMapChartAreaRepository;
        this.configMapChartAreaMapper = configMapChartAreaMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a configMapChartArea.
     *
     * @param configMapChartAreaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMapChartAreaDTO save(ConfigMapChartAreaDTO configMapChartAreaDTO) {
        log.debug("Request to save ConfigMapChartArea : {}", configMapChartAreaDTO);
        ConfigMapChartArea configMapChartArea = configMapChartAreaMapper.toEntity(configMapChartAreaDTO);
        configMapChartArea.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configMapChartArea.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMapChartArea = configMapChartAreaRepository.save(configMapChartArea);
        return configMapChartAreaMapper.toDto(configMapChartArea);
    }

    @Override
    public List<ConfigMapChartAreaDTO> saveAll(List<ConfigMapChartAreaDTO> configMapChartAreaDTOS) {
        if (DataUtil.isNullOrEmpty(configMapChartAreaDTOS)) return new ArrayList<>();
        configMapChartAreaDTOS = configMapChartAreaDTOS.stream().peek(item -> {
            item.setStatus(Constants.STATUS_ACTIVE);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigMapChartArea> entities = configMapChartAreaMapper.toEntity(configMapChartAreaDTOS);
        entities = configMapChartAreaRepository.saveAll(entities);
        return configMapChartAreaMapper.toDto(entities);
    }

    @Override
    public Page<ConfigMapChartAreaDTO> findAll(Long[] chartIds, Long[] areaIds, Long[] groupChartIds, Long status, Pageable pageable) {
        log.debug("Request to get page ConfigMapChartArea");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMapChartArea> criteria = cb.createQuery(ConfigMapChartArea.class);
        Root<ConfigMapChartArea> root = criteria.from(ConfigMapChartArea.class);
        List<Predicate> predicates = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(chartIds)) {
            Expression<Long> inExpression = root.get(ConfigMapChartArea_.CHART_ID);
            predicates.add(inExpression.in(chartIds));
        }
        if (!DataUtil.isNullOrEmpty(areaIds)) {
            Expression<Long> inExpression = root.get(ConfigMapChartArea_.AREA_ID);
            predicates.add(inExpression.in(areaIds));
        }
        if (!DataUtil.isNullOrEmpty(groupChartIds)) {
            Root<ConfigChart> chart = criteria.from(ConfigChart.class);
            predicates.add(cb.equal(root.get(ConfigMapChartArea_.CHART_ID), chart.get(ConfigChart_.ID)));
            Expression<Long> inExpression = chart.get(ConfigChart_.GROUP_CHART_ID);
            predicates.add(inExpression.in(groupChartIds));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigMapChartArea_.STATUS), status));
        criteria.select(root);
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<ConfigMapChartArea> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigMapChartArea> rootCount = countQuery.from(ConfigMapChartArea.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigMapChartAreaDTO> rsDTOs = configMapChartAreaMapper.toDto(rs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    public List<ConfigMapChartAreaDTO> findAll(Long[] chartIds, Long[] areaIds, Long[] groupChartIds, Long status) {
        log.debug("Request to get all ConfigMapChartArea");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMapChartArea> criteria = cb.createQuery(ConfigMapChartArea.class);
        Root<ConfigMapChartArea> root = criteria.from(ConfigMapChartArea.class);
        Root<ConfigChart> chart = criteria.from(ConfigChart.class);
        Root<ConfigArea> area = criteria.from(ConfigArea.class);
        List<Predicate> predicates = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(chartIds)) {
            Expression<Long> inExpression = root.get(ConfigMapChartArea_.CHART_ID);
            predicates.add(inExpression.in(chartIds));
        }
        if (!DataUtil.isNullOrEmpty(areaIds)) {
            Expression<Long> inExpression = root.get(ConfigMapChartArea_.AREA_ID);
            predicates.add(inExpression.in(areaIds));
        }
        if (!DataUtil.isNullOrEmpty(groupChartIds)) {
            Expression<Long> inExpression = chart.get(ConfigChart_.GROUP_CHART_ID);
            predicates.add(inExpression.in(groupChartIds));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigMapChartArea_.STATUS), status));
        predicates.add(cb.equal(root.get(ConfigMapChartArea_.CHART_ID), chart.get(ConfigChart_.ID)));
        predicates.add(cb.equal(chart.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));
        predicates.add(cb.equal(root.get(ConfigMapChartArea_.AREA_ID), area.get(ConfigArea_.ID)));
        predicates.add(cb.equal(area.get(ConfigArea_.STATUS), Constants.STATUS_ACTIVE));

        criteria.select(root).distinct(true);
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        List<ConfigMapChartArea> rs = entityManager.createQuery(criteria).getResultList();
        return configMapChartAreaMapper.toDto(rs);
    }

    /**
     * Get one configMapChartArea by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMapChartAreaDTO> findOne(Long id) {
        log.debug("Request to get ConfigMapChartArea : {}", id);
        return configMapChartAreaRepository.findById(id)
            .map(configMapChartAreaMapper::toDto);
    }

    /**
     * Delete the configMapChartArea by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMapChartArea : {}", id);
        Optional<ConfigMapChartAreaDTO> entityOpt = findOne(id);
        entityOpt.ifPresent(this::delete);
    }

    @Override
    public void delete(ConfigMapChartAreaDTO dto) {
        if (dto == null) return;
        log.debug("Request to delete ConfigMapChartArea : {}", dto.getId());
        ConfigMapChartArea entity = configMapChartAreaMapper.toEntity(dto);
        entity.setStatus(Constants.STATUS_DISABLED);
        entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMapChartAreaRepository.save(entity);
    }

    @Override
    public void delete(List<ConfigMapChartAreaDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        dtos = dtos.stream().peek(item -> {
            item.setStatus(Constants.STATUS_DISABLED);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        configMapChartAreaRepository.saveAll(configMapChartAreaMapper.toEntity(dtos));
    }

    @Override
    public void delete(Long[] areaIds, Long[] chartIds) {
        if (DataUtil.isNullOrEmpty(areaIds) && DataUtil.isNullOrEmpty(chartIds)) return;
        List<ConfigMapChartAreaDTO> dtos = findAll(chartIds, areaIds, null, null);
        delete(dtos);
    }
}
