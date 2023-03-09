package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.AreaGmapDataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AreaGmapData} and its DTO {@link AreaGmapDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AreaGmapDataMapper extends EntityMapper<AreaGmapDataDTO, AreaGmapData> {



    default AreaGmapData fromId(Long id) {
        if (id == null) {
            return null;
        }
        AreaGmapData areaGmapData = new AreaGmapData();
        areaGmapData.setId(id);
        return areaGmapData;
    }
}
