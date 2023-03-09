package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

public class ConfigReportImportDTO implements Serializable {
    @NotNull(message = "{error.configReportImportDTO.reportIdNull}")
    private Long reportId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String unit;
    @NotNull(message = "{error.configReportImportDTO.importTimeNull}")
    private Instant importTime;
    private String result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultipartFile importFile;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Instant getImportTime() {
        return importTime;
    }

    public void setImportTime(Instant importTime) {
        this.importTime = importTime;
    }

    public MultipartFile getImportFile() {
        return importFile;
    }

    public void setImportFile(MultipartFile importFile) {
        this.importFile = importFile;
    }
}
