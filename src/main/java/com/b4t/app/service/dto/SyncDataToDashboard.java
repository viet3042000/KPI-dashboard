package com.b4t.app.service.dto;

import java.util.List;

public class SyncDataToDashboard {
    private RpReportDTO reportDTO;
    List<MapReportDataToDashboardDTO> lstReportDatas;

    public SyncDataToDashboard() {
    }

    public RpReportDTO getReportDTO() {
        return reportDTO;
    }

    public void setReportDTO(RpReportDTO reportDTO) {
        this.reportDTO = reportDTO;
    }

    public List<MapReportDataToDashboardDTO> getLstReportDatas() {
        return lstReportDatas;
    }

    public void setLstReportDatas(List<MapReportDataToDashboardDTO> lstReportDatas) {
        this.lstReportDatas = lstReportDatas;
    }
}
