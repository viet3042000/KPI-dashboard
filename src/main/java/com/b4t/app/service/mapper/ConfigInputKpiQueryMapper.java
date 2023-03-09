package com.b4t.app.service.mapper;

import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigInputKpiQueryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigInputKpiQuery} and its DTO {@link ConfigInputKpiQueryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigInputKpiQueryMapper extends EntityMapper<ConfigInputKpiQueryDTO, ConfigInputKpiQuery> {



    default ConfigInputKpiQuery fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigInputKpiQuery configInputKpiQuery = new ConfigInputKpiQuery();
        configInputKpiQuery.setId(id);
        return configInputKpiQuery;
    }
}
