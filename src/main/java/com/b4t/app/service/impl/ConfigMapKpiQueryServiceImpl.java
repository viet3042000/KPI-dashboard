package com.b4t.app.service.impl;

import com.b4t.app.service.ConfigMapKpiQueryService;
import com.b4t.app.domain.ConfigMapKpiQuery;
import com.b4t.app.repository.ConfigMapKpiQueryRepository;
import com.b4t.app.service.dto.ConfigMapKpiQueryDTO;
import com.b4t.app.service.mapper.ConfigMapKpiQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ConfigMapKpiQuery}.
 */
@Service
@Transactional
public class ConfigMapKpiQueryServiceImpl implements ConfigMapKpiQueryService {

    private final Logger log = LoggerFactory.getLogger(ConfigMapKpiQueryServiceImpl.class);

    private final ConfigMapKpiQueryRepository configMapKpiQueryRepository;

    private final ConfigMapKpiQueryMapper configMapKpiQueryMapper;

    public ConfigMapKpiQueryServiceImpl(ConfigMapKpiQueryRepository configMapKpiQueryRepository, ConfigMapKpiQueryMapper configMapKpiQueryMapper) {
        this.configMapKpiQueryRepository = configMapKpiQueryRepository;
        this.configMapKpiQueryMapper = configMapKpiQueryMapper;
    }

    /**
     * Save a configMapKpiQuery.
     *
     * @param configMapKpiQueryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMapKpiQueryDTO save(ConfigMapKpiQueryDTO configMapKpiQueryDTO) {
        log.debug("Request to save ConfigMapKpiQuery : {}", configMapKpiQueryDTO);
        ConfigMapKpiQuery configMapKpiQuery = configMapKpiQueryMapper.toEntity(configMapKpiQueryDTO);
        configMapKpiQuery = configMapKpiQueryRepository.save(configMapKpiQuery);
        return configMapKpiQueryMapper.toDto(configMapKpiQuery);
    }

    /**
     * Get all the configMapKpiQueries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMapKpiQueryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigMapKpiQueries");
        return configMapKpiQueryRepository.findAll(pageable)
            .map(configMapKpiQueryMapper::toDto);
    }


    /**
     * Get one configMapKpiQuery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMapKpiQueryDTO> findOne(Long id) {
        log.debug("Request to get ConfigMapKpiQuery : {}", id);
        return configMapKpiQueryRepository.findById(id)
            .map(configMapKpiQueryMapper::toDto);
    }

    /**
     * Delete the configMapKpiQuery by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMapKpiQuery : {}", id);
        configMapKpiQueryRepository.deleteById(id);
    }
}
