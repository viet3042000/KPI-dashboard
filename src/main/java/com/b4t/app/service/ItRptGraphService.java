package com.b4t.app.service;

import com.b4t.app.service.dto.ItRptGraphDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ItRptGraph}.
 */
public interface ItRptGraphService {

    /**
     * Save a itRptGraph.
     *
     * @param itRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    ItRptGraphDTO save(ItRptGraphDTO itRptGraphDTO);

    /**
     * Get all the itRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItRptGraphDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itRptGraph.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItRptGraphDTO> findOne(Long id);

    /**
     * Delete the "id" itRptGraph.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
