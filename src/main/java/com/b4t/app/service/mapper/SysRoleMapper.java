package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.SysRoleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysRole} and its DTO {@link SysRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysRoleMapper extends EntityMapper<SysRoleDTO, SysRole> {



    default SysRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        return sysRole;
    }
}
