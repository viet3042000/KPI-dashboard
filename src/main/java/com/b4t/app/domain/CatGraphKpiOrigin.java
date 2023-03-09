package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * A CatGraphKpiOrigin.
 */
@Entity
@Table(name = "cat_graph_kpi_origin", schema = "mic_dashboard", catalog = "")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatGraphKpiOrigin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatGraphKpiOrigin)) {
            return false;
        }
        return id != null && id.equals(((CatGraphKpiOrigin) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatGraphKpiOrigin{" +
            "id=" + getId() +
            "}";
    }

    @Basic
    @Column(name = "kpi_origin_code")
    public String getKpiOriginCode() {
        return kpiOriginCode;
    }

    public void setKpiOriginCode(String kpiOriginCode) {
        this.kpiOriginCode = kpiOriginCode;
    }

    @Basic
    @Column(name = "kpi_origin_name")
    public String getKpiOriginName() {
        return kpiOriginName;
    }

    public void setKpiOriginName(String kpiOriginName) {
        this.kpiOriginName = kpiOriginName;
    }

    @Basic
    @Column(name = "unit_kpi")
    public String getUnitKpi() {
        return unitKpi;
    }

    public void setUnitKpi(String unitKpi) {
        this.unitKpi = unitKpi;
    }

    @Basic
    @Column(name = "domain_code")
    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    @Basic
    @Column(name = "time_type")
    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    @Basic
    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "created_time")
    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "modified_by")
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Basic
    @Column(name = "modified_time")
    public Instant getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Instant modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
