package com.b4t.app.web.rest;

import com.b4t.app.service.ConfigColumnQueryKpiService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigColumnQueryKpiDTO;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigColumnQueryKpi}.
 */
@RestController
@RequestMapping("/api")
public class ConfigColumnQueryKpiResource {

    private final Logger log = LoggerFactory.getLogger(ConfigColumnQueryKpiResource.class);

    private static final String ENTITY_NAME = "configColumnQueryKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigColumnQueryKpiService configColumnQueryKpiService;

    public ConfigColumnQueryKpiResource(ConfigColumnQueryKpiService configColumnQueryKpiService) {
        this.configColumnQueryKpiService = configColumnQueryKpiService;
    }

    /**
     * {@code POST  /config-column-query-kpis} : Create a new configColumnQueryKpi.
     *
     * @param configColumnQueryKpiDTO the configColumnQueryKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configColumnQueryKpiDTO, or with status {@code 400 (Bad Request)} if the configColumnQueryKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-column-query-kpis")
    public ResponseEntity<ConfigColumnQueryKpiDTO> createConfigColumnQueryKpi(@RequestBody ConfigColumnQueryKpiDTO configColumnQueryKpiDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigColumnQueryKpi : {}", configColumnQueryKpiDTO);
        if (configColumnQueryKpiDTO.getId() != null) {
            throw new BadRequestAlertException("A new configColumnQueryKpi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigColumnQueryKpiDTO result = configColumnQueryKpiService.save(configColumnQueryKpiDTO);
        return ResponseEntity.created(new URI("/api/config-column-query-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-column-query-kpis} : Updates an existing configColumnQueryKpi.
     *
     * @param configColumnQueryKpiDTO the configColumnQueryKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configColumnQueryKpiDTO,
     * or with status {@code 400 (Bad Request)} if the configColumnQueryKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configColumnQueryKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-column-query-kpis")
    public ResponseEntity<ConfigColumnQueryKpiDTO> updateConfigColumnQueryKpi(@RequestBody ConfigColumnQueryKpiDTO configColumnQueryKpiDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigColumnQueryKpi : {}", configColumnQueryKpiDTO);
        if (configColumnQueryKpiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigColumnQueryKpiDTO result = configColumnQueryKpiService.save(configColumnQueryKpiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configColumnQueryKpiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-column-query-kpis} : get all the configColumnQueryKpis.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configColumnQueryKpis in body.
     */
    @GetMapping("/config-column-query-kpis")
    public ResponseEntity<List<ConfigColumnQueryKpiDTO>> getAllConfigColumnQueryKpis(Pageable pageable) {
        log.debug("REST request to get a page of ConfigColumnQueryKpis");
        Page<ConfigColumnQueryKpiDTO> page = configColumnQueryKpiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-column-query-kpis/:id} : get the "id" configColumnQueryKpi.
     *
     * @param id the id of the configColumnQueryKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configColumnQueryKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-column-query-kpis/{id}")
    public ResponseEntity<ConfigColumnQueryKpiDTO> getConfigColumnQueryKpi(@PathVariable Long id) {
        log.debug("REST request to get ConfigColumnQueryKpi : {}", id);
        Optional<ConfigColumnQueryKpiDTO> configColumnQueryKpiDTO = configColumnQueryKpiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configColumnQueryKpiDTO);
    }

    /**
     * {@code DELETE  /config-column-query-kpis/:id} : delete the "id" configColumnQueryKpi.
     *
     * @param id the id of the configColumnQueryKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-column-query-kpis/{id}")
    public ResponseEntity<Void> deleteConfigColumnQueryKpi(@PathVariable Long id) {
        log.debug("REST request to delete ConfigColumnQueryKpi : {}", id);
        configColumnQueryKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
