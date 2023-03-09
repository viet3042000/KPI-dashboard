package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.MonitorQueryKpi} entity.
 */
public class MonitorQueryKpiDTO implements Serializable {

    private Long id;

    private Integer queryKpiId;

    private Integer status;

    private Instant runTimeSucc;
    private Instant runTimeReport;

    private Instant updateTime;

    public Instant getRunTimeReport() {
        return runTimeReport;
    }

    public void setRunTimeReport(Instant runTimeReport) {
        this.runTimeReport = runTimeReport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQueryKpiId() {
        return queryKpiId;
    }

    public void setQueryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getRunTimeSucc() {
        return runTimeSucc;
    }

    public void setRunTimeSucc(Instant runTimeSucc) {
        this.runTimeSucc = runTimeSucc;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MonitorQueryKpiDTO monitorQueryKpiDTO = (MonitorQueryKpiDTO) o;
        if (monitorQueryKpiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), monitorQueryKpiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MonitorQueryKpiDTO{" +
            "id=" + getId() +
            ", queryKpiId=" + getQueryKpiId() +
            ", status=" + getStatus() +
            ", runTimeSucc='" + getRunTimeSucc() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
