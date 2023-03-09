package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ClassifyColorGmapLevelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassifyColorGmapLevel} and its DTO {@link ClassifyColorGmapLevelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClassifyColorGmapLevelMapper extends EntityMapper<ClassifyColorGmapLevelDTO, ClassifyColorGmapLevel> {



    default ClassifyColorGmapLevel fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClassifyColorGmapLevel classifyColorGmapLevel = new ClassifyColorGmapLevel();
        classifyColorGmapLevel.setId(id);
        return classifyColorGmapLevel;
    }
}
