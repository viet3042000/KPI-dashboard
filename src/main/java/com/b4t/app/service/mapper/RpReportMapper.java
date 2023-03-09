package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.RpReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RpReport} and its DTO {@link RpReportDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RpReportMapper extends EntityMapper<RpReportDTO, RpReport> {



    default RpReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        RpReport rpReport = new RpReport();
        rpReport.setId(id);
        return rpReport;
    }
}
