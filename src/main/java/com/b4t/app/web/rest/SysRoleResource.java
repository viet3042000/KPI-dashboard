package com.b4t.app.web.rest;

import com.b4t.app.service.SysRoleService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.SysRoleDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.SysRole}.
 */
@RestController
@RequestMapping("/api")
public class SysRoleResource {

    private final Logger log = LoggerFactory.getLogger(SysRoleResource.class);

    private static final String ENTITY_NAME = "sysRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysRoleService sysRoleService;

    public SysRoleResource(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("/roles/getAllRoles")
    public ResponseEntity<List<SysRoleDTO>> getAllRole() {
        return ResponseEntity.ok(sysRoleService.getAllRole());
    }

    @PostMapping(value = "/roles/doSearch")
    @PreAuthorize("hasPermission('ROLE_MODULE', '*')")
    public ResponseEntity<List<SysRoleDTO>> doSearch(@RequestBody SysRoleDTO paramSearch, Pageable pageable){
        if(pageable == null){
            pageable = PageRequest.of(0, 100);
        }
        Page<SysRoleDTO> page =sysRoleService.doSearch(paramSearch.getCode(), paramSearch.getName(), paramSearch.getStatus(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST  /sys-roles} : Create a new sysRole.
     *
     * @param sysRoleDTO the sysRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysRoleDTO, or with status {@code 400 (Bad Request)} if the sysRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles/insert")
    @PreAuthorize("hasPermission('ROLE_MODULE', 'INSERT')")
    public ResponseEntity<SysRoleDTO> createSysRole(@Valid @RequestBody SysRoleDTO sysRoleDTO) throws URISyntaxException {
        log.debug("REST request to save SysRole : {}", sysRoleDTO);
        if (sysRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysRoleDTO result = sysRoleService.save(sysRoleDTO);
        return ResponseEntity.created(new URI("/api/sys-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-roles} : Updates an existing sysRole.
     *
     * @param sysRoleDTO the sysRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysRoleDTO,
     * or with status {@code 400 (Bad Request)} if the sysRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles/update")
    @PreAuthorize("hasPermission('ROLE_MODULE', 'UPDATE')")
    public ResponseEntity<SysRoleDTO> updateSysRole(@Valid @RequestBody SysRoleDTO sysRoleDTO) throws URISyntaxException {
        log.debug("REST request to update SysRole : {}", sysRoleDTO);
        if (sysRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysRoleDTO result = sysRoleService.save(sysRoleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysRoleDTO.getId().toString()))
            .body(result);
    }


    /**
     * {@code DELETE  /sys-roles/:id} : delete the "id" sysRole.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/roles/delete")
    @PreAuthorize("hasPermission('ROLE_MODULE', 'DELETE')")
    public ResponseEntity<Void> deleteSysRole(@RequestBody SysRoleDTO sysRoleDTO) {
        log.debug("REST request to delete SysRole : {}", sysRoleDTO.getId());
        sysRoleService.delete(sysRoleDTO.getId());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, sysRoleDTO.getId().toString())).build();
    }

    @PostMapping("/roles/multiple-delete")
    @PreAuthorize("hasPermission('ROLE_MODULE', 'DELETE')")
    public ResponseEntity<Void> deleteMultipleSysRole(@Valid @RequestBody List<SysRoleDTO> sysRoleDTOs) {
        log.debug("REST request to delete multiple SysAction : {}", "");
        sysRoleService.deleteMultiple(sysRoleDTOs);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, sysRoleDTOs.size() + " item")).build();
    }
}
