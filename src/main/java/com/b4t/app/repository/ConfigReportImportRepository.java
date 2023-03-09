package com.b4t.app.repository;

import com.b4t.app.domain.ConfigReport;
import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.dto.ConfigReportDTO;
import com.b4t.app.service.dto.ConfigReportDataForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ConfigReportImportRepository {

    void importData(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport) throws Exception;
    void importUpdateData(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReportColumnDTO keyColumn, ConfigReport configReport) throws Exception;

    Page<Object> findDataConfigReport(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                      ConfigReport configReport, Pageable pageable, String columnTime, String primaryKey);

    List<Object> findDataConfigReport(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                      ConfigReport configReport, Pageable pageable, String columnTime, boolean isCount, String primaryKey);

    List<Object> findDataConfigReportFormInput(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                      ConfigReport configReport, Pageable pageable, String columnTime, boolean isCount, String primaryKey);

    void deleteDataConfigReports(ConfigReportDataForm configReportDataForm, String columnTime, ConfigReport configReport) throws Exception;

    Map<String, String> deleteRowDataConfigReports(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, ConfigReportColumnDTO primaryKey);

    void updateRowDataConfigReports(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, ConfigReportColumnDTO primaryKey, String keyColum, Object keyValue) throws Exception;

    void insertRowDataConfigReports(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, String primarykeyColumn);

    int getIdPrimaryKey(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, String primaryKeyColumn);

    List<Object[]> getFullDescriptionOfTable(String tableName);

    List<Map<String, Object>> executeQuery(String sql);

    Map<String, Object> buildRefData(List<ConfigReportColumnDTO> lstColumn) throws Exception;

    void importUpdateDataFormInput(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport) throws Exception;

    Page<Object> findDataConfigReportFormInput(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstAllColumn, ConfigReport configReport, Pageable pageable, String columnName, String primaryKeyColumn);

    void deleteDataConfigReportsFormInput(ConfigReportDataForm configReportDataForm, String columnName, ConfigReport configReport) throws Exception;

    void importDataFormInput(List<Map<String, String>> lstMapDataInsert, List<ConfigReportColumnDTO> lstAllColumnIgnoreKey, ConfigReport configReport) throws Exception;

    void importUpdateDataFormInputExcel(List<Map<String, String>> lstMapDataInsert, List<ConfigReportColumnDTO> lstAllColumnIgnoreKey, ConfigReport configReport, String selectedYear) throws Exception;
}
