package com.b4t.app.web.rest;

import com.b4t.app.service.ChartDescriptionService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ChartDescriptionDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.ChartDescription}.
 */
@RestController
@RequestMapping("/api")
public class ChartDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(ChartDescriptionResource.class);

    private static final String ENTITY_NAME = "chartDescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChartDescriptionService chartDescriptionService;

    public ChartDescriptionResource(ChartDescriptionService chartDescriptionService) {
        this.chartDescriptionService = chartDescriptionService;
    }

//    /**
//     * {@code POST  /chart-descriptions} : Create a new chartDescription.
//     *
//     * @param chartDescriptionDTO the chartDescriptionDTO to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chartDescriptionDTO, or with status {@code 400 (Bad Request)} if the chartDescription has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("/chart-descriptions")
//    public ResponseEntity<ChartDescriptionDTO> createChartDescription(@RequestBody ChartDescriptionDTO chartDescriptionDTO) throws URISyntaxException {
//        log.debug("REST request to save ChartDescription : {}", chartDescriptionDTO);
//        if (chartDescriptionDTO.getId() != null) {
//            throw new BadRequestAlertException("A new chartDescription cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        ChartDescriptionDTO result = chartDescriptionService.createNew(chartDescriptionDTO);
//        return ResponseEntity.created(new URI("/api/chart-descriptions/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code PUT  /chart-descriptions} : Updates an existing chartDescription.
     *
     * @param chartDescriptionDTO the chartDescriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartDescriptionDTO,
     * or with status {@code 400 (Bad Request)} if the chartDescriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chartDescriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chart-descriptions")
    public ResponseEntity<?> saveChartDescription(@RequestBody ChartDescriptionDTO chartDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to update ChartDescription : {}", chartDescriptionDTO);
        ChartDescriptionDTO result = chartDescriptionService.save(chartDescriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chart-descriptions} : get all the chartDescriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chartDescriptions in body.
     */
    @GetMapping("/chart-descriptions")
    public ResponseEntity<List<ChartDescriptionDTO>> getAllChartDescriptions(@RequestParam(value = "chartId") Long chartId, @PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        log.debug("REST request to get a page of ChartDescriptions");
        Page<ChartDescriptionDTO> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
        if(chartId != null){
            page = chartDescriptionService.findAllByChartId(chartId, pageable);
        }else{
            page = chartDescriptionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chart-descriptions/:id} : get the "id" chartDescription.
     *
     * @param id the id of the chartDescriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chartDescriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chart-descriptions/{id}")
    public ResponseEntity<ChartDescriptionDTO> getChartDescription(@PathVariable Long id) {
        log.debug("REST request to get ChartDescription : {}", id);
        Optional<ChartDescriptionDTO> chartDescriptionDTO = chartDescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chartDescriptionDTO);
    }

    /**
     * {@code DELETE  /chart-descriptions/:id} : delete the "id" chartDescription.
     *
     * @param id the id of the chartDescriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chart-descriptions/{id}")
    public ResponseEntity<Void> deleteChartDescription(@PathVariable Long id) {
        log.debug("REST request to delete ChartDescription : {}", id);
        chartDescriptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
