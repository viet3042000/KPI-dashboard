package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.ExcelUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatGraphKpi;
import com.b4t.app.domain.RptGraph;
import com.b4t.app.service.CatGraphKpiService;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.CatGraphKpi}.
 */
@RestController
@RequestMapping("/api")
public class CatGraphKpiResource {

    public static final String REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS = "REST request to get a page of CatGraphKpis";
    private final Logger log = LoggerFactory.getLogger(CatGraphKpiResource.class);

    private static final String ENTITY_NAME = "catGraphKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatGraphKpiService catGraphKpiService;


    @Autowired
    ExcelUtils excelUtils;

    public CatGraphKpiResource(CatGraphKpiService catGraphKpiService) {
        this.catGraphKpiService = catGraphKpiService;
    }

    /**
     * {@code POST  /cat-graph-kpis} : Create a new catGraphKpi.
     *
     * @param catGraphKpiDTO the catGraphKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catGraphKpiDTO, or with status {@code 400 (Bad Request)} if the catGraphKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cat-graph-kpis")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'INSERT')")
    public ResponseEntity<CatGraphKpiDTO> createCatGraphKpi(@RequestBody CatGraphKpiDTO catGraphKpiDTO) throws URISyntaxException {
        log.debug("REST request to save CatGraphKpi : {}", catGraphKpiDTO);
        if (catGraphKpiDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        if (isDuplicateKpiId(catGraphKpiDTO)) {
            throw new BadRequestAlertException(Translator.toLocale("catGraphKpi.idexists.kpiId") + ":" + catGraphKpiDTO.getKpiId().toString(), ENTITY_NAME, "error.idexists");
        }
        catGraphKpiDTO.setKpiId(null);
        CatGraphKpiDTO result = catGraphKpiService.save(catGraphKpiDTO);
        catGraphKpiService.setKpiIdForNewKpi(result.getId());
        return ResponseEntity.created(new URI("/api/cat-graph-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /cat-graph-kpis-clone} : Create a new catGraphKpi.
     *
     * @param catGraphKpiDTO the catGraphKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catGraphKpiDTO, or with status {@code 400 (Bad Request)} if the catGraphKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cat-graph-kpis-clone")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'INSERT')")
    public ResponseEntity<CatGraphKpiDTO> copyCatGraphKpi(@RequestBody CatGraphKpiDTO catGraphKpiDTO) throws URISyntaxException {
        log.debug("REST request to save CatGraphKpi : {}", catGraphKpiDTO);
        catGraphKpiDTO.setKpiId(catGraphKpiService.getMaxKpiId());
        if (catGraphKpiDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        if (isDuplicateKpiId(catGraphKpiDTO)) {
            throw new BadRequestAlertException(Translator.toLocale("catGraphKpi.idexists.kpiId") + ":" + catGraphKpiDTO.getKpiId().toString(), ENTITY_NAME, "error.idexists");
        }
        CatGraphKpiDTO result = catGraphKpiService.save(catGraphKpiDTO);
        return ResponseEntity.created(new URI("/api/cat-graph-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private boolean isDuplicateKpiId(CatGraphKpiDTO catGraphKpiDTO) {
        Optional<CatGraphKpiDTO> opt = catGraphKpiService.findByKpiId(catGraphKpiDTO.getKpiId());
        if (opt.isPresent() && !opt.get().getId().equals(catGraphKpiDTO.getId())) {
            return true;
        }
        return false;
    }

    /**
     * {@code PUT  /cat-graph-kpis} : Updates an existing catGraphKpi.
     *
     * @param catGraphKpiDTO the catGraphKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catGraphKpiDTO,
     * or with status {@code 400 (Bad Request)} if the catGraphKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catGraphKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cat-graph-kpis")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'UPDATE')")
    public ResponseEntity<CatGraphKpiDTO> updateCatGraphKpi(@RequestBody CatGraphKpiDTO catGraphKpiDTO) throws URISyntaxException {
        log.debug("REST request to update CatGraphKpi : {}", catGraphKpiDTO);
        if (catGraphKpiDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        if (isDuplicateKpiId(catGraphKpiDTO)) {
            throw new BadRequestAlertException(Translator.toLocale("catGraphKpi.idexists.kpiId") + ":" + catGraphKpiDTO.getKpiId().toString(), ENTITY_NAME, "error.idexists");
        }
        CatGraphKpiDTO result = catGraphKpiService.save(catGraphKpiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catGraphKpiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cat-graph-kpis} : get all the catGraphKpis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catGraphKpis in body.
     */
    @GetMapping("/cat-graph-kpis")
    public ResponseEntity<List<CatGraphKpiDTO>> getAllCatGraphKpis(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType, Integer showOnMap, Long status, Pageable pageable) {
        log.debug(REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS);
        Page<CatGraphKpiDTO> page = catGraphKpiService.findAll(keyword, kpiId, domainCode, groupKpiCode, kpiType, showOnMap, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/cat-graph-kpis-all")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', '*')")
    public ResponseEntity<List<CatGraphKpiDTO>> findAllCatGraphKpi() {
        log.debug(REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS);
        List<CatGraphKpiDTO> lstCatKpi = catGraphKpiService.getAllCatGraphKpi();
        return ResponseEntity.ok().body(lstCatKpi);
    }

    @PostMapping("/cat-graph-kpis-search")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', '*')")
    public ResponseEntity<List<CatGraphKpiDetailDTO>> onSearch(@RequestBody CatGraphKpiDetailDTO catGraphKpiDetailDTO, Pageable pageable) {
        log.debug(REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS);
        Page<CatGraphKpiDetailDTO> page = catGraphKpiService.onSearch(catGraphKpiDetailDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/cat-graph-kpis-export")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', '*')")
    public ResponseEntity<Resource> onExport(@RequestBody CatGraphKpiDetailDTO catGraphKpiDetailDTO) throws Exception {
        List<CatGraphKpiDetailDTO> lstDatas = catGraphKpiService.onExport(catGraphKpiDetailDTO);
        List<ExcelColumn> lstColumn = buildColumn();
        String title = Translator.toLocale("catGraphKpi.export.title");
        File fileOut = excelUtils.onExport(lstColumn, lstDatas, 3, 0, title, Translator.toLocale("catGraphKpi.export.fileName"));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));
        return ResponseEntity.ok()
            .contentLength(fileOut.length())
            .header("filename", fileOut.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
    }

    @GetMapping("/cat-graph-kpis/list-kpi-maps")
    public ResponseEntity<List<CatGraphKpiDTO>> getListKpiForMaps(CatGrapKpiExtendDTO catGrapKpiExtendDTO) {
        List<CatGraphKpiDTO> list = catGraphKpiService.getListKpiForMaps(catGrapKpiExtendDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private void updateKpiInfo(List<CatGraphKpiDTO> lstDatas) {
        if (DataUtil.isNullOrEmpty(lstDatas)) return;
        lstDatas.forEach(bean -> {
            bean.setKpiTypeName(Constants.KPI_TYPE.ORIGINAL.equals(bean.getKpiType()) ? Translator.toLocale("catGraphKpi.label.original") : Translator.toLocale("catGraphKpi.label.synthetic"));
            bean.setAlarmThresholdTypeName(Constants.THRESHOLDTYPE.POSITIVE.equals(bean.getAlarmThresholdType()) ? Translator.toLocale("catGraphKpi.column.positive") : Translator.toLocale("catGraphKpi.column.negative"));
        });
    }

    /**
     * {@code GET  /cat-graph-kpis} : get all the catGraphKpis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catGraphKpis in body.
     */
    @PostMapping("/cat-graph-kpis/export")
    public ResponseEntity<Resource> getAllCatGraphKpisNoPage(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType, Long status) throws Exception {
        List<CatGraphKpiDTO> lstDatas = catGraphKpiService.findAll(keyword, kpiId, domainCode, groupKpiCode, kpiType, status);
        updateKpiInfo(lstDatas);
        List<ExcelColumn> lstColumn = buildColumn();
        String title = Translator.toLocale("catGraphKpi.export.title");
        File fileOut = excelUtils.onExport(lstColumn, lstDatas, 3, 0, title, Translator.toLocale("catGraphKpi.export.fileName"));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));
        return ResponseEntity.ok()
            .contentLength(fileOut.length())
            .header("filename", fileOut.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
    }
    private List<ExcelColumn> buildColumn() {
        List<ExcelColumn> lstColumn = new ArrayList<>();
        lstColumn.add(new ExcelColumn("kpiCode", Translator.toLocale("catGraphKpi.column.kpiCode"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("kpiId", Translator.toLocale("catGraphKpi.column.kpiIdTxt"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("kpiDisplay", Translator.toLocale("catGraphKpi.column.kpiDisplay"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("unitName", Translator.toLocale("catGraphKpi.column.unitKpi"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("unitViewName", Translator.toLocale("catGraphKpi.column.unitViewName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("rate", Translator.toLocale("catGraphKpi.column.rate"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("groupKpiName", Translator.toLocale("catGraphKpi.column.groupKpiName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("domainName", Translator.toLocale("catGraphKpi.column.domainName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("kpiTypeName", Translator.toLocale("catGraphKpi.column.kpiType"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("formulaLevel", Translator.toLocale("catGraphKpi.column.formulaLevel"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("formulaQuar", Translator.toLocale("catGraphKpi.column.formulaQuar"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("formulaYear", Translator.toLocale("catGraphKpi.column.formulaYear"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("alarmThresholdTypeName", Translator.toLocale("catGraphKpi.column.alarmThresholdType"), ExcelColumn.ALIGN_MENT.LEFT));
//        lstColumn.add(new ExcelColumn("formulaYear", Translator.toLocale("catGraphKpi.column.formulaYear"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("alarmPlanTypeName", Translator.toLocale("catGraphKpi.column.alarmPlanType"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("formulaAcc", Translator.toLocale("catGraphKpi.column.formulaAcc"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("synonyms", Translator.toLocale("catGraphKpi.column.synonyms"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("source", Translator.toLocale("catGraphKpi.column.source"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("updateTime", Translator.toLocale("catGraphKpi.column.updateTime"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("updateUser", Translator.toLocale("catGraphKpi.column.updateUser"), ExcelColumn.ALIGN_MENT.LEFT));
        return lstColumn;
    }

    /**
     * {@code GET  /cat-graph-kpis} : get all the catGraphKpis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catGraphKpis in body.
     */
    @GetMapping("/cat-graph-kpis-main")
    public ResponseEntity<List<CatGraphKpiDTO>> getCatGraphKpisMain(String groupKpiCode, String domainCode) {
        log.debug(REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS);
        List<CatGraphKpiDTO> page = catGraphKpiService.getKpiMain(groupKpiCode, domainCode);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/cat-graph-kpis-domain-code/{domainCode}")
    public ResponseEntity<List<CatGraphKpiDTO>> getGraphKpiByDomain(@PathVariable String domainCode) {
        log.debug("REST request to get a page of getGraphKpiByDomain");
        List<CatGraphKpiDTO> lstCatGraph = catGraphKpiService.getGraphKpiByDomain(domainCode);
        return new ResponseEntity<>(lstCatGraph, HttpStatus.OK);
    }

    @GetMapping("/cat-graph-kpis/get-by-keyword")
    public ResponseEntity<List<CatGraphKpiDTO>> getAllByKeyword(String keyword, Pageable pageable) {
        log.debug(REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS);
        Page<CatGraphKpiDTO> page = catGraphKpiService.findAll(keyword, null, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cat-graph-kpis/:id} : get the "id" catGraphKpi.
     *
     * @param id the id of the catGraphKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catGraphKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cat-graph-kpis/{id}")
    public ResponseEntity<CatGraphKpiDTO> getCatGraphKpi(@PathVariable Long id) {
        log.debug("REST request to get CatGraphKpi : {}", id);
        Optional<CatGraphKpiDTO> catGraphKpiDTO = catGraphKpiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catGraphKpiDTO);
    }

    /**
     * {@code DELETE  /cat-graph-kpis/:id} : delete the "id" catGraphKpi.
     *
     * @param id the id of the catGraphKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cat-graph-kpis/{id}")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'DELETE')")
    public ResponseEntity<Void> deleteCatGraphKpi(@PathVariable Long id) {
        log.debug("REST request to delete CatGraphKpi : {}", id);
        Optional<CatGraphKpiDTO> ckOpt = catGraphKpiService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        catGraphKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/cat-graph-kpis/delete-multiple")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'DELETE')")
    @Transactional(rollbackFor = BadRequestAlertException.class)
    public ResponseEntity<Void> deleteCatGraphKpiMultiple(@RequestBody List<CatGraphKpi> catGraphKpis) {
        for(CatGraphKpi cat : catGraphKpis){
            Optional<CatGraphKpiDTO> ckOpt = catGraphKpiService.findOne(cat.getId());
            if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(cat.getStatus())) {
                throw new BadRequestAlertException(Translator.toLocale("error.disabled.item"), ENTITY_NAME, "error.disabled.item");
            }
            catGraphKpiService.delete(cat.getId());
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build();
    }

    @GetMapping("/cat-graph-kpis-hashtag")
    public ResponseEntity<List<ComboDTO>> getAllConfigCharts(String keyword) throws Exception {
        List<ComboDTO> result = catGraphKpiService.onSearchHashTag(keyword);
        return ResponseEntity.ok().body(result);
    }
    @GetMapping("/cat-graph-kpis/get-max-kpi-id")
    public ResponseEntity<Long> getMaxKpiId() {
        Long maxKpiId = catGraphKpiService.getMaxKpiId();
        return ResponseEntity.ok().body(maxKpiId);
    }

    @PostMapping ("/cat-graph-kpis/get-dst-data-by-cat-graph-kpi")
    public ResponseEntity<?> getDstDataByCatGraphKpi(@RequestBody CatGraphKpiDTO catGraphKpiDTO, @PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        if( catGraphKpiDTO == null || catGraphKpiDTO.getKpiId() == null || catGraphKpiDTO.getTableName() == null ){
            return ResponseEntity.badRequest().body("Params null");
        }
        if(! DataUtil.isRptGraphTable(catGraphKpiDTO.getTableName())){
            return ResponseEntity.badRequest().body("Table name wrong");
        }
        Page<RptGraphDTO> page = catGraphKpiService.getDstDataByCatGraphKpi(catGraphKpiDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping ("/cat-graph-kpis/delete-rpt-graph-by-id")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'DELETE')")
    public ResponseEntity<?> deleteRptGraphById(@RequestParam Long id, @RequestParam("tb") String tableName) {
        if( id == null || tableName == null ){
            return ResponseEntity.badRequest().body("Params null");
        }
        if(! DataUtil.isRptGraphTable(tableName)){
            return ResponseEntity.badRequest().body("Table not permitted");
        }
        catGraphKpiService.deleteRptGraphById(id, tableName);
        return ResponseEntity.ok().body("Success");
    }

    @DeleteMapping ("/cat-graph-kpis/delete-rpt-graph-by-kpi-id")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'DELETE')")
    public ResponseEntity<?> deleteRptGraphByKpiId(@RequestParam("kpiId") Long id, @RequestParam("tb") String tableName) {
        if( id == null || tableName == null ){
            return ResponseEntity.badRequest().body("Params null");
        }
        if(! DataUtil.isRptGraphTable(tableName)){
            return ResponseEntity.badRequest().body("Table not permitted");
        }
        catGraphKpiService.deleteRptGraphByKpiId(id, tableName);
        return ResponseEntity.ok().body("Success");
    }
}
