package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigMenu;
import com.b4t.app.service.*;
import com.b4t.app.service.ConfigMenuService;

import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigMenu}.
 */
@RestController
@RequestMapping("/api")
public class ConfigMenuResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMenuResource.class);

    private static final String ENTITY_NAME = "configMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ConfigMenuService configMenuService;

    @Autowired
    private ConfigMenuItemService configMenuItemService;

    @Autowired
    private ConfigMapChartMenuItemService configMapChartMenuItemService;

    @Autowired
    private ConfigScreenService configScreenService;

    @Autowired
    private ConfigProfileService configProfileService;

    /**
     * {@code POST  /config-menus} : Create a new configMenu.
     *
     * @param configMenuDTO the configMenuDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configMenuDTO, or with status {@code 400 (Bad Request)} if the configMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-menus")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<ConfigMenuDTO> createConfigMenu(@RequestBody ConfigMenuDTO configMenuDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigMenu : {}", configMenuDTO);
        if (configMenuDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        ConfigMenuDTO result = configMenuService.save(configMenuDTO);
        return ResponseEntity.created(new URI("/api/config-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-menus} : Updates an existing configMenu.
     *
     * @param configMenuDTO the configMenuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configMenuDTO,
     * or with status {@code 400 (Bad Request)} if the configMenuDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configMenuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-menus")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<ConfigMenuDTO> updateConfigMenu(@RequestBody ConfigMenuDTO configMenuDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigMenu : {}", configMenuDTO);
        if (configMenuDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        Optional<ConfigMenuDTO> ckOpt = configMenuService.findOne(configMenuDTO.getId());
        if (ckOpt.isPresent() && !ckOpt.get().getUpdateTime().equals(configMenuDTO.getUpdateTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notlastestdata"), ENTITY_NAME, "error.notlastestdata");
        }
        ConfigMenuDTO result = configMenuService.save(configMenuDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configMenuDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-menus} : get all the configMenus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configMenus in body.
     */
    @GetMapping("/config-menus")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<List<ConfigMenuDTO>> getAllConfigMenus(String keyword, String domainCode, Long status, @PageableDefault(value = Integer.MAX_VALUE) Pageable pageable) {
        log.debug("REST request to get a page of ConfigMenus");
        Page<ConfigMenuDTO> page = configMenuService.findAll(keyword, domainCode, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/config-menus/get-by-profile")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<List<ConfigMenuDTO>> getAllByProfileId(Long profileId) {
        log.debug("REST request to get a page of ConfigMenus");
        Optional<ConfigProfileDTO> profile = configProfileService.findOne(profileId);
        if (!profile.isPresent() || Constants.STATUS_DISABLED.equals(profile.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notexisted"), ENTITY_NAME, "error.notexisted");
        }

        List<ConfigMenuDTO> rs = configMenuService.findAllByProfileIds(new Long[]{profileId});
        return ResponseEntity.ok().body(rs);
    }

    /**
     * {@code GET  /config-menus/:id} : get the "id" configMenu.
     *
     * @param id the id of the configMenuDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configMenuDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-menus/{id}")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<ConfigMenuDTO> getConfigMenu(@PathVariable Long id, Long[] profileIds, Boolean showAllItems) {
        log.debug("REST request to get ConfigMenu : {}", id);
        Optional<ConfigMenuDTO> configMenuDTO = configMenuService.findOne(id);
        if (configMenuDTO.isPresent()) {
            List<ConfigMenuItemDTO> menuItems = configMenuItemService.findAll(null, new Long[]{id}, null, null);
            Long[] menuItemIds = menuItems.stream().map(ConfigMenuItemDTO::getId).toArray(Long[]::new);
            List<ConfigMapChartMenuItemDTO> mapCharts = configMapChartMenuItemService.findAll(null, menuItemIds, null, Constants.STATUS_ACTIVE);
            List<ConfigScreenDTO> screens = configScreenService.findAll(null, profileIds, null, menuItemIds, null, null);
            menuItems.forEach(item -> {
                List<ConfigMapChartMenuItemDTO> itemMapCharts =
                    mapCharts.stream()
                        .filter(mc -> item.getId().equals(mc.getMenuItemId()))
                        .collect(Collectors.toList());
                item.setMapCharts(itemMapCharts);

                List<Long> screenIds = screens.stream()
                    .filter(s -> item.getId().equals(s.getMenuItemId()))
                    .sorted((a, b) -> {
                        if (a.getOrderIndex() == null && b.getOrderIndex() != null) return 1;
                        if (a.getOrderIndex() == null && b.getOrderIndex() == null) return 1;
                        if (a.getOrderIndex() != null && b.getOrderIndex() == null) return -1;
                        return a.getOrderIndex().compareTo(b.getOrderIndex());
                    })
                    .map(ConfigScreenDTO::getId)
                    .collect(Collectors.toList());
                item.setScreenIds(screenIds);
            });
            if (showAllItems == null || !showAllItems) {
                menuItems = menuItems.stream().filter(i -> !DataUtil.isNullOrEmpty(i.getScreenIds())).collect(Collectors.toList());
            }
            configMenuDTO.get().setItems(menuItems);
        }
        return ResponseUtil.wrapOrNotFound(configMenuDTO);
    }

    /**
     * {@code DELETE  /config-menus/:id} : delete the "id" configMenu.
     *
     * @param id the id of the configMenuDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-menus/{id}")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<Void> deleteConfigMenu(@PathVariable Long id) {
        log.debug("REST request to delete ConfigMenu : {}", id);
        configMenuService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/config-menus/delete-multiple")
    @PreAuthorize("hasPermission('MENU', '*')")
    public ResponseEntity<Void> deleteConfigMenuMultiple(@RequestBody List<ConfigMenu> configMenus) {
//        log.debug("REST request to delete ConfigMenu : {}", id);
        for(ConfigMenu con : configMenus){
            Optional<ConfigMenuDTO> ckOpt = configMenuService.findOne(con.getId());
            if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(con.getStatus())) {
                throw new BadRequestAlertException(Translator.toLocale("error.disabled.item"), ENTITY_NAME, "error.disabled.item");
            }
            configMenuService.delete(con.getId());
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, "")).build();
    }
}
