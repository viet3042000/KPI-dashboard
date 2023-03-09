package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.service.ConfigColumnQueryKpiService;
import com.b4t.app.domain.ConfigColumnQueryKpi;
import com.b4t.app.repository.ConfigColumnQueryKpiRepository;
import com.b4t.app.service.dto.ConfigColumnQueryKpiDTO;
import com.b4t.app.service.dto.ConfigQueryKpiColumn;
import com.b4t.app.service.mapper.ConfigColumnQueryKpiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigColumnQueryKpi}.
 */
@Service
@Transactional
public class ConfigColumnQueryKpiServiceImpl implements ConfigColumnQueryKpiService {

    private final Logger log = LoggerFactory.getLogger(ConfigColumnQueryKpiServiceImpl.class);

    private final ConfigColumnQueryKpiRepository configColumnQueryKpiRepository;

    private final ConfigColumnQueryKpiMapper configColumnQueryKpiMapper;

    public ConfigColumnQueryKpiServiceImpl(ConfigColumnQueryKpiRepository configColumnQueryKpiRepository, ConfigColumnQueryKpiMapper configColumnQueryKpiMapper) {
        this.configColumnQueryKpiRepository = configColumnQueryKpiRepository;
        this.configColumnQueryKpiMapper = configColumnQueryKpiMapper;
    }

    /**
     * Save a configColumnQueryKpi.
     *
     * @param configColumnQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigColumnQueryKpiDTO save(ConfigColumnQueryKpiDTO configColumnQueryKpiDTO) {
        log.debug("Request to save ConfigColumnQueryKpi : {}", configColumnQueryKpiDTO);
        ConfigColumnQueryKpi configColumnQueryKpi = configColumnQueryKpiMapper.toEntity(configColumnQueryKpiDTO);
        configColumnQueryKpi = configColumnQueryKpiRepository.save(configColumnQueryKpi);
        return configColumnQueryKpiMapper.toDto(configColumnQueryKpi);
    }

    /**
     * Get all the configColumnQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigColumnQueryKpiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigColumnQueryKpis");
        return configColumnQueryKpiRepository.findAll(pageable)
            .map(configColumnQueryKpiMapper::toDto);
    }


    /**
     * Get one configColumnQueryKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigColumnQueryKpiDTO> findOne(Long id) {
        log.debug("Request to get ConfigColumnQueryKpi : {}", id);
        return configColumnQueryKpiRepository.findById(id)
            .map(configColumnQueryKpiMapper::toDto);
    }

    /**
     * Delete the configColumnQueryKpi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigColumnQueryKpi : {}", id);
        configColumnQueryKpiRepository.deleteById(id);
    }
}
