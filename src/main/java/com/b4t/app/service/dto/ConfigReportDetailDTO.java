package com.b4t.app.service.dto;

import java.util.List;

public class ConfigReportDetailDTO {
    private ConfigReportDTO configReport;
    private List<ConfigReportColumnDTO> configReportColumns;

    public ConfigReportDetailDTO() {
    }

    public ConfigReportDTO getConfigReport() {
        return configReport;
    }

    public void setConfigReport(ConfigReportDTO configReport) {
        this.configReport = configReport;
    }

    public List<ConfigReportColumnDTO> getConfigReportColumns() {
        return configReportColumns;
    }

    public void setConfigReportColumns(List<ConfigReportColumnDTO> configReportColumns) {
        this.configReportColumns = configReportColumns;
    }
}
