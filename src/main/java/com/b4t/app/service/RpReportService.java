package com.b4t.app.service;

import com.b4t.app.service.dto.RpReportDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.RpReport}.
 */
public interface RpReportService {

    /**
     * Save a rpReport.
     *
     * @param rpReportDTO the entity to save.
     * @return the persisted entity.
     */
    RpReportDTO save(RpReportDTO rpReportDTO);

    /**
     * Get all the rpReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RpReportDTO> findAll(Pageable pageable);

    /**
     * Get all Report
     * @return
     */
    List<RpReportDTO> findAllReport();


    /**
     * Get the "id" rpReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RpReportDTO> findOne(Long id);

    /**
     * Delete the "id" rpReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
