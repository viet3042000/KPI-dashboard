package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigQueryKpiService;
import com.b4t.app.service.MapReportDataToDashboardService;
import com.b4t.app.domain.MapReportDataToDashboard;
import com.b4t.app.repository.MapReportDataToDashboardRepository;
import com.b4t.app.service.MonitorQueryKpiService;
import com.b4t.app.service.RpReportService;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.MapReportDataToDashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MapReportDataToDashboard}.
 */
@Service
@Transactional
public class MapReportDataToDashboardServiceImpl implements MapReportDataToDashboardService {

    private final Logger log = LoggerFactory.getLogger(MapReportDataToDashboardServiceImpl.class);

    private final MapReportDataToDashboardRepository mapReportDataToDashboardRepository;

    private final MapReportDataToDashboardMapper mapReportDataToDashboardMapper;
    private final RpReportService rpReportService;
    private final MonitorQueryKpiService monitorQueryKpiService;
    private final ConfigQueryKpiService configQueryKpiService;

    public MapReportDataToDashboardServiceImpl(MapReportDataToDashboardRepository mapReportDataToDashboardRepository, MapReportDataToDashboardMapper mapReportDataToDashboardMapper, RpReportService rpReportService, MonitorQueryKpiService monitorQueryKpiService, ConfigQueryKpiService configQueryKpiService) {
        this.mapReportDataToDashboardRepository = mapReportDataToDashboardRepository;
        this.mapReportDataToDashboardMapper = mapReportDataToDashboardMapper;
        this.rpReportService = rpReportService;
        this.monitorQueryKpiService = monitorQueryKpiService;
        this.configQueryKpiService = configQueryKpiService;
    }

    /**
     * Save a mapReportDataToDashboard.
     *
     * @param mapReportDataToDashboardDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MapReportDataToDashboardDTO save(MapReportDataToDashboardDTO mapReportDataToDashboardDTO) {
        log.debug("Request to save MapReportDataToDashboard : {}", mapReportDataToDashboardDTO);
        MapReportDataToDashboard mapReportDataToDashboard = mapReportDataToDashboardMapper.toEntity(mapReportDataToDashboardDTO);
        mapReportDataToDashboard = mapReportDataToDashboardRepository.save(mapReportDataToDashboard);
        return mapReportDataToDashboardMapper.toDto(mapReportDataToDashboard);
    }

    /**
     * Get all the mapReportDataToDashboards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MapReportDataToDashboardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MapReportDataToDashboards");
        return mapReportDataToDashboardRepository.findAll(pageable)
            .map(mapReportDataToDashboardMapper::toDto);
    }


    /**
     * Get one mapReportDataToDashboard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MapReportDataToDashboardDTO> findOne(Long id) {
        log.debug("Request to get MapReportDataToDashboard : {}", id);
        return mapReportDataToDashboardRepository.findById(id)
            .map(mapReportDataToDashboardMapper::toDto);
    }

    /**
     * Delete the mapReportDataToDashboard by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MapReportDataToDashboard : {}", id);
        mapReportDataToDashboardRepository.deleteById(id);
    }

    @Override
    public void synDataToDashboard(SyncDataToDashboard syncDataToDashboard) {
        List<MapReportDataToDashboardDTO> lstReportDatas = syncDataToDashboard.getLstReportDatas();
        if (DataUtil.isNullOrEmpty(lstReportDatas)) return;
        Optional<RpReportDTO> rpReportDTO = rpReportService.findOne(syncDataToDashboard.getReportDTO().getId());
        RpReportDTO rpReport;
        if (rpReportDTO.isPresent()) {
            rpReport = rpReportDTO.get();
            rpReport.setPrdId(syncDataToDashboard.getReportDTO().getPrdId());
            rpReport.setUpdateTime(Instant.now());
        } else {
            rpReport = new RpReportDTO();
            rpReport.setId(syncDataToDashboard.getReportDTO().getId());
            rpReport.setPrdId(syncDataToDashboard.getReportDTO().getPrdId());
            rpReport.setReportCode(syncDataToDashboard.getReportDTO().getReportCode());
            rpReport.setReportName(syncDataToDashboard.getReportDTO().getReportName());
            rpReport.setTimeType(syncDataToDashboard.getReportDTO().getTimeType());
            rpReport.setUpdateTime(Instant.now());
        }
        rpReportService.save(rpReport);
        List<ConfigQueryKpiDTO> lstConfigQuery = configQueryKpiService.findAllByReportId(syncDataToDashboard.getReportDTO().getId());
        for (ConfigQueryKpiDTO config : lstConfigQuery) {
            try {
                Optional<MonitorQueryKpiDTO> monitor = monitorQueryKpiService.findByQueryKpiId(config.getQueryKpiId().intValue());
                if (monitor.isPresent() && monitor.get().getRunTimeReport() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    Date date = sdf.parse(String.valueOf(syncDataToDashboard.getReportDTO().getPrdId()));
                    if (date.before(Date.from(monitor.get().getRunTimeReport()))) {
                        monitorQueryKpiService.updateRunTimeReport(config.getQueryKpiId().intValue(), syncDataToDashboard.getReportDTO().getPrdId(), syncDataToDashboard.getReportDTO().getTimeType());
                    }
                } else {
                    monitorQueryKpiService.updateRunTimeReport(config.getQueryKpiId().intValue(), syncDataToDashboard.getReportDTO().getPrdId(), syncDataToDashboard.getReportDTO().getTimeType());
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        List<MapReportDataToDashboardDTO> listDataChange = new ArrayList<>();
        List<MapReportDataToDashboardDTO> lstDTO = mapReportDataToDashboardRepository.
            findAllByRpInputGrantIdAndReportId(lstReportDatas.get(0).
                getRpInputGrantId(), lstReportDatas.get(0).getReportId()).
            stream().map(mapReportDataToDashboardMapper::toDto).collect(Collectors.toList());

        for (MapReportDataToDashboardDTO mapReportDataToDashboardDTO : lstReportDatas) {
            if (!lstDTO.contains(mapReportDataToDashboardDTO)) {
                listDataChange.add(mapReportDataToDashboardDTO);
            }
        }
        listDataChange.forEach(bean -> {
            mapReportDataToDashboardRepository.deleteAllByRpInputGrantIdAndReportIdAndPrdIdAndColumnIdAndRowId(bean.getRpInputGrantId(), bean.getReportId(), bean.getPrdId(), bean.getColumnId(), bean.getRowId());

        });
        listDataChange.stream().forEach(bean -> {
            bean.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            bean.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        });
        List<MapReportDataToDashboard> lstModelsUpdate = mapReportDataToDashboardMapper.toEntity(listDataChange);
        mapReportDataToDashboardRepository.saveAll(lstModelsUpdate);
    }
}
