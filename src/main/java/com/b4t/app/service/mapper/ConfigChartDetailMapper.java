package com.b4t.app.service.mapper;

import com.b4t.app.domain.ConfigChartDetail;
import com.b4t.app.service.dto.ConfigChartDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ConfigChartDetailMapper extends EntityMapper<ConfigChartDetailDTO, ConfigChartDetail> {
}
