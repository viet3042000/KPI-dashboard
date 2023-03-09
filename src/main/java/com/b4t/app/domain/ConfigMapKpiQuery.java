package com.b4t.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigMapKpiQuery.
 */
@Entity
@Table(name = "config_map_kpi_query")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigMapKpiQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_kpi_id")
    private Integer queryKpiId;

    @Column(name = "kpi_id")
    private Integer kpiId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "table_destination")
    private String tableDestination;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQueryKpiId() {
        return queryKpiId;
    }

    public ConfigMapKpiQuery queryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
        return this;
    }

    public void setQueryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public Integer getKpiId() {
        return kpiId;
    }

    public ConfigMapKpiQuery kpiId(Integer kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Integer kpiId) {
        this.kpiId = kpiId;
    }

    public Integer getStatus() {
        return status;
    }

    public ConfigMapKpiQuery status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public ConfigMapKpiQuery description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigMapKpiQuery updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ConfigMapKpiQuery updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getTableDestination() {
        return tableDestination;
    }

    public ConfigMapKpiQuery tableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
        return this;
    }

    public void setTableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigMapKpiQuery)) {
            return false;
        }
        return id != null && id.equals(((ConfigMapKpiQuery) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigMapKpiQuery{" +
            "id=" + getId() +
            ", queryKpiId=" + getQueryKpiId() +
            ", kpiId=" + getKpiId() +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            ", tableDestination='" + getTableDestination() + "'" +
            "}";
    }
}
