package com.b4t.app.repository;

import com.b4t.app.domain.SysModule;
import com.b4t.app.domain.SysRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SysRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    @Query(value = "select sr " +
        " from SysRole sr where sr.status = 1 and (:tenantId is null or sr.tenantId = :tenantId)" +
        " order by sr.name")
    List<SysRole> getAllRole(@Param("tenantId") Long tenantId);

    @Query(value = " select sr.id, sr.name, sr.code, sr.description, sr.status,sr.update_time, sr.tenant_id, sr.default_module, sr.priority_level, sm.name as defaultModuleName " +
        " from sys_role sr " +
        " left join sys_module sm on sr.default_module = sm.id" +
        " where 1=1 " +
        " and (:code is null or lower(sr.code) like %:code% escape '&' ) " +
        " and (:name is null or lower(sr.name) like %:name% escape '&' ) " +
        " and (:status is null or sr.status = :status ) " +
        " order by sr.name, sr.code asc ",
        countQuery = "select count(sr.id) from sys_role sr" +
            " left join sys_module sm on sr.default_module = sm.id" +
            " where 1=1 " +
            " and (:code is null or lower(sr.code) like %:code% escape '&' ) " +
            " and (:name is null or lower(sr.name) like %:name% escape '&' ) " +
            " and (:status is null or sr.status = :status ) " +
            " order by sr.name, sr.code asc ", nativeQuery=true)
    Page<Object[]> doSearch(@Param("code") String code, @Param("name") String name, @Param("status") Integer status, Pageable pageable);

    SysRole findSysRoleByCode(String code);

    List<SysRole> findSysRoleById(Long id);

    @Query(value = "select s from SysRole s " +
        " where upper(s.code) = upper(:code) and (:id is null or s.id != :id)")
    SysRole getSysRoleByCodeAndId(@Param("code") String code, @Param("id") Long id);

    @Query(value = "select a from SysRole a where exists (select 1 from SysUserRole r, User u where r.userId = u.id and u.login = :username and a.id = r.roleId)")
    List<SysRole> findAllByUser(@Param("username") String username);

    List<SysRole> findAllByDefaultModule(Long defaultModule);
}
