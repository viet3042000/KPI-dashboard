package com.b4t.app.service.dto;

import java.io.Serializable;

public class RptGraphDTO implements Serializable {

    private static final long serialVersionUID = 1L;
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
    private Double valPlan;
    private Double valPlanYear;
    private Double val;
    private Double valTotal;
    private Double valTotalLastYear;
    private Double valAcc;
    private Double valLastest;
    private Double valLastYear;
    private Double valDelta;
    private Double valDeltaYear;
    private Double percentPlan;
    private Double percentPlanYear;
    private Double percentGrow;
    private Double percentGrowYear;
    private Double percentPrevious;
    private Double valPrePeriod;
    private Double percentPrePeriod;
    private Double valAccLastYear;
    private Double percentAccLastYear;
    private Double valLastQuarter;
    private Double valQuarter;
    private Double percentLastQuarter;
    private Double valAccLastQuarter;
    private Double percentAccLastQuarter;
    private Long alarmLevelPlan;
    private Long alarmLevelPlanYear;
    private Long alarmLevelGrow;
    private Long alarmLevelGrowYear;
    private String unitKpi;
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

    public Double getValPlan() {
        return valPlan;
    }

    public void setValPlan(Double valPlan) {
        this.valPlan = valPlan;
    }

    public Double getValPlanYear() {
        return valPlanYear;
    }

    public void setValPlanYear(Double valPlanYear) {
        this.valPlanYear = valPlanYear;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public Double getValTotal() {
        return valTotal;
    }

    public void setValTotal(Double valTotal) {
        this.valTotal = valTotal;
    }

    public Double getValTotalLastYear() {
        return valTotalLastYear;
    }

    public void setValTotalLastYear(Double valTotalLastYear) {
        this.valTotalLastYear = valTotalLastYear;
    }

    public Double getValAcc() {
        return valAcc;
    }

    public void setValAcc(Double valAcc) {
        this.valAcc = valAcc;
    }

    public Double getValLastest() {
        return valLastest;
    }

    public void setValLastest(Double valLastest) {
        this.valLastest = valLastest;
    }

    public Double getValLastYear() {
        return valLastYear;
    }

    public void setValLastYear(Double valLastYear) {
        this.valLastYear = valLastYear;
    }

    public Double getValDelta() {
        return valDelta;
    }

    public void setValDelta(Double valDelta) {
        this.valDelta = valDelta;
    }

    public Double getValDeltaYear() {
        return valDeltaYear;
    }

    public void setValDeltaYear(Double valDeltaYear) {
        this.valDeltaYear = valDeltaYear;
    }

    public Double getPercentPlan() {
        return percentPlan;
    }

    public void setPercentPlan(Double percentPlan) {
        this.percentPlan = percentPlan;
    }

    public Double getPercentPlanYear() {
        return percentPlanYear;
    }

    public void setPercentPlanYear(Double percentPlanYear) {
        this.percentPlanYear = percentPlanYear;
    }

    public Double getPercentGrow() {
        return percentGrow;
    }

    public void setPercentGrow(Double percentGrow) {
        this.percentGrow = percentGrow;
    }

    public Double getPercentGrowYear() {
        return percentGrowYear;
    }

    public void setPercentGrowYear(Double percentGrowYear) {
        this.percentGrowYear = percentGrowYear;
    }

    public Double getPercentPrevious() {
        return percentPrevious;
    }

    public void setPercentPrevious(Double percentPrevious) {
        this.percentPrevious = percentPrevious;
    }

    public Double getValPrePeriod() {
        return valPrePeriod;
    }

    public void setValPrePeriod(Double valPrePeriod) {
        this.valPrePeriod = valPrePeriod;
    }

    public Double getPercentPrePeriod() {
        return percentPrePeriod;
    }

    public void setPercentPrePeriod(Double percentPrePeriod) {
        this.percentPrePeriod = percentPrePeriod;
    }

    public Double getValAccLastYear() {
        return valAccLastYear;
    }

    public void setValAccLastYear(Double valAccLastYear) {
        this.valAccLastYear = valAccLastYear;
    }

    public Double getPercentAccLastYear() {
        return percentAccLastYear;
    }

    public void setPercentAccLastYear(Double percentAccLastYear) {
        this.percentAccLastYear = percentAccLastYear;
    }

    public Double getValLastQuarter() {
        return valLastQuarter;
    }

    public void setValLastQuarter(Double valLastQuarter) {
        this.valLastQuarter = valLastQuarter;
    }

    public Double getValQuarter() {
        return valQuarter;
    }

    public void setValQuarter(Double valQuarter) {
        this.valQuarter = valQuarter;
    }

    public Double getPercentLastQuarter() {
        return percentLastQuarter;
    }

    public void setPercentLastQuarter(Double percentLastQuarter) {
        this.percentLastQuarter = percentLastQuarter;
    }

    public Double getValAccLastQuarter() {
        return valAccLastQuarter;
    }

    public void setValAccLastQuarter(Double valAccLastQuarter) {
        this.valAccLastQuarter = valAccLastQuarter;
    }

    public Double getPercentAccLastQuarter() {
        return percentAccLastQuarter;
    }

    public void setPercentAccLastQuarter(Double percentAccLastQuarter) {
        this.percentAccLastQuarter = percentAccLastQuarter;
    }

    public Long getAlarmLevelPlan() {
        return alarmLevelPlan;
    }

    public void setAlarmLevelPlan(Long alarmLevelPlan) {
        this.alarmLevelPlan = alarmLevelPlan;
    }

    public Long getAlarmLevelPlanYear() {
        return alarmLevelPlanYear;
    }

    public void setAlarmLevelPlanYear(Long alarmLevelPlanYear) {
        this.alarmLevelPlanYear = alarmLevelPlanYear;
    }

    public Long getAlarmLevelGrow() {
        return alarmLevelGrow;
    }

    public void setAlarmLevelGrow(Long alarmLevelGrow) {
        this.alarmLevelGrow = alarmLevelGrow;
    }

    public Long getAlarmLevelGrowYear() {
        return alarmLevelGrowYear;
    }

    public void setAlarmLevelGrowYear(Long alarmLevelGrowYear) {
        this.alarmLevelGrowYear = alarmLevelGrowYear;
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
}
