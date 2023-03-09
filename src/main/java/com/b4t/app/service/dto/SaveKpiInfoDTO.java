package com.b4t.app.service.dto;

import java.io.Serializable;
import java.util.List;

public class SaveKpiInfoDTO implements Serializable {
    private List<CatGraphKpiDTO> kpis;
    private String tableName;

    public SaveKpiInfoDTO() {}

    public SaveKpiInfoDTO(String tableName, List<CatGraphKpiDTO> kpis) {
        this.tableName = tableName;
        this.kpis = kpis;
    }

    public List<CatGraphKpiDTO> getKpis() {
        return kpis;
    }

    public void setKpis(List<CatGraphKpiDTO> kpis) {
        this.kpis = kpis;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
