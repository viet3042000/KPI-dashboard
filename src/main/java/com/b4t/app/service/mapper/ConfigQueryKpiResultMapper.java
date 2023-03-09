package com.b4t.app.service.mapper;

import com.b4t.app.domain.ConfigQueryKpiResult;
import com.b4t.app.service.dto.ConfigQueryKpiResultDTO;
import org.mapstruct.Mapper;

/**
 * @author tamdx
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigQueryKpiResultMapper extends EntityMapper<ConfigQueryKpiResultDTO, ConfigQueryKpiResult> {
}
