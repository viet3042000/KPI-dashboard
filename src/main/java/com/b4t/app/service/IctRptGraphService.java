package com.b4t.app.service;

import com.b4t.app.service.dto.IctRptGraphDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.IctRptGraph}.
 */
public interface IctRptGraphService {

    /**
     * Save a ictRptGraph.
     *
     * @param ictRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    IctRptGraphDTO save(IctRptGraphDTO ictRptGraphDTO);

    /**
     * Get all the ictRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IctRptGraphDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ictRptGraph.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IctRptGraphDTO> findOne(Long id);

    /**
     * Delete the "id" ictRptGraph.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
