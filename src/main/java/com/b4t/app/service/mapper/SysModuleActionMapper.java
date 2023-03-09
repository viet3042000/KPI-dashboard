package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.SysModuleActionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysModuleAction} and its DTO {@link SysModuleActionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysModuleActionMapper extends EntityMapper<SysModuleActionDTO, SysModuleAction> {



    default SysModuleAction fromId(Long id) {
        if (id == null) {
            return null;
        }
        SysModuleAction sysModuleAction = new SysModuleAction();
        sysModuleAction.setId(id);
        return sysModuleAction;
    }
}
