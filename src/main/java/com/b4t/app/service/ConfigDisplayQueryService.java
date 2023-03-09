package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigDisplayQueryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigDisplayQuery}.
 */
public interface ConfigDisplayQueryService {

    /**
     * Save a configDisplayQuery.
     *
     * @param configDisplayQueryDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigDisplayQueryDTO save(ConfigDisplayQueryDTO configDisplayQueryDTO);

    List<ConfigDisplayQueryDTO> saveAll(List<ConfigDisplayQueryDTO> dtos);
    /**
     * Get all the configDisplayQueries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigDisplayQueryDTO> findAll(Pageable pageable);

    List<ConfigDisplayQueryDTO> findByChartItemIds(List<Long> chartItemIds);

    /**
     * Get the "id" configDisplayQuery.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigDisplayQueryDTO> findOne(Long id);

    /**
     * Delete the "id" configDisplayQuery.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void delete(List<ConfigDisplayQueryDTO> dtos);
}
