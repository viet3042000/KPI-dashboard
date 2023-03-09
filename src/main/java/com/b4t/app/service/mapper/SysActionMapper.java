package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.SysActionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysAction} and its DTO {@link SysActionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysActionMapper extends EntityMapper<SysActionDTO, SysAction> {



    default SysAction fromId(Long id) {
        if (id == null) {
            return null;
        }
        SysAction sysAction = new SysAction();
        sysAction.setId(id);
        return sysAction;
    }
}
