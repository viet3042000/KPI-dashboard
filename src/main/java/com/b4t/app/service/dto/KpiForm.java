package com.b4t.app.service.dto;

import java.util.List;

public class KpiForm {
    private List<Integer> lstKpi;

    public KpiForm() {
    }

    public List<Integer> getLstKpi() {
        return lstKpi;
    }

    public void setLstKpi(List<Integer> lstKpi) {
        this.lstKpi = lstKpi;
    }
}
