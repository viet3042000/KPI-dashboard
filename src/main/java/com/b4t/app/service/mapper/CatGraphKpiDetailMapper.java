package com.b4t.app.service.mapper;

import com.b4t.app.domain.CatGraphKpiDetail;
import com.b4t.app.service.dto.CatGraphKpiDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CatGraphKpiDetailMapper extends EntityMapper<CatGraphKpiDetailDTO, CatGraphKpiDetail> {
}
