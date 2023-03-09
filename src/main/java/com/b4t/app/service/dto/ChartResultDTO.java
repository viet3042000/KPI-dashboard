package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartResultDTO extends ConfigChartDTO implements Serializable {

    public ChartResultDTO() {
        details = new ArrayList<>();
    }

    public ChartResultDTO(ConfigChartDTO config) {
        this.setId(config.getId());
        this.setChartCode(config.getChartCode());
        this.setChartName(config.getChartName());
        this.setChartUrl(config.getChartUrl());
        this.setDescription(config.getDescription());
        this.setDomainCode(config.getDomainCode());
        this.setGroupChartId(config.getGroupChartId());
        this.setGroupKpiCode(config.getGroupKpiCode());
        this.setOrderIndex(config.getOrderIndex());
        this.setRelativeTime(config.getRelativeTime());
        this.setChartIdNextto(config.getChartIdNextto());
        this.setStatus(config.getStatus());
        this.setTimeTypeDefault(config.getTimeTypeDefault());
        this.setTitleChart(config.getTitleChart());
        this.setTypeChart(config.getTypeChart());
        this.setUpdateTime(config.getUpdateTime());
        this.setUpdateUser(config.getUpdateUser());
        this.setChartConfig(config.getChartConfig());
        this.setScreenDetailId(config.getScreenDetailId());
    }

    private Map<String, Object> filterParams;

    private List<ChartDetailDTO> details;

    public Map<String, Object> getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(Map<String, Object> filterParams) {
        this.filterParams = filterParams;
    }

    public List<ChartDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<ChartDetailDTO> details) {
        this.details = details;
    }
}
