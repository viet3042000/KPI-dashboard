package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.domain.CatKpiReport;
import com.b4t.app.repository.KpiReportSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KpiReportSearchRepositoryImpl implements KpiReportSearchRepository {
    private final Logger log = LoggerFactory.getLogger(KpiReportSearchRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;

    public List<CatKpiReport> onSearchKpi(String keyword, List<String> lstKpi, Integer size) {
        try {
            String strQuery = "select cgk.DOMAIN_CODE, d.item_name as domain_name, " +
                " cgk.GROUP_KPI_CODE as parent_code, " +
                " c.ITEM_NAME as parent_name, " +
                " cgk.KPI_ID, " +
                " IF(u.item_name is null,cgk.kpi_name,concat(cgk.kpi_name,' (',u.item_name,')')) as kpi_name, " +
                " d.ITEM_VALUE as table_name, " +
                " u.item_name as unit_name, " +
                " d.position as d_position, " +
                " c.position as c_position, " +
                " IF(u.item_name is null,cgk.kpi_name,concat(cgk.kpi_name,' (',k.item_name,')')) as kpi_name_org " +
                " from cat_graph_kpi cgk  " +
                " inner join cat_item d on d.CATEGORY_CODE ='DOMAIN_TABLE' and cgk.DOMAIN_CODE = d.ITEM_CODE and d.status = 1 " +
                " inner join cat_item c on cgk.GROUP_KPI_CODE = c.ITEM_CODE and c.STATUS =1 and c.CATEGORY_CODE ='GROUP_KPI' " +
                " left join cat_item u on cgk.UNIT_VIEW_CODE = u.ITEM_CODE and u.STATUS =1 and u.CATEGORY_CODE ='UNIT' " +
                " left join cat_item k on cgk.UNIT_KPI = k.ITEM_CODE and k.STATUS =1 and k.CATEGORY_CODE ='UNIT' " +
                " where cgk.STATUS = 1 ";
            if (!DataUtil.isNullOrEmpty(keyword)) {
                strQuery += " and :keyword is null or cgk.kpi_name like :keyword or cgk.kpi_code like :keyword ";
            }
            if (!DataUtil.isNullOrEmpty(lstKpi)) {
                strQuery += " and cgk.KPI_ID in :lstKpi ";
            }
            if (size != null) {
                strQuery += " limit " + size;
            }
            Query query = manager.createNativeQuery(strQuery, CatKpiReport.class);
            if (!DataUtil.isNullOrEmpty(keyword)) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            if (!DataUtil.isNullOrEmpty(lstKpi)) {
                query.setParameter("lstKpi", lstKpi);
            }
            return query.getResultList();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public List<CatKpiReport> onSearchKpiHashTag(List<String> lstTag) {
        try {
            String strQuery = "select cgk.DOMAIN_CODE, d.item_name as domain_name, " +
                " cgk.GROUP_KPI_CODE as parent_code, " +
                " c.ITEM_NAME as parent_name, " +
                " cgk.KPI_ID, " +
                " IF(u.item_name is null,cgk.kpi_name,concat(cgk.kpi_name,' (',u.item_name,')')) as kpi_name, " +
                " d.ITEM_VALUE as table_name, " +
                " u.item_name as unit_name, " +
                " d.position as d_position, " +
                " c.position as c_position " +
                " from cat_graph_kpi cgk  " +
                " inner join cat_item d on d.CATEGORY_CODE ='DOMAIN_TABLE' and cgk.DOMAIN_CODE = d.ITEM_CODE and d.status = 1 " +
                " inner join cat_item c on cgk.GROUP_KPI_CODE = c.ITEM_CODE and c.STATUS =1 and c.CATEGORY_CODE ='GROUP_KPI' " +
                " left join cat_item u on cgk.UNIT_VIEW_CODE = u.ITEM_CODE and u.STATUS =1 and u.CATEGORY_CODE ='UNIT' " +
                " where cgk.STATUS = 1 ";
            Map<String, Object> mapParam = new HashMap<>();
            if (!DataUtil.isNullOrEmpty(lstTag)) {
                for (int i = 0; i < lstTag.size(); i++) {
                    String paramName = "lstTag" + i;
                    strQuery += " and cgk.KPI_ID in (select kpi_id from cat_kpi_synonym where synonym in :" + paramName + ")";
                    mapParam.put(paramName, Arrays.asList(lstTag.get(i)));
                }
            }
            Query query = manager.createNativeQuery(strQuery, CatKpiReport.class);
            if (!mapParam.isEmpty()) {
                for (Map.Entry<String, Object> entry : mapParam.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }

            return query.getResultList();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

}
