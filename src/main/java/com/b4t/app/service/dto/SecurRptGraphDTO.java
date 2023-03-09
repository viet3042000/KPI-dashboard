package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.SecurRptGraph} entity.
 */
public class SecurRptGraphDTO implements Serializable {

    private Long id;

    private Long timeType;

    private Long prdId;

    private Long kpiId;

    private String kpiCode;

    private String kpiName;

    private String objCode;

    private String objName;

    private String parentCode;

    private String parentName;

    private Long inputLevel;

    private Long valPlan;

    private Long valPlanMon;

    private Long valPlanQuar;

    private Long valPlanYear;

    private Long val;

    private Long valMon;

    private Long valQuar;

    private Long valYear;

    private Long valLastMon;

    private Long valLastQuar;

    private Long valLastYear;

    private Long valDelta;

    private Long valDeltaMon;

    private Long valDeltaQuar;

    private Long valDeltaYear;

    private Long percentPlan;

    private Long percentPlanMon;

    private Long percentPlanQuar;

    private Long percentPlanYear;

    private Long percentGrow;

    private Long percentGrowMon;

    private Long percentGrowQuar;

    private Long percentGrowYear;

    private String colorAlarm;

    private String domainCode;


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

    public String getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
    }

    public Long getValPlan() {
        return valPlan;
    }

    public void setValPlan(Long valPlan) {
        this.valPlan = valPlan;
    }

    public Long getValPlanMon() {
        return valPlanMon;
    }

    public void setValPlanMon(Long valPlanMon) {
        this.valPlanMon = valPlanMon;
    }

    public Long getValPlanQuar() {
        return valPlanQuar;
    }

    public void setValPlanQuar(Long valPlanQuar) {
        this.valPlanQuar = valPlanQuar;
    }

    public Long getValPlanYear() {
        return valPlanYear;
    }

    public void setValPlanYear(Long valPlanYear) {
        this.valPlanYear = valPlanYear;
    }

    public Long getVal() {
        return val;
    }

    public void setVal(Long val) {
        this.val = val;
    }

    public Long getValMon() {
        return valMon;
    }

    public void setValMon(Long valMon) {
        this.valMon = valMon;
    }

    public Long getValQuar() {
        return valQuar;
    }

    public void setValQuar(Long valQuar) {
        this.valQuar = valQuar;
    }

    public Long getValYear() {
        return valYear;
    }

    public void setValYear(Long valYear) {
        this.valYear = valYear;
    }

    public Long getValLastMon() {
        return valLastMon;
    }

    public void setValLastMon(Long valLastMon) {
        this.valLastMon = valLastMon;
    }

    public Long getValLastQuar() {
        return valLastQuar;
    }

    public void setValLastQuar(Long valLastQuar) {
        this.valLastQuar = valLastQuar;
    }

    public Long getValLastYear() {
        return valLastYear;
    }

    public void setValLastYear(Long valLastYear) {
        this.valLastYear = valLastYear;
    }

    public Long getValDelta() {
        return valDelta;
    }

    public void setValDelta(Long valDelta) {
        this.valDelta = valDelta;
    }

    public Long getValDeltaMon() {
        return valDeltaMon;
    }

    public void setValDeltaMon(Long valDeltaMon) {
        this.valDeltaMon = valDeltaMon;
    }

    public Long getValDeltaQuar() {
        return valDeltaQuar;
    }

    public void setValDeltaQuar(Long valDeltaQuar) {
        this.valDeltaQuar = valDeltaQuar;
    }

    public Long getValDeltaYear() {
        return valDeltaYear;
    }

    public void setValDeltaYear(Long valDeltaYear) {
        this.valDeltaYear = valDeltaYear;
    }

    public Long getPercentPlan() {
        return percentPlan;
    }

    public void setPercentPlan(Long percentPlan) {
        this.percentPlan = percentPlan;
    }

    public Long getPercentPlanMon() {
        return percentPlanMon;
    }

    public void setPercentPlanMon(Long percentPlanMon) {
        this.percentPlanMon = percentPlanMon;
    }

    public Long getPercentPlanQuar() {
        return percentPlanQuar;
    }

    public void setPercentPlanQuar(Long percentPlanQuar) {
        this.percentPlanQuar = percentPlanQuar;
    }

    public Long getPercentPlanYear() {
        return percentPlanYear;
    }

    public void setPercentPlanYear(Long percentPlanYear) {
        this.percentPlanYear = percentPlanYear;
    }

    public Long getPercentGrow() {
        return percentGrow;
    }

    public void setPercentGrow(Long percentGrow) {
        this.percentGrow = percentGrow;
    }

    public Long getPercentGrowMon() {
        return percentGrowMon;
    }

    public void setPercentGrowMon(Long percentGrowMon) {
        this.percentGrowMon = percentGrowMon;
    }

    public Long getPercentGrowQuar() {
        return percentGrowQuar;
    }

    public void setPercentGrowQuar(Long percentGrowQuar) {
        this.percentGrowQuar = percentGrowQuar;
    }

    public Long getPercentGrowYear() {
        return percentGrowYear;
    }

    public void setPercentGrowYear(Long percentGrowYear) {
        this.percentGrowYear = percentGrowYear;
    }

    public String getColorAlarm() {
        return colorAlarm;
    }

    public void setColorAlarm(String colorAlarm) {
        this.colorAlarm = colorAlarm;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SecurRptGraphDTO securRptGraphDTO = (SecurRptGraphDTO) o;
        if (securRptGraphDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), securRptGraphDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SecurRptGraphDTO{" +
            "id=" + getId() +
            ", timeType=" + getTimeType() +
            ", prdId=" + getPrdId() +
            ", kpiId=" + getKpiId() +
            ", kpiCode='" + getKpiCode() + "'" +
            ", kpiName='" + getKpiName() + "'" +
            ", objCode='" + getObjCode() + "'" +
            ", objName='" + getObjName() + "'" +
            ", parentCode='" + getParentCode() + "'" +
            ", parentName='" + getParentName() + "'" +
            ", inputLevel=" + getInputLevel() +
            ", valPlan=" + getValPlan() +
            ", valPlanMon=" + getValPlanMon() +
            ", valPlanQuar=" + getValPlanQuar() +
            ", valPlanYear=" + getValPlanYear() +
            ", val=" + getVal() +
            ", valMon=" + getValMon() +
            ", valQuar=" + getValQuar() +
            ", valYear=" + getValYear() +
            ", valLastMon=" + getValLastMon() +
            ", valLastQuar=" + getValLastQuar() +
            ", valLastYear=" + getValLastYear() +
            ", valDelta=" + getValDelta() +
            ", valDeltaMon=" + getValDeltaMon() +
            ", valDeltaQuar=" + getValDeltaQuar() +
            ", valDeltaYear=" + getValDeltaYear() +
            ", percentPlan=" + getPercentPlan() +
            ", percentPlanMon=" + getPercentPlanMon() +
            ", percentPlanQuar=" + getPercentPlanQuar() +
            ", percentPlanYear=" + getPercentPlanYear() +
            ", percentGrow=" + getPercentGrow() +
            ", percentGrowMon=" + getPercentGrowMon() +
            ", percentGrowQuar=" + getPercentGrowQuar() +
            ", percentGrowYear=" + getPercentGrowYear() +
            ", colorAlarm='" + getColorAlarm() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            "}";
    }
}
