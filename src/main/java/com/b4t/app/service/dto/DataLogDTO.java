package com.b4t.app.service.dto;

import com.b4t.app.config.Constants;
import com.b4t.app.domain.DataLog;
import com.b4t.app.domain.User;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

//import com.b4t.app.domain.Authority;

/**
 * A DTO representing a user, with his authorities.
 */
public class DataLogDTO {

    private Long id;

    private String tableName;

    private Long recordId;

    private int monkey;

    private int quarkey;

    private int yearkey;

    private String field;

    private String fieldName;

    private String oldValue;

    private String newValue;

    private int actionType;

    private String modifiedBy;

    private Date modifiedTime;

    private String schemaName;

    private String updateTime;

    public DataLogDTO() {
        // Empty constructor needed for Jackson.
    }

    public DataLogDTO(DataLog dataLog) {
        this.id = id;
        this.tableName = tableName;
        this.recordId = recordId;
        this.monkey = monkey;
        this.quarkey = quarkey;
        this.yearkey = yearkey;
        this.field = field;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.actionType = actionType;
        this.modifiedBy = modifiedBy;
        this.modifiedTime = modifiedTime;
        this.schemaName = schemaName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public int getMonkey() {
        return monkey;
    }

    public void setMonkey(int monkey) {
        this.monkey = monkey;
    }

    public int getQuarkey() {
        return quarkey;
    }

    public void setQuarkey(int quarkey) {
        this.quarkey = quarkey;
    }

    public int getYearkey() {
        return yearkey;
    }

    public void setYearkey(int yearkey) {
        this.yearkey = yearkey;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public String toString() {
        return "DataLogDTO{" +
            "tableName='" + tableName + '\'' +
            ", recordId='" + recordId + '\'' +
            ", monkey='" + monkey + '\'' +
            ", quarkey='" + quarkey + '\'' +
            ", yearkey='" + yearkey + '\'' +
            ", field=" + field +
            ", fieldName=" + fieldName +
            ", oldValue='" + oldValue + '\'' +
            ", newValue=" + newValue +
            ", actionType=" + actionType +
            ", modifiedBy='" + modifiedBy + '\'' +
            ", modifiedTime=" + modifiedTime +
            ", schemaName=" + schemaName +
            "}";
    }
}
