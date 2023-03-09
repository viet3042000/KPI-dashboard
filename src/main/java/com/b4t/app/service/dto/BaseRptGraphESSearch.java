package com.b4t.app.service.dto;

public class BaseRptGraphESSearch {
    private String tableDestination;
    private Integer fromPrdId;
    private Integer toPrdId;
    private Integer kpiId;

    public Integer getKpiId() {
        return kpiId;
    }

    public void setKpiId(Integer kpiId) {
        this.kpiId = kpiId;
    }

    public Integer getFromPrdId() {
        return fromPrdId;
    }

    public void setFromPrdId(Integer fromPrdId) {
        this.fromPrdId = fromPrdId;
    }

    public Integer getToPrdId() {
        return toPrdId;
    }

    public void setToPrdId(Integer toPrdId) {
        this.toPrdId = toPrdId;
    }

    public String getTableDestination() {
        return tableDestination;
    }

    public void setTableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
    }
}
