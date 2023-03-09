package com.b4t.app.web.rest;

import com.b4t.app.service.ClassifyColorGmapLevelService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ClassifyColorGmapLevelDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ClassifyColorGmapLevel}.
 */
@RestController
@RequestMapping("/api")
public class ClassifyColorGmapLevelResource {

    private final Logger log = LoggerFactory.getLogger(ClassifyColorGmapLevelResource.class);

    private static final String ENTITY_NAME = "classifyColorGmapLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassifyColorGmapLevelService classifyColorGmapLevelService;

    public ClassifyColorGmapLevelResource(ClassifyColorGmapLevelService classifyColorGmapLevelService) {
        this.classifyColorGmapLevelService = classifyColorGmapLevelService;
    }

    /**
     * {@code POST  /classify-color-gmap-levels} : Create a new classifyColorGmapLevel.
     *
     * @param classifyColorGmapLevelDTO the classifyColorGmapLevelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classifyColorGmapLevelDTO, or with status {@code 400 (Bad Request)} if the classifyColorGmapLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classify-color-gmap-levels")
    public ResponseEntity<ClassifyColorGmapLevelDTO> createClassifyColorGmapLevel(@RequestBody ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO) throws URISyntaxException {
        log.debug("REST request to save ClassifyColorGmapLevel : {}", classifyColorGmapLevelDTO);
        if (classifyColorGmapLevelDTO.getId() != null) {
            throw new BadRequestAlertException("A new classifyColorGmapLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassifyColorGmapLevelDTO result = classifyColorGmapLevelService.save(classifyColorGmapLevelDTO);
        return ResponseEntity.created(new URI("/api/classify-color-gmap-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classify-color-gmap-levels} : Updates an existing classifyColorGmapLevel.
     *
     * @param classifyColorGmapLevelDTO the classifyColorGmapLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classifyColorGmapLevelDTO,
     * or with status {@code 400 (Bad Request)} if the classifyColorGmapLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classifyColorGmapLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classify-color-gmap-levels")
    public ResponseEntity<ClassifyColorGmapLevelDTO> updateClassifyColorGmapLevel(@RequestBody ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO) throws URISyntaxException {
        log.debug("REST request to update ClassifyColorGmapLevel : {}", classifyColorGmapLevelDTO);
        if (classifyColorGmapLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassifyColorGmapLevelDTO result = classifyColorGmapLevelService.save(classifyColorGmapLevelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classifyColorGmapLevelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /classify-color-gmap-levels} : get all the classifyColorGmapLevels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifyColorGmapLevels in body.
     */
    @GetMapping("/classify-color-gmap-levels")
    public ResponseEntity<List<ClassifyColorGmapLevelDTO>> getAllClassifyColorGmapLevels(Pageable pageable) {
        log.debug("REST request to get a page of ClassifyColorGmapLevels");
        Page<ClassifyColorGmapLevelDTO> page = classifyColorGmapLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classify-color-gmap-levels/:id} : get the "id" classifyColorGmapLevel.
     *
     * @param id the id of the classifyColorGmapLevelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classifyColorGmapLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classify-color-gmap-levels/{id}")
    public ResponseEntity<ClassifyColorGmapLevelDTO> getClassifyColorGmapLevel(@PathVariable Long id) {
        log.debug("REST request to get ClassifyColorGmapLevel : {}", id);
        Optional<ClassifyColorGmapLevelDTO> classifyColorGmapLevelDTO = classifyColorGmapLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classifyColorGmapLevelDTO);
    }

    /**
     * {@code DELETE  /classify-color-gmap-levels/:id} : delete the "id" classifyColorGmapLevel.
     *
     * @param id the id of the classifyColorGmapLevelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classify-color-gmap-levels/{id}")
    public ResponseEntity<Void> deleteClassifyColorGmapLevel(@PathVariable Long id) {
        log.debug("REST request to delete ClassifyColorGmapLevel : {}", id);
        classifyColorGmapLevelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
