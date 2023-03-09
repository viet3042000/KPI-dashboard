package com.b4t.app.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComboDTO {
    private String label;
    private Object value;
    private Object status;
    private String icon;

    public ComboDTO() {
    }

    public ComboDTO(Object value, String label) {
        this.label = label;
        this.value = value;
    }

    public ComboDTO(Object value, String label, String icon) {
        this.label = label;
        this.value = value;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
