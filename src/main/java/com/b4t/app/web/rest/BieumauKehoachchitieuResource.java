package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.ExcelUtils;
import com.b4t.app.commons.StringUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.BieumauKehoachchitieu;
import com.b4t.app.service.BieumauKehoachchitieuService;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import jdk.nashorn.internal.parser.JSONParser;

/**
 * REST controller for managing {@link com.b4t.app.domain.BieumauKehoachchitieu}.
 */
@RestController
@RequestMapping("/api")
public class BieumauKehoachchitieuResource {

    private final Logger log = LoggerFactory.getLogger(BieumauKehoachchitieuResource.class);
    private final Environment env;
    private static final String ENTITY_NAME = "bieumauKehoachchitieu";
    @Autowired
    ExcelUtils excelUtils;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BieumauKehoachchitieuService bieumauKehoachchitieuService;

    public BieumauKehoachchitieuResource(Environment env, BieumauKehoachchitieuService bieumauKehoachchitieuService) {
        this.env = env;
        this.bieumauKehoachchitieuService = bieumauKehoachchitieuService;
    }

    /**
     * {@code POST  /bieumau-kehoachchitieu} : Create a new bieumauKehoachchitieu.
     *
     * @param bieumauKehoachchitieuDTO the bieumauKehoachchitieuDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bieumauKehoachchitieuDTO, or with status {@code 400 (Bad Request)} if the bieumauKehoachchitieu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bieumau-kehoachchitieu")
    @PreAuthorize("hasPermission('KE-HOACH-CHI-TIEU', '*')")
    public ResponseEntity<BieumauKehoachchitieuDTO> createBieumauKehoachchitieu(@RequestBody BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) throws URISyntaxException {
        log.debug("REST request to save BieumauKehoachchitieu : {}", bieumauKehoachchitieuDTO);

        if (bieumauKehoachchitieuDTO.getId() != null) {
            throw new BadRequestAlertException("A new bieumauKehoachchitieu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (bieumauKehoachchitieuService.validateDuplicate(bieumauKehoachchitieuDTO).isPresent()) {
            BieumauKehoachchitieu obj = bieumauKehoachchitieuService.validateDuplicate(bieumauKehoachchitieuDTO).get().get(0);
            bieumauKehoachchitieuDTO.setId(obj.getId());
            BieumauKehoachchitieuDTOError bieumauKehoachchitieuDTOError = new BieumauKehoachchitieuDTOError(bieumauKehoachchitieuDTO);
            bieumauKehoachchitieuDTOError.setShowError(Translator.toLocale("error.duplicate.field", new Object[]{bieumauKehoachchitieuDTO.getKpiName(), bieumauKehoachchitieuDTO.getPrdId().toString().substring(0, 4)}));
           /* return ResponseEntity.created(new URI("/api/bieumau-kehoachchitieu/" + obj.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, Translator.toLocale("error.duplicate.field", new Object[]{bieumauKehoachchitieuDTO.getKpiName(), bieumauKehoachchitieuDTO.getPrdId().toString()})))
                .body(bieumauKehoachchitieuDTOError);*/
            throw new BadRequestAlertException(Translator.toLocale("error.duplicate.field", new Object[]{bieumauKehoachchitieuDTO.getKpiName(), bieumauKehoachchitieuDTO.getPrdId().toString().substring(0, 4)}), ENTITY_NAME, "idexists");
        }
        BieumauKehoachchitieuDTO result = bieumauKehoachchitieuService.save(bieumauKehoachchitieuDTO);
        return ResponseEntity.created(new URI("/api/bieumau-kehoachchitieu/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bieumau-kehoachchitieu} : Updates an existing bieumauKehoachchitieu.
     *
     * @param bieumauKehoachchitieuDTO the bieumauKehoachchitieuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bieumauKehoachchitieuDTO,
     * or with status {@code 400 (Bad Request)} if the bieumauKehoachchitieuDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bieumauKehoachchitieuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bieumau-kehoachchitieu")
    @PreAuthorize("hasPermission('KE-HOACH-CHI-TIEU', '*')")
    public ResponseEntity<BieumauKehoachchitieuDTO> updateBieumauKehoachchitieu(@RequestBody BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO) throws URISyntaxException {
        log.debug("REST request to update BieumauKehoachchitieu : {}", bieumauKehoachchitieuDTO);
        if (bieumauKehoachchitieuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (bieumauKehoachchitieuService.validateDuplicate(bieumauKehoachchitieuDTO).isPresent()
            && !bieumauKehoachchitieuService.validateDuplicate(bieumauKehoachchitieuDTO).get().get(0).getId().equals(bieumauKehoachchitieuDTO.getId())) {
            throw new BadRequestAlertException(Translator.toLocale("error.duplicate.field", new Object[]{bieumauKehoachchitieuDTO.getKpiName(), bieumauKehoachchitieuDTO.getPrdId().toString().substring(0, 4)}), ENTITY_NAME, "idexists");
            /*return ResponseEntity.created(new URI("/api/bieumau-kehoachchitieu/" + bieumauKehoachchitieuDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, Translator.toLocale("error.duplicate.field", new Object[]{bieumauKehoachchitieuDTO.getKpiName(), bieumauKehoachchitieuDTO.getPrdId().toString().substring(0, 4)})))
                .body(bieumauKehoachchitieuDTO);*/
        }
        BieumauKehoachchitieuDTO result = bieumauKehoachchitieuService.save(bieumauKehoachchitieuDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bieumauKehoachchitieuDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bieumau-kehoachchitieu} : get all the bieumauKehoachchitieus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bieumauKehoachchitieus in body.
     */
    @GetMapping("/bieumau-kehoachchitieu")
    public ResponseEntity<List<BieumauKehoachchitieuDTO>> getAllBieumauKehoachchitieus(Pageable pageable) {
        log.debug("REST request to get a page of BieumauKehoachchitieus");
        Page<BieumauKehoachchitieuDTO> page = bieumauKehoachchitieuService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/bieumau-kehoachchitieu/query")
    @PreAuthorize("hasPermission('KE-HOACH-CHI-TIEU', '*')")
    public ResponseEntity<List<BieumauKehoachchitieuDTO>> query(SearchBieuMauParram searchBieuMauParram, Pageable pageable) {
        log.debug("REST request to get a page of BieumauKehoachchitieus");
        Page<BieumauKehoachchitieuDTO> page = bieumauKehoachchitieuService.query(searchBieuMauParram, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bieumau-kehoachchitieu/:id} : get the "id" bieumauKehoachchitieu.
     *
     * @param id the id of the bieumauKehoachchitieuDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bieumauKehoachchitieuDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bieumau-kehoachchitieu/{id}")
    public ResponseEntity<BieumauKehoachchitieuDTO> getBieumauKehoachchitieu(@PathVariable Long id) {
        log.debug("REST request to get BieumauKehoachchitieu : {}", id);
        Optional<BieumauKehoachchitieuDTO> bieumauKehoachchitieuDTO = bieumauKehoachchitieuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bieumauKehoachchitieuDTO);
    }

    /**
     * {@code DELETE  /bieumau-kehoachchitieu/:id} : delete the "id" bieumauKehoachchitieu.
     *
     * @param id the id of the bieumauKehoachchitieuDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bieumau-kehoachchitieu/{id}")
    @PreAuthorize("hasPermission('KE-HOACH-CHI-TIEU', '*')")
    public ResponseEntity<Void> deleteBieumauKehoachchitieu(@PathVariable Long id) {
        log.debug("REST request to delete BieumauKehoachchitieu : {}", id);
        bieumauKehoachchitieuService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/bieumau-kehoachchitieu/import")
    @PreAuthorize("hasPermission('KE-HOACH-CHI-TIEU', '*')")
    @ResponseBody
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("Info") String info) throws NoSuchFieldException {
        System.out.println("Json is" + info);
        Gson gson = new Gson();
        Object parram = (Object) gson.fromJson(info, Object.class);
        Long prdId = DataUtil.safeToLong(((LinkedTreeMap) parram).get("prdId"));
        UploadFileResponse uploadFileResponse;
        if (file.isEmpty()) {
            throw new BadRequestAlertException(Translator.toLocale("error.file.format"), ENTITY_NAME, "idnull");
        }
        File errorFile = null;
        try {
            uploadFileResponse = bieumauKehoachchitieuService.doImportFile(file, prdId);
            if (!uploadFileResponse.isImportSucces()) {
                errorFile = makeReportValidateError(uploadFileResponse.getLstError());
                uploadFileResponse.setErrorFile(errorFile.getName());
                uploadFileResponse.setFileName(errorFile.getName());
            } else {
                uploadFileResponse.setFileName(file.getOriginalFilename());
                uploadFileResponse.setImportSucces(true);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestAlertException(Translator.toLocale("error.file.format"), ENTITY_NAME, "idnull");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestAlertException(Translator.toLocale("error.file.format"), ENTITY_NAME, "idnull");
//            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "idnull");
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileResponse.getFileName().toString()))
            .body(uploadFileResponse);
    }

    @PostMapping("/bieumau-kehoachchitieu/download")
    public ResponseEntity<Resource> download(String fileName) throws FileNotFoundException {
        fileName = StringUtils.getSafeFileName(fileName);
        String folder = env.getProperty("export-tmp.kpi-export");
        File file = new File(folder + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
            .contentLength(file.length())
            .header("filename", file.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);

    }

    @PostMapping("/bieumau-kehoachchitieu/downloadTemplate")
    public ResponseEntity<Resource> downloadTemplate(BieumauKehoachchitieuTmpDTO bieumauKehoachchitieuTmpDTO) throws Exception {
        List<BieumauKehoachchitieuDTO> lstTemplate = bieumauKehoachchitieuService.buildTemplate(bieumauKehoachchitieuTmpDTO);
        File file = makeFileTemplate(lstTemplate);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
            .contentLength(file.length())
            .header("filename", file.getName())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);

    }

    private File makeFileTemplate(List<BieumauKehoachchitieuDTO> lstTemplate) throws Exception {
        List<ExcelColumn> lstColumn = new ArrayList<>();
//        lstColumn.add(new ExcelColumn("prdId", Translator.toLocale("label.column.prdId"), ExcelColumn.ALIGN_MENT.LEFT));
//        lstColumn.add(new ExcelColumn("domainCode", Translator.toLocale("catGraphKpi.column.domainCode"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("kpiId", Translator.toLocale("catGraphKpi.column.kpiId"), ExcelColumn.ALIGN_MENT.LEFT));
//        lstColumn.add(new ExcelColumn("kpiCode", Translator.toLocale("catGraphKpi.column.kpiCode"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("kpiName", Translator.toLocale("catGraphKpi.column.kpiName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("unitName", Translator.toLocale("catGraphKpi.column.UnitName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("valPlan", Translator.toLocale("catGraphKpi.column.target"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("totalRank", Translator.toLocale("catGraphKpi.column.totalRank"), ExcelColumn.ALIGN_MENT.LEFT));
//        lstColumn.add(new ExcelColumn("description", Translator.toLocale("label.column.description"), ExcelColumn.ALIGN_MENT.LEFT));
//        lstColumn.add(new ExcelColumn("status", Translator.toLocale("label.column.status"), ExcelColumn.ALIGN_MENT.LEFT));
        String title = Translator.toLocale("label.file.template.file.title");
        /*if(DataUtil.isNullOrEmpty(lstTemplate)){
            lstTemplate = makeDefaultObject();
        }*/
        File fileOut = excelUtils.onExportTemplate(lstColumn, lstTemplate, 3, 0, title, Translator.toLocale("label.file.template.name"));
        return fileOut;
    }

    private List<BieumauKehoachchitieuDTO> makeDefaultObject() {
        List<BieumauKehoachchitieuDTO> results = new ArrayList<>();
        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO;
        for (int i = 0; i < 100; i++) {
            bieumauKehoachchitieuDTO = new BieumauKehoachchitieuDTO();
            results.add(bieumauKehoachchitieuDTO);
        }
        return results;
    }

    private File makeReportValidateError(List<BieumauKehoachchitieuDTOError> lstDatas) throws Exception {
        List<ExcelColumn> lstColumn = new ArrayList<>();
        lstColumn.add(new ExcelColumn("kpiId", Translator.toLocale("catGraphKpi.column.kpiId"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("kpiName", Translator.toLocale("catGraphKpi.column.kpiName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("unitName", Translator.toLocale("catGraphKpi.column.UnitName"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("valPlan", Translator.toLocale("catGraphKpi.column.target"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("totalRank", Translator.toLocale("catGraphKpi.column.totalRank"), ExcelColumn.ALIGN_MENT.LEFT));
        lstColumn.add(new ExcelColumn("showError", Translator.toLocale("label.file.error.errorMessages"), ExcelColumn.ALIGN_MENT.LEFT));
        String title = Translator.toLocale("label.file.error.file.title");
        File fileOut = excelUtils.onExportTemplate(lstColumn, lstDatas, 3, 0, title, Translator.toLocale("label.file.error.name"));
        return fileOut;
    }

    @PutMapping("/bieumau-kehoachchitieu/multiple-delete")
    @PreAuthorize("hasPermission('KE-HOACH-CHI-TIEU', '*')")
    public ResponseEntity<Void> multiDeleteBieumauKehoachchitieu(@RequestBody List<BieumauKehoachchitieuDTO> data) {
        log.debug("REST request to multiple delete BieumauKehoachchitieu ");
        bieumauKehoachchitieuService.multiDelete(data);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, data.size()+" row")).build();
    }
}
