package com.b4t.app.service.dto;

public class BaseRptTooltip {
    private String valueSuffix;

    public BaseRptTooltip() {
    }

    public BaseRptTooltip(String valueSuffix) {
        this.valueSuffix = valueSuffix;
    }

    public String getValueSuffix() {
        return valueSuffix;
    }

    public void setValueSuffix(String valueSuffix) {
        this.valueSuffix = valueSuffix;
    }
}
