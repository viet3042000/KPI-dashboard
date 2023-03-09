package com.b4t.app.service.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportKpiDTO implements Cloneable{
    private List<String> conditionData;
    private Map<String, Object> mapTable; // Key ten bang, value: listKpi
    private List<TreeValue> kpiIds;
    private String timeType;
    private Date fromDate;
    private Date toDate;
    private List<String> inputLevels;
    private String valueType; //So lieu bieu dien
    private String outputSearch; //Dang ket qua hien thi (TABLE, LINE, COLUMN, PIE, AREA, GROUP_BAR, STACK, CORRELATE_CHART)
    private List<String> objects; //Danh sach doi tuong
    private Double fromValue; //Tu gia tri
    private Double toValue; //Den gia tri
    private Boolean haveKpiTag = false;

    public Map<String, Object> getMapTable() {
        return mapTable;
    }

    public void setMapTable(Map<String, Object> mapTable) {
        this.mapTable = mapTable;
    }

    public Boolean getHaveKpiTag() {
        return haveKpiTag;
    }

    public void setHaveKpiTag(Boolean haveKpiTag) {
        this.haveKpiTag = haveKpiTag;
    }

    public List<String> getConditionData() {
        return conditionData;
    }

    public void setConditionData(List<String> conditionData) {
        this.conditionData = conditionData;
    }

    public List<TreeValue> getKpiIds() {
        return kpiIds;
    }

    public void setKpiIds(List<TreeValue> kpiIds) {
        this.kpiIds = kpiIds;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getOutputSearch() {
        return outputSearch;
    }

    public void setOutputSearch(String outputSearch) {
        this.outputSearch = outputSearch;
    }

    public List<String> getObjects() {
        return objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
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

    public List<String> getInputLevels() {
        return inputLevels;
    }

    public void setInputLevels(List<String> inputLevels) {
        this.inputLevels = inputLevels;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
