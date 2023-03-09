package com.b4t.app.service.dto;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartParamDTO implements Serializable {
    private List<String> kpiIds;
    private String timeType;
    private String inputLevel;
    private String fromDate;
    private String toDate;
    private String prdId;
    private String objectCode;
    private String tableName;
    private String xAxis;
    private String yAxis;
    private String legend;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (!DataUtil.isNullOrEmpty(kpiIds))
            map.put(Constants.KPI_IDS_PARAM, kpiIds);
        if (StringUtils.isNotEmpty(timeType))
            map.put(Constants.TIME_TYPE_PARAM, timeType);
        if (StringUtils.isNotEmpty(inputLevel))
            map.put(Constants.INPUT_LEVEL_PARAM, inputLevel);
        if (StringUtils.isNotEmpty(prdId))
            map.put(Constants.PRD_ID_PARAM, prdId);
        if (StringUtils.isNotEmpty(fromDate))
            map.put(Constants.FROM_DATE_PARAM, fromDate);
        if (StringUtils.isNotEmpty(toDate))
            map.put(Constants.TO_DATE_PARAM, toDate);
        if (StringUtils.isNotEmpty(objectCode))
            map.put(Constants.OBJECT_CODE, objectCode);
        if (StringUtils.isNotEmpty(tableName))
            map.put(Constants.TABLE_NAME, tableName);
        if (StringUtils.isNotEmpty(xAxis))
            map.put(Constants.X_AXIS_PARAM, xAxis);
        if (StringUtils.isNotEmpty(yAxis))
            map.put(Constants.Y_AXIS_PARAM, yAxis);
        if (StringUtils.isNotEmpty(legend))
            map.put(Constants.LEGEND_PARAM, legend);
        return map;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public List<String> getKpiIds() {
        return kpiIds;
    }

    public void setKpiIds(List<String> kpiIds) {
        this.kpiIds = kpiIds;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getInputLevel() {
        return inputLevel;
    }

    public void setInputLevel(String inputLevel) {
        this.inputLevel = inputLevel;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getPrdId() {
        return prdId;
    }

    public void setPrdId(String prdId) {
        this.prdId = prdId;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
