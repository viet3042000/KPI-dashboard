package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMapGroupChartAreaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMapGroupChartArea}.
 */
public interface ConfigMapGroupChartAreaService {

    /**
     * Save a configMapGroupChartArea.
     *
     * @param configMapGroupChartAreaDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigMapGroupChartAreaDTO save(ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO);

    List<ConfigMapGroupChartAreaDTO> saveAll(List<ConfigMapGroupChartAreaDTO> dtos);

    /**
     * Get all the configMapGroupChartAreas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigMapGroupChartAreaDTO> findAll(Long[] groupChartIds,Long[] areaIds, Long status, Pageable pageable);

    List<ConfigMapGroupChartAreaDTO> findAll(Long[] groupChartIds, Long[] areaIds, Long status);

    /**
     * Get the "id" configMapGroupChartArea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigMapGroupChartAreaDTO> findOne(Long id);

    /**
     * Delete the "id" configMapGroupChartArea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void delete(Long[] areaIds, Long[] groupChartIds);
}
