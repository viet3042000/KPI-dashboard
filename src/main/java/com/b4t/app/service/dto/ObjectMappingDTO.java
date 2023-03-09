package com.b4t.app.service.dto;

import java.io.Serializable;

public class ObjectMappingDTO implements Serializable {
   String colName;
   String table_name_dashboard;
   String kpiDashboardId;
   String objectDashboardId;
   String prdId;
   String timeType;
   String cellName;

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getTable_name_dashboard() {
        return table_name_dashboard;
    }

    public void setTable_name_dashboard(String table_name_dashboard) {
        this.table_name_dashboard = table_name_dashboard;
    }

    public String getKpiDashboardId() {
        return kpiDashboardId;
    }

    public void setKpiDashboardId(String kpiDashboardId) {
        this.kpiDashboardId = kpiDashboardId;
    }

    public String getObjectDashboardId() {
        return objectDashboardId;
    }

    public void setObjectDashboardId(String objectDashboardId) {
        this.objectDashboardId = objectDashboardId;
    }

    public String getPrdId() {
        return prdId;
    }

    public void setPrdId(String prdId) {
        this.prdId = prdId;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
}
