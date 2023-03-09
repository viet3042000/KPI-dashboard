package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.domain.CatKpiReport;
import com.b4t.app.domain.ObjectReport;
import com.b4t.app.repository.KpiReportRepository;
import com.b4t.app.service.dto.BaseRptGraphESSearch;
import com.b4t.app.service.dto.ReportKpiDTO;
import com.b4t.app.service.dto.TreeValue;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class KpiReportRepositoryImpl implements KpiReportRepository {
    private final Logger log = LoggerFactory.getLogger(KpiReportRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;
    private static final String YYYYMMDD = "yyyyMMdd";

    @Override
    public List<CatKpiReport> findTreeKpi() {
        try {
            String strQuery = "select cgk.DOMAIN_CODE, d.item_name as domain_name, " +
                "cgk.GROUP_KPI_CODE as parent_code, " +
                "c.ITEM_NAME as parent_name, " +
                "cgk.KPI_ID, " +
                "IF(u.item_name is null,cgk.kpi_name,concat(cgk.kpi_name,' (',u.item_name,')')) as kpi_name, " +
                "d.ITEM_VALUE as table_name, " +
                "u.item_name as unit_name, " +
                "d.position as d_position, " +
                "c.position as c_position, " +
                " IF(u.item_name is null,cgk.kpi_name,concat(cgk.kpi_name,' (',k.item_name,')')) as kpi_name_org " +
                "from cat_graph_kpi cgk  " +
                "inner join cat_item d on d.CATEGORY_CODE ='DOMAIN_TABLE' and cgk.DOMAIN_CODE = d.ITEM_CODE and d.status = 1 " +
                "inner join cat_item c on cgk.GROUP_KPI_CODE = c.ITEM_CODE and c.STATUS =1 and c.CATEGORY_CODE ='GROUP_KPI' " +
                "left join cat_item u on cgk.UNIT_VIEW_CODE = u.ITEM_CODE and u.STATUS =1 and u.CATEGORY_CODE ='UNIT' " +
                " left join cat_item k on cgk.UNIT_KPI = k.ITEM_CODE and k.STATUS =1 and k.CATEGORY_CODE ='UNIT' " +
                "where cgk.STATUS = 1 " +
                "order by d.position, c.position , cgk.KPI_NAME ";
            Query query = manager.createNativeQuery(strQuery, CatKpiReport.class);
            return query.getResultList();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public List<ObjectReport> findObjectReport(ReportKpiDTO reportKpiDTO) {
        Map<String, Object> mapParamFindObj = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, List<TreeValue>> lstData = reportKpiDTO.getKpiIds().stream().collect(Collectors.groupingBy(TreeValue::getDes, Collectors.toList()));
        AtomicInteger index = new AtomicInteger(0);
        lstData.forEach((table, lstKpi) -> {
            if (index.get() > 0) {
                queryBuilder.append(" UNION ");
            }
            queryBuilder.append(" (select distinct if(parent_code is null,obj_code,concat(parent_code,'-',obj_code)) as obj_code, ");
            queryBuilder.append(" if(parent_name is null,obj_name,concat(parent_name,'-',obj_name)) as obj_name  ");
            queryBuilder.append(" from ").append(table).append(" ");
            queryBuilder.append(" where input_level in :lstInputLevel ");
            String lstKpiIdParam = "listKpiId" + index.get();
            queryBuilder.append(" and kpi_id in :").append(lstKpiIdParam);
            queryBuilder.append(" and time_type = :timeType ");
            queryBuilder.append(" and prd_id >= :fromPrdId ");
            queryBuilder.append(" and prd_id <= :toPrdId ");
            queryBuilder.append(" order by obj_name )");

            mapParamFindObj.put("lstInputLevel", reportKpiDTO.getInputLevels());
            List<String> lstKpiId = lstKpi.stream().map(TreeValue::getId).collect(Collectors.toList());
            mapParamFindObj.put(lstKpiIdParam, lstKpiId);
            mapParamFindObj.put("timeType", reportKpiDTO.getTimeType());
            mapParamFindObj.put("fromPrdId", DataUtil.getDateInt(reportKpiDTO.getFromDate(), YYYYMMDD));
            mapParamFindObj.put("toPrdId", DataUtil.getDateInt(reportKpiDTO.getToDate(), YYYYMMDD));

            index.getAndIncrement();

        });
        StringBuilder sb = new StringBuilder("select tbl1.* from (").append(queryBuilder).append(") as tbl1");

        Query query = manager.createNativeQuery(sb.toString());
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        mapParamFindObj.forEach((param, value) -> {
            if (value instanceof Collection) {
                queryHibernate.setParameterList(param, (List) value);
            } else {
                queryHibernate.setParameter(param, value);
            }
        });
        List<Map> result = queryHibernate.list();
        List<ObjectReport> lstObj = result.stream().map(row -> {
            ObjectReport obj = new ObjectReport();
            obj.setObjectCode(DataUtil.safeToString(row.get("obj_code")));
            obj.setObjectName(DataUtil.safeToString(row.get("obj_name")));
            return obj;
        }).collect(Collectors.toList());
        return lstObj;
    }

    public List<ObjectReport> findObjectReportForTree(ReportKpiDTO reportKpiDTO, String keyword) {
        Map<String, Object> mapParamObjectTree = new HashMap<>();
        StringBuilder queryFindObjTree = new StringBuilder();
        Map<String, List<TreeValue>> lstData = reportKpiDTO.getKpiIds().stream().collect(Collectors.groupingBy(TreeValue::getDes, Collectors.toList()));
        AtomicInteger index = new AtomicInteger(0);
        lstData.forEach((table, lstKpi) -> {
            if (index.get() > 0) {
                queryFindObjTree.append(" UNION ");
            }
            queryFindObjTree.append(" ( select distinct parent_code, parent_name, if(parent_code is null,obj_code,concat(parent_code,'-',obj_code)) as obj_code, if(parent_name is null,obj_name,concat(parent_name, ' ',obj_name))  as obj_name, if(input_level=2,3,if(input_level=4,2,input_level)) as input_level");
            queryFindObjTree.append(" from ").append(table).append(" ");
            queryFindObjTree.append(" where 1=1 ");
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels())) {
                queryFindObjTree.append(" and input_level in :lstInputLevel ");
                mapParamObjectTree.put("lstInputLevel", reportKpiDTO.getInputLevels());
            }
            if (!DataUtil.isNullOrEmpty((List) lstKpi)) {
                String lstKpiIdParam = "listKpiId" + index.get();
                queryFindObjTree.append(" and kpi_id in :").append(lstKpiIdParam);
                mapParamObjectTree.put(lstKpiIdParam, lstKpi);
            }
            if (!DataUtil.isNullOrEmpty(keyword)) {
                queryFindObjTree.append(" and (obj_name like :keyword or concat(parent_name, ' ',obj_name) like :keyword )");
                mapParamObjectTree.put("keyword", "%" + keyword + "%");
            }
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getTimeType())) {
                queryFindObjTree.append(" and time_type = :timeType");
                mapParamObjectTree.put("timeType", reportKpiDTO.getTimeType());
            }
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getFromDate())) {
                queryFindObjTree.append(" and prd_id >= :fromPrdId ");
                mapParamObjectTree.put("fromPrdId", DataUtil.getDateInt(reportKpiDTO.getFromDate(), YYYYMMDD));
            }
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getToDate())) {
                queryFindObjTree.append(" and prd_id <= :toPrdId ");
                mapParamObjectTree.put("toPrdId", DataUtil.getDateInt(reportKpiDTO.getToDate(), YYYYMMDD));
            }
            queryFindObjTree.append(" order by input_level asc, obj_name )");

            index.getAndIncrement();

        });
        StringBuilder sbQuery = new StringBuilder("select tbl2.* from (").append(queryFindObjTree).append(") as tbl2");

        Query queryBuilder = manager.createNativeQuery(sbQuery.toString());
        org.hibernate.query.Query hibernateQuery = queryBuilder.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        mapParamObjectTree.forEach((param, value) -> {
            if (value instanceof Collection) {
                hibernateQuery.setParameterList(param, (List) value);
            } else {
                hibernateQuery.setParameter(param, value);
            }
        });

        List<Map<String, Object>> result = hibernateQuery.list();
        List<ObjectReport> lstObj = result.stream().map(this::mapToObjectReport).collect(Collectors.toList());

        return lstObj;
    }

    @Override
    public Integer findConfigNumberBackTime(String timeType) {
        Integer result = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" select dt.itemValue from CatItem dt ");
        sb.append(" inner join CatItem tt on dt.parentItemId = tt.itemId and tt.status = 1 and tt.categoryCode = 'TIME_TYPE' ");
        sb.append(" where dt.categoryCode = 'DURATION_TIME' and dt.status = 1 ");
        sb.append(" AND tt.itemValue = :timeType ");
        Query query = manager.createQuery(sb.toString());
        query.setParameter("timeType", timeType);
        List lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            result = DataUtil.safeToInt(lst.get(0));
        }
        return result;
    }



    @Override
    public Long getMaxPrdId(ReportKpiDTO reportKpiDTO) {
        Long result = null;
        Map<String, Object> mapParam = new HashMap<>();
        String sql = buildSql(reportKpiDTO, mapParam, Constants.QUERY_TYPE.MAX_PRD_ID);
        Query query = manager.createNativeQuery(sql);
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class);
        DbUtils.setParramToQuery(queryHibernate, mapParam);
        List<Object> lstData = queryHibernate.list();
        if (!DataUtil.isNullOrEmpty(lstData)) {
            result = DataUtil.safeToLong(lstData.get(0), null);
        }
        return result;
    }


    public String buildSql(ReportKpiDTO reportKpiDTO, Map<String, Object> mapParam, int queryType) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, List<TreeValue>> lstData = reportKpiDTO.getKpiIds().stream().collect(Collectors.groupingBy(TreeValue::getDes, Collectors.toList()));
        AtomicInteger index = new AtomicInteger(0);
        lstData.forEach((table, lstKpi) -> {
            if (index.get() > 0) {
                stringBuilder.append(" UNION ");
            }
            stringBuilder.append(" ( ");
            stringBuilder.append(" select if(a.parent_code is null, a.obj_code,concat(a.parent_code,'-', a.obj_code)) as obj_code, if(a.parent_name is null, a.obj_name,concat(a.parent_name,'-', a.obj_name)) as obj_name, a.KPI_ID, b.KPI_DISPLAY,  ");
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getValueType())) {
                stringBuilder.append(" round(a.").append(reportKpiDTO.getValueType()).append("/b.RATE,2) as value, ");
            }
            stringBuilder.append(" a.time_type, a.prd_id, a.kpi_code, a.kpi_name, a.parent_code, a.parent_name, a.input_level,  ");
            stringBuilder.append(" a.val_plan, round(a.val_plan_year/(if(b.RATE is null, 1, b.RATE)),2) val_plan_year, round(a.val/(if(b.RATE is null, 1, b.RATE)),2) val, round(a.val_acc/(if(b.RATE is null, 1, b.RATE)),2) val_acc, round(a.val_total/(if(b.RATE is null, 1, b.RATE)),2) val_total, a.val_lastest, a.val_last_year,  ");
            stringBuilder.append(" a.val_delta, a.val_delta_year, a.percent_plan, a.percent_plan_year,  a.percent_grow, a.percent_grow_year, a.alarm_level_plan,  a.unit_kpi, b.unit_view_code, a.domain_code, prd_id as x_axis, c.ITEM_NAME as unit_name  ");
            stringBuilder.append(" from ").append(table).append(" a ");
            stringBuilder.append(" left join cat_graph_kpi b on a.kpi_id = b.kpi_id and b.STATUS = 1 ");
            stringBuilder.append(" left join cat_item c on b.UNIT_VIEW_CODE = c.ITEM_CODE and c.STATUS =1 and c.CATEGORY_CODE ='UNIT' ");
            stringBuilder.append(" where 1 = 1 ");
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels())) {
                stringBuilder.append(" and a.input_level in :lstInputLevel ");
                mapParam.put("lstInputLevel", reportKpiDTO.getInputLevels());
            }
            if (!DataUtil.isNullOrEmpty(lstKpi)) {
                List<String> lstKpiId = lstKpi.stream().map(TreeValue::getId).filter(Objects::nonNull).collect(Collectors.toList());
                if (!DataUtil.isNullOrEmpty(lstKpiId)) {
                    String lstKpiIdParam = "listKpiId" + index.get();
                    stringBuilder.append(" and a.kpi_id in :").append(lstKpiIdParam);
                    mapParam.put(lstKpiIdParam, lstKpiId);
                }
            }
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getTimeType())) {
                stringBuilder.append(" and a.time_type = :timeType");
                mapParam.put("timeType", reportKpiDTO.getTimeType());
            }
            if (reportKpiDTO.getFromDate() != null) {
                stringBuilder.append(" and a.prd_id >= :fromPrdId ");
                mapParam.put("fromPrdId", DataUtil.getDateInt(reportKpiDTO.getFromDate(), YYYYMMDD));
            }
            if (reportKpiDTO.getToDate() != null) {
                stringBuilder.append(" and a.prd_id <= :toPrdId ");
                mapParam.put("toPrdId", DataUtil.getDateInt(reportKpiDTO.getToDate(), YYYYMMDD));
            }
            if (reportKpiDTO.getFromValue() != null) {
                stringBuilder.append(" and a.").append(reportKpiDTO.getValueType()).append("/b.RATE >= :fromValue ");
                mapParam.put("fromValue", reportKpiDTO.getFromValue());
            }
            if (reportKpiDTO.getToValue() != null) {
                stringBuilder.append(" and a.").append(reportKpiDTO.getValueType()).append("/b.RATE <= :toValue ");
                mapParam.put("toValue", reportKpiDTO.getToValue());
            }

            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getObjects()) && queryType != Constants.QUERY_TYPE.MAX_PRD_ID) {
                stringBuilder.append(" and (a.obj_code in :listObject or concat(a.parent_code,'-', a.obj_code) in :listObject) ");
                mapParam.put("listObject", reportKpiDTO.getObjects());
            }
            stringBuilder.append(" )");

            index.getAndIncrement();

        });
        StringBuilder sb = new StringBuilder();
        switch (queryType) {
            case Constants.QUERY_TYPE.MAX_PRD_ID:
                sb.append("select max(tbl3.prd_id) from (").append(stringBuilder).append(" ) as tbl3");
                break;
            case Constants.QUERY_TYPE.QUERY_DATA:
                sb.append("select tbl3.* from (").append(stringBuilder).append(" ) as tbl3");
                break;
            case Constants.QUERY_TYPE.COUNT:
                sb.append("select count(*) from (").append(stringBuilder).append(" ) as tbl3");
                break;
        }
        return sb.toString();
    }

    @Override
    public List<BaseRptGraph> findRptGraph(ReportKpiDTO reportKpiDTO, Pageable pageable) {

        Map<String, Object> mapParam = new HashMap<>();
        String sql = buildSql(reportKpiDTO, mapParam, Constants.QUERY_TYPE.QUERY_DATA);
        Query query = manager.createNativeQuery(sql);
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        DbUtils.setParramToQuery(queryHibernate, mapParam);
        if (pageable != null) {
            queryHibernate.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }
        List<Map<String, Object>> lstData = queryHibernate.list();
        List<BaseRptGraph> lstResult = lstData.stream().map(this::mapToBaseRptGraph).collect(Collectors.toList());
        return lstResult;
    }

    @Override
    public List<BaseRptGraphES> findBaseRptGraph(BaseRptGraphESSearch baseDTO) {
        List<BaseRptGraphES> lstResult;
        Map<String, Object> mapParam = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.id, a.time_type, a.prd_id, a.kpi_id, a.kpi_code, a.kpi_name, a.obj_code, a.obj_name, a.parent_code, a.parent_name, a.input_level, a.val_plan, a.val_plan_year, " +
            " a.val, a.val_acc, a.val_lastest, a.val_last_year, a.val_delta, a.val_delta_year, a.percent_plan, a.percent_plan_year, a.percent_grow, a.percent_grow_year, " +
            " a.alarm_level_plan, a.alarm_level_plan_year, a.alarm_level_grow, a.alarm_level_grow_year, a.unit_kpi, b.unit_view_code, a.domain_code, a.update_time," +
            " a.val_total, a.val_total_last_year, b.rate, b.kpi_display, c.table_destination, b.alarm_plan_type, b.alarm_threshold_type, b.unit_name, if(a.parent_code is null,a.obj_code,concat(a.parent_code,'-',a.obj_code)) obj_code_full, " +
            " '' obj_name_full, '' obj_name_unsigned, '' kpi_name_unsigned " +
            " FROM ").append(baseDTO.getTableDestination()).append(" a ");
        sb.append(" left join (select b1.*, b2.item_name as unit_name from cat_graph_kpi b1 left join cat_item b2 on b1.unit_view_code = b2.item_code and b2.status =1 and b2.category_code = 'UNIT') b on a.kpi_id = b.kpi_id");
        sb.append(" left join config_map_kpi_query c on a.kpi_id = c.kpi_id");
        sb.append(" where 1=1 ");

        if(!DataUtil.isNullOrEmpty(baseDTO.getFromPrdId())) {
            sb.append(" and a.prd_id >= :fromPrdId ");
            mapParam.put("fromPrdId", baseDTO.getFromPrdId());
        }
        if(!DataUtil.isNullOrEmpty(baseDTO.getToPrdId())) {
            sb.append(" and a.prd_id <= :toPrdId ");
            mapParam.put("toPrdId", baseDTO.getToPrdId());
        }
        if(!DataUtil.isNullOrEmpty(baseDTO.getKpiId())) {
            sb.append(" and a.kpi_id = :kpiId");
            mapParam.put("kpiId", baseDTO.getKpiId());
        }
        Query query = manager.createNativeQuery(sb.toString(), BaseRptGraphES.class);
        query = DbUtils.setParam(query, mapParam);
        lstResult = query.getResultList();
        return lstResult;
    }

    public Long findRptGraphCount(ReportKpiDTO reportKpiDTO) {
        Long numRow = 0L;
        Map<String, Object> mapParam = new HashMap<>();
        String sql = buildSql(reportKpiDTO, mapParam, Constants.QUERY_TYPE.COUNT);
        Query query = manager.createNativeQuery(sql);
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class);
        DbUtils.setParramToQuery(queryHibernate, mapParam);
        List<Object> lstData = queryHibernate.list();
        if (!DataUtil.isNullOrEmpty(lstData)) {
            numRow = Long.valueOf((lstData.get(0)).toString());
        }
        return numRow;
    }

    private BaseRptGraph mapToBaseRptGraph(Map<String, Object> map) {
        BaseRptGraph result = DbUtils.transformMapToEntity(map, BaseRptGraph.class);
        return result;
    }

    private ObjectReport mapToObjectReport(Map<String, Object> map) {
        ObjectReport result = DbUtils.transformMapToEntity(map, ObjectReport.class);
        return result;
    }

}
