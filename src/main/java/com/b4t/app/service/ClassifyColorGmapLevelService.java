package com.b4t.app.service;

import com.b4t.app.service.dto.ClassifyColorGmapLevelDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ClassifyColorGmapLevel}.
 */
public interface ClassifyColorGmapLevelService {

    /**
     * Save a classifyColorGmapLevel.
     *
     * @param classifyColorGmapLevelDTO the entity to save.
     * @return the persisted entity.
     */
    ClassifyColorGmapLevelDTO save(ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO);

    /**
     * Get all the classifyColorGmapLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassifyColorGmapLevelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" classifyColorGmapLevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassifyColorGmapLevelDTO> findOne(Long id);

    /**
     * Delete the "id" classifyColorGmapLevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
