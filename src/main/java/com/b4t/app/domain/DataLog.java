package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * A user.
 */
@Entity
@Table(name = "data_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "monkey")
    private int monkey;

    @Column(name = "quarkey")
    private int quarkey;

    @Column(name = "yearkey")
    private int yearkey;

    @Column(name = "field")
    private String field;

    @Column(name = "field_name")
    private Long fieldName;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "action_type")
    private int actionType;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_time")
    private Date modifiedTime;

    @Column(name = "schema_name")
    private String schemaName;

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

    public Long getFieldName() {
        return fieldName;
    }

    public void setFieldName(Long fieldName) {
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

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataLog)) {
            return false;
        }
        return id != null && id.equals(((DataLog) o).id);
    }

    @Override
    public String toString() {
        return "";
    }
}
