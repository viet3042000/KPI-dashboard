package com.b4t.app.web.rest;

import com.b4t.app.service.ClassifyColorGmapService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ClassifyColorGmapDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ClassifyColorGmap}.
 */
@RestController
@RequestMapping("/api")
public class ClassifyColorGmapResource {

    private final Logger log = LoggerFactory.getLogger(ClassifyColorGmapResource.class);

    private static final String ENTITY_NAME = "classifyColorGmap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassifyColorGmapService classifyColorGmapService;

    public ClassifyColorGmapResource(ClassifyColorGmapService classifyColorGmapService) {
        this.classifyColorGmapService = classifyColorGmapService;
    }

    /**
     * {@code POST  /classify-color-gmaps} : Create a new classifyColorGmap.
     *
     * @param classifyColorGmapDTO the classifyColorGmapDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classifyColorGmapDTO, or with status {@code 400 (Bad Request)} if the classifyColorGmap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classify-color-gmaps")
    public ResponseEntity<ClassifyColorGmapDTO> createClassifyColorGmap(@RequestBody ClassifyColorGmapDTO classifyColorGmapDTO) throws URISyntaxException {
        log.debug("REST request to save ClassifyColorGmap : {}", classifyColorGmapDTO);
        if (classifyColorGmapDTO.getId() != null) {
            throw new BadRequestAlertException("A new classifyColorGmap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassifyColorGmapDTO result = classifyColorGmapService.save(classifyColorGmapDTO);
        return ResponseEntity.created(new URI("/api/classify-color-gmaps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classify-color-gmaps} : Updates an existing classifyColorGmap.
     *
     * @param classifyColorGmapDTO the classifyColorGmapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classifyColorGmapDTO,
     * or with status {@code 400 (Bad Request)} if the classifyColorGmapDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classifyColorGmapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classify-color-gmaps")
    public ResponseEntity<ClassifyColorGmapDTO> updateClassifyColorGmap(@RequestBody ClassifyColorGmapDTO classifyColorGmapDTO) throws URISyntaxException {
        log.debug("REST request to update ClassifyColorGmap : {}", classifyColorGmapDTO);
        if (classifyColorGmapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassifyColorGmapDTO result = classifyColorGmapService.save(classifyColorGmapDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classifyColorGmapDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /classify-color-gmaps} : get all the classifyColorGmaps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifyColorGmaps in body.
     */
    @GetMapping("/classify-color-gmaps")
    public ResponseEntity<List<ClassifyColorGmapDTO>> getAllClassifyColorGmaps(Pageable pageable) {
        log.debug("REST request to get a page of ClassifyColorGmaps");
        Page<ClassifyColorGmapDTO> page = classifyColorGmapService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classify-color-gmaps/:id} : get the "id" classifyColorGmap.
     *
     * @param id the id of the classifyColorGmapDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classifyColorGmapDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classify-color-gmaps/{id}")
    public ResponseEntity<ClassifyColorGmapDTO> getClassifyColorGmap(@PathVariable Long id) {
        log.debug("REST request to get ClassifyColorGmap : {}", id);
        Optional<ClassifyColorGmapDTO> classifyColorGmapDTO = classifyColorGmapService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classifyColorGmapDTO);
    }

    /**
     * {@code DELETE  /classify-color-gmaps/:id} : delete the "id" classifyColorGmap.
     *
     * @param id the id of the classifyColorGmapDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classify-color-gmaps/{id}")
    public ResponseEntity<Void> deleteClassifyColorGmap(@PathVariable Long id) {
        log.debug("REST request to delete ClassifyColorGmap : {}", id);
        classifyColorGmapService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
