package com.b4t.app.service;

import com.b4t.app.service.dto.CatGraphKpiOriginDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.CatGraphKpiOrigin}.
 */
public interface CatGraphKpiOriginService {

    /**
     * Save a catGraphKpiOrigin.
     *
     * @param catGraphKpiOriginDTO the entity to save.
     * @return the persisted entity.
     */
    CatGraphKpiOriginDTO save(CatGraphKpiOriginDTO catGraphKpiOriginDTO);

    /**
     * Get all the catGraphKpiOrigins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatGraphKpiOriginDTO> findAll(Pageable pageable);


    /**
     * Get the "id" catGraphKpiOrigin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatGraphKpiOriginDTO> findOne(Long id);

    /**
     * Delete the "id" catGraphKpiOrigin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<CatGraphKpiOriginDTO> onSearch(CatGraphKpiOriginDTO catGraphKpiOriginDTO, Pageable pageable);
}
