package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.ItRptGraphService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ItRptGraphDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ItRptGraph}.
 */
@RestController
@RequestMapping("/api")
public class ItRptGraphResource {

    private final Logger log = LoggerFactory.getLogger(ItRptGraphResource.class);

    private static final String ENTITY_NAME = "itRptGraph";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItRptGraphService itRptGraphService;

    public ItRptGraphResource(ItRptGraphService itRptGraphService) {
        this.itRptGraphService = itRptGraphService;
    }

    /**
     * {@code POST  /it-rpt-graphs} : Create a new itRptGraph.
     *
     * @param itRptGraphDTO the itRptGraphDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itRptGraphDTO, or with status {@code 400 (Bad Request)} if the itRptGraph has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/it-rpt-graphs")
    public ResponseEntity<ItRptGraphDTO> createItRptGraph(@RequestBody ItRptGraphDTO itRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to save ItRptGraph : {}", itRptGraphDTO);
        if (itRptGraphDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ItRptGraphDTO result = itRptGraphService.save(itRptGraphDTO);
        return ResponseEntity.created(new URI("/api/it-rpt-graphs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /it-rpt-graphs} : Updates an existing itRptGraph.
     *
     * @param itRptGraphDTO the itRptGraphDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itRptGraphDTO,
     * or with status {@code 400 (Bad Request)} if the itRptGraphDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itRptGraphDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/it-rpt-graphs")
    public ResponseEntity<ItRptGraphDTO> updateItRptGraph(@RequestBody ItRptGraphDTO itRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to update ItRptGraph : {}", itRptGraphDTO);
        if (itRptGraphDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        ItRptGraphDTO result = itRptGraphService.save(itRptGraphDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itRptGraphDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /it-rpt-graphs} : get all the itRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itRptGraphs in body.
     */
    @GetMapping("/it-rpt-graphs")
    public ResponseEntity<List<ItRptGraphDTO>> getAllItRptGraphs(Pageable pageable) {
        log.debug("REST request to get a page of ItRptGraphs");
        Page<ItRptGraphDTO> page = itRptGraphService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /it-rpt-graphs/:id} : get the "id" itRptGraph.
     *
     * @param id the id of the itRptGraphDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itRptGraphDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/it-rpt-graphs/{id}")
    public ResponseEntity<ItRptGraphDTO> getItRptGraph(@PathVariable Long id) {
        log.debug("REST request to get ItRptGraph : {}", id);
        Optional<ItRptGraphDTO> itRptGraphDTO = itRptGraphService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itRptGraphDTO);
    }

    /**
     * {@code DELETE  /it-rpt-graphs/:id} : delete the "id" itRptGraph.
     *
     * @param id the id of the itRptGraphDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/it-rpt-graphs/{id}")
    public ResponseEntity<Void> deleteItRptGraph(@PathVariable Long id) {
        log.debug("REST request to delete ItRptGraph : {}", id);
        itRptGraphService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
