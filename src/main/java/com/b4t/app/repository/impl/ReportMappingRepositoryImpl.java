package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.ObjectReport;
import com.b4t.app.repository.ReportMappingRepository;
import com.b4t.app.service.dto.ReportKpiDTO;
import com.b4t.app.service.dto.ReportMappingSearchDTO;
import com.b4t.app.service.dto.TreeValue;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Repository
public class ReportMappingRepositoryImpl implements ReportMappingRepository {
    private final Logger log = LoggerFactory.getLogger(KpiReportRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;
    private static final String YYYYMMDD = "yyyyMMdd";

    public List<ObjectReport> findObjectReportForTree(String keyword , String kpiId, String tableName) {
        Map<String, Object> mapParamObjectTree = new HashMap<>();
        StringBuilder queryFindObjTree = new StringBuilder();
            queryFindObjTree.append(" ( select distinct parent_code, parent_name, if(parent_code is null,obj_code,concat(parent_code,'-',obj_code)) as obj_code, if(parent_name is null,obj_name,concat(parent_name, ' ',obj_name))  as obj_name, if(input_level=2,3,if(input_level=4,2,input_level)) as input_level");
            queryFindObjTree.append(" from ").append(tableName).append(" ");
            queryFindObjTree.append(" where 1=1 ");
            if (!DataUtil.isNullOrEmpty(kpiId)) {
                queryFindObjTree.append(" and kpi_id like :kpiId");
                mapParamObjectTree.put("kpiId", "%" + kpiId + "%");
            }
            if (!DataUtil.isNullOrEmpty(keyword)) {
                queryFindObjTree.append(" and (obj_name like :keyword or concat(parent_name, ' ',obj_name) like :keyword )");
                mapParamObjectTree.put("keyword", "%" + keyword + "%");
            }
            queryFindObjTree.append(" order by input_level asc, obj_name )");
        StringBuilder sb = new StringBuilder("select tbl2.* from (").append(queryFindObjTree).append(") as tbl2");
        Query query = manager.createNativeQuery(sb.toString());
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        mapParamObjectTree.forEach(queryHibernate::setParameter);
        List<Map<String, Object>> result = queryHibernate.list();
        List<ObjectReport> lstObj = result.stream().map(this::mapToObjectReport).collect(Collectors.toList());
        return lstObj;
    }
    private ObjectReport mapToObjectReport(Map<String, Object> map) {
        ObjectReport result = DbUtils.transformMapToEntity(map, ObjectReport.class);
        return result;
    }
}
