package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.service.FlagRunQueryKpiService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.FlagRunQueryKpiDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.FlagRunQueryKpi}.
 */
@RestController
@RequestMapping("/api")
public class FlagRunQueryKpiResource {

    private final Logger log = LoggerFactory.getLogger(FlagRunQueryKpiResource.class);

    private static final String ENTITY_NAME = "flagRunQueryKpi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlagRunQueryKpiService flagRunQueryKpiService;

    public FlagRunQueryKpiResource(FlagRunQueryKpiService flagRunQueryKpiService) {
        this.flagRunQueryKpiService = flagRunQueryKpiService;
    }

    /**
     * {@code POST  /flag-run-query-kpis} : Create a new flagRunQueryKpi.
     *
     * @param flagRunQueryKpiDTO the flagRunQueryKpiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flagRunQueryKpiDTO, or with status {@code 400 (Bad Request)} if the flagRunQueryKpi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flag-run-query-kpis")
    public ResponseEntity<FlagRunQueryKpiDTO> createFlagRunQueryKpi(@RequestBody FlagRunQueryKpiDTO flagRunQueryKpiDTO) throws Exception {
        log.debug("REST request to save FlagRunQueryKpi : {}", flagRunQueryKpiDTO);
        if (flagRunQueryKpiDTO.getId() != null) {
            throw new BadRequestAlertException("A new flagRunQueryKpi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (DataUtil.isNullOrEmpty(flagRunQueryKpiDTO.getTableName()) || flagRunQueryKpiDTO.getPrdId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.require.field") + " tableName, prdId", ENTITY_NAME, "require value");
        }
        FlagRunQueryKpiDTO result = flagRunQueryKpiService.save(flagRunQueryKpiDTO);
        if(result == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.monitorQueryKpi.prdIdAndTableExists"),ENTITY_NAME,"monitorQueryKpi.prdIdAndTableExists");
        }
        return ResponseEntity.created(new URI("/api/flag-run-query-kpis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/flag-run-query-kpis/save-all")
    public ResponseEntity<List<FlagRunQueryKpiDTO>> createFlagRunQueryKpi(@RequestBody List<FlagRunQueryKpiDTO> flagRunQueryKpiDTOs) throws URISyntaxException {
        log.debug("REST request to save List FlagRunQueryKpi : {}", flagRunQueryKpiDTOs);
        if (flagRunQueryKpiDTOs == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.require.list.object"), ENTITY_NAME, "require");
        }
        flagRunQueryKpiDTOs.forEach(bean -> {
            if (bean.getId() != null) {
                throw new BadRequestAlertException("A new flagRunQueryKpi cannot already have an ID", ENTITY_NAME, "idexists");
            }
            if (DataUtil.isNullOrEmpty(bean.getTableName()) || bean.getPrdId() == null) {
                throw new BadRequestAlertException(Translator.toLocale("error.require.field") + " tableName, prdId", ENTITY_NAME, "require value");
            }
        });

        List<FlagRunQueryKpiDTO> result = flagRunQueryKpiService.saveAll(flagRunQueryKpiDTOs);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, "Sucess");
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code PUT  /flag-run-query-kpis} : Updates an existing flagRunQueryKpi.
     *
     * @param flagRunQueryKpiDTO the flagRunQueryKpiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flagRunQueryKpiDTO,
     * or with status {@code 400 (Bad Request)} if the flagRunQueryKpiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flagRunQueryKpiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flag-run-query-kpis")
    public ResponseEntity<FlagRunQueryKpiDTO> updateFlagRunQueryKpi(@RequestBody FlagRunQueryKpiDTO flagRunQueryKpiDTO) throws Exception {
        log.debug("REST request to update FlagRunQueryKpi : {}", flagRunQueryKpiDTO);
        if (flagRunQueryKpiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FlagRunQueryKpiDTO result = flagRunQueryKpiService.save(flagRunQueryKpiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flagRunQueryKpiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /flag-run-query-kpis} : get all the flagRunQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flagRunQueryKpis in body.
     */
    @GetMapping("/flag-run-query-kpis")
    public ResponseEntity<List<FlagRunQueryKpiDTO>> getAllFlagRunQueryKpis(@RequestParam Integer status ,Pageable pageable) {
        log.debug("REST request to get a page of FlagRunQueryKpis");

        Page<FlagRunQueryKpiDTO> page = flagRunQueryKpiService.findAllByStatus(!DataUtil.isNullOrEmpty(status) ? Long.valueOf(status) : 1L, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flag-run-query-kpis/:id} : get the "id" flagRunQueryKpi.
     *
     * @param id the id of the flagRunQueryKpiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flagRunQueryKpiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flag-run-query-kpis/{id}")
    public ResponseEntity<FlagRunQueryKpiDTO> getFlagRunQueryKpi(@PathVariable Long id) {
        log.debug("REST request to get FlagRunQueryKpi : {}", id);
        Optional<FlagRunQueryKpiDTO> flagRunQueryKpiDTO = flagRunQueryKpiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flagRunQueryKpiDTO);
    }

    /**
     * {@code DELETE  /flag-run-query-kpis/:id} : delete the "id" flagRunQueryKpi.
     *
     * @param id the id of the flagRunQueryKpiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flag-run-query-kpis/{id}")
    public ResponseEntity<Void> deleteFlagRunQueryKpi(@PathVariable Long id) {
        log.debug("REST request to delete FlagRunQueryKpi : {}", id);
        flagRunQueryKpiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
