package com.b4t.app.repository;


import com.b4t.app.domain.SysAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SysAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysActionRepository extends JpaRepository<SysAction, Long> {
    @Query(value = "select sa " +
            " from SysAction sa " +
            " where 1=1 " +
            " and (:code is null or upper(sa.code) like %:code% escape '&' ) " +
            " and (:name is null or upper(sa.name) like %:name% escape '&' ) " +
            " and (:status is null or sa.status = :status)" +
            " order by sa.name, sa.code asc",
            countQuery = "select count (sa) from SysAction sa" +
                    " where 1=1 " +
                    " and (:code is null or lower(sa.code) like %:code% escape '&' ) " +
                    " and (:name is null or lower(sa.name) like %:name% escape '&' ) " +
                    " and (:status is null or sa.status = :status)")
    Page<SysAction> doSearch(@Param("code") String code, @Param("name") String name, @Param("status") Integer status, Pageable pageable);

    @Query("select s from SysAction s" +
            " where upper(s.code) = upper(:code) and (:id is null or s.id != :id)")
    SysAction getSysActionByCodeAndId(@Param("code") String code, @Param("id") Long id);

    List<SysAction> findAllById(Long id);

    List<SysAction> findAllByTenantId(Long tenantId);

    List<SysAction> findAllByCodeIn(List<String> codes);

    @Query(value = "select sa from SysAction sa where sa.status = 1 " +
            " and (:code is null or upper(sa.code) like %:code% escape '&' ) " +
            "order by sa.name, sa.code")
    List<SysAction> getAll(@Param("code") String code);

    @Query("select distinct a from SysAction a" +
            " left outer join SysModuleAction ma on ma.actionId = a.id" +
            " left outer join SysModule m on m.id = ma.moduleId" +
            " left outer join SysRoleModule rm on rm.moduleCode = m.code" +
            " left outer join SysUserRole ur on ur.roleId = rm.roleId" +
            " left outer join User u on u.id = ur.userId" +
            " where u.login = :userName" +
            "    and (rm.actionCode is null or rm.actionCode is not null and a.code = rm.actionCode)")
    List<SysAction> findAllByUserName(@Param("userName") String userName);

    @Query("select s from SysAction s" +
        " where upper(s.name) = upper(:name) and (:id is null or s.id != :id)")
    SysAction getSysActionByNameAndId(@Param("name") String name, @Param("id") Long id);
}
