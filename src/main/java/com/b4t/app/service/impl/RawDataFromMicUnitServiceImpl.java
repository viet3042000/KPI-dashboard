package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatGraphKpi;
import com.b4t.app.domain.FlagRunQueryKpi;
import com.b4t.app.repository.CatGraphKpiRepository;
import com.b4t.app.repository.FlagRunQueryKpiRepository;
import com.b4t.app.service.FlagRunQueryKpiService;
import com.b4t.app.service.RawDataFromMicUnitService;
import com.b4t.app.domain.RawDataFromMicUnit;
import com.b4t.app.repository.RawDataFromMicUnitRepository;
import com.b4t.app.service.dto.FlagRunQueryKpiDTO;
import com.b4t.app.service.dto.RawDataFromMicUnitDTO;
import com.b4t.app.service.mapper.RawDataFromMicUnitMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RawDataFromMicUnit}.
 */
@Service
@Transactional
public class RawDataFromMicUnitServiceImpl implements RawDataFromMicUnitService {
    private static final String ENTITY_NAME = "rawDataFromMicUnit";
    private final Logger log = LoggerFactory.getLogger(RawDataFromMicUnitServiceImpl.class);

    private final RawDataFromMicUnitRepository rawDataFromMicUnitRepository;
    private final FlagRunQueryKpiRepository flagRunQueryKpiRepository;
    private final CatGraphKpiRepository catGraphKpiRepository;
    private final RawDataFromMicUnitMapper rawDataFromMicUnitMapper;
    private final Environment env;
    private final FlagRunQueryKpiService flagRunQueryKpiService;

    public RawDataFromMicUnitServiceImpl(RawDataFromMicUnitRepository rawDataFromMicUnitRepository, FlagRunQueryKpiRepository flagRunQueryKpiRepository, CatGraphKpiRepository catGraphKpiRepository, RawDataFromMicUnitMapper rawDataFromMicUnitMapper, Environment env, FlagRunQueryKpiService flagRunQueryKpiService) {
        this.rawDataFromMicUnitRepository = rawDataFromMicUnitRepository;
        this.flagRunQueryKpiRepository = flagRunQueryKpiRepository;
        this.catGraphKpiRepository = catGraphKpiRepository;
        this.rawDataFromMicUnitMapper = rawDataFromMicUnitMapper;
        this.env = env;
        this.flagRunQueryKpiService = flagRunQueryKpiService;
    }

    /**
     * Save a rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RawDataFromMicUnitDTO save(RawDataFromMicUnitDTO rawDataFromMicUnitDTO) {
        log.debug("Request to save RawDataFromMicUnit : {}", rawDataFromMicUnitDTO);
        RawDataFromMicUnit rawDataFromMicUnit = rawDataFromMicUnitMapper.toEntity(rawDataFromMicUnitDTO);
        rawDataFromMicUnit = rawDataFromMicUnitRepository.save(rawDataFromMicUnit);
        return rawDataFromMicUnitMapper.toDto(rawDataFromMicUnit);
    }

    /**
     * Save a rawDataFromMicUnit.
     *
     * @param rawDataFromMicUnitDTOs the entity to save.
     * @return the persisted entity.
     */
    @Override
    public List<RawDataFromMicUnitDTO> saveAll(List<RawDataFromMicUnitDTO> rawDataFromMicUnitDTOs) {
        List<RawDataFromMicUnit> rawDataFromMicUnits = rawDataFromMicUnitMapper.toEntity(rawDataFromMicUnitDTOs);
        List<Long> kpiIds = rawDataFromMicUnits.stream().map(RawDataFromMicUnit::getKpiId).distinct().collect(Collectors.toList());
        List<CatGraphKpi> catGraphKpis = catGraphKpiRepository.findAllByKpiIdInAndStatus(kpiIds, Constants.STATUS_ACTIVE);
        if (DataUtil.isNullOrEmpty(catGraphKpis)) {
            throw new BadRequestAlertException("Kpi id is not exits ", ENTITY_NAME, "Kpi id is not exits ");
        }
        Map<Long, String> mapKpis = catGraphKpis.stream().collect(Collectors.toMap(CatGraphKpi::getKpiId, CatGraphKpi::getKpiName));
        rawDataFromMicUnits.forEach(bean -> {
            if (bean.getId() != null) {
                bean.setId(null);
            }
            bean.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            bean.setKpiName(mapKpis.get(bean.getKpiId()));
        });
        rawDataFromMicUnits = rawDataFromMicUnitRepository.saveAll(rawDataFromMicUnits);
        updateFlagRunQuery(rawDataFromMicUnitDTOs);
        return rawDataFromMicUnitMapper.toDto(rawDataFromMicUnits);
    }

    private void updateFlagRunQuery(List<RawDataFromMicUnitDTO> rawDataFromMicUnitDTOs) {
        Map<Long, List<Long>> groupByPriceMaps =
            rawDataFromMicUnitDTOs.stream().collect(Collectors.groupingBy(RawDataFromMicUnitDTO::getKpiId, Collectors.mapping(RawDataFromMicUnitDTO::getPrdId, Collectors.toList())));
        List<FlagRunQueryKpiDTO> lsFlag = new ArrayList<>();
        FlagRunQueryKpiDTO flagRunQueryKpi;
        for (Map.Entry entry : groupByPriceMaps.entrySet()) {
            List<Long> lstPrdids = (List<Long>) entry.getValue();
            if (DataUtil.isNullOrEmpty(lstPrdids)) continue;// neu list rong thi bo qua
            Collections.sort(lstPrdids);//sort kpi tang dan
            flagRunQueryKpi = new FlagRunQueryKpiDTO();
            flagRunQueryKpi.setKpiId((Long) entry.getKey());
            flagRunQueryKpi.setPrdId(lstPrdids.get(0));//lay prdId nho nhat
            flagRunQueryKpi.setTableName(env.getProperty("mic_dashboard.raw_data_from_mic_unit").toUpperCase());
            flagRunQueryKpi.setStatus(Constants.STATUS_ACTIVE);
            flagRunQueryKpi.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            lsFlag.add(flagRunQueryKpi);
        }
        insertFlag(lsFlag);
    }

    private void insertFlag(List<FlagRunQueryKpiDTO> lsFlag) {
        if (DataUtil.isNullOrEmpty(lsFlag)) return;
        flagRunQueryKpiService.saveAll(lsFlag);
        /*lsFlag.forEach(bean -> {
            flagRunQueryKpiService.save(bean);
        });*/
    }

    /**
     * Get all the rawDataFromMicUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RawDataFromMicUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RawDataFromMicUnits");
        return rawDataFromMicUnitRepository.findAll(pageable)
            .map(rawDataFromMicUnitMapper::toDto);
    }

    /**
     * Get one rawDataFromMicUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RawDataFromMicUnitDTO> findOne(Long id) {
        log.debug("Request to get RawDataFromMicUnit : {}", id);
        return rawDataFromMicUnitRepository.findById(id)
            .map(rawDataFromMicUnitMapper::toDto);
    }

    /**
     * Delete the rawDataFromMicUnit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RawDataFromMicUnit : {}", id);
        rawDataFromMicUnitRepository.deleteById(id);
    }
}
