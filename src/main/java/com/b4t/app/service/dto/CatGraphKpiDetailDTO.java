package com.b4t.app.service.dto;

import java.time.Instant;
import java.util.List;

public class CatGraphKpiDetailDTO {
    private Long id;
    private Long kpiId;
    private String kpiCode;
    private String kpiName;
    private String kpiDisplay;
    private String unitKpi;
    private String unitName;
    private String unitViewCode;
    private String unitViewName;
    private Double rate;
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
    private String synonyms;
    private String keySearch;
    private List<String> synonym;
    private Long kpiOriginId;
    private String kpiOriginName;

    public String getAlarmPlanTypeName() {
        return alarmPlanTypeName;
    }

    public void setAlarmPlanTypeName(String alarmPlanTypeName) {
        this.alarmPlanTypeName = alarmPlanTypeName;
    }

    public String getAlarmThresholdTypeName() {
        return alarmThresholdTypeName;
    }

    public void setAlarmThresholdTypeName(String alarmThresholdTypeName) {
        this.alarmThresholdTypeName = alarmThresholdTypeName;
    }

    public String getKpiTypeName() {
        return kpiTypeName;
    }

    public void setKpiTypeName(String kpiTypeName) {
        this.kpiTypeName = kpiTypeName;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitViewCode() {
        return unitViewCode;
    }

    public void setUnitViewCode(String unitViewCode) {
        this.unitViewCode = unitViewCode;
    }

    public String getUnitViewName() {
        return unitViewName;
    }

    public void setUnitViewName(String unitViewName) {
        this.unitViewName = unitViewName;
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

    public String getGroupKpiName() {
        return groupKpiName;
    }

    public void setGroupKpiName(String groupKpiName) {
        this.groupKpiName = groupKpiName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
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

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public Long getKpiOriginId() {
        return kpiOriginId;
    }

    public void setKpiOriginId(Long kpiOriginId) {
        this.kpiOriginId = kpiOriginId;
    }

    public String getKpiOriginName() {
        return kpiOriginName;
    }

    public void setKpiOriginName(String kpiOriginName) {
        this.kpiOriginName = kpiOriginName;
    }
}
