package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.FlagRunQueryKpi} entity.
 */
public class FlagRunQueryKpiDTO implements Serializable {

    private Long id;

    private String tableName;

    private Long kpiId;

    private Long prdId;

    private Long status;

    private Instant updateTime;

    public FlagRunQueryKpiDTO() {
    }

    public FlagRunQueryKpiDTO(String tableName, Long kpiId, Long prdId) {
        this.tableName = tableName;
        this.kpiId = kpiId;
        this.prdId = prdId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FlagRunQueryKpiDTO flagRunQueryKpiDTO = (FlagRunQueryKpiDTO) o;
        if (flagRunQueryKpiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), flagRunQueryKpiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FlagRunQueryKpiDTO{" +
            "id=" + getId() +
            ", tableName='" + getTableName() + "'" +
            ", kpiId=" + getKpiId() +
            ", prdId=" + getPrdId() +
            ", status=" + getStatus() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
