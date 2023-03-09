package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.impl.ConfigReportUtilsServiceImpl;
import com.b4t.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigReport}.
 */
@RestController
@RequestMapping("/api")
public class ConfigReportResource {

    private final Logger log = LoggerFactory.getLogger(ConfigReportResource.class);

    private static final String ENTITY_NAME = "configReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigReportService configReportService;

    private final ConfigReportColumnService configReportColumnService;
    private final CommonService commonService;
    private final ReportAlertDetailService reportAlertDetailService;


    @Autowired
    CatItemService catItemService;

    @Autowired
    ConfigReportUtilsServiceImpl configReportUtilsServiceImpl;

    public ConfigReportResource(ConfigReportService configReportService, ConfigReportColumnService configReportColumnService, CommonService commonService, ReportAlertDetailService reportAlertDetailService) {
        this.configReportService = configReportService;
        this.configReportColumnService = configReportColumnService;
        this.commonService = commonService;
        this.reportAlertDetailService = reportAlertDetailService;
    }

    /**
     * {@code POST  /config-reports} : Create a new configReport.
     *
     * @param configReportDetailDTO the configReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configReportDTO, or with status {@code 400 (Bad Request)} if the configReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-reports")
    @PreAuthorize("hasPermission('USER', 'INSERT')")
    public ResponseEntity<ConfigReportDetailDTO> addConfigReport(@Valid @RequestBody ConfigReportDetailDTO configReportDetailDTO,
                                                                 HttpServletRequest request) throws URISyntaxException {
        String username = (String) request.getAttribute(Constants.ACTOR);
        log.debug("REST request to save ConfigReport : {}", configReportDetailDTO);
        validateColumn(configReportDetailDTO);

        if (!commonService.checkTableExist(configReportDetailDTO.getConfigReport().getDatabaseName(), configReportDetailDTO.getConfigReport().getTableName())) {
            commonService.createTable(configReportDetailDTO);
            catItemService.addSourceTable(configReportDetailDTO.getConfigReport().getDatabaseName(), configReportDetailDTO.getConfigReport().getTableName());
            commonService.saveTriggerConfigReport(configReportDetailDTO);
        }
        Optional<ConfigReport> configReport = configReportService.findByDatabaseAndTable(configReportDetailDTO.getConfigReport().getDatabaseName(),
            configReportDetailDTO.getConfigReport().getTableName());
        if (configReport.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.tableNameExists"), ENTITY_NAME, "configReport.tableNameExists");
        }
        if (DataUtil.isNullOrEmpty(configReportDetailDTO.getConfigReportColumns())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.configReportColumnNull"), ENTITY_NAME, "configReport.configReportColumnNull");
        }
        ConfigReportDetailDTO result = this.configReportService.save(configReportDetailDTO, username);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getConfigReport().getId().toString()))
            .body(result);
    }
    @PostMapping("/config-reports/report_alert_detail")
    public ResponseEntity<List<ReportAlertDetailDTO>> addReportAlertDetail(@Valid @RequestBody List<ReportAlertDetailDTO> reportAlertDetailDTO,
                                                                 HttpServletRequest request) throws URISyntaxException {

        List<ReportAlertDetailDTO> result = this.reportAlertDetailService.saveAll(reportAlertDetailDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(result.size())))
            .body(result);
    }

    @GetMapping("/config-reports/report_alert_detail/{id}")
    public ResponseEntity<List<ReportAlertDetailDTO>> getReportAlertDetail(@Valid @PathVariable int id,
                                                             HttpServletRequest request) throws URISyntaxException {

        List<ReportAlertDetailDTO> result = this.reportAlertDetailService.findAllCondition(id);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(result.size())))
            .body(result);
    }
    @DeleteMapping("/config-reports/report_alert_detail/{id}")
    public ResponseEntity<Void> deleteReportAlertDetail(@Valid @PathVariable int id,
                                                             HttpServletRequest request) throws URISyntaxException {

         this.reportAlertDetailService.deleteByTableId(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, String.valueOf(id))).build();
    }
    /**
     * {@code PUT  /config-reports} : Updates an existing configReport.
     *
     * @param configReportDetailDTO the configReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configReportDTO,
     * or with status {@code 400 (Bad Request)} if the configReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-reports")
    public ResponseEntity<ConfigReportDetailDTO> updateConfigReport(@Valid @RequestBody ConfigReportDetailDTO configReportDetailDTO,
                                                                    HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update ConfigReport : {}", configReportDetailDTO);
        validateColumn(configReportDetailDTO);

        String username = (String) request.getAttribute(Constants.ACTOR);
        if (configReportDetailDTO.getConfigReport().getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNull"), ENTITY_NAME, "configReport.idNull");
        }
        if (!configReportService.findOne(configReportDetailDTO.getConfigReport().getId()).isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNotExisted"), ENTITY_NAME, "configReport.idNotExisted");
        }
        Optional<ConfigReport> configReport = configReportService.findByDatabaseAndTable(configReportDetailDTO.getConfigReport().getDatabaseName(),
            configReportDetailDTO.getConfigReport().getTableName());
        if (configReport.isPresent() && !configReport.get().getId().equals(configReportDetailDTO.getConfigReport().getId())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.tableNameExists"), ENTITY_NAME, "configReport.tableNameExists");
        }
        if (DataUtil.isNullOrEmpty(configReportDetailDTO.getConfigReportColumns())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.configReportColumnNull"), ENTITY_NAME, "configReport.configReportColumnNull");
        }
        List<ConfigReportColumnDTO> lstColumn = configReportColumnService.findAllByReportId(configReportDetailDTO.getConfigReport().getId());
        configReportColumnService.compareColumn(configReportDetailDTO, lstColumn);

//        configReportService.updateApproveStatus(configReportDetailDTO);
        ConfigReportDetailDTO result = configReportService.saveUpdate(configReportDetailDTO, username);
        commonService.saveTriggerConfigReport(configReportDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configReportDetailDTO.getConfigReport().getId().toString()))
            .body(result);
    }


    @GetMapping("/config-reports/find-column-table/{table}")
    public ResponseEntity<List<ConfigReportColumnDTO>> findColumnOfTable(@PathVariable String table, String schema, String tableName, Long timeType) throws URISyntaxException {
        log.debug("REST request to get a page of ConfigReports");
        List<ConfigReportColumnDTO> lstColumn;
        if(timeType == null) {
            throw new BadRequestAlertException(Translator.toLocale("configReport.timeType.null", new Object[]{tableName}), ENTITY_NAME, "configReport.timeType.null");
        }
        if(!validateSchemaTableName(table)) {
            throw new BadRequestAlertException(Translator.toLocale("configReport.tableName.mustbeNormalCharacter", new Object[]{tableName}), ENTITY_NAME, "configReport.tableName.mustbeNormalCharacter");
        }
        if (commonService.checkTableExist(schema, tableName)) {
            lstColumn = configReportService.findColumnOfTableShowEnable(table);
        } else {
            lstColumn = commonService.generateTable(timeType);
        }

        return ResponseEntity.created(new URI("/config-reports/find-column-table/" + table))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, String.valueOf(lstColumn)))
            .body(lstColumn);
    }

    @PostMapping("/config-reports/query")
    @PreAuthorize("hasPermission('USER', 'GET')")
    public ResponseEntity<List<ConfigReportDTO>> getAllConfigReportsByForm(@RequestBody ConfigReportForm configReport, Pageable pageable, HttpServletRequest request) {
        log.debug("REST request to get a page of ConfigReports");
        if (pageable == null) {
            pageable = PageRequest.of(0, 500);
        }
        configReport = configReportUtilsServiceImpl.updateCondition(configReport);

        Page<ConfigReportDTO> page;

        if (!DataUtil.isNullOrEmpty(configReport.getLstDomainCode())) {
            page = configReportService.findAllCondition(configReport, pageable);
        } else {
            page = new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/config-reports/main-data")
    public ResponseEntity<List<ConfigReportDTO>> getConfigReportsByMainData(@RequestBody ConfigReportForm configReport, HttpServletRequest request) {
        log.debug("REST request to get a page of ConfigReports");

        configReport = configReportUtilsServiceImpl.updateCondition(configReport);

        List<ConfigReportDTO> page;

        if (!DataUtil.isNullOrEmpty(configReport.getLstDomainCode())) {
            page = configReportService.findMainData(configReport);
        } else {
            page = new ArrayList<>();
        }
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /config-reports/:id} : get the "id" configReport.
     *
     * @param id the id of the configReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-reports/{id}")
    public ResponseEntity<ConfigReportDetailDTO> getConfigReport(@PathVariable Long id) {
        log.debug("REST request to get ConfigReport : {}", id);
        Optional<ConfigReportDetailDTO> result = Optional.empty();
        Optional<ConfigReportDTO> configReportDTO = configReportService.findOne(id);
        if (configReportDTO.isPresent()) {
            List<ConfigReportColumnDTO> configReportColumnDTOS = configReportColumnService.findAllByReportId(id)
                .stream().filter(e -> !Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey())).collect(Collectors.toList());
            ConfigReportDetailDTO configReportDetailDTO = new ConfigReportDetailDTO();
            configReportDetailDTO.setConfigReport(configReportDTO.get());
            configReportDetailDTO.setConfigReportColumns(configReportColumnDTOS);
            result = Optional.of(configReportDetailDTO);
        }
        return ResponseUtil.wrapOrNotFound(result);
    }

    @GetMapping("/config-reports-detail/{id}")
    public ResponseEntity<ConfigReportDetailDTO> getConfigReportDetail(@PathVariable Long id) {
        log.debug("REST request to get ConfigReport : {}", id);
        Optional<ConfigReportDetailDTO> result = configReportUtilsServiceImpl.getReportDetail(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * {@code DELETE  /config-reports/:id} : delete the "id" configReport.
     *
     * @param id the id of the configReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-reports/{id}")
    @PreAuthorize("hasPermission('USER', 'DELETE')")
    public ResponseEntity<Void> deleteConfigReport(@PathVariable Long id) {
        log.debug("REST request to delete ConfigReport : {}", id);
        configReportService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/config-reports/download-template/{id}")
    public ResponseEntity<Resource> downloadTemplate(@PathVariable Long id) throws Exception {

        File fileOut = configReportService.downloadTemplate(id);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));
        return ResponseEntity.ok()
            .contentLength(fileOut.length())
            .header("filename", fileOut.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
    }

    @PostMapping(value = "/config-reports/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasPermission('USER', 'IMPORT')")
    public ResponseEntity<Resource> importFile(@Valid @ModelAttribute ConfigReportImportDTO configReportImportDTO) throws Exception {
        if (configReportImportDTO.getImportFile() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.fileImportNull"), "param", "error.configReport.fileImportNull");
        }
        String fileName = configReportImportDTO.getImportFile().getOriginalFilename().toLowerCase();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.templateInvalid"), "param", "error.configReport.templateInvalid");
        }

        File fileOut = configReportService.importFile(configReportImportDTO);
        System.out.println("import success");
        if (fileOut != null) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));

            return ResponseEntity.ok()
                .contentLength(fileOut.length())
                .header("filename", fileOut.getName())
                .header("result", configReportImportDTO.getResult())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
        } else {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.importSomeError"), ENTITY_NAME, "configReport.importSomeError");
        }
    }

    @PostMapping(value = "/config-reports/updateDataApi")
    public ResponseEntity<ConfigReportImportDetailDTO> updateDataApi(@Valid @RequestBody ConfigReportImportDetailDTO configReportImportInputDTO) throws Exception {
        ConfigReportImportDetailDTO configReportDTO = configReportService.updateDataApi(configReportImportInputDTO);
        return ResponseUtil.wrapOrNotFound(Optional.of(configReportDTO));
    }

    @GetMapping("/config-reports-full/{id}")
    public ResponseEntity<ConfigReportDataDTO> getConfigReportFull(@PathVariable Long id) {
        log.debug("REST request to getConfigReportFull");
        ConfigReportDataDTO configReportDataDTO = configReportService.getConfigReport(id);
        return ResponseEntity.ok().body(configReportDataDTO);
    }

    @PostMapping("/config-reports/data")
    public ResponseEntity<ConfigReportDataDTO> getDataConfigReports(@RequestBody ConfigReportDataForm configReportDataForm, Pageable pageable) {
        log.debug("REST request to get a page of ConfigReports");
        long reportId = configReportDataForm.getReportId();
        ConfigReportDTO configReportDTO = configReportUtilsServiceImpl.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTO.getformInput();

        ConfigReportDataDTO configReportDataDTO = configReportService.findDataConfigReport(configReportDataForm, pageable);
        Page<Object> page = configReportDataDTO.getPageObj();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(configReportDataDTO);
    }

    @PostMapping("/config-reports/export-data")
    public ResponseEntity<Resource> exportDataConfigReports(@RequestBody ConfigReportDataForm configReportDataForm) throws Exception {

        File fileOut = configReportService.exportDataConfigReports(configReportDataForm);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));
        return ResponseEntity.ok()
            .contentLength(fileOut.length())
            .header("filename", fileOut.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
    }

    @PostMapping("/config-reports/data/delete")
    public ResponseEntity<Void> deleteDataConfigReports(@RequestBody ConfigReportDataForm configReportDataForm) {
        log.debug("REST request to delete deleteDataConfigReports : {}", configReportDataForm);
        if (DataUtil.isNullOrZero(configReportDataForm.getReportId())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNull"), ENTITY_NAME, "configReport.idNull");
        }
        if (DataUtil.isNullObject(configReportDataForm.getDataTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.dataTimeNull"), ENTITY_NAME, "configReport.dataTimeNull");
        }
        long reportId = configReportDataForm.getReportId();
        ConfigReportDTO configReportDTO = configReportUtilsServiceImpl.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTO.getformInput();
        if(Constants.formInputYear.equals(formInput)){
            configReportService.deleteDataConfigReportsFormInput(configReportDataForm);
        }else{
            configReportService.deleteDataConfigReports(configReportDataForm);
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, configReportDataForm.getReportId().toString())).build();
    }

    @PostMapping("/config-reports/data/delete-row")
    public ResponseEntity<Void> deleteRowDataConfigReports(@RequestBody ConfigReportImportForm configReportImportForm) throws Exception {
        log.debug("REST request to delete deleteRowDataConfigReports : {}", configReportImportForm);
        if (DataUtil.isNullOrZero(configReportImportForm.getReportId())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNull"), ENTITY_NAME, "configReport.idNull");
        }

        if (DataUtil.isNullOrEmpty(configReportImportForm.getMapValue())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.mapDataNull"), ENTITY_NAME, "configReport.mapDataNull");
        }
        configReportService.deleteRowDataConfigReports(configReportImportForm);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, configReportImportForm.getReportId().toString())).build();
    }

    @PostMapping("/config-reports/data/update-row")
    public ResponseEntity<ConfigReportImportForm> updateRowDataConfigReports(@RequestBody ConfigReportImportForm configReportUpdate) throws Exception {
        log.debug("REST request to delete updateRowDataConfigReports : {}", configReportUpdate);
        if (DataUtil.isNullOrZero(configReportUpdate.getReportId())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNull"), ENTITY_NAME, "configReport.idNull");
        }

        if (DataUtil.isNullOrEmpty(configReportUpdate.getMapValue())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.mapDataNull"), ENTITY_NAME, "configReport.mapDataNull");
        }
        ConfigReportImportForm result = configReportService.updateRowDataConfigReports(configReportUpdate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, result.getReportId().toString()))
            .body(result);
    }

    private boolean validateColumn(ConfigReportDetailDTO configReportDetailDTO) {
        Pattern pattern = Pattern.compile("[a-z0-9_]*");

        if (configReportDetailDTO.getConfigReportColumns() == null) {
            throw new BadRequestAlertException(Translator.toLocale("configReport.column.notNull"), ENTITY_NAME, "configReport.column.notNull");
        }
        if (!pattern.matcher(configReportDetailDTO.getConfigReport().getTableName()).matches()) {
            throw new BadRequestAlertException(Translator.toLocale("configReport.tableName.wrongPatternCharacter", new Object[]{configReportDetailDTO.getConfigReport().getTableName()}), ENTITY_NAME, "configReport.tableName.wrongPatternCharacter");
        }

        Map<String, Long> counted = configReportDetailDTO.getConfigReportColumns().stream().collect(Collectors.groupingBy(ConfigReportColumnDTO::getColumnName, Collectors.counting()));
        for(Map.Entry<String, Long> entry: counted.entrySet()) {
            if(!entry.getValue().equals(1L)) {
                throw new BadRequestAlertException(Translator.toLocale("configReport.column.mustBeUnique", new Object[]{entry.getKey()}), ENTITY_NAME, "configReport.column.mustbeNormalCharacter");
            }
        }
        for (ConfigReportColumnDTO columnDTO : configReportDetailDTO.getConfigReportColumns()) {
            if (!pattern.matcher(columnDTO.getColumnName()).matches()) {
                throw new BadRequestAlertException(Translator.toLocale("configReport.column.mustbeNormalCharacter", new Object[]{columnDTO.getColumnName()}), ENTITY_NAME, "configReport.column.mustbeNormalCharacter");
            }
        }
        return true;
    }
    private boolean validateSchemaTableName(String tableName) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_.-]*");

        if (!pattern.matcher(tableName).matches()) {
            return false;
        }

        return true;
    }

}
