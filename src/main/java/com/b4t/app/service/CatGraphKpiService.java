package com.b4t.app.service;

import com.b4t.app.domain.RptGraph;
import com.b4t.app.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.CatGraphKpi}.
 */
public interface CatGraphKpiService {

    /**
     * Save a catGraphKpi.
     *
     * @param catGraphKpiDTO the entity to save.
     * @return the persisted entity.
     */
    CatGraphKpiDTO save(CatGraphKpiDTO catGraphKpiDTO);

    /**
     * Get all the catGraphKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatGraphKpiDTO> findAll(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType, Integer showOnMap, Long status, Pageable pageable);

    List<CatGraphKpiDTO> findAll(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType, Long status);

    Page<CatGraphKpiDTO> findAll(String keyword, List<Long> kpiIds, Pageable pageable);

    List<CatGraphKpiDTO> getListKpiForMaps(CatGrapKpiExtendDTO catGrapKpiExtendDTO);

    /**
     * Get the "id" catGraphKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatGraphKpiDTO> findOne(Long id);

    Optional<CatGraphKpiDTO> findByCode(String code);

    Optional<CatGraphKpiDTO> findByKpiId(Long kpiId);

    List<CatGraphKpiDTO> findByKpiIds(List<Long> kpiIds);

    /**
     * Delete the "id" catGraphKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CatGraphKpiDTO> getKpiMain(String groupKpiCode, String domainCode);

    List<CatGraphKpiDTO> getGraphKpiByDomain(String domainCode);

    List<ComboDTO> onSearchHashTag(String keyword);

    Page<CatGraphKpiDetailDTO> onSearch(CatGraphKpiDetailDTO catGraphKpiDetailDTO, Pageable pageable);

    List<CatGraphKpiDetailDTO> onExport(CatGraphKpiDetailDTO catGraphKpiDetailDTO);

    List<CatGraphKpiDTO> getAllCatGraphKpi();

    Long getMaxKpiId();

    Page<RptGraphDTO> getDstDataByCatGraphKpi(CatGraphKpiDTO catGraphKpiDTO, Pageable pageable);

    void setKpiIdForNewKpi(Long id);

    void deleteRptGraphById(Long id, String tableName);

    void deleteRptGraphByKpiId(Long kpiId, String tableName);
}
