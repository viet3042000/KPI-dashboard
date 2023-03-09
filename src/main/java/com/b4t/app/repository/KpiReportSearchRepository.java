package com.b4t.app.repository;

import com.b4t.app.domain.CatKpiReport;

import java.util.List;

public interface KpiReportSearchRepository {

    List<CatKpiReport> onSearchKpiHashTag(List<String> lstTag);

    List<CatKpiReport> onSearchKpi(String keyword, List<String> lstKpi, Integer size);

}
