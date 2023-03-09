package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ConfigMapChartAreaService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigMapChartAreaDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigMapChartArea}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMapChartAreaResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartAreaResource.class);

    private static final String ENTITY_NAME = "configMapChartArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigMapChartAreaService configMapChartAreaService;

    public ConfigMapChartAreaResource(ConfigMapChartAreaService configMapChartAreaService) {
        this.configMapChartAreaService = configMapChartAreaService;
    }

    /**
     * {@code POST  /config-map-chart-areas} : Create a new configMapChartArea.
     *
     * @param configMapChartAreaDTO the configMapChartAreaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMapChartAreaDTO, or with status {@code 400 (Bad Request)} if the configMapChartArea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-map-chart-areas")
    public ResponseEntity<ConfigMapChartAreaDTO> createConfigMapChartArea(@RequestBody ConfigMapChartAreaDTO configMapChartAreaDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMapChartArea : {}", configMapChartAreaDTO);
        if (configMapChartAreaDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigMapChartAreaDTO result = configMapChartAreaService.save(configMapChartAreaDTO);
        return ResponseEntity.created(new URI("/api/config-map-chart-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-map-chart-areas} : Updates an existing configMapChartArea.
     *
     * @param configMapChartAreaDTO the configMapChartAreaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMapChartAreaDTO,
     * or with status {@code 400 (Bad Request)} if the configMapChartAreaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMapChartAreaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-map-chart-areas")
    public ResponseEntity<ConfigMapChartAreaDTO> updateConfigMapChartArea(@RequestBody ConfigMapChartAreaDTO configMapChartAreaDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMapChartArea : {}", configMapChartAreaDTO);
        if (configMapChartAreaDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ConfigMapChartAreaDTO result = configMapChartAreaService.save(configMapChartAreaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMapChartAreaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-map-chart-areas} : get all the configMapChartAreas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMapChartAreas in body.
     */
    @GetMapping("/config-map-chart-areas")
    public ResponseEntity<List<ConfigMapChartAreaDTO>> getAllConfigMapChartAreas(Long[] chartIds, Long[] areaIds, Long[] groupChartIds, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigMapChartAreas");
        Page<ConfigMapChartAreaDTO> page = configMapChartAreaService.findAll(chartIds, areaIds, groupChartIds, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-map-chart-areas/:id} : get the "id" configMapChartArea.
     *
     * @param id the id of the configMapChartAreaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMapChartAreaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-map-chart-areas/{id}")
    public ResponseEntity<ConfigMapChartAreaDTO> getConfigMapChartArea(@PathVariable Long id) {
        log.debug("REST request to get ConfigMapChartArea : {}", id);
        Optional<ConfigMapChartAreaDTO> configMapChartAreaDTO = configMapChartAreaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapChartAreaDTO);
    }

    /**
     * {@code DELETE  /config-map-chart-areas/:id} : delete the "id" configMapChartArea.
     *
     * @param id the id of the configMapChartAreaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-map-chart-areas/{id}")
    public ResponseEntity<Void> deleteConfigMapChartArea(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMapChartArea : {}", id);
        configMapChartAreaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
