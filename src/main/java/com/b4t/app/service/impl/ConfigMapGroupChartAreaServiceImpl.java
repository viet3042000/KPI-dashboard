package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMapGroupChartArea;
import com.b4t.app.domain.ConfigMapGroupChartArea_;
import com.b4t.app.repository.ConfigMapGroupChartAreaRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapGroupChartAreaService;
import com.b4t.app.service.dto.ConfigMapGroupChartAreaDTO;
import com.b4t.app.service.mapper.ConfigMapGroupChartAreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
 * Service Implementation for managing {@link ConfigMapGroupChartArea}.
 */
@Service
@Transactional
public class ConfigMapGroupChartAreaServiceImpl implements ConfigMapGroupChartAreaService {

    private final Logger log = LoggerFactory.getLogger(ConfigMapGroupChartAreaServiceImpl.class);

    private final ConfigMapGroupChartAreaRepository configMapGroupChartAreaRepository;

    private final ConfigMapGroupChartAreaMapper configMapGroupChartAreaMapper;

    @Autowired
    private EntityManager entityManager;

    public ConfigMapGroupChartAreaServiceImpl(ConfigMapGroupChartAreaRepository configMapGroupChartAreaRepository, ConfigMapGroupChartAreaMapper configMapGroupChartAreaMapper) {
        this.configMapGroupChartAreaRepository = configMapGroupChartAreaRepository;
        this.configMapGroupChartAreaMapper = configMapGroupChartAreaMapper;
    }

    /**
     * Save a configMapGroupChartArea.
     *
     * @param configMapGroupChartAreaDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMapGroupChartAreaDTO save(ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO) {
        log.debug("Request to save ConfigMapGroupChartArea : {}", configMapGroupChartAreaDTO);
        ConfigMapGroupChartArea configMapGroupChartArea = configMapGroupChartAreaMapper.toEntity(configMapGroupChartAreaDTO);
        configMapGroupChartArea.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configMapGroupChartArea.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMapGroupChartArea = configMapGroupChartAreaRepository.save(configMapGroupChartArea);
        return configMapGroupChartAreaMapper.toDto(configMapGroupChartArea);
    }

    @Override
    public List<ConfigMapGroupChartAreaDTO> saveAll(List<ConfigMapGroupChartAreaDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return new ArrayList<>();
        dtos = dtos.stream().peek(dto -> {
            dto.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            dto.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigMapGroupChartArea> entities = configMapGroupChartAreaMapper.toEntity(dtos);
        entities = configMapGroupChartAreaRepository.saveAll(entities);
        return configMapGroupChartAreaMapper.toDto(entities);
    }

    /**
     * Get all the configMapGroupChartAreas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMapGroupChartAreaDTO> findAll(Long[] groupChartIds,Long[] areaIds, Long status, Pageable pageable) {
        log.debug("Request to get to page ConfigMapGroupChartAreas");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMapGroupChartArea> criteria = cb.createQuery(ConfigMapGroupChartArea.class);
        Root<ConfigMapGroupChartArea> root = criteria.from(ConfigMapGroupChartArea.class);

        List<Predicate> predicates = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(groupChartIds)) {
            Expression<Long> inExpression = root.get(ConfigMapGroupChartArea_.GROUP_CHART_ID);
            predicates.add(inExpression.in(groupChartIds));
        }
        if (!DataUtil.isNullOrEmpty(areaIds)) {
            Expression<Long> inExpression = root.get(ConfigMapGroupChartArea_.AREA_ID);
            predicates.add(inExpression.in(areaIds));
        }

        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigMapGroupChartArea_.STATUS), status));
        criteria.select(root);
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<ConfigMapGroupChartArea> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigMapGroupChartArea> rootCount = countQuery.from(ConfigMapGroupChartArea.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigMapGroupChartAreaDTO> rsDTOs = configMapGroupChartAreaMapper.toDto(rs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigMapGroupChartAreaDTO> findAll(Long[] groupChartIds,Long[] areaIds, Long status) {
        log.debug("Request to get all ConfigMapGroupChartAreas");
        Page<ConfigMapGroupChartAreaDTO> rs = findAll(groupChartIds, areaIds, status, PageRequest.of(0, Integer.MAX_VALUE));
        return rs.getContent();
    }

    /**
     * Get one configMapGroupChartArea by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMapGroupChartAreaDTO> findOne(Long id) {
        log.debug("Request to get ConfigMapGroupChartArea : {}", id);
        return configMapGroupChartAreaRepository.findById(id)
            .map(configMapGroupChartAreaMapper::toDto);
    }

    /**
     * Delete the configMapGroupChartArea by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMapGroupChartArea : {}", id);
        configMapGroupChartAreaRepository.deleteById(id);
    }

    @Override
    public void delete(Long[] areaIds, Long[] groupChartIds) {
        if (DataUtil.isNullOrEmpty(areaIds) && DataUtil.isNullOrEmpty(groupChartIds)) return;
        List<ConfigMapGroupChartAreaDTO> map = findAll(groupChartIds, areaIds, null);
        map = map.stream().peek(i -> i.setStatus(Constants.STATUS_DISABLED)).collect(Collectors.toList());
        saveAll(map);
    }

}
