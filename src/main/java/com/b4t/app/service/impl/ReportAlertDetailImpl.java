package com.b4t.app.service.impl;

import com.b4t.app.domain.ReportAlertDetail;
import com.b4t.app.repository.ReportAlertDetailRepository;
import com.b4t.app.service.ReportAlertDetailService;
import com.b4t.app.service.dto.ReportAlertDetailDTO;
import com.b4t.app.service.mapper.ReportAlertDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ReportAlertDetailImpl implements ReportAlertDetailService {
    private final Logger log = LoggerFactory.getLogger(ConfigReportServiceImpl.class);
    private final ReportAlertDetailRepository reportAlertDetailRepository;
    private final ReportAlertDetailMapper reportAlertDetailMapper;

    public ReportAlertDetailImpl(ReportAlertDetailRepository reportAlertDetailRepository, ReportAlertDetailMapper reportAlertDetailMapper) {
        this.reportAlertDetailRepository = reportAlertDetailRepository;
        this.reportAlertDetailMapper = reportAlertDetailMapper;
    }

    @Override
    public List<ReportAlertDetailDTO> saveAll(List<ReportAlertDetailDTO> reportAlertDetailDTO) {
        List<ReportAlertDetailDTO> result = new ArrayList<>();
        if(reportAlertDetailDTO.size() > 0) {
            List<ReportAlertDetail> reportAlertDetail = reportAlertDetailMapper.toEntity(reportAlertDetailDTO);
            result = reportAlertDetailMapper.toDto(reportAlertDetailRepository.saveAll(reportAlertDetail));
        }
        return result;
    }

    @Override
    public List<ReportAlertDetailDTO> findAllCondition(int reportId) {
        List<ReportAlertDetailDTO> result = reportAlertDetailMapper.toDto(reportAlertDetailRepository.findByReportId(reportId));

        return result;
    }

    @Override
    public void deleteByTableId(int reportId) {
        reportAlertDetailRepository.deleteByTableId(String.valueOf(reportId));
    }
}
