package com.b4t.app.service.mapper;


import com.b4t.app.domain.ConfigMapKpiIdMaLv;
import com.b4t.app.service.dto.ConfigMapKpiIdMaLvDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ConfigMapKpiIdMaLv} and its DTO {@link ConfigMapKpiIdMaLvDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigMapKpiIdMaLvMapper extends EntityMapper<ConfigMapKpiIdMaLvDTO, ConfigMapKpiIdMaLv> {
    default ConfigMapKpiIdMaLv fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigMapKpiIdMaLv configMapKpiIdMaLv = new ConfigMapKpiIdMaLv();
        configMapKpiIdMaLv.setId(id);
        return configMapKpiIdMaLv;
    }
}
