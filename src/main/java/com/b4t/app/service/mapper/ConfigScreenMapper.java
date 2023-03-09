package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigScreenDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigScreen} and its DTO {@link ConfigScreenDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigScreenMapper extends EntityMapper<ConfigScreenDTO, ConfigScreen> {



    default ConfigScreen fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigScreen configScreen = new ConfigScreen();
        configScreen.setId(id);
        return configScreen;
    }
}
