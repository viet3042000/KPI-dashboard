package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigAreaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigArea} and its DTO {@link ConfigAreaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigAreaMapper extends EntityMapper<ConfigAreaDTO, ConfigArea> {



    default ConfigArea fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigArea configArea = new ConfigArea();
        configArea.setId(id);
        return configArea;
    }
}
