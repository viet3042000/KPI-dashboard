package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.CatGraphKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatGraphKpi} and its DTO {@link CatGraphKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatGraphKpiMapper extends EntityMapper<CatGraphKpiDTO, CatGraphKpi> {
    default CatGraphKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        CatGraphKpi catGraphKpi = new CatGraphKpi();
        catGraphKpi.setId(id);
        return catGraphKpi;
    }
}
