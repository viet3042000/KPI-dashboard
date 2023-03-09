package com.b4t.app.web.rest;

import com.b4t.app.service.ConfigMapKpiQueryService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigMapKpiQueryDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigMapKpiQuery}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMapKpiQueryResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMapKpiQueryResource.class);

    private static final String ENTITY_NAME = "configMapKpiQuery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigMapKpiQueryService configMapKpiQueryService;

    public ConfigMapKpiQueryResource(ConfigMapKpiQueryService configMapKpiQueryService) {
        this.configMapKpiQueryService = configMapKpiQueryService;
    }

    /**
     * {@code POST  /config-map-kpi-queries} : Create a new configMapKpiQuery.
     *
     * @param configMapKpiQueryDTO the configMapKpiQueryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMapKpiQueryDTO, or with status {@code 400 (Bad Request)} if the configMapKpiQuery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-map-kpi-queries")
    public ResponseEntity<ConfigMapKpiQueryDTO> createConfigMapKpiQuery(@RequestBody ConfigMapKpiQueryDTO configMapKpiQueryDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMapKpiQuery : {}", configMapKpiQueryDTO);
        if (configMapKpiQueryDTO.getId() != null) {
            throw new BadRequestAlertException("A new configMapKpiQuery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigMapKpiQueryDTO result = configMapKpiQueryService.save(configMapKpiQueryDTO);
        return ResponseEntity.created(new URI("/api/config-map-kpi-queries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-map-kpi-queries} : Updates an existing configMapKpiQuery.
     *
     * @param configMapKpiQueryDTO the configMapKpiQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMapKpiQueryDTO,
     * or with status {@code 400 (Bad Request)} if the configMapKpiQueryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMapKpiQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-map-kpi-queries")
    public ResponseEntity<ConfigMapKpiQueryDTO> updateConfigMapKpiQuery(@RequestBody ConfigMapKpiQueryDTO configMapKpiQueryDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMapKpiQuery : {}", configMapKpiQueryDTO);
        if (configMapKpiQueryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigMapKpiQueryDTO result = configMapKpiQueryService.save(configMapKpiQueryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMapKpiQueryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-map-kpi-queries} : get all the configMapKpiQueries.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMapKpiQueries in body.
     */
    @GetMapping("/config-map-kpi-queries")
    public ResponseEntity<List<ConfigMapKpiQueryDTO>> getAllConfigMapKpiQueries(Pageable pageable) {
        log.debug("REST request to get a page of ConfigMapKpiQueries");
        Page<ConfigMapKpiQueryDTO> page = configMapKpiQueryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-map-kpi-queries/:id} : get the "id" configMapKpiQuery.
     *
     * @param id the id of the configMapKpiQueryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMapKpiQueryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-map-kpi-queries/{id}")
    public ResponseEntity<ConfigMapKpiQueryDTO> getConfigMapKpiQuery(@PathVariable Long id) {
        log.debug("REST request to get ConfigMapKpiQuery : {}", id);
        Optional<ConfigMapKpiQueryDTO> configMapKpiQueryDTO = configMapKpiQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapKpiQueryDTO);
    }

    /**
     * {@code DELETE  /config-map-kpi-queries/:id} : delete the "id" configMapKpiQuery.
     *
     * @param id the id of the configMapKpiQueryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-map-kpi-queries/{id}")
    public ResponseEntity<Void> deleteConfigMapKpiQuery(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMapKpiQuery : {}", id);
        configMapKpiQueryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
