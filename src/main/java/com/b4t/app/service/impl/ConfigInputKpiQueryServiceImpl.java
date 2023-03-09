package com.b4t.app.service.impl;

import com.b4t.app.service.ConfigInputKpiQueryService;
import com.b4t.app.domain.ConfigInputKpiQuery;
import com.b4t.app.repository.ConfigInputKpiQueryRepository;
import com.b4t.app.service.dto.ConfigInputKpiQueryDTO;
import com.b4t.app.service.mapper.ConfigInputKpiQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ConfigInputKpiQuery}.
 */
@Service
@Transactional
public class ConfigInputKpiQueryServiceImpl implements ConfigInputKpiQueryService {

    private final Logger log = LoggerFactory.getLogger(ConfigInputKpiQueryServiceImpl.class);

    private final ConfigInputKpiQueryRepository configInputKpiQueryRepository;

    private final ConfigInputKpiQueryMapper configInputKpiQueryMapper;

    public ConfigInputKpiQueryServiceImpl(ConfigInputKpiQueryRepository configInputKpiQueryRepository, ConfigInputKpiQueryMapper configInputKpiQueryMapper) {
        this.configInputKpiQueryRepository = configInputKpiQueryRepository;
        this.configInputKpiQueryMapper = configInputKpiQueryMapper;
    }

    /**
     * Save a configInputKpiQuery.
     *
     * @param configInputKpiQueryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigInputKpiQueryDTO save(ConfigInputKpiQueryDTO configInputKpiQueryDTO) {
        log.debug("Request to save ConfigInputKpiQuery : {}", configInputKpiQueryDTO);
        ConfigInputKpiQuery configInputKpiQuery = configInputKpiQueryMapper.toEntity(configInputKpiQueryDTO);
        configInputKpiQuery = configInputKpiQueryRepository.save(configInputKpiQuery);
        return configInputKpiQueryMapper.toDto(configInputKpiQuery);
    }

    /**
     * Get all the configInputKpiQueries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigInputKpiQueryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigInputKpiQueries");
        return configInputKpiQueryRepository.findAll(pageable)
            .map(configInputKpiQueryMapper::toDto);
    }


    /**
     * Get one configInputKpiQuery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigInputKpiQueryDTO> findOne(Long id) {
        log.debug("Request to get ConfigInputKpiQuery : {}", id);
        return configInputKpiQueryRepository.findById(id)
            .map(configInputKpiQueryMapper::toDto);
    }

    /**
     * Delete the configInputKpiQuery by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigInputKpiQuery : {}", id);
        configInputKpiQueryRepository.deleteById(id);
    }
}
