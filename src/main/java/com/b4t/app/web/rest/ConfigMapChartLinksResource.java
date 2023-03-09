package com.b4t.app.web.rest;

import com.b4t.app.service.ConfigMapChartLinksService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigMapChartLinksDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigMapChartLinks}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMapChartLinksResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartLinksResource.class);

    private static final String ENTITY_NAME = "configMapChartLinks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigMapChartLinksService configMapChartLinksService;

    public ConfigMapChartLinksResource(ConfigMapChartLinksService configMapChartLinksService) {
        this.configMapChartLinksService = configMapChartLinksService;
    }

    /**
     * {@code POST  /config-map-chart-links} : Create a new configMapChartLinks.
     *
     * @param configMapChartLinksDTO the configMapChartLinksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMapChartLinksDTO, or with status {@code 400 (Bad Request)} if the configMapChartLinks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-map-chart-links")
    public ResponseEntity<ConfigMapChartLinksDTO> createConfigMapChartLinks(@RequestBody ConfigMapChartLinksDTO configMapChartLinksDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMapChartLinks : {}", configMapChartLinksDTO);
        if (configMapChartLinksDTO.getId() != null) {
            throw new BadRequestAlertException("A new configMapChartLinks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigMapChartLinksDTO result = configMapChartLinksService.save(configMapChartLinksDTO);
        return ResponseEntity.created(new URI("/api/config-map-chart-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-map-chart-links} : Updates an existing configMapChartLinks.
     *
     * @param configMapChartLinksDTO the configMapChartLinksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMapChartLinksDTO,
     * or with status {@code 400 (Bad Request)} if the configMapChartLinksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMapChartLinksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-map-chart-links")
    public ResponseEntity<ConfigMapChartLinksDTO> updateConfigMapChartLinks(@RequestBody ConfigMapChartLinksDTO configMapChartLinksDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMapChartLinks : {}", configMapChartLinksDTO);
        if (configMapChartLinksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigMapChartLinksDTO result = configMapChartLinksService.save(configMapChartLinksDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMapChartLinksDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-map-chart-links} : get all the configMapChartLinks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMapChartLinks in body.
     */
    @GetMapping("/config-map-chart-links")
    public ResponseEntity<List<ConfigMapChartLinksDTO>> getAllConfigMapChartLinks(Pageable pageable) {
        log.debug("REST request to get a page of ConfigMapChartLinks");
        Page<ConfigMapChartLinksDTO> page = configMapChartLinksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-map-chart-links/:id} : get the "id" configMapChartLinks.
     *
     * @param id the id of the configMapChartLinksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMapChartLinksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-map-chart-links/{id}")
    public ResponseEntity<ConfigMapChartLinksDTO> getConfigMapChartLinks(@PathVariable Long id) {
        log.debug("REST request to get ConfigMapChartLinks : {}", id);
        Optional<ConfigMapChartLinksDTO> configMapChartLinksDTO = configMapChartLinksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapChartLinksDTO);
    }

    /**
     * {@code DELETE  /config-map-chart-links/:id} : delete the "id" configMapChartLinks.
     *
     * @param id the id of the configMapChartLinksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-map-chart-links/{id}")
    public ResponseEntity<Void> deleteConfigMapChartLinks(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMapChartLinks : {}", id);
        configMapChartLinksService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/config-map-chart-links/delete-by-chart-map/{chartMapId}")
    public ResponseEntity<Void> deleteAllByChartMapId(@PathVariable Long chartMapId) {
        log.debug("REST request to delete ConfigMapChartLinks : {}", chartMapId);
        configMapChartLinksService.deleteAllByChartMapId(chartMapId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, chartMapId.toString())).build();
    }
}
