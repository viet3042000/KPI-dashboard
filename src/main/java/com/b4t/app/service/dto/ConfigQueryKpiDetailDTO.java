package com.b4t.app.service.dto;

import java.util.List;

public class ConfigQueryKpiDetailDTO {
    private ConfigQueryKpiForm configQueryKpi;
    private List<ConfigQueryKpiColumn> configQueryColum;

    public ConfigQueryKpiForm getConfigQueryKpi() {
        return configQueryKpi;
    }

    public void setConfigQueryKpi(ConfigQueryKpiForm configQueryKpi) {
        this.configQueryKpi = configQueryKpi;
    }

    public List<ConfigQueryKpiColumn> getConfigQueryColum() {
        return configQueryColum;
    }

    public void setConfigQueryColum(List<ConfigQueryKpiColumn> configQueryColum) {
        this.configQueryColum = configQueryColum;
    }
}
