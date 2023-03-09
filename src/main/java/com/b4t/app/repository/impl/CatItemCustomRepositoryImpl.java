package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.domain.CatItem;
import com.b4t.app.repository.CatItemCustomRepository;
import com.b4t.app.service.dto.CatItemDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CatItemCustomRepositoryImpl implements CatItemCustomRepository {
    private final EntityManager entityManager;
    private static final String SQL_TIMETYPE_MAPS = " select distinct tt.item_value, tt.item_name\n" +
        " from cat_item a\n" +
        "         inner join cat_item b on b.item_code = a.item_value and b.category_code ='DOMAIN_TABLE' and b.status = 1\n" +
        "         inner join\n" +
        "     (\n" +
        "         select distinct kpi_id, kpi_name, INPUT_LEVEL, DOMAIN_CODE\n" +
        "         from (\n" +
        "                  select distinct a.kpi_id, b.INPUT_LEVEL,  d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1\n" +
        "                           inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID\n" +
        "                  where a.STATUS =1\n" +
        "                  union\n" +
        "                  select distinct a.kpi_id, b.list_parent_input_level input_level, d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1\n" +
        "                           inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID and d.FORMULA_LEVEL is not null\n" +
        "                  where a.STATUS =1 and b.list_parent_input_level is not null\n" +
        "              ) a where a.input_level = 2\n" +
        "     ) kpi_level on a.item_value = kpi_level.domain_code\n" +
        "         inner join\n" +
        "     (\n" +
        "         select distinct kpi_id, time_type, domain_code\n" +
        "         from (\n" +
        "                  select distinct a.kpi_id, b.TIME_TYPE, c.RUN_TIME_SUCC , d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1\n" +
        "                           inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID\n" +
        "                  where a.STATUS =1\n" +
        "                  union\n" +
        "                  select distinct a.kpi_id, 3 TIME_TYPE,\n" +
        "                                  makedate(year(c.RUN_TIME_SUCC),1) + interval quarter(c.RUN_TIME_SUCC) quarter - interval 1 quarter as RUN_TIME_SUCC,\n" +
        "                                  d.DOMAIN_CODE,d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1\n" +
        "                           inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID and d.FORMULA_QUAR is not null\n" +
        "                  where a.STATUS =1\n" +
        "                  union\n" +
        "                  select distinct a.kpi_id, 4 TIME_TYPE,\n" +
        "                                  makedate(year(c.RUN_TIME_SUCC),1) as RUN_TIME_SUCC, d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1\n" +
        "                           inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID and d.FORMULA_YEAR is not null\n" +
        "                  where a.STATUS =1\n" +
        "              ) a\n" +
        "     ) kpi_time on kpi_level.kpi_id = kpi_time.kpi_id and kpi_level.domain_code =kpi_time.domain_code\n" +
        "         inner join cat_item as tt on kpi_time.time_type = tt.ITEM_VALUE and tt.CATEGORY_CODE ='TIME_TYPE' and tt.STATUS =1\n" +
        " where a.CATEGORY_CODE ='DOMAIN'\n" +
        "  and a.item_value = :DOMAIN_CODE \n" +
        "  and a.status = 1\n" +
        " order by a.position, a.ITEM_NAME, tt.position, tt.item_name, kpi_level.kpi_name  ";
    private static final String SQL_DOMAIN_AND_TABLE = " select distinct a.item_value as domain_code,\n" +
        "                a.item_name  as domain_name,\n" +
        "                b.item_value as table_name\n" +
        " from cat_item a\n" +
        "         inner join cat_item b\n" +
        "                    on b.item_code = a.item_value and b.category_code = 'DOMAIN_TABLE' and b.status = 1\n" +
        "         inner join\n" +
        "     (\n" +
        "         select distinct kpi_id, kpi_name, INPUT_LEVEL, DOMAIN_CODE\n" +
        "         from (\n" +
        "                  select distinct a.kpi_id, b.INPUT_LEVEL, d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS = 1\n" +
        "                           inner join monitor_query_kpi c\n" +
        "                                      on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS = 1 and d.KPI_ID = a.KPI_ID\n" +
        "                  where a.STATUS = 1\n" +
        "                  union\n" +
        "                  select distinct a.kpi_id, b.list_parent_input_level input_level, d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS = 1\n" +
        "                           inner join monitor_query_kpi c\n" +
        "                                      on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d\n" +
        "                                      on d.STATUS = 1 and d.KPI_ID = a.KPI_ID and d.FORMULA_LEVEL is not null\n" +
        "                  where a.STATUS = 1\n" +
        "                    and b.list_parent_input_level is not null\n" +
        "              ) a\n" +
        "         where a.input_level = 2\n" +
        "     ) kpi_level on a.item_value = kpi_level.domain_code\n" +
        "         inner join\n" +
        "     (\n" +
        "         select distinct kpi_id, time_type, domain_code\n" +
        "         from (\n" +
        "                  select distinct a.kpi_id, b.TIME_TYPE, c.RUN_TIME_SUCC, d.DOMAIN_CODE, d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS = 1\n" +
        "                           inner join monitor_query_kpi c\n" +
        "                                      on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d on d.STATUS = 1 and d.KPI_ID = a.KPI_ID\n" +
        "                  where a.STATUS = 1\n" +
        "                  union\n" +
        "                  select distinct a.kpi_id,\n" +
        "                                  3                     TIME_TYPE,\n" +
        "                                  makedate(year(c.RUN_TIME_SUCC), 1) + interval quarter(c.RUN_TIME_SUCC) quarter -\n" +
        "                                  interval 1 quarter as RUN_TIME_SUCC,\n" +
        "                                  d.DOMAIN_CODE,\n" +
        "                                  d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS = 1\n" +
        "                           inner join monitor_query_kpi c\n" +
        "                                      on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d\n" +
        "                                      on d.STATUS = 1 and d.KPI_ID = a.KPI_ID and d.FORMULA_QUAR is not null\n" +
        "                  where a.STATUS = 1\n" +
        "                  union\n" +
        "                  select distinct a.kpi_id,\n" +
        "                                  4                                     TIME_TYPE,\n" +
        "                                  makedate(year(c.RUN_TIME_SUCC), 1) as RUN_TIME_SUCC,\n" +
        "                                  d.DOMAIN_CODE,\n" +
        "                                  d.kpi_name\n" +
        "                  from config_map_kpi_query a\n" +
        "                           inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS = 1\n" +
        "                           inner join monitor_query_kpi c\n" +
        "                                      on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                           inner join cat_graph_kpi d\n" +
        "                                      on d.STATUS = 1 and d.KPI_ID = a.KPI_ID and d.FORMULA_YEAR is not null\n" +
        "                  where a.STATUS = 1\n" +
        "              ) a\n" +
        "     ) kpi_time on kpi_level.kpi_id = kpi_time.kpi_id and kpi_level.domain_code = kpi_time.domain_code\n" +
        "         inner join cat_item as tt\n" +
        "                    on kpi_time.time_type = tt.ITEM_VALUE and tt.CATEGORY_CODE = 'TIME_TYPE' and tt.STATUS = 1\n" +
        " where a.CATEGORY_CODE = 'DOMAIN'\n" +
        "  and a.status = 1\n" +
        " order by a.position, a.ITEM_NAME, tt.position, tt.item_name, kpi_level.kpi_name\n ";

    public CatItemCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CatItemDTO> findDomainAndTable() {
        StringBuilder stringBuffer = new StringBuilder(SQL_DOMAIN_AND_TABLE);
        Query query = entityManager.createNativeQuery(stringBuffer.toString());
        List<Object[]> lst = query.getResultList();
        List<CatItemDTO> results = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(lst)) {
            CatItemDTO catItemDTO;
            for (Object[] objects : lst) {
                catItemDTO = new CatItemDTO(DataUtil.safeToString(objects[0]), DataUtil.safeToString(objects[1]), DataUtil.safeToString(objects[2]));
                results.add(catItemDTO);
            }
        }
        return results;
    }

    @Override
    public List<CatItemDTO> getTimeTypeMap(String domainCode) {
        StringBuilder stringBuffer = new StringBuilder(SQL_TIMETYPE_MAPS);
        Query query = entityManager.createNativeQuery(stringBuffer.toString());
        query.setParameter("DOMAIN_CODE", domainCode);
        List<Object[]> lst = query.getResultList();
        List<CatItemDTO> results = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(lst)) {
            CatItemDTO catItem;
            for (Object[] objects : lst) {
                catItem = new CatItemDTO(DataUtil.safeToString(objects[0]), DataUtil.safeToString(objects[1]));
                results.add(catItem);
            }
        }
        return results;
    }
}
