package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.RpReport} entity.
 */
public class RpReportDTO implements Serializable {
    
    private Long id;

    private String reportName;

    private String reportCode;

    private Instant updateTime;

    private Integer prdId;

    private Integer timeType;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpReportDTO)) {
            return false;
        }

        return id != null && id.equals(((RpReportDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpReportDTO{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportCode='" + getReportCode() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", prdId=" + getPrdId() +
            ", timeType=" + getTimeType() +
            "}";
    }
}
