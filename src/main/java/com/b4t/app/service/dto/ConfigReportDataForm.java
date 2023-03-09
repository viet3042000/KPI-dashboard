package com.b4t.app.service.dto;

import java.time.Instant;
import java.util.Date;

public class ConfigReportDataForm {
    private Long reportId;
    private Instant dataTime;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Instant getDataTime() {
        return dataTime;
    }

    public void setDataTime(Instant dataTime) {
        this.dataTime = dataTime;
    }
}
