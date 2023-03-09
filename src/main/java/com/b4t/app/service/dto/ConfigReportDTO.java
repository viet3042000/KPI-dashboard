package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigReport} entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigReportDTO implements Serializable {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String timeType;
    private String timeTypeName;

    @NotNull
    private String formInput;

    @NotNull
    private String domainCode;
    private String domainName;
    private String databaseName;

    private String tableName;

    @NotNull
    private String inputLevel;
    private String inputLevelName;

    private String unit;
    private String unitName;

    private Integer status;
    private Integer mainData;
    private String statusName;

    private String description;

    private String creator;

    private Instant updateTime;

    private String lockRow;

    private String rowPermission;

    private Integer alertDay;

    private Boolean alert;

    private Integer approveRequest;

    public Integer getApproveRequest() {
        return approveRequest;
    }

    public void setApproveRequest(Integer approveRequest) {
        this.approveRequest = approveRequest;
    }

    public Integer getAlertDay() {
        return alertDay;
    }

    public void setAlertDay(Integer alertDay) {
        this.alertDay = alertDay;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    public Integer getMainData() {
        return mainData;
    }

    public void setMainData(Integer mainData) {
        this.mainData = mainData;
    }

    public String getLockRow() {
        return lockRow;
    }

    public void setLockRow(String lockRow) {
        this.lockRow = lockRow;
    }

    public String getRowPermission() {
        return rowPermission;
    }

    public void setRowPermission(String rowPermission) {
        this.rowPermission = rowPermission;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTimeTypeName() {
        return timeTypeName;
    }

    public void setTimeTypeName(String timeTypeName) {
        this.timeTypeName = timeTypeName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getInputLevelName() {
        return inputLevelName;
    }

    public void setInputLevelName(String inputLevelName) {
        this.inputLevelName = inputLevelName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getformInput() {
        return formInput;
    }

    public void setformInput(String formInput) {
        this.formInput = formInput;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigReportDTO configReportDTO = (ConfigReportDTO) o;
        if (configReportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configReportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigReportDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", timeType='" + getTimeType() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            ", databaseName='" + getDatabaseName() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", inputLevel='" + getInputLevel() + "'" +
            ", unit='" + getUnit() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", creator='" + getCreator() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
