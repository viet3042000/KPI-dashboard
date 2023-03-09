package com.b4t.app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CatKpiReport {
    @Id
    @Column(name = "kpi_id")
    private String kpiId;
    @Column(name = "kpi_name")
    private String kpiName;
    @Column(name = "domain_code")
    private String domainCode;
    @Column(name = "domain_name")
    private String domainName;
    @Column(name = "parent_code")
    private String parentCode;
    @Column(name = "parent_name")
    private String parentName;
    @Column(name = "unit_name")
    private String unitName;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "d_position")
    private Integer dPosition;
    @Column(name = "c_position")
    private Integer cPosition;
    @Column(name = "kpi_name_org")
    private String kpiNameOrg;

    public String getKpiNameOrg() {
        return kpiNameOrg;
    }

    public void setKpiNameOrg(String kpiNameOrg) {
        this.kpiNameOrg = kpiNameOrg;
    }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
