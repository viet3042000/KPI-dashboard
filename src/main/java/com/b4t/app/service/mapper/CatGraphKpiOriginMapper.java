package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.CatGraphKpiOriginDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatGraphKpiOrigin} and its DTO {@link CatGraphKpiOriginDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatGraphKpiOriginMapper extends EntityMapper<CatGraphKpiOriginDTO, CatGraphKpiOrigin> {



    default CatGraphKpiOrigin fromId(Long id) {
        if (id == null) {
            return null;
        }
        CatGraphKpiOrigin catGraphKpiOrigin = new CatGraphKpiOrigin();
        catGraphKpiOrigin.setId(id);
        return catGraphKpiOrigin;
    }
}
