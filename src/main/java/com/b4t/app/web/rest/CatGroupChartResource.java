package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatGroupChart;
import com.b4t.app.service.CatGroupChartService;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.CatGroupChartDTO;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.CatGroupChart}.
 */
@RestController
@RequestMapping("/api")
public class CatGroupChartResource {

    private final Logger log = LoggerFactory.getLogger(CatGroupChartResource.class);

    private static final String ENTITY_NAME = "catGroupChart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatGroupChartService catGroupChartService;

    public CatGroupChartResource(CatGroupChartService catGroupChartService) {
        this.catGroupChartService = catGroupChartService;
    }

    /**
     * {@code POST  /cat-group-charts} : Create a new catGroupChart.
     *
     * @param catGroupChartDTO the catGroupChartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catGroupChartDTO, or with status {@code 400 (Bad Request)} if the catGroupChart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cat-group-charts")
    @PreAuthorize("hasPermission('GROUP_CHART', '*')")
    public ResponseEntity<CatGroupChartDTO> createCatGroupChart(@RequestBody CatGroupChartDTO catGroupChartDTO) throws URISyntaxException {
        log.debug("REST request to save CatGroupChart : {}", catGroupChartDTO);
        if (catGroupChartDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        CatGroupChartDTO result = catGroupChartService.save(catGroupChartDTO);
        return ResponseEntity.created(new URI("/api/cat-group-charts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cat-group-charts} : Updates an existing catGroupChart.
     *
     * @param catGroupChartDTO the catGroupChartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catGroupChartDTO,
     * or with status {@code 400 (Bad Request)} if the catGroupChartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catGroupChartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cat-group-charts")
    @PreAuthorize("hasPermission('GROUP_CHART', '*')")
    public ResponseEntity<CatGroupChartDTO> updateCatGroupChart(@RequestBody CatGroupChartDTO catGroupChartDTO) throws URISyntaxException {
        log.debug("REST request to update CatGroupChart : {}", catGroupChartDTO);
        if (catGroupChartDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        CatGroupChartDTO result = catGroupChartService.save(catGroupChartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catGroupChartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cat-group-charts} : get all the catGroupCharts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catGroupCharts in body.
     */
    @GetMapping("/cat-group-charts")
    @PreAuthorize("hasPermission('GROUP_CHART', '*')")
    public ResponseEntity<List<CatGroupChartDTO>> getAllCatGroupCharts(String keyword, String[] groupKpiCodes, Long[] screenIds, Long status, Pageable pageable) {
        log.debug("REST request to get a page of CatGroupCharts");
        Page<CatGroupChartDTO> page = catGroupChartService.findAll(keyword, groupKpiCodes, screenIds, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cat-group-charts/:id} : get the "id" catGroupChart.
     *
     * @param id the id of the catGroupChartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catGroupChartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cat-group-charts/{id}")
    public ResponseEntity<CatGroupChartDTO> getCatGroupChart(@PathVariable Long id) {
        log.debug("REST request to get CatGroupChart : {}", id);
        Optional<CatGroupChartDTO> catGroupChartDTO = catGroupChartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catGroupChartDTO);
    }

    /**
     * {@code DELETE  /cat-group-charts/:id} : delete the "id" catGroupChart.
     *
     * @param id the id of the catGroupChartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cat-group-charts/{id}")
    @PreAuthorize("hasPermission('GROUP_CHART', '*')")
    public ResponseEntity<Void> deleteCatGroupChart(@PathVariable Long id) {
        Optional<CatGroupChartDTO> ckOpt = catGroupChartService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "error.isnotexisted");
        }
        catGroupChartService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/cat-group-charts/delete-multiple")
    @PreAuthorize("hasPermission('GROUP_CHART', '*')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> deleteCatGroupChartMultiple(@RequestBody List<CatGroupChart> catGroupChart) {
        //Optional<CatGroupChartDTO> ckOpt = catGroupChartService.findOne(id);
        for(CatGroupChart cat : catGroupChart) {
            Optional<CatGroupChartDTO> ckOpt = catGroupChartService.findOne(cat.getId());
            if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(cat.getStatus())) {
                throw new BadRequestAlertException(Translator.toLocale("error.disabled.item"), ENTITY_NAME, "error.disabled.item");
            }
            catGroupChartService.delete(cat.getId());
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build();
    }

}
