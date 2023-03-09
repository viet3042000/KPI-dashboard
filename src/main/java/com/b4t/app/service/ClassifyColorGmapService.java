package com.b4t.app.service;

import com.b4t.app.service.dto.ClassifyColorGmapDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ClassifyColorGmap}.
 */
public interface ClassifyColorGmapService {

    /**
     * Save a classifyColorGmap.
     *
     * @param classifyColorGmapDTO the entity to save.
     * @return the persisted entity.
     */
    ClassifyColorGmapDTO save(ClassifyColorGmapDTO classifyColorGmapDTO);

    /**
     * Get all the classifyColorGmaps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassifyColorGmapDTO> findAll(Pageable pageable);

    /**
     * Get the "id" classifyColorGmap.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassifyColorGmapDTO> findOne(Long id);

    /**
     * Delete the "id" classifyColorGmap.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
