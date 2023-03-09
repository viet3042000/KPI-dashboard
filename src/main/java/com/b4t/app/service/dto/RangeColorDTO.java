package com.b4t.app.service.dto;

import com.b4t.app.commons.DataUtil;

public class RangeColorDTO {
    private String name;
    private Long kpiId;
    private String colorCode;
    private Long totalLevel;
    private Long classLevel;
    private Double fromValue;
    private Double toValue;

    public RangeColorDTO(Object[] objects) {
        int i = 0;
        this.name = DataUtil.safeToString(objects[i++]);
        this.kpiId = DataUtil.safeToLong(objects[i++]);
        this.colorCode = DataUtil.safeToString(objects[i++]);
        this.totalLevel = DataUtil.safeToLong(objects[i++]);
        this.classLevel = DataUtil.safeToLong(objects[i++]);
        this.fromValue = DataUtil.safeToDouble(objects[i++]);
        this.toValue = DataUtil.safeToDouble(objects[i]);
    }

    public RangeColorDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(Long totalLevel) {
        this.totalLevel = totalLevel;
    }

    public Long getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Long classLevel) {
        this.classLevel = classLevel;
    }

    public Double getFromValue() {
        return fromValue;
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }
}
