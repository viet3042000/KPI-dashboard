package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A FlagRunQueryKpi.
 */
@Entity
@Table(name = "flag_run_query_kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FlagRunQueryKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "prd_id")
    private Long prdId;

    @Column(name = "status")
    private Long status;

    @Column(name = "update_time")
    private Instant updateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public FlagRunQueryKpi tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public FlagRunQueryKpi kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public FlagRunQueryKpi prdId(Long prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getStatus() {
        return status;
    }

    public FlagRunQueryKpi status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public FlagRunQueryKpi updateTime(Instant updateTime) {
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
        if (!(o instanceof FlagRunQueryKpi)) {
            return false;
        }
        return id != null && id.equals(((FlagRunQueryKpi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FlagRunQueryKpi{" +
            "id=" + getId() +
            ", tableName='" + getTableName() + "'" +
            ", kpiId=" + getKpiId() +
            ", prdId=" + getPrdId() +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
