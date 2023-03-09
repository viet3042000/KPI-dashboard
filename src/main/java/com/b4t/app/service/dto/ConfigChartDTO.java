package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigChart} entity.
 */
public class ConfigChartDTO implements Serializable {

    private Long id;

    private String chartCode;

    private String chartName;

    private String titleChart;

    private String typeChart;

    private Integer timeTypeDefault;

    private Integer relativeTime;

    private String chartUrl;

    private Long groupChartId;

    private Long chartIdNextto;

    private Long orderIndex;

    private String groupKpiCode;

    private String domainCode;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private String chartConfig;

    private Integer childChart;

    private Long screenDetailId;

    public Long getScreenDetailId() {
        return screenDetailId;
    }

    public void setScreenDetailId(Long screenDetailId) {
        this.screenDetailId = screenDetailId;
    }

    public Integer getChildChart() {
        return childChart;
    }

    public void setChildChart(Integer childChart) {
        this.childChart = childChart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getTitleChart() {
        return titleChart;
    }

    public void setTitleChart(String titleChart) {
        this.titleChart = titleChart;
    }

    public String getTypeChart() {
        return typeChart;
    }

    public void setTypeChart(String typeChart) {
        this.typeChart = typeChart;
    }

    public Integer getTimeTypeDefault() {
        return timeTypeDefault;
    }

    public void setTimeTypeDefault(Integer timeTypeDefault) {
        this.timeTypeDefault = timeTypeDefault;
    }

    public Integer getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(Integer relativeTime) {
        this.relativeTime = relativeTime;
    }

    public String getChartUrl() {
        return chartUrl;
    }

    public void setChartUrl(String chartUrl) {
        this.chartUrl = chartUrl;
    }

    public Long getGroupChartId() {
        return groupChartId;
    }

    public void setGroupChartId(Long groupChartId) {
        this.groupChartId = groupChartId;
    }

    public Long getChartIdNextto() {
        return chartIdNextto;
    }

    public void setChartIdNextto(Long chartIdNextto) {
        this.chartIdNextto = chartIdNextto;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getGroupKpiCode() {
        return groupKpiCode;
    }

    public void setGroupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
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

    public String getChartConfig() {
        return chartConfig;
    }

    public void setChartConfig(String chartConfig) {
        this.chartConfig = chartConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigChartDTO configChartDTO = (ConfigChartDTO) o;
        if (configChartDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configChartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigChartDTO{" +
            "id=" + getId() +
            ", chartCode='" + getChartCode() + "'" +
            ", chartName='" + getChartName() + "'" +
            ", titleChart='" + getTitleChart() + "'" +
            ", typeChart='" + getTypeChart() + "'" +
            ", timeTypeDefault=" + getTimeTypeDefault() +
            ", relativeTime=" + getRelativeTime() +
            ", chartUrl='" + getChartUrl() + "'" +
            ", groupChartId=" + getGroupChartId() +
            ", orderIndex=" + getOrderIndex() +
            ", groupKpiCode='" + getGroupKpiCode() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
