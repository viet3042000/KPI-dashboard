package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigReport} and its DTO {@link ConfigReportDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigReportMapper extends EntityMapper<ConfigReportDTO, ConfigReport> {



    default ConfigReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigReport configReport = new ConfigReport();
        configReport.setId(id);
        return configReport;
    }
}
