package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ClassifyColorGmapDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassifyColorGmap} and its DTO {@link ClassifyColorGmapDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClassifyColorGmapMapper extends EntityMapper<ClassifyColorGmapDTO, ClassifyColorGmap> {



    default ClassifyColorGmap fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClassifyColorGmap classifyColorGmap = new ClassifyColorGmap();
        classifyColorGmap.setId(id);
        return classifyColorGmap;
    }
}
