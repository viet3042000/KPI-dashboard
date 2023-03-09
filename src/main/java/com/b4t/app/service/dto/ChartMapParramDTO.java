package com.b4t.app.service.dto;

public class ChartMapParramDTO {
    private String domainCode;
    private Long timeType;
    private Long kpiId;
    private String tableName;
    private String dataPresent;
    private Long fromDate;
    private Long toDate;

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public Long getTimeType() {
        return timeType;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataPresent() {
        return dataPresent;
    }

    public void setDataPresent(String dataPresent) {
        this.dataPresent = dataPresent;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }
}
