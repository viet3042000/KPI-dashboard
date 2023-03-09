package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.domain.SysRole;
import com.b4t.app.repository.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.net.ntp.TimeStamp;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

//import javafx.scene.chart.ScatterChart;

@Repository
@Transactional
public class ConfigReportImportRepositoryImpl implements ConfigReportImportRepository {
    private static final String ENTITY_NAME = "configReport";
    private final Logger log = LoggerFactory.getLogger(ConfigReportImportRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;
    //fix sonar
    private SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat ddMMyyyyHHmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Autowired
    SysRoleRepository sysRoleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfigReportColumnRepository configReportColumnRepository;

    @Override
    public void importData(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport) throws Exception {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbCol = new StringBuilder();
            StringBuilder sbParam = new StringBuilder();
            StringBuilder sbColUpdate = new StringBuilder();
            for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                sbCol.append(columnDTO.getColumnName()).append(",");
                sbParam.append("?").append(",");
                sbColUpdate.append(columnDTO.getColumnName()).append("=").append("?").append(",");
            }
            sbCol.deleteCharAt(sbCol.length() - 1);
            sbParam.deleteCharAt(sbParam.length() - 1);
            sbColUpdate.deleteCharAt(sbColUpdate.length() - 1);

            sb.append("INSERT INTO " + configReport.getDatabaseName() + "." + configReport.getTableName() + "(");
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
                // Set cac tham so buoc them moi
                for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                    setParameter(i++, stmt, columnDTO, rowData);
                }

                //Set cac tham so buoc cap nhat
                for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                    setParameter(i++, stmt, columnDTO, rowData);
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

    @Override
    public void importUpdateData(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReportColumnDTO keyColumn, ConfigReport configReport) throws Exception {
        PreparedStatement stmt1 = null;
//        List<Map<String, String>> lstOldData = new ArrayList<>();
        try {
            StringBuilder field = new StringBuilder();
            Set<String> set = lstMapData.get(0).keySet();
            Set<String> setField = new HashSet<>();
            for (String key : set) {
                if(!key.equals("id") && !key.equals("update_time") && !key.equals("update_user") && !key.equals("RESULT_VALIDATE_MSG") && !key.equals("yearkey") && !key.equals("monkey") && !key.equals("quarkey")){
                    field.append(key).append(",");
                    setField.add(key);
                }
            }
            field.deleteCharAt(field.length() - 1);
//            //lay du lieu cu truoc khi update de luu log
//            for (Map<String, String> rowData : lstMapData){
//                StringBuilder sbOld = new StringBuilder("");
//                sbOld.append(" SELECT ").append(field.toString()).append(" FROM ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
////                if (Constants.formInputYear.equals(configReport.getformInput())) {
////                    sbOld.append(" WHERE parent_id = :value");
////                } else {
//                sbOld.append(" WHERE ").append(" id = :value");
////                }
////                Query queryLog = manager.createNativeQuery(sbOld.toString());
//////                if (Constants.formInputYear.equals(configReportDTO.getformInput())) {
//////                    queryLog.setParameter("value", mapRowData.get("parent_id"));
//////                } else {
////                queryLog.setParameter("value", rowData.get("id"));
//////                }
//
//                Query queryLog = manager.createNativeQuery(sbOld.toString(),Tuple.class);
//                queryLog.setParameter("value", rowData.get("id"));
//                StringBuilder old_value = new StringBuilder();
//                List<Tuple> result = queryLog.getResultList();
//                for (Tuple row: result){
//                    for (String file : setField){
//                        old_value.append(row.get(file)).append(";");
//                    }
//                }
//                old_value.deleteCharAt(old_value.length() - 1);
//                Map<String, String> rowOld = new HashMap<>();
//                rowOld.put("id", rowData.get("id"));
//                rowOld.put("value",old_value.toString());
//                lstOldData.add(rowOld);
//            }

            StringBuilder sb = new StringBuilder();
            StringBuilder sbColUpdate = new StringBuilder();
            for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                sbColUpdate.append(columnDTO.getColumnName()).append("=").append("?").append(",");
            }
            sbColUpdate.deleteCharAt(sbColUpdate.length() - 1);

            sb.append("UPDATE " + configReport.getDatabaseName() + "." + configReport.getTableName() + " ");

            sb.append(" SET ");
            sb.append(sbColUpdate);
            sb.append(" WHERE " + keyColumn.getColumnName() + " = ? ");


            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            Connection connection = myWork.getConnection();
            connection.setAutoCommit(false);
            stmt1 = connection.prepareStatement(sb.toString());
            int i, batch = 0;

            for (Map<String, String> rowData : lstMapData) {
                i = 1;
                //Set cac tham so buoc cap nhat
                for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                    setParameter(i++, stmt1, columnDTO, rowData);
                }
                setParameter(i++, stmt1, keyColumn, rowData);
                stmt1.addBatch();
                batch++;
                if (batch % 200 == 0) {
                    stmt1.executeBatch();
                    stmt1.getConnection().commit();
                }
            }
            stmt1.executeBatch();
            stmt1.getConnection().commit();

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            close(stmt1);
        }
//        return lstOldData;
    }

    @Override
    public void importDataFormInput(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport) throws Exception {
        PreparedStatement stmt = null;
        Connection connection = null;
        List<PreparedStatement> preparedStatements = new ArrayList<>();
        int parent_id = 0;
//        DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReport.getTimeType()));
        try {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbGetParentId = new StringBuilder();
            StringBuilder sbCol = new StringBuilder();
            StringBuilder sbParam = new StringBuilder();
            StringBuilder sbColUpdate = new StringBuilder();
            for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                sbCol.append(columnDTO.getColumnName()).append(",");
                sbParam.append("?").append(",");
                sbColUpdate.append(columnDTO.getColumnName()).append("=").append("?").append(",");
            }
            sbCol.deleteCharAt(sbCol.length() - 1);
            sbParam.deleteCharAt(sbParam.length() - 1);
            sbColUpdate.deleteCharAt(sbColUpdate.length() - 1);

            sbGetParentId.append("SELECT MAX(parent_id) FROM " + configReport.getDatabaseName() + "." + configReport.getTableName());

            Query queryCount = manager.createNativeQuery(sbGetParentId.toString());
            org.hibernate.query.Query queryHibernateCount = queryCount.unwrap(org.hibernate.query.Query.class);
            List<Object> lstParentId = queryHibernateCount.list();
            List<Map<String, String>> lstDataTmp = new ArrayList<>();
            Map<String, String> tmpElement = new LinkedHashMap<>();
            if (lstParentId.get(0) == null) {
                parent_id = 1;
            } else {
                parent_id = Integer.valueOf(lstParentId.get(0).toString()) + 1;
            }
            for (Map<String, String> rowData : lstMapData) {
                tmpElement.put("parent_id", String.valueOf(parent_id++));
                for (Map.Entry row : rowData.entrySet()) {
                    if (row.getValue() == null) {
                        tmpElement.put(row.getKey().toString(), null);
                    } else {
                        tmpElement.put(row.getKey().toString(), row.getValue().toString());
                    }
                }
                lstDataTmp.add(tmpElement);
                tmpElement = new LinkedHashMap<>();
            }
            lstMapData = lstDataTmp;
            sb.append("INSERT INTO " + configReport.getDatabaseName() + "." + configReport.getTableName() + "(");
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
            String insertQuery = sb.toString();
            String selectedYear = "";
            for (Map<String, String> rowData : lstMapData) {
                if (rowData.get("selectedYear") != null) {
                    selectedYear = rowData.get("selectedYear");
                    break;
                }
            }

//            stmt.executeBatch();
//            stmt.getConnection().commit();
            int maxRec = 30;
            int pool = 20;
            List<ImportThread> importThreads = new ArrayList<>();
            for (int i1 = 0; i1 < Math.ceil(lstMapData.size() * 1.0 / maxRec); i1++) {
                int endIndex = Math.min((i1 + 1) * maxRec, lstMapData.size());
                List<Map<String, String>> subLstMapData = lstMapData.subList(i1 * maxRec, endIndex);
                ImportThread importThread = new ImportThread(subLstMapData, lstColumnConfig, selectedYear, insertQuery, preparedStatements);
                importThreads.add(importThread);
            }
            ExecutorService executorService = Executors.newFixedThreadPool(pool);
            long start = System.currentTimeMillis();
            List<Future> futures = importThreads.stream().map(executorService::submit).collect(Collectors.toList());
            while (futures.stream().anyMatch((future) -> !future.isDone())) {
            }
            executorService.shutdown();
            System.out.println("Duration insert: " + (System.currentTimeMillis() - start));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            preparedStatements.forEach(preparedStatement -> close(preparedStatement));
            System.out.println("closed connection insert excel");
            close(stmt);
        }
    }

    private class ImportThread extends Thread {
        Connection connection;
        List<Map<String, String>> lstMapData;
        List<ConfigReportColumnDTO> lstColumnConfig;
        String selectedYear;
        String insertQuery;
        List<PreparedStatement> preparedStatements;

        public ImportThread(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, String selectedYear, String insertQuery, List<PreparedStatement> preparedStatements) throws SQLException {
            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            connection = myWork.getConnection();
            connection.setAutoCommit(false);
            this.lstMapData = lstMapData;
            this.lstColumnConfig = lstColumnConfig;
            this.selectedYear = selectedYear;
            this.insertQuery = insertQuery;
            this.preparedStatements = preparedStatements;
        }

        @Override
        public void run() {
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(insertQuery);
                preparedStatements.add(stmt);
            } catch (SQLException throwables) {
                log.error(throwables.getMessage(), throwables);
            }
            try {
                int insertIndex = 0;
                int i, batch = 0;
                for (Map<String, String> rowData : lstMapData) {
//                    i = 1;
                    // Set cac tham so buoc them moi
                    insertIndex = 0;
                    for (Map.Entry row : rowData.entrySet()) {
                        i = 1;
                        for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                            setParameterYear(i++, stmt, columnDTO, rowData, row.getKey().toString(), selectedYear);
                        }
                        for (ConfigReportColumnDTO columnDTO : lstColumnConfig) {
                            setParameterYear(i++, stmt, columnDTO, rowData, row.getKey().toString(), selectedYear);
                        }
                        insertIndex++;
                        if (!"id".equals(row.getKey()) && !"selectedYear".equals(row.getKey()) && !"parent_id".equals(row.getKey()) && !"gia_tri".equals(row.getKey()) && insertIndex > rowData.size() - 17) {
                            stmt.addBatch();
                            batch++;
                        }
                    }
                    if (batch % 200 == 0) {
                        stmt.executeBatch();
                    }
                }
                if (batch % 200 != 0) {
                    stmt.executeBatch();
                }
                stmt.getConnection().commit();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void importUpdateDataFormInput(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport) throws Exception {
        PreparedStatement stmt1 = null;
        try {

            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            Connection connection = myWork.getConnection();
            connection.setAutoCommit(false);


            StringBuilder sbDelete = new StringBuilder();
            sbDelete.append("DELETE FROM " + configReport.getDatabaseName() + "." + configReport.getTableName() + " WHERE PARENT_ID = ? ");
            stmt1 = connection.prepareStatement(sbDelete.toString());
            for (Map<String, String> rowData : lstMapData) {
                if (rowData.get("selectedYear") == null) {
                    setLongJdbc(stmt1, 1, rowData.get("parent_id"));
                    stmt1.addBatch();
                    stmt1.executeBatch();
                    stmt1.getConnection().commit();
                }
            }
            importDataFormInput(lstMapData, lstColumnConfig, configReport);


        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            close(stmt1);
        }
    }

    public boolean validateExcel(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, String selectedYear) {
        String value = selectedYear;
        Map<String, String> params = new HashMap<>();
        String username = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        String lstDeptCode = userRepository.getUserByLogin(username);

        StringBuilder sbCount = new StringBuilder("");
        sbCount.append(" SELECT ");
        sbCount.append(" DISTINCT dept_permission_code, ma_lv_cap1, ma_lv_cap2, ma_lv_cap3, ma_lv_cap4, ma_lv_cap5 ");

        sbCount.append(" FROM ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
        sbCount.append(" WHERE 1=1 ");
        sbCount.append(" AND ").append("(monkey").append(" like Concat(:dataTime,'%') ");
        sbCount.append(" OR ").append("quarkey").append(" like Concat(:dataTime,'%') ");
        sbCount.append(" OR ").append("yearkey").append(" = :dataTime )");
        if (lstDeptCode != null && lstDeptCode.length() != 0) {
            String[] arrDeptCode = lstDeptCode.split(";");
            if (arrDeptCode.length == 1) {
                sbCount.append("and dept_permission_code like Concat('%',:deptCode,'%') ");
                params.put("deptCode", arrDeptCode[0]);
            } else {
                for (int i = 0; i < arrDeptCode.length; i++) {
                    if (i == 0) {
                        sbCount.append("and (dept_permission_code like Concat('%',:deptCode" + i + ",'%') ");
                        params.put("deptCode" + i, arrDeptCode[i]);
                    } else {
                        sbCount.append(" or dept_permission_code like Concat('%',:deptCode" + i + ",'%') ");
                        params.put("deptCode" + i, arrDeptCode[i]);
                    }
                    if (i == arrDeptCode.length - 1) {
                        sbCount.append(")");
                    }
                }
            }
        }
        params.put("dataTime", value);


        Query queryCount = manager.createNativeQuery(sbCount.toString());
        org.hibernate.query.Query queryHibernateCount = queryCount.unwrap(org.hibernate.query.Query.class);

        params.forEach((paramKey, paramValue) ->

        {
            queryHibernateCount.setParameter(paramKey, paramValue);
        });
        queryHibernateCount.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> lstPermissionRowData = queryHibernateCount.list();

        List<String> lstDeptMaLv = new ArrayList<>();
        List<String> lstDeptMaLvExcel = new ArrayList<>();
        for (Map<String, Object> rowData : lstPermissionRowData) {
            for (Map.Entry row : rowData.entrySet()) {
                if ("ma_lv_cap1".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLv.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap2".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLv.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap3".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLv.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap4".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLv.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap5".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLv.add(rowData.get("dept_permission_code").toString() + row.getValue());
                }
            }
        }

        for (Map<String, String> rowData : lstMapData) {
            for (Map.Entry row : rowData.entrySet()) {
                if ("ma_lv_cap1".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap2".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap3".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap4".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(rowData.get("dept_permission_code").toString() + row.getValue());
                } else if ("ma_lv_cap5".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(rowData.get("dept_permission_code").toString() + row.getValue());
                }
            }
        }
        Collections.sort(lstDeptMaLv);
        Collections.sort(lstDeptMaLvExcel);
//        check = lstDeptMaLv.equals(lstDeptMaLvExcel);
        /**
         * check lstDeptMaLvExcel có bất kỳ element nào không thuộc lstDeptMaLv
         */
        return !lstDeptMaLvExcel.stream().anyMatch(e -> !lstDeptMaLv.contains(e));
    }

    /**
     * check dòng bị trống mã lĩnh vực (dòng thừa)
     */
    public void checkNoMaLVRow(List<Map<String, String>> lstMapData) throws Exception {
        for (Map<String, String> rowData : lstMapData) {
            boolean check = false;
            // mapData không chứa mã lĩnh vực
            if (!Constants.LIST_CODE_MALV.stream().anyMatch(e -> rowData.keySet().contains(e))){
                continue;
            }
            for (Map.Entry row : rowData.entrySet()) {
                if ("ma_lv_cap1".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    check = true;
                } else if ("ma_lv_cap2".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    check = true;
                } else if ("ma_lv_cap3".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    check = true;
                } else if ("ma_lv_cap4".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    check = true;
                } else if ("ma_lv_cap5".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    check = true;
                }
            }
            if(!check){
                throw new BadRequestAlertException(Translator.toLocale("error.configReport.lackOfCode"), ENTITY_NAME, "configReport.checkNoMaLVRow");
            }
        }
    }

    public boolean checkDuplicateMaLV(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, String selectedYear) throws Exception {
        boolean check = false;
//        List<String> lstDeptMaLvExcel = new ArrayList<>();
        int index = 0;
        label1:
        for (Map<String, String> rowData : lstMapData) {
            index = 0;
            for (Map.Entry row : rowData.entrySet()) {

                if ("ma_lv_cap1".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    index++;
                } else if ("ma_lv_cap2".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    index++;
                } else if ("ma_lv_cap3".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    index++;
                } else if ("ma_lv_cap4".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    index++;
                } else if ("ma_lv_cap5".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    index++;
                }
                if (index > 1) {
                    check = true;
                    break label1;
                }
            }
        }
        return check;
    }

    public boolean checkDuplicateDataRow(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, String selectedYear) throws Exception {
        boolean check = false;
        List<String> lstDeptMaLvExcel = new ArrayList<>();
        HashSet<String> set = new HashSet<String>();
        for (Map<String, String> rowData : lstMapData) {
            for (Map.Entry row : rowData.entrySet()) {

                if ("ma_lv_cap1".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(row.getValue() + rowData.get("ten_lv").toString());
                } else if ("ma_lv_cap2".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(row.getValue() + rowData.get("ten_lv").toString());
                } else if ("ma_lv_cap3".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(row.getValue() + rowData.get("ten_lv").toString());
                } else if ("ma_lv_cap4".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(row.getValue() + rowData.get("ten_lv").toString());
                } else if ("ma_lv_cap5".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(row.getValue() + rowData.get("ten_lv").toString());
                }

            }
        }
        for (int i = 0; i < lstDeptMaLvExcel.size(); i++) {
            set.add(lstDeptMaLvExcel.get(i));
        }
        if (set.size() == lstDeptMaLvExcel.size()) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    public boolean validateExcelAdmin(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, String selectedYear) {
        boolean check = false;
//        String value = selectedYear;
//        Map<String, String> params = new HashMap<>();
        String username = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        String strDeptCode = userRepository.getUserByLogin(username);
        String[] arrDeptCode = strDeptCode.split(";");
        List<String> lstDeptCode = Arrays.asList(arrDeptCode);
        TreeSet myTreeSet = new TreeSet();
        List<String> lstDeptMaLvExcel = new ArrayList<>();

        for (Map<String, String> rowData : lstMapData) {
            for (Map.Entry row : rowData.entrySet()) {
                if ("dept_permission_code".equals(row.getKey().toString()) && !"".equals(row.getValue())) {
                    lstDeptMaLvExcel.add(rowData.get("dept_permission_code").toString());
                }
            }
        }
        Collections.sort(lstDeptCode);
        Collections.sort(lstDeptMaLvExcel);
        for (int i = 0; i < lstDeptMaLvExcel.size(); i++) {
            myTreeSet.add(lstDeptMaLvExcel.get(i));
        }
        check = lstDeptCode.equals(new ArrayList<Object>(myTreeSet));

        return check;
    }

    @Override
    public void importUpdateDataFormInputExcel(List<Map<String, String>> lstMapData, List<ConfigReportColumnDTO> lstColumnConfig, ConfigReport configReport, String selectedYear) throws Exception {
//        PreparedStatement stmt1 = null;
        Query query = null;
        try {
            /**
             * check dòng bị trống mã lĩnh vực (dòng thừa)
             */
            checkNoMaLVRow(lstMapData);
            String lockRow = configReport.getLockRow();
            if ("1".equals(lockRow)) {
                if (checkDuplicateMaLV(lstMapData, lstColumnConfig, configReport, selectedYear)) {
                    throw new BadRequestAlertException(Translator.toLocale("error.configReport.multipleCode"), ENTITY_NAME, "configReport.duplicateMaLVInvalid");
                } else if (!checkDuplicateDataRow(lstMapData, lstColumnConfig, configReport, selectedYear)) {
                    throw new BadRequestAlertException(Translator.toLocale("error.configReport.duplicateRow"), ENTITY_NAME, "configReport.duplicateRow");
                } else if (!validateExcel(lstMapData, lstColumnConfig, configReport, selectedYear)) {
                    throw new BadRequestAlertException(Translator.toLocale("error.configReport.rowInvalid"), ENTITY_NAME, "configReport.permissionCodeInvalid");
                }
            } else {
//                if(!validateExcelAdmin(lstMapData, lstColumnConfig, configReport, selectedYear)){
//                    throw new BadRequestAlertException(Translator.toLocale("error.configReport.rowInvalid"), ENTITY_NAME, "configReport.permissionCodeInvalid");
//                }
            }

            Session session = manager.unwrap(Session.class);
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            Connection connection = myWork.getConnection();
            connection.setAutoCommit(false);


            StringBuilder sb = new StringBuilder("");
            sb.append(" DELETE FROM ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
//        sb.append(" WHERE ").append(columnTime).append("= :dataTime");
            sb.append(" WHERE 1=1 and :ten_ma_lv = ?");
            sb.append(" AND ").append("(monkey").append(" like Concat(?,'%') ");
            sb.append(" OR ").append("quarkey").append(" like Concat(?,'%') ");
            sb.append(" OR ").append("yearkey").append(" = ? )");

            String tenMaLv = "";
            String maLv = "";
            int batch = 0;
            try (PreparedStatement stmt1 = connection.prepareStatement(sb.toString())) {
//                stmt1 = connection.prepareStatement(sb.toString());
//            query = manager.createNativeQuery(sb.toString());
                String queryDelete = "";
                int nUpdate = 0;
                long start = System.currentTimeMillis();
                for (int i = 0; i < lstMapData.size(); i++) {
                    tenMaLv = "";
                    maLv = "";
                    for (Map.Entry<String, String> mapElement : lstMapData.get(i).entrySet()) {
                        if (!"".equals(mapElement.getValue()) && mapElement.getKey().contains("ma_lv")) {
                            tenMaLv = mapElement.getKey();
                            maLv = mapElement.getValue();
                            break;
                        }
                    }
                    if (!"".equals(tenMaLv)) {

                        if ("ma_lv_cap1".equals(tenMaLv)) {
                            queryDelete = sb.toString().replace(":ten_ma_lv", tenMaLv);
                        } else if ("ma_lv_cap2".equals(tenMaLv)) {
                            queryDelete = sb.toString().replace(":ten_ma_lv", tenMaLv);
                        } else if ("ma_lv_cap3".equals(tenMaLv)) {
                            queryDelete = sb.toString().replace(":ten_ma_lv", tenMaLv);
                        } else if ("ma_lv_cap4".equals(tenMaLv)) {
                            queryDelete = sb.toString().replace(":ten_ma_lv", tenMaLv);
                        } else if ("ma_lv_cap5".equals(tenMaLv)) {
                            queryDelete = sb.toString().replace(":ten_ma_lv", tenMaLv);
                        }
//                    query = manager.createNativeQuery(queryDelete);
//                    query.setParameter("ma_lv", maLv);
//                    query.setParameter("selectedYear", selectedYear);
//                    query.setParameter("selectedYear", selectedYear);
//                    query.setParameter("selectedYear", selectedYear);
//                    query.executeUpdate();
                        try (PreparedStatement stmt2 = connection.prepareStatement(queryDelete)) {
//                        stmt1 = connection.prepareStatement(queryDelete);
                            stmt2.setString(1, maLv);
                            stmt2.setString(2, selectedYear);
                            stmt2.setString(3, selectedYear);
                            stmt2.setString(4, selectedYear);
                            stmt2.addBatch();
                            stmt2.executeBatch();
                            batch++;

                            nUpdate = i;
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
                System.out.println("nUpdate " + nUpdate);

                stmt1.getConnection().commit();
//            connection.commit();
                System.out.println("Duration delete: " + (System.currentTimeMillis() - start));
                importDataFormInput(lstMapData, lstColumnConfig, configReport);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        } finally {
        }
    }

    public void setParameter(int i, PreparedStatement stmt, ConfigReportColumnDTO columnDTO, Map<String, String> rowData) throws Exception {
        if (Constants.DATA_TYPE.LONG.equalsIgnoreCase(columnDTO.getDataType()) ||
            Constants.DATA_TYPE.BIGINT.equalsIgnoreCase(columnDTO.getDataType()) ||
            Constants.DATA_TYPE.INT.equalsIgnoreCase(columnDTO.getDataType())) {
            setLongJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else if (Constants.DATA_TYPE.DOUBLE.equalsIgnoreCase(columnDTO.getDataType())) {
            setDoubleJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(columnDTO.getDataType())) {
            setDateJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else {
            stmt.setString(i, rowData.containsKey(columnDTO.getColumnName()) ? rowData.get(columnDTO.getColumnName()) : "");
        }
    }

    private void setParameterYear(int i, PreparedStatement stmt, ConfigReportColumnDTO columnDTO, Map<String, String> rowData, String key, String selectedYear) throws Exception {
        if (Constants.DATA_TYPE.LONG.equalsIgnoreCase(columnDTO.getDataType()) ||
            Constants.DATA_TYPE.BIGINT.equalsIgnoreCase(columnDTO.getDataType()) ||
            Constants.DATA_TYPE.INT.equalsIgnoreCase(columnDTO.getDataType())) {
            if ("thang_1".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "01");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_2".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "02");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_3".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "03");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_4".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "04");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_5".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "05");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_6".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "06");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_7".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "07");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_8".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "08");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_9".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "09");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_10".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "10");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_11".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "11");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("thang_12".equals(key)) {
                if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "12");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("quy_I".equals(key)) {
                if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "1");
                } else if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("quy_II".equals(key)) {
                if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "2");
                } else if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("quy_III".equals(key)) {
                if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "3");
                } else if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("quy_IV".equals(key)) {
                if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear + "4");
                } else if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else if ("year".equals(key)) {
                if ("yearkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, selectedYear);
                } else if ("monkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                } else if ("quarkey".equals(columnDTO.getColumnName())) {
                    setLongJdbc(stmt, i, "");
                }
            } else {
                setLongJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
            }

        } else if (Constants.DATA_TYPE.DOUBLE.equalsIgnoreCase(columnDTO.getDataType())) {

            if ("thang_1".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_1"));
            } else if ("thang_2".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_2"));
            } else if ("thang_3".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_3"));
            } else if ("thang_4".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_4"));
            } else if ("thang_5".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_5"));
            } else if ("thang_6".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_6"));
            } else if ("thang_7".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_7"));
            } else if ("thang_8".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_8"));
            } else if ("thang_9".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_9"));
            } else if ("thang_10".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_10"));
            } else if ("thang_11".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_11"));
            } else if ("thang_12".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("thang_12"));
            } else if ("quy_I".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("quy_I"));
            } else if ("quy_II".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("quy_II"));
            } else if ("quy_III".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("quy_III"));
            } else if ("quy_IV".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("quy_IV"));
            } else if ("year".equals(key) && "gia_tri".equals(columnDTO.getColumnName())) {
                setDoubleJdbc(stmt, i, rowData.get("year"));
            } else {
                setDoubleJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
            }
        } else if (Constants.DATA_TYPE.DATE.equalsIgnoreCase(columnDTO.getDataType())) {
            setDateJdbc(stmt, i, rowData.get(columnDTO.getColumnName()));
        } else {
            stmt.setString(i, rowData.containsKey(columnDTO.getColumnName()) ? rowData.get(columnDTO.getColumnName()) : "");
        }
    }

    private Date getDateValue(String value) {
        try {
            return ddMMyyyy.parse(value);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            try {
                return ddMMyyyyHHmmss.parse(value);
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

    private void setDateJdbc(PreparedStatement stmt, int index, String value) throws Exception {
        if (!DataUtil.isNullObject(value)) {
            stmt.setDate(index, new java.sql.Date(getDateValue(value).getTime()));
        } else {
            stmt.setNull(index, Types.DATE);
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

    /**
     * Tim kiem danh sach chi tiet hoac dem so luong phan tu
     *
     * @param configReportDataForm
     * @param lstColumn
     * @param configReport
     * @param pageable
     * @param columnTime
     * @param isCount
     * @return
     */
    public List<Object> findDataConfigReportFormInput(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                                      ConfigReport configReport, Pageable pageable, String columnTime, boolean isCount, String primaryKey) {
        String value = DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReport.getTimeType()));
        Map<String, String> params = new HashMap<>();

        String username = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        String lstDeptCode = userRepository.getUserByLogin(username);
//        List<String> lstRole = sysRoleRepository.findAllByUser(username).stream().map(SysRole::getCode).collect(Collectors.toList());
//        List<String> lstColumnName = configReportColumnRepository.findAllByReportIdEquals(configReportDataForm.getReportId()).stream().map(e -> e.getColumnName().toLowerCase()).collect(Collectors.toList());

        StringBuilder sbCount = new StringBuilder("");
        sbCount.append(" SELECT ");
        sbCount.append(" * ");
//        if (isCount) {
//            sbCount.append(" count(*) ");
//        } else {
//            StringBuilder sbField = new StringBuilder("");
//            for (ConfigReportColumnDTO columnDTO : lstColumn) {
//                sbField.append(columnDTO.getColumnName()).append(",");
//            }
//            if (sbField.length() > 0) {
//                sbField = sbField.deleteCharAt(sbField.length() - 1);
//            }
//            sbCount.append(sbField);
//        }
        sbCount.append(" FROM ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
        sbCount.append(" WHERE 1=1 ");
        sbCount.append(" AND ").append("(monkey").append(" like Concat(:dataTime,'%') ");
        sbCount.append(" OR ").append("quarkey").append(" like Concat(:dataTime,'%') ");
        sbCount.append(" OR ").append("yearkey").append(" = :dataTime )");
        if (lstDeptCode != null && lstDeptCode.length() != 0 && "1".equals(configReport.getRowPermission())) {
            String[] arrDeptCode = lstDeptCode.split(";");
            if (arrDeptCode.length == 1) {
                sbCount.append("and dept_permission_code like Concat('%',:deptCode,'%') ");
                params.put("deptCode", arrDeptCode[0]);
            } else {
                for (int i = 0; i < arrDeptCode.length; i++) {
                    if (i == 0) {
                        sbCount.append("and (dept_permission_code like Concat('%',:deptCode" + i + ",'%') ");
                        params.put("deptCode" + i, arrDeptCode[i]);
                    } else {
                        sbCount.append(" or dept_permission_code like Concat('%',:deptCode" + i + ",'%') ");
                        params.put("deptCode" + i, arrDeptCode[i]);
                    }
                    if (i == arrDeptCode.length - 1) {
                        sbCount.append(")");
                    }
                }
            }
        }
//        if (value.length() == 4) {
//            sbCount.append(" AND ").append(columnTime).append(" like Concat(:dataTime,'%') ");
//        } else {
//            sbCount.append(" AND ").append(columnTime).append(" = :dataTime ");
//        }
        params.put("dataTime", value);

//        if ((lstRole == null || !
//
//            isNormalUser(lstRole)) && lstColumnName.contains(Constants.UPDATE_USER)) {
//            sbCount.append(" AND ").append(Constants.UPDATE_USER).append(" = ").append(":updateUser");
//            params.put("updateUser", username);
//        }
        if (!DataUtil.isNullOrEmpty(primaryKey)) {
            sbCount.append(" ORDER BY ").append(primaryKey).append(" ASC ");
        }

        Query queryCount = manager.createNativeQuery(sbCount.toString());
        org.hibernate.query.Query queryHibernateCount = queryCount.unwrap(org.hibernate.query.Query.class);
//        if (pageable != null && !isCount) {
//            queryHibernateCount.setFirstResult(((int) pageable.getOffset()) * 17).setMaxResults(((int) pageable.getPageSize()) * 17);
//        }
        params.forEach((paramKey, paramValue) ->

        {
            queryHibernateCount.setParameter(paramKey, paramValue);
        });
        queryHibernateCount.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> lstCountColumn = queryHibernateCount.list();


//        List<Object> lstCount = queryHibernateCount.list();
        List<Map<String, Object>> objectList = new ArrayList<>();
        List<Object> resustList = new ArrayList<>();
        List<Object> itemLst = new ArrayList<>();

        Object[] rowData ;
        int index = 0;
        boolean check = false;
        boolean checkEndLst = false;
//        int size = lstColumn.size() + 17;
        int indexMonth = 0;
        List<ConfigReportColumnDTO> tmpLstColumn = lstColumn;
        for (
            int i = 0; i < tmpLstColumn.size(); i++) {
            if ("thang_1".equals(tmpLstColumn.get(i).getColumnName())) {
                indexMonth = i;
                lstColumn = new ArrayList<>(tmpLstColumn.subList(0, indexMonth));
                break;
            }
        }

        label1:
        for (int i = 0; i < lstCountColumn.size(); i++) {
            if (!checkEndLst) {
                objectList.add(lstCountColumn.get(i));
            }
            for (int j = i + 1; j < lstCountColumn.size(); j++) {
                Map<String, Object> currentEle = lstCountColumn.get(i);
                Map<String, Object> nextEle = lstCountColumn.get(j);

                if (currentEle.get("parent_id").toString().equals(nextEle.get("parent_id").toString()) && !checkEndLst) {
                    objectList.add(nextEle);
                    check = false;
                    if (j == lstCountColumn.size() - 1)
                        checkEndLst = true;

                } else {
                    for (int k = 0; k < lstColumn.size(); k++) {
                        if (objectList.get(0).get(lstColumn.get(k).getColumnName()) != null) {
                            itemLst.add(objectList.get(0).get(lstColumn.get(k).getColumnName()).toString());
                        } else {
                            itemLst.add("");
                        }
                    }
                    for (Map<String, Object> ob : objectList) {
                        for (Map.Entry<String, Object> entry : ob.entrySet()) {
                            if ("monkey".equals(entry.getKey().toString()) && entry.getValue() != null) {
//                                Integer.valueOf(entry.getValue().toString().replace(value, ""));
                                itemLst.add(ob.get("gia_tri"));
                            } else if ("quarkey".equals(entry.getKey().toString()) && entry.getValue() != null) {
                                itemLst.add(ob.get("gia_tri"));
                            } else if ("yearkey".equals(entry.getKey().toString()) && entry.getValue() != null) {
                                itemLst.add(ob.get("gia_tri"));
                            }
                        }

                    }

//                    resustList.add(new Object[]{"",Arrays.stream((Object[])objectList.get(0)).toArray()[7].toString(),Double.valueOf(Arrays.stream((Object[])objectList.get(0)).toArray()[6].toString()),Double.valueOf(Arrays.stream((Object[])objectList.get(0)).toArray()[6].toString()),Double.valueOf(Arrays.stream((Object[])objectList.get(0)).toArray()[6].toString())});
                    rowData = new Object[itemLst.size()];
                    for (int k = 0; k < itemLst.size(); k++) {
                        rowData[k] = itemLst.get(k);
                    }
                    resustList.add(rowData);
                    itemLst = new ArrayList<>();
                    index = j;
                    check = true;
                    if (checkEndLst) {
                        break label1;
                    }
                    break;
                }
            }
            if (check) {
                i = index - 1;
                objectList = new ArrayList<>();
                check = false;
            }
        }

        //        resustList = resustList.stream().sorted((o1, o2) -> {
//            if (Integer.valueOf(Arrays.stream((Object[])o1).toArray()[1].toString()) <= Integer.valueOf(Arrays.stream((Object[])o2).toArray()[1].toString())) {
//                return -1;
//            } else {
//                return 1;
//            }
//        }).collect(Collectors.toList());
//        List<Object> resustListByDeptCode = new ArrayList<>();
//        if (lstDeptCode != null && lstDeptCode.length() != 0) {
//            String[] arrDeptCode = lstDeptCode.split(";");
//            String[] arrDeptCodeResult;
//            int indexDeptCode = 0;
//            for (int i = 0; i < lstColumn.size(); i++) {
//                if("dept_permission_code".equals(lstColumn.get(i).getColumnName())){
//                    indexDeptCode = i;
//                }
//            }
//            System.out.println("resustList.size(): " + resustList.size());
//            for (int i = 0; i < resustList.size(); i++) {
//                arrDeptCodeResult = Arrays.stream((Object[]) resustList.get(i)).toArray()[indexDeptCode].toString().split(";");
//                for (int j = 0; j < arrDeptCodeResult.length; j++) {
//                    for (int k = 0; k < arrDeptCode.length; k++) {
//                        if(arrDeptCodeResult[j].equals(arrDeptCode[k])){
//                            resustListByDeptCode.add(resustList.get(i));
//                            System.out.println("isDeptCode: true");
//                            break;
//                        }
//                    }
//                }
//            }
//            resustList = resustListByDeptCode;
//        }
        System.out.println("resustList.size() final : " + resustList.size());
        resustList = resustList.stream().sorted((o1, o2) -> {
                String num1 = "";
                String num2 = "";
                for (int i = 4; i < 9; i++) {
                    if (!"".equals(Arrays.stream((Object[]) o1).toArray()[i].toString())) {
                        num1 = Arrays.stream((Object[]) o1).toArray()[i].toString();
                    }
                    if (!"".equals(Arrays.stream((Object[]) o2).toArray()[i].toString())) {
                        num2 = Arrays.stream((Object[]) o2).toArray()[i].toString();
                    }
                }
                    return num1.compareTo(num2);
                }).collect(Collectors.toList() );
        if (pageable != null && !isCount && resustList.size() > 10) {
            resustList = resustList.subList((int) pageable.getOffset(), (int) pageable.getPageSize() + (int) pageable.getOffset());
        }

        return resustList;
    }

    /**
     * Tim kiem danh sach chi tiet hoac dem so luong phan tu
     *
     * @param configReportDataForm
     * @param lstColumn
     * @param configReport
     * @param pageable
     * @param columnTime
     * @param isCount
     * @return
     */
    public List<Object> findDataConfigReport(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                             ConfigReport configReport, Pageable pageable, String columnTime, boolean isCount, String primaryKey) {
        String value = DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReport.getTimeType()));
        Map<String, String> params = new HashMap<>();

        String username = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        List<String> lstRole = sysRoleRepository.findAllByUser(username).stream().map(SysRole::getCode).collect(Collectors.toList());
        List<String> lstColumnName = configReportColumnRepository.findAllByReportIdEquals(configReportDataForm.getReportId()).stream().map(e -> e.getColumnName().toLowerCase()).collect(Collectors.toList());

        StringBuilder sbCount = new StringBuilder("");
        sbCount.append(" SELECT ");
        if (isCount) {
            sbCount.append(" count(*) ");
        } else {
            StringBuilder sbField = new StringBuilder("");
            for (ConfigReportColumnDTO columnDTO : lstColumn) {
                sbField.append(columnDTO.getColumnName()).append(",");
            }
            if (sbField.length() > 0) {
                sbField = sbField.deleteCharAt(sbField.length() - 1);
            }
            sbCount.append(sbField);
        }
        sbCount.append(" FROM ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
        sbCount.append(" WHERE 1=1 ");
        sbCount.append(" AND ").append(columnTime).append(" = :dataTime");
        params.put("dataTime", value);
        if ((lstRole == null || !isNormalUser(lstRole)) && lstColumnName.contains(Constants.UPDATE_USER)) {
            sbCount.append(" AND ").append(Constants.UPDATE_USER).append(" = ").append(":updateUser");
            params.put("updateUser", username);
        }
        if (!DataUtil.isNullOrEmpty(primaryKey)) {
            sbCount.append(" ORDER BY ").append(primaryKey).append(" ASC ");
        }
        Query queryCount = manager.createNativeQuery(sbCount.toString());
        org.hibernate.query.Query queryHibernateCount = queryCount.unwrap(org.hibernate.query.Query.class);
        if (pageable != null && !isCount) {
            queryHibernateCount.setFirstResult((int) pageable.getOffset()).setMaxResults((int) pageable.getPageSize());
        }
        params.forEach((paramKey, paramValue) -> {
            queryHibernateCount.setParameter(paramKey, paramValue);
        });
        List<Object> lstCount = queryHibernateCount.list();
        return lstCount;
    }

    /**
     * Tim kiem phan trang
     *
     * @param configReportDataForm
     * @param lstColumn
     * @param configReport
     * @param pageable
     * @param columnTime
     * @return
     */
    @Override
    public Page<Object> findDataConfigReport(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                             ConfigReport configReport, Pageable pageable, String columnTime, String primaryKey) {

        Long total = 0L;
        List<Object> lstObj = findDataConfigReport(configReportDataForm, lstColumn, configReport, pageable, columnTime, false, primaryKey);
        List<Object> lstCount = findDataConfigReport(configReportDataForm, lstColumn, configReport, pageable, columnTime, true, primaryKey);
        if (!DataUtil.isNullOrEmpty(lstCount)) {
            total = Long.valueOf(lstCount.size());
        }
        return new PageImpl<>(lstObj, pageable, total);
    }

    /**
     * Tim kiem phan trang
     *
     * @param configReportDataForm
     * @param lstColumn
     * @param configReport
     * @param pageable
     * @param columnTime
     * @return
     */
    @Override
    public Page<Object> findDataConfigReportFormInput(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstColumn,
                                                      ConfigReport configReport, Pageable pageable, String columnTime, String primaryKey) {

        Long total = 0L;
        List<Object> lstObj = findDataConfigReportFormInput(configReportDataForm, lstColumn, configReport, pageable, columnTime, false, primaryKey);
        List<Object> lstCount = findDataConfigReportFormInput(configReportDataForm, lstColumn, configReport, pageable, columnTime, true, primaryKey);
        if (!DataUtil.isNullOrEmpty(lstCount)) {
            total = Long.valueOf(lstCount.size());
        }
        return new PageImpl<>(lstObj, pageable, total);
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

    @Override
    public void deleteDataConfigReports(ConfigReportDataForm configReportDataForm, String columnTime, ConfigReport configReport) throws Exception {
        String timeValue = DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReport.getTimeType()));

        String username = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        Map<String, String> mapParam = new HashMap<>();
        List<String> lstRole = sysRoleRepository.findAllByUser(username).stream().map(SysRole::getCode).collect(Collectors.toList());
        List<String> lstColumnName = configReportColumnRepository.findAllByReportIdEquals(configReportDataForm.getReportId()).stream().map(e -> e.getColumnName().toLowerCase()).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("");
        sb.append(" DELETE from ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
        sb.append(" WHERE ").append(columnTime).append("= :dataTime");
        mapParam.put("dataTime", timeValue);
        if ((lstRole == null || !isNormalUser(lstRole)) && lstColumnName.contains(Constants.UPDATE_USER)) {
            sb.append(" AND ").append(Constants.UPDATE_USER).append(" = ").append(":updateUser");
            mapParam.put("updateUser", username);
        }
//        Query queue = manager.createNativeQuery("select * from mic");
        Query query = manager.createNativeQuery(sb.toString());
        mapParam.forEach((paramKey, paramValue) -> {
            query.setParameter(paramKey, paramValue);
        });
        query.executeUpdate();

    }

    public void deleteDataConfigReportsFormInput(ConfigReportDataForm configReportDataForm, String columnTime, ConfigReport configReport) throws Exception {
        String timeValue = DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReport.getTimeType()));

        String username = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        Map<String, String> mapParam = new HashMap<>();
        List<String> lstRole = sysRoleRepository.findAllByUser(username).stream().map(SysRole::getCode).collect(Collectors.toList());
        List<String> lstColumnName = configReportColumnRepository.findAllByReportIdEquals(configReportDataForm.getReportId()).stream().map(e -> e.getColumnName().toLowerCase()).collect(Collectors.toList());
//        String value = DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReport.getTimeType()));


        String lstDeptCode = userRepository.getUserByLogin(username);

        StringBuilder sb = new StringBuilder("");
        sb.append(" DELETE from ").append(configReport.getDatabaseName()).append(".").append(configReport.getTableName());
//        sb.append(" WHERE ").append(columnTime).append("= :dataTime");
        sb.append(" WHERE 1=1 ");
        sb.append(" AND ").append(" (monkey").append(" like Concat(:dataTime,'%') ");
        sb.append(" OR ").append("quarkey").append(" like Concat(:dataTime,'%') ");
        sb.append(" OR ").append("yearkey").append(" = :dataTime )");
        mapParam.put("dataTime", timeValue);
        if ((lstRole == null || !isNormalUser(lstRole)) && lstColumnName.contains(Constants.UPDATE_USER)) {
            sb.append(" AND ").append(Constants.UPDATE_USER).append(" = ").append(":updateUser");
            mapParam.put("updateUser", username);
        }
        if (lstDeptCode != null && lstDeptCode.length() != 0) {
            String[] arrDeptCode = lstDeptCode.split(";");
            if (arrDeptCode.length == 1) {
                sb.append("and dept_permission_code like Concat('%',:deptCode,'%') ");
                mapParam.put("deptCode", arrDeptCode[0]);
            } else {
                for (int i = 0; i < arrDeptCode.length; i++) {
                    if (i == 0) {
                        sb.append("and (dept_permission_code like Concat('%',:deptCode" + i + ",'%') ");
                        mapParam.put("deptCode" + i, arrDeptCode[i]);
                    } else {
                        sb.append(" or dept_permission_code like Concat('%',:deptCode" + i + ",'%') ");
                        mapParam.put("deptCode" + i, arrDeptCode[i]);
                    }
                    if (i == arrDeptCode.length - 1) {
                        sb.append(")");
                    }
                }
            }
        }

        Query query = manager.createNativeQuery(sb.toString());
        mapParam.forEach((paramKey, paramValue) -> {
            query.setParameter(paramKey, paramValue);
        });

        query.executeUpdate();
    }

    private boolean isNormalUser(List<String> lstRole) {
        List<String> roles = new ArrayList<>();
        roles.add(Constants.ROLE.ROLE_ADMIN);
        roles.add(Constants.ROLE.ROLE_USER);
        return lstRole.stream().filter(e -> roles.contains(e)).findFirst().isPresent();
    }

    @Override
    public Map<String, String> deleteRowDataConfigReports(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, ConfigReportColumnDTO primaryKey) {
        //lay gia tri time cua ban ghi de luu log
        Map<String, String> mapLogDelete = new HashMap<>();
        StringBuilder sbLog = new StringBuilder("");
        if(Constants.TIME_TYPE_YEAR.equals(Integer.parseInt(configReportDTO.getTimeType()))){
            sbLog.append(" SELECT yearkey FROM ").append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
        } else if(Constants.TIME_TYPE_QUARTER.equals(Integer.parseInt(configReportDTO.getTimeType()))){
                sbLog.append(" SELECT quarkey FROM ").append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
        } else if(Constants.TIME_TYPE_MONTH.equals(Integer.parseInt(configReportDTO.getTimeType()))){
            sbLog.append(" SELECT monkey FROM ").append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
        }
        if (Constants.formInputYear.equals(configReportDTO.getformInput())) {
            sbLog.append(" WHERE parent_id = :value");
        } else {
            sbLog.append(" WHERE ").append(primaryKey.getColumnName()).append("= :value");
        }
        Query queryLog = manager.createNativeQuery(sbLog.toString());
        if (Constants.formInputYear.equals(configReportDTO.getformInput())) {
            queryLog.setParameter("value", mapRowData.get("parent_id"));
        } else {
            queryLog.setParameter("value", mapRowData.get(primaryKey.getColumnName()));
        }
        List<Integer> list = queryLog.getResultList();
        while (list.remove(null)) {
        }
        int time = (int) list.get(0);
        mapLogDelete.put("time",String.valueOf(time));

        StringBuilder sb = new StringBuilder("");
        sb.append(" DELETE from ").append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
        if (Constants.formInputYear.equals(configReportDTO.getformInput())) {
            sb.append(" WHERE parent_id = :value");
        } else {
            sb.append(" WHERE ").append(primaryKey.getColumnName()).append("= :value");
        }
        Query query = manager.createNativeQuery(sb.toString());
        if (Constants.formInputYear.equals(configReportDTO.getformInput())) {
            query.setParameter("value", mapRowData.get("parent_id"));
        } else {
            query.setParameter("value", mapRowData.get(primaryKey.getColumnName()));
        }
        query.executeUpdate();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        mapLogDelete.put("timestamp",timestamp.toString());
        return mapLogDelete;
    }

    @Override
    public void updateRowDataConfigReports(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData,
                                           ConfigReportColumnDTO primaryKey, String keyColum, Object keyValue) throws Exception {
        try {
            StringBuilder sb = new StringBuilder("");
            StringBuilder sbFieldUpdate = new StringBuilder("");
            Map<String, Object> mapParam = new HashMap<>();

            for (Map.Entry<String, Object> entry : mapRowData.entrySet()) {
                sbFieldUpdate.append(entry.getKey()).append("=").append(":").append(entry.getKey()).append(",");
                mapParam.put(entry.getKey(), entry.getValue());
            }
            sbFieldUpdate = sbFieldUpdate.deleteCharAt(sbFieldUpdate.length() - 1);

            sb.append(" UPDATE ").append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
            sb.append(" SET ");
            sb.append(sbFieldUpdate);
            sb.append(" WHERE ").append(keyColum).append(" = ").append(":").append(keyColum);
            mapParam.put(keyColum, keyValue);

            Query query = manager.createNativeQuery(sb.toString());

            mapParam.forEach((key, value) -> {
                if (value instanceof java.util.Date) {
                    query.setParameter(key, (java.util.Date) value, TemporalType.TIMESTAMP);
                } else {
                    if (DataUtil.isNullOrEmpty(value)) {
                        query.setParameter(key, null);
                    } else {
                        query.setParameter(key, value);
                    }
                }
            });

            query.executeUpdate();
        } catch (Exception ex) {
            if (ex instanceof PersistenceException && ex.getMessage().contains("ConstraintViolationException")) {
                throw new BadRequestAlertException(Translator.toLocale("error.configReport.dataIsExisted"), "configReport", "configReport.dataIsExisted");
            }
        }
    }

    @Override
    @Transactional
    public void insertRowDataConfigReports(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, String primaryKeyColumn) {


        StringBuilder sbField = new StringBuilder("");
        StringBuilder sbFieldParam = new StringBuilder("");
        StringBuilder sbFieldUpdate = new StringBuilder("");

        Map<String, Object> mapParam = new HashMap<>();
        for (Map.Entry<String, Object> entry : mapRowData.entrySet()) {
            sbField.append(entry.getKey()).append(",");
            sbFieldParam.append(":").append(entry.getKey()).append(",");
            sbFieldUpdate.append(entry.getKey()).append("=").append(":").append(entry.getKey()).append(",");
            mapParam.put(entry.getKey(), entry.getValue());
        }
        sbField = sbField.deleteCharAt(sbField.length() - 1);
        sbFieldParam = sbFieldParam.deleteCharAt(sbFieldParam.length() - 1);
        sbFieldUpdate = sbFieldUpdate.deleteCharAt(sbFieldUpdate.length() - 1);

        StringBuilder sb = new StringBuilder("");
        sb.append(" insert into ").append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
        sb.append("(");
        sb.append(sbField);
        sb.append(")");
        sb.append(" VALUES( ");
        sb.append(sbFieldParam);
        sb.append(") ");
        sb.append(" ON DUPLICATE KEY update  ");
        sb.append(sbFieldUpdate);

        Query query = manager.createNativeQuery(sb.toString());

        mapParam.forEach((key, value) -> {
            query.setParameter(key, value);
        });

        query.executeUpdate();
        manager.flush();
    }

    @Override
    public int getIdPrimaryKey(ConfigReportDTO configReportDTO, Map<String, Object> mapRowData, String primaryKeyColumn) {

        StringBuilder sbId = new StringBuilder("");

        Map<String, Object> mapParam = new HashMap<>();
        for (Map.Entry<String, Object> entry : mapRowData.entrySet()) {
            sbId.append(" AND ").append(entry.getKey()).append(" LIKE ").append(":").append(entry.getKey());
            mapParam.put(entry.getKey(), entry.getValue());
        }

        Integer id = 0;
        StringBuilder sqlId = new StringBuilder("SELECT " + primaryKeyColumn + " FROM ");
        sqlId.append(configReportDTO.getDatabaseName()).append(".").append(configReportDTO.getTableName());
        sqlId.append(" WHERE 1=1 ");
        sqlId.append(sbId);

        Query queryId = manager.createNativeQuery(sqlId.toString());
        mapParam.forEach((key, value) -> {
            queryId.setParameter(key, value);
        });

        List lstResult = queryId.getResultList();
        if (lstResult != null && !lstResult.isEmpty()) {
            id = Integer.valueOf(lstResult.get(0).toString());
        }

        return id;
    }

    public List<Object[]> getFullDescriptionOfTable(String tableName) {
        Query query = manager.createNativeQuery(" SHOW full COLUMNS from " + tableName);
        return query.getResultList();
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        Map<String, Object> mapParam = new HashMap<>();
        Query query = manager.createNativeQuery(sql);
        org.hibernate.query.Query queryHibernate = query.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        DbUtils.setParramToQuery(queryHibernate, mapParam);
        List<Map<String, Object>> lstData = queryHibernate.list();
        return lstData;
    }

    @Override
    public Map<String, Object> buildRefData(List<ConfigReportColumnDTO> lstColumn) throws Exception {
        Map<String, Object> mapRef = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        for (ConfigReportColumnDTO e : lstColumn) {
            RefDataDTO ref = mapper.readValue(e.getRefData(), RefDataDTO.class);
            List<ComboDTO> mapData = this.executeQuery(ref.getSql()).stream()
                .map(data -> {
                    ComboDTO comboDTO = new ComboDTO();
                    comboDTO.setLabel(data.get("label").toString());
                    comboDTO.setValue(data.get("value"));
                    return comboDTO;
                }).collect(Collectors.toList());
            mapRef.put(e.getColumnName(), mapData);
        }
        return mapRef;
    }
}
