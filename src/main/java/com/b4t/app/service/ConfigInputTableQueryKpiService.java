package com.b4t.app.service;

import com.b4t.app.domain.ConfigInputTableQueryKpi;
import com.b4t.app.service.dto.ConfigInputTableQueryKpiDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigInputTableQueryKpi}.
 */
public interface ConfigInputTableQueryKpiService {

    /**
     * Save a configInputTableQueryKpi.
     *
     * @param configInputTableQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigInputTableQueryKpiDTO save(ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO);

    /**
     * Get all the configInputTableQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigInputTableQueryKpiDTO> findAll(Pageable pageable);

    /**
     * Get the "id" configInputTableQueryKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigInputTableQueryKpiDTO> findOne(Long id);

    /**
     * Delete the "id" configInputTableQueryKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ConfigInputTableQueryKpiDTO> findAllByQueryKpiId(Integer queryKpiId);
}
