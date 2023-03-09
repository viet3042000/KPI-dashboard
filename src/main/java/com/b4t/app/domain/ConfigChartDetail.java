package com.b4t.app.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConfigChartDetail {

    @Id
    private String queryId;
    private String queryData;
    private String defaultValue;
    private String inputCondition;
    private Integer timeTypeDefault;
    private String columnQuery;

    public ConfigChartDetail(String queryId, String queryData, String defaultValue, String inputCondition, Integer timeTypeDefault, String columnQuery) {
        this.queryId = queryId;
        this.queryData = queryData;
        this.defaultValue = defaultValue;
        this.inputCondition = inputCondition;
        this.timeTypeDefault = timeTypeDefault;
        this.columnQuery = columnQuery;
    }

    public ConfigChartDetail() {
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryData() {
        return queryData;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getInputCondition() {
        return inputCondition;
    }

    public void setInputCondition(String inputCondition) {
        this.inputCondition = inputCondition;
    }

    public Integer getTimeTypeDefault() {
        return timeTypeDefault;
    }

    public void setTimeTypeDefault(Integer timeTypeDefault) {
        this.timeTypeDefault = timeTypeDefault;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }
}
