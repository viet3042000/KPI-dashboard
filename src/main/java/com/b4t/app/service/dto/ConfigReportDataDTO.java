package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigReportDataDTO {
    List<ConfigReportColumnDTO> lstColumn;
    @JsonIgnore
    Page<Object> pageObj;
    List<Object> lstObj;
    Map<String, Object> mapRef;

    public Map<String, Object> getMapRef() {
        return mapRef;
    }

    public void setMapRef(Map<String, Object> mapRef) {
        this.mapRef = mapRef;
    }

    public List<ConfigReportColumnDTO> getLstColumn() {
        return lstColumn;
    }

    public void setLstColumn(List<ConfigReportColumnDTO> lstColumn) {
        this.lstColumn = lstColumn;
    }

    public Page<Object> getPageObj() {
        return pageObj;
    }

    public void setPageObj(Page<Object> pageObj) {
        this.pageObj = pageObj;
    }

    public List<Object> getLstObj() {
        return lstObj;
    }

    public void setLstObj(List<Object> lstObj) {
        this.lstObj = lstObj;
    }
}
