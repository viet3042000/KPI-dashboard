package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ConfigReportDataImport {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> headers;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<List<String>> lstData;

    @NotNull(message = "{error.configReportDataImport.mapDataNull}")
    private List<Map<String, String>> mapData;

    public List<Map<String, String>> getMapData() {
        return mapData;
    }

    public void setMapData(List<Map<String, String>> mapData) {
        this.mapData = mapData;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getLstData() {
        return lstData;
    }

    public void setLstData(List<List<String>> lstData) {
        this.lstData = lstData;
    }
}
