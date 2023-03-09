package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.ConfigChartItem} entity.
 */
public class ConfigChartItemDTO implements Serializable {

    public ConfigChartItemDTO() {
        displayConfigs = new ArrayList<>();
    }

    private Long id;

    private Long chartId;

    private String typeChart;

    private Long hasAvgLine;

    private String listColor;

    private Long orderIndex;

    private Long queryId;

    private ConfigQueryChartDTO query;

    private String condition1;

    private String condition2;

    private String condition3;

    private String condition4;

    private String condition5;

    private String inputCondition;

    private Long status;

    private String description;

    private Instant updateTime;

    private String updateUser;

    List<ConfigDisplayQueryDTO> displayConfigs;

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

    public String getTypeChart() {
        return typeChart;
    }

    public void setTypeChart(String typeChart) {
        this.typeChart = typeChart;
    }

    public Long getHasAvgLine() {
        return hasAvgLine;
    }

    public void setHasAvgLine(Long hasAvgLine) {
        this.hasAvgLine = hasAvgLine;
    }

    public String getListColor() {
        return listColor;
    }

    public void setListColor(String listColor) {
        this.listColor = listColor;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public ConfigQueryChartDTO getQuery() {
        return query;
    }

    public void setQuery(ConfigQueryChartDTO query) {
        this.query = query;
    }

    public String getCondition1() {
        return condition1;
    }

    public void setCondition1(String condition1) {
        this.condition1 = condition1;
    }

    public String getCondition2() {
        return condition2;
    }

    public void setCondition2(String condition2) {
        this.condition2 = condition2;
    }

    public String getCondition3() {
        return condition3;
    }

    public void setCondition3(String condition3) {
        this.condition3 = condition3;
    }

    public String getCondition4() {
        return condition4;
    }

    public void setCondition4(String condition4) {
        this.condition4 = condition4;
    }

    public String getCondition5() {
        return condition5;
    }

    public void setCondition5(String condition5) {
        this.condition5 = condition5;
    }

    public String getInputCondition() {
        return inputCondition;
    }

    public void setInputCondition(String inputCondition) {
        this.inputCondition = inputCondition;
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

    public List<ConfigDisplayQueryDTO> getDisplayConfigs() {
        return displayConfigs;
    }

    public void setDisplayConfigs(List<ConfigDisplayQueryDTO> displayConfigs) {
        this.displayConfigs = displayConfigs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigChartItemDTO configChartItemDTO = (ConfigChartItemDTO) o;
        if (configChartItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configChartItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigChartItemDTO{" +
            "id=" + getId() +
            ", chartId=" + getChartId() +
            ", typeChart='" + getTypeChart() + "'" +
            ", hasAvgLine=" + getHasAvgLine() +
            ", listColor='" + getListColor() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", queryId='" + getQueryId() + "'" +
            ", condition1='" + getCondition1() + "'" +
            ", condition2='" + getCondition2() + "'" +
            ", condition3='" + getCondition3() + "'" +
            ", condition4='" + getCondition4() + "'" +
            ", condition5='" + getCondition5() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
