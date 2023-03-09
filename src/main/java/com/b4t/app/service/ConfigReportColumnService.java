package com.b4t.app.service;

import com.b4t.app.domain.ConfigReportColumn;
import com.b4t.app.service.dto.ConfigReportColumnDTO;

import com.b4t.app.service.dto.ConfigReportDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigReportColumn}.
 */
public interface ConfigReportColumnService {

    /**
     * Save list configReportColumn
     * @param configReportColumnDTOs
     * @return
     */
    List<ConfigReportColumnDTO> saveAll(List<ConfigReportColumnDTO> configReportColumnDTOs);
    /**
     * Save a configReportColumn.
     *
     * @param configReportColumnDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigReportColumnDTO save(ConfigReportColumnDTO configReportColumnDTO);

    /**
     * Get all the configReportColumns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigReportColumnDTO> findAll(Pageable pageable);

    /**
     * Get the "id" configReportColumn.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigReportColumnDTO> findOne(Long id);

    /**
     * Delete the "id" configReportColumn.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Xoa danh sach cac cot
     * @param configReportColumns
     */
    void delete(List<ConfigReportColumn> configReportColumns);


    /**
     * Get Tat ca danh sach column cua config_report
     * @param reportId
     * @return
     */
    List<ConfigReportColumn> findAllByReportIdEquals(Long reportId);

    List<ConfigReportColumnDTO> findAllByReportId(Long reportId);

    /**
     * Lay cac cot luu time
     * @param isTimeColumn
     * @return
     */
    List<ConfigReportColumnDTO> findAllByIsTimeColumnEquals(Integer isTimeColumn, Long reportId);

    void compareColumn(ConfigReportDetailDTO configReportDetailDTO, List<ConfigReportColumnDTO> lstColumnDb);
}
