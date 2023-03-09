package com.b4t.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A MonitorQueryKpi.
 */
@Entity
@Table(name = "monitor_query_kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MonitorQueryKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_kpi_id")
    private Integer queryKpiId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "run_time_succ")
    private Instant runTimeSucc;

    @Column(name = "run_time_report")
    private Instant runTimeReport;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "priority")
    private Integer priority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRunTimeReport() {
        return runTimeReport;
    }

    public void setRunTimeReport(Instant runTimeReport) {
        this.runTimeReport = runTimeReport;
    }

    public Integer getQueryKpiId() {
        return queryKpiId;
    }

    public MonitorQueryKpi queryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
        return this;
    }

    public void setQueryKpiId(Integer queryKpiId) {
        this.queryKpiId = queryKpiId;
    }

    public Integer getStatus() {
        return status;
    }

    public MonitorQueryKpi status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getRunTimeSucc() {
        return runTimeSucc;
    }

    public MonitorQueryKpi runTimeSucc(Instant runTimeSucc) {
        this.runTimeSucc = runTimeSucc;
        return this;
    }

    public void setRunTimeSucc(Instant runTimeSucc) {
        this.runTimeSucc = runTimeSucc;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public MonitorQueryKpi updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitorQueryKpi)) {
            return false;
        }
        return id != null && id.equals(((MonitorQueryKpi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MonitorQueryKpi{" +
            "id=" + getId() +
            ", queryKpiId=" + getQueryKpiId() +
            ", status=" + getStatus() +
            ", runTimeSucc='" + getRunTimeSucc() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
