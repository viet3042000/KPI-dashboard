package com.b4t.app.service;

import com.b4t.app.service.dto.PostalRptGraphDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.PostalRptGraph}.
 */
public interface PostalRptGraphService {

    /**
     * Save a postalRptGraph.
     *
     * @param postalRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    PostalRptGraphDTO save(PostalRptGraphDTO postalRptGraphDTO);

    /**
     * Get all the postalRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostalRptGraphDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postalRptGraph.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostalRptGraphDTO> findOne(Long id);

    /**
     * Delete the "id" postalRptGraph.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List getDescriptionOfTable();
}
