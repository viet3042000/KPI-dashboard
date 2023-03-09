package com.b4t.app.service;

import com.b4t.app.domain.ConfigQueryKpi;
import com.b4t.app.service.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.ConfigQueryKpi}.
 */
public interface ConfigQueryKpiService {

    /**
     * Save a configQueryKpi.
     *
     * @param configQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigQueryKpiDTO save(ConfigQueryKpiDTO configQueryKpiDTO);

    /**
     * Them moi cau hinh
     * @param configQueryKpiDTO
     * @param actor
     * @return
     * @throws Exception
     */
    ConfigQueryKpiDetailDTO save(ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor) throws Exception;

    void checkQueryData(String query) throws Exception;


    /**
     * Cap nhat cau hinh
     * @param configQueryKpiDTO
     * @param actor
     * @return
     * @throws Exception
     */
    ConfigQueryKpiDetailDTO update(ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor) throws Exception;

    /**
     * Get all the configQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigQueryKpiDTO> findAll(Pageable pageable);

    /**
     * Tim kiem cau hinh
     * @param searchForm
     * @param pageable
     * @return
     */
    Page<ConfigQueryKpiResultDTO> findConfigQueryKpis(ConfigQueryKpiSearch searchForm, Pageable pageable);


    /**
     * Get the "id" configQueryKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigQueryKpiDTO> findOne(Long id);

    /**
     * Lay thong tin chi tiet 1 cau hinh
     * @param id
     * @return
     */
    Optional<ConfigQueryKpiDetailDTO> findConfigById(Long id);

    /**
     * Delete the "id" configQueryKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Sao chep cau hinh
     * @param id
     */
    void clone(Long id) throws Exception;

    /**
     * Lay danh sach cac KPI tong hop
     * @param kpiId
     * @param pageable
     * @return
     */
    Page<ComboDTO> findAllKpiOutput(String kpiId, Pageable pageable);

    /**
     * Lay danh sach cac KPI Dau vao
     * @param kpiId
     * @param pageable
     * @return
     */
    Page<ComboDTO> findAllKpiInput(String kpiId, Pageable pageable);

    /**
     * Lay danh sach cac bang nguon
     * @param lstTableSource
     * @return
     */
    List<ComboDTO> findAllKpiSourceTable(List<String> lstTableSource);

    /**
     * Lay danh sach cac cot mapping KPI, column
     * @param queryId
     * @return
     */
    List<ConfigQueryKpiColumn> findAllColumnByQueryId(Integer queryId);

    List<ConfigQueryKpiDTO> findAllByReportId(Long reportId);

}
