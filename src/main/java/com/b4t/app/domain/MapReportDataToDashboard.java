package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A MapReportDataToDashboard.
 */
@Entity
@Table(name = "map_report_data_to_dashboard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MapReportDataToDashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "row_id")
    private Long rowId;

    @Column(name = "row_name")
    private String rowName;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "column_id")
    private Long columnId;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "rp_input_grant_id")
    private Long rpInputGrantId;

    @Column(name = "time_type")
    private Long timeType;

    @Column(name = "prd_id")
    private Long prdId;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "kpi_name")
    private String kpiName;

    @Column(name = "kpi_code")
    private String kpiCode;

    @Column(name = "domain_code")
    private String domainCode;

    @Column(name = "object_code")
    private String objectCode;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public MapReportDataToDashboard reportId(Long reportId) {
        this.reportId = reportId;
        return this;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getRowId() {
        return rowId;
    }

    public MapReportDataToDashboard rowId(Long rowId) {
        this.rowId = rowId;
        return this;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getRowName() {
        return rowName;
    }

    public MapReportDataToDashboard rowName(String rowName) {
        this.rowName = rowName;
        return this;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public MapReportDataToDashboard orgId(Long orgId) {
        this.orgId = orgId;
        return this;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public MapReportDataToDashboard columnId(Long columnId) {
        this.columnId = columnId;
        return this;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public MapReportDataToDashboard columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getRpInputGrantId() {
        return rpInputGrantId;
    }

    public MapReportDataToDashboard rpInputGrantId(Long rpInputGrantId) {
        this.rpInputGrantId = rpInputGrantId;
        return this;
    }

    public void setRpInputGrantId(Long rpInputGrantId) {
        this.rpInputGrantId = rpInputGrantId;
    }

    public Long getTimeType() {
        return timeType;
    }

    public MapReportDataToDashboard timeType(Long timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getPrdId() {
        return prdId;
    }

    public MapReportDataToDashboard prdId(Long prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public MapReportDataToDashboard kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public MapReportDataToDashboard kpiName(String kpiName) {
        this.kpiName = kpiName;
        return this;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public MapReportDataToDashboard kpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
        return this;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public MapReportDataToDashboard domainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public MapReportDataToDashboard objectCode(String objectCode) {
        this.objectCode = objectCode;
        return this;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getValue() {
        return value;
    }

    public MapReportDataToDashboard value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public MapReportDataToDashboard description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public MapReportDataToDashboard updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public MapReportDataToDashboard updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapReportDataToDashboard)) {
            return false;
        }
        return id != null && id.equals(((MapReportDataToDashboard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapReportDataToDashboard{" +
            "id=" + getId() +
            ", reportId=" + getReportId() +
            ", rowId=" + getRowId() +
            ", rowName='" + getRowName() + "'" +
            ", orgId=" + getOrgId() +
            ", columnId=" + getColumnId() +
            ", columnName='" + getColumnName() + "'" +
            ", rpInputGrantId=" + getRpInputGrantId() +
            ", timeType=" + getTimeType() +
            ", prdId=" + getPrdId() +
            ", kpiId=" + getKpiId() +
            ", kpiName='" + getKpiName() + "'" +
            ", kpiCode='" + getKpiCode() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            ", objectCode='" + getObjectCode() + "'" +
            ", value='" + getValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
