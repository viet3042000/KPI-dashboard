package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.repository.*;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.KpiReportSearchESService;
import com.b4t.app.service.ReportMappingService;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.CatKpiReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportMappingServiceImpl implements ReportMappingService {
    private static final Logger logger = LoggerFactory.getLogger(KpiReportSearchServiceImpl.class);
    @PersistenceContext
    private EntityManager manager;
    @Autowired
    KpiReportSearchRepository kpiReportSearchRepository;

    @Autowired
    CatKpiReportMapper catKpiReportMapper;

    @Autowired
    CatItemService catItemService;

    @Autowired
    CatKpiSynonymRepository catKpiSynonymRepository;

//    @Autowired
//    KpiReportSearchESRepository kpiReportSearchESRepository;

    @Autowired
    KpiReportSearchESService kpiReportSearchESService;

    @Autowired
    KpiReportRepository kpiReportRepository;
    @Autowired
    ReportMappingRepository reportMappingRepository;

    @Override
    public List<KpiReportMappingDTO> onSearchKpi(String keyword) {
        List<KpiReportMappingDTO> result = kpiReportSearchRepository.onSearchKpi(keyword, null, 50).stream()
            .map(e -> {
                KpiReportMappingDTO kpiReportMappingDTO = new KpiReportMappingDTO(e.getKpiId(), e.getKpiNameOrg(), e.getTableName());
                return kpiReportMappingDTO;
            }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ObjectReportDTO> findObject(String keyword, String kpiId, String tableName) throws Exception {

        List<ObjectReportDTO> lstObj;
        try {
//            lstObj = kpiReportSearchESService.onSearchObject(keyword).stream().map(e -> {
//                ObjectReportDTO objectReportDTO = new ObjectReportDTO("object#" + e.getObjCodeFull(), e.getObjNameFull());
//                return objectReportDTO;
//            }).collect(Collectors.toList());
            lstObj = reportMappingRepository.findObjectReportForTree(keyword, kpiId, tableName).stream().map(e -> {
                ObjectReportDTO objectReportDTO = new ObjectReportDTO(e.getObjectCode(), e.getObjectName());
                return objectReportDTO;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            lstObj = null;
        }
        return lstObj;
    }

    @Override
    public String getMappingData(String colName, String table_name_dashboard, String objectDashboardId, String prdId, String kpiDashboardId , String timeType) {
        Map<String, Object> mapParam = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        if (!DataUtil.isNullOrEmpty(colName)) {
            sql.append(" select ").append(colName).append(" ");
        }
        if (!DataUtil.isNullOrEmpty(table_name_dashboard)) {
            sql.append(" from ").append(table_name_dashboard).append(" where 1=1 ");
        }
        if (!DataUtil.isNullOrEmpty(kpiDashboardId)) {
            sql.append(" and kpi_id = :kpiDashboardId");
            mapParam.put("kpiDashboardId", kpiDashboardId);
        }
        if (!DataUtil.isNullOrEmpty(objectDashboardId)) {
            sql.append(" and obj_code = :objectDashboardId");
            mapParam.put("objectDashboardId", objectDashboardId);
        }
        if (!DataUtil.isNullOrEmpty(timeType)) {
            sql.append(" and time_type = :timeType");
            mapParam.put("timeType", timeType);
        }
        if (!DataUtil.isNullOrEmpty(prdId)) {
            sql.append(" and prd_id = :prdId");
            mapParam.put("prdId", prdId);
        }
        Query q = manager.createNativeQuery(sql.toString());
        mapParam.forEach(q::setParameter);
        List<?> rs = q.getResultList();
        return !DataUtil.isNullOrEmpty(rs) && rs.get(0) != null ? rs.get(0).toString(): "N/A";
    }
}
