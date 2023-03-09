package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ClassifyColorGmapKpiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassifyColorGmapKpi} and its DTO {@link ClassifyColorGmapKpiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClassifyColorGmapKpiMapper extends EntityMapper<ClassifyColorGmapKpiDTO, ClassifyColorGmapKpi> {



    default ClassifyColorGmapKpi fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClassifyColorGmapKpi classifyColorGmapKpi = new ClassifyColorGmapKpi();
        classifyColorGmapKpi.setId(id);
        return classifyColorGmapKpi;
    }
}
