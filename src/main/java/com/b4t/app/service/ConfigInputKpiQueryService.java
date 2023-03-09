package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigInputKpiQueryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigInputKpiQuery}.
 */
public interface ConfigInputKpiQueryService {

    /**
     * Save a configInputKpiQuery.
     *
     * @param configInputKpiQueryDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigInputKpiQueryDTO save(ConfigInputKpiQueryDTO configInputKpiQueryDTO);

    /**
     * Get all the configInputKpiQueries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigInputKpiQueryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" configInputKpiQuery.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigInputKpiQueryDTO> findOne(Long id);

    /**
     * Delete the "id" configInputKpiQuery.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
