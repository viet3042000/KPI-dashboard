package com.b4t.app.repository;

import com.b4t.app.domain.SysUserRole;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SysUserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    List<SysUserRole> findByRoleId(Long id);

    List<SysUserRole> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
