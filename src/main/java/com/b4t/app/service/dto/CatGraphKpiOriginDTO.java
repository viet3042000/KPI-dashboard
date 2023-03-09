package com.b4t.app.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * A DTO for the {@link com.b4t.app.domain.CatGraphKpiOrigin} entity.
 */
public class CatGraphKpiOriginDTO implements Serializable {

    private Long id;
    private String kpiOriginCode;
    private String kpiOriginName;
    private String unitKpi;
    private String domainCode;
    private Integer timeType;
    private String source;
    private String description;
    private Integer status;
    private String createdBy;
    private Instant createdTime;
    private String modifiedBy;
    private Instant modifiedTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKpiOriginCode() {
        return kpiOriginCode;
    }

    public void setKpiOriginCode(String kpiOriginCode) {
        this.kpiOriginCode = kpiOriginCode;
    }

    public String getKpiOriginName() {
        return kpiOriginName;
    }

    public void setKpiOriginName(String kpiOriginName) {
        this.kpiOriginName = kpiOriginName;
    }

    public String getUnitKpi() {
        return unitKpi;
    }

    public void setUnitKpi(String unitKpi) {
        this.unitKpi = unitKpi;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Instant modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatGraphKpiOriginDTO)) {
            return false;
        }

        return id != null && id.equals(((CatGraphKpiOriginDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatGraphKpiOriginDTO{" +
            "id=" + getId() +
            "}";
    }
}
