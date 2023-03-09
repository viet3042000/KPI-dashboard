package com.b4t.app.service.dto;

import java.io.Serializable;

public class ReportAlertDetailDTO implements Serializable {
    private Integer id;
    private String schema;
    private String tableName;
    private String tableTitle;
    private Integer timeType;
    private String domainCode;
    private String domainName;
    private String tableId;
    private Float alertTime;
    private Integer UserId;
    private String userName;
    private String firstName;
    private String lastName;
    private Integer status;
    private String modifiedTime;
    private String modifiedBy;
    private String createdTime;
    private String createBy;
    private String email;

    public ReportAlertDetailDTO() {
    }

    public ReportAlertDetailDTO(Integer id, String schema, String tableName, String tableTitle, Integer timeType, String domainCode, String domainName, String tableId, Float alertTime, Integer userId, String userName, String firstName, String lastName, Integer status, String modifiedTime, String modifiedBy, String createdTime, String createBy, String email) {
        this.id = id;
        this.schema = schema;
        this.tableName = tableName;
        this.tableTitle = tableTitle;
        this.timeType = timeType;
        this.domainCode = domainCode;
        this.domainName = domainName;
        this.tableId = tableId;
        this.alertTime = alertTime;
        UserId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.modifiedTime = modifiedTime;
        this.modifiedBy = modifiedBy;
        this.createdTime = createdTime;
        this.createBy = createBy;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Float getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Float alertTime) {
        this.alertTime = alertTime;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
