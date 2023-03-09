package com.b4t.app.service.dto;

import com.b4t.app.commons.DataUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigChartDetailDTO {
    private String queryId;
    private String queryData;
    private String defaultValue;
    @JsonIgnore
    private String inputCondition;
    private JsonNode inputConditionJson;
    private Integer timeTypeDefault;
    private String columnQuery;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ConfigChartDetailDTO.class);

    public ConfigChartDetailDTO() {
    }

    public JsonNode getInputConditionJson() {
        if(!DataUtil.isNullOrEmpty(inputCondition)) {
            try {
                inputConditionJson = mapper.readTree(inputCondition);
            }catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return inputConditionJson;
    }

    public void setInputConditionJson(JsonNode inputConditionJson) {
        this.inputConditionJson = inputConditionJson;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryData() {
        return queryData;
    }

    public void setQueryData(String queryData) {
        this.queryData = queryData;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getInputCondition() {
        return inputCondition;
    }

    public void setInputCondition(String inputCondition) {
        this.inputCondition = inputCondition;
    }

    public Integer getTimeTypeDefault() {
        return timeTypeDefault;
    }

    public void setTimeTypeDefault(Integer timeTypeDefault) {
        this.timeTypeDefault = timeTypeDefault;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }
}
