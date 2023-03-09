package com.b4t.app.web.rest;

import com.b4t.app.service.ConfigReportColumnService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigReportColumnDTO;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigReportColumn}.
 */
@RestController
@RequestMapping("/api")
public class ConfigReportColumnResource {

    private final Logger log = LoggerFactory.getLogger(ConfigReportColumnResource.class);

    private static final String ENTITY_NAME = "configReportColumn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigReportColumnService configReportColumnService;

    public ConfigReportColumnResource(ConfigReportColumnService configReportColumnService) {
        this.configReportColumnService = configReportColumnService;
    }

    /**
     * {@code POST  /config-report-columns} : Create a new configReportColumn.
     *
     * @param configReportColumnDTO the configReportColumnDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configReportColumnDTO, or with status {@code 400 (Bad Request)} if the configReportColumn has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-report-columns")
    public ResponseEntity<ConfigReportColumnDTO> createConfigReportColumn(@Valid @RequestBody ConfigReportColumnDTO configReportColumnDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigReportColumn : {}", configReportColumnDTO);
        if (configReportColumnDTO.getId() != null) {
            throw new BadRequestAlertException("A new configReportColumn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigReportColumnDTO result = configReportColumnService.save(configReportColumnDTO);
        return ResponseEntity.created(new URI("/api/config-report-columns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-report-columns} : Updates an existing configReportColumn.
     *
     * @param configReportColumnDTO the configReportColumnDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configReportColumnDTO,
     * or with status {@code 400 (Bad Request)} if the configReportColumnDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configReportColumnDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-report-columns")
    public ResponseEntity<ConfigReportColumnDTO> updateConfigReportColumn(@Valid @RequestBody ConfigReportColumnDTO configReportColumnDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigReportColumn : {}", configReportColumnDTO);
        if (configReportColumnDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigReportColumnDTO result = configReportColumnService.save(configReportColumnDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configReportColumnDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-report-columns} : get all the configReportColumns.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configReportColumns in body.
     */
    @GetMapping("/config-report-columns")
    public ResponseEntity<List<ConfigReportColumnDTO>> getAllConfigReportColumns(Pageable pageable) {
        log.debug("REST request to get a page of ConfigReportColumns");
        Page<ConfigReportColumnDTO> page = configReportColumnService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-report-columns/:id} : get the "id" configReportColumn.
     *
     * @param id the id of the configReportColumnDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configReportColumnDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-report-columns/{id}")
    public ResponseEntity<ConfigReportColumnDTO> getConfigReportColumn(@PathVariable Long id) {
        log.debug("REST request to get ConfigReportColumn : {}", id);
        Optional<ConfigReportColumnDTO> configReportColumnDTO = configReportColumnService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configReportColumnDTO);
    }

    /**
     * {@code DELETE  /config-report-columns/:id} : delete the "id" configReportColumn.
     *
     * @param id the id of the configReportColumnDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-report-columns/{id}")
    public ResponseEntity<Void> deleteConfigReportColumn(@PathVariable Long id) {
        log.debug("REST request to delete ConfigReportColumn : {}", id);
        configReportColumnService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
