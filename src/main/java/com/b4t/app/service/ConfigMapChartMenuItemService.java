package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMapChartMenuItem}.
 */
public interface ConfigMapChartMenuItemService {

    /**
     * Save a configMapChartMenuItem.
     *
     * @param configMapChartMenuItemDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMapChartMenuItemDTO save(ConfigMapChartMenuItemDTO configMapChartMenuItemDTO);

    List<ConfigMapChartMenuItemDTO> saveAll(List<ConfigMapChartMenuItemDTO> configMapChartMenuItemDTOs);

    /**
     * Get all the configMapChartMenuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMapChartMenuItemDTO> findAll(Long[] chartIds, Long[] menuItemIds, Long isMain, Long status, Pageable pageable);

    List<ConfigMapChartMenuItemDTO> findAll(Long[] chartIds, Long[] menuItemIds, Long isMain, Long status);

    /**
     * Get the "id" configMapChartMenuItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMapChartMenuItemDTO> findOne(Long id);

    /**
     * Delete the "id" configMapChartMenuItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void delete(ConfigMapChartMenuItemDTO dto);

    void delete(List<ConfigMapChartMenuItemDTO> dtos);

    void delete(Long[] menuItemIds, Long[] chartIds);
}
