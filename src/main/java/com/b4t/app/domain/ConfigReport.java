package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigReport.
 */
@Entity
@Table(name = "config_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "time_type", nullable = false)
    private String timeType;

    @NotNull
    @Column(name = "form_input", nullable = false)
    private String formInput;

    @NotNull
    @Column(name = "domain_code", nullable = false)
    private String domainCode;

    @NotNull
    @Column(name = "database_name", nullable = false)
    private String databaseName;

    @NotNull
    @Column(name = "table_name", nullable = false)
    private String tableName;

    @NotNull
    @Column(name = "input_level", nullable = false)
    private String inputLevel;

    @Column(name = "unit")
    private String unit;

    @Column(name = "alert_time")
    private Integer alertDay;

    @Column(name = "alert")
    private Boolean alert;

    @Column(name = "row_permission")
    private String rowPermission;

    @Column(name = "lock_row")
    private String lockRow;

    @Column(name = "status")
    private Integer status;

    @Column(name = "main_data")
    private Integer mainData;

    @Column(name = "approve_yn")
    private Integer approveRequest;

    @Column(name = "description")
    private String description;

    @Column(name = "creator")
    private String creator;

    @Column(name = "update_time")
    private Instant updateTime;

    public Integer getApproveRequest() {
        return approveRequest;
    }

    public void setApproveRequest(Integer approveRequest) {
        this.approveRequest = approveRequest;
    }

    public Integer getAlertDay() {
        return alertDay;
    }

    public void setAlertDay(Integer alertDay) {
        this.alertDay = alertDay;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    public Integer getMainData() {
        return mainData;
    }

    public void setMainData(Integer mainData) {
        this.mainData = mainData;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ConfigReport title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeType() {
        return timeType;
    }

    public ConfigReport timeType(String timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getFormInput() {
        return formInput;
    }

    public ConfigReport formInput(String formInput) {
        this.formInput = formInput;
        return this;
    }

    public void setFormInput(String formInput) {
        this.formInput = formInput;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public ConfigReport domainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public ConfigReport databaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public ConfigReport tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getInputLevel() {
        return inputLevel;
    }

    public ConfigReport inputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
        return this;
    }

    public void setInputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getUnit() {
        return unit;
    }

    public ConfigReport unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStatus() {
        return status;
    }

    public ConfigReport status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigReport description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public ConfigReport creator(String creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigReport updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public ConfigReport rowPermission(String rowPermission) {
        this.rowPermission = rowPermission;
        return this;
    }

    public String getRowPermission() {
        return rowPermission;
    }

    public void setRowPermission(String rowPermission) {
        this.rowPermission = rowPermission;
    }

    public ConfigReport lockRow(String lockRow) {
        this.lockRow = lockRow;
        return this;
    }

    public String getLockRow() {
        return lockRow;
    }

    public void setLockRow(String lockRow) {
        this.lockRow = lockRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigReport)) {
            return false;
        }
        return id != null && id.equals(((ConfigReport) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigReport{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", timeType='" + getTimeType() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            ", databaseName='" + getDatabaseName() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", inputLevel='" + getInputLevel() + "'" +
            ", unit='" + getUnit() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", creator='" + getCreator() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
