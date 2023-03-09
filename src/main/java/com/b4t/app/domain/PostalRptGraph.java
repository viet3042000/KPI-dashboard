package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A PostalRptGraph.
 */
@Entity
@Table(name = "postal_rpt_graph")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostalRptGraph implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_type")
    private Long timeType;

    @Column(name = "prd_id")
    private Long prdId;

    @Column(name = "kpi_id")
    private Long kpiId;

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
    private Long inputLevel;

    @Column(name = "val_plan")
    private Long valPlan;

    @Column(name = "val_plan_mon")
    private Long valPlanMon;

    @Column(name = "val_plan_quar")
    private Long valPlanQuar;

    @Column(name = "val_plan_year")
    private Long valPlanYear;

    @Column(name = "val")
    private Long val;

    @Column(name = "val_mon")
    private Long valMon;

    @Column(name = "val_quar")
    private Long valQuar;

    @Column(name = "val_year")
    private Long valYear;

    @Column(name = "val_last_mon")
    private Long valLastMon;

    @Column(name = "val_last_quar")
    private Long valLastQuar;

    @Column(name = "val_last_year")
    private Long valLastYear;

    @Column(name = "val_delta")
    private Long valDelta;

    @Column(name = "val_delta_mon")
    private Long valDeltaMon;

    @Column(name = "val_delta_quar")
    private Long valDeltaQuar;

    @Column(name = "val_delta_year")
    private Long valDeltaYear;

    @Column(name = "percent_plan")
    private Long percentPlan;

    @Column(name = "percent_plan_mon")
    private Long percentPlanMon;

    @Column(name = "percent_plan_quar")
    private Long percentPlanQuar;

    @Column(name = "percent_plan_year")
    private Long percentPlanYear;

    @Column(name = "percent_grow")
    private Long percentGrow;

    @Column(name = "percent_grow_mon")
    private Long percentGrowMon;

    @Column(name = "percent_grow_quar")
    private Long percentGrowQuar;

    @Column(name = "percent_grow_year")
    private Long percentGrowYear;

    @Column(name = "color_alarm")
    private String colorAlarm;

    @Column(name = "domain_code")
    private String domainCode;

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

    public PostalRptGraph timeType(Long timeType) {
        this.timeType = timeType;
        return this;
    }

    public void setTimeType(Long timeType) {
        this.timeType = timeType;
    }

    public Long getPrdId() {
        return prdId;
    }

    public PostalRptGraph prdId(Long prdId) {
        this.prdId = prdId;
        return this;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public PostalRptGraph kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public PostalRptGraph kpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
        return this;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getKpiName() {
        return kpiName;
    }

    public PostalRptGraph kpiName(String kpiName) {
        this.kpiName = kpiName;
        return this;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getObjCode() {
        return objCode;
    }

    public PostalRptGraph objCode(String objCode) {
        this.objCode = objCode;
        return this;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName;
    }

    public PostalRptGraph objName(String objName) {
        this.objName = objName;
        return this;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public PostalRptGraph parentCode(String parentCode) {
        this.parentCode = parentCode;
        return this;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public PostalRptGraph parentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getInputLevel() {
        return inputLevel;
    }

    public PostalRptGraph inputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
        return this;
    }

    public void setInputLevel(Long inputLevel) {
        this.inputLevel = inputLevel;
    }

    public Long getValPlan() {
        return valPlan;
    }

    public PostalRptGraph valPlan(Long valPlan) {
        this.valPlan = valPlan;
        return this;
    }

    public void setValPlan(Long valPlan) {
        this.valPlan = valPlan;
    }

    public Long getValPlanMon() {
        return valPlanMon;
    }

    public PostalRptGraph valPlanMon(Long valPlanMon) {
        this.valPlanMon = valPlanMon;
        return this;
    }

    public void setValPlanMon(Long valPlanMon) {
        this.valPlanMon = valPlanMon;
    }

    public Long getValPlanQuar() {
        return valPlanQuar;
    }

    public PostalRptGraph valPlanQuar(Long valPlanQuar) {
        this.valPlanQuar = valPlanQuar;
        return this;
    }

    public void setValPlanQuar(Long valPlanQuar) {
        this.valPlanQuar = valPlanQuar;
    }

    public Long getValPlanYear() {
        return valPlanYear;
    }

    public PostalRptGraph valPlanYear(Long valPlanYear) {
        this.valPlanYear = valPlanYear;
        return this;
    }

    public void setValPlanYear(Long valPlanYear) {
        this.valPlanYear = valPlanYear;
    }

    public Long getVal() {
        return val;
    }

    public PostalRptGraph val(Long val) {
        this.val = val;
        return this;
    }

    public void setVal(Long val) {
        this.val = val;
    }

    public Long getValMon() {
        return valMon;
    }

    public PostalRptGraph valMon(Long valMon) {
        this.valMon = valMon;
        return this;
    }

    public void setValMon(Long valMon) {
        this.valMon = valMon;
    }

    public Long getValQuar() {
        return valQuar;
    }

    public PostalRptGraph valQuar(Long valQuar) {
        this.valQuar = valQuar;
        return this;
    }

    public void setValQuar(Long valQuar) {
        this.valQuar = valQuar;
    }

    public Long getValYear() {
        return valYear;
    }

    public PostalRptGraph valYear(Long valYear) {
        this.valYear = valYear;
        return this;
    }

    public void setValYear(Long valYear) {
        this.valYear = valYear;
    }

    public Long getValLastMon() {
        return valLastMon;
    }

    public PostalRptGraph valLastMon(Long valLastMon) {
        this.valLastMon = valLastMon;
        return this;
    }

    public void setValLastMon(Long valLastMon) {
        this.valLastMon = valLastMon;
    }

    public Long getValLastQuar() {
        return valLastQuar;
    }

    public PostalRptGraph valLastQuar(Long valLastQuar) {
        this.valLastQuar = valLastQuar;
        return this;
    }

    public void setValLastQuar(Long valLastQuar) {
        this.valLastQuar = valLastQuar;
    }

    public Long getValLastYear() {
        return valLastYear;
    }

    public PostalRptGraph valLastYear(Long valLastYear) {
        this.valLastYear = valLastYear;
        return this;
    }

    public void setValLastYear(Long valLastYear) {
        this.valLastYear = valLastYear;
    }

    public Long getValDelta() {
        return valDelta;
    }

    public PostalRptGraph valDelta(Long valDelta) {
        this.valDelta = valDelta;
        return this;
    }

    public void setValDelta(Long valDelta) {
        this.valDelta = valDelta;
    }

    public Long getValDeltaMon() {
        return valDeltaMon;
    }

    public PostalRptGraph valDeltaMon(Long valDeltaMon) {
        this.valDeltaMon = valDeltaMon;
        return this;
    }

    public void setValDeltaMon(Long valDeltaMon) {
        this.valDeltaMon = valDeltaMon;
    }

    public Long getValDeltaQuar() {
        return valDeltaQuar;
    }

    public PostalRptGraph valDeltaQuar(Long valDeltaQuar) {
        this.valDeltaQuar = valDeltaQuar;
        return this;
    }

    public void setValDeltaQuar(Long valDeltaQuar) {
        this.valDeltaQuar = valDeltaQuar;
    }

    public Long getValDeltaYear() {
        return valDeltaYear;
    }

    public PostalRptGraph valDeltaYear(Long valDeltaYear) {
        this.valDeltaYear = valDeltaYear;
        return this;
    }

    public void setValDeltaYear(Long valDeltaYear) {
        this.valDeltaYear = valDeltaYear;
    }

    public Long getPercentPlan() {
        return percentPlan;
    }

    public PostalRptGraph percentPlan(Long percentPlan) {
        this.percentPlan = percentPlan;
        return this;
    }

    public void setPercentPlan(Long percentPlan) {
        this.percentPlan = percentPlan;
    }

    public Long getPercentPlanMon() {
        return percentPlanMon;
    }

    public PostalRptGraph percentPlanMon(Long percentPlanMon) {
        this.percentPlanMon = percentPlanMon;
        return this;
    }

    public void setPercentPlanMon(Long percentPlanMon) {
        this.percentPlanMon = percentPlanMon;
    }

    public Long getPercentPlanQuar() {
        return percentPlanQuar;
    }

    public PostalRptGraph percentPlanQuar(Long percentPlanQuar) {
        this.percentPlanQuar = percentPlanQuar;
        return this;
    }

    public void setPercentPlanQuar(Long percentPlanQuar) {
        this.percentPlanQuar = percentPlanQuar;
    }

    public Long getPercentPlanYear() {
        return percentPlanYear;
    }

    public PostalRptGraph percentPlanYear(Long percentPlanYear) {
        this.percentPlanYear = percentPlanYear;
        return this;
    }

    public void setPercentPlanYear(Long percentPlanYear) {
        this.percentPlanYear = percentPlanYear;
    }

    public Long getPercentGrow() {
        return percentGrow;
    }

    public PostalRptGraph percentGrow(Long percentGrow) {
        this.percentGrow = percentGrow;
        return this;
    }

    public void setPercentGrow(Long percentGrow) {
        this.percentGrow = percentGrow;
    }

    public Long getPercentGrowMon() {
        return percentGrowMon;
    }

    public PostalRptGraph percentGrowMon(Long percentGrowMon) {
        this.percentGrowMon = percentGrowMon;
        return this;
    }

    public void setPercentGrowMon(Long percentGrowMon) {
        this.percentGrowMon = percentGrowMon;
    }

    public Long getPercentGrowQuar() {
        return percentGrowQuar;
    }

    public PostalRptGraph percentGrowQuar(Long percentGrowQuar) {
        this.percentGrowQuar = percentGrowQuar;
        return this;
    }

    public void setPercentGrowQuar(Long percentGrowQuar) {
        this.percentGrowQuar = percentGrowQuar;
    }

    public Long getPercentGrowYear() {
        return percentGrowYear;
    }

    public PostalRptGraph percentGrowYear(Long percentGrowYear) {
        this.percentGrowYear = percentGrowYear;
        return this;
    }

    public void setPercentGrowYear(Long percentGrowYear) {
        this.percentGrowYear = percentGrowYear;
    }

    public String getColorAlarm() {
        return colorAlarm;
    }

    public PostalRptGraph colorAlarm(String colorAlarm) {
        this.colorAlarm = colorAlarm;
        return this;
    }

    public void setColorAlarm(String colorAlarm) {
        this.colorAlarm = colorAlarm;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public PostalRptGraph domainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostalRptGraph)) {
            return false;
        }
        return id != null && id.equals(((PostalRptGraph) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PostalRptGraph{" +
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
