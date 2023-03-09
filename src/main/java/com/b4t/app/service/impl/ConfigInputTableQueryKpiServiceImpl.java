package com.b4t.app.service.impl;

import com.b4t.app.service.ConfigInputTableQueryKpiService;
import com.b4t.app.domain.ConfigInputTableQueryKpi;
import com.b4t.app.repository.ConfigInputTableQueryKpiRepository;
import com.b4t.app.service.dto.ConfigInputTableQueryKpiDTO;
import com.b4t.app.service.mapper.ConfigInputTableQueryKpiMapper;
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
 * Service Implementation for managing {@link ConfigInputTableQueryKpi}.
 */
@Service
@Transactional
public class ConfigInputTableQueryKpiServiceImpl implements ConfigInputTableQueryKpiService {

    private final Logger log = LoggerFactory.getLogger(ConfigInputTableQueryKpiServiceImpl.class);

    private final ConfigInputTableQueryKpiRepository configInputTableQueryKpiRepository;

    private final ConfigInputTableQueryKpiMapper configInputTableQueryKpiMapper;

    public ConfigInputTableQueryKpiServiceImpl(ConfigInputTableQueryKpiRepository configInputTableQueryKpiRepository, ConfigInputTableQueryKpiMapper configInputTableQueryKpiMapper) {
        this.configInputTableQueryKpiRepository = configInputTableQueryKpiRepository;
        this.configInputTableQueryKpiMapper = configInputTableQueryKpiMapper;
    }

    /**
     * Save a configInputTableQueryKpi.
     *
     * @param configInputTableQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigInputTableQueryKpiDTO save(ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO) {
        log.debug("Request to save ConfigInputTableQueryKpi : {}", configInputTableQueryKpiDTO);
        ConfigInputTableQueryKpi configInputTableQueryKpi = configInputTableQueryKpiMapper.toEntity(configInputTableQueryKpiDTO);
        configInputTableQueryKpi = configInputTableQueryKpiRepository.save(configInputTableQueryKpi);
        return configInputTableQueryKpiMapper.toDto(configInputTableQueryKpi);
    }

    /**
     * Get all the configInputTableQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigInputTableQueryKpiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigInputTableQueryKpis");
        return configInputTableQueryKpiRepository.findAll(pageable)
            .map(configInputTableQueryKpiMapper::toDto);
    }

    /**
     * Get one configInputTableQueryKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigInputTableQueryKpiDTO> findOne(Long id) {
        log.debug("Request to get ConfigInputTableQueryKpi : {}", id);
        return configInputTableQueryKpiRepository.findById(id)
            .map(configInputTableQueryKpiMapper::toDto);
    }

    /**
     * Delete the configInputTableQueryKpi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigInputTableQueryKpi : {}", id);
        configInputTableQueryKpiRepository.deleteById(id);
    }

    @Override
    public List<ConfigInputTableQueryKpiDTO> findAllByQueryKpiId(Integer queryKpiId) {
        return configInputTableQueryKpiRepository.findAllByQueryKpiId(queryKpiId).stream()
            .map(configInputTableQueryKpiMapper::toDto).collect(Collectors.toList());
    }
}
