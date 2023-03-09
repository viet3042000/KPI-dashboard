package com.b4t.app.service.mapper;

import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.service.dto.BaseRptGraphESDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BaseRptGraphESMapper extends EntityMapper<BaseRptGraphESDTO, BaseRptGraphES> {
}
