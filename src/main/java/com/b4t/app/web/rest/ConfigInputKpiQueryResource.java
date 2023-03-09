package com.b4t.app.web.rest;

import com.b4t.app.service.ConfigInputKpiQueryService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigInputKpiQueryDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigInputKpiQuery}.
 */
@RestController
@RequestMapping("/api")
public class ConfigInputKpiQueryResource {

    private final Logger log = LoggerFactory.getLogger(ConfigInputKpiQueryResource.class);

    private static final String ENTITY_NAME = "configInputKpiQuery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigInputKpiQueryService configInputKpiQueryService;

    public ConfigInputKpiQueryResource(ConfigInputKpiQueryService configInputKpiQueryService) {
        this.configInputKpiQueryService = configInputKpiQueryService;
    }

    /**
     * {@code POST  /config-input-kpi-queries} : Create a new configInputKpiQuery.
     *
     * @param configInputKpiQueryDTO the configInputKpiQueryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configInputKpiQueryDTO, or with status {@code 400 (Bad Request)} if the configInputKpiQuery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-input-kpi-queries")
    public ResponseEntity<ConfigInputKpiQueryDTO> createConfigInputKpiQuery(@RequestBody ConfigInputKpiQueryDTO configInputKpiQueryDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigInputKpiQuery : {}", configInputKpiQueryDTO);
        if (configInputKpiQueryDTO.getId() != null) {
            throw new BadRequestAlertException("A new configInputKpiQuery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigInputKpiQueryDTO result = configInputKpiQueryService.save(configInputKpiQueryDTO);
        return ResponseEntity.created(new URI("/api/config-input-kpi-queries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-input-kpi-queries} : Updates an existing configInputKpiQuery.
     *
     * @param configInputKpiQueryDTO the configInputKpiQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configInputKpiQueryDTO,
     * or with status {@code 400 (Bad Request)} if the configInputKpiQueryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configInputKpiQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-input-kpi-queries")
    public ResponseEntity<ConfigInputKpiQueryDTO> updateConfigInputKpiQuery(@RequestBody ConfigInputKpiQueryDTO configInputKpiQueryDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigInputKpiQuery : {}", configInputKpiQueryDTO);
        if (configInputKpiQueryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigInputKpiQueryDTO result = configInputKpiQueryService.save(configInputKpiQueryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configInputKpiQueryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-input-kpi-queries} : get all the configInputKpiQueries.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configInputKpiQueries in body.
     */
    @GetMapping("/config-input-kpi-queries")
    public ResponseEntity<List<ConfigInputKpiQueryDTO>> getAllConfigInputKpiQueries(Pageable pageable) {
        log.debug("REST request to get a page of ConfigInputKpiQueries");
        Page<ConfigInputKpiQueryDTO> page = configInputKpiQueryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-input-kpi-queries/:id} : get the "id" configInputKpiQuery.
     *
     * @param id the id of the configInputKpiQueryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configInputKpiQueryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-input-kpi-queries/{id}")
    public ResponseEntity<ConfigInputKpiQueryDTO> getConfigInputKpiQuery(@PathVariable Long id) {
        log.debug("REST request to get ConfigInputKpiQuery : {}", id);
        Optional<ConfigInputKpiQueryDTO> configInputKpiQueryDTO = configInputKpiQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configInputKpiQueryDTO);
    }

    /**
     * {@code DELETE  /config-input-kpi-queries/:id} : delete the "id" configInputKpiQuery.
     *
     * @param id the id of the configInputKpiQueryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-input-kpi-queries/{id}")
    public ResponseEntity<Void> deleteConfigInputKpiQuery(@PathVariable Long id) {
        log.debug("REST request to delete ConfigInputKpiQuery : {}", id);
        configInputKpiQueryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
