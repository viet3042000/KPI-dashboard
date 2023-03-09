package com.b4t.app.service.dto;

import com.b4t.app.commons.DataUtil;

import java.util.Date;

public class BaseRptGraphESDTO {
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
    private Long alarmLevelPlan;
    private Long alarmLevelPlanYear;
    private Long alarmLevelGrow;
    private Long alarmLevelGrowYear;
    private String unitKpi;
    private String domainCode;
    private Long alarmPlanType;
    private Long alarmThresholdType;
    private String unitViewCode;
    private String tableDestination;
    private Date updateTime;
    private String objCodeFull;
    private String unitName;
    private Double rate;
    private String kpiDisplay;
    private String timeTypeName;
    private String inputLevelName;
    private Double value;
    private Integer xAxis;
    private String dateTime;
    private String objNameFull;

    public String getDateTime() {

        switch (this.timeType.intValue()) {
            case 2:
                // Thoi gian la MONTH
                this.dateTime = DataUtil.formatDatePattern(this.getPrdId(), "MM/yyyy");
                break;
            case 3:
                // Thoi gian la QUARTER
                this.dateTime = DataUtil.formatQuarterPattern(this.getPrdId());
                break;
            case 4:
                // Thoi gian la YEAR
                this.dateTime = DataUtil.formatDatePattern(this.getPrdId(), "yyyy");
                break;
        }
        return this.dateTime;
    }


    public String groupKpiObj() {
        return this.objNameFull + "###" + this.kpiName + "###" + this.unitName;
    }

    public String groupKpiGroupBar() {
        return this.kpiName + "###" + this.unitName;
    }

    public String getObjNameFull() {
        return objNameFull;
    }

    public void setObjNameFull(String objNameFull) {
        this.objNameFull = objNameFull;
    }

    public String getTimeTypeName() {
        return timeTypeName;
    }

    public void setTimeTypeName(String timeTypeName) {
        this.timeTypeName = timeTypeName;
    }

    public String getInputLevelName() {
        return inputLevelName;
    }

    public void setInputLevelName(String inputLevelName) {
        this.inputLevelName = inputLevelName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getKpiDisplay() {
        return kpiDisplay;
    }

    public void setKpiDisplay(String kpiDisplay) {
        this.kpiDisplay = kpiDisplay;
    }

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

    public Long getAlarmPlanType() {
        return alarmPlanType;
    }

    public void setAlarmPlanType(Long alarmPlanType) {
        this.alarmPlanType = alarmPlanType;
    }

    public Long getAlarmThresholdType() {
        return alarmThresholdType;
    }

    public void setAlarmThresholdType(Long alarmThresholdType) {
        this.alarmThresholdType = alarmThresholdType;
    }


    public String getUnitViewCode() {
        return unitViewCode;
    }

    public void setUnitViewCode(String unitViewCode) {
        this.unitViewCode = unitViewCode;
    }


    public String getTableDestination() {
        return tableDestination;
    }

    public void setTableDestination(String tableDestination) {
        this.tableDestination = tableDestination;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getObjCodeFull() {
        return objCodeFull;
    }

    public void setObjCodeFull(String objCodeFull) {
        this.objCodeFull = objCodeFull;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
