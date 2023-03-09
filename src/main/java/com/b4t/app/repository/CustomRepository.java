package com.b4t.app.repository;

import com.b4t.app.service.dto.CatItemDTO;

import java.util.Date;
import java.util.List;

public interface CustomRepository {

    List<CatItemDTO> getTimeTypeByKpis(Long[] kpiIds);

    boolean checkHasData(String sql, Date date);

    boolean checkQueryData(String sql, Date date);
}
