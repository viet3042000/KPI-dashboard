package com.b4t.app.service;

import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.dto.ConfigReportDetailDTO;

import java.util.List;

public interface CommonService {
    Integer getMaxCode(String tableName, String fieldName, String code);

    boolean checkTableExist(String schema, String table);

    List<ConfigReportColumnDTO> generateTable(Long timeType);

    boolean createTable(ConfigReportDetailDTO configReportDetailDTO);

    boolean alterModifyColumn(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO);
    boolean alterAddColumn(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO);
    boolean alterNotRequired(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO);

    ConfigReportColumnDTO createColumn(String columnName, String title, String dataType, Integer isShow,
                                              Integer isRequired, Integer isTimeColumn, Integer isPrimary, Integer maxLength, Integer columnUnique);
	boolean dropUniqueConstraint(ConfigReportDetailDTO configReportDetailDTO);
    boolean addUniqueConstraint(ConfigReportDetailDTO configReportDetailDTO, String lstColumnUnique);
    boolean saveTriggerConfigReport(ConfigReportDetailDTO configReportDetailDTO);
    boolean saveInsertTrigger(ConfigReportDetailDTO configReportDetailDTO);
    boolean saveUpdateTrigger(ConfigReportDetailDTO configReportDetailDTO);
    boolean saveDeleteTrigger(ConfigReportDetailDTO configReportDetailDTO);
}
