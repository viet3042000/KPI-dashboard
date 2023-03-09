package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.service.ConfigQueryChartService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigQueryChartDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigQueryChart}.
 */
@RestController
@RequestMapping("/api")
public class ConfigQueryChartResource {

    private final Logger log = LoggerFactory.getLogger(ConfigQueryChartResource.class);

    private static final String ENTITY_NAME = "configQueryChart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigQueryChartService configQueryChartService;

    public ConfigQueryChartResource(ConfigQueryChartService configQueryChartService) {
        this.configQueryChartService = configQueryChartService;
    }

    /**
     * {@code POST  /config-query-charts} : Create a new configQueryChart.
     *
     * @param configQueryChartDTO the configQueryChartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configQueryChartDTO, or with status {@code 400 (Bad Request)} if the configQueryChart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-query-charts")
    public ResponseEntity<ConfigQueryChartDTO> createConfigQueryChart(@RequestBody ConfigQueryChartDTO configQueryChartDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigQueryChart : {}", configQueryChartDTO);
        if (configQueryChartDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigQueryChartDTO result = configQueryChartService.save(configQueryChartDTO);
        return ResponseEntity.created(new URI("/api/config-query-charts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-query-charts} : Updates an existing configQueryChart.
     *
     * @param configQueryChartDTO the configQueryChartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configQueryChartDTO,
     * or with status {@code 400 (Bad Request)} if the configQueryChartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configQueryChartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-query-charts")
    public ResponseEntity<ConfigQueryChartDTO> updateConfigQueryChart(@RequestBody ConfigQueryChartDTO configQueryChartDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigQueryChart : {}", configQueryChartDTO);
        if (configQueryChartDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale(Constants.VALIDATE.ID_NULL), ENTITY_NAME, Constants.VALIDATE.ID_NULL);
        }
        ConfigQueryChartDTO result = configQueryChartService.save(configQueryChartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configQueryChartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-query-charts} : get all the configQueryCharts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configQueryCharts in body.
     */
    @GetMapping("/config-query-charts")
    public ResponseEntity<List<ConfigQueryChartDTO>> getAllConfigQueryCharts(Pageable pageable) {
        log.debug("REST request to get a page of ConfigQueryCharts");
        Page<ConfigQueryChartDTO> page = configQueryChartService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-query-charts/:id} : get the "id" configQueryChart.
     *
     * @param id the id of the configQueryChartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configQueryChartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-query-charts/{id}")
    public ResponseEntity<ConfigQueryChartDTO> getConfigQueryChart(@PathVariable Long id) {
        log.debug("REST request to get ConfigQueryChart : {}", id);
        Optional<ConfigQueryChartDTO> configQueryChartDTO = configQueryChartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configQueryChartDTO);
    }

    /**
     * {@code DELETE  /config-query-charts/:id} : delete the "id" configQueryChart.
     *
     * @param id the id of the configQueryChartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-query-charts/{id}")
    public ResponseEntity<Void> deleteConfigQueryChart(@PathVariable Long id) {
        log.debug("REST request to delete ConfigQueryChart : {}", id);
        configQueryChartService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
