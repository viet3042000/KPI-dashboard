package com.b4t.app.service.dto;

public class TreeValue {
    private String id;
    private String des;
    private String unit;

    public TreeValue() {
    }

    public TreeValue(String id) {
        this.id = id;
    }

    public TreeValue(String id, String des) {
        this.id = id;
        this.des = des;
    }

    public TreeValue(String id, String des, String unit) {
        this.id = id;
        this.des = des;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
