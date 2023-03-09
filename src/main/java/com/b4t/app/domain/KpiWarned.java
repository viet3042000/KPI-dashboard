package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A KpiWarned.
 */
@Entity
@Table(name = "kpi_warned")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KpiWarned implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "prd_id")
    private Long prdId;

    @Column(name = "status")
    private Long status;

    @Column(name = "time_type")
    private Long timeType;

    @Column(name = "warning_type")
    private Long warningType;

    @Column(name = "warning_level")
    private Long warningLevel;

    @Column(name = "warning_content")
    private String warningContent;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "table_name")
    private String tableName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public KpiWarned kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public KpiWarned prdId(Long prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getStatus() {
        return status;
    }

    public KpiWarned status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTimeType() {
        return timeType;
    }

    public KpiWarned timeType(Long timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getWarningType() {
        return warningType;
    }

    public KpiWarned warningType(Long warningType) {
        this.warningType = warningType;
        return this;
    }

    public void setWarningType(Long warningType) {
        this.warningType = warningType;
    }

    public Long getWarningLevel() {
        return warningLevel;
    }

    public KpiWarned warningLevel(Long warningLevel) {
        this.warningLevel = warningLevel;
        return this;
    }

    public void setWarningLevel(Long warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningContent() {
        return warningContent;
    }

    public KpiWarned warningContent(String warningContent) {
        this.warningContent = warningContent;
        return this;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public KpiWarned updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public KpiWarned updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getTableName() {
        return tableName;
    }

    public KpiWarned tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KpiWarned)) {
            return false;
        }
        return id != null && id.equals(((KpiWarned) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KpiWarned{" +
            "id=" + getId() +
            ", kpiId=" + getKpiId() +
            ", prdId=" + getPrdId() +
            ", status=" + getStatus() +
            ", timeType=" + getTimeType() +
            ", warningType=" + getWarningType() +
            ", warningLevel=" + getWarningLevel() +
            ", warningContent='" + getWarningContent() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            ", tableName='" + getTableName() + "'" +
            "}";
    }
}
