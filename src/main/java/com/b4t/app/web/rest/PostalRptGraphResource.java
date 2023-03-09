package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.PostalRptGraphService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.PostalRptGraphDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.PostalRptGraph}.
 */
@RestController
@RequestMapping("/api")
public class PostalRptGraphResource {

    private final Logger log = LoggerFactory.getLogger(PostalRptGraphResource.class);

    private static final String ENTITY_NAME = "postalRptGraph";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostalRptGraphService postalRptGraphService;

    public PostalRptGraphResource(PostalRptGraphService postalRptGraphService) {
        this.postalRptGraphService = postalRptGraphService;
    }

    /**
     * {@code POST  /postal-rpt-graphs} : Create a new postalRptGraph.
     *
     * @param postalRptGraphDTO the postalRptGraphDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postalRptGraphDTO, or with status {@code 400 (Bad Request)} if the postalRptGraph has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/postal-rpt-graphs")
    public ResponseEntity<PostalRptGraphDTO> createPostalRptGraph(@RequestBody PostalRptGraphDTO postalRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to save PostalRptGraph : {}", postalRptGraphDTO);
        if (postalRptGraphDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        PostalRptGraphDTO result = postalRptGraphService.save(postalRptGraphDTO);
        return ResponseEntity.created(new URI("/api/postal-rpt-graphs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /postal-rpt-graphs} : Updates an existing postalRptGraph.
     *
     * @param postalRptGraphDTO the postalRptGraphDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postalRptGraphDTO,
     * or with status {@code 400 (Bad Request)} if the postalRptGraphDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postalRptGraphDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/postal-rpt-graphs")
    public ResponseEntity<PostalRptGraphDTO> updatePostalRptGraph(@RequestBody PostalRptGraphDTO postalRptGraphDTO) throws URISyntaxException {
        log.debug("REST request to update PostalRptGraph : {}", postalRptGraphDTO);
        if (postalRptGraphDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        PostalRptGraphDTO result = postalRptGraphService.save(postalRptGraphDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postalRptGraphDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /postal-rpt-graphs} : get all the postalRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postalRptGraphs in body.
     */
    @GetMapping("/postal-rpt-graphs")
    public ResponseEntity<List<PostalRptGraphDTO>> getAllPostalRptGraphs(Pageable pageable) {
        log.debug("REST request to get a page of PostalRptGraphs");
        Page<PostalRptGraphDTO> page = postalRptGraphService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /postal-rpt-graphs/:id} : get the "id" postalRptGraph.
     *
     * @param id the id of the postalRptGraphDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postalRptGraphDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/postal-rpt-graphs/{id}")
    public ResponseEntity<PostalRptGraphDTO> getPostalRptGraph(@PathVariable Long id) {
        log.debug("REST request to get PostalRptGraph : {}", id);
        Optional<PostalRptGraphDTO> postalRptGraphDTO = postalRptGraphService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postalRptGraphDTO);
    }

    /**
     * {@code DELETE  /postal-rpt-graphs/:id} : delete the "id" postalRptGraph.
     *
     * @param id the id of the postalRptGraphDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/postal-rpt-graphs/{id}")
    public ResponseEntity<Void> deletePostalRptGraph(@PathVariable Long id) {
        log.debug("REST request to delete PostalRptGraph : {}", id);
        postalRptGraphService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/postal-rpt-graphs/getDescriptionOfTable")
    public Object getDescriptionOfTable() {
        return postalRptGraphService.getDescriptionOfTable();
    }
}
