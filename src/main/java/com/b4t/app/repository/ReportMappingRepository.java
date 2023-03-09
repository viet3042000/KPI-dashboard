package com.b4t.app.repository;


import com.b4t.app.domain.ObjectReport;
import com.b4t.app.service.dto.ReportKpiDTO;
import com.b4t.app.service.dto.ReportMappingSearchDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ReportMappingRepository {
    List<ObjectReport> findObjectReportForTree(String keyword , String kpiId, String tableName);
}
