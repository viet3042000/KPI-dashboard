package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMenuDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMenu}.
 */
public interface ConfigMenuService {

    /**
     * Save a configMenu.
     *
     * @param configMenuDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMenuDTO save(ConfigMenuDTO configMenuDTO);

    /**
     * Get all the configMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMenuDTO> findAll(String keyword, String domainCode, Long status, Pageable pageable);

    List<ConfigMenuDTO> findAllByProfileIds(Long[] profileIds);

    /**
     * Get the "id" configMenu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMenuDTO> findOne(Long id);

    Optional<ConfigMenuDTO> findByCode(String code);
    /**
     * Delete the "id" configMenu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
