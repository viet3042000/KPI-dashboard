package com.b4t.app.service.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportKpiSearchDTO {
    private List<String> conditionData; //Danh sach dieu kien loc
    private Map<String, Object> mapTable; // Key ten bang, value: listKpi
    private List<String> inputLevels;
    private String timeType;
    private Date fromDate;
    private Date toDate;

    private Double fromValue; //Tu gia tri
    private Double toValue; //Den gia tri

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Double getFromValue() {
        return fromValue;
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public List<String> getInputLevels() {
        return inputLevels;
    }

    public void setInputLevels(List<String> inputLevels) {
        this.inputLevels = inputLevels;
    }

    public Map<String, Object> getMapTable() {
        return mapTable;
    }

    public void setMapTable(Map<String, Object> mapTable) {
        this.mapTable = mapTable;
    }

    public List<String> getConditionData() {
        return conditionData;
    }

    public void setConditionData(List<String> conditionData) {
        this.conditionData = conditionData;
    }
}
