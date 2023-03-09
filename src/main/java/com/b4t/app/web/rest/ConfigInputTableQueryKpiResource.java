package com.b4t.app.web.rest;

import com.b4t.app.service.ConfigInputTableQueryKpiService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigInputTableQueryKpiDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigInputTableQueryKpi}.
 */
@RestController
@RequestMapping("/api")
public class ConfigInputTableQueryKpiResource {

    private final Logger log = LoggerFactory.getLogger(ConfigInputTableQueryKpiResource.class);

    private static final String ENTITY_NAME = "configInputTableQueryKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigInputTableQueryKpiService configInputTableQueryKpiService;

    public ConfigInputTableQueryKpiResource(ConfigInputTableQueryKpiService configInputTableQueryKpiService) {
        this.configInputTableQueryKpiService = configInputTableQueryKpiService;
    }

    /**
     * {@code POST  /config-input-table-query-kpis} : Create a new configInputTableQueryKpi.
     *
     * @param configInputTableQueryKpiDTO the configInputTableQueryKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configInputTableQueryKpiDTO, or with status {@code 400 (Bad Request)} if the configInputTableQueryKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-input-table-query-kpis")
    public ResponseEntity<ConfigInputTableQueryKpiDTO> createConfigInputTableQueryKpi(@RequestBody ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigInputTableQueryKpi : {}", configInputTableQueryKpiDTO);
        if (configInputTableQueryKpiDTO.getId() != null) {
            throw new BadRequestAlertException("A new configInputTableQueryKpi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigInputTableQueryKpiDTO result = configInputTableQueryKpiService.save(configInputTableQueryKpiDTO);
        return ResponseEntity.created(new URI("/api/config-input-table-query-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-input-table-query-kpis} : Updates an existing configInputTableQueryKpi.
     *
     * @param configInputTableQueryKpiDTO the configInputTableQueryKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configInputTableQueryKpiDTO,
     * or with status {@code 400 (Bad Request)} if the configInputTableQueryKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configInputTableQueryKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-input-table-query-kpis")
    public ResponseEntity<ConfigInputTableQueryKpiDTO> updateConfigInputTableQueryKpi(@RequestBody ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigInputTableQueryKpi : {}", configInputTableQueryKpiDTO);
        if (configInputTableQueryKpiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigInputTableQueryKpiDTO result = configInputTableQueryKpiService.save(configInputTableQueryKpiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configInputTableQueryKpiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-input-table-query-kpis} : get all the configInputTableQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configInputTableQueryKpis in body.
     */
    @GetMapping("/config-input-table-query-kpis")
    public ResponseEntity<List<ConfigInputTableQueryKpiDTO>> getAllConfigInputTableQueryKpis(Pageable pageable) {
        log.debug("REST request to get a page of ConfigInputTableQueryKpis");
        Page<ConfigInputTableQueryKpiDTO> page = configInputTableQueryKpiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-input-table-query-kpis/:id} : get the "id" configInputTableQueryKpi.
     *
     * @param id the id of the configInputTableQueryKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configInputTableQueryKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-input-table-query-kpis/{id}")
    public ResponseEntity<ConfigInputTableQueryKpiDTO> getConfigInputTableQueryKpi(@PathVariable Long id) {
        log.debug("REST request to get ConfigInputTableQueryKpi : {}", id);
        Optional<ConfigInputTableQueryKpiDTO> configInputTableQueryKpiDTO = configInputTableQueryKpiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configInputTableQueryKpiDTO);
    }

    /**
     * {@code DELETE  /config-input-table-query-kpis/:id} : delete the "id" configInputTableQueryKpi.
     *
     * @param id the id of the configInputTableQueryKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-input-table-query-kpis/{id}")
    public ResponseEntity<Void> deleteConfigInputTableQueryKpi(@PathVariable Long id) {
        log.debug("REST request to delete ConfigInputTableQueryKpi : {}", id);
        configInputTableQueryKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
