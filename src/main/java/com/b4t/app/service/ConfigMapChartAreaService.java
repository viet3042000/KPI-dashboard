package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMapChartAreaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMapChartArea}.
 */
public interface ConfigMapChartAreaService {

    /**
     * Save a configMapChartArea.
     *
     * @param configMapChartAreaDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMapChartAreaDTO save(ConfigMapChartAreaDTO configMapChartAreaDTO);

    List<ConfigMapChartAreaDTO> saveAll(List<ConfigMapChartAreaDTO> configMapChartAreaDTOS);

    /**
     * Get all the configMapChartAreas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMapChartAreaDTO> findAll(Long[] chartIds, Long[] areaIds, Long[] groupChartIds, Long status, Pageable pageable);

    /**
     * Get all the configMapChartAreas.
     *
     * @return the list of entities.
     */
    List<ConfigMapChartAreaDTO> findAll(Long[] chartIds, Long[] areaIds, Long[] groupChartIds, Long status);

    /**
     * Get the "id" configMapChartArea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMapChartAreaDTO> findOne(Long id);

    /**
     * Delete the "id" configMapChartArea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete the configMapChartArea.
     *
     * @param dto dto.
     */
    void delete(ConfigMapChartAreaDTO dto);

    /**
     * Delete the configMapChartArea.
     *
     * @param dtos list of dto.
     */
    void delete(List<ConfigMapChartAreaDTO> dtos);

    void delete(Long[] areaIds, Long[] chartIds);
}
