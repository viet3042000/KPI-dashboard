package com.b4t.app.service.dto;

import com.b4t.app.domain.BieumauKehoachchitieu;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.CatGraphKpi} entity.
 */
//@Entity
@SqlResultSetMapping(
    name = "CatGraphKpiDTO",
    entities = {
        @EntityResult(
            entityClass = CatGraphKpiDTO.class,
            fields = {
                @FieldResult(name = "id", column = "id"),
                @FieldResult(name = "kpiId", column = "kpi_id"),
                @FieldResult(name = "kpiCode", column = "kpi_code"),
                @FieldResult(name = "kpiName", column = "kpi_name"),
                @FieldResult(name = "kpiDisplay", column = "kpi_display"),
                @FieldResult(name = "unitKpi", column = "unit_kpi"),
                @FieldResult(name = "unitViewCode", column = "unit_view_code"),
                @FieldResult(name = "unitViewName", column = "unit_view_name"),
                @FieldResult(name = "rate", column = "rate"),
                @FieldResult(name = "unitName", column = "unit_name"),
                @FieldResult(name = "groupKpiCode", column = "group_kpi_code"),
                @FieldResult(name = "groupKpiName", column = "group_kpi_name"),
                @FieldResult(name = "domainCode", column = "domain_code"),
                @FieldResult(name = "domainName", column = "domain_name"),
                @FieldResult(name = "tableName", column = "table_name"),
                @FieldResult(name = "source", column = "source"),
                @FieldResult(name = "status", column = "status"),
                @FieldResult(name = "description", column = "description"),
                @FieldResult(name = "updateTime", column = "update_time"),
                @FieldResult(name = "updateUser", column = "update_user"),
                @FieldResult(name = "formulaLevel", column = "formula_level"),
                @FieldResult(name = "formulaQuar", column = "formula_quar"),
                @FieldResult(name = "formulaYear", column = "formula_year"),
                @FieldResult(name = "alarmThresholdType", column = "alarm_threshold_type"),
                @FieldResult(name = "alarmPlanType", column = "alarm_plan_type"),
                @FieldResult(name = "alarmPlanTypeName", column = "alarm_plan_type_name"),
                @FieldResult(name = "formulaAcc", column = "formula_acc"),
                @FieldResult(name = "kpiType", column = "kpi_type"),
            })}
)
public class CatGraphKpiDTO implements Serializable {
//    @Id
    private Long id;

    private Long kpiId;

    private String kpiCode;

    private String kpiName;
    private String kpiNameCombo;

    private String kpiDisplay;

    private String unitKpi;

    private String unitViewCode;

    private String unitViewName;

    private Double rate;

    private String unitName;

    private String tableName;

    private Long status;

    private String groupKpiCode;

    private String groupKpiName;

    private String domainCode;

    private String domainName;

    private String source;

    private String description;

    private Instant updateTime;

    private String updateUser;

    private String formulaLevel;

    private String formulaQuar;

    private String formulaYear;

    private Long alarmThresholdType;

    private String alarmThresholdTypeName;

    private Long alarmPlanType;

    private String alarmPlanTypeName;

    private String formulaAcc;

    private Long kpiType;

    private String kpiTypeName;

    private List<String> synonym;

    private String synonyms;

    private Long position;

    private Integer showOnMap;

    private Integer isAlarm;

    private Long orderIndex;

    private Long kpiOriginId;

    public String getKpiNameCombo() {
        return kpiNameCombo;
    }

    public void setKpiNameCombo(String kpiNameCombo) {
        this.kpiNameCombo = kpiNameCombo;
    }

    private BieumauKehoachchitieu plan;

    public CatGraphKpiDTO() {
    }

    public CatGraphKpiDTO(Long kpiId, String kpiName, String domainCode) {
        this.kpiId = kpiId;
        this.kpiName = kpiName;
        this.domainCode = domainCode;
    }

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

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getSynonym() {
        return synonym;
    }

    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getKpiDisplay() {
        return kpiDisplay;
    }

    public void setKpiDisplay(String kpiDisplay) {
        this.kpiDisplay = kpiDisplay;
    }

    public String getUnitKpi() {
        return unitKpi;
    }

    public void setUnitKpi(String unitKpi) {
        this.unitKpi = unitKpi;
    }

    public String getUnitViewCode() {
        return unitViewCode;
    }

    public void setUnitViewCode(String unitViewCode) {
        this.unitViewCode = unitViewCode;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getGroupKpiCode() {
        return groupKpiCode;
    }

    public void setGroupKpiCode(String groupKpiCode) {
        this.groupKpiCode = groupKpiCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGroupKpiName() {
        return groupKpiName;
    }

    public void setGroupKpiName(String groupKpiName) {
        this.groupKpiName = groupKpiName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public BieumauKehoachchitieu getPlan() {
        return plan;
    }

    public void setPlan(BieumauKehoachchitieu plan) {
        this.plan = plan;
    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatGraphKpiDTO catGraphKpiDTO = (CatGraphKpiDTO) o;
        if (catGraphKpiDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), catGraphKpiDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CatGraphKpiDTO{" +
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

    public String getUnitViewName() {
        return unitViewName;
    }

    public void setUnitViewName(String unitViewName) {
        this.unitViewName = unitViewName;
    }

    public String getKpiTypeName() {
        return kpiTypeName;
    }

    public void setKpiTypeName(String kpiTypeName) {
        this.kpiTypeName = kpiTypeName;
    }

    public String getAlarmThresholdTypeName() {
        return alarmThresholdTypeName;
    }

    public void setAlarmThresholdTypeName(String alarmThresholdTypeName) {
        this.alarmThresholdTypeName = alarmThresholdTypeName;
    }

    public String getAlarmPlanTypeName() {
        return alarmPlanTypeName;
    }

    public void setAlarmPlanTypeName(String alarmPlanTypeName) {
        this.alarmPlanTypeName = alarmPlanTypeName;
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
}
