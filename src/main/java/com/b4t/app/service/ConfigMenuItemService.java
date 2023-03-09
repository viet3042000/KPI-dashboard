package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMenuItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMenuItem}.
 */
public interface ConfigMenuItemService {

    /**
     * Save a configMenuItem.
     *
     * @param configMenuItemDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMenuItemDTO save(ConfigMenuItemDTO configMenuItemDTO);

    List<ConfigMenuItemDTO> saveAll(List<ConfigMenuItemDTO> configAreaDTOS);

    void preSave(ConfigMenuItemDTO configMenuItemDTO);

    /**
     * Get all the configMenuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMenuItemDTO> findAll(String keyword, Long[] menuId, Long isDefault, Long status, Pageable pageable);

    Page<ConfigMenuItemDTO> findAllRelate(String keyword, Long[] menuId, Long isDefault, Long status, Pageable pageable);

    List<ConfigMenuItemDTO> findAll(String keyword, Long[] menuId, Long isDefault, Long status);

    List<ConfigMenuItemDTO> findByIds(List<Long> ids);

    /**
     * Get the "id" configMenuItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMenuItemDTO> findOne(Long id);

    Optional<ConfigMenuItemDTO> findOneAllInfo(Long id);

    Optional<ConfigMenuItemDTO> findByCode(String code);

    /**
     * Delete the "id" configMenuItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete the ConfigMenuItem.
     *
     * @param dto dto.
     */
    void delete(ConfigMenuItemDTO dto);

    /**
     * Delete the ConfigMenuItem.
     *
     * @param dtos list of dto.
     */
    void delete(List<ConfigMenuItemDTO> dtos);
}
