package com.b4t.app.repository;

import com.b4t.app.domain.SysRole;
import com.b4t.app.domain.User;

import com.b4t.app.service.dto.UserDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesById(Long id);

    Optional<User> findOneById(Long id);

//    @EntityGraph(attributePaths = "authorities")
//    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
//    Optional<User> findOneWithAuthoritiesByLogin(String login);

//    @EntityGraph(attributePaths = "authorities")
//    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
//    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    List<User> findByLoginIn(List<String> logins);

    List<User> findByIdIn(List<Long> ids);

    @Query(value = " select a.id, a.login, a.email, a.activated, a.phone , a.created_date, b.authorities_name, c.domain_code from jhi_user a \n" +
        " left join (select sr.user_id , GROUP_CONCAT(lower(r.name ) separator ', ') authorities_name from sys_user_role sr, sys_role r where sr.role_id = r.id  group by sr.user_id ) b \n" +
        " on a.id = b.user_id\n" +
        " left join (select user_id , GROUP_CONCAT(lower(c.item_name) separator ', ') domain_code from jhi_user_domain d, cat_item c where d.domain_code = c.ITEM_VALUE group by user_id ) c \n" +
        " on a.id = c.user_id\n" +
        " where 1=1\n" +
        " and (:authorities is null or a.id in (select user_id from sys_user_role where role_id in (:authorities) )) " +
        " and (:domains is null or a.id in (select user_id from jhi_user_domain do where do.domain_code in (:domains))) " +
        " and (:keyword is null or lower(a.email) like %:keyword% ESCAPE '&' or lower(a.login) like %:keyword% ESCAPE '&' or lower(a.phone) like %:keyword% ESCAPE '&') " +
        "",
    countQuery = " select count(*) from jhi_user a \n" +
        " left join (select sr.user_id , GROUP_CONCAT(lower(r.name ) separator ', ') authorities_name from sys_user_role sr, sys_role r where sr.role_id = r.id  group by sr.user_id ) b \n" +
        " on a.id = b.user_id\n" +
        " left join (select user_id , GROUP_CONCAT(lower(domain_code) separator ', ') domain_code from jhi_user_domain  group by user_id ) c \n" +
        " on a.id = c.user_id\n" +
        " where 1=1\n" +
        " and (:authorities is null or a.id in (select user_id from sys_user_role where role_id in (:authorities) ))" +
        " and (:domains is null or a.id in (select user_id from jhi_user_domain do where do.domain_code in (:domains))) " +
        " and (:keyword is null or lower(a.email) like %:keyword% ESCAPE '&' or lower(a.login) like %:keyword% ESCAPE '&' or lower(a.phone) like %:keyword% ESCAPE '&') " +
        "",
    nativeQuery = true
    )
    Page<Object[]> queryUser(@Param("authorities") List<String> authorities, @Param("domains") List<String> domains, @Param("keyword") String keyword,  Pageable pageable);

    @Query(value = " select a.id, a.login, a.email, a.activated, a.phone , a.created_date, b.authorities_name, c.domain_code from jhi_user a \n" +
        " left join (select sr.user_id , GROUP_CONCAT(lower(r.name ) separator ', ') authorities_name from sys_user_role sr, sys_role r where sr.role_id = r.id  group by sr.user_id ) b \n" +
        " on a.id = b.user_id\n" +
        " left join (select user_id , GROUP_CONCAT(lower(c.item_name) separator ', ') domain_code from jhi_user_domain d, cat_item c where d.domain_code = c.ITEM_VALUE group by user_id ) c \n" +
        " on a.id = c.user_id\n" +
        " where 1=1\n" +
        " and a.id = :id " ,
        countQuery = " select count(*) from jhi_user a \n" +
            " left join (select sr.user_id , GROUP_CONCAT(lower(r.name ) separator ', ') authorities_name from sys_user_role sr, sys_role r where sr.role_id = r.id  group by sr.user_id ) b \n" +
            " on a.id = b.user_id\n" +
            " left join (select user_id , GROUP_CONCAT(lower(domain_code) separator ', ') domain_code from jhi_user_domain  group by user_id ) c \n" +
            " on a.id = c.user_id\n" +
            " where 1=1\n" +
            " and a.id = :id" ,
        nativeQuery = true
    )
    Page<Object[]> queryUserById(@Param("id") String keyword,  Pageable pageable);


    @Query(value = " select distinct t1.codeAciton codeAction, t1.id id,t1.code code,t1.name name,t1.tenant_id as tenantId, t1.description as description,  t1.status as status, t1.update_time as updateTime, t1.parent_id as parentId from (\n" +
        "select a.code as codeAciton, sm.id id,sm.code code,sm.name name,sm.tenant_id, sm.description, sm.status, sm.update_time, sm.parent_id  from sys_module as sm \n" +
        "inner join sys_role_module as srm\n" +
        "on sm.code = srm.module_code\n" +
        "inner join(\n" +
        "SELECT ur.role_id,su.tenant_id as tenant_id FROM sys_role as sr\n" +
        "inner join sys_user_role as ur\n" +
        "on sr.id = ur.role_id\n" +
        "inner join jhi_user as su \n" +
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
        "SELECT ur.role_id,su.tenant_id as tenant_id FROM sys_role as sr\n" +
        "inner join sys_user_role as ur\n" +
        "on sr.id = ur.role_id\n" +
        "inner join jhi_user as su \n" +
        "on ur.user_id = su.id where sr.status = 1 and  su.id = :idUser\n" +
        ") role on role.role_id = srm.role_id and role.tenant_id = sm.tenant_id \n" +
        "       \n" +
        "       ) THEN @idlist ::= CONCAT(IFNULL(@idlist,''),',',parent_id)\n" +
        "            WHEN FIND_IN_SET(id,@idlist) THEN @idlist ::= CONCAT(@idlist,',',parent_id)\n" +
        "            END as checkId" +
        " FROM sys_module\n" +
        "ORDER BY id DESC)T " +
        "WHERE checkId IS NOT NULL )) as t1 order by t1.id,(select @idlist ::=null)\n ", nativeQuery = true)
    List<Object[]> getRoleAction(@Param("idUser") Long idUser);

    @Query(value = " select distinct t1.id id,t1.code code,t1.name name,t1.tenant_id as tenantId, t1.description as description,  t1.status as status, t1.update_time as updateTime, t1.parent_id as parentId,t1.icon as icon,t1.path_url as pathUrl, t1.position as position from (\n" +
        "select sm.id id,sm.code code,sm.name name,sm.tenant_id, sm.description, sm.status, sm.update_time, sm.parent_id,sm.icon,sm.path_url,sm.position  from sys_module as sm \n" +
        "inner join sys_role_module as srm\n" +
        "on sm.code = srm.module_code\n" +
        "inner join(\n" +
        "SELECT ur.role_id,su.tenant_id as tenant_id FROM sys_role as sr\n" +
        "inner join sys_user_role as ur\n" +
        "on sr.id = ur.role_id \n" +
        "inner join jhi_user as su \n" +
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
        "SELECT ur.role_id,su.tenant_id as tenant_id FROM sys_role as sr\n" +
        "inner join sys_user_role as ur\n" +
        "on sr.id = ur.role_id\n" +
        "inner join jhi_user as su \n" +
        "on ur.user_id = su.id where sr.status = 1 and  su.id = :idUser\n" +
        ") role on role.role_id = srm.role_id and role.tenant_id = sm.tenant_id \n" +
        "       \n" +
        "       ) THEN @idlist ::= CONCAT(IFNULL(@idlist,''),',',parent_id)\n" +
        "            WHEN FIND_IN_SET(id,@idlist) THEN @idlist ::= CONCAT(@idlist,',',parent_id)\n" +
        "            END as checkId" +
        " FROM sys_module\n" +
        "ORDER BY id DESC)T " +
        "WHERE checkId IS NOT NULL )) as t1 order by t1.position,t1.name,(select @idlist ::=null)\n ", nativeQuery = true)
    List<Object[]> getRole(@Param("idUser") Long idUser);

    @Query(value = "SELECT sm.path_url\n" +
        "                FROM  \n" +
        "                  (SELECT tmp2.default_module,  \n" +
        "                          tmp2.code, tmp2.priority_level\n" +
        "                   FROM  \n" +
        "                     (SELECT tmp.default_module,tmp.priority_level,\n" +
        "                             sm.code  \n" +
        "                      FROM  \n" +
        "                        (SELECT DISTINCT default_module, priority_level \n" +
        "                         FROM sys_role sr  \n" +
        "                         INNER JOIN  \n" +
        "                           (SELECT role_id  \n" +
        "                            FROM sys_user_role  \n" +
        "                            WHERE user_id=:idUser) sur ON sr.id = sur.role_id  \n" +
        "                         INNER JOIN sys_role_module srm ON sur.role_id = srm.role_id) tmp  \n" +
        "                      INNER JOIN sys_module sm ON tmp.default_module = sm.id) tmp2  \n" +
        "                   INNER JOIN  \n" +
        "                     (SELECT DISTINCT srm.module_code\n" +
        "                      FROM sys_role_module srm  \n" +
        "                      WHERE role_id in \n" +
        "                          (SELECT sur.role_id\n" +
        "                           FROM sys_user_role sur  \n" +
        "                           LEFT JOIN sys_role sr ON sur.role_id = sr.id  \n" +
        "                           WHERE user_id=  :idUser\n" +
        "                           )) tmp3 ON tmp2.code = tmp3.module_code) tmp4  \n" +
        "                INNER JOIN sys_module sm ON tmp4.default_module = sm.id\n" +
        "                order by tmp4.priority_level  asc limit 1", nativeQuery = true)
    String getPathDefaultLogin(@Param("idUser") Long idUser);

//    @Query(value = "select sr.name from sys_user_role sur inner join sys_role sr on sur.role_id = sr.id\n" +
//        "where sur.user_id = :idUser", nativeQuery = true)
//    List<String> getRoleName(@Param("idUser") Long idUser);

    @Query(value = "select sr.id, sr.name, sr.code, sr.description," +
        " sr.status, sr.update_time, sr.tenant_id, sr.default_module, sr.priority_level" +
        " from sys_user_role sur inner join sys_role sr on sur.role_id = sr.id\n" +
        "where sur.user_id = :idUser and sr.status = 1", nativeQuery = true)
    List<Object[]> getRoleName(@Param("idUser") Long idUser);

    @Query(value = "select a.dept_permission_code from jhi_user a " +
        "where a.login = :userName", nativeQuery = true)
    String getUserByLogin(@Param("userName") String userName);

//    @Query(value = "select * "+
//        " from jhi_user sur \n" +
//        "where sur.login in (:lstLogin)", nativeQuery = true)
//    List<Object[]> getUserForDataLog(@Param("lstLogin") String lstLogin);

    @Query(value = "select sr " +
        " from User sr where sr.login = :login ")
//        " order by sr.name")
    User getUserForDataLog(@Param("login") String login);
}

