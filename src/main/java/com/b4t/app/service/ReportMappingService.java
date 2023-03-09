package com.b4t.app.service;

import com.b4t.app.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportMappingService {
    List<KpiReportMappingDTO> onSearchKpi(String keyword);

    List<ObjectReportDTO> findObject(String keyword , String kpiId, String tableName) throws Exception;
    String getMappingData(String colName, String table_name_dashboard, String objectDashboardId, String prdId , String kpiDashboardId, String timeType);
}
