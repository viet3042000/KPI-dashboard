package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChartDetailDTO implements Serializable {
    public ChartDetailDTO() {
        data = new ArrayList<>();
        displayConfigs = new ArrayList<>();
        kpiInfos = new ArrayList<>();
    }

    public ChartDetailDTO(
        String chartType, Long orderIndex, List<Object> data, List<ConfigDisplayQueryDTO> displayConfigs,
        ConfigQueryChartDTO query, CatGraphKpiDTO kpiInfo, List<CatGraphKpiDTO> kpiInfos) {
        this.chartType = chartType;
        this.orderIndex = orderIndex;
        this.data = data;
        this.displayConfigs = displayConfigs;
        this.query = query;
        this.kpiInfo = kpiInfo;
        this.kpiInfos = kpiInfos;
    }

    private String chartType;

    private Long orderIndex;

    private ConfigQueryChartDTO query;

    private CatGraphKpiDTO kpiInfo;

    private List<CatGraphKpiDTO> kpiInfos;

    private List<Object> data;

    private List<ConfigDisplayQueryDTO> displayConfigs;

    public ConfigQueryChartDTO getQuery() {
        return query;
    }

    public void setQuery(ConfigQueryChartDTO query) {
        this.query = query;
    }

    public CatGraphKpiDTO getKpiInfo() {
        return kpiInfo;
    }

    public void setKpiInfo(CatGraphKpiDTO kpiInfo) {
        this.kpiInfo = kpiInfo;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public List<ConfigDisplayQueryDTO> getDisplayConfigs() {
        return displayConfigs;
    }

    public void setDisplayConfigs(List<ConfigDisplayQueryDTO> displayConfigs) {
        this.displayConfigs = displayConfigs;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public List<CatGraphKpiDTO> getKpiInfos() {
        return kpiInfos;
    }

    public void setKpiInfos(List<CatGraphKpiDTO> kpiInfos) {
        this.kpiInfos = kpiInfos;
    }
}
