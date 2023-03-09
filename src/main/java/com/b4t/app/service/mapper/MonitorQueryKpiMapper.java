package com.b4t.app.service.mapper;

import com.b4t.app.domain.*;
import com.b4t.app.service.dto.MonitorQueryKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonitorQueryKpi} and its DTO {@link MonitorQueryKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MonitorQueryKpiMapper extends EntityMapper<MonitorQueryKpiDTO, MonitorQueryKpi> {



    default MonitorQueryKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        MonitorQueryKpi monitorQueryKpi = new MonitorQueryKpi();
        monitorQueryKpi.setId(id);
        return monitorQueryKpi;
    }
}
