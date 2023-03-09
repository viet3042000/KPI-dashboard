package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ChartDescriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChartDescription} and its DTO {@link ChartDescriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChartDescriptionMapper extends EntityMapper<ChartDescriptionDTO, ChartDescription> {



    default ChartDescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChartDescription chartDescription = new ChartDescription();
        chartDescription.setId(id);
        return chartDescription;
    }
}
