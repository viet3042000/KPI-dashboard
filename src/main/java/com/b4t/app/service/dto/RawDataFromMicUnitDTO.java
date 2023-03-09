package com.b4t.app.service.dto;

import com.b4t.app.commons.DateTypeValidate;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.RawDataFromMicUnit} entity.
 */
public class RawDataFromMicUnitDTO implements Serializable {
    static final String REGEXP_NUM = "/^[0-9]*$/";
    private Long id;
    @NotNull
    @Min(1)
    @Max(4)
    private Long timeType;
    @NotNull
    private Long inputLevel;
    @NotNull
    @DateTypeValidate
    private Long prdId;
    @NotNull
    private Long kpiId;
    @Size(min = 2, max = 150)
    private String kpiName;
    @Size(min = 2, max = 150)
    private String objCode;
    @Size(min = 2, max = 150)
    private String objName;
    @Size(min = 2, max = 150)
    private String parentCode;
    @Size(min = 2, max = 150)
    private String parentName;
    @NotNull
    private Double val;
    private Instant updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeType() {
        return timeType;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
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
        return kpiName != null ? kpiName.trim() : "";
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getObjCode() {
        return objCode != null ? objCode.trim() : objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName != null ? objName.trim() : objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getParentCode() {
        return parentCode != null ? parentCode.trim() : parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName != null ? parentName.trim() : parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
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

        RawDataFromMicUnitDTO rawDataFromMicUnitDTO = (RawDataFromMicUnitDTO) o;
        if (rawDataFromMicUnitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rawDataFromMicUnitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RawDataFromMicUnitDTO{" +
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
