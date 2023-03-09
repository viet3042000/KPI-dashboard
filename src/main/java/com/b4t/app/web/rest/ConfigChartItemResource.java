package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ConfigChartItemService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigChartItemDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigChartItem}.
 */
@RestController
@RequestMapping("/api")
public class ConfigChartItemResource {

    private final Logger log = LoggerFactory.getLogger(ConfigChartItemResource.class);

    private static final String ENTITY_NAME = "configChartItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigChartItemService configChartItemService;

    public ConfigChartItemResource(ConfigChartItemService configChartItemService) {
        this.configChartItemService = configChartItemService;
    }

    /**
     * {@code POST  /config-chart-items} : Create a new configChartItem.
     *
     * @param configChartItemDTO the configChartItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configChartItemDTO, or with status {@code 400 (Bad Request)} if the configChartItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-chart-items")
    public ResponseEntity<ConfigChartItemDTO> createConfigChartItem(@RequestBody ConfigChartItemDTO configChartItemDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigChartItem : {}", configChartItemDTO);
        if (configChartItemDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigChartItemDTO result = configChartItemService.save(configChartItemDTO);
        return ResponseEntity.created(new URI("/api/config-chart-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-chart-items} : Updates an existing configChartItem.
     *
     * @param configChartItemDTO the configChartItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configChartItemDTO,
     * or with status {@code 400 (Bad Request)} if the configChartItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configChartItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-chart-items")
    public ResponseEntity<ConfigChartItemDTO> updateConfigChartItem(@RequestBody ConfigChartItemDTO configChartItemDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigChartItem : {}", configChartItemDTO);
        if (configChartItemDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ConfigChartItemDTO result = configChartItemService.save(configChartItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configChartItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-chart-items} : get all the configChartItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configChartItems in body.
     */
    @GetMapping("/config-chart-items")
    public ResponseEntity<List<ConfigChartItemDTO>> getAllConfigChartItems(Pageable pageable) {
        log.debug("REST request to get a page of ConfigChartItems");
        Page<ConfigChartItemDTO> page = configChartItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-chart-items/:id} : get the "id" configChartItem.
     *
     * @param id the id of the configChartItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configChartItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-chart-items/{id}")
    public ResponseEntity<ConfigChartItemDTO> getConfigChartItem(@PathVariable Long id) {
        log.debug("REST request to get ConfigChartItem : {}", id);
        Optional<ConfigChartItemDTO> configChartItemDTO = configChartItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configChartItemDTO);
    }

    /**
     * {@code DELETE  /config-chart-items/:id} : delete the "id" configChartItem.
     *
     * @param id the id of the configChartItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-chart-items/{id}")
    public ResponseEntity<Void> deleteConfigChartItem(@PathVariable Long id) {
        log.debug("REST request to delete ConfigChartItem : {}", id);
        configChartItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
