package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A RpReport.
 */
@Entity
@Table(name = "rp_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RpReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "report_code")
    private String reportCode;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "prd_id")
    private Integer prdId;

    @Column(name = "time_type")
    private Integer timeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public RpReport reportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportCode() {
        return reportCode;
    }

    public RpReport reportCode(String reportCode) {
        this.reportCode = reportCode;
        return this;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public RpReport updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPrdId() {
        return prdId;
    }

    public RpReport prdId(Integer prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public RpReport timeType(Integer timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RpReport)) {
            return false;
        }
        return id != null && id.equals(((RpReport) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RpReport{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportCode='" + getReportCode() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", prdId=" + getPrdId() +
            ", timeType=" + getTimeType() +
            "}";
    }
}
