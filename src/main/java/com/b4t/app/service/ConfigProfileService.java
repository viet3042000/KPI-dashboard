package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigProfileDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigProfile}.
 */
public interface ConfigProfileService {

    /**
     * Save a configProfile.
     *
     * @param configProfileDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigProfileDTO save(ConfigProfileDTO configProfileDTO);

    /**
     * Get all the configProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigProfileDTO> findAll(String keyword, Boolean hasScreenOnly, Long isDefault, Long status, Pageable pageable);

    List<ConfigProfileDTO> findAll(String keyword, Boolean hasScreenOnly, Long isDefault, Long status);

    /**
     * Get all the configProfiles.
     *
     * @param code the code information.
     * @return the list of entities.
     */
    Optional<ConfigProfileDTO> findByCode(String code);

    /**
     * Get the "id" configProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigProfileDTO> findOne(Long id);

    List<ConfigProfileDTO> findByIds(List<Long> ids);

    /**
     * Delete the "id" configProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteAll(List<ConfigProfileDTO> dtos);

    ConfigProfileDTO copy(Long cloneId);
}
