package com.b4t.app.service;

import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.dto.ConfigChartDTO;
import com.b4t.app.service.dto.ConfigChartDetailDTO;
import com.b4t.app.service.dto.SaveChartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigChart}.
 */
public interface ConfigChartService {

    /**
     * Save a configChart.
     *
     * @param configChartDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigChartDTO save(ConfigChartDTO configChartDTO);

    SaveChartDTO clone(Long id);

    /**
     * Get all the configCharts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigChartDTO> findAll(String keyword, String typeChart, Long[] groupChartIds, String[] domainCodes, String[] groupKpiCodes,
                                 Integer timeTypeDefault, Long status, Integer childChart, Long[] listChartNotIn, Long[] listChartIn, Pageable pageable);

    List<ConfigChartDTO> findConfigChartByIds(List<Long> chartIds);
    /**
     * Get the "id" configChart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigChartDTO> findOne(Long id);

    Optional<ConfigChartDTO> findByCode(String code);
    /**
     * Delete the "id" configChart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void delete(List<ConfigChartDTO> dtos);

    /**
     * Find all info of chartId
     * @param chartId
     * @return
     */
    List<ConfigChartDetailDTO> findByChartId(Long chartId);

    List<ConfigChartDTO> findByIds(List<Long> ids);

    List<ComboDTO> onSearchChart(Long profileId, String keyword);
}
