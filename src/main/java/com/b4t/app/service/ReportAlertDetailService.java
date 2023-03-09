package com.b4t.app.service;

import com.b4t.app.service.dto.ReportAlertDetailDTO;

import java.util.List;

public interface ReportAlertDetailService {

    List<ReportAlertDetailDTO> saveAll(List<ReportAlertDetailDTO> reportAlertDetailDTO);

    List<ReportAlertDetailDTO> findAllCondition(int reportId);

    void deleteByTableId(int reportId);
}
