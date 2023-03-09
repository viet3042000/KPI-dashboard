package com.b4t.app.service;

import com.b4t.app.service.dto.CatKpiSynonymDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.CatKpiSynonym}.
 */
public interface CatKpiSynonymService {

    /**
     * Save a catKpiSynonym.
     *
     * @param catKpiSynonymDTO the entity to save.
     * @return the persisted entity.
     */
    CatKpiSynonymDTO save(CatKpiSynonymDTO catKpiSynonymDTO);

    /**
     * Get all the catKpiSynonyms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatKpiSynonymDTO> findAll(Pageable pageable);


    /**
     * Get the "id" catKpiSynonym.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatKpiSynonymDTO> findOne(Long id);

    /**
     * Delete the "id" catKpiSynonym.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CatKpiSynonymDTO> findAllByKpiId(Long kpiId);
}
