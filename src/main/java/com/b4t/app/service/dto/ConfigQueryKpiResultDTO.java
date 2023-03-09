package com.b4t.app.service.dto;

import java.math.BigInteger;
import java.time.Instant;

/**
 * @author tamdx
 */
public class ConfigQueryKpiResultDTO {
    private Long queryKpiId;
    private String timeTypeName;
    private String inputLevelName;
    private String queryData;
    private String queryCheckData;
    private String listParentInputLevel;
    private String description;
    private Instant updateTime;
    private String updateUser;
    private String tableDestination;
    private String listTableSource;

    public Long getQueryKpiId() {
        return queryKpiId;
    }

    public String getListTableSource() {
        return listTableSource;
    }

    public void setListTableSource(String listTableSource) {
        this.listTableSource = listTableSource;
    }

    public void setQueryKpiId(Long queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public String getTimeTypeName() {
        return timeTypeName;
    }

    public void setTimeTypeName(String timeTypeName) {
        this.timeTypeName = timeTypeName;
    }

    public String getInputLevelName() {
        return inputLevelName;
    }

    public void setInputLevelName(String inputLevelName) {
        this.inputLevelName = inputLevelName;
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

    public String getListParentInputLevel() {
        return listParentInputLevel;
    }

    public void setListParentInputLevel(String listParentInputLevel) {
        this.listParentInputLevel = listParentInputLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getTableDestination() {
        return tableDestination;
    }

    public void setTableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
    }
}
