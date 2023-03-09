package com.b4t.app.service.dto;

public class ConfigColumnQueryKpiDetail {
    private Integer kpiId;
    private String columnQuery;
    private String columnDestination;
    private String dataType;
    private Long mapKpiQueryId;
    private Long id;

    public Integer getKpiId() {
        return kpiId;
    }

    public void setKpiId(Integer kpiId) {
        this.kpiId = kpiId;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }

    public String getColumnDestination() {
        return columnDestination;
    }

    public void setColumnDestination(String columnDestination) {
        this.columnDestination = columnDestination;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getMapKpiQueryId() {
        return mapKpiQueryId;
    }

    public void setMapKpiQueryId(Long mapKpiQueryId) {
        this.mapKpiQueryId = mapKpiQueryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String groupKey(){
        return this.kpiId+"_" + this.getColumnDestination();
    }
}
