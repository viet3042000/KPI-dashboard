package com.b4t.app.service.mapper;

import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMapKpiQueryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMapKpiQuery} and its DTO {@link ConfigMapKpiQueryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapKpiQueryMapper extends EntityMapper<ConfigMapKpiQueryDTO, ConfigMapKpiQuery> {



    default ConfigMapKpiQuery fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapKpiQuery configMapKpiQuery = new ConfigMapKpiQuery();
        configMapKpiQuery.setId(id);
        return configMapKpiQuery;
    }
}
