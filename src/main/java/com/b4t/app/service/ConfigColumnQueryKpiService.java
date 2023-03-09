package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigColumnQueryKpiDTO;

import com.b4t.app.service.dto.ConfigQueryKpiColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigColumnQueryKpi}.
 */
public interface ConfigColumnQueryKpiService {

    /**
     * Save a configColumnQueryKpi.
     *
     * @param configColumnQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigColumnQueryKpiDTO save(ConfigColumnQueryKpiDTO configColumnQueryKpiDTO);

    /**
     * Get all the configColumnQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigColumnQueryKpiDTO> findAll(Pageable pageable);


    /**
     * Get the "id" configColumnQueryKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigColumnQueryKpiDTO> findOne(Long id);

    /**
     * Delete the "id" configColumnQueryKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

}
