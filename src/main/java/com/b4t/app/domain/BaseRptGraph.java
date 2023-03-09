package com.b4t.app.domain;

import io.swagger.models.auth.In;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Entity
public class BaseRptGraph {

    @Id
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "time_type")
    private Integer timeType;
    @Column(name = "prd_id")
    private Integer prdId;
    @Column(name = "kpi_id")
    private Integer kpiId;
    @Column(name = "kpi_code")
    private String kpiCode;
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
    @Column(name = "input_level")
    private BigInteger inputLevel;
    @Column(name = "val_plan")
    private Double valPlan;
    @Column(name = "val_plan_year")
    private Double valPlanYear;
    @Column(name = "val")
    private Double val;
    @Column(name = "val_acc")
    private Double valAcc;
    @Column(name = "val_total")
    private Double valTotal;
    @Column(name = "val_lastest")
    private Double valLastest;
    @Column(name = "val_last_year")
    private Double valLastYear;
    @Column(name = "val_delta")
    private Double valDelta;
    @Column(name = "val_delta_year")
    private Double valDeltaYear;
    @Column(name = "percent_plan")
    private Double percentPlan;
    @Column(name = "percent_plan_year")
    private Double percentPlanYear;
    @Column(name = "percent_grow")
    private Double percentGrow;
    @Column(name = "percent_grow_year")
    private Double percentGrowYear;
    @Column(name = "alarm_level_plan")
    private Integer alarmLevelPlan;
    @Column(name = "domain_code")
    private String domainCode;
    @Column(name = "kpi_display")
    private String kpiDisplay;
    @Column(name = "value")
    private Double value;
    @Column(name = "unit_kpi")
    private String unitKpi;
    @Column(name = "unit_view_code")
    private String unitViewCode;
    @Column(name = "x_axis")
    private Integer xAxis;
    @Column(name = "unit_name")
    private String unitName;

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

    public Double getValTotal() {
        return valTotal;
    }

    public void setValTotal(Double valTotal) {
        this.valTotal = valTotal;
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

    public Integer getKpiId() {
        return kpiId;
    }

    public void setKpiId(Integer kpiId) {
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
}
