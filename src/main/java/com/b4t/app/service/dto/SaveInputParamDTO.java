package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class SaveInputParamDTO implements Serializable {
    private String paramName;
    private String fieldName;
    private String value;
    private String valueDefault;
    private String operator;
    private Boolean isFilterParam;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValueDefault() {
        return valueDefault;
    }

    public void setValueDefault(String valueDefault) {
        this.valueDefault = valueDefault;
    }

    public Boolean getFilterParam() {
        return isFilterParam;
    }

    public void setFilterParam(Boolean filterParam) {
        isFilterParam = filterParam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramName, fieldName, value, valueDefault, operator, isFilterParam);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaveInputParamDTO that = (SaveInputParamDTO) o;
        return Objects.equals(getValue(), that.getValue())
            && Objects.equals(getValueDefault(), that.getValueDefault())
            && Objects.equals(getFieldName(), that.getFieldName())
            && Objects.equals(getOperator(), that.getOperator())
            && Objects.equals(getParamName(), that.getParamName());
    }
}
