package com.b4t.app.web.rest;


import com.b4t.app.domain.SysModuleAction;
import com.b4t.app.service.SysModuleActionService;
import com.b4t.app.service.dto.SysModuleActionDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link com.b4t.app.domain.SysModuleAction}.
 */
@RestController
@RequestMapping("/api")
public class SysModuleActionResource {

    private final Logger log = LoggerFactory.getLogger(SysModuleActionResource.class);

    private static final String ENTITY_NAME = "sysModuleAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysModuleActionService sysModuleActionService;

    public SysModuleActionResource(SysModuleActionService sysModuleActionService) {
        this.sysModuleActionService = sysModuleActionService;
    }

    /**
     * {@code PUT  /sys-module-actions} : insert sysModuleAction.
     *
     * @param listInsert the sysModuleActionDTO to insert.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insert sysModuleActionDTO,
     * or with status {@code 400 (Bad Request)} if the sysModuleActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysModuleActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/moduleAction/insert")
    @PreAuthorize("hasPermission('MODULES', 'MODULE_ACTION')")
    public ResponseEntity<Object> insertSysModuleAction(@RequestBody List<SysModuleActionDTO> listInsert) throws URISyntaxException {
        sysModuleActionService.saveMultiple(listInsert);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/moduleAction/getAllByModuleId")
    public ResponseEntity<List<SysModuleAction>> getAllByModuleId(@RequestParam("id") Long id) {
        List<SysModuleAction> result = sysModuleActionService.getAllByModuleId(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id)))
                .body(result);
    }

    @PostMapping("/moduleAction/delete")
    @PreAuthorize("hasPermission('MODULES', 'MODULE_ACTION')")
    public ResponseEntity<Object> delete(@RequestBody List<SysModuleAction> listDelete) {
        sysModuleActionService.deteleMultiple(listDelete);
        return ResponseEntity.ok()
                .build();
    }
}
