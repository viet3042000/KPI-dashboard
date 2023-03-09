package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMapChartMenuItem} and its DTO {@link ConfigMapChartMenuItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapChartMenuItemMapper extends EntityMapper<ConfigMapChartMenuItemDTO, ConfigMapChartMenuItem> {



    default ConfigMapChartMenuItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapChartMenuItem configMapChartMenuItem = new ConfigMapChartMenuItem();
        configMapChartMenuItem.setId(id);
        return configMapChartMenuItem;
    }
}
