package com.b4t.app.web.rest;

import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.SysActionService;
import com.b4t.app.service.dto.SysActionDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.SysAction}.
 */
@RestController
@RequestMapping("/api")
public class SysActionResource {

    private final Logger log = LoggerFactory.getLogger(SysActionResource.class);

    private static final String ENTITY_NAME = "sysAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysActionService sysActionService;

    public SysActionResource(SysActionService sysActionService) {
        this.sysActionService = sysActionService;
    }



    /**
     * {@code POST  /sys-actions} : Create a new sysAction.
     *
     * @param sysActionDTO the sysActionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysActionDTO, or with status {@code 400 (Bad Request)} if the sysAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/action/insert")
    @PreAuthorize("hasPermission('ACTIONS', 'INSERT')")
    public ResponseEntity<SysActionDTO> createSysAction(@Valid @RequestBody SysActionDTO sysActionDTO) throws URISyntaxException {
        log.debug("REST request to save SysAction : {}", sysActionDTO);
        if (sysActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysActionDTO result = sysActionService.save(sysActionDTO);
        return ResponseEntity.created(new URI("/api/sys-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-actions} : Updates an existing sysAction.
     *
     * @param sysActionDTO the sysActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysActionDTO,
     * or with status {@code 400 (Bad Request)} if the sysActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/action/update")
    @PreAuthorize("hasPermission('ACTIONS', 'UPDATE')")
    public ResponseEntity<SysActionDTO> updateSysAction(@Valid @RequestBody SysActionDTO sysActionDTO) throws URISyntaxException {
        log.debug("REST request to update SysAction : {}", sysActionDTO);
        if (sysActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysActionDTO result = sysActionService.save(sysActionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysActionDTO.getId().toString()))
            .body(result);
    }

    @PostMapping(value = "/action/doSearch")
    @PreAuthorize("hasPermission('ACTIONS', '*')")
    public ResponseEntity<List<SysActionDTO>> doSearch(@RequestBody SysActionDTO paramSearch, Pageable pageable){
        if(pageable == null){
            pageable = PageRequest.of(0, 100);
        }
        Page<SysActionDTO> page =sysActionService.doSearch(paramSearch.getCode(), paramSearch.getName(), paramSearch.getStatus(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/sys-actions/{id}")
    public ResponseEntity<SysActionDTO> getSysAction(@PathVariable Long id) {
        log.debug("REST request to get SysAction : {}", id);
        Optional<SysActionDTO> sysActionDTO = sysActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysActionDTO);
    }

    /**
     * {@code DELETE  /sys-actions/:id} : delete the "id" sysAction.
     *
     * @param sysActionDTO the id of the sysActionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/action/delete")
    @PreAuthorize("hasPermission('ACTIONS', 'DELETE')")
    public ResponseEntity<Void> deleteSysAction(@Valid @RequestBody SysActionDTO sysActionDTO) {
        log.debug("REST request to delete SysAction : {}", sysActionDTO.getId());
        sysActionService.delete(sysActionDTO.getId());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, sysActionDTO.getId().toString())).build();
    }

    @GetMapping(value = "/sys-actions-getAll")
    public ResponseEntity<List<SysActionDTO>> getAll() {
        List<SysActionDTO> lstData = new ArrayList<>();
        sysActionService.findAllByTenantId();
        return ResponseEntity.ok().body(lstData);
    }

    @GetMapping(value = "/sys-actions/get-by-current-user")
    public ResponseEntity<List<SysActionDTO>> getByCurrentUser() {
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (StringUtils.isEmpty(username)) return ResponseEntity.ok().body(new ArrayList<>());

        List<SysActionDTO> lstData = sysActionService.findAllByUserName(username);
        return ResponseEntity.ok().body(lstData);
    }
    @PostMapping(value = "/sys-actions/getAll")
    public ResponseEntity<List<SysActionDTO>> getAllAction(@RequestBody SysActionDTO sysActionDTO) {
        List<SysActionDTO> lstData =sysActionService.getAll(sysActionDTO);
        return ResponseEntity.ok().body(lstData);
    }

    @PutMapping("/action/multiple-delete")
    @PreAuthorize("hasPermission('ACTIONS', 'DELETE')")
    public ResponseEntity<Void> deleteMultipleSysAction(@Valid @RequestBody List<SysActionDTO> sysActionDTOs) {
        log.debug("REST request to delete multiple SysAction : {}", "");
        sysActionService.deleteMultiple(sysActionDTOs);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, sysActionDTOs.size() + " item")).build();
    }
}
