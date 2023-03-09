package com.b4t.app.web.rest;

import com.b4t.app.service.CatKpiSynonymService;
import com.b4t.app.service.dto.CatKpiSynonymDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.CatKpiSynonym}.
 */
@RestController
@RequestMapping("/api")
public class CatKpiSynonymResource {

    private final Logger log = LoggerFactory.getLogger(CatKpiSynonymResource.class);

    private static final String ENTITY_NAME = "catKpiSynonym";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatKpiSynonymService catKpiSynonymService;

    public CatKpiSynonymResource(CatKpiSynonymService catKpiSynonymService) {
        this.catKpiSynonymService = catKpiSynonymService;
    }

    /**
     * {@code POST  /cat-kpi-synonyms} : Create a new catKpiSynonym.
     *
     * @param catKpiSynonymDTO the catKpiSynonymDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catKpiSynonymDTO, or with status {@code 400 (Bad Request)} if the catKpiSynonym has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cat-kpi-synonyms")
    public ResponseEntity<CatKpiSynonymDTO> createCatKpiSynonym(@Valid @RequestBody CatKpiSynonymDTO catKpiSynonymDTO) throws URISyntaxException {
        log.debug("REST request to save CatKpiSynonym : {}", catKpiSynonymDTO);
        if (catKpiSynonymDTO.getId() != null) {
            throw new BadRequestAlertException("A new catKpiSynonym cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatKpiSynonymDTO result = catKpiSynonymService.save(catKpiSynonymDTO);
        return ResponseEntity.created(new URI("/api/cat-kpi-synonyms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cat-kpi-synonyms} : Updates an existing catKpiSynonym.
     *
     * @param catKpiSynonymDTO the catKpiSynonymDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catKpiSynonymDTO,
     * or with status {@code 400 (Bad Request)} if the catKpiSynonymDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catKpiSynonymDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cat-kpi-synonyms")
    public ResponseEntity<CatKpiSynonymDTO> updateCatKpiSynonym(@Valid @RequestBody CatKpiSynonymDTO catKpiSynonymDTO) throws URISyntaxException {
        log.debug("REST request to update CatKpiSynonym : {}", catKpiSynonymDTO);
        if (catKpiSynonymDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CatKpiSynonymDTO result = catKpiSynonymService.save(catKpiSynonymDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catKpiSynonymDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cat-kpi-synonyms} : get all the catKpiSynonyms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catKpiSynonyms in body.
     */
    @GetMapping("/cat-kpi-synonyms")
    public ResponseEntity<List<CatKpiSynonymDTO>> getAllCatKpiSynonyms(Pageable pageable) {
        log.debug("REST request to get a page of CatKpiSynonyms");
        Page<CatKpiSynonymDTO> page = catKpiSynonymService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cat-kpi-synonyms/:id} : get the "id" catKpiSynonym.
     *
     * @param id the id of the catKpiSynonymDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catKpiSynonymDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cat-kpi-synonyms/{id}")
    public ResponseEntity<CatKpiSynonymDTO> getCatKpiSynonym(@PathVariable Long id) {
        log.debug("REST request to get CatKpiSynonym : {}", id);
        Optional<CatKpiSynonymDTO> catKpiSynonymDTO = catKpiSynonymService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catKpiSynonymDTO);
    }
    @GetMapping("/cat-kpi-synonyms-kpi-id/{id}")
    public ResponseEntity<List<CatKpiSynonymDTO>> getCatKpiSynonymByKpiId(@PathVariable Long id) {
        log.debug("REST request to get CatKpiSynonym : {}", id);
        List<CatKpiSynonymDTO> catKpiSynonymDTO = catKpiSynonymService.findAllByKpiId(id);
        return ResponseEntity.ok().body(catKpiSynonymDTO);
    }

    /**
     * {@code DELETE  /cat-kpi-synonyms/:id} : delete the "id" catKpiSynonym.
     *
     * @param id the id of the catKpiSynonymDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cat-kpi-synonyms/{id}")
    public ResponseEntity<Void> deleteCatKpiSynonym(@PathVariable Long id) {
        log.debug("REST request to delete CatKpiSynonym : {}", id);
        catKpiSynonymService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
