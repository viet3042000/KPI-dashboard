package com.b4t.app.service;

import com.b4t.app.service.dto.ChartDescriptionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ChartDescription}.
 */
public interface ChartDescriptionService {
    /**
     * Create new ChartDescription
     * @param chartDescriptionDTO
     * @return
     */
    ChartDescriptionDTO createNew(ChartDescriptionDTO chartDescriptionDTO);

    /**
     * Save a chartDescription.
     *
     * @param chartDescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    ChartDescriptionDTO save(ChartDescriptionDTO chartDescriptionDTO);

    /**
     * Get all the chartDescriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChartDescriptionDTO> findAll(Pageable pageable);

    /**
     *
     * @param chartId
     * @param pageable
     * @return
     */
    Page<ChartDescriptionDTO> findAllByChartId(Long chartId, Pageable pageable);


    /**
     * Get the "id" chartDescription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChartDescriptionDTO> findOne(Long id);

    /**
     * Get the unique chartDescription.
     * @param chartId
     * @param prdId
     * @param timeType
     * @return
     */
    ChartDescriptionDTO findByChartIdAndPrdIdAndTimeType(Long chartId, Integer prdId, Integer timeType);

    /**
     * Delete the "id" chartDescription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
