package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ConfigMapGroupChartAreaService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigMapGroupChartAreaDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigMapGroupChartArea}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMapGroupChartAreaResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMapGroupChartAreaResource.class);

    private static final String ENTITY_NAME = "configMapGroupChartArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigMapGroupChartAreaService configMapGroupChartAreaService;

    public ConfigMapGroupChartAreaResource(ConfigMapGroupChartAreaService configMapGroupChartAreaService) {
        this.configMapGroupChartAreaService = configMapGroupChartAreaService;
    }

    /**
     * {@code POST  /config-map-group-chart-areas} : Create a new configMapGroupChartArea.
     *
     * @param configMapGroupChartAreaDTO the configMapGroupChartAreaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMapGroupChartAreaDTO, or with status {@code 400 (Bad Request)} if the configMapGroupChartArea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-map-group-chart-areas")
    public ResponseEntity<ConfigMapGroupChartAreaDTO> createConfigMapGroupChartArea(@RequestBody ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMapGroupChartArea : {}", configMapGroupChartAreaDTO);
        if (configMapGroupChartAreaDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigMapGroupChartAreaDTO result = configMapGroupChartAreaService.save(configMapGroupChartAreaDTO);
        return ResponseEntity.created(new URI("/api/config-map-group-chart-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-map-group-chart-areas} : Updates an existing configMapGroupChartArea.
     *
     * @param configMapGroupChartAreaDTO the configMapGroupChartAreaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMapGroupChartAreaDTO,
     * or with status {@code 400 (Bad Request)} if the configMapGroupChartAreaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMapGroupChartAreaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-map-group-chart-areas")
    public ResponseEntity<ConfigMapGroupChartAreaDTO> updateConfigMapGroupChartArea(@RequestBody ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMapGroupChartArea : {}", configMapGroupChartAreaDTO);
        if (configMapGroupChartAreaDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ConfigMapGroupChartAreaDTO result = configMapGroupChartAreaService.save(configMapGroupChartAreaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMapGroupChartAreaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-map-group-chart-areas} : get all the configMapGroupChartAreas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMapGroupChartAreas in body.
     */
    @GetMapping("/config-map-group-chart-areas")
    public ResponseEntity<List<ConfigMapGroupChartAreaDTO>> getAllConfigMapGroupChartAreas(Long[] groupChartIds,Long[] areaIds, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigMapGroupChartAreas");
        Page<ConfigMapGroupChartAreaDTO> page = configMapGroupChartAreaService.findAll(groupChartIds, areaIds, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-map-group-chart-areas/:id} : get the "id" configMapGroupChartArea.
     *
     * @param id the id of the configMapGroupChartAreaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMapGroupChartAreaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-map-group-chart-areas/{id}")
    public ResponseEntity<ConfigMapGroupChartAreaDTO> getConfigMapGroupChartArea(@PathVariable Long id) {
        log.debug("REST request to get ConfigMapGroupChartArea : {}", id);
        Optional<ConfigMapGroupChartAreaDTO> configMapGroupChartAreaDTO = configMapGroupChartAreaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapGroupChartAreaDTO);
    }

    /**
     * {@code DELETE  /config-map-group-chart-areas/:id} : delete the "id" configMapGroupChartArea.
     *
     * @param id the id of the configMapGroupChartAreaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-map-group-chart-areas/{id}")
    public ResponseEntity<Void> deleteConfigMapGroupChartArea(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMapGroupChartArea : {}", id);
        configMapGroupChartAreaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
