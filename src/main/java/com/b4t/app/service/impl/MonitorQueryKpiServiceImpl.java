package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatGraphKpi;
import com.b4t.app.domain.CatItem;
import com.b4t.app.domain.MonitorQueryKpi;
import com.b4t.app.repository.MonitorQueryKpiRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.MonitorQueryKpiService;
import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.dto.MonitorQueryForm;
import com.b4t.app.service.dto.MonitorQueryKpiDTO;
import com.b4t.app.service.dto.MonitorQueryKpiDetailDTO;
import com.b4t.app.service.mapper.MonitorQueryKpiMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MonitorQueryKpi}.
 */
@Service
@Transactional
public class MonitorQueryKpiServiceImpl implements MonitorQueryKpiService {

    private final Logger log = LoggerFactory.getLogger(MonitorQueryKpiServiceImpl.class);

    private final MonitorQueryKpiRepository monitorQueryKpiRepository;

    private final MonitorQueryKpiMapper monitorQueryKpiMapper;

    private static final String ENTITY_NAME = "monitorQueryKpi";

    public static final Integer STATUS_DISABLED = 0;


    @Autowired
    public MonitorQueryKpiServiceImpl(MonitorQueryKpiRepository monitorQueryKpiRepository, MonitorQueryKpiMapper monitorQueryKpiMapper) {
        this.monitorQueryKpiRepository = monitorQueryKpiRepository;
        this.monitorQueryKpiMapper = monitorQueryKpiMapper;
    }

    /**
     * Save a monitorQueryKpi.
     *
     * @param monitorQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MonitorQueryKpiDTO save(MonitorQueryKpiDTO monitorQueryKpiDTO) {
        log.debug("Request to save MonitorQueryKpi : {}", monitorQueryKpiDTO);
        MonitorQueryKpi monitorQueryKpi = monitorQueryKpiMapper.toEntity(monitorQueryKpiDTO);
        monitorQueryKpi = monitorQueryKpiRepository.save(monitorQueryKpi);
        return monitorQueryKpiMapper.toDto(monitorQueryKpi);
    }

    /**
     * Get all the monitorQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MonitorQueryKpiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MonitorQueryKpis");
        return monitorQueryKpiRepository.findAll(pageable)
            .map(monitorQueryKpiMapper::toDto);
    }


    /**
     * Get one monitorQueryKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MonitorQueryKpiDTO> findOne(Long id) {
        log.debug("Request to get MonitorQueryKpi : {}", id);
        return monitorQueryKpiRepository.findById(id)
            .map(monitorQueryKpiMapper::toDto);
    }

    /**
     * Delete the monitorQueryKpi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MonitorQueryKpi : {}", id);
        monitorQueryKpiRepository.deleteById(id);
    }

    @Override
    public Page<MonitorQueryKpiDetailDTO> findMonitorQueryKpiByKey(MonitorQueryForm monitorQueryForm, Pageable pageable) {
        String keyword = monitorQueryForm.getKeyword() != null ? monitorQueryForm.getKeyword().trim(): null;
        return monitorQueryKpiRepository
            .findMonitorQueryKpiByKey(keyword, monitorQueryForm.getTableSource(),
                monitorQueryForm.getKpiId(),monitorQueryForm.getStatus(),
                pageable).map(this::objectToMonitorQueryKpiDetail);
    }

    public MonitorQueryKpiDetailDTO objectToMonitorQueryKpiDetail(Object[] object) {
        MonitorQueryKpiDetailDTO model = new MonitorQueryKpiDetailDTO();
        model.setQueryKpiId((Integer) object[0]);
        model.setTimeType((String) object[1]);
        model.setStatus((Integer) object[2]);
        model.setRunTimeSucc(DataUtil.toInstant(object[3]));
        model.setUpdateTime(DataUtil.toInstant(object[4]));
        model.setTimeTypeId((String) object[5]);
        model.setId(DataUtil.safeToLong(object[6]));
        return model;
    }

    public void updateRunTimeReport(Integer queryId, Integer prdId, Integer timeType) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(String.valueOf(prdId));
        Date restartTime = DataUtil.getAbsoluteDate(date, -1, timeType);

        Optional<MonitorQueryKpi> monitor = monitorQueryKpiRepository.findFirstByQueryKpiIdEquals(queryId.intValue());
        if(monitor.isPresent()){
            restartTime = DateUtils.truncate(restartTime, Calendar.DAY_OF_MONTH);
            String restartTimeStr = DataUtil.dateToString(restartTime,"yyyy-MM-dd");
            log.info("startTime:",restartTimeStr);
            monitorQueryKpiRepository.updateRunTimeReport(restartTimeStr, queryId);
        }
    }

    @Override
    public void clearRunTimeReport(Integer queryId) throws Exception {
        Optional<MonitorQueryKpi> monitor = monitorQueryKpiRepository.findFirstByQueryKpiIdEquals(queryId.intValue());
        if(monitor.isPresent()){
            monitorQueryKpiRepository.clearRunTimeReport(queryId);
        }
    }

    @Override
    public void restartMonitorQueryKpi(MonitorQueryKpiDetailDTO monitorQueryKpiDetailDTO) throws Exception {
        Date restartTime = DataUtil.getAbsoluteDate(Date.from(monitorQueryKpiDetailDTO.getRunTimeSucc()), -1, monitorQueryKpiDetailDTO.getTimeTypeId());
        Optional<MonitorQueryKpi> monitor = monitorQueryKpiRepository.findFirstByQueryKpiIdEquals(monitorQueryKpiDetailDTO.getQueryKpiId());
        if(!monitor.isPresent()){
            throw new BadRequestAlertException(Translator.toLocale("error.monitorQueryKpi.queryIdNotExist"), ENTITY_NAME, "monitorQueryKpi.queryIdNotExist");
        }
        MonitorQueryKpi monitorQueryKpi = monitor.get();
        Date runTimeSucc = null;
        if(monitorQueryKpi.getRunTimeSucc() != null) {
            runTimeSucc = DateUtils.truncate(Date.from(monitorQueryKpi.getRunTimeSucc()), Calendar.DAY_OF_MONTH);
        }
        if(runTimeSucc != null && runTimeSucc.compareTo(restartTime) <=0) {
            throw new BadRequestAlertException(Translator.toLocale("error.monitorQueryKpi.runTimeInvalid"), ENTITY_NAME, "monitorQueryKpi.runTimeInvalid");
        }
        String restartTimeStr = DataUtil.dateToString(restartTime,"yyyy-MM-dd");
        log.info("startTime:",restartTimeStr);
        monitorQueryKpiRepository.updateMonitorQueryKpi(restartTimeStr, monitorQueryKpiDetailDTO.getQueryKpiId());
        System.out.println(runTimeSucc);
    }

    @Override
    public List<ComboDTO> getMonitorTemplate() {
        return monitorQueryKpiRepository.getMonitorTemplate().stream()
            .map(e-> this.convertToComboDTO(e)).collect(Collectors.toList());
    }

    @Override
    public List<ComboDTO> getKpiByTemplate(String template) {
        return monitorQueryKpiRepository.getKpiByTemplate(template).stream()
            .map(e-> this.convertToComboDTO(e)).collect(Collectors.toList());
    }

    private ComboDTO convertToComboDTO(Object[] obj) {
        ComboDTO comboDTO = new ComboDTO();
        comboDTO.setValue(obj[0]);
        comboDTO.setLabel(DataUtil.safeToString(obj[1]));
        return comboDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMultiple(List<MonitorQueryKpiDTO> monitorQueryKpis) {
//        monitorQueryKpis.forEach(this::setStatusDisabled);
        for(MonitorQueryKpiDTO mon : monitorQueryKpis){
            if (!findOne(mon.getId()).isPresent()
                || Constants.STATUS_DISABLED.equals(mon.getStatus().longValue())) {
                throw new BadRequestAlertException(Translator.toLocale("error.disabled.item"), ENTITY_NAME, "error.disabled.item");
            }
                setStatusDisabled(mon);
        }
    }

    @Override
    public Optional<MonitorQueryKpiDTO> findByQueryKpiId(Integer queryId) {
        return monitorQueryKpiRepository.findFirstByQueryKpiId(queryId).map(monitorQueryKpiMapper::toDto);
    }

    private void setStatusDisabled(MonitorQueryKpiDTO monitorQueryKpis){
        log.debug("Request to disable CatGraphKpi : {}", monitorQueryKpis.getId());
        Optional<MonitorQueryKpi> entityOption = monitorQueryKpiRepository.findById(monitorQueryKpis.getId());
        if (entityOption.isPresent()) {
            MonitorQueryKpi entity = entityOption.get();
            entity.setStatus(STATUS_DISABLED);
            entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            monitorQueryKpiRepository.save(entity);
        }
    }
}
