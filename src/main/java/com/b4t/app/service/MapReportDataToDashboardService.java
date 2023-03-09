package com.b4t.app.service;

import com.b4t.app.service.dto.MapReportDataToDashboardDTO;

import com.b4t.app.service.dto.SyncDataToDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.MapReportDataToDashboard}.
 */
public interface MapReportDataToDashboardService {

    /**
     * Save a mapReportDataToDashboard.
     *
     * @param mapReportDataToDashboardDTO the entity to save.
     * @return the persisted entity.
     */
    MapReportDataToDashboardDTO save(MapReportDataToDashboardDTO mapReportDataToDashboardDTO);

    /**
     * Get all the mapReportDataToDashboards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MapReportDataToDashboardDTO> findAll(Pageable pageable);


    /**
     * Get the "id" mapReportDataToDashboard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MapReportDataToDashboardDTO> findOne(Long id);

    /**
     * Delete the "id" mapReportDataToDashboard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void synDataToDashboard(SyncDataToDashboard syncDataToDashboard);
}
