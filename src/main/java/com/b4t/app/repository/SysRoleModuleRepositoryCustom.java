package com.b4t.app.repository;

import com.b4t.app.service.dto.SysRoleModuleDTO;
import java.util.List;

public interface SysRoleModuleRepositoryCustom {
    List<SysRoleModuleDTO> getTreeByRoleId(Long id);

}
