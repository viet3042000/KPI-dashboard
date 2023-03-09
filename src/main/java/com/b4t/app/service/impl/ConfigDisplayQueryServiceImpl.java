package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigDisplayQueryService;
import com.b4t.app.domain.ConfigDisplayQuery;
import com.b4t.app.repository.ConfigDisplayQueryRepository;
import com.b4t.app.service.dto.ConfigDisplayQueryDTO;
import com.b4t.app.service.mapper.ConfigDisplayQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigDisplayQuery}.
 */
@Service
@Transactional
public class ConfigDisplayQueryServiceImpl implements ConfigDisplayQueryService {

    private final Logger log = LoggerFactory.getLogger(ConfigDisplayQueryServiceImpl.class);

    private final ConfigDisplayQueryRepository configDisplayQueryRepository;

    private final ConfigDisplayQueryMapper configDisplayQueryMapper;

    public ConfigDisplayQueryServiceImpl(ConfigDisplayQueryRepository configDisplayQueryRepository, ConfigDisplayQueryMapper configDisplayQueryMapper) {
        this.configDisplayQueryRepository = configDisplayQueryRepository;
        this.configDisplayQueryMapper = configDisplayQueryMapper;
    }

    /**
     * Save a configDisplayQuery.
     *
     * @param configDisplayQueryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigDisplayQueryDTO save(ConfigDisplayQueryDTO configDisplayQueryDTO) {
        log.debug("Request to save ConfigDisplayQuery : {}", configDisplayQueryDTO);
        ConfigDisplayQuery configDisplayQuery = configDisplayQueryMapper.toEntity(configDisplayQueryDTO);
        configDisplayQuery.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configDisplayQuery.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configDisplayQuery = configDisplayQueryRepository.save(configDisplayQuery);
        return configDisplayQueryMapper.toDto(configDisplayQuery);
    }

    @Override
    public List<ConfigDisplayQueryDTO> saveAll(List<ConfigDisplayQueryDTO> dtos) {
        dtos = dtos.stream().peek(i -> {
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        List<ConfigDisplayQuery> entities = configDisplayQueryMapper.toEntity(dtos);
        entities = configDisplayQueryRepository.saveAll(entities);
        return configDisplayQueryMapper.toDto(entities);
    }

    /**
     * Get all the configDisplayQueries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigDisplayQueryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigDisplayQueries");
        return configDisplayQueryRepository.findAll(pageable)
            .map(configDisplayQueryMapper::toDto);
    }

    @Override
    public List<ConfigDisplayQueryDTO> findByChartItemIds(List<Long> chartItemIds) {
        return configDisplayQueryRepository.findAllByItemChartIdInAndStatus(chartItemIds, Constants.STATUS_ACTIVE)
            .stream().map(configDisplayQueryMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one configDisplayQuery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigDisplayQueryDTO> findOne(Long id) {
        log.debug("Request to get ConfigDisplayQuery : {}", id);
        return configDisplayQueryRepository.findById(id)
            .map(configDisplayQueryMapper::toDto);
    }

    /**
     * Delete the configDisplayQuery by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigDisplayQuery : {}", id);
        configDisplayQueryRepository.deleteById(id);
    }

    @Override
    public void delete(List<ConfigDisplayQueryDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        List<ConfigDisplayQuery> entities = configDisplayQueryMapper.toEntity(dtos);
        entities = entities.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        configDisplayQueryRepository.saveAll(entities);
    }
}
