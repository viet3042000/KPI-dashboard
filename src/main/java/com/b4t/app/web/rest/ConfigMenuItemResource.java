package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMenu;
import com.b4t.app.domain.ConfigMenuItem;
import com.b4t.app.service.ConfigMenuItemService;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigMenuItemDTO;

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
 * REST controller for managing {@link com.b4t.app.domain.ConfigMenuItem}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMenuItemResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMenuItemResource.class);

    private static final String ENTITY_NAME = "configMenuItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigMenuItemService configMenuItemService;

    public ConfigMenuItemResource(ConfigMenuItemService configMenuItemService) {
        this.configMenuItemService = configMenuItemService;
    }

    /**
     * {@code POST  /config-menu-items} : Create a new configMenuItem.
     *
     * @param configMenuItemDTO the configMenuItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMenuItemDTO, or with status {@code 400 (Bad Request)} if the configMenuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-menu-items")
    @PreAuthorize("hasPermission('MENU-ITEM', '*')")
    public ResponseEntity<ConfigMenuItemDTO> createConfigMenuItem(@RequestBody ConfigMenuItemDTO configMenuItemDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMenuItem : {}", configMenuItemDTO);
        if (configMenuItemDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        configMenuItemService.preSave(configMenuItemDTO);
        ConfigMenuItemDTO result = configMenuItemService.save(configMenuItemDTO);
        return ResponseEntity.created(new URI("/api/config-menu-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-menu-items} : Updates an existing configMenuItem.
     *
     * @param configMenuItemDTO the configMenuItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMenuItemDTO,
     * or with status {@code 400 (Bad Request)} if the configMenuItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMenuItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-menu-items")
    @PreAuthorize("hasPermission('MENU-ITEM', '*')")
    public ResponseEntity<ConfigMenuItemDTO> updateConfigMenuItem(@RequestBody ConfigMenuItemDTO configMenuItemDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMenuItem : {}", configMenuItemDTO);
        if (configMenuItemDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        Optional<ConfigMenuItemDTO> ckOpt = configMenuItemService.findOne(configMenuItemDTO.getId());
        if (ckOpt.isPresent() && !ckOpt.get().getUpdateTime().equals(configMenuItemDTO.getUpdateTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notlastestdata"), ENTITY_NAME, "error.notlastestdata");
        }
        configMenuItemService.preSave(configMenuItemDTO);
        ConfigMenuItemDTO result = configMenuItemService.save(configMenuItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMenuItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-menu-items} : get all the configMenuItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMenuItems in body.
     */
    @GetMapping("/config-menu-items")
    public ResponseEntity<List<ConfigMenuItemDTO>> getAllConfigMenuItems(String keyword, Long[] menuIds, Long isDefault, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigMenuItems");
        Page<ConfigMenuItemDTO> page = configMenuItemService.findAll(keyword, menuIds, isDefault, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/config-menu-items/all-info")
    @PreAuthorize("hasPermission('MENU-ITEM', '*')")
    public ResponseEntity<List<ConfigMenuItemDTO>> getMenuItems(String keyword, Long[] menuIds, Long isDefault, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigMenuItems");
        Page<ConfigMenuItemDTO> page = configMenuItemService.findAllRelate(keyword, menuIds, isDefault, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-menu-items/:id} : get the "id" configMenuItem.
     *
     * @param id the id of the configMenuItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMenuItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-menu-items/{id}")
    public ResponseEntity<ConfigMenuItemDTO> getConfigMenuItem(@PathVariable Long id) {
        log.debug("REST request to get ConfigMenuItem : {}", id);
        Optional<ConfigMenuItemDTO> configMenuItemDTO = configMenuItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMenuItemDTO);
    }

    @GetMapping("/config-menu-items/allInfo/{id}")
    public ResponseEntity<ConfigMenuItemDTO> getConfigMenuItemAllInfo(@PathVariable Long id) {
        log.debug("REST request to get ConfigMenuItem : {}", id);
        Optional<ConfigMenuItemDTO> configMenuItemDTO = configMenuItemService.findOneAllInfo(id);
        return ResponseUtil.wrapOrNotFound(configMenuItemDTO);
    }

    /**
     * {@code DELETE  /config-menu-items/:id} : delete the "id" configMenuItem.
     *
     * @param id the id of the configMenuItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-menu-items/{id}")
    @PreAuthorize("hasPermission('MENU-ITEM', '*')")
    public ResponseEntity<Void> deleteConfigMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMenuItem : {}", id);
        Optional<ConfigMenuItemDTO> ckOpt = configMenuItemService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.not.exist.item"), ENTITY_NAME, "error.not.exist.item");
        }
        configMenuItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/config-menu-items/delete-multiple")
    @PreAuthorize("hasPermission('MENU-ITEM', '*')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> deleteConfigMenuItemMultiple(@RequestBody List<ConfigMenuItem> configMenuItems) {
//        log.debug("REST request to delete ConfigMenuItem : {}", id);
//        Optional<ConfigMenuItemDTO> ckOpt = configMenuItemService.findOne(id);
        for(ConfigMenuItem con : configMenuItems){
            Optional<ConfigMenuItemDTO> ckOpt = configMenuItemService.findOne(con.getId());
            if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(con.getStatus())) {
                throw new BadRequestAlertException(Translator.toLocale("error.disabled.item"), ENTITY_NAME, "error.disabled.item");
            }
            configMenuItemService.delete(con.getId());
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build();
    }

}
