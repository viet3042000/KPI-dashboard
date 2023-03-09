package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigQueryChartDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigQueryChart}.
 */
public interface ConfigQueryChartService {

    /**
     * Save a configQueryChart.
     *
     * @param configQueryChartDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigQueryChartDTO save(ConfigQueryChartDTO configQueryChartDTO);

    /**
     * Get all the configQueryCharts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigQueryChartDTO> findAll(Pageable pageable);

    List<ConfigQueryChartDTO> findByIds(List<Long> ids);

    /**
     * Get the "id" configQueryChart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigQueryChartDTO> findOne(Long id);

    /**
     * Delete the "id" configQueryChart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void delete(List<Long> ids);
}
