package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMapChartMenuItem_;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapChartMenuItemService;
import com.b4t.app.domain.ConfigMapChartMenuItem;
import com.b4t.app.repository.ConfigMapChartMenuItemRepository;
import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;
import com.b4t.app.service.mapper.ConfigMapChartMenuItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Service Implementation for managing {@link ConfigMapChartMenuItem}.
 */
@Service
@Transactional
public class ConfigMapChartMenuItemServiceImpl implements ConfigMapChartMenuItemService {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartMenuItemServiceImpl.class);

    private final ConfigMapChartMenuItemRepository configMapChartMenuItemRepository;

    private final ConfigMapChartMenuItemMapper configMapChartMenuItemMapper;

    private final EntityManager entityManager;

    public ConfigMapChartMenuItemServiceImpl(ConfigMapChartMenuItemRepository configMapChartMenuItemRepository, ConfigMapChartMenuItemMapper configMapChartMenuItemMapper, EntityManager entityManager) {
        this.configMapChartMenuItemRepository = configMapChartMenuItemRepository;
        this.configMapChartMenuItemMapper = configMapChartMenuItemMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a configMapChartMenuItem.
     *
     * @param configMapChartMenuItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMapChartMenuItemDTO save(ConfigMapChartMenuItemDTO configMapChartMenuItemDTO) {
        log.debug("Request to save ConfigMapChartMenuItem : {}", configMapChartMenuItemDTO);
        ConfigMapChartMenuItem configMapChartMenuItem = configMapChartMenuItemMapper.toEntity(configMapChartMenuItemDTO);
        configMapChartMenuItem.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configMapChartMenuItem.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMapChartMenuItem = configMapChartMenuItemRepository.save(configMapChartMenuItem);
        return configMapChartMenuItemMapper.toDto(configMapChartMenuItem);
    }

    @Override
    public List<ConfigMapChartMenuItemDTO> saveAll(List<ConfigMapChartMenuItemDTO> configMapChartMenuItemDTOs) {
        if (DataUtil.isNullOrEmpty(configMapChartMenuItemDTOs)) return new ArrayList<>();
        configMapChartMenuItemDTOs = configMapChartMenuItemDTOs.stream().peek(item -> {
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigMapChartMenuItem> entities = configMapChartMenuItemMapper.toEntity(configMapChartMenuItemDTOs);
        entities = configMapChartMenuItemRepository.saveAll(entities);
        return configMapChartMenuItemMapper.toDto(entities);
    }

    /**
     * Get all the configMapChartMenuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMapChartMenuItemDTO> findAll(Long[] chartIds, Long[] menuItemIds, Long isMain, Long status, Pageable pageable) {
        log.debug("Request to get to page ConfigMapChartMenuItems");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMapChartMenuItem> criteria = cb.createQuery(ConfigMapChartMenuItem.class);
        Root<ConfigMapChartMenuItem> root = criteria.from(ConfigMapChartMenuItem.class);
        List<Predicate> predicates = new ArrayList<>();

        if (chartIds != null && !DataUtil.isNullOrEmpty(chartIds)) {
            Expression<Long> inExpression = root.get(ConfigMapChartMenuItem_.CHART_ID);
            predicates.add(inExpression.in(chartIds));
        }
        if (menuItemIds != null && !DataUtil.isNullOrEmpty(menuItemIds)) {
            Expression<Long> inExpression = root.get(ConfigMapChartMenuItem_.MENU_ITEM_ID);
            predicates.add(inExpression.in(menuItemIds));
        }
        if (isMain != null) {
            predicates.add(cb.equal(root.get(ConfigMapChartMenuItem_.IS_MAIN), isMain));
        }

        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigMapChartMenuItem_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<ConfigMapChartMenuItem> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigMapChartMenuItem> rootCount = countQuery.from(ConfigMapChartMenuItem.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigMapChartMenuItemDTO> rsDTOs = rs.stream().map(configMapChartMenuItemMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigMapChartMenuItemDTO> findAll(Long[] chartIds, Long[] menuItemIds, Long isMain, Long status) {
        log.debug("Request to get all ConfigMapChartMenuItems");
        Page<ConfigMapChartMenuItemDTO> rs = findAll(chartIds, menuItemIds, isMain, status, PageRequest.of(0, Integer.MAX_VALUE));
        return rs.getContent();
    }
    /**
     * Get one configMapChartMenuItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMapChartMenuItemDTO> findOne(Long id) {
        log.debug("Request to get ConfigMapChartMenuItem : {}", id);
        return configMapChartMenuItemRepository.findById(id)
            .map(configMapChartMenuItemMapper::toDto);
    }

    /**
     * Delete the configMapChartMenuItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMapChartMenuItem : {}", id);
        log.debug("Request to delete ConfigMapChartArea : {}", id);
        Optional<ConfigMapChartMenuItemDTO> entityOpt = findOne(id);
        entityOpt.ifPresent(this::delete);
    }

    @Override
    public void delete(ConfigMapChartMenuItemDTO dto) {
        if (dto == null) return;
        log.debug("Request to delete ConfigMapChartArea : {}", dto.getId());
        ConfigMapChartMenuItem entity = configMapChartMenuItemMapper.toEntity(dto);
        entity.setStatus(Constants.STATUS_DISABLED);
        entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMapChartMenuItemRepository.save(entity);
    }

    @Override
    public void delete(List<ConfigMapChartMenuItemDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        dtos = dtos.stream().peek(item -> {
            item.setStatus(Constants.STATUS_DISABLED);
            item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        configMapChartMenuItemRepository.saveAll(configMapChartMenuItemMapper.toEntity(dtos));
    }

    @Override
    public void delete(Long[] menuItemIds, Long[] chartIds) {
        List<ConfigMapChartMenuItemDTO> dtos = findAll(chartIds, menuItemIds, null, null);
        delete(dtos);
    }
}
