package com.b4t.app.web.rest;

import com.b4t.app.service.RpReportService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.RpReportDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.RpReport}.
 */
@RestController
@RequestMapping("/api")
public class RpReportResource {

    private final Logger log = LoggerFactory.getLogger(RpReportResource.class);

    private static final String ENTITY_NAME = "rpReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RpReportService rpReportService;

    public RpReportResource(RpReportService rpReportService) {
        this.rpReportService = rpReportService;
    }

    /**
     * {@code POST  /rp-reports} : Create a new rpReport.
     *
     * @param rpReportDTO the rpReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rpReportDTO, or with status {@code 400 (Bad Request)} if the rpReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rp-reports")
    public ResponseEntity<RpReportDTO> createRpReport(@RequestBody RpReportDTO rpReportDTO) throws URISyntaxException {
        log.debug("REST request to save RpReport : {}", rpReportDTO);
        if (rpReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rpReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RpReportDTO result = rpReportService.save(rpReportDTO);
        return ResponseEntity.created(new URI("/api/rp-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rp-reports} : Updates an existing rpReport.
     *
     * @param rpReportDTO the rpReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rpReportDTO,
     * or with status {@code 400 (Bad Request)} if the rpReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rpReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rp-reports")
    public ResponseEntity<RpReportDTO> updateRpReport(@RequestBody RpReportDTO rpReportDTO) throws URISyntaxException {
        log.debug("REST request to update RpReport : {}", rpReportDTO);
        if (rpReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RpReportDTO result = rpReportService.save(rpReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rpReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rp-reports} : get all the rpReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rpReports in body.
     */
    @GetMapping("/rp-reports")
    public ResponseEntity<List<RpReportDTO>> getAllRpReports(Pageable pageable) {
        log.debug("REST request to get a page of RpReports");
        Page<RpReportDTO> page = rpReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rp-reports/:id} : get the "id" rpReport.
     *
     * @param id the id of the rpReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rpReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rp-reports/{id}")
    public ResponseEntity<RpReportDTO> getRpReport(@PathVariable Long id) {
        log.debug("REST request to get RpReport : {}", id);
        Optional<RpReportDTO> rpReportDTO = rpReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rpReportDTO);
    }

    /**
     * {@code DELETE  /rp-reports/:id} : delete the "id" rpReport.
     *
     * @param id the id of the rpReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rp-reports/{id}")
    public ResponseEntity<Void> deleteRpReport(@PathVariable Long id) {
        log.debug("REST request to delete RpReport : {}", id);
        rpReportService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/rp-reports-list")
    public ResponseEntity<List<RpReportDTO>> getRpReportsList() {
        log.debug("REST request to get a page of RpReports");
        List<RpReportDTO> lstReport = rpReportService.findAllReport();
        return ResponseEntity.ok().body(lstReport);
    }
}
