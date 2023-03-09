package com.b4t.app.service.dto;

import com.b4t.app.config.Constants;

import java.io.Serializable;

public class CatKpiReportDTO implements Serializable {
    private String kpiId;
    private String kpiName;
    private String domainCode;
    private String domainName;
    private String parentCode;
    private String parentName;
    private String unitName;
    private String tableName;
    private Integer dPosition;
    private Integer cPosition;

    public Integer getdPosition() {
        return dPosition;
    }

    public void setdPosition(Integer dPosition) {
        this.dPosition = dPosition;
    }

    public Integer getcPosition() {
        return cPosition;
    }

    public void setcPosition(Integer cPosition) {
        this.cPosition = cPosition;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String domainGroup() {
        return domainCode + Constants.SEPARATE_CHARACTER + domainName;
    }

    public String parentGroup() {
        return parentCode + Constants.SEPARATE_CHARACTER + parentName;
    }
}
