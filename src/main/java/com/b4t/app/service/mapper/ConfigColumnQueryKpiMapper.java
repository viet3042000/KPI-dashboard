package com.b4t.app.service.mapper;

import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigColumnQueryKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigColumnQueryKpi} and its DTO {@link ConfigColumnQueryKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigColumnQueryKpiMapper extends EntityMapper<ConfigColumnQueryKpiDTO, ConfigColumnQueryKpi> {



    default ConfigColumnQueryKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigColumnQueryKpi configColumnQueryKpi = new ConfigColumnQueryKpi();
        configColumnQueryKpi.setId(id);
        return configColumnQueryKpi;
    }
}
