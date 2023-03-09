package com.b4t.app.service;

import com.b4t.app.domain.KpiReport;
import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.dto.ReportKpiDTO;
import com.b4t.app.service.dto.TreeValue;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface KpiReportSearchService {

    CompletableFuture<List<ComboDTO>> onSearchKpi(String keyword);

    CompletableFuture<List<ComboDTO>> findHashTag(String keyword);

    CompletableFuture<List<ComboDTO>> findObject(ReportKpiDTO reportKpiDTO, String keyword) throws Exception;

    List<TreeValue> onSearchKpi(String keyword, List<String> lstKpi, Integer size);

    List<TreeValue> onSearchKpiHashTag(List<String> lstTag);

    ReportKpiDTO buildCondition(List<String> lstCondition);

    Page<KpiReport> search(String queryText, int pageNum, int size);
}
