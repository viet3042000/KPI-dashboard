package com.b4t.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigColumnQueryKpi.
 */
@Entity
@Table(name = "config_column_query_kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigColumnQueryKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "map_kpi_query_id")
    private Long mapKpiQueryId;

    @Column(name = "column_query")
    private String columnQuery;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "column_destination")
    private String columnDestination;

    @Column(name = "status")
    private Integer status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    public ConfigColumnQueryKpi() {

    }

    public ConfigColumnQueryKpi(
        Long mapKpiQueryId, String columnQuery, String dataType, String columnDestination,
                                Integer status, Instant updateTime, String updateUser) {
        this.setMapKpiQueryId(mapKpiQueryId);
        this.setColumnQuery(columnQuery);
        this.setDataType(dataType);
        this.setColumnDestination(columnDestination);
        this.setStatus(status);
        this.setUpdateTime(updateTime);
        this.setUpdateUser(updateUser);
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMapKpiQueryId() {
        return mapKpiQueryId;
    }

    public ConfigColumnQueryKpi mapKpiQueryId(Long mapKpiQueryId) {
        this.mapKpiQueryId = mapKpiQueryId;
        return this;
    }

    public void setMapKpiQueryId(Long mapKpiQueryId) {
        this.mapKpiQueryId = mapKpiQueryId;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public ConfigColumnQueryKpi columnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
        return this;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }

    public String getDataType() {
        return dataType;
    }

    public ConfigColumnQueryKpi dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnDestination() {
        return columnDestination;
    }

    public ConfigColumnQueryKpi columnDestination(String columnDestination) {
        this.columnDestination = columnDestination;
        return this;
    }

    public void setColumnDestination(String columnDestination) {
        this.columnDestination = columnDestination;
    }

    public Integer getStatus() {
        return status;
    }

    public ConfigColumnQueryKpi status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigColumnQueryKpi description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigColumnQueryKpi updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigColumnQueryKpi updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigColumnQueryKpi)) {
            return false;
        }
        return id != null && id.equals(((ConfigColumnQueryKpi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigColumnQueryKpi{" +
            "id=" + getId() +
            ", mapKpiQueryId=" + getMapKpiQueryId() +
            ", columnQuery='" + getColumnQuery() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", columnDestination='" + getColumnDestination() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
