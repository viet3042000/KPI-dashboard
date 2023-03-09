package com.b4t.app.service.dto;

import java.time.Instant;
import java.util.Map;

public class ConfigReportImportForm {
    private Long reportId;
    private Instant dataTime;
    private Map<String, Object> mapValue;

    public Instant getDataTime() {
        return dataTime;
    }

    public void setDataTime(Instant dataTime) {
        this.dataTime = dataTime;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Map<String, Object> getMapValue() {
        return mapValue;
    }

    public void setMapValue(Map<String, Object> mapValue) {
        this.mapValue = mapValue;
    }
}
