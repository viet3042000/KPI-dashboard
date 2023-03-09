package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class BaseRptGraphChartDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    private String chartType;
    private List<String> xAxis;
    private List<BaseRptSeries> series;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }

    public List<BaseRptSeries> getSeries() {
        return series;
    }

    public void setSeries(List<BaseRptSeries> series) {
        this.series = series;
    }
}
