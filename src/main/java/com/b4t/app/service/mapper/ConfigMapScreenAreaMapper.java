package com.b4t.app.service.mapper;

import com.b4t.app.domain.ConfigMapChartArea;
import com.b4t.app.domain.ConfigMapScreenArea;
import com.b4t.app.service.dto.ConfigMapChartAreaDTO;
import com.b4t.app.service.dto.ConfigMapScreenAreaDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ConfigMapChartArea} and its DTO {@link ConfigMapChartAreaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapScreenAreaMapper extends EntityMapper<ConfigMapScreenAreaDTO, ConfigMapScreenArea>{
    default ConfigMapScreenArea fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapScreenArea configMapScreenArea = new ConfigMapScreenArea();
        configMapScreenArea.setId(id);
        return configMapScreenArea;
    }
}
