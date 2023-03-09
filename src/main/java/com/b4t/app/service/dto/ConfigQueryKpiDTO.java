package com.b4t.app.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigQueryKpi} entity.
 */
public class ConfigQueryKpiDTO implements Serializable {

    private Long queryKpiId;

    private Integer timeType;

    private Long inputLevel;

    private String queryData;

    private String queryCheckData;

    private Integer status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private String listParentInputLevel;
    private Long reportId;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }


    public Long getQueryKpiId() {
        return queryKpiId;
    }

    public void setQueryKpiId(Long queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public Long getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getQueryData() {
        return queryData;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getQueryCheckData() {
        return queryCheckData;
    }

    public void setQueryCheckData(String queryCheckData) {
        this.queryCheckData = queryCheckData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getListParentInputLevel() {
        return listParentInputLevel;
    }

    public void setListParentInputLevel(String listParentInputLevel) {
        this.listParentInputLevel = listParentInputLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigQueryKpiDTO configQueryKpiDTO = (ConfigQueryKpiDTO) o;
        if (configQueryKpiDTO.getQueryKpiId() == null || getQueryKpiId() == null) {
            return false;
        }
        return Objects.equals(getQueryKpiId(), configQueryKpiDTO.getQueryKpiId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getQueryKpiId());
    }

    @Override
    public String toString() {
        return "ConfigQueryKpiDTO{" +
            "id=" + getQueryKpiId() +
            ", timeType=" + getTimeType() +
            ", inputLevel=" + getInputLevel() +
            ", queryData='" + getQueryData() + "'" +
            ", queryCheckData='" + getQueryCheckData() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            ", listParentInputLevel='" + getListParentInputLevel() + "'" +
            "}";
    }
}
