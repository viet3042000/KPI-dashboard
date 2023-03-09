package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.service.RawDataFromMicUnitService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.RawDataFromMicUnitDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.RawDataFromMicUnit}.
 */
@RestController
@Validated
@RequestMapping("/api")
public class RawDataFromMicUnitResource {

    private final Logger log = LoggerFactory.getLogger(RawDataFromMicUnitResource.class);

    private static final String ENTITY_NAME = "rawDataFromMicUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final Environment env;
    private final RawDataFromMicUnitService rawDataFromMicUnitService;

    public RawDataFromMicUnitResource(Environment env, RawDataFromMicUnitService rawDataFromMicUnitService) {
        this.env = env;
        this.rawDataFromMicUnitService = rawDataFromMicUnitService;
    }

    /**
     * {@code POST  /raw-data-from-mic-units} : Create a new rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTO the rawDataFromMicUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rawDataFromMicUnitDTO, or with status {@code 400 (Bad Request)} if the rawDataFromMicUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raw-data-from-mic-units")
    public ResponseEntity<RawDataFromMicUnitDTO> createRawDataFromMicUnit(@Valid @RequestBody RawDataFromMicUnitDTO rawDataFromMicUnitDTO) throws URISyntaxException {
        log.debug("REST request to save RawDataFromMicUnit : {}", rawDataFromMicUnitDTO);
        if (rawDataFromMicUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new rawDataFromMicUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RawDataFromMicUnitDTO result = rawDataFromMicUnitService.save(rawDataFromMicUnitDTO);
        return ResponseEntity.created(new URI("/api/raw-data-from-mic-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code POST  /raw-data-from-mic-units} : Create a new rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTOs the rawDataFromMicUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rawDataFromMicUnitDTO, or with status {@code 400 (Bad Request)} if the rawDataFromMicUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/import-kpis")
    public ResponseEntity<String> createRawDataFromMicUnit(@Valid @RequestBody List<RawDataFromMicUnitDTO> rawDataFromMicUnitDTOs) throws URISyntaxException {
        log.debug("REST request to save RawDataFromMicUnit : {}", rawDataFromMicUnitDTOs);
        if (DataUtil.isNullOrEmpty(rawDataFromMicUnitDTOs)) {
            throw new BadRequestAlertException("Require list data", ENTITY_NAME, "require data");
        }
        Long limitRows = Long.parseLong(env.getProperty("api-import.limit-rows-insert"));
        if (rawDataFromMicUnitDTOs.size() > limitRows) {
            throw new BadRequestAlertException("Require data size < 1000 rows ", ENTITY_NAME, "require data < 1000 rows ");
        }
        rawDataFromMicUnitDTOs.forEach(bean -> {
            if (DataUtil.isNullOrEmpty(bean.getObjCode()) || DataUtil.isNullOrEmpty(bean.getObjName()) || bean.getInputLevel() == null || bean.getPrdId() == null || bean.getKpiId() == null || bean.getTimeType() == null) {
                throw new BadRequestAlertException("Yeu cau nhap du cac thong tin objectCode, objectName, kpiCode, kpiName, inputLeve, timeType, kpiId, prdId ", ENTITY_NAME, "require data field ");
            }
        });

        rawDataFromMicUnitService.saveAll(rawDataFromMicUnitDTOs);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).body("Success");
    }

    /**
     * {@code PUT  /raw-data-from-mic-units} : Updates an existing rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTO the rawDataFromMicUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rawDataFromMicUnitDTO,
     * or with status {@code 400 (Bad Request)} if the rawDataFromMicUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rawDataFromMicUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raw-data-from-mic-units")
    public ResponseEntity<RawDataFromMicUnitDTO> updateRawDataFromMicUnit(@RequestBody RawDataFromMicUnitDTO rawDataFromMicUnitDTO) throws URISyntaxException {
        log.debug("REST request to update RawDataFromMicUnit : {}", rawDataFromMicUnitDTO);
        if (rawDataFromMicUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RawDataFromMicUnitDTO result = rawDataFromMicUnitService.save(rawDataFromMicUnitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rawDataFromMicUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /raw-data-from-mic-units} : get all the rawDataFromMicUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rawDataFromMicUnits in body.
     */
    @GetMapping("/raw-data-from-mic-units")
    public ResponseEntity<List<RawDataFromMicUnitDTO>> getAllRawDataFromMicUnits(Pageable pageable) {
        log.debug("REST request to get a page of RawDataFromMicUnits");
        Page<RawDataFromMicUnitDTO> page = rawDataFromMicUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /raw-data-from-mic-units/:id} : get the "id" rawDataFromMicUnit.
     *
     * @param id the id of the rawDataFromMicUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rawDataFromMicUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raw-data-from-mic-units/{id}")
    public ResponseEntity<RawDataFromMicUnitDTO> getRawDataFromMicUnit(@PathVariable Long id) {
        log.debug("REST request to get RawDataFromMicUnit : {}", id);
        Optional<RawDataFromMicUnitDTO> rawDataFromMicUnitDTO = rawDataFromMicUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rawDataFromMicUnitDTO);
    }

    /**
     * {@code DELETE  /raw-data-from-mic-units/:id} : delete the "id" rawDataFromMicUnit.
     *
     * @param id the id of the rawDataFromMicUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raw-data-from-mic-units/{id}")
    public ResponseEntity<Void> deleteRawDataFromMicUnit(@PathVariable Long id) {
        log.debug("REST request to delete RawDataFromMicUnit : {}", id);
        rawDataFromMicUnitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
