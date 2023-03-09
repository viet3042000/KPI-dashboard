package com.b4t.app.service.mapper;

import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigQueryKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigQueryKpi} and its DTO {@link ConfigQueryKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigQueryKpiMapper extends EntityMapper<ConfigQueryKpiDTO, ConfigQueryKpi> {



    default ConfigQueryKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigQueryKpi configQueryKpi = new ConfigQueryKpi();
        configQueryKpi.setId(id);
        return configQueryKpi;
    }
}
