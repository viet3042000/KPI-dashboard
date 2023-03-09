package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.b4t.app.domain.KpiWarned} entity.
 */
public class KpiWarnedDTO implements Serializable {
    
    private Long id;

    private Long kpiId;

    private Long prdId;

    private Long status;

    private Long timeType;

    private Long warningType;

    private Long warningLevel;

    private String warningContent;

    private Instant updateTime;

    private String updateUser;

    private String tableName;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTimeType() {
        return timeType;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getWarningType() {
        return warningType;
    }

    public void setWarningType(Long warningType) {
        this.warningType = warningType;
    }

    public Long getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(Long warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningContent() {
        return warningContent;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KpiWarnedDTO)) {
            return false;
        }

        return id != null && id.equals(((KpiWarnedDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KpiWarnedDTO{" +
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
