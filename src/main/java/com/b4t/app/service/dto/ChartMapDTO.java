package com.b4t.app.service.dto;

import java.util.List;

public class ChartMapDTO {
    List<DataChartMapDTO> lstData;
    List<ClusterDTO> lstCuster;

    public ChartMapDTO(List<DataChartMapDTO> lstData, List<ClusterDTO> lstCuster) {
        this.lstData = lstData;
        this.lstCuster = lstCuster;
    }

    public ChartMapDTO() {
    }

    public List<DataChartMapDTO> getLstData() {
        return lstData;
    }

    public void setLstData(List<DataChartMapDTO> lstData) {
        this.lstData = lstData;
    }

    public List<ClusterDTO> getLstCuster() {
        return lstCuster;
    }

    public void setLstCuster(List<ClusterDTO> lstCuster) {
        this.lstCuster = lstCuster;
    }
}
