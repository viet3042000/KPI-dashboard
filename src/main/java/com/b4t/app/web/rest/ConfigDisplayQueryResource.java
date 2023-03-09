package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ConfigDisplayQueryService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigDisplayQueryDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigDisplayQuery}.
 */
@RestController
@RequestMapping("/api")
public class ConfigDisplayQueryResource {

    private final Logger log = LoggerFactory.getLogger(ConfigDisplayQueryResource.class);

    private static final String ENTITY_NAME = "configDisplayQuery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigDisplayQueryService configDisplayQueryService;

    public ConfigDisplayQueryResource(ConfigDisplayQueryService configDisplayQueryService) {
        this.configDisplayQueryService = configDisplayQueryService;
    }

    /**
     * {@code POST  /config-display-queries} : Create a new configDisplayQuery.
     *
     * @param configDisplayQueryDTO the configDisplayQueryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configDisplayQueryDTO, or with status {@code 400 (Bad Request)} if the configDisplayQuery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-display-queries")
    public ResponseEntity<ConfigDisplayQueryDTO> createConfigDisplayQuery(@RequestBody ConfigDisplayQueryDTO configDisplayQueryDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigDisplayQuery : {}", configDisplayQueryDTO);
        if (configDisplayQueryDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigDisplayQueryDTO result = configDisplayQueryService.save(configDisplayQueryDTO);
        return ResponseEntity.created(new URI("/api/config-display-queries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-display-queries} : Updates an existing configDisplayQuery.
     *
     * @param configDisplayQueryDTO the configDisplayQueryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configDisplayQueryDTO,
     * or with status {@code 400 (Bad Request)} if the configDisplayQueryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configDisplayQueryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-display-queries")
    public ResponseEntity<ConfigDisplayQueryDTO> updateConfigDisplayQuery(@RequestBody ConfigDisplayQueryDTO configDisplayQueryDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigDisplayQuery : {}", configDisplayQueryDTO);
        if (configDisplayQueryDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ConfigDisplayQueryDTO result = configDisplayQueryService.save(configDisplayQueryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configDisplayQueryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-display-queries} : get all the configDisplayQueries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configDisplayQueries in body.
     */
    @GetMapping("/config-display-queries")
    public ResponseEntity<List<ConfigDisplayQueryDTO>> getAllConfigDisplayQueries(Pageable pageable) {
        log.debug("REST request to get a page of ConfigDisplayQueries");
        Page<ConfigDisplayQueryDTO> page = configDisplayQueryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-display-queries/:id} : get the "id" configDisplayQuery.
     *
     * @param id the id of the configDisplayQueryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configDisplayQueryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-display-queries/{id}")
    public ResponseEntity<ConfigDisplayQueryDTO> getConfigDisplayQuery(@PathVariable Long id) {
        log.debug("REST request to get ConfigDisplayQuery : {}", id);
        Optional<ConfigDisplayQueryDTO> configDisplayQueryDTO = configDisplayQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configDisplayQueryDTO);
    }

    /**
     * {@code DELETE  /config-display-queries/:id} : delete the "id" configDisplayQuery.
     *
     * @param id the id of the configDisplayQueryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-display-queries/{id}")
    public ResponseEntity<Void> deleteConfigDisplayQuery(@PathVariable Long id) {
        log.debug("REST request to delete ConfigDisplayQuery : {}", id);
        configDisplayQueryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
