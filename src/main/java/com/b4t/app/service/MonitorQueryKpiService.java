package com.b4t.app.service;

import com.b4t.app.domain.MonitorQueryKpi;
import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.dto.MonitorQueryForm;
import com.b4t.app.service.dto.MonitorQueryKpiDTO;

import com.b4t.app.service.dto.MonitorQueryKpiDetailDTO;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.MonitorQueryKpi}.
 */
public interface MonitorQueryKpiService {

    /**
     * Save a monitorQueryKpi.
     *
     * @param monitorQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    MonitorQueryKpiDTO save(MonitorQueryKpiDTO monitorQueryKpiDTO);

    /**
     * Get all the monitorQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MonitorQueryKpiDTO> findAll(Pageable pageable);


    /**
     * Get the "id" monitorQueryKpi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MonitorQueryKpiDTO> findOne(Long id);

    /**
     * Delete the "id" monitorQueryKpi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<MonitorQueryKpiDetailDTO> findMonitorQueryKpiByKey(MonitorQueryForm monitorQueryForm, Pageable pageable);

    /**
     * Update run_time_report
     * @param queryId
     * @param prdId
     * @param timeType
     * @throws Exception
     */
    void updateRunTimeReport(Integer queryId, Integer prdId, Integer timeType) throws Exception;

    /**
     * Clear run_time_report
     * @param queryId
     * @throws Exception
     */
    void clearRunTimeReport(Integer queryId) throws Exception;

    void restartMonitorQueryKpi(MonitorQueryKpiDetailDTO monitorQueryKpiDetailDTO) throws Exception;


    List<ComboDTO> getMonitorTemplate();

    List<ComboDTO> getKpiByTemplate(String template);

    void deleteMultiple(List<MonitorQueryKpiDTO> monitorQueryKpis);

    Optional<MonitorQueryKpiDTO> findByQueryKpiId(Integer queryId);
}
