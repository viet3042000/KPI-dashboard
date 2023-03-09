package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigDisplayQueryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigDisplayQuery} and its DTO {@link ConfigDisplayQueryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigDisplayQueryMapper extends EntityMapper<ConfigDisplayQueryDTO, ConfigDisplayQuery> {
    default ConfigDisplayQuery fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigDisplayQuery configDisplayQuery = new ConfigDisplayQuery();
        configDisplayQuery.setId(id);
        return configDisplayQuery;
    }
}
