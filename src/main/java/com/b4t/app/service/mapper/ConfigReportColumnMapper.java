package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigReportColumnDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigReportColumn} and its DTO {@link ConfigReportColumnDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigReportColumnMapper extends EntityMapper<ConfigReportColumnDTO, ConfigReportColumn> {



    default ConfigReportColumn fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigReportColumn configReportColumn = new ConfigReportColumn();
        configReportColumn.setId(id);
        return configReportColumn;
    }
}
