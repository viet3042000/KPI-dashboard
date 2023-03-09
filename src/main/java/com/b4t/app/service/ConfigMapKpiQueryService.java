package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMapKpiQueryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMapKpiQuery}.
 */
public interface ConfigMapKpiQueryService {

    /**
     * Save a configMapKpiQuery.
     *
     * @param configMapKpiQueryDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMapKpiQueryDTO save(ConfigMapKpiQueryDTO configMapKpiQueryDTO);

    /**
     * Get all the configMapKpiQueries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMapKpiQueryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" configMapKpiQuery.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMapKpiQueryDTO> findOne(Long id);

    /**
     * Delete the "id" configMapKpiQuery.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
