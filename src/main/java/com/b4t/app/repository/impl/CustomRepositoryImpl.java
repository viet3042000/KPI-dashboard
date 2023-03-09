package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.domain.CatItem;
import com.b4t.app.repository.CustomRepository;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.mapper.CatItemMapper;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class CustomRepositoryImpl implements CustomRepository {
    @PersistenceContext
    private EntityManager manager;
    private static final Logger logger = LoggerFactory.getLogger(CustomRepositoryImpl.class);

    private CatItemMapper catItemMapper;

    public CustomRepositoryImpl(CatItemMapper catItemMapper) {
        this.catItemMapper = catItemMapper;
    }

    private static final String DATA_TIME_PARAM = "#dataTime";

    @Override
    public List<CatItemDTO> getTimeTypeByKpis(Long[] kpiIds) {
        if (DataUtil.isNullOrEmpty(kpiIds)) return new ArrayList<>();
        String sql = "select *" +
            " from cat_item " +
            " where CATEGORY_CODE = 'TIME_TYPE'" +
            "  and STATUS = 1" +
            "  and ITEM_VALUE in (" +
            "    select b.TIME_TYPE as TIME_TYPE" +
            "    from config_map_kpi_query a" +
            "             inner join config_query_kpi b " +
            "                        on a.QUERY_KPI_ID = b.id and b.STATUS = 1" +
            "             inner join monitor_query_kpi c" +
            "                        on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null" +
            "    where a.STATUS = 1" +
            "      and a.KPI_ID in (:kpiIds)" +
            "    union" +
            "    select '3' as TIME_TYPE" +
            "    from config_map_kpi_query a" +
            "             inner join config_query_kpi b " +
            "                        on a.QUERY_KPI_ID = b.id and b.STATUS = 1" +
            "             inner join monitor_query_kpi c" +
            "                        on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null" +
            "             inner join cat_graph_kpi d" +
            "                        on d.STATUS = 1 and d.KPI_ID = a.KPI_ID and d.FORMULA_QUAR is not null" +
            "    where a.STATUS = 1" +
            "      and a.KPI_ID in (:kpiIds)" +
            "    union" +
            "    select '4' TIME_TYPE" +
            "    from config_map_kpi_query a" +
            "             inner join config_query_kpi b " +
            "                        on a.QUERY_KPI_ID = b.id and b.STATUS = 1" +
            "             inner join monitor_query_kpi c" +
            "                        on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null" +
            "             inner join cat_graph_kpi d" +
            "                        on d.STATUS = 1 and d.KPI_ID = a.KPI_ID and d.FORMULA_YEAR is not null" +
            "    where a.STATUS = 1" +
            "      and a.KPI_ID in (:kpiIds))";

        Query query = manager.createNativeQuery(sql, CatItem.class);
        query.setParameter("kpiIds", Arrays.asList(kpiIds));
        List<CatItem> rs = query.getResultList();
        return catItemMapper.toDto(rs);
    }


    public boolean checkHasData(String sql, Date date) {
        try {
            String dateFormat = null;
            if (sql.contains(DATA_TIME_PARAM)) {
                sql = sql.replace(DATA_TIME_PARAM, ":dataTime");
                dateFormat = DataUtil.dateToString(date, "yyyy-MM-dd");
            }
            Query query = manager.createNativeQuery(sql);
            if (dateFormat != null) {
                query.setParameter("dataTime", dateFormat);
            }
            query.getResultList();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }
    public boolean checkQueryData(String sql, Date date) {
        try {
            String queryData = buildQuery(sql, date);
            Query query = manager.createNativeQuery(queryData);
            query.getResultList();
            return true;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }
    private String buildQuery(String sql, Date dataTime) {
        if (sql.contains(DATA_TIME_PARAM)) {
            sql = sql.replace(DATA_TIME_PARAM, "STR_TO_DATE('" + DataUtil.dateToString(dataTime, "yyyyMMdd") + "','%Y%m%d')");
        }
        return sql;
    }
}
