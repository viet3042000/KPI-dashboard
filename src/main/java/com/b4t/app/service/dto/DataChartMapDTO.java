package com.b4t.app.service.dto;

import com.b4t.app.commons.DataUtil;

public class DataChartMapDTO {
    private String objetCode;
    private String objectName;
    private Long kpiId;
    private String kpiName;
    private Double value;
    private String xAxis;
    private String unitName;
    private Long prdId;
    public DataChartMapDTO() {
    }

    public DataChartMapDTO(String objetCode, String objectName, Long kpiId, String kpiName, Double value, String xAxis, String unitName) {
        this.objetCode = objetCode;
        this.objectName = objectName;
        this.kpiId = kpiId;
        this.kpiName = kpiName;
        this.value = value;
        this.xAxis = xAxis;
        this.unitName = unitName;
    }

    public DataChartMapDTO(String objetCode, String objectName, Long kpiId, String kpiName, Double value, String xAxis, String unitName, Long prdId) {
        this.objetCode = objetCode;
        this.objectName = objectName;
        this.kpiId = kpiId;
        this.kpiName = kpiName;
        this.value = value;
        this.xAxis = xAxis;
        this.unitName = unitName;
        this.prdId = prdId;
    }

    public DataChartMapDTO(Object[] objects) {
        int i = 0;
        this.objetCode = DataUtil.safeToString(objects[i++]);
        this.objectName = DataUtil.safeToString(objects[i++]);
        this.kpiId = DataUtil.safeToLong(objects[i++]);
        this.kpiName = DataUtil.safeToString(objects[i++]);
        this.value = DataUtil.safeToDouble(objects[i++]);
        this.prdId = DataUtil.safeToLong(objects[i++]);
        this.unitName = DataUtil.safeToString(objects[i]);
    }

    public String getObjetCode() {
        return objetCode;
    }

    public void setObjetCode(String objetCode) {
        this.objetCode = objetCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }
}
