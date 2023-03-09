package com.b4t.app.repository;

import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.domain.CatKpiReport;
import com.b4t.app.domain.ObjectReport;
import com.b4t.app.service.dto.BaseRptGraphESSearch;
import com.b4t.app.service.dto.ReportKpiDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KpiReportRepository {
    String CONFIG_NUMBER_BACK_TIME = "configNumberBackTime";

    List<CatKpiReport> findTreeKpi();

    List<ObjectReport> findObjectReportForTree(ReportKpiDTO reportKpiDTO, String keyword);

    List<ObjectReport> findObjectReport(ReportKpiDTO reportKpiDTO);

    @Cacheable(cacheNames = CONFIG_NUMBER_BACK_TIME)
    Integer findConfigNumberBackTime(String timeType);

    Long getMaxPrdId(ReportKpiDTO reportKpiDTO);

    Long findRptGraphCount(ReportKpiDTO reportKpiDTO);

    List<BaseRptGraph> findRptGraph(ReportKpiDTO reportKpiDTO, Pageable pageable);

    List<BaseRptGraphES> findBaseRptGraph(BaseRptGraphESSearch baseDTO);
}
