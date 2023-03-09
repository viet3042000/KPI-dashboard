package com.b4t.app.service.dto;

import java.io.Serializable;

public class ConfigQueryKpiColumn implements Serializable {
    private Integer kpi;
    private String aliasQuery;
    private String dataType;
    private Integer status = 0;
    private String columnInDestinationTable;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getKpi() {
        return kpi;
    }

    public void setKpi(Integer kpi) {
        this.kpi = kpi;
    }

    public String getAliasQuery() {
        return aliasQuery;
    }

    public void setAliasQuery(String aliasQuery) {
        this.aliasQuery = aliasQuery;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnInDestinationTable() {
        return columnInDestinationTable;
    }

    public void setColumnInDestinationTable(String columnInDestinationTable) {
        this.columnInDestinationTable = columnInDestinationTable;
    }
    public String getKeyGroup() {
        return kpi+"_"+columnInDestinationTable;
    }
}
