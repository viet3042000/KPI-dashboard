package com.b4t.app.service.mapper;

import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.service.dto.BaseRptGraphDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BaseRptGraphMapper extends EntityMapper<BaseRptGraphDTO, BaseRptGraph> {
}
