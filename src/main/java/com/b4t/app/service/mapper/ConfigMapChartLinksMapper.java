package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMapChartLinksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMapChartLinks} and its DTO {@link ConfigMapChartLinksDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapChartLinksMapper extends EntityMapper<ConfigMapChartLinksDTO, ConfigMapChartLinks> {



    default ConfigMapChartLinks fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapChartLinks configMapChartLinks = new ConfigMapChartLinks();
        configMapChartLinks.setId(id);
        return configMapChartLinks;
    }
}
