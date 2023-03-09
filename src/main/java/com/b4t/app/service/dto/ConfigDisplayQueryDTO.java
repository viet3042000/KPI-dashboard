package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigDisplayQuery} entity.
 */
public class ConfigDisplayQueryDTO implements Serializable {

    private Long id;

    private Long itemChartId;

    private String columnQuery;

    private String dataType;

    private String columnChart;

    private Long isRequire;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemChartId() {
        return itemChartId;
    }

    public void setItemChartId(Long itemChartId) {
        this.itemChartId = itemChartId;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnChart() {
        return columnChart;
    }

    public void setColumnChart(String columnChart) {
        this.columnChart = columnChart;
    }

    public Long getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(Long isRequire) {
        this.isRequire = isRequire;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigDisplayQueryDTO configDisplayQueryDTO = (ConfigDisplayQueryDTO) o;
        if (configDisplayQueryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configDisplayQueryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigDisplayQueryDTO{" +
            "id=" + getId() +
            ", itemChartId=" + getItemChartId() +
            ", columnQuery='" + getColumnQuery() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", columnChart='" + getColumnChart() + "'" +
            ", isRequire=" + getIsRequire() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
