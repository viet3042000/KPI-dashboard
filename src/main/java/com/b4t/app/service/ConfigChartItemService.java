package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigChartItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigChartItem}.
 */
public interface ConfigChartItemService {

    /**
     * Save a configChartItem.
     *
     * @param configChartItemDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigChartItemDTO save(ConfigChartItemDTO configChartItemDTO);

    List<ConfigChartItemDTO> saveAll(List<ConfigChartItemDTO> dtos);
    /**
     * Get all the configChartItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigChartItemDTO> findAll(Pageable pageable);

    List<ConfigChartItemDTO> findByChartIds(List<Long> chartIds);

    /**
     * Get configChartItems by chartId.
     *
     * @param chartId id of chart.
     * @return the list of entities.
     */
    List<ConfigChartItemDTO> findByChartId(Long chartId);

    /**
     * Get the "id" configChartItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigChartItemDTO> findOne(Long id);

    /**
     * Delete the "id" configChartItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void delete(List<ConfigChartItemDTO> dtos);
}
