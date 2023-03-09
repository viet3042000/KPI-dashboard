package com.b4t.app.service.dto;

import java.util.List;

public class ConfigQueryKpiForm {
    private Long id;
    private String timeType;
    private String inputLevel;
    private List<String> listParentInputLevel;
    private boolean status;
    private String description;
    private List<String> tableSource;
    private List<Integer> inputKpi;
    private String tableDestination;
    private String queryData;
    private String queryCheckData;
    private Long reportId;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
    }

    public List<String> getListParentInputLevel() {
        return listParentInputLevel;
    }

    public void setListParentInputLevel(List<String> listParentInputLevel) {
        this.listParentInputLevel = listParentInputLevel;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTableSource() {
        return tableSource;
    }

    public void setTableSource(List<String> tableSource) {
        this.tableSource = tableSource;
    }

    public List<Integer> getInputKpi() {
        return inputKpi;
    }

    public void setInputKpi(List<Integer> inputKpi) {
        this.inputKpi = inputKpi;
    }

    public String getTableDestination() {
        return tableDestination;
    }

    public void setTableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
    }

    public String getQueryData() {
        return queryData;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getQueryCheckData() {
        return queryCheckData;
    }

    public void setQueryCheckData(String queryCheckData) {
        this.queryCheckData = queryCheckData;
    }
}
