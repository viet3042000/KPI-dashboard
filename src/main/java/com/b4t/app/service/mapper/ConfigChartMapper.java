package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigChartDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigChart} and its DTO {@link ConfigChartDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigChartMapper extends EntityMapper<ConfigChartDTO, ConfigChart> {
    default ConfigChart fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigChart configChart = new ConfigChart();
        configChart.setId(id);
        return configChart;
    }
}
