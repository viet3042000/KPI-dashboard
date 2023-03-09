package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.CatGraphKpiOriginService;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.CatGraphKpiDetailDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.CatGraphKpiOriginDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.CatGraphKpiOrigin}.
 */
@RestController
@RequestMapping("/api")
public class CatGraphKpiOriginResource {

    private final Logger log = LoggerFactory.getLogger(CatGraphKpiOriginResource.class);

    private static final String ENTITY_NAME = "catGraphKpiOrigin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatGraphKpiOriginService catGraphKpiOriginService;

    public CatGraphKpiOriginResource(CatGraphKpiOriginService catGraphKpiOriginService) {
        this.catGraphKpiOriginService = catGraphKpiOriginService;
    }

    /**
     * {@code POST  /cat-graph-kpi-origins} : Create a new catGraphKpiOrigin.
     *
     * @param catGraphKpiOriginDTO the catGraphKpiOriginDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catGraphKpiOriginDTO, or with status {@code 400 (Bad Request)} if the catGraphKpiOrigin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cat-graph-kpis-origin")
    public ResponseEntity<CatGraphKpiOriginDTO> createCatGraphKpiOrigin(@RequestBody CatGraphKpiOriginDTO catGraphKpiOriginDTO) throws URISyntaxException {
        log.debug("REST request to save CatGraphKpiOrigin : {}", catGraphKpiOriginDTO);
        if (catGraphKpiOriginDTO.getId() != null) {
            //update
            CatGraphKpiOriginDTO catGraphKpiOriginDTO1 = catGraphKpiOriginService.findOne(catGraphKpiOriginDTO.getId())
                .orElseThrow(() -> new BadRequestAlertException("Not exists", ENTITY_NAME, "Not exists"));
            String user = SecurityUtils.getCurrentUserLogin().get();
            catGraphKpiOriginDTO.setCreatedBy(catGraphKpiOriginDTO1.getCreatedBy());
            catGraphKpiOriginDTO.setCreatedTime(catGraphKpiOriginDTO1.getCreatedTime());
            catGraphKpiOriginDTO.setModifiedBy(user);
            catGraphKpiOriginDTO.setModifiedTime(Instant.now());
        } else{
            //create
            String user = SecurityUtils.getCurrentUserLogin().get();
            catGraphKpiOriginDTO.setCreatedBy(user);
            catGraphKpiOriginDTO.setCreatedTime(Instant.now());
            catGraphKpiOriginDTO.setModifiedBy(user);
            catGraphKpiOriginDTO.setModifiedTime(Instant.now());
        }

        CatGraphKpiOriginDTO result = catGraphKpiOriginService.save(catGraphKpiOriginDTO);
        return ResponseEntity.created(new URI("/api/cat-graph-kpi-origins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cat-graph-kpi-origins} : Updates an existing catGraphKpiOrigin.
     *
     * @param catGraphKpiOriginDTO the catGraphKpiOriginDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catGraphKpiOriginDTO,
     * or with status {@code 400 (Bad Request)} if the catGraphKpiOriginDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catGraphKpiOriginDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.

    @PutMapping("/cat-graph-kpis-origin")
    public ResponseEntity<CatGraphKpiOriginDTO> updateCatGraphKpiOrigin(@RequestBody CatGraphKpiOriginDTO catGraphKpiOriginDTO) throws URISyntaxException {
        log.debug("REST request to update CatGraphKpiOrigin : {}", catGraphKpiOriginDTO);
        if (catGraphKpiOriginDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String user = SecurityUtils.getCurrentUserLogin().get();
        catGraphKpiOriginDTO.setCreatedBy(user);
        catGraphKpiOriginDTO.setCreatedTime(Instant.now());
        catGraphKpiOriginDTO.setModifiedBy(user);
        catGraphKpiOriginDTO.setModifiedTime(Instant.now());
        CatGraphKpiOriginDTO result = catGraphKpiOriginService.save(catGraphKpiOriginDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catGraphKpiOriginDTO.getId().toString()))
            .body(result);
    }
     */

    /**
     * {@code GET  /cat-graph-kpi-origins} : get all the catGraphKpiOrigins.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catGraphKpiOrigins in body.
     */
    @GetMapping("/cat-graph-kpi-origins")
    public ResponseEntity<List<CatGraphKpiOriginDTO>> getAllCatGraphKpiOrigins(Pageable pageable) {
        log.debug("REST request to get a page of CatGraphKpiOrigins");
        Page<CatGraphKpiOriginDTO> page = catGraphKpiOriginService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cat-graph-kpi-origins/:id} : get the "id" catGraphKpiOrigin.
     *
     * @param id the id of the catGraphKpiOriginDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catGraphKpiOriginDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cat-graph-kpis-origin/{id}")
    public ResponseEntity<CatGraphKpiOriginDTO> getCatGraphKpiOrigin(@PathVariable Long id) {
        log.debug("REST request to get CatGraphKpiOrigin : {}", id);
        Optional<CatGraphKpiOriginDTO> catGraphKpiOriginDTO = catGraphKpiOriginService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catGraphKpiOriginDTO);
    }

    @PostMapping("/cat-graph-kpis-origin-search")
//    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', '*')")
    public ResponseEntity<List<CatGraphKpiOriginDTO>> onSearch(@RequestBody CatGraphKpiOriginDTO catGraphKpiOriginDTO,@PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
//        log.debug(REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS);
        Page<CatGraphKpiOriginDTO> page = catGraphKpiOriginService.onSearch(catGraphKpiOriginDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/cat-graph-kpis-origin-clone")
//    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'INSERT')")
    public ResponseEntity<CatGraphKpiOriginDTO> copyCatGraphOriginKpi(@RequestBody CatGraphKpiOriginDTO catGraphKpiOriginDTO) throws URISyntaxException {
//        log.debug("REST request to save CatGraphKpi : {}", catGraphKpiDTO);
        if (catGraphKpiOriginDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        CatGraphKpiOriginDTO result = catGraphKpiOriginService.save(catGraphKpiOriginDTO);
        return ResponseEntity.created(new URI("/api/cat-graph-kpis-origin/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code DELETE  /cat-graph-kpi-origins/:id} : delete the "id" catGraphKpiOrigin.
     *
     * @param id the id of the catGraphKpiOriginDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cat-graph-kpis-origin/{id}")
    @PreAuthorize("hasPermission('CAT-GRAPH-KPI', 'DELETE')")
    public ResponseEntity<Void> deleteCatGraphKpi(@PathVariable Long id) {
//        log.debug("REST request to delete CatGraphKpi : {}", id);
        CatGraphKpiOriginDTO catGraphKpiOriginDTO = catGraphKpiOriginService.findOne(id).orElseThrow(() -> new BadRequestAlertException("Not exists", ENTITY_NAME ,"Not exists"));
        catGraphKpiOriginDTO.setStatus(Constants.STATUS_DISABLED_INT);
        catGraphKpiOriginService.save(catGraphKpiOriginDTO);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
