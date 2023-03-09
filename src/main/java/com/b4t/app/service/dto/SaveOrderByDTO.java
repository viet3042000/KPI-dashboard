package com.b4t.app.service.dto;

import java.util.Objects;

public class SaveOrderByDTO {
    private String value;
    private String label;
    private String type;
    private String sortDir;
    private String function;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, label, type, sortDir, function);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaveOrderByDTO that = (SaveOrderByDTO) o;
        return Objects.equals(getValue(), that.getValue())
            && Objects.equals(getSortDir(), that.getSortDir())
            && Objects.equals(getFunction(), that.getFunction())
            && Objects.equals(getType(), that.getType());
    }
}
