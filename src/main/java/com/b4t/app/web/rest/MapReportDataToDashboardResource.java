package com.b4t.app.web.rest;

import com.b4t.app.service.MapReportDataToDashboardService;
import com.b4t.app.service.dto.SyncDataToDashboard;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.MapReportDataToDashboardDTO;

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
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.MapReportDataToDashboard}.
 */
@RestController
@RequestMapping("/api")
public class MapReportDataToDashboardResource {

    private final Logger log = LoggerFactory.getLogger(MapReportDataToDashboardResource.class);

    private static final String ENTITY_NAME = "mapReportDataToDashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MapReportDataToDashboardService mapReportDataToDashboardService;

    public MapReportDataToDashboardResource(MapReportDataToDashboardService mapReportDataToDashboardService) {
        this.mapReportDataToDashboardService = mapReportDataToDashboardService;
    }

    /**
     * {@code POST  /map-report-data-to-dashboards} : Create a new mapReportDataToDashboard.
     *
     * @param mapReportDataToDashboardDTO the mapReportDataToDashboardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mapReportDataToDashboardDTO, or with status {@code 400 (Bad Request)} if the mapReportDataToDashboard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/map-report-data-to-dashboards")
    public ResponseEntity<MapReportDataToDashboardDTO> createMapReportDataToDashboard(@RequestBody MapReportDataToDashboardDTO mapReportDataToDashboardDTO) throws URISyntaxException {
        log.debug("REST request to save MapReportDataToDashboard : {}", mapReportDataToDashboardDTO);
        if (mapReportDataToDashboardDTO.getId() != null) {
            throw new BadRequestAlertException("A new mapReportDataToDashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MapReportDataToDashboardDTO result = mapReportDataToDashboardService.save(mapReportDataToDashboardDTO);
        return ResponseEntity.created(new URI("/api/map-report-data-to-dashboards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /map-report-data-to-dashboards} : Updates an existing mapReportDataToDashboard.
     *
     * @param mapReportDataToDashboardDTO the mapReportDataToDashboardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapReportDataToDashboardDTO,
     * or with status {@code 400 (Bad Request)} if the mapReportDataToDashboardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mapReportDataToDashboardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/map-report-data-to-dashboards")
    public ResponseEntity<MapReportDataToDashboardDTO> updateMapReportDataToDashboard(@RequestBody MapReportDataToDashboardDTO mapReportDataToDashboardDTO) throws URISyntaxException {
        log.debug("REST request to update MapReportDataToDashboard : {}", mapReportDataToDashboardDTO);
        if (mapReportDataToDashboardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MapReportDataToDashboardDTO result = mapReportDataToDashboardService.save(mapReportDataToDashboardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mapReportDataToDashboardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /map-report-data-to-dashboards} : get all the mapReportDataToDashboards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mapReportDataToDashboards in body.
     */
    @GetMapping("/map-report-data-to-dashboards")
    public ResponseEntity<List<MapReportDataToDashboardDTO>> getAllMapReportDataToDashboards(Pageable pageable) {
        log.debug("REST request to get a page of MapReportDataToDashboards");
        Page<MapReportDataToDashboardDTO> page = mapReportDataToDashboardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /map-report-data-to-dashboards/:id} : get the "id" mapReportDataToDashboard.
     *
     * @param id the id of the mapReportDataToDashboardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mapReportDataToDashboardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/map-report-data-to-dashboards/{id}")
    public ResponseEntity<MapReportDataToDashboardDTO> getMapReportDataToDashboard(@PathVariable Long id) {
        log.debug("REST request to get MapReportDataToDashboard : {}", id);
        Optional<MapReportDataToDashboardDTO> mapReportDataToDashboardDTO = mapReportDataToDashboardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mapReportDataToDashboardDTO);
    }

    /**
     * {@code DELETE  /map-report-data-to-dashboards/:id} : delete the "id" mapReportDataToDashboard.
     *
     * @param id the id of the mapReportDataToDashboardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/map-report-data-to-dashboards/{id}")
    public ResponseEntity<Void> deleteMapReportDataToDashboard(@PathVariable Long id) {
        log.debug("REST request to delete MapReportDataToDashboard : {}", id);
        mapReportDataToDashboardService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/map-report-data-to-dashboards/syn-data-to-dashboard")
    public ResponseEntity synDataToDashboard(@RequestBody SyncDataToDashboard syncDataToDashboard) throws ParseException {
        mapReportDataToDashboardService.synDataToDashboard(syncDataToDashboard);
        return ResponseEntity.ok().build();
    }
}
