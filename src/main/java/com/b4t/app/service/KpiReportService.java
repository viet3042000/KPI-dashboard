package com.b4t.app.service;

import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service lay du lieu cho tra cuu chi tieu
 */
public interface KpiReportService {

    /**
     * Lay toan bo cac chi tieu
     * @return
     */
    List<CatKpiReportDTO> findCatKpiReport();

    /**
     * Lay danh sach cac object theo dieu kien ReportKpiDTO
     * @param reportKpiDTO
     * @return
     */
    List<ObjectReportDTO> findObjectReport(ReportKpiDTO reportKpiDTO);

    void updateTimeOfForm(ReportKpiDTO reportKpiDTO, BaseRptGraph mapTimeType) throws Exception;

    Page<BaseRptGraphESDTO> findRptGraph(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception;

    List<BaseRptGraphESDTO> findRptGraphFull(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception;

    Page<BaseRptGraphESDTO> findRptGraphPaging(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception;

    List<String> buildXAxis(ReportKpiDTO reportKpiDTO);

    CompletableFuture<BaseRptGraphDTO> getMaxPrdId(ReportKpiDTO reportKpiDTO, Integer timeType) throws Exception;

    List<BaseRptGraphESDTO> findBaseRptGraphDTO(BaseRptGraphESSearch baseDTO);

    List<BaseRptGraphES> findBaseRptGraph(BaseRptGraphESSearch baseDTO);
}
