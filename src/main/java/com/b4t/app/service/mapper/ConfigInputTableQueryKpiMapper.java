package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigInputTableQueryKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigInputTableQueryKpi} and its DTO {@link ConfigInputTableQueryKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigInputTableQueryKpiMapper extends EntityMapper<ConfigInputTableQueryKpiDTO, ConfigInputTableQueryKpi> {



    default ConfigInputTableQueryKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigInputTableQueryKpi configInputTableQueryKpi = new ConfigInputTableQueryKpi();
        configInputTableQueryKpi.setId(id);
        return configInputTableQueryKpi;
    }
}
