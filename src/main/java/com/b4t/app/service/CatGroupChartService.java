package com.b4t.app.service;

import com.b4t.app.service.dto.CatGroupChartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.CatGroupChart}.
 */
public interface CatGroupChartService {

    /**
     * Save a catGroupChart.
     *
     * @param catGroupChartDTO the entity to save.
     * @return the persisted entity.
     */
    CatGroupChartDTO save(CatGroupChartDTO catGroupChartDTO);

    /**
     * Get all the catGroupCharts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatGroupChartDTO> findAll(String keyword, String[] groupKpiCodes, Long[] screenIds, Long status, Pageable pageable);

    /**
     * Get the "id" catGroupChart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatGroupChartDTO> findOne(Long id);

    public Optional<CatGroupChartDTO> findByCode(String code);

    /**
     * Delete the "id" catGroupChart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
