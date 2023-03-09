package com.b4t.app.service.impl;

import com.b4t.app.config.Constants;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigQueryChartService;
import com.b4t.app.domain.ConfigQueryChart;
import com.b4t.app.repository.ConfigQueryChartRepository;
import com.b4t.app.service.dto.ConfigQueryChartDTO;
import com.b4t.app.service.mapper.ConfigQueryChartMapper;
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
 * Service Implementation for managing {@link ConfigQueryChart}.
 */
@Service
@Transactional
public class ConfigQueryChartServiceImpl implements ConfigQueryChartService {

    private final Logger log = LoggerFactory.getLogger(ConfigQueryChartServiceImpl.class);

    private final ConfigQueryChartRepository configQueryChartRepository;

    private final ConfigQueryChartMapper configQueryChartMapper;

    public ConfigQueryChartServiceImpl(ConfigQueryChartRepository configQueryChartRepository, ConfigQueryChartMapper configQueryChartMapper) {
        this.configQueryChartRepository = configQueryChartRepository;
        this.configQueryChartMapper = configQueryChartMapper;
    }

    /**
     * Save a configQueryChart.
     *
     * @param configQueryChartDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigQueryChartDTO save(ConfigQueryChartDTO configQueryChartDTO) {
        log.debug("Request to save ConfigQueryChart : {}", configQueryChartDTO);
        ConfigQueryChart configQueryChart = configQueryChartMapper.toEntity(configQueryChartDTO);
        configQueryChart.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configQueryChart.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configQueryChart = configQueryChartRepository.save(configQueryChart);
        return configQueryChartMapper.toDto(configQueryChart);
    }

    /**
     * Get all the configQueryCharts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigQueryChartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigQueryCharts");
        return configQueryChartRepository.findAll(pageable)
            .map(configQueryChartMapper::toDto);
    }

    @Override
    public List<ConfigQueryChartDTO> findByIds(List<Long> ids) {
        return configQueryChartRepository.findByIdIn(ids).stream()
            .map(configQueryChartMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one configQueryChart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigQueryChartDTO> findOne(Long id) {
        log.debug("Request to get ConfigQueryChart : {}", id);
        return configQueryChartRepository.findById(id)
            .map(configQueryChartMapper::toDto);
    }

    /**
     * Delete the configQueryChart by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigQueryChart : {}", id);
        Optional<ConfigQueryChart> entityOpt = configQueryChartRepository.findById(id);
        if (!entityOpt.isPresent() || Constants.STATUS_DISABLED.equals(entityOpt.get().getStatus()))
            return;
        ConfigQueryChart entity = entityOpt.get();
        entity.setStatus(Constants.STATUS_DISABLED);
        entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configQueryChartRepository.save(entity);
    }

    @Override
    public void delete(List<Long> ids) {
        log.debug("Request to delete ConfigQueryChart : {}", ids);
        List<ConfigQueryChartDTO> dtos = findByIds(ids);
        List<ConfigQueryChart> entities = configQueryChartMapper.toEntity(dtos);
        entities = entities.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        configQueryChartRepository.saveAll(entities);
    }
}
