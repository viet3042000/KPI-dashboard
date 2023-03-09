package com.b4t.app.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SaveChartItemDTO extends ConfigChartItemDTO implements Serializable {

    public SaveChartItemDTO() {
        params = new ArrayList<>();
        kpiInfos = new ArrayList<>();
        orderBys = new ArrayList<>();
        columns = new ArrayList<>();
        outColumns = new ArrayList<>();
    }

    public SaveChartItemDTO(ConfigChartItemDTO dto) throws JsonProcessingException {
        this.setId(dto.getId());
        this.setChartId(dto.getChartId());
        this.setCondition1(dto.getCondition1());
        this.setCondition2(dto.getCondition2());
        this.setCondition3(dto.getCondition3());
        this.setCondition4(dto.getCondition4());
        this.setCondition5(dto.getCondition5());
        this.setDescription(dto.getDescription());
        this.setHasAvgLine(dto.getHasAvgLine());
        this.setListColor(dto.getListColor());
        this.setOrderIndex(dto.getOrderIndex());
        this.setQueryId(dto.getQueryId());
        this.setStatus(dto.getStatus());
        this.setTypeChart(dto.getTypeChart());
        this.setUpdateTime(dto.getUpdateTime());
        this.setUpdateUser(dto.getUpdateUser());
        this.setDisplayConfigs(dto.getDisplayConfigs());

        if (StringUtils.isNotEmpty(dto.getInputCondition())) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            SaveInputConditionDTO inputConditionObj = mapper.readValue(dto.getInputCondition(), SaveInputConditionDTO.class);
            this.customizeSql = inputConditionObj.getCustomizeSql();
            this.kpiInfos = inputConditionObj.getKpiInfos();
            this.params = inputConditionObj.getParams();
            this.limit = inputConditionObj.getLimit();
            this.orderBys = inputConditionObj.getOrderBys();
            this.columns = inputConditionObj.getColumns();
            this.joinCatGraphKpi = inputConditionObj.getJoinCatGraphKpi();
        }
    }

    public ConfigChartItemDTO toDto() throws JsonProcessingException {
        ConfigChartItemDTO dto = new ConfigChartItemDTO();
        dto.setId(this.getId());
        dto.setChartId(this.getChartId());
        dto.setCondition1(this.getCondition1());
        dto.setCondition2(this.getCondition2());
        dto.setCondition3(this.getCondition3());
        dto.setCondition4(this.getCondition4());
        dto.setCondition5(this.getCondition5());
        dto.setDescription(this.getDescription());
        dto.setHasAvgLine(this.getHasAvgLine() == null ? 0 : this.getHasAvgLine());
        dto.setListColor(this.getListColor());
        dto.setOrderIndex(this.getOrderIndex());
        dto.setQueryId(this.getQueryId());
        dto.setStatus(this.getStatus());
        dto.setTypeChart(this.getTypeChart());
        dto.setUpdateTime(this.getUpdateTime());
        dto.setUpdateUser(this.getUpdateUser());
        SaveInputConditionDTO inputConditionObj = new SaveInputConditionDTO();
        inputConditionObj.setCustomizeSql(this.getCustomizeSql());
        inputConditionObj.setKpiInfos(this.getKpiInfos());
        inputConditionObj.setParams(this.getParams());
        inputConditionObj.setLimit(this.getLimit());
        inputConditionObj.setOrderBys(this.getOrderBys());
        inputConditionObj.setColumns(this.getColumns());
        inputConditionObj.setJoinCatGraphKpi(this.getJoinCatGraphKpi());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        dto.setInputCondition(mapper.writeValueAsString(inputConditionObj));
        return dto;
    }

    private String customizeSql;
    private String customizeMaxPrdIdSql;
    private Boolean joinCatGraphKpi;
    private List<SaveKpiInfoDTO> kpiInfos;
    private List<SaveInputParamDTO> params;
    private String limit;
    private List<SaveOrderByDTO> orderBys;
    private List<SaveDisplayQueryDTO> columns;
    private List<SaveDisplayQueryDTO> outColumns;
    private List<Map<String, Object>> allColumns;

    public Boolean getJoinCatGraphKpi() {
        return joinCatGraphKpi;
    }

    public void setJoinCatGraphKpi(Boolean joinCatGraphKpi) {
        this.joinCatGraphKpi = joinCatGraphKpi;
    }

    public List<SaveInputParamDTO> getParams() {
        return params;
    }

    public void setParams(List<SaveInputParamDTO> params) {
        this.params = params;
    }

    public List<SaveKpiInfoDTO> getKpiInfos() {
        return kpiInfos;
    }

    public void setKpiInfos(List<SaveKpiInfoDTO> kpiInfos) {
        this.kpiInfos = kpiInfos;
    }

    public String getCustomizeSql() {
        return customizeSql;
    }

    public void setCustomizeSql(String customizeSql) {
        this.customizeSql = customizeSql;
    }

    public String getCustomizeMaxPrdIdSql() {
        return customizeMaxPrdIdSql;
    }

    public void setCustomizeMaxPrdIdSql(String customizeMaxPrdIdSql) {
        this.customizeMaxPrdIdSql = customizeMaxPrdIdSql;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public List<SaveOrderByDTO> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<SaveOrderByDTO> orderBys) {
        this.orderBys = orderBys;
    }

    public List<SaveDisplayQueryDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<SaveDisplayQueryDTO> columns) {
        this.columns = columns;
    }

    public List<SaveDisplayQueryDTO> getOutColumns() {
        return outColumns;
    }

    public void setOutColumns(List<SaveDisplayQueryDTO> outColumns) {
        this.outColumns = outColumns;
    }

    public List<Map<String, Object>> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<Map<String, Object>> allColumns) {
        this.allColumns = allColumns;
    }

    public boolean canMerge(SaveChartItemDTO o) {
        if (this.equals(o)) {
            return true;
        }
        List<Long> thisKpiIds = this.kpiInfos.stream()
            .map(SaveKpiInfoDTO::getKpis).flatMap(List::stream)
            .map(CatGraphKpiDTO::getKpiId)
            .sorted()
            .collect(Collectors.toList());
        List<Long> thatKpiIds = o.getKpiInfos().stream()
            .map(SaveKpiInfoDTO::getKpis).flatMap(List::stream)
            .map(CatGraphKpiDTO::getKpiId)
            .sorted()
            .collect(Collectors.toList());
        if (StringUtils.isNotEmpty(this.customizeSql) && StringUtils.isNotEmpty(o.getCustomizeSql()))
            return this.getCustomizeSql().trim().equals(o.getCustomizeSql().trim());

        if (StringUtils.equals(this.limit, o.getLimit()))
            if (this.getJoinCatGraphKpi() == o.getJoinCatGraphKpi())
                if (thisKpiIds.containsAll(thatKpiIds) && thisKpiIds.size() == thatKpiIds.size())
                    if (this.params.containsAll(o.getParams()) && this.params.size() == o.getParams().size())
                        return this.orderBys.containsAll(o.getOrderBys()) && this.orderBys.size() == o.getOrderBys().size();
        return false;
    }
}

class SaveInputConditionDTO {

    private String customizeSql;
    private Boolean joinCatGraphKpi;
    private List<SaveKpiInfoDTO> kpiInfos;
    private List<SaveInputParamDTO> params;
    private String limit;
    private List<SaveOrderByDTO> orderBys;
    private List<SaveDisplayQueryDTO> columns;

    public String getCustomizeSql() {
        return customizeSql;
    }

    public void setCustomizeSql(String customizeSql) {
        this.customizeSql = customizeSql;
    }

    public Boolean getJoinCatGraphKpi() {
        return joinCatGraphKpi;
    }

    public void setJoinCatGraphKpi(Boolean joinCatGraphKpi) {
        this.joinCatGraphKpi = joinCatGraphKpi;
    }

    public List<SaveKpiInfoDTO> getKpiInfos() {
        return kpiInfos;
    }

    public void setKpiInfos(List<SaveKpiInfoDTO> kpiInfos) {
        this.kpiInfos = kpiInfos;
    }

    public List<SaveInputParamDTO> getParams() {
        return params;
    }

    public void setParams(List<SaveInputParamDTO> params) {
        this.params = params;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public List<SaveOrderByDTO> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<SaveOrderByDTO> orderBys) {
        this.orderBys = orderBys;
    }

    public List<SaveDisplayQueryDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<SaveDisplayQueryDTO> columns) {
        this.columns = columns;
    }

}
