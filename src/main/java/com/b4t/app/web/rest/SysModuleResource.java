package com.b4t.app.web.rest;

import com.b4t.app.domain.SysModule;
import com.b4t.app.service.SysModuleService;
import com.b4t.app.service.dto.ParentModule;
import com.b4t.app.service.dto.SysActionDTO;
import com.b4t.app.service.dto.SysModuleDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.SysModule}.
 */
@RestController
@RequestMapping("/api")
public class SysModuleResource {

    private final Logger log = LoggerFactory.getLogger(SysModuleResource.class);

    private static final String ENTITY_NAME = "sysModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysModuleService sysModuleService;

    public SysModuleResource(SysModuleService sysModuleService) {
        this.sysModuleService = sysModuleService;
    }


    @PostMapping(value = "/module/doSearch")
    @PreAuthorize("hasPermission('MODULES', '*')")
    public ResponseEntity<List<SysModuleDTO>> doSearch(@RequestBody SysModuleDTO paramSearch){
        List<SysModuleDTO> res = sysModuleService.doSearch(paramSearch.getCode(), paramSearch.getName(), paramSearch.getStatus(), paramSearch.getParentId());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("module/getParent")
    public ResponseEntity<List<ParentModule>> findListParent() {
        List<ParentModule> rs = sysModuleService.getParent();
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("module/getTreeParent")
    public ResponseEntity<List<SysModule>> getTreeParent() {
        List<SysModule> rs = sysModuleService.getTreeParent(null);
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("module/getTreeParentActive")
    public ResponseEntity<List<SysModule>> getTreeParentRole() {
        List<SysModule> rs = sysModuleService.getTreeParent(1);
        return ResponseEntity.ok().body(rs);
    }
    /**
     * {@code POST  /sys-modules} : Create a new sysModule.
     *
     * @param sysModuleDTO the sysModuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysModuleDTO, or with status {@code 400 (Bad Request)} if the sysModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/module/insert")
    @PreAuthorize("hasPermission('MODULES', 'INSERT')")
    public ResponseEntity<SysModuleDTO> createSysModule(@Valid @RequestBody SysModuleDTO sysModuleDTO) throws URISyntaxException {
        log.debug("REST request to save SysModule : {}", sysModuleDTO);
        if (sysModuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysModuleDTO result = sysModuleService.save(sysModuleDTO);
        return ResponseEntity.created(new URI("/api/sys-modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-modules} : Updates an existing sysModule.
     *
     * @param sysModuleDTO the sysModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysModuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysModuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/module/update")
    @PreAuthorize("hasPermission('MODULES', 'UPDATE')")
    public ResponseEntity<SysModuleDTO> updateSysModule(@Valid @RequestBody SysModuleDTO sysModuleDTO) throws URISyntaxException {
        log.debug("REST request to update SysModule : {}", sysModuleDTO);
        if (sysModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysModuleDTO result = sysModuleService.save(sysModuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysModuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sys-modules} : get all the sysModules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysModules in body.
     */
    @GetMapping("/sys-modules")
    public ResponseEntity<List<SysModuleDTO>> getAllSysModules(Pageable pageable) {
        log.debug("REST request to get a page of SysModules");
        Page<SysModuleDTO> page = sysModuleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sys-modules/:id} : get the "id" sysModule.
     *
     * @param id the id of the sysModuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysModuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sys-modules/{id}")
    public ResponseEntity<SysModuleDTO> getSysModule(@PathVariable Long id) {
        log.debug("REST request to get SysModule : {}", id);
        Optional<SysModuleDTO> sysModuleDTO = sysModuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysModuleDTO);
    }

    /**
     * {@code DELETE  /sys-modules/:id} : delete the "id" sysModule.
     *
     * @param sysModuleDTO the id of the sysModuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/module/delete")
    @PreAuthorize("hasPermission('MODULES', 'DELETE')")
    public ResponseEntity<Void> deleteSysModule(@RequestBody SysModuleDTO sysModuleDTO) {
        log.debug("REST request to delete SysModule : {}", sysModuleDTO.getId());
        sysModuleService.delete(sysModuleDTO.getId(),sysModuleDTO.getCode());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, sysModuleDTO.getId().toString())).build();
    }

    @PostMapping("/module/multiple-delete")
    @PreAuthorize("hasPermission('MODULES', 'DELETE')")
    public ResponseEntity<Void> deleteMultipleSysAction(@Valid @RequestBody List<SysModuleDTO> sysModuleDTOS) {
        log.debug("REST request to delete multiple SysModule : {}", "");
        sysModuleService.deleteMultiple(sysModuleDTOS);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, sysModuleDTOS.size() + " item")).build();
    }
}
