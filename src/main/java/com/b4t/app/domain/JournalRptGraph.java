package com.b4t.app.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "journal_rpt_graph", schema = "mic_dashboard", catalog = "")
public class JournalRptGraph {
    private Long id;
    private Integer timeType;
    private Integer prdId;
    private Integer kpiId;
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

    @Basic
    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TIME_TYPE")
    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    @Basic
    @Column(name = "PRD_ID")
    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    @Basic
    @Column(name = "KPI_ID")
    public Integer getKpiId() {
        return kpiId;
    }

    public void setKpiId(Integer kpiId) {
        this.kpiId = kpiId;
    }

    @Basic
    @Column(name = "KPI_CODE")
    public String getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    @Basic
    @Column(name = "KPI_NAME")
    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    @Basic
    @Column(name = "OBJ_CODE")
    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    @Basic
    @Column(name = "OBJ_NAME")
    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    @Basic
    @Column(name = "PARENT_CODE")
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Basic
    @Column(name = "PARENT_NAME")
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Basic
    @Column(name = "INPUT_LEVEL")
    public Long getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
    }

    @Basic
    @Column(name = "VAL_PLAN")
    public Long getValPlan() {
        return valPlan;
    }

    public void setValPlan(Long valPlan) {
        this.valPlan = valPlan;
    }

    @Basic
    @Column(name = "VAL_PLAN_MON")
    public Long getValPlanMon() {
        return valPlanMon;
    }

    public void setValPlanMon(Long valPlanMon) {
        this.valPlanMon = valPlanMon;
    }

    @Basic
    @Column(name = "VAL_PLAN_QUAR")
    public Long getValPlanQuar() {
        return valPlanQuar;
    }

    public void setValPlanQuar(Long valPlanQuar) {
        this.valPlanQuar = valPlanQuar;
    }

    @Basic
    @Column(name = "VAL_PLAN_YEAR")
    public Long getValPlanYear() {
        return valPlanYear;
    }

    public void setValPlanYear(Long valPlanYear) {
        this.valPlanYear = valPlanYear;
    }

    @Basic
    @Column(name = "VAL")
    public Long getVal() {
        return val;
    }

    public void setVal(Long val) {
        this.val = val;
    }

    @Basic
    @Column(name = "VAL_MON")
    public Long getValMon() {
        return valMon;
    }

    public void setValMon(Long valMon) {
        this.valMon = valMon;
    }

    @Basic
    @Column(name = "VAL_QUAR")
    public Long getValQuar() {
        return valQuar;
    }

    public void setValQuar(Long valQuar) {
        this.valQuar = valQuar;
    }

    @Basic
    @Column(name = "VAL_YEAR")
    public Long getValYear() {
        return valYear;
    }

    public void setValYear(Long valYear) {
        this.valYear = valYear;
    }

    @Basic
    @Column(name = "VAL_LAST_MON")
    public Long getValLastMon() {
        return valLastMon;
    }

    public void setValLastMon(Long valLastMon) {
        this.valLastMon = valLastMon;
    }

    @Basic
    @Column(name = "VAL_LAST_QUAR")
    public Long getValLastQuar() {
        return valLastQuar;
    }

    public void setValLastQuar(Long valLastQuar) {
        this.valLastQuar = valLastQuar;
    }

    @Basic
    @Column(name = "VAL_LAST_YEAR")
    public Long getValLastYear() {
        return valLastYear;
    }

    public void setValLastYear(Long valLastYear) {
        this.valLastYear = valLastYear;
    }

    @Basic
    @Column(name = "VAL_DELTA")
    public Long getValDelta() {
        return valDelta;
    }

    public void setValDelta(Long valDelta) {
        this.valDelta = valDelta;
    }

    @Basic
    @Column(name = "VAL_DELTA_MON")
    public Long getValDeltaMon() {
        return valDeltaMon;
    }

    public void setValDeltaMon(Long valDeltaMon) {
        this.valDeltaMon = valDeltaMon;
    }

    @Basic
    @Column(name = "VAL_DELTA_QUAR")
    public Long getValDeltaQuar() {
        return valDeltaQuar;
    }

    public void setValDeltaQuar(Long valDeltaQuar) {
        this.valDeltaQuar = valDeltaQuar;
    }

    @Basic
    @Column(name = "VAL_DELTA_YEAR")
    public Long getValDeltaYear() {
        return valDeltaYear;
    }

    public void setValDeltaYear(Long valDeltaYear) {
        this.valDeltaYear = valDeltaYear;
    }

    @Basic
    @Column(name = "PERCENT_PLAN")
    public Long getPercentPlan() {
        return percentPlan;
    }

    public void setPercentPlan(Long percentPlan) {
        this.percentPlan = percentPlan;
    }

    @Basic
    @Column(name = "PERCENT_PLAN_MON")
    public Long getPercentPlanMon() {
        return percentPlanMon;
    }

    public void setPercentPlanMon(Long percentPlanMon) {
        this.percentPlanMon = percentPlanMon;
    }

    @Basic
    @Column(name = "PERCENT_PLAN_QUAR")
    public Long getPercentPlanQuar() {
        return percentPlanQuar;
    }

    public void setPercentPlanQuar(Long percentPlanQuar) {
        this.percentPlanQuar = percentPlanQuar;
    }

    @Basic
    @Column(name = "PERCENT_PLAN_YEAR")
    public Long getPercentPlanYear() {
        return percentPlanYear;
    }

    public void setPercentPlanYear(Long percentPlanYear) {
        this.percentPlanYear = percentPlanYear;
    }

    @Basic
    @Column(name = "PERCENT_GROW")
    public Long getPercentGrow() {
        return percentGrow;
    }

    public void setPercentGrow(Long percentGrow) {
        this.percentGrow = percentGrow;
    }

    @Basic
    @Column(name = "PERCENT_GROW_MON")
    public Long getPercentGrowMon() {
        return percentGrowMon;
    }

    public void setPercentGrowMon(Long percentGrowMon) {
        this.percentGrowMon = percentGrowMon;
    }

    @Basic
    @Column(name = "PERCENT_GROW_QUAR")
    public Long getPercentGrowQuar() {
        return percentGrowQuar;
    }

    public void setPercentGrowQuar(Long percentGrowQuar) {
        this.percentGrowQuar = percentGrowQuar;
    }

    @Basic
    @Column(name = "PERCENT_GROW_YEAR")
    public Long getPercentGrowYear() {
        return percentGrowYear;
    }

    public void setPercentGrowYear(Long percentGrowYear) {
        this.percentGrowYear = percentGrowYear;
    }

    @Basic
    @Column(name = "COLOR_ALARM")
    public String getColorAlarm() {
        return colorAlarm;
    }

    public void setColorAlarm(String colorAlarm) {
        this.colorAlarm = colorAlarm;
    }

    @Basic
    @Column(name = "DOMAIN_CODE")
    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalRptGraph that = (JournalRptGraph) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(timeType, that.timeType) &&
            Objects.equals(prdId, that.prdId) &&
            Objects.equals(kpiId, that.kpiId) &&
            Objects.equals(kpiCode, that.kpiCode) &&
            Objects.equals(kpiName, that.kpiName) &&
            Objects.equals(objCode, that.objCode) &&
            Objects.equals(objName, that.objName) &&
            Objects.equals(parentCode, that.parentCode) &&
            Objects.equals(parentName, that.parentName) &&
            Objects.equals(inputLevel, that.inputLevel) &&
            Objects.equals(valPlan, that.valPlan) &&
            Objects.equals(valPlanMon, that.valPlanMon) &&
            Objects.equals(valPlanQuar, that.valPlanQuar) &&
            Objects.equals(valPlanYear, that.valPlanYear) &&
            Objects.equals(val, that.val) &&
            Objects.equals(valMon, that.valMon) &&
            Objects.equals(valQuar, that.valQuar) &&
            Objects.equals(valYear, that.valYear) &&
            Objects.equals(valLastMon, that.valLastMon) &&
            Objects.equals(valLastQuar, that.valLastQuar) &&
            Objects.equals(valLastYear, that.valLastYear) &&
            Objects.equals(valDelta, that.valDelta) &&
            Objects.equals(valDeltaMon, that.valDeltaMon) &&
            Objects.equals(valDeltaQuar, that.valDeltaQuar) &&
            Objects.equals(valDeltaYear, that.valDeltaYear) &&
            Objects.equals(percentPlan, that.percentPlan) &&
            Objects.equals(percentPlanMon, that.percentPlanMon) &&
            Objects.equals(percentPlanQuar, that.percentPlanQuar) &&
            Objects.equals(percentPlanYear, that.percentPlanYear) &&
            Objects.equals(percentGrow, that.percentGrow) &&
            Objects.equals(percentGrowMon, that.percentGrowMon) &&
            Objects.equals(percentGrowQuar, that.percentGrowQuar) &&
            Objects.equals(percentGrowYear, that.percentGrowYear) &&
            Objects.equals(colorAlarm, that.colorAlarm) &&
            Objects.equals(domainCode, that.domainCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeType, prdId, kpiId, kpiCode, kpiName, objCode, objName, parentCode, parentName, inputLevel, valPlan, valPlanMon, valPlanQuar, valPlanYear, val, valMon, valQuar, valYear, valLastMon, valLastQuar, valLastYear, valDelta, valDeltaMon, valDeltaQuar, valDeltaYear, percentPlan, percentPlanMon, percentPlanQuar, percentPlanYear, percentGrow, percentGrowMon, percentGrowQuar, percentGrowYear, colorAlarm, domainCode);
    }
}
