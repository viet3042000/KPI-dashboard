package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMenuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMenu} and its DTO {@link ConfigMenuDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMenuMapper extends EntityMapper<ConfigMenuDTO, ConfigMenu> {



    default ConfigMenu fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMenu configMenu = new ConfigMenu();
        configMenu.setId(id);
        return configMenu;
    }
}
