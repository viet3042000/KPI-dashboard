package com.b4t.app.web.rest;

import com.b4t.app.service.AreaGmapDataService;
import com.b4t.app.service.dto.AreaAlarmDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.AreaGmapDataDTO;

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

//import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.AreaGmapData}.
 */
@RestController
@RequestMapping("/api")
public class AreaGmapDataResource {

    private final Logger log = LoggerFactory.getLogger(AreaGmapDataResource.class);

    private static final String ENTITY_NAME = "areaGmapData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaGmapDataService areaGmapDataService;

    public AreaGmapDataResource(AreaGmapDataService areaGmapDataService) {
        this.areaGmapDataService = areaGmapDataService;
    }

    /**
     * {@code POST  /area-gmap-data} : Create a new areaGmapData.
     *
     * @param areaGmapDataDTO the areaGmapDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new areaGmapDataDTO, or with status {@code 400 (Bad Request)} if the areaGmapData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/area-gmap-data")
    public ResponseEntity<AreaGmapDataDTO> createAreaGmapData(@RequestBody AreaGmapDataDTO areaGmapDataDTO) throws URISyntaxException {
        log.debug("REST request to save AreaGmapData : {}", areaGmapDataDTO);
        if (areaGmapDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new areaGmapData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AreaGmapDataDTO result = areaGmapDataService.save(areaGmapDataDTO);
        return ResponseEntity.created(new URI("/api/area-gmap-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /area-gmap-data} : Updates an existing areaGmapData.
     *
     * @param areaGmapDataDTO the areaGmapDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaGmapDataDTO,
     * or with status {@code 400 (Bad Request)} if the areaGmapDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the areaGmapDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/area-gmap-data")
    public ResponseEntity<AreaGmapDataDTO> updateAreaGmapData(@RequestBody AreaGmapDataDTO areaGmapDataDTO) throws URISyntaxException {
        log.debug("REST request to update AreaGmapData : {}", areaGmapDataDTO);
        if (areaGmapDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AreaGmapDataDTO result = areaGmapDataService.save(areaGmapDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, areaGmapDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /area-gmap-data} : get all the areaGmapData.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areaGmapData in body.
     */
    @GetMapping("/area-gmap-data")
    public ResponseEntity<List<AreaGmapDataDTO>> getAllAreaGmapData(Pageable pageable) {
        log.debug("REST request to get a page of AreaGmapData");
        Page<AreaGmapDataDTO> page = areaGmapDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /area-gmap-data/:id} : get the "id" areaGmapData.
     *
     * @param id the id of the areaGmapDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the areaGmapDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/area-gmap-data/{id}")
    public ResponseEntity<AreaGmapDataDTO> getAreaGmapData(@PathVariable Long id) {
        log.debug("REST request to get AreaGmapData : {}", id);
        Optional<AreaGmapDataDTO> areaGmapDataDTO = areaGmapDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(areaGmapDataDTO);
    }

    /**
     * {@code DELETE  /area-gmap-data/:id} : delete the "id" areaGmapData.
     *
     * @param id the id of the areaGmapDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/area-gmap-data/{id}")
    public ResponseEntity<Void> deleteAreaGmapData(@PathVariable Long id) {
        log.debug("REST request to delete AreaGmapData : {}", id);
        areaGmapDataService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/area-gmap-data/getByParentCode")
    public ResponseEntity<List<AreaGmapDataDTO>> findAllByParentCode(@PathVariable AreaGmapDataDTO areaGmapDataDTO) {
        List<AreaGmapDataDTO> results = areaGmapDataService.findAllByParentCodeAndStatus(areaGmapDataDTO);
        HttpHeaders headers = HeaderUtil.createAlert(ENTITY_NAME, "List area data", "Query ok");
        return ResponseEntity.ok().headers(headers).body(results);
    }

    @GetMapping("/area-gmap-data/getByObjectCode")
    public ResponseEntity<List<AreaGmapDataDTO>> findByObjectCode(@PathVariable AreaGmapDataDTO areaGmapDataDTO) {
        List<AreaGmapDataDTO> results = areaGmapDataService.findByObjectCode(areaGmapDataDTO);
        HttpHeaders headers = HeaderUtil.createAlert(ENTITY_NAME, "object", "Query ok");
        return ResponseEntity.ok().headers(headers).body(results);
    }

    @GetMapping("/area-gmap-data/getAreaAlarm")
    public ResponseEntity<List<AreaAlarmDTO>> getAreaAlarm( AreaGmapDataDTO areaGmapDataDTO) {
        List<AreaAlarmDTO> results = areaGmapDataService.getAreaAlarm(areaGmapDataDTO);
        HttpHeaders headers = HeaderUtil.createAlert(ENTITY_NAME, "object", "Query ok");
        return ResponseEntity.ok().headers(headers).body(results);
    }
}
