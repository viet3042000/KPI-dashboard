package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigMapGroupChartAreaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigMapGroupChartArea} and its DTO {@link ConfigMapGroupChartAreaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapGroupChartAreaMapper extends EntityMapper<ConfigMapGroupChartAreaDTO, ConfigMapGroupChartArea> {



    default ConfigMapGroupChartArea fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapGroupChartArea configMapGroupChartArea = new ConfigMapGroupChartArea();
        configMapGroupChartArea.setId(id);
        return configMapGroupChartArea;
    }
}
