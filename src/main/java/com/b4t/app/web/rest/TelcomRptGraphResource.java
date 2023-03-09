package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.TelcomRptGraphService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.TelcomRptGraphDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.TelcomRptGraph}.
 */
@RestController
@RequestMapping("/api")
public class TelcomRptGraphResource {

    private final Logger log = LoggerFactory.getLogger(TelcomRptGraphResource.class);

    private static final String ENTITY_NAME = "telcomRptGraph";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TelcomRptGraphService telcomRptGraphService;

    public TelcomRptGraphResource(TelcomRptGraphService telcomRptGraphService) {
        this.telcomRptGraphService = telcomRptGraphService;
    }

    /**
     * {@code POST  /telcom-rpt-graphs} : Create a new telcomRptGraph.
     *
     * @param telcomRptGraphDTO the telcomRptGraphDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new telcomRptGraphDTO, or with status {@code 400 (Bad Request)} if the telcomRptGraph has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/telcom-rpt-graphs")
    public ResponseEntity<TelcomRptGraphDTO> createTelcomRptGraph(@RequestBody TelcomRptGraphDTO telcomRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to save TelcomRptGraph : {}", telcomRptGraphDTO);
        if (telcomRptGraphDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        TelcomRptGraphDTO result = telcomRptGraphService.save(telcomRptGraphDTO);
        return ResponseEntity.created(new URI("/api/telcom-rpt-graphs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /telcom-rpt-graphs} : Updates an existing telcomRptGraph.
     *
     * @param telcomRptGraphDTO the telcomRptGraphDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telcomRptGraphDTO,
     * or with status {@code 400 (Bad Request)} if the telcomRptGraphDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the telcomRptGraphDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/telcom-rpt-graphs")
    public ResponseEntity<TelcomRptGraphDTO> updateTelcomRptGraph(@RequestBody TelcomRptGraphDTO telcomRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to update TelcomRptGraph : {}", telcomRptGraphDTO);
        if (telcomRptGraphDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        TelcomRptGraphDTO result = telcomRptGraphService.save(telcomRptGraphDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, telcomRptGraphDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /telcom-rpt-graphs} : get all the telcomRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of telcomRptGraphs in body.
     */
    @GetMapping("/telcom-rpt-graphs")
    public ResponseEntity<List<TelcomRptGraphDTO>> getAllTelcomRptGraphs(Pageable pageable) {
        log.debug("REST request to get a page of TelcomRptGraphs");
        Page<TelcomRptGraphDTO> page = telcomRptGraphService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /telcom-rpt-graphs/:id} : get the "id" telcomRptGraph.
     *
     * @param id the id of the telcomRptGraphDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the telcomRptGraphDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/telcom-rpt-graphs/{id}")
    public ResponseEntity<TelcomRptGraphDTO> getTelcomRptGraph(@PathVariable Long id) {
        log.debug("REST request to get TelcomRptGraph : {}", id);
        Optional<TelcomRptGraphDTO> telcomRptGraphDTO = telcomRptGraphService.findOne(id);
        return ResponseUtil.wrapOrNotFound(telcomRptGraphDTO);
    }

    /**
     * {@code DELETE  /telcom-rpt-graphs/:id} : delete the "id" telcomRptGraph.
     *
     * @param id the id of the telcomRptGraphDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/telcom-rpt-graphs/{id}")
    public ResponseEntity<Void> deleteTelcomRptGraph(@PathVariable Long id) {
        log.debug("REST request to delete TelcomRptGraph : {}", id);
        telcomRptGraphService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
