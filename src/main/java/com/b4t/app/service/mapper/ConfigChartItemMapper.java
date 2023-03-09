package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigChartItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigChartItem} and its DTO {@link ConfigChartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigChartItemMapper extends EntityMapper<ConfigChartItemDTO, ConfigChartItem> {



    default ConfigChartItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigChartItem configChartItem = new ConfigChartItem();
        configChartItem.setId(id);
        return configChartItem;
    }
}
