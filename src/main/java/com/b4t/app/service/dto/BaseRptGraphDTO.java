package com.b4t.app.service.dto;

import java.math.BigInteger;

public class BaseRptGraphDTO {
    private BigInteger id;
    private Integer timeType;
    private String timeTypeName;
    private Integer prdId;
    private String kpiId;
    private String kpiCode;
    private String kpiName;
    private String objCode;
    private String objName;
    private String parentCode;
    private String parentName;
    private BigInteger inputLevel;
    private Double valPlan;
    private Double valPlanYear;
    private Double val;
    private Double valAcc;
    private Double valTotal;
    private Double valLastest;
    private Double valLastYear;
    private Double valDelta;
    private Double valDeltaYear;
    private Double percentPlan;
    private Double percentPlanYear;
    private Double percentGrow;
    private Double percentGrowYear;
    private Integer alarmLevelPlan;
    private String domainCode;
    private String kpiDisplay;
    private Double value;
    private String unitKpi;
    private String unitViewCode;
    private Integer xAxis;
    private String unitName;
    private String dateTime;
    private String inputLevelName;

    public String getUnitViewCode() {
        return unitViewCode;
    }

    public void setUnitViewCode(String unitViewCode) {
        this.unitViewCode = unitViewCode;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTimeTypeName() {
        return timeTypeName;
    }

    public void setTimeTypeName(String timeTypeName) {
        this.timeTypeName = timeTypeName;
    }

    public Double getValTotal() {
        return valTotal;
    }

    public void setValTotal(Double valTotal) {
        this.valTotal = valTotal;
    }

    public String getInputLevelName() {

        return inputLevelName;
    }

    public void setInputLevelName(String inputLevelName) {
        this.inputLevelName = inputLevelName;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
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

    public BigInteger getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(BigInteger inputLevel) {
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

    public Integer getAlarmLevelPlan() {
        return alarmLevelPlan;
    }

    public void setAlarmLevelPlan(Integer alarmLevelPlan) {
        this.alarmLevelPlan = alarmLevelPlan;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getKpiDisplay() {
        return kpiDisplay;
    }

    public void setKpiDisplay(String kpiDisplay) {
        this.kpiDisplay = kpiDisplay;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnitKpi() {
        return unitKpi;
    }

    public void setUnitKpi(String unitKpi) {
        this.unitKpi = unitKpi;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDateTime() {

//        switch (this.timeType) {
//            case 2:
//                // Thoi gian la MONTH
//                this.dateTime = DataUtil.formatDatePattern(this.getPrdId(), "MM/yyyy");
//                break;
//            case 3:
//                // Thoi gian la QUARTER
//                this.dateTime = DataUtil.formatQuarterPattern(this.getPrdId());
//                break;
//            case 4:
//                // Thoi gian la YEAR
//                this.dateTime = DataUtil.formatDatePattern(this.getPrdId(), "yyyy");
//                break;
//        }
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String groupKpiObj() {
        return this.objName + "###" + this.kpiName + "###" + this.unitName;
    }
    public String groupKpiGroupBar() {
        return this.kpiName + "###" + this.unitName;
    }


}
