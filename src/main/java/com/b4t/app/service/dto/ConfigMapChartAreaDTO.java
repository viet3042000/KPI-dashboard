package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigMapChartArea} entity.
 */
public class ConfigMapChartAreaDTO implements Serializable {

    private Long id;

    private Long chartId;

    private String chartName;

    private String typeChart;

    private Long areaId;

    private Long orderIndex;

    private Long screenIdNextto;

    private List<Long> linksChart;
    private List<Long> childsChart;

    private String screenIdNexttoName;

    private Long status;

    private Instant updateTime;

    private String updateUser;

    public List<Long> getChildsChart() {
        return childsChart;
    }

    public void setChildsChart(List<Long> childsChart) {
        this.childsChart = childsChart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getScreenIdNextto() {
        return screenIdNextto;
    }

    public void setScreenIdNextto(Long screenIdNextto) {
        this.screenIdNextto = screenIdNextto;
    }

    public String getScreenIdNexttoName() {
        return screenIdNexttoName;
    }

    public void setScreenIdNexttoName(String screenIdNexttoName) {
        this.screenIdNexttoName = screenIdNexttoName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

        ConfigMapChartAreaDTO configMapChartAreaDTO = (ConfigMapChartAreaDTO) o;
        if (configMapChartAreaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configMapChartAreaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigMapChartAreaDTO{" +
            "id=" + getId() +
            ", chartId=" + getChartId() +
            ", areaId=" + getAreaId() +
            ", orderIndex=" + getOrderIndex() +
            ", screenIdNextto=" + getScreenIdNextto() +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }

    public String getTypeChart() {
        return typeChart;
    }

    public void setTypeChart(String typeChart) {
        this.typeChart = typeChart;
    }

    public List<Long> getLinksChart() {
        return linksChart;
    }

    public void setLinksChart(List<Long> linksChart) {
        this.linksChart = linksChart;
    }
}
