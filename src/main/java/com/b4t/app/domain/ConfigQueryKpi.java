package com.b4t.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigQueryKpi.
 */
@Entity
@Table(name = "config_query_kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigQueryKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_type")
    private Integer timeType;

    @Column(name = "input_level")
    private Long inputLevel;

    @Column(name = "query_data")
    private String queryData;

    @Column(name = "query_check_data")
    private String queryCheckData;

    @Column(name = "status")
    private Integer status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "list_parent_input_level")
    private String listParentInputLevel;

    @Column(name = "report_id")
    private Long reportId;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public ConfigQueryKpi timeType(Integer timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public Long getInputLevel() {
        return inputLevel;
    }

    public ConfigQueryKpi inputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
        return this;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getQueryData() {
        return queryData;
    }

    public ConfigQueryKpi queryData(String queryData) {
        this.queryData = queryData;
        return this;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getQueryCheckData() {
        return queryCheckData;
    }

    public ConfigQueryKpi queryCheckData(String queryCheckData) {
        this.queryCheckData = queryCheckData;
        return this;
    }

    public void setQueryCheckData(String queryCheckData) {
        this.queryCheckData = queryCheckData;
    }

    public Integer getStatus() {
        return status;
    }

    public ConfigQueryKpi status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigQueryKpi description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigQueryKpi updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigQueryKpi updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getListParentInputLevel() {
        return listParentInputLevel;
    }

    public ConfigQueryKpi listParentInputLevel(String listParentInputLevel) {
        this.listParentInputLevel = listParentInputLevel;
        return this;
    }

    public void setListParentInputLevel(String listParentInputLevel) {
        this.listParentInputLevel = listParentInputLevel;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigQueryKpi)) {
            return false;
        }
        return id != null && id.equals(((ConfigQueryKpi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigQueryKpi{" +
            "id=" + getId() +
            ", timeType=" + getTimeType() +
            ", inputLevel=" + getInputLevel() +
            ", queryData='" + getQueryData() + "'" +
            ", queryCheckData='" + getQueryCheckData() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            ", listParentInputLevel='" + getListParentInputLevel() + "'" +
            "}";
    }
}
