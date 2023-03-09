package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.CatGroupChartDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatGroupChart} and its DTO {@link CatGroupChartDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatGroupChartMapper extends EntityMapper<CatGroupChartDTO, CatGroupChart> {



    default CatGroupChart fromId(Long id) {
        if (id == null) {
            return null;
        }
        CatGroupChart catGroupChart = new CatGroupChart();
        catGroupChart.setId(id);
        return catGroupChart;
    }
}
