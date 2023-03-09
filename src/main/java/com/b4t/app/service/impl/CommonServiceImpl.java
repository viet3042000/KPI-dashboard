package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.service.CommonService;
import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.dto.ConfigReportDTO;
import com.b4t.app.service.dto.ConfigReportDetailDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {

    @Autowired
    private EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Override
    public Integer getMaxCode(String tableName, String fieldName, String code) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT IFNULL(MAX(CONVERT(REPLACE(").append(fieldName).append(", '").append(code).append("', '') , SIGNED INTEGER)), 0)");
        sql.append(" FROM ").append(tableName);
        sql.append(" WHERE ");
        sql.append(fieldName).append(" REGEXP '^").append(code).append("[0-9]+$'");

        Query q = entityManager.createNativeQuery(sql.toString());
        Number rs = (Number) q.getSingleResult();
        return (rs != null ? rs.intValue() : 0);
    }


    @Override
    public boolean checkTableExist(String schema, String table) {
        String sql = "SELECT count(*) total FROM information_schema.tables where TABLE_SCHEMA = :schemaName and TABLE_NAME  = :tableName ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("schemaName", schema);
        query.setParameter("tableName", table);
        Object result = query.getSingleResult();
        if ("0".equalsIgnoreCase(result.toString())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<ConfigReportColumnDTO> generateTable(Long timeType) {
        List<ConfigReportColumnDTO> lstData = new ArrayList<>();
        ConfigReportColumnDTO idColumn = createColumn("id", "id", Constants.DATA_TYPE.BIGINT, 1, 1, 0, 1, 20, 0);
        ConfigReportColumnDTO timeColumn = null;
        if (Constants.TIME_TYPE_MONTH.equals(timeType.intValue())) {
            timeColumn = createColumn("monkey", Translator.toLocale("configReport.column.month"), Constants.DATA_TYPE.INT, 0, 1, 1, 0, 6, 1);
        } else if (Constants.TIME_TYPE_QUARTER.equals(timeType.intValue())) {
            timeColumn = createColumn("quarkey", Translator.toLocale("configReport.column.quater"), Constants.DATA_TYPE.INT, 0, 0, 1, 0, 5, 1);
        } else if (Constants.TIME_TYPE_YEAR.equals(timeType.intValue())) {
            timeColumn = createColumn("yearkey", Translator.toLocale("configReport.column.year"), Constants.DATA_TYPE.INT, 0, 0, 1, 0, 4, 1);
        }
        ConfigReportColumnDTO updateUserColumn = createColumn("update_user", "update_user", Constants.DATA_TYPE.STRING, 0, 0, 0, 0, 200, 0);
        ConfigReportColumnDTO updateTimeColumn = createColumn("update_time", "update_time", Constants.DATA_TYPE.DATE, 0, 0, 0, 0, 200, 0);

        lstData.add(idColumn);
        if (timeColumn != null) {
            lstData.add(timeColumn);
        }
        lstData.add(updateUserColumn);
        lstData.add(updateTimeColumn);

        return lstData;
    }

    public ConfigReportColumnDTO createColumn(String columnName, String title, String dataType, Integer isShow,
                                               Integer isRequired, Integer isTimeColumn, Integer isPrimary, Integer maxLength, Integer columnUnique) {
        ConfigReportColumnDTO idColumn = new ConfigReportColumnDTO();
        idColumn.setDataType(dataType);
        idColumn.setColumnName(columnName);
        idColumn.setTitle(title);
        idColumn.setIsShow(isShow);
        idColumn.setIsRequire(isRequired);
        idColumn.setIsTimeColumn(isTimeColumn);
        idColumn.setIsPrimaryKey(isPrimary);
        idColumn.setMaxLength(maxLength);
        idColumn.setStatus(1);
        idColumn.setColumnUnique(columnUnique);
        return idColumn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTable(ConfigReportDetailDTO configReportDetailDTO) {
        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TABLE IF NOT EXISTS `").append(configReportDetailDTO.getConfigReport().getDatabaseName()).append("`.");
        sb.append(configReportDetailDTO.getConfigReport().getTableName()).append("(");
        for (ConfigReportColumnDTO column : configReportDetailDTO.getConfigReportColumns()) {
            sb.append(generateScriptDll(column));
        }
        ConfigReportColumnDTO primaryKeyColumn = configReportDetailDTO.getConfigReportColumns().stream().filter(e -> Integer.valueOf(1).equals(e.getIsPrimaryKey())).findFirst().orElse(null);
        if (primaryKeyColumn != null) {
            sb.append(" PRIMARY KEY (").append(primaryKeyColumn.getColumnName()).append(") ,");
        }
        String lstColumnUnique = configReportDetailDTO.getConfigReportColumns().stream().
            filter(e -> Integer.valueOf(1).equals(e.getColumnUnique())).map(ConfigReportColumnDTO::getColumnName).collect(Collectors.joining(","));
        if (!DataUtil.isNullOrEmpty(lstColumnUnique)) {
            sb.append(" UNIQUE KEY unique1 (").append(lstColumnUnique).append(") ,");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8 COLLATE=utf8_unicode_ci");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return false;
    }

    private String generateScriptDll(ConfigReportColumnDTO column) {
        StringBuffer sb = new StringBuffer("").append(column.getColumnName());
        if (Constants.DATA_TYPE.DOUBLE.equalsIgnoreCase(column.getDataType())) {
            sb.append(" ").append(column.getDataType()).append(" ");
        } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(column.getDataType())) {
            sb.append(" datetime ");
        } else if (Constants.DATA_TYPE.STRING.equalsIgnoreCase(column.getDataType())) {
            sb.append(" varchar(");
            if (column.getMaxLength() == null || column.getMaxLength() == 0) {
                sb.append(200).append(")");
            } else {
                sb.append(column.getMaxLength()).append(")");
            }
        } else if (Constants.DATA_TYPE.BIGINT.equalsIgnoreCase(column.getDataType())
            || Constants.DATA_TYPE.INT.equalsIgnoreCase(column.getDataType())) {
            sb.append(" ").append(column.getDataType().toLowerCase()).append("(");
            if (column.getMaxLength() == null || column.getMaxLength() == 0) {
                sb.append(20).append(")");
            } else {
                sb.append(column.getMaxLength()).append(")");
            }
        } else if (Constants.DATA_TYPE.LONG.equalsIgnoreCase(column.getDataType())) {
            sb.append(" int(10)");
        }
        if (!DataUtil.isNullOrEmpty(column.getDefaultValue()) && !"NULL".equalsIgnoreCase(column.getDefaultValue())) {
            if (Arrays.asList(Constants.DATA_TYPE.INT, Constants.DATA_TYPE.BIGINT, Constants.DATA_TYPE.LONG, Constants.DATA_TYPE.DOUBLE).contains(column.getDataType().toUpperCase())) {
                sb.append(" DEFAULT ").append(column.getDefaultValue());
            } else if (Constants.DATA_TYPE.STRING.equalsIgnoreCase(column.getDataType())) {
                sb.append(" DEFAULT '").append(column.getDefaultValue()).append("'");
            } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(column.getDataType())) {
                sb.append(" DEFAULT CURRENT_TIMESTAMP ");
            }
        }
        if (Integer.valueOf(1).equals(column.getIsRequire())) {
            sb.append(" NOT NULL ");
        }
        if (Integer.valueOf(1).equals(column.getIsPrimaryKey())) {
            sb.append(" AUTO_INCREMENT ");
        }
        if (column.getTitle() != null) {
            sb.append(" COMMENT '").append(column.getTitle()).append("'");
        }
        sb.append(",");
        return sb.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean alterModifyColumn(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO) {
        StringBuffer sb = buildModifyQuery(configReportDetailDTO, configReportColumnDTO, "MODIFY", true);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean alterAddColumn(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO) {
        if (!checkColumnExist(configReportDetailDTO.getConfigReport().getDatabaseName(), configReportDetailDTO.getConfigReport().getTableName(), configReportColumnDTO.getColumnName())) {
            StringBuffer sb = buildModifyQuery(configReportDetailDTO, configReportColumnDTO, "ADD", true);
            Query query = entityManager.createNativeQuery(sb.toString());
            query.executeUpdate();
        } else {
            alterModifyColumn(configReportDetailDTO, configReportColumnDTO);
        }
        return true;
    }

    private boolean checkColumnExist(String schema, String table, String columnName) {
        String sql = "SELECT count(*) total FROM information_schema.COLUMNS where TABLE_SCHEMA = :schemaName and TABLE_NAME  = :tableName and COLUMN_NAME = :columnName";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("schemaName", schema);
        query.setParameter("tableName", table);
        query.setParameter("columnName", columnName);
        Object result = query.getSingleResult();
        if ("0".equalsIgnoreCase(result.toString())) {
            return false;
        } else {
            return true;
        }
    }

    private int getLength(Integer maxLength, Integer lengthDefault) {
        if (maxLength != null && !maxLength.equals(0)) {
            return maxLength;
        }
        return lengthDefault;
    }

    @Override
    public boolean alterNotRequired(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO) {
        StringBuffer sb = buildModifyQuery(configReportDetailDTO, configReportColumnDTO, "MODIFY", false);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return true;
    }

    private StringBuffer buildModifyQuery(ConfigReportDetailDTO configReportDetailDTO, ConfigReportColumnDTO configReportColumnDTO, String action, boolean checkRequire) {
        if (!DataUtil.validateDatabaseAndTableName(configReportDetailDTO.getConfigReport().getDatabaseName())
            || !DataUtil.validateDatabaseAndTableName(configReportDetailDTO.getConfigReport().getTableName())
            || !DataUtil.validateDatabaseAndTableName(configReportColumnDTO.getColumnName())) {
            throw new BadRequestAlertException(Translator.toLocale("configReport.column.nameMustbeNormalCharacter"), "configReport", "configReport.column.mustbeNormalCharacter");
        }
        StringBuffer sb = new StringBuffer("");
        sb.append("ALTER TABLE `" + configReportDetailDTO.getConfigReport().getDatabaseName() + "`.`" + configReportDetailDTO.getConfigReport().getTableName() + "` ").append(action).append(" ");
        sb.append(" ").append(configReportColumnDTO.getColumnName());

        switch (configReportColumnDTO.getDataType()) {
            case Constants.DATA_TYPE.DATE:
                sb.append(" datetime ");
                break;
            case Constants.DATA_TYPE.DOUBLE:
                sb.append(" double ");
                break;
            case Constants.DATA_TYPE.LONG:
                sb.append(" int(10) ");
                break;
            case Constants.DATA_TYPE.BIGINT:
                sb.append(" bigint(").append(getLength(configReportColumnDTO.getMaxLength(), 20)).append(") ");
                break;
            case Constants.DATA_TYPE.INT:
                sb.append(" int(").append(getLength(configReportColumnDTO.getMaxLength(), 10)).append(") ");
                break;
            case Constants.DATA_TYPE.STRING:
                sb.append(" varchar(").append(getLength(configReportColumnDTO.getMaxLength(), 200)).append(") ");
                break;
        }

        if (!DataUtil.isNullOrEmpty(configReportColumnDTO.getDefaultValue()) && !"NULL".equalsIgnoreCase(configReportColumnDTO.getDefaultValue())) {
            if (Arrays.asList(Constants.DATA_TYPE.INT, Constants.DATA_TYPE.BIGINT, Constants.DATA_TYPE.LONG, Constants.DATA_TYPE.DOUBLE).contains(configReportColumnDTO.getDataType().toUpperCase())) {
                sb.append(" DEFAULT ").append(configReportColumnDTO.getDefaultValue());
            } else if (Constants.DATA_TYPE.STRING.equalsIgnoreCase(configReportColumnDTO.getDataType())) {
                sb.append(" DEFAULT '").append(configReportColumnDTO.getDefaultValue()).append("'");
            } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(configReportColumnDTO.getDataType())) {
                sb.append(" DEFAULT CURRENT_TIMESTAMP ");
            }
        }

        if (checkRequire && Integer.valueOf(1).equals(configReportColumnDTO.getIsRequire())) {
            sb.append(" NOT NULL ");
        }

        if (Constants.IS_PRIMARY_KEY.equals(configReportColumnDTO.getIsPrimaryKey())) {
            sb.append(" AUTO_INCREMENT ");
        }

        if (configReportColumnDTO.getTitle() != null) {
            sb.append(" COMMENT '").append(configReportColumnDTO.getTitle()).append("'");
        }
        return sb;
    }

    @Override
    @Transactional
    public boolean dropUniqueConstraint(ConfigReportDetailDTO configReportDetailDTO) {
        if (!DataUtil.validateDatabaseAndTableName(configReportDetailDTO.getConfigReport().getDatabaseName())
            || !DataUtil.validateDatabaseAndTableName(configReportDetailDTO.getConfigReport().getTableName())) {
            throw new BadRequestAlertException(Translator.toLocale("configReport.column.nameMustbeNormalCharacter"), "configReport", "configReport.column.mustbeNormalCharacter");
        }
        try {
            StringBuilder sbCheck = new StringBuilder("SHOW INDEX from `" + configReportDetailDTO.getConfigReport().getDatabaseName() + "`.`" + configReportDetailDTO.getConfigReport().getTableName() + "` where key_name = 'unique1'");
            Query queryCheck = entityManager.createNativeQuery(sbCheck.toString());
            List lstCheck = queryCheck.getResultList();
            if(!DataUtil.isNullOrEmpty(lstCheck)) {
                StringBuffer sb = new StringBuffer("");
                sb.append("ALTER TABLE `" + configReportDetailDTO.getConfigReport().getDatabaseName() + "`.`" + configReportDetailDTO.getConfigReport().getTableName() + "` ");
                sb.append(" DROP INDEX unique1");
                Query query = entityManager.createNativeQuery(sb.toString());
                query.executeUpdate();
            }
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean addUniqueConstraint(ConfigReportDetailDTO configReportDetailDTO, String lstColumnUnique) {
        StringBuffer sb = new StringBuffer("");
        sb.append("ALTER TABLE `" + configReportDetailDTO.getConfigReport().getDatabaseName() + "`.`" + configReportDetailDTO.getConfigReport().getTableName() + "` ");
        sb.append(" ADD  UNIQUE KEY unique1 (").append(lstColumnUnique).append(") ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean  saveInsertTrigger(ConfigReportDetailDTO configReportDetailDTO) {
        ConfigReportDTO configReportDTO = configReportDetailDTO.getConfigReport();
        List<ConfigReportColumnDTO> configReportColumnDTOList = configReportDetailDTO.getConfigReportColumns();
        if(configReportColumnDTOList.stream().filter(e -> !Constants.LIST_DEFAULT_COLUMN.contains(e.getColumnName())).collect(Collectors.toList()).size() == 0){
            return false;
        }

        // run drop trigger query
        StringBuilder dropSB = new StringBuilder("DROP TRIGGER IF EXISTS `");
        dropSB.append(configReportDTO.getDatabaseName()).append("`.").append("`trigger1_").append(configReportDetailDTO.getConfigReport().getTableName()).append("`");
        Query dropQuery = entityManager.createNativeQuery(dropSB.toString());
        dropQuery.executeUpdate();

        // run create trigger query
        StringBuilder fieldContent = new StringBuilder("\'");
        StringBuilder fieldNameContent = new StringBuilder("\'");
        StringBuilder newValueContent = new StringBuilder("concat_ws(");
        for(ConfigReportColumnDTO column : configReportColumnDTOList){
            if(Constants.LIST_DEFAULT_COLUMN.contains(column.getColumnName())) continue;
            fieldContent.append(column.getColumnName()).append(";");
            fieldNameContent.append(column.getTitle()).append(";");
            newValueContent.append("new.").append(column.getColumnName()).append(",';',");
        }
        fieldContent.deleteCharAt(fieldContent.length() - 1);
        fieldNameContent.deleteCharAt(fieldNameContent.length() - 1);
        newValueContent = new StringBuilder(newValueContent.substring(0,newValueContent.length() - 5));
        fieldContent.append("\',");
        fieldNameContent.append("\',");
        newValueContent.append("),");

        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TRIGGER `").append(configReportDTO.getDatabaseName()).append("`.").append("`trigger1_").append(configReportDTO.getTableName()).append("`");
        sb.append("\nAFTER INSERT ON `").append(configReportDTO.getDatabaseName()).append("`.").append(configReportDTO.getTableName());
        sb.append("\nFOR EACH row\n");
        sb.append("BEGIN\n");
        sb.append("INSERT INTO data_log(");
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_MONTH){
            sb.append("monkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_QUARTER){
            sb.append("quarkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_YEAR){
            sb.append("yearkey,");
        }
        sb.append("table_name,record_id,field,field_name,old_value,new_value,action_type,modified_by,modified_time,schema_name)\n");
        //insert into data_log(yearkey,table_name,record_id,field,field_name,old_value,new_value,action_type,modified_by,modified_time,schema_name)
        sb.append("values(");
        //timeKey
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_MONTH){
            sb.append("new.monkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_QUARTER){
            sb.append("new.quarkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_YEAR){
            sb.append("new.yearkey,");
        }
        //table_name
        sb.append("\'").append(configReportDTO.getTableName()).append("\',");
        //record_id
        sb.append("new.id,");
        //field,fieldName
        sb.append(fieldContent).append(fieldNameContent);
        //old_value,new_value
        sb.append("'null',").append(newValueContent);
        //action_type,modified_by,modified_time
        sb.append("1,new.update_user,new.update_time,");
        //schema_name
        sb.append("\'").append(configReportDTO.getDatabaseName()).append("\');\n");
        sb.append("end");
        logger.info(sb.toString());
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdateTrigger(ConfigReportDetailDTO configReportDetailDTO) {
        ConfigReportDTO configReportDTO = configReportDetailDTO.getConfigReport();
        List<ConfigReportColumnDTO> configReportColumnDTOList = configReportDetailDTO.getConfigReportColumns();
        if(configReportColumnDTOList.stream().filter(e -> !Constants.LIST_DEFAULT_COLUMN.contains(e.getColumnName())).collect(Collectors.toList()).size() == 0){
            return false;
        }

        // run drop trigger query
        StringBuilder dropSB = new StringBuilder("DROP TRIGGER IF EXISTS `");
        dropSB.append(configReportDTO.getDatabaseName()).append("`.").append("`trigger2_").append(configReportDetailDTO.getConfigReport().getTableName()).append("`");
        Query dropQuery = entityManager.createNativeQuery(dropSB.toString());
        dropQuery.executeUpdate();

        // run create trigger query
        StringBuilder fieldContent = new StringBuilder("\'");
        StringBuilder fieldNameContent = new StringBuilder("\'");
        StringBuilder oldValueContent = new StringBuilder("concat_ws(");
        StringBuilder newValueContent = new StringBuilder("concat_ws(");
        for(ConfigReportColumnDTO column : configReportColumnDTOList){
            if(Constants.LIST_DEFAULT_COLUMN.contains(column.getColumnName())) continue;
            fieldContent.append(column.getColumnName()).append(";");
            fieldNameContent.append(column.getTitle()).append(";");
            oldValueContent.append("old.").append(column.getColumnName()).append(",';',");
            newValueContent.append("new.").append(column.getColumnName()).append(",';',");
        }
        fieldContent.deleteCharAt(fieldContent.length() - 1);
        fieldNameContent.deleteCharAt(fieldNameContent.length() - 1);
        oldValueContent = new StringBuilder(oldValueContent.substring(0,oldValueContent.length() - 5));
        newValueContent = new StringBuilder(newValueContent.substring(0,newValueContent.length() - 5));
        fieldContent.append("\',");
        fieldNameContent.append("\',");
        oldValueContent.append("),");
        newValueContent.append("),");

        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TRIGGER `").append(configReportDTO.getDatabaseName()).append("`.").append("`trigger2_").append(configReportDTO.getTableName()).append("`");
        sb.append("\nAFTER UPDATE ON `").append(configReportDTO.getDatabaseName()).append("`.").append(configReportDTO.getTableName());
        sb.append("\nFOR EACH row\n");
        sb.append("BEGIN\n");
        // so sánh dữ liệu bản ghi cũ và mới => khác nhau mới nhận là update => insert vào bảng data_log
        sb.append("if ").append(oldValueContent.substring(0, oldValueContent.length()-1)).append(" not like ").append(newValueContent.substring(0, newValueContent.length()-1)).append('\n');
        sb.append("then\n");
        sb.append("INSERT INTO data_log(");
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_MONTH){
            sb.append("monkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_QUARTER){
            sb.append("quarkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_YEAR){
            sb.append("yearkey,");
        }
        sb.append("table_name,record_id,field,field_name,old_value,new_value,action_type,modified_by,modified_time,schema_name)\n");
        //insert into data_log(yearkey,table_name,record_id,field,field_name,old_value,new_value,action_type,modified_by,modified_time,schema_name)
        sb.append("values(");
        //timeKey
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_MONTH){
            sb.append("old.monkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_QUARTER){
            sb.append("old.quarkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_YEAR){
            sb.append("old.yearkey,");
        }
        //table_name
        sb.append("\'").append(configReportDTO.getTableName()).append("\',");
        //record_id
        sb.append("old.id,");
        //field,fieldName
        sb.append(fieldContent).append(fieldNameContent);
        //old_value,new_value
        sb.append(oldValueContent).append(newValueContent);
        //action_type,modified_by,modified_time
        sb.append("2,new.update_user,new.update_time,");
        //schema_name
        sb.append("\'").append(configReportDTO.getDatabaseName()).append("\');\n");
        sb.append("end if;\n");
        sb.append("end");
        logger.info(sb.toString());
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDeleteTrigger(ConfigReportDetailDTO configReportDetailDTO) {
        ConfigReportDTO configReportDTO = configReportDetailDTO.getConfigReport();
        List<ConfigReportColumnDTO> configReportColumnDTOList = configReportDetailDTO.getConfigReportColumns();
        if(configReportColumnDTOList.stream().filter(e -> !Constants.LIST_DEFAULT_COLUMN.contains(e.getColumnName())).collect(Collectors.toList()).size() == 0){
            return false;
        }

        // run drop trigger query
        StringBuilder dropSB = new StringBuilder("DROP TRIGGER IF EXISTS `");
        dropSB.append(configReportDTO.getDatabaseName()).append("`.").append("`trigger3_").append(configReportDetailDTO.getConfigReport().getTableName()).append("`");
        Query dropQuery = entityManager.createNativeQuery(dropSB.toString());
        dropQuery.executeUpdate();

        // run create trigger query
        StringBuilder fieldContent = new StringBuilder("\'");
        StringBuilder fieldNameContent = new StringBuilder("\'");
        StringBuilder oldValueContent = new StringBuilder("concat_ws(");
        for(ConfigReportColumnDTO column : configReportColumnDTOList){
            if(Constants.LIST_DEFAULT_COLUMN.contains(column.getColumnName())) continue;
            fieldContent.append(column.getColumnName()).append(";");
            fieldNameContent.append(column.getTitle()).append(";");
            oldValueContent.append("old.").append(column.getColumnName()).append(",';',");
        }
        fieldContent.deleteCharAt(fieldContent.length() - 1);
        fieldNameContent.deleteCharAt(fieldNameContent.length() - 1);
        oldValueContent = new StringBuilder(oldValueContent.substring(0,oldValueContent.length() - 5));
        fieldContent.append("\',");
        fieldNameContent.append("\',");
        oldValueContent.append("),");

        StringBuffer sb = new StringBuffer("");
        sb.append("CREATE TRIGGER `").append(configReportDTO.getDatabaseName()).append("`.").append("`trigger3_").append(configReportDTO.getTableName()).append("`");
        sb.append("\nAFTER DELETE ON `").append(configReportDTO.getDatabaseName()).append("`.").append(configReportDTO.getTableName());
        sb.append("\nFOR EACH row\n");
        sb.append("BEGIN\n");
        sb.append("INSERT INTO data_log(");
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_MONTH){
            sb.append("monkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_QUARTER){
            sb.append("quarkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_YEAR){
            sb.append("yearkey,");
        }
        sb.append("table_name,record_id,field,field_name,old_value,new_value,action_type,modified_by,modified_time,schema_name)\n");
        //insert into data_log(yearkey,table_name,record_id,field,field_name,old_value,new_value,action_type,modified_by,modified_time,schema_name)
        sb.append("values(");
        //timeKey
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_MONTH){
            sb.append("old.monkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_QUARTER){
            sb.append("old.quarkey,");
        }
        if(Integer.valueOf(configReportDTO.getTimeType()) == Constants.TIME_TYPE_YEAR){
            sb.append("old.yearkey,");
        }
        //table_name
        sb.append("\'").append(configReportDTO.getTableName()).append("\',");
        //record_id
        sb.append("old.id,");
        //field,fieldName
        sb.append(fieldContent).append(fieldNameContent);
        //old_value,new_value
        sb.append(oldValueContent).append("'null',");
        //action_type,modified_by,modified_time
        sb.append("3,null,null,");
        //schema_name
        sb.append("\'").append(configReportDTO.getDatabaseName()).append("\');\n");
        sb.append("end");
        logger.info(sb.toString());
        Query query = entityManager.createNativeQuery(sb.toString());
        query.executeUpdate();
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTriggerConfigReport(ConfigReportDetailDTO configReportDetailDTO) {
        saveInsertTrigger(configReportDetailDTO);
        saveUpdateTrigger(configReportDetailDTO);
        saveDeleteTrigger(configReportDetailDTO);
        return false;
    }
}
