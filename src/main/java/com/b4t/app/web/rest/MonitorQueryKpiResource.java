package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.MonitorQueryKpi;
import com.b4t.app.service.MonitorQueryKpiService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.MonitorQueryKpi}.
 */
@RestController
@RequestMapping("/api")
public class MonitorQueryKpiResource {

    private final Logger log = LoggerFactory.getLogger(MonitorQueryKpiResource.class);

    private static final String ENTITY_NAME = "monitorQueryKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitorQueryKpiService monitorQueryKpiService;

    public MonitorQueryKpiResource(MonitorQueryKpiService monitorQueryKpiService) {
        this.monitorQueryKpiService = monitorQueryKpiService;
    }

    /**
     * {@code POST  /monitor-query-kpis} : Create a new monitorQueryKpi.
     *
     * @param monitorQueryKpiDTO the monitorQueryKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitorQueryKpiDTO, or with status {@code 400 (Bad Request)} if the monitorQueryKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monitor-query-kpis")
    @PreAuthorize("hasPermission('MONITOR-QUERY-KPI', '*')")
    public ResponseEntity<MonitorQueryKpiDTO> createMonitorQueryKpi(@RequestBody MonitorQueryKpiDTO monitorQueryKpiDTO) throws URISyntaxException {
        log.debug("REST request to save MonitorQueryKpi : {}", monitorQueryKpiDTO);
        if (monitorQueryKpiDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitorQueryKpi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonitorQueryKpiDTO result = monitorQueryKpiService.save(monitorQueryKpiDTO);
        return ResponseEntity.created(new URI("/api/monitor-query-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monitor-query-kpis} : Updates an existing monitorQueryKpi.
     *
     * @param monitorQueryKpiDTO the monitorQueryKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitorQueryKpiDTO,
     * or with status {@code 400 (Bad Request)} if the monitorQueryKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitorQueryKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monitor-query-kpis")
    @PreAuthorize("hasPermission('MONITOR-QUERY-KPI', '*')")
    public ResponseEntity<MonitorQueryKpiDTO> updateMonitorQueryKpi(@RequestBody MonitorQueryKpiDTO monitorQueryKpiDTO) throws URISyntaxException {
        log.debug("REST request to update MonitorQueryKpi : {}", monitorQueryKpiDTO);
        if (monitorQueryKpiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MonitorQueryKpiDTO result = monitorQueryKpiService.save(monitorQueryKpiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitorQueryKpiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /monitor-query-kpis} : get all the monitorQueryKpis.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitorQueryKpis in body.
     */
    @GetMapping("/monitor-query-kpis")
    public ResponseEntity<List<MonitorQueryKpiDTO>> getAllMonitorQueryKpis(Pageable pageable) {
        log.debug("REST request to get a page of MonitorQueryKpis");
        Page<MonitorQueryKpiDTO> page = monitorQueryKpiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/monitor-query-kpis/query")
    @PreAuthorize("hasPermission('MONITOR-QUERY-KPI', '*')")
    public ResponseEntity<List<MonitorQueryKpiDetailDTO>> findMonitorQueryKpiByKey(@RequestBody MonitorQueryForm monitorQueryForm, Pageable pageable) {
        log.debug("REST request to get a page of MonitorQueryKpis");
        Page<MonitorQueryKpiDetailDTO> page = monitorQueryKpiService.findMonitorQueryKpiByKey(monitorQueryForm, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/monitor-query-kpis/restart")
    @PreAuthorize("hasPermission('MONITOR-QUERY-KPI', '*')")
    public ResponseEntity<Void> restartMonitorQueryKpi(@RequestBody MonitorQueryKpiDetailDTO monitorQueryForm) throws Exception {
        log.debug("REST request to restart MonitorQueryKpis");
        if(monitorQueryForm.getRunTimeSucc() == null){
            throw new BadRequestAlertException(Translator.toLocale("error.monitorQueryKpi.runTimeNotNull"), ENTITY_NAME, "monitorQueryKpi.runTimeNotNull");
        }
        monitorQueryKpiService.restartMonitorQueryKpi(monitorQueryForm);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, monitorQueryForm.toString())).build();
    }

    /**
     * {@code GET  /monitor-query-kpis/:id} : get the "id" monitorQueryKpi.
     *
     * @param id the id of the monitorQueryKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitorQueryKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monitor-query-kpis/{id}")
    public ResponseEntity<MonitorQueryKpiDTO> getMonitorQueryKpi(@PathVariable Long id) {
        log.debug("REST request to get MonitorQueryKpi : {}", id);
        Optional<MonitorQueryKpiDTO> monitorQueryKpiDTO = monitorQueryKpiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitorQueryKpiDTO);
    }

    /**
     * {@code DELETE  /monitor-query-kpis/:id} : delete the "id" monitorQueryKpi.
     *
     * @param id the id of the monitorQueryKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monitor-query-kpis/{id}")
    public ResponseEntity<Void> deleteMonitorQueryKpi(@PathVariable Long id) {
        log.debug("REST request to delete MonitorQueryKpi : {}", id);
        monitorQueryKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/monitor-query-kpis-template")
    public ResponseEntity<List<ComboDTO>> getMonitorTemplate() {
        log.debug("REST request to get MonitorQueryKpi : {}");
        List<ComboDTO> monitorQueryKpiDTO = monitorQueryKpiService.getMonitorTemplate();
        return ResponseEntity.ok(monitorQueryKpiDTO);
    }

    @GetMapping("/monitor-query-kpis-template/{template}")
    public ResponseEntity<List<ComboDTO>> getKpiByTemplate(@PathVariable String template) {
        log.debug("REST request to get MonitorQueryKpi : {}");
        List<ComboDTO> monitorQueryKpiDTO = monitorQueryKpiService.getKpiByTemplate(template);
        return ResponseEntity.ok(monitorQueryKpiDTO);
    }

    @PostMapping("/monitor-query-kpis/delete-multiple")
    public ResponseEntity<Void> deleteMonitorQueryKpiMultiple(@RequestBody List<MonitorQueryKpiDTO> monitorQueryKpis){
        log.debug("REST request to delete multiple item.");
//        for(MonitorQueryKpiDTO mo : monitorQueryKpis){
//            monitorQueryKpiService.deleteMultiple(mo.getId());
//        }
        monitorQueryKpiService.deleteMultiple(monitorQueryKpis);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build();
    }
}
