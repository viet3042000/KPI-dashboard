package com.b4t.app.repository.impl;

import com.b4t.app.repository.SysRoleModuleRepositoryCustom;
import com.b4t.app.service.dto.SysRoleModuleDTO;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class SysRoleModuleRepositoryImpl implements SysRoleModuleRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<SysRoleModuleDTO> getTreeByRoleId(Long id) {
        Session session = this.entityManager.unwrap(Session.class);
        String sql = "select t1.value, t1.text, t1.id, t1.parentId, t1.checked from (SELECT DISTINCT \n" +
            "                t1.CODE AS VALUE, \n" +
            "                t1.NAME AS text, \n" +
            "                t1.id AS id, \n" +
            "                t1.parent_id AS parentId, \n" +
            "                t1.position,\n" +
            "                CASE \n" +
            "                WHEN t2.id is not null AND t2.action_code is null THEN TRUE \n" +
            "                when t2.id is not null and t2.action_code is not null then NULL \n" +
            "                ELSE false \n" +
            "                END checked \n" +
            "                FROM \n" +
            "                sys_module t1 \n" +
            "                LEFT JOIN sys_role_module t2 on t1.`code` = t2.module_code and t2.role_id = :id\n" +
            "                WHERE \n" +
            "                T1.`status` = 1 \n" +
            "                UNION ALL \n" +
            "                SELECT \n" +
            "                CONCAT( t1.module_code, '#_#_@@', t1.action_code ) AS \n" +
            "                VALUE \n" +
            "                , \n" +
            "                t1.NAME AS text, \n" +
            "                NULL AS id, \n" +
            "                t1.id AS parentId, \n" +
            "                t1.position,\n" +
            "                CASE \n" +
            "                 \n" +
            "                WHEN t2.id IS NULL THEN \n" +
            "                0 ELSE 1 \n" +
            "                END AS checked \n" +
            "                FROM \n" +
            "                (SELECT \n" +
            "                module_code,code as action_code,name,module_id as id ,position\n" +
            "                FROM \n" +
            "                ( \n" +
            "                SELECT \n" +
            "                module_child.id, \n" +
            "                module_child.CODE AS module_code,\n" +
            "                module_child.position\n" +
            "                FROM \n" +
            "                sys_module AS module_child \n" +
            "                LEFT JOIN sys_module AS module_parent ON module_parent.parent_id = module_child.id \n" +
            "                WHERE \n" +
            "                module_parent.id IS NULL \n" +
            "                AND module_child.STATUS = 1 \n" +
            "                ) AS s1 \n" +
            "                join sys_module_action as sma on sma.module_id = s1.id \n" +
            "                join sys_action on sma.action_id = sys_action.id \n" +
            "                ) AS t1 \n" +
            "                LEFT JOIN ( SELECT * FROM sys_role_module WHERE role_id = :id ) AS t2 ON t1.module_code = t2.module_code \n" +
            "                AND t1.action_code = t2.action_code) t1 \n" +
            "                ORDER BY t1.position,t1.value,t1.text\n" +
            "                ";
        Query<SysRoleModuleDTO> query = session.createSQLQuery(sql)
                .addScalar("value", new StringType())
                .addScalar("text", new StringType())
                .addScalar("id", new LongType())
                .addScalar("parentId", new LongType())
                .addScalar("checked", new BooleanType())
                .setParameter("id", id)
                .setResultTransformer(Transformers.aliasToBean(SysRoleModuleDTO.class));
        return query.getResultList();
    }
}
