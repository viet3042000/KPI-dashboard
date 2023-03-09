package com.b4t.app.service;

import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.domain.ObjectRptGraphES;
import com.b4t.app.service.dto.ReportKpiDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KpiReportSearchESService {

    BaseRptGraph getLastData(ReportKpiDTO reportKpiDTO) throws Exception;

    Page<BaseRptGraphES> onSearch(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception;

    List<ObjectRptGraphES> onSearchObject(String keyword) throws Exception;

    List<ObjectRptGraphES> getAllObject(ReportKpiDTO reportKpiDTO) throws Exception;
}
