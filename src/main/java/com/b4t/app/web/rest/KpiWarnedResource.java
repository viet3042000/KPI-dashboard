package com.b4t.app.web.rest;

import com.b4t.app.service.KpiWarnedService;
import com.b4t.app.service.dto.TreeDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.KpiWarnedDTO;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.KpiWarned}.
 */
@RestController
@RequestMapping("/api")
public class KpiWarnedResource {

    private final Logger log = LoggerFactory.getLogger(KpiWarnedResource.class);

    private static final String ENTITY_NAME = "kpiWarned";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KpiWarnedService kpiWarnedService;

    public KpiWarnedResource(KpiWarnedService kpiWarnedService) {
        this.kpiWarnedService = kpiWarnedService;
    }

    /**
     * {@code POST  /kpi-warneds} : Create a new kpiWarned.
     *
     * @param kpiWarnedDTO the kpiWarnedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kpiWarnedDTO, or with status {@code 400 (Bad Request)} if the kpiWarned has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kpi-warneds")
    public ResponseEntity<KpiWarnedDTO> createKpiWarned(@RequestBody KpiWarnedDTO kpiWarnedDTO) throws URISyntaxException {
        log.debug("REST request to save KpiWarned : {}", kpiWarnedDTO);
        if (kpiWarnedDTO.getId() != null) {
            throw new BadRequestAlertException("A new kpiWarned cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KpiWarnedDTO result = kpiWarnedService.save(kpiWarnedDTO);
        return ResponseEntity.created(new URI("/api/kpi-warneds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kpi-warneds} : Updates an existing kpiWarned.
     *
     * @param kpiWarnedDTO the kpiWarnedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kpiWarnedDTO,
     * or with status {@code 400 (Bad Request)} if the kpiWarnedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kpiWarnedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kpi-warneds")
    public ResponseEntity<KpiWarnedDTO> updateKpiWarned(@RequestBody KpiWarnedDTO kpiWarnedDTO) throws URISyntaxException {
        log.debug("REST request to update KpiWarned : {}", kpiWarnedDTO);
        if (kpiWarnedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KpiWarnedDTO result = kpiWarnedService.save(kpiWarnedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kpiWarnedDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kpi-warneds} : get all the kpiWarneds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kpiWarneds in body.
     */
    @GetMapping("/kpi-warneds")
    public ResponseEntity<List<KpiWarnedDTO>> getAllKpiWarneds(Pageable pageable) {
        log.debug("REST request to get a page of KpiWarneds");
        Page<KpiWarnedDTO> page = kpiWarnedService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kpi-warneds/:id} : get the "id" kpiWarned.
     *
     * @param id the id of the kpiWarnedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kpiWarnedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kpi-warneds/{id}")
    public ResponseEntity<KpiWarnedDTO> getKpiWarned(@PathVariable Long id) {
        log.debug("REST request to get KpiWarned : {}", id);
        Optional<KpiWarnedDTO> kpiWarnedDTO = kpiWarnedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kpiWarnedDTO);
    }

    /**
     * {@code DELETE  /kpi-warneds/:id} : delete the "id" kpiWarned.
     *
     * @param id the id of the kpiWarnedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kpi-warneds/{id}")
    public ResponseEntity<Void> deleteKpiWarned(@PathVariable Long id) {
        log.debug("REST request to delete KpiWarned : {}", id);
        kpiWarnedService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/kpi-warneds/get-warning-by-kpi-id")
    public ResponseEntity<List<KpiWarnedDTO>> findAllByKpiId(Long kpiId, Long timeType) {
//        List<KpiWarnedDTO> results = kpiWarnedService.findAllByKpiId(kpiId, timeType);
        List<KpiWarnedDTO> results = new ArrayList<>();
        return ResponseEntity.ok().body(results);
    }
}
