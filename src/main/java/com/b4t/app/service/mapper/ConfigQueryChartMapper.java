package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigQueryChartDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigQueryChart} and its DTO {@link ConfigQueryChartDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigQueryChartMapper extends EntityMapper<ConfigQueryChartDTO, ConfigQueryChart> {



    default ConfigQueryChart fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigQueryChart configQueryChart = new ConfigQueryChart();
        configQueryChart.setId(id);
        return configQueryChart;
    }
}
