package com.b4t.app.web.rest;

import com.b4t.app.service.ClassifyColorGmapKpiService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ClassifyColorGmapKpiDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ClassifyColorGmapKpi}.
 */
@RestController
@RequestMapping("/api")
public class ClassifyColorGmapKpiResource {

    private final Logger log = LoggerFactory.getLogger(ClassifyColorGmapKpiResource.class);

    private static final String ENTITY_NAME = "classifyColorGmapKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassifyColorGmapKpiService classifyColorGmapKpiService;

    public ClassifyColorGmapKpiResource(ClassifyColorGmapKpiService classifyColorGmapKpiService) {
        this.classifyColorGmapKpiService = classifyColorGmapKpiService;
    }

    /**
     * {@code POST  /classify-color-gmap-kpis} : Create a new classifyColorGmapKpi.
     *
     * @param classifyColorGmapKpiDTO the classifyColorGmapKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classifyColorGmapKpiDTO, or with status {@code 400 (Bad Request)} if the classifyColorGmapKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classify-color-gmap-kpis")
    public ResponseEntity<ClassifyColorGmapKpiDTO> createClassifyColorGmapKpi(@RequestBody ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO) throws URISyntaxException {
        log.debug("REST request to save ClassifyColorGmapKpi : {}", classifyColorGmapKpiDTO);
        if (classifyColorGmapKpiDTO.getId() != null) {
            throw new BadRequestAlertException("A new classifyColorGmapKpi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassifyColorGmapKpiDTO result = classifyColorGmapKpiService.save(classifyColorGmapKpiDTO);
        return ResponseEntity.created(new URI("/api/classify-color-gmap-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classify-color-gmap-kpis} : Updates an existing classifyColorGmapKpi.
     *
     * @param classifyColorGmapKpiDTO the classifyColorGmapKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classifyColorGmapKpiDTO,
     * or with status {@code 400 (Bad Request)} if the classifyColorGmapKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classifyColorGmapKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classify-color-gmap-kpis")
    public ResponseEntity<ClassifyColorGmapKpiDTO> updateClassifyColorGmapKpi(@RequestBody ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO) throws URISyntaxException {
        log.debug("REST request to update ClassifyColorGmapKpi : {}", classifyColorGmapKpiDTO);
        if (classifyColorGmapKpiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassifyColorGmapKpiDTO result = classifyColorGmapKpiService.save(classifyColorGmapKpiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classifyColorGmapKpiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /classify-color-gmap-kpis} : get all the classifyColorGmapKpis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifyColorGmapKpis in body.
     */
    @GetMapping("/classify-color-gmap-kpis")
    public ResponseEntity<List<ClassifyColorGmapKpiDTO>> getAllClassifyColorGmapKpis(Pageable pageable) {
        log.debug("REST request to get a page of ClassifyColorGmapKpis");
        Page<ClassifyColorGmapKpiDTO> page = classifyColorGmapKpiService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classify-color-gmap-kpis/:id} : get the "id" classifyColorGmapKpi.
     *
     * @param id the id of the classifyColorGmapKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classifyColorGmapKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classify-color-gmap-kpis/{id}")
    public ResponseEntity<ClassifyColorGmapKpiDTO> getClassifyColorGmapKpi(@PathVariable Long id) {
        log.debug("REST request to get ClassifyColorGmapKpi : {}", id);
        Optional<ClassifyColorGmapKpiDTO> classifyColorGmapKpiDTO = classifyColorGmapKpiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classifyColorGmapKpiDTO);
    }

    /**
     * {@code DELETE  /classify-color-gmap-kpis/:id} : delete the "id" classifyColorGmapKpi.
     *
     * @param id the id of the classifyColorGmapKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classify-color-gmap-kpis/{id}")
    public ResponseEntity<Void> deleteClassifyColorGmapKpi(@PathVariable Long id) {
        log.debug("REST request to delete ClassifyColorGmapKpi : {}", id);
        classifyColorGmapKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
