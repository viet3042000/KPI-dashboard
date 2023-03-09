package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.repository.ChartMapRepository;
import com.b4t.app.service.dto.ChartMapParramDTO;
import com.b4t.app.service.dto.DataChartMapDTO;
import com.b4t.app.service.dto.RangeColorDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class ChartMapRepositoryImpl implements ChartMapRepository {
    private final EntityManager entityManager;
    private static final String QUERY_DATA_MAPS = "select a.obj_code,\n" +
        "       a.obj_name,\n" +
        "       a.KPI_ID,\n" +
        "       a.KPI_NAME,\n" +
        "       #dataPresent / b.RATE as        value,\n" +
        "       DATE_FORMAT(a.prd_id, '%Y%m%d') PRD_ID,\n" +
        "       c.ITEM_NAME           as        unit_name\n" +
        "from #tableName a\n" +
        "         left join cat_graph_kpi b\n" +
        "                   on a.kpi_id = b.kpi_id and b.STATUS = 1\n" +
        "         left join cat_item c on b.UNIT_VIEW_CODE = c.ITEM_CODE and c.STATUS = 1 and c.CATEGORY_CODE = 'UNIT'\n" +
        "where a.input_level = 2 \n" +
        "  and a.kpi_id = :kpiId \n" +
        "  and a.time_type = :timeType\n" +
        "  and a.prd_id = :toDate\n" +
        "  and a.parent_code is null ";


    private static final String QUERY_GET_MAX_TIME = " select distinct time_type, kpi_id, run_time_succ\n" +
        "from (\n" +
        "         select distinct a.kpi_id, b.TIME_TYPE, c.RUN_TIME_SUCC , d.DOMAIN_CODE\n" +
        "         from config_map_kpi_query a\n" +
        "                  inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1 and b.input_level = 2 \n" +
        "                  inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                  inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID\n" +
        "         where a.STATUS =1\n" +
        "         union\n" +
        "         select distinct a.kpi_id, 3 TIME_TYPE,\n" +
        "                         makedate(year(c.RUN_TIME_SUCC),1) + interval quarter(c.RUN_TIME_SUCC) quarter - interval 1 quarter as RUN_TIME_SUCC, d.DOMAIN_CODE\n" +
        "         from config_map_kpi_query a\n" +
        "                  inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1 and b.input_level = 2 \n" +
        "                  inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                  inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID and d.FORMULA_QUAR is not null\n" +
        "         where a.STATUS =1\n" +
        "         union\n" +
        "         select distinct a.kpi_id, 4 TIME_TYPE,\n" +
        "                         makedate(year(c.RUN_TIME_SUCC),1) as RUN_TIME_SUCC, d.DOMAIN_CODE\n" +
        "         from config_map_kpi_query a\n" +
        "                  inner join config_query_kpi b on a.QUERY_KPI_ID = b.id and b.STATUS =1 and b.input_level = 2 \n" +
        "                  inner join monitor_query_kpi c on a.QUERY_KPI_ID = c.QUERY_KPI_ID and RUN_TIME_SUCC is not null\n" +
        "                  inner join cat_graph_kpi d on d.STATUS =1 and d.KPI_ID = a.KPI_ID and d.FORMULA_YEAR is not null\n" +
        "         where a.STATUS =1\n" +
        "     ) a\n" +
        "where\n" +
        "        a.KPI_ID = :kpiId " +
        "and a.time_type = :timeType ";
    private static final String GET_SCREEN_ID_CHART_MAP = " select id as screen_id \n" +
        " from config_screen cs where is_default = 3 \n" +
        " and status = 1\n " +
        " and PROFILE_ID = :profileId\n ";
    private static final String QUERY_GET_RANGE_COLOR = " select" +
        "       a.name,\n" +
        "       c.kpi_id, \n" +
        "       a.color_code,  \n" +
        "       a.total_level, \n" +
        "       b.class_level, \n" +
        "       b.from_value,  \n" +
        "       b.to_value     \n" +
        " from CLASSIFY_COLOR_GMAP a\n " +
        "         inner join CLASSIFY_COLOR_GMAP_LEVEL b on a.id = b.class_id\n" +
        "         inner join CLASSIFY_COLOR_GMAP_KPI c on a.id = c.class_id\n" +
        " where a.status = 1 \n" +
        "  and c.kpi_id = :kpiId ";

    public ChartMapRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<RangeColorDTO> getRangeColor(RangeColorDTO rangeColorDTO) {
        Query query = entityManager.createNativeQuery(QUERY_GET_RANGE_COLOR);
        query.setParameter("kpiId", rangeColorDTO.getKpiId());
        List<Object[]> lst = query.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) return null;
        RangeColorDTO rangeColor;
        List<RangeColorDTO> resuls = new ArrayList<>();
        for (Object[] objects : lst) {
            rangeColor = new RangeColorDTO(objects);
            resuls.add(rangeColor);
        }
        return resuls;
    }

    public List<DataChartMapDTO> getChartMapsData(ChartMapParramDTO chartMapParramDTO) {
        StringBuilder stringBuilder = new StringBuilder(QUERY_DATA_MAPS.replace("#dataPresent", chartMapParramDTO.getDataPresent()).replace("#tableName", chartMapParramDTO.getTableName()));
        Map<String, Object> param = new HashMap<>();
        String prdId = chartMapParramDTO.getToDate().toString();
        param.put("kpiId", chartMapParramDTO.getKpiId());
        param.put("timeType", chartMapParramDTO.getTimeType());
        param.put("toDate", DataUtil.safeToLong(prdId));
        Query query = entityManager.createNativeQuery(stringBuilder.toString());
        DbUtils.setParramToQuery(query, param);
        List<Object[]> lst = query.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) return null;
        DataChartMapDTO dataChartMapDTO;
        List<DataChartMapDTO> results = new ArrayList<>();
        for (Object[] objects : lst) {
            dataChartMapDTO = new DataChartMapDTO(objects);
            results.add(dataChartMapDTO);
        }
        return results;
    }

    public Object getMaxTime(ChartMapParramDTO chartMapParramDTO) {
        StringBuilder stringBuilder = new StringBuilder(QUERY_GET_MAX_TIME);
        Map<String, Object> param = new HashMap<>();
        param.put("kpiId", chartMapParramDTO.getKpiId());
        param.put("timeType", chartMapParramDTO.getTimeType());
        Query query = entityManager.createNativeQuery(stringBuilder.toString());
        DbUtils.setParramToQuery(query, param);
        Object result = query.getResultList();
        return result;
    }

    public Long getScreenIdMap(Long profileId) {
        StringBuilder stringBuilder = new StringBuilder(GET_SCREEN_ID_CHART_MAP);
        Map<String, Object> param = new HashMap<>();
        param.put("profileId", profileId);
        Query query = entityManager.createNativeQuery(stringBuilder.toString());
        DbUtils.setParramToQuery(query, param);
        List<Object[]> lst = query.getResultList();
        if (DataUtil.isNullOrEmpty(lst)) {
            return null;
        }
        return DataUtil.safeToLong(lst.get(0));
    }
}
