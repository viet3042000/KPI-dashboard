package com.b4t.app.service;

import com.b4t.app.service.dto.TelcomRptGraphDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.TelcomRptGraph}.
 */
public interface TelcomRptGraphService {

    /**
     * Save a telcomRptGraph.
     *
     * @param telcomRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    TelcomRptGraphDTO save(TelcomRptGraphDTO telcomRptGraphDTO);

    /**
     * Get all the telcomRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TelcomRptGraphDTO> findAll(Pageable pageable);

    /**
     * Get the "id" telcomRptGraph.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TelcomRptGraphDTO> findOne(Long id);

    /**
     * Delete the "id" telcomRptGraph.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
