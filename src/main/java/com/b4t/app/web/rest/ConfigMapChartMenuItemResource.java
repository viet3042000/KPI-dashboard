package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ConfigMapChartMenuItemService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigMapChartMenuItem}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMapChartMenuItemResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartMenuItemResource.class);

    private static final String ENTITY_NAME = "configMapChartMenuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigMapChartMenuItemService configMapChartMenuItemService;

    public ConfigMapChartMenuItemResource(ConfigMapChartMenuItemService configMapChartMenuItemService) {
        this.configMapChartMenuItemService = configMapChartMenuItemService;
    }

    /**
     * {@code POST  /config-map-chart-menu-items} : Create a new configMapChartMenuItem.
     *
     * @param configMapChartMenuItemDTO the configMapChartMenuItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMapChartMenuItemDTO, or with status {@code 400 (Bad Request)} if the configMapChartMenuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-map-chart-menu-items")
    public ResponseEntity<ConfigMapChartMenuItemDTO> createConfigMapChartMenuItem(@RequestBody ConfigMapChartMenuItemDTO configMapChartMenuItemDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMapChartMenuItem : {}", configMapChartMenuItemDTO);
        if (configMapChartMenuItemDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigMapChartMenuItemDTO result = configMapChartMenuItemService.save(configMapChartMenuItemDTO);
        return ResponseEntity.created(new URI("/api/config-map-chart-menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-map-chart-menu-items} : Updates an existing configMapChartMenuItem.
     *
     * @param configMapChartMenuItemDTO the configMapChartMenuItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMapChartMenuItemDTO,
     * or with status {@code 400 (Bad Request)} if the configMapChartMenuItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMapChartMenuItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-map-chart-menu-items")
    public ResponseEntity<ConfigMapChartMenuItemDTO> updateConfigMapChartMenuItem(@RequestBody ConfigMapChartMenuItemDTO configMapChartMenuItemDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMapChartMenuItem : {}", configMapChartMenuItemDTO);
        if (configMapChartMenuItemDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ConfigMapChartMenuItemDTO result = configMapChartMenuItemService.save(configMapChartMenuItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMapChartMenuItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-map-chart-menu-items} : get all the configMapChartMenuItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMapChartMenuItems in body.
     */
    @GetMapping("/config-map-chart-menu-items")
    public ResponseEntity<List<ConfigMapChartMenuItemDTO>> getAllConfigMapChartMenuItems(Long[] chartIds, Long[] menuItemIds, Long isDefault, Long status,Pageable pageable) {
        log.debug("REST request to get a page of ConfigMapChartMenuItems");
        Page<ConfigMapChartMenuItemDTO> page = configMapChartMenuItemService.findAll(chartIds, menuItemIds, isDefault, status,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-map-chart-menu-items/:id} : get the "id" configMapChartMenuItem.
     *
     * @param id the id of the configMapChartMenuItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMapChartMenuItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-map-chart-menu-items/{id}")
    public ResponseEntity<ConfigMapChartMenuItemDTO> getConfigMapChartMenuItem(@PathVariable Long id) {
        log.debug("REST request to get ConfigMapChartMenuItem : {}", id);
        Optional<ConfigMapChartMenuItemDTO> configMapChartMenuItemDTO = configMapChartMenuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapChartMenuItemDTO);
    }

    /**
     * {@code DELETE  /config-map-chart-menu-items/:id} : delete the "id" configMapChartMenuItem.
     *
     * @param id the id of the configMapChartMenuItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-map-chart-menu-items/{id}")
    public ResponseEntity<Void> deleteConfigMapChartMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMapChartMenuItem : {}", id);
        configMapChartMenuItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
