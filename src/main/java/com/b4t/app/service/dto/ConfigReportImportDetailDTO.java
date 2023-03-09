package com.b4t.app.service.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ConfigReportImportDetailDTO {
    @Valid
    @NotNull(message = "{error.configReport.configReportImportDTONull}")
    private ConfigReportImportDTO configReportImportDTO;

    @Valid
    @NotNull(message = "{error.configReport.configReportDataImportNull}")
    private ConfigReportDataImport configReportDataImport;

    public ConfigReportImportDTO getConfigReportImportDTO() {
        return configReportImportDTO;
    }

    public void setConfigReportImportDTO(ConfigReportImportDTO configReportImportDTO) {
        this.configReportImportDTO = configReportImportDTO;
    }

    public ConfigReportDataImport getConfigReportDataImport() {
        return configReportDataImport;
    }

    public void setConfigReportDataImport(ConfigReportDataImport configReportDataImport) {
        this.configReportDataImport = configReportDataImport;
    }
}
