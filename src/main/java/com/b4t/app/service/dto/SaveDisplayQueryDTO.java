package com.b4t.app.service.dto;

import java.util.ArrayList;
import java.util.List;

public class SaveDisplayQueryDTO extends ConfigDisplayQueryDTO {
    public SaveDisplayQueryDTO() {
        values = new ArrayList<>();
    }

    public SaveDisplayQueryDTO(ConfigDisplayQueryDTO dto) {
        this.setItemChartId(dto.getItemChartId());
        this.setId(dto.getId());
        this.setUpdateTime(dto.getUpdateTime());
        this.setUpdateUser(dto.getUpdateUser());
        this.setColumnChart(dto.getColumnChart());
        this.setColumnQuery(dto.getColumnQuery());
        this.setDataType(dto.getDataType());
        this.setDescription(dto.getDescription());
        this.setIsRequire(dto.getIsRequire());
        this.setStatus(dto.getStatus());
    }

    public ConfigDisplayQueryDTO toDto() {
        ConfigDisplayQueryDTO dto = new ConfigDisplayQueryDTO();
        dto.setItemChartId(this.getItemChartId());
        dto.setId(this.getId());
        dto.setUpdateTime(this.getUpdateTime());
        dto.setUpdateUser(this.getUpdateUser());
        dto.setColumnChart(this.getColumnChart());
        dto.setColumnQuery(this.getColumnQuery());
        dto.setDataType(this.getDataType());
        dto.setDescription(this.getDescription());
        dto.setIsRequire(this.getIsRequire());
        dto.setStatus(this.getStatus());
        return dto;
    }

    private String tableName;

    private String fieldSql;

    private List<SaveDisplayQueryValueDTO> values;

    public String getFieldSql() {
        return fieldSql;
    }

    public void setFieldSql(String fieldSql) {
        this.fieldSql = fieldSql;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SaveDisplayQueryValueDTO> getValues() {
        return values;
    }

    public void setValues(List<SaveDisplayQueryValueDTO> values) {
        this.values = values;
    }
}
