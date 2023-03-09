package com.b4t.app.repository;

import com.b4t.app.domain.SysRoleModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysRoleModuleRepository extends JpaRepository<SysRoleModule, Long>, SysRoleModuleRepositoryCustom {
    List<SysRoleModule> getAllByRoleId(Long id);

    void deleteById(Long id);

    List<SysRoleModule> findByModuleCode(String moduleCode);

    @Query(value = "select a from SysRoleModule a where a.roleId in (select b.roleId from SysUserRole b where b.userId = ?1) ")
    List<SysRoleModule> findModuleByUser(Long userId);
}
