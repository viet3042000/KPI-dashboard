package com.b4t.app.service;

import com.b4t.app.service.dto.CatItemDTO;

import java.util.List;

public interface CustomService {

    List<CatItemDTO> getTimeTypeByKpis(Long[] kpiIds);
}
