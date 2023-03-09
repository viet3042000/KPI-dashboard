package com.b4t.app.service.mapper;

import com.b4t.app.domain.ObjectReport;
import com.b4t.app.service.dto.ObjectReportDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ObjectReportMapper extends EntityMapper<ObjectReportDTO, ObjectReport>{
}
