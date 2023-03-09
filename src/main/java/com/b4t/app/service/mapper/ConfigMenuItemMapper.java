package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMenuItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMenuItem} and its DTO {@link ConfigMenuItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMenuItemMapper extends EntityMapper<ConfigMenuItemDTO, ConfigMenuItem> {



    default ConfigMenuItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMenuItem configMenuItem = new ConfigMenuItem();
        configMenuItem.setId(id);
        return configMenuItem;
    }
}
