package com.b4t.app.repository;

import com.b4t.app.domain.SysModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SysModule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysModuleRepository extends JpaRepository<SysModule, Long> {

    @Query(value = " select a.parent_id as parentId," +
            " b.name as parentName " +
            " from " +
            " (select DISTINCT parent_id, tenant_id from sys_module) a " +
            " left join " +
            " (select id,name from sys_module) b " +
            " on b.id = a.parent_id " +
            " where a.parent_id != 0 and a.tenant_id = :tenantId " +
            " order by b.name ", nativeQuery = true)
    List<Object[]> getParent(@Param("tenantId") Long tenantId);

    List<SysModule> findAllById(Long id);

    @Query(value = "call searchSysModule(:code ,:name ,:status ,:parentId)", nativeQuery = true)
    List<SysModule> doSearch(@Param("code") String code,@Param("name") String name,@Param("status") Integer status,@Param("parentId") Long parentId);

    @Query(value = "select m " +
            " from SysModule m" +
            " where m.tenantId = :tenantId" +
        " and (:status is null or m.status=:status)")
    List<SysModule> getTreeParent(@Param("tenantId") Long tenantId, @Param("status") Integer status);

    @Query(value = " select distinct t1.codeAciton codeAction, t1.id id,t1.code code,t1.name name,t1.tenant_id as tenantId, t1.description as description,  t1.status as status, t1.update_time as updateTime, t1.parent_id as parentId from (\n" +
            "select a.code as codeAciton, sm.id id,sm.code code,sm.name name,sm.tenant_id, sm.description, sm.status, sm.update_time, sm.parent_id  from sys_module as sm \n" +
            "inner join sys_role_module as srm\n" +
            "on sm.code = srm.module_code\n" +
            "inner join(\n" +
            "SELECT ur.role_id,su.tenant_id as tenant_id FROM mic_report.sys_role as sr\n" +
            "inner join sys_user_role as ur\n" +
            "on sr.id = ur.role_id\n" +
            "inner join sys_user as su \n" +
            "on ur.user_id = su.id where  sr.status = 1 and su.id = :idUser\n" +
            ") role on role.role_id = srm.role_id and role.tenant_id = sm.tenant_id\n" +
            " left join sys_action as a \n" +
            " on a.code = srm.action_code and a.tenant_id = role.tenant_id where sm.status = 1 and (sm.parent_id in (select id from sys_module where status = 1 )  or sm.parent_id =0)\n" +
            " Union\n" +
            "\n" +
            " select a.code as codeAciton, sm.id id,sm.code code,sm.name name,sm.tenant_id, sm.description, sm.status, sm.update_time, sm.parent_id   from sys_module as sm\n" +
            " left join sys_role_module as srm\n" +
            "on sm.code = srm.module_code\n" +
            " left join sys_action as a \n" +
            " on a.code = srm.action_code\n" +
            " where sm.id in (SELECT parent_id FROM\n" +
            "(SELECT id,parent_id,\n" +
            "       CASE WHEN id in (\n" +
            "       \n" +
            "        select sm.id  from sys_module as sm \n" +
            "inner join sys_role_module as srm\n" +
            "on sm.code = srm.module_code\n" +
            "inner join(\n" +
            "SELECT ur.role_id,su.tenant_id as tenant_id FROM mic_report.sys_role as sr\n" +
            "inner join sys_user_role as ur\n" +
            "on sr.id = ur.role_id\n" +
            "inner join sys_user as su \n" +
            "on ur.user_id = su.id where sr.status = 1 and  su.id = :idUser\n" +
            ") role on role.role_id = srm.role_id and role.tenant_id = sm.tenant_id \n" +
            "       \n" +
            "       ) THEN @idlist ::= CONCAT(IFNULL(@idlist,''),',',parent_id)\n" +
            "            WHEN FIND_IN_SET(id,@idlist) THEN @idlist ::= CONCAT(@idlist,',',parent_id)\n" +
            "            END as checkId" +
            " FROM sys_module\n" +
            "ORDER BY id DESC)T " +
            "WHERE checkId IS NOT NULL )) as t1 order by t1.id\n ", nativeQuery = true)
    List<Object[]> getRoleAction(@Param("idUser") Long idUser);

    @Query(value = " select distinct t1.id id,t1.code code,t1.name name,t1.tenant_id as tenantId, t1.description as description,  t1.status as status, t1.update_time as updateTime, t1.parent_id as parentId,t1.icon as icon,t1.path_url as pathUrl, t1.position as position from (\n" +
            "select sm.id id,sm.code code,sm.name name,sm.tenant_id, sm.description, sm.status, sm.update_time, sm.parent_id,sm.icon,sm.path_url,sm.position  from sys_module as sm \n" +
            "inner join sys_role_module as srm\n" +
            "on sm.code = srm.module_code\n" +
            "inner join(\n" +
            "SELECT ur.role_id,su.tenant_id as tenant_id FROM mic_report.sys_role as sr\n" +
            "inner join sys_user_role as ur\n" +
            "on sr.id = ur.role_id \n" +
            "inner join sys_user as su \n" +
            "on ur.user_id = su.id where sr.status = 1 and  su.id = :idUser \n" +
            ") role on role.role_id = srm.role_id and role.tenant_id = sm.tenant_id where sm.status = 1 and (sm.parent_id in (select id from sys_module where status = 1 ) or sm.parent_id =0)\n" +
            " Union\n" +
            "\n" +
            " select sm.id id,sm.code code,sm.name name,sm.tenant_id, sm.description, sm.status, sm.update_time, sm.parent_id,sm.icon,sm.path_url,sm.position   from sys_module as sm\n" +
            " where sm.status = 1 and sm.id in ( SELECT parent_id FROM\n" +
            "(SELECT id,parent_id,\n" +
            "       CASE WHEN id in (\n" +
            "       \n" +
            "        select sm.id  from sys_module as sm \n" +
            "inner join sys_role_module as srm\n" +
            "on sm.code = srm.module_code\n" +
            "inner join(\n" +
            "SELECT ur.role_id,su.tenant_id as tenant_id FROM mic_report.sys_role as sr\n" +
            "inner join sys_user_role as ur\n" +
            "on sr.id = ur.role_id\n" +
            "inner join sys_user as su \n" +
            "on ur.user_id = su.id where sr.status = 1 and  su.id = :idUser\n" +
            ") role on role.role_id = srm.role_id and role.tenant_id = sm.tenant_id \n" +
            "       \n" +
            "       ) THEN @idlist ::= CONCAT(IFNULL(@idlist,''),',',parent_id)\n" +
            "            WHEN FIND_IN_SET(id,@idlist) THEN @idlist ::= CONCAT(@idlist,',',parent_id)\n" +
            "            END as checkId" +
            " FROM sys_module\n" +
            "ORDER BY id DESC)T " +
            "WHERE checkId IS NOT NULL )) as t1 order by t1.position,t1.name\n ", nativeQuery = true)
    List<Object[]> getRole(@Param("idUser") Long idUser);


    @Query("select s from SysModule s" +
            " where upper(s.code) = upper(:code) and (:id is null or s.id != :id)")
    SysModule getSysModuleByCodeAndId(@Param("code") String code, @Param("id") Long id);

    @Query(value = "SELECT " +
            "   T2.id, " +
            "   T2.CODE, " +
            "   T2.NAME, " +
            "   T2.tenant_id, " +
            "   T2.description, " +
            "   T2.STATUS, " +
            "   T2.update_time, " +
            "   T2.parent_id, " +
            "   T2.path_url, " +
            "   T2.icon  " +
            "  FROM " +
            "   (SELECT " +
            "     sys_module.id, " +
            "     sys_module.CODE, " +
            "     sys_module.NAME, " +
            "     sys_module.tenant_id, " +
            "     sys_module.description, " +
            "     sys_module.STATUS, " +
            "     sys_module.update_time, " +
            "     sys_module.parent_id, " +
            "     sys_module.path_url, " +
            "     sys_module.icon, " +
            "     CASE " +
            "       WHEN id IN (:parentId) THEN @findParent ::= CONCAT(IFNULL(@findParent,''),',',parent_id) " +
            "       WHEN FIND_IN_SET(id, @findParent) THEN @findParent ::= CONCAT(@findParent,',',parent_id) " +
            "     END AS checked  " +
            "    FROM " +
            "      sys_module  " +
            "   ) AS T2, (SELECT @findParent ::= NULL) T3  " +
            "WHERE checked IS NOT NULL AND id = :id", nativeQuery = true)
    List<SysModule> findParentInvalid(@Param("id") Long id, @Param("parentId") Long parentId);

    List<SysModule> findAllByParentId(Long parentId);

    @Query(" SELECT s FROM SysModule s " +
        " WHERE 1 = 1 " +
        " AND ( :code IS NULL or lower(s.code) like :code ESCAPE '&' )" +
        " AND ( :name IS NULL or lower(s.name) like :name ESCAPE '&' )" +
        " AND ( :status IS NULL or s.status = :status) " +
        " AND ( :parentId IS NULL or s.parentId = :parentId) ")
    List<SysModule> findByParamSearch(@Param("code") String code,@Param("name") String name,@Param("status") Integer status,@Param("parentId") Long parentId);

    List<SysModule> findAllByParentIdIsIn(List<Long> parentId);

    List<SysModule> findAllByIdIsIn(List<Long> id);
}
