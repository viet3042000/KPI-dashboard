package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.FlagRunQueryKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FlagRunQueryKpi} and its DTO {@link FlagRunQueryKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FlagRunQueryKpiMapper extends EntityMapper<FlagRunQueryKpiDTO, FlagRunQueryKpi> {



    default FlagRunQueryKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        FlagRunQueryKpi flagRunQueryKpi = new FlagRunQueryKpi();
        flagRunQueryKpi.setId(id);
        return flagRunQueryKpi;
    }
}
