package com.b4t.app.service.dto;

import java.util.List;

public class ChartObjDTO {
    private List<String> kpiIds;
    private String timeType;
    private String tableName;
    private String inputLevel;
    private String fromDate;
    private String toDate;
    private String prdId;
    private List<String> xAxes;
    private List<String> yAxes;
    private ChartDetailDTO chartDetailDTO;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<String> getKpiIds() {
        return kpiIds;
    }

    public void setKpiIds(List<String> kpiIds) {
        this.kpiIds = kpiIds;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getxAxes() {
        return xAxes;
    }

    public void setxAxes(List<String> xAxes) {
        this.xAxes = xAxes;
    }

    public List<String> getyAxes() {
        return yAxes;
    }

    public void setyAxes(List<String> yAxes) {
        this.yAxes = yAxes;
    }

    public ChartDetailDTO getChartDetailDTO() {
        return chartDetailDTO;
    }

    public void setChartDetailDTO(ChartDetailDTO chartDetailDTO) {
        this.chartDetailDTO = chartDetailDTO;
    }

    public String getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getPrdId() {
        return prdId;
    }

    public void setPrdId(String prdId) {
        this.prdId = prdId;
    }
}
