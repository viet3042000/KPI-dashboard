package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A RawDataFromMicUnit.
 */
@Entity
@Table(name = "raw_data_from_mic_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RawDataFromMicUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_type")
    private Long timeType;

    @Column(name = "input_level")
    private Long inputLevel;

    @Column(name = "prd_id")
    private Long prdId;

    @Column(name = "kpi_id")
    private Long kpiId;
    @Column(name = "kpi_name")
    private String kpiName;
    @Column(name = "obj_code")
    private String objCode;

    @Column(name = "obj_name")
    private String objName;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "val")
    private Double val;

    @Column(name = "update_time")
    private Instant updateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeType() {
        return timeType;
    }

    public RawDataFromMicUnit timeType(Long timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getInputLevel() {
        return inputLevel;
    }

    public RawDataFromMicUnit inputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
        return this;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
    }

    public Long getPrdId() {
        return prdId;
    }

    public RawDataFromMicUnit prdId(Long prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public RawDataFromMicUnit kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public RawDataFromMicUnit kpiName(String kpiName) {
        this.kpiName = kpiName;
        return this;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getObjCode() {
        return objCode;
    }

    public RawDataFromMicUnit objCode(String objCode) {
        this.objCode = objCode;
        return this;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName;
    }

    public RawDataFromMicUnit objName(String objName) {
        this.objName = objName;
        return this;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public RawDataFromMicUnit parentCode(String parentCode) {
        this.parentCode = parentCode;
        return this;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public RawDataFromMicUnit parentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Double getVal() {
        return val;
    }

    public RawDataFromMicUnit val(Double val) {
        this.val = val;
        return this;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public RawDataFromMicUnit updateTime(Instant updateTime) {
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
        if (!(o instanceof RawDataFromMicUnit)) {
            return false;
        }
        return id != null && id.equals(((RawDataFromMicUnit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RawDataFromMicUnit{" +
            "id=" + getId() +
            ", timeType=" + getTimeType() +
            ", inputLevel=" + getInputLevel() +
            ", prdId=" + getPrdId() +
            ", kpiId=" + getKpiId() +
            ", kpiName='" + getKpiName() + "'" +
            ", objCode='" + getObjCode() + "'" +
            ", objName='" + getObjName() + "'" +
            ", parentCode='" + getParentCode() + "'" +
            ", parentName='" + getParentName() + "'" +
            ", val=" + getVal() +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
