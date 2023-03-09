package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.KpiWarnedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link KpiWarned} and its DTO {@link KpiWarnedDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KpiWarnedMapper extends EntityMapper<KpiWarnedDTO, KpiWarned> {



    default KpiWarned fromId(Long id) {
        if (id == null) {
            return null;
        }
        KpiWarned kpiWarned = new KpiWarned();
        kpiWarned.setId(id);
        return kpiWarned;
    }
}
