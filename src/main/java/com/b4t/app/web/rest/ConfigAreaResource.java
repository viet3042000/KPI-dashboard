package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ConfigAreaService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigAreaDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigArea}.
 */
@RestController
@RequestMapping("/api")
public class ConfigAreaResource {

    private final Logger log = LoggerFactory.getLogger(ConfigAreaResource.class);

    private static final String ENTITY_NAME = "configArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigAreaService configAreaService;

    public ConfigAreaResource(ConfigAreaService configAreaService) {
        this.configAreaService = configAreaService;
    }

    /**
     * {@code POST  /config-areas} : Create a new configArea.
     *
     * @param configAreaDTO the configAreaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configAreaDTO, or with status {@code 400 (Bad Request)} if the configArea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-areas")
    public ResponseEntity<ConfigAreaDTO> createConfigArea(@RequestBody ConfigAreaDTO configAreaDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigArea : {}", configAreaDTO);
        if (configAreaDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigAreaDTO result = configAreaService.save(configAreaDTO);
        return ResponseEntity.created(new URI("/api/config-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-areas} : Updates an existing configArea.
     *
     * @param configAreaDTO the configAreaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configAreaDTO,
     * or with status {@code 400 (Bad Request)} if the configAreaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configAreaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-areas")
    public ResponseEntity<ConfigAreaDTO> updateConfigArea(@RequestBody ConfigAreaDTO configAreaDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigArea : {}", configAreaDTO);
        if (configAreaDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ConfigAreaDTO result = configAreaService.save(configAreaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configAreaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-areas} : get all the configAreas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configAreas in body.
     */
    @GetMapping("/config-areas")
    public ResponseEntity<List<ConfigAreaDTO>> getAllConfigAreas(String keyword, Long[] screenIds, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigAreas");
        Page<ConfigAreaDTO> page = configAreaService.findAll(keyword, screenIds, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-areas/:id} : get the "id" configArea.
     *
     * @param id the id of the configAreaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configAreaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-areas/{id}")
    public ResponseEntity<ConfigAreaDTO> getConfigArea(@PathVariable Long id) {
        log.debug("REST request to get ConfigArea : {}", id);
        Optional<ConfigAreaDTO> configAreaDTO = configAreaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configAreaDTO);
    }

    /**
     * {@code DELETE  /config-areas/:id} : delete the "id" configArea.
     *
     * @param id the id of the configAreaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-areas/{id}")
    public ResponseEntity<Void> deleteConfigArea(@PathVariable Long id) {
        log.debug("REST request to delete ConfigArea : {}", id);
        configAreaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
