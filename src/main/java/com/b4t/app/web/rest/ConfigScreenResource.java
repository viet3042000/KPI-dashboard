package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigArea;
import com.b4t.app.domain.ConfigScreen;
import com.b4t.app.service.*;
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
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigScreen}.
 */
@RestController
@RequestMapping("/api")
public class ConfigScreenResource {

    private final Logger log = LoggerFactory.getLogger(ConfigScreenResource.class);

    private static final String ENTITY_NAME = "configScreen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ConfigScreenService configScreenService;

    @Autowired
    private ConfigAreaService configAreaService;

    @Autowired
    private ConfigMenuItemService configMenuItemService;

    @Autowired
    private ConfigProfileService configProfileService;

    @Autowired
    private CatItemService catItemService;

    /**
     * {@code POST  /config-screens} : Create a new configScreen.
     *
     * @param configScreenDTO the configScreenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configScreenDTO, or with status {@code 400 (Bad Request)} if the configScreen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-screens")
    @PreAuthorize("hasPermission('DASHBOARD', 'INSERT')")
    public ResponseEntity<ConfigScreenDTO> createConfigScreen(@RequestBody ConfigScreenDTO configScreenDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigScreen : {}", configScreenDTO);
        if (configScreenDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        List<ConfigAreaDTO> areas = configScreenDTO.getConfigAreaDTOs();
        if (DataUtil.isNullOrEmpty(areas)) {
            throw new BadRequestAlertException(Translator.toLocale("error.screen.areanull"), ENTITY_NAME, "error.screen.areanull");
        }
        if (Constants.SCREEN_DEFAULT.equals(configScreenDTO.getIsDefault())) {
            List<ConfigScreenDTO> ckOpts = configScreenService.findAll(null, new Long[]{configScreenDTO.getProfileId()}, null, null, Constants.SCREEN_DEFAULT, null);
            if (!DataUtil.isNullOrEmpty(ckOpts)) {
                throw new BadRequestAlertException(Translator.toLocale("error.isdefaultduplicate"), ENTITY_NAME, "error.isdefaultduplicate");
            }
        }

        configScreenDTO.setStatus(Constants.STATUS_ACTIVE);
        ConfigScreenDTO result = configScreenService.save(configScreenDTO);
        return ResponseEntity.created(new URI("/api/config-screens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-screens} : Updates an existing configScreen.
     *
     * @param configScreenDTO the configScreenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configScreenDTO,
     * or with status {@code 400 (Bad Request)} if the configScreenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configScreenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-screens")
    @PreAuthorize("hasPermission('DASHBOARD', 'UPDATE')")
    public ResponseEntity<ConfigScreenDTO> updateConfigScreen(@RequestBody ConfigScreenDTO configScreenDTO) {
        log.debug("REST request to update ConfigScreen : {}", configScreenDTO);
        if (configScreenDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        List<ConfigAreaDTO> areas = configScreenDTO.getConfigAreaDTOs();
        if (DataUtil.isNullOrEmpty(areas)) {
            throw new BadRequestAlertException(Translator.toLocale("error.screen.areanull"), ENTITY_NAME, "error.screen.areanull");
        }
        Optional<ConfigScreenDTO> ckOpt = configScreenService.findOne(configScreenDTO.getId());
        if (ckOpt.isPresent() && !ckOpt.get().getUpdateTime().equals(configScreenDTO.getUpdateTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notlastestdata"), ENTITY_NAME, "error.notlastestdata");
        }
        if (Constants.SCREEN_DEFAULT.equals(configScreenDTO.getIsDefault())) {
            List<ConfigScreenDTO> ckOpts = configScreenService.findAll(null, new Long[]{configScreenDTO.getProfileId()}, null, null, Constants.SCREEN_DEFAULT, null);
            if (!DataUtil.isNullOrEmpty(ckOpts) && ckOpts.stream().anyMatch(s -> !configScreenDTO.getId().equals(s.getId()))) {
                throw new BadRequestAlertException(Translator.toLocale("error.isdefaultduplicate"), ENTITY_NAME, "error.isdefaultduplicate");
            }
        }

        ConfigScreenDTO result = configScreenService.save(configScreenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configScreenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-screens} : get all the configScreens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configScreens in body.
     */
    @GetMapping("/config-screens")
    public ResponseEntity<List<ConfigScreenDTO>> getAllConfigScreens(String keyword, Long[] profileIds, Long[] menuIds, Long[] menuItemIds, Long isDefault, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigScreens");
        List<CatItemDTO> lstProfileType = this.catItemService.getCatItemByCategoryCode("TYPE_DASHBOARD");
        Map<String, String> mapProfileType = lstProfileType.stream().collect(Collectors.toMap(CatItemDTO::getItemValue, CatItemDTO::getItemName, (o1,o2) -> o1));
        Page<ConfigScreenDTO> page = configScreenService.findAll(keyword, profileIds, menuIds, menuItemIds, isDefault, status, pageable);
        List<Long> menuItemOutIds = page.getContent().stream()
            .map(ConfigScreenDTO::getMenuItemId)
            .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<ConfigMenuItemDTO> menuItems = configMenuItemService.findByIds(menuItemOutIds);
        List<ConfigScreenDTO> rs = page.getContent().stream().peek(i -> {
            Optional<ConfigMenuItemDTO> menuItem = menuItems.stream().filter(m -> m.getId().equals(i.getMenuItemId())).findFirst();
            menuItem.ifPresent(i::setMenuItem);
            if(mapProfileType != null) {
                i.setType(mapProfileType.get(i.getIsDefault() != null ? i.getIsDefault().toString() : "0"));
            }
        }).collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(rs);
    }

    @GetMapping("/config-screens/get-slide-screen")
    public ResponseEntity<List<ConfigScreenDTO>> getSlideScreens(Long profileId) {
        log.debug("REST request to get a page of ConfigScreens");
        List<ConfigScreenDTO> screens = configScreenService.findAll(null, new Long[]{profileId}, null, null, null, null);

        screens.sort((a, b) -> {
            if (a.getIsDefault() == null && b.getIsDefault() != null) return 1;
            if (a.getIsDefault() != null && b.getIsDefault() == null) return -1;
            if (b.getIsDefault() != null && b.getIsDefault().compareTo(a.getIsDefault()) > 0) return 1;
            if (a.getOrderIndex() == null && b.getOrderIndex() != null) return 1;
            if (a.getOrderIndex() == null && b.getOrderIndex() == null) return 1;
            if (a.getOrderIndex() != null && b.getOrderIndex() == null) return -1;

            return a.getOrderIndex().compareTo(b.getOrderIndex());
        });

        return ResponseEntity.ok().body(screens);
    }

    /**
     * {@code GET  /config-screens/:id} : get the "id" configScreen.
     *
     * @param id the id of the configScreenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configScreenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-screens/{id}")
    public ResponseEntity<ConfigScreenDTO> getConfigScreen(@PathVariable Long id, Long groupChartId) {
        log.debug("REST request to get ConfigScreen : {}", id);
        Optional<ConfigScreenDTO> configScreenDTO = configScreenService.findOne(id);
        if (configScreenDTO.isPresent() && !Constants.STATUS_DISABLED.equals(configScreenDTO.get().getStatus())) {
            List<ConfigAreaDTO> areas = configAreaService.findByScreenIds(new Long[]{id}, groupChartId);
            List<Long> screenIds = areas.stream()
                .filter(a -> !DataUtil.isNullOrEmpty(a.getMapCharts()))
                .map(ConfigAreaDTO::getMapCharts)
                .flatMap(List::stream)
                .map(ConfigMapChartAreaDTO::getScreenIdNextto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            List<ConfigScreenDTO> screens = configScreenService.findByIds(screenIds);
            areas.forEach(a -> {
                if (!DataUtil.isNullOrEmpty(a.getMapCharts())) {
                    a.getMapCharts().forEach(ma -> {
                        Optional<ConfigScreenDTO> screen = screens.stream().filter(s -> s.getId().equals(ma.getScreenIdNextto())).findFirst();
                        screen.ifPresent(screenDTO -> ma.setScreenIdNexttoName(screenDTO.getScreenName()));
                    });
                }
            });
            configScreenDTO.get().setConfigAreaDTOs(areas);
            if (configScreenDTO.get().getMenuItemId() != null) {
                Optional<ConfigMenuItemDTO> menuItemDTO = configMenuItemService.findOne(configScreenDTO.get().getMenuItemId());
                Optional<ConfigScreenDTO> finalConfigScreenDTO = configScreenDTO;
                menuItemDTO.ifPresent(configMenuItemDTO -> finalConfigScreenDTO.get().setMenuItem(configMenuItemDTO));
            }
            Optional<ConfigProfileDTO> profileDTO = configProfileService.findOne(configScreenDTO.get().getProfileId());
            if (profileDTO.isPresent()) {
                configScreenDTO.get().setProfileType(profileDTO.get().getType());
            }
        } else {
            configScreenDTO = Optional.empty();
        }
        return ResponseUtil.wrapOrNotFound(configScreenDTO);
    }

    /**
     * {@code DELETE  /config-screens/:id} : delete the "id" configScreen.
     *
     * @param id the id of the configScreenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-screens/{id}")
    @PreAuthorize("hasPermission('DASHBOARD', 'DELETE')")
    public ResponseEntity<Void> deleteConfigScreen(@PathVariable Long id) {
        log.debug("REST request to delete ConfigScreen : {}", id);
        Optional<ConfigScreenDTO> ckOpt = configScreenService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        configScreenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/config-screens/delete-all")
    @PreAuthorize("hasPermission('DASHBOARD', 'DELETE')")
    public ResponseEntity<Void> deleteConfigScreen(@RequestBody  Long[] ids) {
        log.debug("REST request to delete ConfigScreen : {}", ids);
        if (DataUtil.isNullOrEmpty(ids))
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        List<ConfigScreenDTO> screens = configScreenService.findByIds(Arrays.asList(ids));
        if (DataUtil.isNullOrEmpty(screens) || screens.stream().anyMatch(s -> Constants.STATUS_DISABLED.equals(s.getStatus()))) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        configScreenService.delete(screens);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, ids.toString())).build();
    }


    @PostMapping("/config-screens/copy/{id}")
    public ResponseEntity<ConfigScreenDTO> copyConfigScreen(@PathVariable Long id) {
        log.debug("REST request to delete ConfigScreen : {}", id);
        Optional<ConfigScreenDTO> ckOpt = configScreenService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        ConfigScreenDTO rs = configScreenService.copy(id);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(rs);

    }

    @GetMapping("/config-screens/find-screen-by-profile-id")
    public ResponseEntity<List<ConfigScreenDTO>> findSreenWithProfile(Long profileId) {
        List<ConfigScreenDTO> results = configScreenService.findSreenWithProfile(profileId);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/config-screens/find-screen-by-profile-and-parent")
    public ResponseEntity<List<ConfigScreenDTO>> findSreenWithProfileAndParent(Long profileId, Long parentId) {
        List<ConfigScreenDTO> results = configScreenService.findSreenWithProfileAndParent(profileId, parentId);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/config-screens/find-screen-root")
    public ResponseEntity<List<ConfigScreenDTO>> findScreenRoot(Long profileId) {
        List<ConfigScreenDTO> results = configScreenService.findScreenRoot(profileId);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/config-screens/find-screen-home")
    public ResponseEntity<List<ConfigScreenDTO>> findScreenHome(Long profileId) {
        List<ConfigScreenDTO> results = configScreenService.findScreenHome(profileId);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/config-screens/find-tabs-for-screen")
    public ResponseEntity<List<ConfigScreenDTO>> findTabsForScreen(Long profileId, Long curentScreenId, Long parentId) {
        List<ConfigScreenDTO> results = configScreenService.findTabsForScreen(profileId, curentScreenId, parentId);
        return ResponseEntity.ok().body(results);
    }

    @PostMapping("/config-screens/find-keyword")
    public ResponseEntity<List<ConfigScreenDTO>> findKeyword(String keyword) {
        List<ConfigScreenDTO> results = configScreenService.findAllByKeywordAndScreenType(keyword, Constants.SCREEN_DETAIL_CHILD);
        return ResponseEntity.ok().body(results);
    }


    @GetMapping("/config-screens/find-screen-detail-child")
    public ResponseEntity<List<ConfigScreenDTO>> findScreenDetailChild(String keyword) {
        List<ConfigScreenDTO> results = configScreenService.findAllScreenType(Arrays.asList(Constants.SCREEN_DETAIL_CHILD, Constants.SCREEN_NORMAL));
        return ResponseEntity.ok().body(results);
    }
}
