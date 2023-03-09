package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.service.ConfigReportService;
import com.b4t.app.service.LgspService;
import com.b4t.app.service.dto.ConfigReportDTO;
import com.b4t.app.service.dto.ConfigReportDetailDTO;
import com.b4t.app.service.dto.ConfigReportForm;
import com.b4t.app.service.dto.ConfigReportImportDetailDTO;
import com.b4t.app.service.dto.lgsp.BaseSyncResDTO;
import com.b4t.app.service.dto.lgsp.SyncLGSPError;
import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

@Service
public class LgspServiceImpl implements LgspService {
    private static final Logger log = LoggerFactory.getLogger(LgspServiceImpl.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ConfigReportUtilsServiceImpl configReportUtilsServiceImpl;

    @Autowired
    private ConfigReportService configReportService;

    @PostConstruct
    void init() {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();
    }

    @Override
    public BaseSyncResDTO listForm(SyncLGSPReqDTO req) {
        BaseSyncResDTO res = new BaseSyncResDTO();
        res.setErrorCode(SyncLGSPError.SUCCESS.value());
        String dataBase64 = req.getData();
        ConfigReportForm configReport = null;
        try {
            String json = new String(Base64.getDecoder().decode(dataBase64));
            configReport = mapper.readValue(json, ConfigReportForm.class);

            configReport = configReportUtilsServiceImpl.updateCondition(configReport);
            Pageable pageable = PageRequest.of(0, 500);
            Page<ConfigReportDTO> page;

            if (!DataUtil.isNullOrEmpty(configReport.getLstDomainCode())) {
                page = configReportService.findAllCondition(configReport, pageable);
            } else {
                page = new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
            res.setData(Base64.getEncoder().encodeToString(mapper.writeValueAsString(page.getContent()).getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setErrorCode(SyncLGSPError.FAIL.value());
            res.setErrorMessage(e.getMessage());
        }
        return res;
    }

    public BaseSyncResDTO getReportDetail(SyncLGSPReqDTO req) {
        BaseSyncResDTO res = new BaseSyncResDTO();
        res.setErrorCode(SyncLGSPError.SUCCESS.value());
        String dataBase64 = req.getData();
        ConfigReportForm configReport = null;
        try {
            String json = new String(Base64.getDecoder().decode(dataBase64));
            configReport = mapper.readValue(json, ConfigReportForm.class);
            Optional<ConfigReportDetailDTO> result = configReportUtilsServiceImpl.getReportDetail(configReport.getId());
            if (result.isPresent()) {
                res.setData(Base64.getEncoder().encodeToString(mapper.writeValueAsString(result.get()).getBytes()));
            } else {
                res.setErrorCode(SyncLGSPError.FAIL.value());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            res.setErrorCode(SyncLGSPError.FAIL.value());
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }

    @Override
    public BaseSyncResDTO updateReportData(SyncLGSPReqDTO req) {
        BaseSyncResDTO res = new BaseSyncResDTO();
        res.setErrorCode(SyncLGSPError.SUCCESS.value());
        String dataBase64 = req.getData();
        ConfigReportImportDetailDTO configReport;
        try {
            String json = new String(Base64.getDecoder().decode(dataBase64));
            configReport = mapper.readValue(json, ConfigReportImportDetailDTO.class);
            ConfigReportImportDetailDTO configReportDTO = configReportService.updateDataApi(configReport);
            res.setData(Base64.getEncoder().encodeToString(mapper.writeValueAsString(configReportDTO).getBytes()));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            res.setErrorCode(SyncLGSPError.FAIL.value());
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }
}
