package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMapChartAreaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMapChartArea} and its DTO {@link ConfigMapChartAreaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapChartAreaMapper extends EntityMapper<ConfigMapChartAreaDTO, ConfigMapChartArea> {



    default ConfigMapChartArea fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapChartArea configMapChartArea = new ConfigMapChartArea();
        configMapChartArea.setId(id);
        return configMapChartArea;
    }
}
