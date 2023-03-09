package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.SecurRptGraphService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.SecurRptGraphDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.SecurRptGraph}.
 */
@RestController
@RequestMapping("/api")
public class SecurRptGraphResource {

    private final Logger log = LoggerFactory.getLogger(SecurRptGraphResource.class);

    private static final String ENTITY_NAME = "securRptGraph";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurRptGraphService securRptGraphService;

    public SecurRptGraphResource(SecurRptGraphService securRptGraphService) {
        this.securRptGraphService = securRptGraphService;
    }

    /**
     * {@code POST  /secur-rpt-graphs} : Create a new securRptGraph.
     *
     * @param securRptGraphDTO the securRptGraphDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securRptGraphDTO, or with status {@code 400 (Bad Request)} if the securRptGraph has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secur-rpt-graphs")
    public ResponseEntity<SecurRptGraphDTO> createSecurRptGraph(@RequestBody SecurRptGraphDTO securRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to save SecurRptGraph : {}", securRptGraphDTO);
        if (securRptGraphDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        SecurRptGraphDTO result = securRptGraphService.save(securRptGraphDTO);
        return ResponseEntity.created(new URI("/api/secur-rpt-graphs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /secur-rpt-graphs} : Updates an existing securRptGraph.
     *
     * @param securRptGraphDTO the securRptGraphDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securRptGraphDTO,
     * or with status {@code 400 (Bad Request)} if the securRptGraphDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securRptGraphDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secur-rpt-graphs")
    public ResponseEntity<SecurRptGraphDTO> updateSecurRptGraph(@RequestBody SecurRptGraphDTO securRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to update SecurRptGraph : {}", securRptGraphDTO);
        if (securRptGraphDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        SecurRptGraphDTO result = securRptGraphService.save(securRptGraphDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securRptGraphDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /secur-rpt-graphs} : get all the securRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securRptGraphs in body.
     */
    @GetMapping("/secur-rpt-graphs")
    public ResponseEntity<List<SecurRptGraphDTO>> getAllSecurRptGraphs(Pageable pageable) {
        log.debug("REST request to get a page of SecurRptGraphs");
        Page<SecurRptGraphDTO> page = securRptGraphService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /secur-rpt-graphs/:id} : get the "id" securRptGraph.
     *
     * @param id the id of the securRptGraphDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securRptGraphDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/secur-rpt-graphs/{id}")
    public ResponseEntity<SecurRptGraphDTO> getSecurRptGraph(@PathVariable Long id) {
        log.debug("REST request to get SecurRptGraph : {}", id);
        Optional<SecurRptGraphDTO> securRptGraphDTO = securRptGraphService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securRptGraphDTO);
    }

    /**
     * {@code DELETE  /secur-rpt-graphs/:id} : delete the "id" securRptGraph.
     *
     * @param id the id of the securRptGraphDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secur-rpt-graphs/{id}")
    public ResponseEntity<Void> deleteSecurRptGraph(@PathVariable Long id) {
        log.debug("REST request to delete SecurRptGraph : {}", id);
        securRptGraphService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
