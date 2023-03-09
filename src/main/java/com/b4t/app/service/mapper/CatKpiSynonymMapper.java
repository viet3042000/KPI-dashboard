package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.CatKpiSynonymDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatKpiSynonym} and its DTO {@link CatKpiSynonymDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatKpiSynonymMapper extends EntityMapper<CatKpiSynonymDTO, CatKpiSynonym> {



    default CatKpiSynonym fromId(Long id) {
        if (id == null) {
            return null;
        }
        CatKpiSynonym catKpiSynonym = new CatKpiSynonym();
        catKpiSynonym.setId(id);
        return catKpiSynonym;
    }
}
