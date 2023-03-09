package com.b4t.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ConfigInputKpiQuery.
 */
@Entity
@Table(name = "config_input_kpi_query")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigInputKpiQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_kpi_id")
    private Integer queryKpiId;

    @Column(name = "kpi_id")
    private String kpiId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "update_time")
    private Instant updateTime;

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

    public ConfigInputKpiQuery queryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
        return this;
    }

    public void setQueryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public String getKpiId() {
        return kpiId;
    }

    public ConfigInputKpiQuery kpiId(String kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public Integer getStatus() {
        return status;
    }

    public ConfigInputKpiQuery status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public ConfigInputKpiQuery updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigInputKpiQuery)) {
            return false;
        }
        return id != null && id.equals(((ConfigInputKpiQuery) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigInputKpiQuery{" +
            "id=" + getId() +
            ", queryKpiId=" + getQueryKpiId() +
            ", kpiId='" + getKpiId() + "'" +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
