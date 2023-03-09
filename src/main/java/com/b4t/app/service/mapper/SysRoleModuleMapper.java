package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.SysRoleModuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysRoleModule} and its DTO {@link SysRoleModuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysRoleModuleMapper extends EntityMapper<SysRoleModuleDTO, SysRoleModule> {



    default SysRoleModule fromId(Long id) {
        if (id == null) {
            return null;
        }
        SysRoleModule sysRoleModule = new SysRoleModule();
        sysRoleModule.setId(id);
        return sysRoleModule;
    }
}
