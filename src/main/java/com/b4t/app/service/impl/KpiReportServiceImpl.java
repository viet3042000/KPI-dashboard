package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.repository.KpiReportRepository;
import com.b4t.app.service.KpiReportSearchESService;
import com.b4t.app.service.KpiReportService;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.BaseRptGraphESMapper;
import com.b4t.app.service.mapper.CatKpiReportMapper;
import com.b4t.app.service.mapper.ObjectReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class KpiReportServiceImpl implements KpiReportService {
    private static final Logger logger = LoggerFactory.getLogger(KpiReportServiceImpl.class);
    private final KpiReportRepository kpiReportRepository;
    private final CatKpiReportMapper catKpiReportMapper;
    private final ObjectReportMapper objectReportMapper;
    @Autowired
    CatItemRepository catItemRepository;

    @Autowired
    KpiReportSearchESService kpiReportSearchESService;

    @Autowired
    BaseRptGraphESMapper baseRptGraphESMapper;

    public KpiReportServiceImpl(KpiReportRepository kpiReportRepository,
                                 CatKpiReportMapper catKpiReportMapper,
                                 ObjectReportMapper objectReportMapper) {
        this.kpiReportRepository = kpiReportRepository;
        this.catKpiReportMapper = catKpiReportMapper;
        this.objectReportMapper = objectReportMapper;
    }

    @Override
    public List<CatKpiReportDTO> findCatKpiReport() {
        List<CatKpiReport> lstCatKpi = kpiReportRepository.findTreeKpi();
        return catKpiReportMapper.toDto(lstCatKpi);
    }


    @Override
    public List<ObjectReportDTO> findObjectReport(ReportKpiDTO reportKpiDTO) {
        List<ObjectReport> lstObj = kpiReportRepository.findObjectReportForTree(reportKpiDTO, null);
        return objectReportMapper.toDto(lstObj).stream().map(e -> {
                e.setObjectName(DataUtil.isNullOrEmpty(e.getObjectName()) ? e.getObjectCode() : e.getObjectCode() + "_" + e.getObjectName());
                return e;
            }
        ).collect(Collectors.toList());
    }

    @Override
    public void updateTimeOfForm(ReportKpiDTO reportKpiDTO, BaseRptGraph mapTimeType) throws Exception {
        //Lay cau hinh number_time_back
        Integer numberTimeBack = kpiReportRepository.findConfigNumberBackTime(reportKpiDTO.getTimeType());

        if (reportKpiDTO.getFromDate() == null && reportKpiDTO.getToDate() == null) {
            Date fromDate, toDate;
            Long prdId = 0L;
            if (mapTimeType != null) {
                prdId = Long.valueOf(mapTimeType.getPrdId());
            }
            if (!DataUtil.isNullOrZero(prdId)) {
                try {
                    toDate = DataUtil.getDatePattern(prdId.toString(), "yyyyMMdd");
                    fromDate = DataUtil.getAbsoluteDate(toDate, numberTimeBack, reportKpiDTO.getTimeType());
                    reportKpiDTO.setFromDate(fromDate);
                    reportKpiDTO.setToDate(toDate);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        } else if (reportKpiDTO.getFromDate() == null && reportKpiDTO.getToDate() != null) {
            Date fromDate = DataUtil.getAbsoluteDate(reportKpiDTO.getToDate(), numberTimeBack, reportKpiDTO.getTimeType());
            reportKpiDTO.setFromDate(fromDate);
        } else if (reportKpiDTO.getFromDate() != null && reportKpiDTO.getToDate() == null) {
            Date toDate = DataUtil.getAbsoluteDate(reportKpiDTO.getFromDate(), (-1) * numberTimeBack, reportKpiDTO.getTimeType());
            reportKpiDTO.setToDate(toDate);
        }
    }

    @Override
    public Page<BaseRptGraphESDTO> findRptGraph(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception {
        Map<String, String> mapInputLevel = catItemRepository.findCatItemByCategoryIdAndStatus(Constants.CATEGORY.INPUT_LEVEL, Constants.STATUS_ACTIVE).
            stream().collect(Collectors.toMap(CatItem::getItemValue, CatItem::getItemName, (e1, e2) -> e1));
        Map<String, String> mapTimeType = catItemRepository.findCatItemByCategoryIdAndStatus(Constants.CATEGORY.TIME_TYPE, Constants.STATUS_ACTIVE).
            stream().collect(Collectors.toMap(CatItem::getItemValue, CatItem::getItemName, (e1, e2) -> e1));

        Page<BaseRptGraphESDTO> pageResult = this.findRptGraphPaging(reportKpiDTO, pageable);
        List<BaseRptGraphESDTO> lstData = pageResult.getContent().stream().peek(e -> {
            e.setInputLevelName(mapInputLevel.get(e.getInputLevel().toString()));
            e.setTimeTypeName(mapTimeType.get(e.getTimeType().toString()));
            if (Constants.VALUE_TYPE.VAL_TOTAL.equalsIgnoreCase(reportKpiDTO.getValueType())) {
                e.setValue(e.getValTotal());
            } else if (Constants.VALUE_TYPE.VAL.equalsIgnoreCase(reportKpiDTO.getValueType())) {
                e.setValue(e.getVal());
            } else if (Constants.VALUE_TYPE.VAL_ACC.equalsIgnoreCase(reportKpiDTO.getValueType())) {
                e.setValue(e.getValAcc());
            }
            e.setDateTime(e.getDateTime());
        }).collect(Collectors.toList());
        return new PageImpl<>(lstData, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<BaseRptGraphESDTO> findRptGraphPaging(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception {
        Page<BaseRptGraphESDTO> pageResult = kpiReportSearchESService.onSearch(reportKpiDTO, pageable).map(baseRptGraphESMapper::toDto);
        return pageResult;
    }

    @Override
    public List<BaseRptGraphESDTO> findRptGraphFull(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception {
        if (pageable == null) {
            pageable = PageRequest.of(0, 1000);
        }
        Page<BaseRptGraphESDTO> pageResult = this.findRptGraph(reportKpiDTO, pageable);
        return pageResult.getContent();
    }


    @Override
    public List<String> buildXAxis(ReportKpiDTO reportKpiDTO) {
        List<String> lstResult = new ArrayList<>();
        if (reportKpiDTO.getFromDate() != null && reportKpiDTO.getToDate() != null) {
            if (Constants.TIME_TYPE_MONTH.toString().equals(reportKpiDTO.getTimeType())) {
                for (Date date = reportKpiDTO.getFromDate(); !date.after(reportKpiDTO.getToDate()); date = DataUtil.add(date, 1, Calendar.MONTH)) {
                    lstResult.add(DataUtil.dateToString(date, "MM/yyyy"));
                }
            } else if (Constants.TIME_TYPE_QUARTER.toString().equals(reportKpiDTO.getTimeType())) {
                for (Date date = reportKpiDTO.getFromDate(); !date.after(reportKpiDTO.getToDate()); date = DataUtil.add(date, 3, Calendar.MONTH)) {
                    lstResult.add(DataUtil.dateToStringQuater(date));
                }
            } else if (Constants.TIME_TYPE_YEAR.toString().equals(reportKpiDTO.getTimeType())) {
                for (Date date = reportKpiDTO.getFromDate(); !date.after(reportKpiDTO.getToDate()); date = DataUtil.add(date, 12, Calendar.MONTH)) {
                    lstResult.add(DataUtil.dateToString(date, "yyyy"));
                }
            }
        }
        return lstResult;
    }


    @Override
    @Async
    public CompletableFuture<BaseRptGraphDTO> getMaxPrdId(ReportKpiDTO reportKpiDTO, Integer timeType) throws Exception {
        ReportKpiDTO dto = (ReportKpiDTO) reportKpiDTO.clone();
        dto.setTimeType(timeType.toString());
        Long prdId = kpiReportRepository.getMaxPrdId(dto);
        if (prdId == null) {
            return CompletableFuture.completedFuture(null);
        }
        BaseRptGraphDTO result = new BaseRptGraphDTO();
        result.setTimeType(timeType);
        result.setPrdId(prdId.intValue());
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public List<BaseRptGraphESDTO> findBaseRptGraphDTO(BaseRptGraphESSearch baseDTO) {
        return kpiReportRepository.findBaseRptGraph(baseDTO).stream().map(baseRptGraphESMapper::toDto).collect(Collectors.toList());
    }

    public List<BaseRptGraphES> findBaseRptGraph(BaseRptGraphESSearch baseDTO) {
        return kpiReportRepository.findBaseRptGraph(baseDTO);
    }
}
