package com.b4t.app.service;

import com.b4t.app.service.dto.SecurRptGraphDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.SecurRptGraph}.
 */
public interface SecurRptGraphService {

    /**
     * Save a securRptGraph.
     *
     * @param securRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    SecurRptGraphDTO save(SecurRptGraphDTO securRptGraphDTO);

    /**
     * Get all the securRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SecurRptGraphDTO> findAll(Pageable pageable);

    /**
     * Get the "id" securRptGraph.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SecurRptGraphDTO> findOne(Long id);

    /**
     * Delete the "id" securRptGraph.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
