package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.service.ConfigProfileService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.ConfigProfileDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigProfile}.
 */
@RestController
@RequestMapping("/api")
public class ConfigProfileResource {

    private final Logger log = LoggerFactory.getLogger(ConfigProfileResource.class);

    private static final String ENTITY_NAME = "configProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigProfileService configProfileService;

    public ConfigProfileResource(ConfigProfileService configProfileService) {
        this.configProfileService = configProfileService;
    }

    /**
     * {@code POST  /config-profiles} : Create a new configProfile.
     *
     * @param configProfileDTO the configProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configProfileDTO, or with status {@code 400 (Bad Request)} if the configProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-profiles")
    @PreAuthorize("hasPermission('PROFILE', 'INSERT')")
    public ResponseEntity<ConfigProfileDTO> createConfigProfile(@RequestBody ConfigProfileDTO configProfileDTO) throws URISyntaxException {
        log.debug("REST request to save ConfigProfile : {}", configProfileDTO);
        if (configProfileDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        if (Constants.PROFILE_MAIN.equals(configProfileDTO.getIsDefault())) {
            List<ConfigProfileDTO> ckOpts = configProfileService.findAll(null, null, Constants.PROFILE_MAIN, null);
            if (!DataUtil.isNullOrEmpty(ckOpts)) {
                throw new BadRequestAlertException(Translator.toLocale("error.isdefaultduplicate"), ENTITY_NAME, "error.isdefaultduplicate");
            }
        }
        configProfileDTO.setStatus(Constants.STATUS_ACTIVE);
        ConfigProfileDTO result = configProfileService.save(configProfileDTO);
        return ResponseEntity.created(new URI("/api/config-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-profiles} : Updates an existing configProfile.
     *
     * @param configProfileDTO the configProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configProfileDTO,
     * or with status {@code 400 (Bad Request)} if the configProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-profiles")
    @PreAuthorize("hasPermission('PROFILE', 'UPDATE')")
    public ResponseEntity<ConfigProfileDTO> updateConfigProfile(@RequestBody ConfigProfileDTO configProfileDTO) throws URISyntaxException {
        log.debug("REST request to update ConfigProfile : {}", configProfileDTO);
        if (configProfileDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        Optional<ConfigProfileDTO> ckOpt = configProfileService.findOne(configProfileDTO.getId());
        if (ckOpt.isPresent() && !ckOpt.get().getUpdateTime().equals(configProfileDTO.getUpdateTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notlastestdata"), ENTITY_NAME, "notlastestdata");
        }
        if (Constants.PROFILE_MAIN.equals(configProfileDTO.getIsDefault())) {
            List<ConfigProfileDTO> ckOpts = configProfileService.findAll(null, null, Constants.PROFILE_MAIN, null);
            if (!DataUtil.isNullOrEmpty(ckOpts) && ckOpts.stream().anyMatch(p -> !p.getId().equals(configProfileDTO.getId()))) {
                throw new BadRequestAlertException(Translator.toLocale("error.isdefaultduplicate"), ENTITY_NAME, "error.isdefaultduplicate");
            }
        }
        configProfileDTO.setStatus(Constants.STATUS_ACTIVE);
        ConfigProfileDTO result = configProfileService.save(configProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-profiles} : get all the configProfiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configProfiles in body.
     */
    @GetMapping("/config-profiles")
    @PreAuthorize("hasPermission('USER', 'GET')")
    public ResponseEntity<List<ConfigProfileDTO>> getAllConfigProfiles(String keyword, Boolean hasScreenOnly, Long isDefault, Long status, Pageable pageable) {
        log.debug("REST request to get a page of ConfigProfiles");
        Page<ConfigProfileDTO> page = configProfileService.findAll(keyword, hasScreenOnly, isDefault, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-profiles/:id} : get the "id" configProfile.
     *
     * @param id the id of the configProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-profiles/{id}")
    @PreAuthorize("hasPermission('PROFILE', '*')")
    public ResponseEntity<ConfigProfileDTO> getConfigProfile(@PathVariable Long id) {
        log.debug("REST request to get ConfigProfile : {}", id);
        Optional<ConfigProfileDTO> configProfileDTO = configProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configProfileDTO);
    }

    /**
     * {@code DELETE  /config-profiles/:id} : delete the "id" configProfile.
     *
     * @param id the id of the configProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-profiles/{id}")
    @PreAuthorize("hasPermission('PROFILE', 'DELETE')")
    public ResponseEntity<Void> deleteConfigProfile(@PathVariable Long id) {
        log.debug("REST request to delete ConfigProfile : {}", id);
        Optional<ConfigProfileDTO> ckOpt = configProfileService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        configProfileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/config-profiles/delete-all")
    @PreAuthorize("hasPermission('PROFILE', 'DELETE')")
    public ResponseEntity<Void> deleteConfigProfile(@RequestBody Long[] ids) {
        log.debug("REST request to delete ConfigProfile : {}", ids);
        if (DataUtil.isNullOrEmpty(ids))
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        List<ConfigProfileDTO> profiles = configProfileService.findByIds(Arrays.asList(ids));
        if (DataUtil.isNullOrEmpty(profiles) || profiles.stream().anyMatch(p -> Constants.STATUS_DISABLED.equals(p.getStatus()))) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        configProfileService.deleteAll(profiles);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, ids.toString())).build();
    }

    /**
     * {@code DELETE  /config-profiles/:id} : delete the "id" configProfile.
     *
     * @param id the id, new config of the configProfileDTO to copy.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/config-profiles/copy/{id}")
    @PreAuthorize("hasPermission('PROFILE', '*')")
    public ResponseEntity<ConfigProfileDTO> copyConfigProfile(@PathVariable Long id) {
        log.debug("REST request to copy ConfigProfile : {}", id);
        Optional<ConfigProfileDTO> ckOpt = configProfileService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        ConfigProfileDTO newDto = configProfileService.copy(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, newDto.getId().toString()))
            .body(newDto);
    }
}
