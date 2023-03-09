package com.b4t.app.repository;

import com.b4t.app.domain.RptGraph;
import com.b4t.app.service.dto.CatGrapKpiExtendDTO;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.SaveKpiInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatGraphKpiCustomRepository {

    Page<CatGraphKpiDTO> findAll(String keyword, List<Long> kpiIds, Pageable pageable);

    List<CatGraphKpiDTO> getListCatGraphKpi(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType);

    List<CatGraphKpiDTO> getListKpiForMaps(CatGrapKpiExtendDTO catGrapKpiExtendDTO);

    Page<RptGraph> getDstDataByCatGraphKpi(CatGraphKpiDTO catGraphKpiDTO, Pageable pageable);

    void deleteRptGraphById(Long id , String tableName);

    void deleteRptGraphByKpiId(Long kpiId , String tableName);
}
