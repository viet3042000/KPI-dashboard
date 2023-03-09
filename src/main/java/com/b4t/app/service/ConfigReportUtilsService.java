package com.b4t.app.service;

import com.b4t.app.domain.ConfigReport;
import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.dto.ConfigReportImportDTO;
import com.b4t.app.service.dto.ConfigReportDetailDTO;
import com.b4t.app.service.dto.ConfigReportForm;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConfigReportUtilsService {
    void validateUpdateRowDataConfigReports(List<ConfigReportColumnDTO> lstColumn, Map<String, Object> mapData) throws Exception;

    String getTimeValue(ConfigReportImportDTO configReportImportDTO, ConfigReport configReport);

    ConfigReportColumnDTO getColumTimeName(Long reportId);

    ConfigReportForm updateCondition(ConfigReportForm configReport);

    Optional<ConfigReportDetailDTO> getReportDetail(Long id);
}
