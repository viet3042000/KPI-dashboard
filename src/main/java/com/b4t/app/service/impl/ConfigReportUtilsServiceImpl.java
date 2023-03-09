package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.ConfigReportColumnService;
import com.b4t.app.service.ConfigReportService;
import com.b4t.app.service.ConfigReportUtilsService;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfigReportUtilsServiceImpl implements ConfigReportUtilsService {
    private static final String ENTITY_NAME = "configReport";

    @Autowired
    ConfigReportColumnService configReportColumnService;

    @Autowired
    CatItemService catItemService;

    @Autowired
    ConfigReportService configReportService;
    /**
     * Validate cap nhat tung row
     *
     * @param lstColumn
     * @param mapData
     * @throws Exception
     */
    public void validateUpdateRowDataConfigReports(List<ConfigReportColumnDTO> lstColumn, Map<String, Object> mapData) throws Exception {
        String actor = SecurityUtils.getCurrentUserLogin().get();
        for (ConfigReportColumnDTO columnDTO : lstColumn) {
            if (Constants.UPDATE_TIME.equalsIgnoreCase(columnDTO.getColumnName()) && Constants.DATA_TYPE.DATE.equals(columnDTO.getDataType())) {
                mapData.put(columnDTO.getColumnName(), new Date());
            }
            if (Constants.UPDATE_USER.equalsIgnoreCase(columnDTO.getColumnName())) {
                mapData.put(columnDTO.getColumnName(), actor);
            }

            Object val = mapData.get(columnDTO.getColumnName());
            String result = DataUtil.validateValue(val, columnDTO);
            if (!Constants.VALIDATE_OK.equalsIgnoreCase(result)) {
                throw new BadRequestAlertException(result, ENTITY_NAME, "configReport.param");
            }

        }
    }

    /**
     * Lay gia tri thoi gian mac dinh
     *
     * @param configReportImportDTO
     * @param configReport
     * @return
     */
    public String getTimeValue(ConfigReportImportDTO configReportImportDTO, ConfigReport configReport) {
        String value = DataUtil.getTimeValue(Date.from(configReportImportDTO.getImportTime()), Integer.valueOf(configReport.getTimeType()));
        return value;
    }

    /**
     * Lay ten cot luu thoi gian
     *
     * @param reportId
     * @return
     */
    public ConfigReportColumnDTO getColumTimeName(Long reportId) {
        List<ConfigReportColumnDTO> lstTimeColumn = configReportColumnService.findAllByIsTimeColumnEquals(Constants.IS_TIME_COLUMN, reportId);
        ConfigReportColumnDTO columnTime = null;
        if (!DataUtil.isNullOrEmpty(lstTimeColumn)) {
            columnTime = lstTimeColumn.get(0);
        }
        return columnTime;
    }

    @Override
    public ConfigReportForm updateCondition(ConfigReportForm configReport) {

        String username = SecurityUtils.getCurrentUserLogin().get();
        List<CatItemDTO> catItems = catItemService.findDomainByUser(username);
        List<String> lstDomainUser = catItems.stream().map(CatItemDTO::getItemValue).collect(Collectors.toList());
        if (!DataUtil.isNullOrEmpty(configReport.getLstDomainCode())) {
            List<String> lstDomain = configReport.getLstDomainCode().stream().filter(e -> lstDomainUser.contains(e)).collect(Collectors.toList());
            configReport.setLstDomainCode(lstDomain);
        } else {
            configReport.setLstDomainCode(lstDomainUser);
        }
        return configReport;
    }

    public Optional<ConfigReportDetailDTO> getReportDetail(Long id) {
        Optional<ConfigReportDetailDTO> result = Optional.empty();
        Optional<ConfigReportDTO> configReportDTO = configReportService.findOne(id);
        if (configReportDTO.isPresent()) {
            List<ConfigReportColumnDTO> configReportColumnDTOS = configReportColumnService.findAllByReportId(id)
                .stream().filter(e-> !Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey()) && Constants.IS_SHOW.equals(e.getIsShow())).peek(e->{
                    e.setId(null);
                    e.setReportId(null);
                    e.setCreator(null);
                    e.setUpdateTime(null);
                    e.setIsTimeColumn(null);
                    e.setStatus(null);
                    e.setPos(null);
                }).collect(Collectors.toList());
            ConfigReportDetailDTO configReportDetailDTO = new ConfigReportDetailDTO();
            ConfigReportDTO configReport = configReportDTO.get();
            configReport.setTableName(null);
            configReport.setDatabaseName(null);
            configReport.setStatus(null);
            configReport.setCreator(null);
            configReport.setUpdateTime(null);
            configReportDetailDTO.setConfigReport(configReport);
            configReportDetailDTO.setConfigReportColumns(configReportColumnDTOS);
            result = Optional.of(configReportDetailDTO);
        }
        return result;
    }
}
