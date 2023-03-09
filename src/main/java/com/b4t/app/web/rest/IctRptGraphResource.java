package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.IctRptGraphService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.IctRptGraphDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.IctRptGraph}.
 */
@RestController
@RequestMapping("/api")
public class IctRptGraphResource {

    private final Logger log = LoggerFactory.getLogger(IctRptGraphResource.class);

    private static final String ENTITY_NAME = "ictRptGraph";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IctRptGraphService ictRptGraphService;

    public IctRptGraphResource(IctRptGraphService ictRptGraphService) {
        this.ictRptGraphService = ictRptGraphService;
    }

    /**
     * {@code POST  /ict-rpt-graphs} : Create a new ictRptGraph.
     *
     * @param ictRptGraphDTO the ictRptGraphDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ictRptGraphDTO, or with status {@code 400 (Bad Request)} if the ictRptGraph has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ict-rpt-graphs")
    public ResponseEntity<IctRptGraphDTO> createIctRptGraph(@RequestBody IctRptGraphDTO ictRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to save IctRptGraph : {}", ictRptGraphDTO);
        if (ictRptGraphDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        IctRptGraphDTO result = ictRptGraphService.save(ictRptGraphDTO);
        return ResponseEntity.created(new URI("/api/ict-rpt-graphs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ict-rpt-graphs} : Updates an existing ictRptGraph.
     *
     * @param ictRptGraphDTO the ictRptGraphDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ictRptGraphDTO,
     * or with status {@code 400 (Bad Request)} if the ictRptGraphDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ictRptGraphDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ict-rpt-graphs")
    public ResponseEntity<IctRptGraphDTO> updateIctRptGraph(@RequestBody IctRptGraphDTO ictRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to update IctRptGraph : {}", ictRptGraphDTO);
        if (ictRptGraphDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        IctRptGraphDTO result = ictRptGraphService.save(ictRptGraphDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ictRptGraphDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ict-rpt-graphs} : get all the ictRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ictRptGraphs in body.
     */
    @GetMapping("/ict-rpt-graphs")
    public ResponseEntity<List<IctRptGraphDTO>> getAllIctRptGraphs(Pageable pageable) {
        log.debug("REST request to get a page of IctRptGraphs");
        Page<IctRptGraphDTO> page = ictRptGraphService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ict-rpt-graphs/:id} : get the "id" ictRptGraph.
     *
     * @param id the id of the ictRptGraphDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ictRptGraphDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ict-rpt-graphs/{id}")
    public ResponseEntity<IctRptGraphDTO> getIctRptGraph(@PathVariable Long id) {
        log.debug("REST request to get IctRptGraph : {}", id);
        Optional<IctRptGraphDTO> ictRptGraphDTO = ictRptGraphService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ictRptGraphDTO);
    }

    /**
     * {@code DELETE  /ict-rpt-graphs/:id} : delete the "id" ictRptGraph.
     *
     * @param id the id of the ictRptGraphDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ict-rpt-graphs/{id}")
    public ResponseEntity<Void> deleteIctRptGraph(@PathVariable Long id) {
        log.debug("REST request to delete IctRptGraph : {}", id);
        ictRptGraphService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
