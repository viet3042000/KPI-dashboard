package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.MapReportDataToDashboardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MapReportDataToDashboard} and its DTO {@link MapReportDataToDashboardDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MapReportDataToDashboardMapper extends EntityMapper<MapReportDataToDashboardDTO, MapReportDataToDashboard> {



    default MapReportDataToDashboard fromId(Long id) {
        if (id == null) {
            return null;
        }
        MapReportDataToDashboard mapReportDataToDashboard = new MapReportDataToDashboard();
        mapReportDataToDashboard.setId(id);
        return mapReportDataToDashboard;
    }
}
