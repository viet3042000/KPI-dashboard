package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A CatGraphKpi.
 */
@Entity
@Table(name = "cat_graph_kpi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CatGraphKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kpi_id")
    private Long kpiId;

    @Column(name = "kpi_code")
    private String kpiCode;

    @Column(name = "kpi_name")
    private String kpiName;

    @Column(name = "kpi_display")
    private String kpiDisplay;

    @Column(name = "unit_kpi")
    private String unitKpi;

    @Column(name = "unit_view_code")
    private String unitViewCode;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "status")
    private Long status;

    @Column(name = "group_kpi_code")
    private String groupKpiCode;

    @Column(name = "domain_code")
    private String domainCode;

    @Column(name = "source")
    private String source;

    @Column(name = "description")
    private String description;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;
    @Column(name = "FORMULA_LEVEL")
    private String formulaLevel;
    @Column(name = "FORMULA_QUAR")
    private String formulaQuar;
    @Column(name = "FORMULA_YEAR")
    private String formulaYear;
    @Column(name = "ALARM_THRESHOLD_TYPE")
    private Long alarmThresholdType;
    @Column(name = "ALARM_PLAN_TYPE")
    private Long alarmPlanType;
    @Column(name = "formula_acc")
    private String formulaAcc;
    @Column(name = "KPI_TYPE")
    private Long kpiType;
    @Column(name = "synonyms")
    private String synonyms;
    @Column(name = "position")
    private Long position;
    @Column(name = "show_on_map")
    private Integer showOnMap;
    @Column(name = "is_alarm")
    private Integer isAlarm;
    @Column(name = "ORDER_INDEX")
    private Long orderIndex;
    @Column(name = "kpi_origin_id")
    private Long kpiOriginId;

    public Integer getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(Integer isAlarm) {
        this.isAlarm = isAlarm;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public CatGraphKpi kpiId(Long kpiId) {
        this.kpiId = kpiId;
        return this;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public CatGraphKpi kpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
        return this;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getKpiName() {
        return kpiName;
    }

    public CatGraphKpi kpiName(String kpiName) {
        this.kpiName = kpiName;
        return this;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getKpiDisplay() {
        return kpiDisplay;
    }

    public CatGraphKpi kpiDisplay(String kpiDisplay) {
        this.kpiDisplay = kpiDisplay;
        return this;
    }

    public void setKpiDisplay(String kpiDisplay) {
        this.kpiDisplay = kpiDisplay;
    }

    public String getUnitKpi() {
        return unitKpi;
    }

    public CatGraphKpi unitKpi(String unitKpi) {
        this.unitKpi = unitKpi;
        return this;
    }

    public void setUnitKpi(String unitKpi) {
        this.unitKpi = unitKpi;
    }

    public String getUnitViewCode() {
        return unitViewCode;
    }

    public CatGraphKpi unitViewCode(String unitViewCode) {
        this.unitViewCode = unitViewCode;
        return this;
    }

    public void setUnitViewCode(String unitViewCode) {
        this.unitViewCode = unitViewCode;
    }

    public Double getRate() {
        return rate;
    }

    public CatGraphKpi rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getStatus() {
        return status;
    }

    public CatGraphKpi status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getGroupKpiCode() {
        return groupKpiCode;
    }

    public CatGraphKpi groupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
        return this;
    }

    public void setGroupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public CatGraphKpi domainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getSource() {
        return source;
    }

    public CatGraphKpi source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public CatGraphKpi description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public CatGraphKpi updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public CatGraphKpi updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Long getKpiOriginId() {
        return kpiOriginId;
    }

    public void setKpiOriginId(Long kpiOriginId) {
        this.kpiOriginId = kpiOriginId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatGraphKpi)) {
            return false;
        }
        return id != null && id.equals(((CatGraphKpi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CatGraphKpi{" +
            "id=" + getId() +
            ", kpiId=" + getKpiId() +
            ", kpiCode='" + getKpiCode() + "'" +
            ", kpiName='" + getKpiName() + "'" +
            ", kpiDisplay='" + getKpiDisplay() + "'" +
            ", unitKpi='" + getUnitKpi() + "'" +
            ", unitViewCode='" + getUnitViewCode() + "'" +
            ", rate=" + getRate() +
            ", status=" + getStatus() +
            ", groupKpiCode='" + getGroupKpiCode() + "'" +
            ", domainCode='" + getDomainCode() + "'" +
            ", source='" + getSource() + "'" +
            ", description='" + getDescription() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }

    public String getFormulaLevel() {
        return formulaLevel;
    }

    public void setFormulaLevel(String formulaLevel) {
        this.formulaLevel = formulaLevel;
    }

    public String getFormulaQuar() {
        return formulaQuar;
    }

    public void setFormulaQuar(String formulaQuar) {
        this.formulaQuar = formulaQuar;
    }

    public String getFormulaYear() {
        return formulaYear;
    }

    public void setFormulaYear(String formulaYear) {
        this.formulaYear = formulaYear;
    }

    public Long getAlarmThresholdType() {
        return alarmThresholdType;
    }

    public void setAlarmThresholdType(Long alarmThresholdType) {
        this.alarmThresholdType = alarmThresholdType;
    }

    public Long getAlarmPlanType() {
        return alarmPlanType;
    }

    public void setAlarmPlanType(Long alarmPlanType) {
        this.alarmPlanType = alarmPlanType;
    }

    public String getFormulaAcc() {
        return formulaAcc;
    }

    public void setFormulaAcc(String formulaAcc) {
        this.formulaAcc = formulaAcc;
    }

    public Long getKpiType() {
        return kpiType;
    }

    public void setKpiType(Long kpiType) {
        this.kpiType = kpiType;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Integer getShowOnMap() {
        return showOnMap;
    }

    public void setShowOnMap(Integer showOnMap) {
        this.showOnMap = showOnMap;
    }

    public CatGraphKpi showOnMap(Integer showOnMap) {
        this.showOnMap = showOnMap;
        return this;
    }
}
