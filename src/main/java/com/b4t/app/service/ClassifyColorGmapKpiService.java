package com.b4t.app.service;

import com.b4t.app.service.dto.ClassifyColorGmapKpiDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ClassifyColorGmapKpi}.
 */
public interface ClassifyColorGmapKpiService {

    /**
     * Save a classifyColorGmapKpi.
     *
     * @param classifyColorGmapKpiDTO the entity to save.
     * @return the persisted entity.
     */
    ClassifyColorGmapKpiDTO save(ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO);

    /**
     * Get all the classifyColorGmapKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassifyColorGmapKpiDTO> findAll(Pageable pageable);

    /**
     * Get the "id" classifyColorGmapKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassifyColorGmapKpiDTO> findOne(Long id);

    /**
     * Delete the "id" classifyColorGmapKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
