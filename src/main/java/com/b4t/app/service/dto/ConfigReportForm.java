package com.b4t.app.service.dto;

import java.util.List;

public class ConfigReportForm {
    private List<String> lstDomainCode;
    private String timeType;
    private Integer status;
    private String title;
    private String databaseName;
    private String tableName;
    private String unit;
    private Long id;

    public ConfigReportForm() {
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getLstDomainCode() {
        return lstDomainCode;
    }

    public void setLstDomainCode(List<String> lstDomainCode) {
        this.lstDomainCode = lstDomainCode;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
