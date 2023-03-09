package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigChartItemService;
import com.b4t.app.domain.ConfigChartItem;
import com.b4t.app.repository.ConfigChartItemRepository;
import com.b4t.app.service.dto.ConfigChartItemDTO;
import com.b4t.app.service.mapper.ConfigChartItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigChartItem}.
 */
@Service
@Transactional
public class ConfigChartItemServiceImpl implements ConfigChartItemService {

    private final Logger log = LoggerFactory.getLogger(ConfigChartItemServiceImpl.class);

    private final ConfigChartItemRepository configChartItemRepository;

    private final ConfigChartItemMapper configChartItemMapper;

    public ConfigChartItemServiceImpl(ConfigChartItemRepository configChartItemRepository, ConfigChartItemMapper configChartItemMapper) {
        this.configChartItemRepository = configChartItemRepository;
        this.configChartItemMapper = configChartItemMapper;
    }

    /**
     * Save a configChartItem.
     *
     * @param configChartItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigChartItemDTO save(ConfigChartItemDTO configChartItemDTO) {
        log.debug("Request to save ConfigChartItem : {}", configChartItemDTO);
        ConfigChartItem configChartItem = configChartItemMapper.toEntity(configChartItemDTO);
        configChartItem.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configChartItem.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configChartItem = configChartItemRepository.save(configChartItem);
        return configChartItemMapper.toDto(configChartItem);
    }

    @Override
    public List<ConfigChartItemDTO> saveAll(List<ConfigChartItemDTO> dtos) {
        log.debug("Request to save ConfigChartItems : {}", dtos);
        if (!DataUtil.isNullOrEmpty(dtos)) return new ArrayList<>();
        List<ConfigChartItem> entites = configChartItemMapper.toEntity(dtos);
        entites = entites.stream().peek(i -> {
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        entites = configChartItemRepository.saveAll(entites);
        return configChartItemMapper.toDto(entites);
    }

    /**
     * Get all the configChartItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigChartItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigChartItems");
        return configChartItemRepository.findAll(pageable)
            .map(configChartItemMapper::toDto);
    }

    @Override
    public List<ConfigChartItemDTO> findByChartIds(List<Long> chartIds) {
        return configChartItemRepository.findAllByChartIdInAndStatus(chartIds, Constants.STATUS_ACTIVE).stream()
            .map(configChartItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConfigChartItemDTO> findByChartId(Long chartId) {
        return configChartItemRepository.findAllByChartIdAndStatus(chartId, Constants.STATUS_ACTIVE).stream()
            .map(configChartItemMapper::toDto).collect(Collectors.toList());
    }


    /**
     * Get one configChartItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigChartItemDTO> findOne(Long id) {
        log.debug("Request to get ConfigChartItem : {}", id);
        return configChartItemRepository.findById(id)
            .map(configChartItemMapper::toDto);
    }

    /**
     * Delete the configChartItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigChartItem : {}", id);
        configChartItemRepository.deleteById(id);
    }

    @Override
    public void delete(List<ConfigChartItemDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        List<ConfigChartItem> entities = configChartItemMapper.toEntity(dtos);
        entities = entities.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        configChartItemRepository.saveAll(entities);
    }
}
