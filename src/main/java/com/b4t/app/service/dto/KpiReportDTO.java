package com.b4t.app.service.dto;

import java.util.Date;
import java.util.List;

public class KpiReportDTO {
    private List<String> lstKpi;
    private List<String> lstObj;
    private Integer timeType;
    private List<String> inputLevel;
    private Date fromDate;
    private Date toDate;
    private Double fromValue;
    private Double toValue;

    public List<String> getLstKpi() {
        return lstKpi;
    }

    public void setLstKpi(List<String> lstKpi) {
        this.lstKpi = lstKpi;
    }

    public List<String> getLstObj() {
        return lstObj;
    }

    public void setLstObj(List<String> lstObj) {
        this.lstObj = lstObj;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public List<String> getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(List<String> inputLevel) {
        this.inputLevel = inputLevel;
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
}
