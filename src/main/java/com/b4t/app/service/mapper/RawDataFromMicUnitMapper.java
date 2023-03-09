package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.RawDataFromMicUnitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RawDataFromMicUnit} and its DTO {@link RawDataFromMicUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RawDataFromMicUnitMapper extends EntityMapper<RawDataFromMicUnitDTO, RawDataFromMicUnit> {



    default RawDataFromMicUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        RawDataFromMicUnit rawDataFromMicUnit = new RawDataFromMicUnit();
        rawDataFromMicUnit.setId(id);
        return rawDataFromMicUnit;
    }
}
