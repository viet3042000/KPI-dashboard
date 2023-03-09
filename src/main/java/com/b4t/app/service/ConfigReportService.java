package com.b4t.app.service;

import com.b4t.app.domain.ConfigReport;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigReport}.
 */
public interface ConfigReportService {


    List<ConfigReportColumnDTO> findColumnOfTableShowEnable(String table);
    List<ConfigReportColumnDTO> findAllColumnOfTable(String table);
    /**
     * Save a configReport.
     *
     * @param configReportDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigReportDTO save(ConfigReportDTO configReportDTO);

    ConfigReportDetailDTO save(ConfigReportDetailDTO configReportDetailDTO, String actor);

    ConfigReportDetailDTO saveUpdate(ConfigReportDetailDTO configReportDetailDTO, String actor) throws BadRequestAlertException;
    void updateApproveStatus(ConfigReportDetailDTO configReportDetailDTO) ;


    /**
     *
     * @param configReport
     * @param pageable
     * @return
     */
    Page<ConfigReportDTO> findAllCondition(ConfigReportForm configReport, Pageable pageable);
    /**
     * Get the "id" configReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigReportDTO> findOne(Long id);

    /**
     * Delete the "id" configReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ConfigReport> findByDatabaseAndTable(String databaseName, String tableName);


    /**
     * Tai file template import du lieu
     * @param reportId
     * @return
     */
    File downloadTemplate(Long reportId);

    ConfigReportImportDetailDTO updateDataApi(ConfigReportImportDetailDTO configReportImportInputDTO) throws Exception;

    File importFile(ConfigReportImportDTO configReportImportDTO) throws Exception;

    /**
     * Tim kiem data cua bang du lieu
     * @param configReportDataForm
     * @param pageable
     * @return
     */
    ConfigReportDataDTO findDataConfigReport(ConfigReportDataForm configReportDataForm, Pageable pageable);

    ConfigReportDataDTO getConfigReport(Long reportId);
    /**
     * Export bao cao chi tiet
     * @param configReportDataForm
     * @return
     */
    File exportDataConfigReports(ConfigReportDataForm configReportDataForm) throws Exception;

    /**
     * Xoa toan bo du lieu cua bang theo 1 thoi gian
     * @param configReportDataForm
     */
    void deleteDataConfigReports(ConfigReportDataForm configReportDataForm);

    /**
     * Xoa 1 row data
     * @param configReportDataForm
     */
    void deleteRowDataConfigReports(ConfigReportImportForm configReportDataForm) throws Exception;

    /**
     * Cap nhat hoac them moi row data
     * @param configReportDataForm
     * @return
     */
    ConfigReportImportForm updateRowDataConfigReports(ConfigReportImportForm configReportDataForm) throws Exception;

    ConfigReportDataDTO findDataConfigReportFormInput(ConfigReportDataForm configReportDataForm, Pageable pageable);

    void deleteDataConfigReportsFormInput(ConfigReportDataForm configReportDataForm);

    List<ConfigReportDTO> findMainData(ConfigReportForm configReport);
}
