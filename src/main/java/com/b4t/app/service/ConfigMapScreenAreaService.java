package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigMapChartAreaDTO;
import com.b4t.app.service.dto.ConfigMapScreenAreaDTO;

import java.util.List;
/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigMapScreenArea}.
 */
public interface ConfigMapScreenAreaService {

    List<ConfigMapScreenAreaDTO> saveAll(List<ConfigMapScreenAreaDTO> configMapScreenAreaDTOS);
    /**
     * Get all the configMapChartAreas.
     *
     * @return the list of entities.
     */
    List<ConfigMapScreenAreaDTO> findAll(Long[] screenIds, Long[] areaIds,  Long status);

    void delete(Long[] areaIds, Long[] screenIds);

    void delete(List<ConfigMapScreenAreaDTO> dtos);
}
