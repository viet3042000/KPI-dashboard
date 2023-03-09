package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.domain.DataLog;
import com.b4t.app.domain.SysRole;
import com.b4t.app.repository.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

//import javafx.scene.chart.ScatterChart;

@Repository
@Transactional
public class DataLogRepositoryImpl {
    private final Logger log = LoggerFactory.getLogger(DataLogRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;
    //fix sonar
    private SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat ddMMyyyyHHmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private ConfigReportImportRepositoryImpl configReportImportRepositoryImpl = new ConfigReportImportRepositoryImpl();

    @Autowired
    SysRoleRepository sysRoleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfigReportColumnRepository configReportColumnRepository;

    public void insertDataLogAdd(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, int actionType) throws Exception {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            List<ConfigDataLogColumnDTO> lstConfigDataLogColumnDTO = new ArrayList<>();
            Map<String, String> mapColumn = new HashMap<>();
            mapColumn.put("schema_name", "varchar");
            mapColumn.put("table_name", "varchar");
            mapColumn.put("record_id", "BIGINT");
            mapColumn.put("monkey", "INT");
            mapColumn.put("quarkey", "INT");
            mapColumn.put("yearkey", "INT");
            mapColumn.put("field", "varchar");
            mapColumn.put("field_name", "varchar");
            mapColumn.put("old_value", "varchar");
            mapColumn.put("new_value", "varchar");
            mapColumn.put("action_type", "INT");
            mapColumn.put("modified_by", "varchar");
            mapColumn.put("modified_time", "timestamp");
            Set<String> setColumnName = mapColumn.keySet();
            for (String key : setColumnName) {
                ConfigDataLogColumnDTO configDataLogColumnDTO = new ConfigDataLogColumnDTO();
                configDataLogColumnDTO.setColumnName(key);
                configDataLogColumnDTO.setDataType(mapColumn.get(key));
                lstConfigDataLogColumnDTO.add(configDataLogColumnDTO);
            }

            StringBuilder field = new StringBuilder();
            Set<String> set = lstMapData.get(0).keySet();
            Set<String> setField = new HashSet<>();
            for (String key : set) {
                if(!key.equals("id") && !key.equals("update_time") && !key.equals("update_user") && !key.equals("RESULT_VALIDATE_MSG") && !key.equals("yearkey") && !key.equals("monkey") && !key.equals("quarkey")){
                    field.append(key).append(";");
                    setField.add(key);
                }
            }
            StringBuilder field_name = new StringBuilder();
            for(String key: setField){
                for(ConfigReportColumnDTO tmp : lstColumnConfig){
                    if(key.equals(tmp.getColumnName())){
                        field_name.append(tmp.getTitle()).append(";");
                    }
                }
            }
            field.deleteCharAt(field.length() - 1);
            field_name.deleteCharAt(field_name.length() - 1);

            StringBuilder sb = new StringBuilder();
            StringBuilder sbCol = new StringBuilder();
            StringBuilder sbParam = new StringBuilder();
            StringBuilder sbColUpdate = new StringBuilder();
            for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                sbCol.append(columnDTO.getColumnName()).append(",");
                sbParam.append("?").append(",");
                sbColUpdate.append(columnDTO.getColumnName()).append("=").append("?").append(",");
            }
            sbCol.deleteCharAt(sbCol.length() - 1);
            sbParam.deleteCharAt(sbParam.length() - 1);
            sbColUpdate.deleteCharAt(sbColUpdate.length() - 1);

            sb.append("INSERT INTO " + configReport.getDatabaseName() + ".data_log " + "(");
            sb.append(sbCol);
            sb.append(") VALUES (");
            sb.append(sbParam);
            sb.append(")");
            sb.append(" ON DUPLICATE KEY update ");
            sb.append(sbColUpdate);


            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            connection = myWork.getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(sb.toString());
            int i, batch = 0;

            for (Map<String, String> rowData : lstMapData) {
                i = 1;
                //Set cac tham so cho Parameter
                Map<String, String> parameter = new HashMap<>();
                parameter.put("record_id", String.valueOf(111));
                parameter.put("schema_name", configReport.getDatabaseName());
                parameter.put("table_name", configReport.getTableName());
                if(set.contains("yearkey")){
                    parameter.put("yearkey", rowData.get("yearkey"));
                } else {
                    parameter.put("yearkey", null);
                }
                if(set.contains("monkey")){
                    parameter.put("monkey", rowData.get("monkey"));
                } else {
                    parameter.put("monkey", null);
                }
                if(set.contains("quarkey")){
                    parameter.put("quarkey", rowData.get("quarkey"));
                } else {
                    parameter.put("quarkey", null);
                }
                parameter.put("field", field.toString());
                parameter.put("field_name", field_name.toString());
                parameter.put("old_value", "null");
                StringBuilder new_value = new StringBuilder();
                for (String key : setField) {
                    int check = 0;
                    for(ConfigReportColumnDTO cl :lstColumnConfig){
                        if(cl.getColumnName().equals(key) && (cl.getDataType().equals("DOUBLE") )){
                            check = 1;
                            break;
                        }
                    }
                    if(check == 1){
                        new_value.append(Double.parseDouble(rowData.get(key))).append(";");
                    }
                    else{
                        new_value.append(rowData.get(key)).append(";");
                    }
                }
                new_value.deleteCharAt(new_value.length() - 1);
                parameter.put("new_value", new_value.toString());
                parameter.put("action_type", String.valueOf(actionType));
                parameter.put("modified_by", rowData.get("update_user"));
                parameter.put("modified_time", rowData.get("update_time"));

                // Set cac tham so buoc them moi
                for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                   setParameter(i++, stmt, columnDTO, parameter);
                }

//                Set cac tham so buoc cap nhat
                for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                    setParameter(i++, stmt, columnDTO, parameter);
                }
                stmt.addBatch();
                batch++;
                if (batch % 200 == 0) {
                    stmt.executeBatch();
                    stmt.getConnection().commit();
                }
            }
            stmt.executeBatch();
            stmt.getConnection().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            close(stmt);
        }
    }


    public void insertDataLogUpdate(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, int actionType, List<Map<String, String>> lstOldData) throws Exception {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            List<ConfigDataLogColumnDTO> lstConfigDataLogColumnDTO = new ArrayList<>();
            Map<String, String> mapColumn = new HashMap<>();
            mapColumn.put("schema_name", "varchar");
            mapColumn.put("table_name", "varchar");
            mapColumn.put("record_id", "BIGINT");
            mapColumn.put("monkey", "INT");
            mapColumn.put("quarkey", "INT");
            mapColumn.put("yearkey", "INT");
            mapColumn.put("field", "varchar");
            mapColumn.put("field_name", "varchar");
            mapColumn.put("old_value", "varchar");
            mapColumn.put("new_value", "varchar");
            mapColumn.put("action_type", "INT");
            mapColumn.put("modified_by", "varchar");
            mapColumn.put("modified_time", "timestamp");
            Set<String> setColumnName = mapColumn.keySet();
            for (String key : setColumnName) {
                ConfigDataLogColumnDTO configDataLogColumnDTO = new ConfigDataLogColumnDTO();
                configDataLogColumnDTO.setColumnName(key);
                configDataLogColumnDTO.setDataType(mapColumn.get(key));
                lstConfigDataLogColumnDTO.add(configDataLogColumnDTO);
            }

            StringBuilder field = new StringBuilder();
            Set<String> set = lstMapData.get(0).keySet();
            Set<String> setField = new HashSet<>();
            for (String key : set) {
                if(!key.equals("id") && !key.equals("update_time") && !key.equals("update_user") && !key.equals("RESULT_VALIDATE_MSG") && !key.equals("yearkey") && !key.equals("monkey") && !key.equals("quarkey")){
                    field.append(key).append(";");
                    setField.add(key);
                }
            }
            StringBuilder field_name = new StringBuilder();
            for(String key: setField){
                for(ConfigReportColumnDTO tmp : lstColumnConfig){
                    if(key.equals(tmp.getColumnName())){
                        field_name.append(tmp.getTitle()).append(";");
                    }
                }
            }
            field.deleteCharAt(field.length() - 1);
            field_name.deleteCharAt(field_name.length() - 1);

            StringBuilder sb = new StringBuilder();
            StringBuilder sbCol = new StringBuilder();
            StringBuilder sbParam = new StringBuilder();
            StringBuilder sbColUpdate = new StringBuilder();
            for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                sbCol.append(columnDTO.getColumnName()).append(",");
                sbParam.append("?").append(",");
                sbColUpdate.append(columnDTO.getColumnName()).append("=").append("?").append(",");
            }
            sbCol.deleteCharAt(sbCol.length() - 1);
            sbParam.deleteCharAt(sbParam.length() - 1);
            sbColUpdate.deleteCharAt(sbColUpdate.length() - 1);

            sb.append("INSERT INTO " + configReport.getDatabaseName() + ".data_log " + "(");
            sb.append(sbCol);
            sb.append(") VALUES (");
            sb.append(sbParam);
            sb.append(")");
            sb.append(" ON DUPLICATE KEY update ");
            sb.append(sbColUpdate);


            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            connection = myWork.getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(sb.toString());
            int i, batch = 0;

            for (Map<String, String> rowData : lstMapData) {
                i = 1;
                //Set cac tham so cho Parameter
                Map<String, String> parameter = new HashMap<>();
                String old_value = null;
                for (Map<String, String> oldData : lstOldData){
                    if(oldData.get("id").equals(rowData.get("id"))){
                        old_value = oldData.get("value");
                        break;
                    }
                }

                StringBuilder new_value = new StringBuilder();
                for (String key : setField) {
                    int check = 0;
                    for(ConfigReportColumnDTO cl :lstColumnConfig){
                        if(cl.getColumnName().equals(key) && (cl.getDataType().equals("DOUBLE") )){
                            check = 1;
                            break;
                        }
                    }
                    if(check == 1){
                        new_value.append(Double.parseDouble(rowData.get(key))).append(";");
                    }
                    else{
                        new_value.append(rowData.get(key)).append(";");
                    }
                }
                new_value.deleteCharAt(new_value.length() - 1);

                if(!old_value.equals(new_value.toString())){
                    parameter.put("old_value", old_value);
                    parameter.put("new_value", new_value.toString());
                    parameter.put("record_id", rowData.get("id"));
                    parameter.put("schema_name", configReport.getDatabaseName());
                    parameter.put("table_name", configReport.getTableName());
                    if(set.contains("yearkey")){
                        parameter.put("yearkey", rowData.get("yearkey"));
                    } else {
                        parameter.put("yearkey", null);
                    }
                    if(set.contains("monkey")){
                        parameter.put("monkey", rowData.get("monkey"));
                    } else {
                        parameter.put("monkey", null);
                    }
                    if(set.contains("quarkey")){
                        parameter.put("quarkey", rowData.get("quarkey"));
                    } else {
                        parameter.put("quarkey", null);
                    }
                    parameter.put("field", field.toString());
                    parameter.put("field_name", field_name.toString());

                    parameter.put("action_type", String.valueOf(actionType));
                    parameter.put("modified_by", rowData.get("update_user"));
                    parameter.put("modified_time", rowData.get("update_time"));
                    // Set cac tham so buoc them moi
                    for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                        setParameter(i++, stmt, columnDTO, parameter);
                    }

//                Set cac tham so buoc cap nhat
                    for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                        setParameter(i++, stmt, columnDTO, parameter);
                    }
                    stmt.addBatch();
                    batch++;
                }
                if (batch % 200 == 0) {
                    stmt.executeBatch();
                    stmt.getConnection().commit();
                }
            }
            stmt.executeBatch();
            stmt.getConnection().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            close(stmt);
        }
    }


    public void insertDataLogDelete(Map<String, Object> mapRowData, ConfigReportDTO configReport, int actionType, Map<String, String> mapLogDelete, List<ConfigReportColumnDTO> lstAllColumn) throws Exception {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            List<ConfigDataLogColumnDTO> lstConfigDataLogColumnDTO = new ArrayList<>();
            Map<String, String> mapColumn = new HashMap<>();
            mapColumn.put("schema_name", "varchar");
            mapColumn.put("table_name", "varchar");
            mapColumn.put("record_id", "BIGINT");
            mapColumn.put("monkey", "INT");
            mapColumn.put("quarkey", "INT");
            mapColumn.put("yearkey", "INT");
            mapColumn.put("field", "varchar");
            mapColumn.put("field_name", "varchar");
            mapColumn.put("old_value", "varchar");
            mapColumn.put("new_value", "varchar");
            mapColumn.put("action_type", "INT");
            mapColumn.put("modified_by", "varchar");
            mapColumn.put("modified_time", "timestamp");
            Set<String> setColumnName = mapColumn.keySet();
            for (String key : setColumnName) {
                ConfigDataLogColumnDTO configDataLogColumnDTO = new ConfigDataLogColumnDTO();
                configDataLogColumnDTO.setColumnName(key);
                configDataLogColumnDTO.setDataType(mapColumn.get(key));
                lstConfigDataLogColumnDTO.add(configDataLogColumnDTO);
            }

            StringBuilder field = new StringBuilder();
            Set<String> set = mapRowData.keySet();
            Set<String> setField = new HashSet<>();
            for (String key : set) {
                if(!key.equals("id")){
                    field.append(key).append(";");
                    setField.add(key);
                }
            }
            StringBuilder field_name = new StringBuilder();
            for(String key: setField){
                for(ConfigReportColumnDTO tmp: lstAllColumn){
                    if(key.equals(tmp.getColumnName())){
                        field_name.append(tmp.getTitle()).append(";");
                    }
                }
            }
            field.deleteCharAt(field.length() - 1);
            field_name.deleteCharAt(field_name.length() - 1);

            StringBuilder sb = new StringBuilder();
            StringBuilder sbCol = new StringBuilder();
            StringBuilder sbParam = new StringBuilder();
            StringBuilder sbColUpdate = new StringBuilder();
            for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                sbCol.append(columnDTO.getColumnName()).append(",");
                sbParam.append("?").append(",");
                sbColUpdate.append(columnDTO.getColumnName()).append("=").append("?").append(",");
            }
            sbCol.deleteCharAt(sbCol.length() - 1);
            sbParam.deleteCharAt(sbParam.length() - 1);
            sbColUpdate.deleteCharAt(sbColUpdate.length() - 1);

            sb.append("INSERT INTO " + configReport.getDatabaseName() + ".data_log " + "(");
            sb.append(sbCol);
            sb.append(") VALUES (");
            sb.append(sbParam);
            sb.append(")");
            sb.append(" ON DUPLICATE KEY update ");
            sb.append(sbColUpdate);


            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            connection = myWork.getConnection();
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement(sb.toString());
            int i, batch = 0;

                i = 1;
                //Set cac tham so cho Parameter
                Map<String, String> parameter = new HashMap<>();
                parameter.put("record_id", mapRowData.get("id").toString());
                parameter.put("schema_name", configReport.getDatabaseName());
                parameter.put("table_name", configReport.getTableName());
                if("4".equals(configReport.getTimeType())){
                    parameter.put("yearkey", mapLogDelete.get("time"));
                } else {
                    parameter.put("yearkey", null);
                }
                if(Constants.formInputYear.equals(configReport.getTimeType())){
                    parameter.put("monkey", mapLogDelete.get("time"));
                } else {
                    parameter.put("monkey", null);
                }
                if("3".equals(configReport.getTimeType())){
                    parameter.put("quarkey", mapLogDelete.get("time"));
                } else {
                    parameter.put("quarkey", null);
                }
                parameter.put("field", field.toString());
                parameter.put("field_name", field_name.toString());
                parameter.put("new_value", "null");
                StringBuilder old_value = new StringBuilder();
                for (String key : setField) {
                    old_value.append(mapRowData.get(key)).append(";");
                }
                old_value.deleteCharAt(old_value.length() - 1);
                parameter.put("old_value", old_value.toString());
                parameter.put("action_type", String.valueOf(actionType));
                parameter.put("modified_by", SecurityUtils.getCurrentUserLogin().get());
                parameter.put("modified_time", mapLogDelete.get("timestamp"));

                // Set cac tham so buoc them moi
                for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                    setParameter(i++, stmt, columnDTO, parameter);
                }

//                Set cac tham so buoc cap nhat
                for (ConfigDataLogColumnDTO columnDTO : lstConfigDataLogColumnDTO) {
                    setParameter(i++, stmt, columnDTO, parameter);
                }
                stmt.addBatch();
                batch++;
                if (batch % 200 == 0) {
                    stmt.executeBatch();
                    stmt.getConnection().commit();
                }
            stmt.executeBatch();
            stmt.getConnection().commit();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            close(stmt);
        }
    }

    public List<String> getModifiedBy(int time, String tableName){
        List<String> lstUserModified = new ArrayList<>();
        try {
            //xử lý khoảng thời gian tìm kiếm
            LocalDate todate = LocalDate.now();
            LocalDate fromdate = null;
            if(time ==1){
                // Go backward to get Monday
                fromdate = todate;
                while (fromdate.getDayOfWeek() != DayOfWeek.MONDAY) {
                    fromdate = fromdate.minusDays(1);
                }
            }
            if(time == 2){
                LocalDate monday = LocalDate.now();
                while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
                    monday = monday.minusDays(1);
                }
                fromdate = monday.minusDays(7);
                todate = monday.minusDays(1);
            }
            if(time ==3){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                // set day to minimum
                calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                fromdate = calendar.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            }
            if(time == 4){
                Calendar aCalendar = Calendar.getInstance();
                // add -1 month to current month
                aCalendar.add(Calendar.MONTH, -1);
                // set DATE to 1, so first date of previous month
                aCalendar.set(Calendar.DATE, 1);
                fromdate = aCalendar.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
                // set actual maximum date of previous month
                aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                todate = aCalendar.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            }
            todate = todate.plusDays(1);
            StringBuilder sb = new StringBuilder("");
            sb.append(" SELECT DISTINCT(modified_by) FROM data_log ");
            sb.append(" WHERE ").append(" table_name = :tableName");
            if(time != 0){
                sb.append(" AND ").append("(modified_time >= :fromdate and modified_time < :todate )");
            }

            Query queryLog = manager.createNativeQuery(sb.toString(),Tuple.class);
            queryLog.setParameter("tableName", tableName);
            if(time != 0){
                queryLog.setParameter("fromdate", fromdate.toString());
                queryLog.setParameter("todate", todate.toString());
            }

            List<Tuple> result = queryLog.getResultList();
            for (Tuple row: result){
                if(row.get("modified_by") == null){
                    lstUserModified.add(null);
                } else {
                    lstUserModified.add(row.get("modified_by").toString());
                }
            }

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        return lstUserModified;
    }

    private void setParameter(int i, PreparedStatement stmt, ConfigDataLogColumnDTO columnDTO, Map<String, String> rowData) throws Exception {
        if (Constants.DATA_TYPE.LONG.equalsIgnoreCase(columnDTO.getDataType()) ||
            Constants.DATA_TYPE.BIGINT.equalsIgnoreCase(columnDTO.getDataType()) ||
            Constants.DATA_TYPE.INT.equalsIgnoreCase(columnDTO.getDataType())) {
            setLongJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else if (Constants.DATA_TYPE.DOUBLE.equalsIgnoreCase(columnDTO.getDataType())) {
            setDoubleJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else if (Constants.DATA_DB_TYPE.TIMESTAMP.equalsIgnoreCase(columnDTO.getDataType())) {
            setDateJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else {
            stmt.setString(i, rowData.containsKey(columnDTO.getColumnName()) ? rowData.get(columnDTO.getColumnName()) : "");
        }
    }

    private void setLongJdbc(PreparedStatement stmt, int index, String value) throws Exception {
        if (!DataUtil.isNullObject(value)) {
            stmt.setLong(index, Double.valueOf(value).longValue());
        } else {
            stmt.setNull(index, Types.BIGINT);
        }
    }

    private void setDoubleJdbc(PreparedStatement stmt, int index, String value) throws Exception {
        if (DataUtil.isNullOrEmpty(value)) {
            stmt.setNull(index, Types.DOUBLE);
        } else {
            if (value.contains(",")) {
                value = value.replace(",", ".");
            }
            stmt.setDouble(index, Double.valueOf(value));
        }
    }

    private void setDateJdbc(PreparedStatement stmt, int index, String value) throws Exception {
        if (!DataUtil.isNullObject(value)) {
            if(value.contains("-")){
                String timeStr = value.substring(0,19).replace("-", "/");
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                stmt.setTimestamp(index, new java.sql.Timestamp(format.parse(timeStr).getTime()));
            }
            else{
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                stmt.setTimestamp(index, new java.sql.Timestamp(format.parse(value).getTime()));
            }
        } else {
            stmt.setNull(index, Types.DATE);
        }
    }

    private Date getDateValue(String value) {
        try {
            return ddMMyyyyHHmmss.parse(value);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            try {
                return ddMMyyyy.parse(value);
            } catch (Exception e) {
                try {
                    return yyyyMMdd.parse(value);
                } catch (Exception ex1) {
                    log.error(ex1.getMessage(), ex1);
                }
                log.error(e.getMessage(), e);
            }
        }
        return new Date();
    }

    public void close(PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
        }
    }

}
