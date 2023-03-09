package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMenu;
import com.b4t.app.domain.ConfigQueryKpi;
import com.b4t.app.service.ConfigQueryKpiService;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigQueryKpi}.
 */
@RestController
@RequestMapping("/api")
public class ConfigQueryKpiResource {

    private final Logger log = LoggerFactory.getLogger(ConfigQueryKpiResource.class);

    private static final String ENTITY_NAME = "configQueryKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigQueryKpiService configQueryKpiService;

    public ConfigQueryKpiResource(ConfigQueryKpiService configQueryKpiService) {
        this.configQueryKpiService = configQueryKpiService;
    }

    /**
     * {@code POST  /config-query-kpis} : Create a new configQueryKpi.
     *
     * @param configQueryKpiDTO the configQueryKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configQueryKpiDTO, or with status {@code 400 (Bad Request)} if the configQueryKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-query-kpis")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', 'INSERT')")
    public ResponseEntity<ConfigQueryKpiDetailDTO> createConfigQueryKpi(@RequestBody ConfigQueryKpiDetailDTO configQueryKpiDTO,
                                                                        HttpServletRequest request) throws Exception {
        log.debug("REST request to save ConfigQueryKpi : {}", configQueryKpiDTO);
        String actor = (String) request.getAttribute(Constants.ACTOR);
        if (configQueryKpiDTO.getConfigQueryKpi().getId() != null) {
            throw new BadRequestAlertException("A new configQueryKpi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigQueryKpiDetailDTO result = configQueryKpiService.save(configQueryKpiDTO, actor);
        return ResponseEntity.created(new URI("/api/config-query-kpis/" + result.getConfigQueryKpi().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getConfigQueryKpi().getId().toString()))
            .body(result);
    }

    @PostMapping("/config-query-kpis-check-query-data")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', '*')")
    public ResponseEntity<Void> checkQueryData(@RequestBody ConfigQueryKpiForm configQueryKpiDTO) throws Exception {
        log.debug("REST request to save ConfigQueryKpi : {}", configQueryKpiDTO);
        configQueryKpiService.checkQueryData(configQueryKpiDTO.getQueryData());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, configQueryKpiDTO.getQueryData().toString())).build();

    }
    @PostMapping("/config-query-kpis-check-query-has-data")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', '*')")
    public ResponseEntity<Void> checkQueryCheckData(@RequestBody ConfigQueryKpiForm configQueryKpiDTO) throws Exception {
        log.debug("REST request to save ConfigQueryKpi : {}", configQueryKpiDTO);
        configQueryKpiService.checkQueryData(configQueryKpiDTO.getQueryCheckData());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, configQueryKpiDTO.getQueryData().toString())).build();

    }

    /**
     * {@code PUT  /config-query-kpis} : Updates an existing configQueryKpi.
     *
     * @param configQueryKpiDTO the configQueryKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configQueryKpiDTO,
     * or with status {@code 400 (Bad Request)} if the configQueryKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configQueryKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-query-kpis")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', 'UPDATE')")
    public ResponseEntity<ConfigQueryKpiDetailDTO> updateConfigQueryKpi(@RequestBody ConfigQueryKpiDetailDTO configQueryKpiDTO, HttpServletRequest request) throws Exception {
        log.debug("REST request to update ConfigQueryKpi : {}", configQueryKpiDTO);
        String actor = (String) request.getAttribute(Constants.ACTOR);
        if (configQueryKpiDTO.getConfigQueryKpi().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigQueryKpiDetailDTO result = configQueryKpiService.update(configQueryKpiDTO, actor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configQueryKpiDTO.getConfigQueryKpi().getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-query-kpis} : get all the configQueryKpis.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configQueryKpis in body.
     */
    @GetMapping("/config-query-kpis")
    public ResponseEntity<List<ConfigQueryKpiDTO>> getAllConfigQueryKpis(Pageable pageable) {
        log.debug("REST request to get a page of ConfigQueryKpis");
        Page<ConfigQueryKpiDTO> page = configQueryKpiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/config-query-kpis-query")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', '*')")
    public ResponseEntity<List<ConfigQueryKpiResultDTO>> findConfigQueryKpis(@RequestBody ConfigQueryKpiSearch searchForm,  Pageable pageable) {
        log.debug("REST request to get a page of ConfigQueryKpis");
        Page<ConfigQueryKpiResultDTO> page = configQueryKpiService.findConfigQueryKpis(searchForm,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-query-kpis/:id} : get the "id" configQueryKpi.
     *
     * @param id the id of the configQueryKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configQueryKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-query-kpis/{id}")
    public ResponseEntity<ConfigQueryKpiDetailDTO> getConfigQueryKpi(@PathVariable Long id) {
        log.debug("REST request to get ConfigQueryKpi : {}", id);
        Optional<ConfigQueryKpiDetailDTO> configQueryKpiDTO = configQueryKpiService.findConfigById(id);
        return ResponseUtil.wrapOrNotFound(configQueryKpiDTO);
    }

    /**
     * {@code DELETE  /config-query-kpis/:id} : delete the "id" configQueryKpi.
     *
     * @param id the id of the configQueryKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-query-kpis/{id}")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', 'DELETE')")
    public ResponseEntity<Void> deleteConfigQueryKpi(@PathVariable Long id) {
        log.debug("REST request to delete ConfigQueryKpi : {}", id);
        configQueryKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/config-query-kpis/delete-multiple")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', 'DELETE')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> deleteConfigQueryKpiMultiple(@RequestBody List<ConfigQueryKpiDTO> configQueryKpis) {
        //log.debug("REST request to delete ConfigQueryKpi : {}", id);
        for(ConfigQueryKpiDTO con : configQueryKpis){
            Optional<ConfigQueryKpiDTO> ckOpt = configQueryKpiService.findOne(con.getQueryKpiId());
            if (!ckOpt.isPresent()) {
                throw new BadRequestAlertException(Translator.toLocale("error.disabled.item"), ENTITY_NAME, "error.disabled.item");
            }
            configQueryKpiService.delete(con.getQueryKpiId());
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build();
    }


    @GetMapping("/config-query-kpis-kpi-in/{id}")
    public ResponseEntity<List<ComboDTO>> findAllKpiInput(@PathVariable String id,  Pageable pageable) {
        log.debug("REST request to get list kpi in : {}", id);
        Page<ComboDTO> page = configQueryKpiService.findAllKpiInput(id,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/config-query-kpis-kpi-out/{id}")
    public ResponseEntity<List<ComboDTO>> findAllKpiOutput(@PathVariable String id, Pageable pageable) {
        log.debug("REST request to get list kpi out : {}", id);
        Page<ComboDTO> page = configQueryKpiService.findAllKpiOutput(id,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/config-query-kpis-source-table")
    public ResponseEntity<List<ComboDTO>> findAllKpiSourceTable(@RequestBody(required = false) LinkedHashMap request) {
        log.debug("REST request to get findAllKpiSourceTable : {}");
        List<String> lstTable = DataUtil.isNullOrEmpty(request) || !request.containsKey("lstSourceTable") ? null : (List<String>) request.get("lstSourceTable");
        List<ComboDTO> page = configQueryKpiService.findAllKpiSourceTable(lstTable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/config-query-kpis-clone/{id}")
    @PreAuthorize("hasPermission('CONFIG-QUERY-KPI', 'INSERT')")
    public ResponseEntity<Void> cloneConfigQueryKpi(@PathVariable Long id) throws Exception{
        log.debug("REST request to delete ConfigQueryKpi : {}", id);
        configQueryKpiService.clone(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
