package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class BaseRptSeries {
    private String type;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BaseRptDataPie> dataPie;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stack;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BaseRptTooltip tooltip;

    public BaseRptSeries() {
    }

    public List<BaseRptDataPie> getDataPie() {
        return dataPie;
    }

    public void setDataPie(List<BaseRptDataPie> dataPie) {
        this.dataPie = dataPie;
    }

    public BaseRptTooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(BaseRptTooltip tooltip) {
        this.tooltip = tooltip;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
