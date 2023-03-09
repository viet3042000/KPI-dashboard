package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.MapReportDataToDashboard} entity.
 */
public class MapReportDataToDashboardDTO implements Serializable {

    private Long id;

    private Long reportId;

    private Long rowId;

    private String rowName;

    private Long orgId;

    private Long columnId;

    private String columnName;

    private Long rpInputGrantId;

    private Long timeType;

    private Long prdId;

    private Long kpiId;

    private String kpiName;

    private String kpiCode;

    private String domainCode;

    private String objectCode;

    private String value;

    private String description;

    private Instant updateTime;

    private String updateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getRpInputGrantId() {
        return rpInputGrantId;
    }

    public void setRpInputGrantId(Long rpInputGrantId) {
        this.rpInputGrantId = rpInputGrantId;
    }

    public Long getTimeType() {
        return timeType;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapReportDataToDashboardDTO)) {
            return false;
        }
        return reportId.equals(((MapReportDataToDashboardDTO) o).reportId)
            && rpInputGrantId.equals(((MapReportDataToDashboardDTO) o).rpInputGrantId)
            && value.equals(((MapReportDataToDashboardDTO) o).value)
            && rowId.equals(((MapReportDataToDashboardDTO) o).rowId)
            && columnId.equals(((MapReportDataToDashboardDTO) o).columnId)
            && prdId.equals(((MapReportDataToDashboardDTO) o).prdId);
    }

    public boolean isUpdateValue(Object o) {
        return reportId.equals(((MapReportDataToDashboardDTO) o).reportId)
            && rpInputGrantId.equals(((MapReportDataToDashboardDTO) o).rpInputGrantId)
            && rowId.equals(((MapReportDataToDashboardDTO) o).rowId)
            && !value.equals(((MapReportDataToDashboardDTO) o).value)
            && columnId.equals(((MapReportDataToDashboardDTO) o).columnId)
            && prdId.equals(((MapReportDataToDashboardDTO) o).prdId);
    }

    public boolean isNewValue(Object o) {
        return (reportId.equals(((MapReportDataToDashboardDTO) o).reportId)
            && rpInputGrantId.equals(((MapReportDataToDashboardDTO) o).rpInputGrantId))
            && (!rowId.equals(((MapReportDataToDashboardDTO) o).rowId)
            || !value.equals(((MapReportDataToDashboardDTO) o).value)
            || !columnId.equals(((MapReportDataToDashboardDTO) o).columnId)
            || !prdId.equals(((MapReportDataToDashboardDTO) o).prdId));
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapReportDataToDashboardDTO{" +
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
