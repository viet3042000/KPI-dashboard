package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.repository.BuildChartRepository;
import com.b4t.app.repository.ExecuteSqlRepository;
import com.b4t.app.repository.SqlParserRepository;
import com.b4t.app.service.CatGraphKpiService;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.ConfigQueryChartService;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BuildChartRepositoryImpl implements BuildChartRepository {
    private final static String ENTITY_NAME = "configchart";
    private final Logger log = LoggerFactory.getLogger(BuildChartRepositoryImpl.class);

    private final EntityManager manager;
    private final ObjectMapper mapper = new ObjectMapper();

    private final CatItemService catItemService;
    private final ConfigQueryChartService configQueryChartService;
    private final CatGraphKpiService catGraphKpiService;
    private final SqlParserRepository sqlParserRepository;
    private final ExecuteSqlRepository executeSqlRepository;

    public BuildChartRepositoryImpl(
        EntityManager manager, CatItemService catItemService,
        ConfigQueryChartService configQueryChartService,
        CatGraphKpiService catGraphKpiService,
        SqlParserRepository sqlParserRepository,
        ExecuteSqlRepository executeSqlRepository) {
        this.manager = manager;
        this.catItemService = catItemService;
        this.configQueryChartService = configQueryChartService;
        this.catGraphKpiService = catGraphKpiService;
        this.sqlParserRepository = sqlParserRepository;
        this.executeSqlRepository = executeSqlRepository;
    }

    public List<Object> getDescriptionOfTable(String tableName) {
        Query query = manager.createNativeQuery(" describe " + tableName);
        return query.getResultList();
    }

    public List<Map<String, Object>> getDescriptionOfTableToMap(String tableName) {
        return sqlParserRepository.getDescriptionOfTableToMap(tableName);
    }

    @Override
    public ChartResultDTO getChartResult(ConfigChartDTO chartDTO, List<ConfigChartItemDTO> chartItems, ChartParamDTO filterParamsObj) throws JsonProcessingException, ParseException {
        ChartResultDTO result = new ChartResultDTO(chartDTO);
        if (DataUtil.isNullOrEmpty(chartItems)) return result;
        Map<ConfigQueryChartDTO, List<ConfigChartItemDTO>> mergeQueries = chartItems.stream().filter(i -> i.getQuery() != null)
            .collect(Collectors.groupingBy(ConfigChartItemDTO::getQuery, Collectors.mapping((ConfigChartItemDTO i) -> i, Collectors.toList())));
        List<ChartDetailDTO> details = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(mergeQueries)) {
            for (Map.Entry<ConfigQueryChartDTO, List<ConfigChartItemDTO>> entry : mergeQueries.entrySet()) {
                Map<String, Object> params = filterParamsObj != null && !DataUtil.isNullOrEmpty(filterParamsObj.toMap()) ? filterParamsObj.toMap() : null;
                ConfigQueryChartDTO query = entry.getKey();
                SaveChartItemDTO item = new SaveChartItemDTO(mergeQueries.get(query).get(0));
                Integer timeTypeDefault = getTimeTypeDefault(chartDTO, params, item);
                if (timeTypeDefault != null) chartDTO.setTimeTypeDefault(timeTypeDefault);

                Map<String, Object> defaultParams = mapper.readValue(query.getDefaultValue(), Map.class);
                defaultParams = mergeParams(defaultParams, params);

                if (!defaultParams.containsKey(Constants.KPI_IDS_PARAM)) {
                    if (!DataUtil.isNullOrEmpty(item.getKpiInfos())) {
                        List<Long> kpiIds = item.getKpiInfos().stream()
                            .map(ki -> {
                                if (!DataUtil.isNullOrEmpty(ki.getKpis())) {
                                    return ki.getKpis().stream().map(CatGraphKpiDTO::getKpiId).collect(Collectors.toList());
                                }
                                return new ArrayList<Long>();
                            })
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
                        defaultParams.put(Constants.KPI_IDS_PARAM, kpiIds);
                    }
                }

                caclTimeFilter(defaultParams, chartDTO, query);
                query.setParams(defaultParams);
                if (!DataUtil.isNullOrEmpty(query.getParams()) && DataUtil.isNullOrEmpty(result.getFilterParams()) ) {
                    result.setFilterParams(query.getParams());
                }

                try {
                    List<Object> data = new ArrayList<>();
                    String sqlQuery = query.getQueryData();
                    String condition =" and " + sqlQuery.substring(sqlQuery.indexOf("select")+"select".length(),sqlQuery.indexOf(".", sqlQuery.indexOf(".") +1)) + ".obj_name = :X_AXIS";
                    sqlQuery = sqlQuery.substring(0, sqlQuery.indexOf("(:KPIIDS)") + "(:KPIIDS)".length()) + condition + sqlQuery.substring(sqlQuery.indexOf("(:KPIIDS)")+ "(:KPIIDS)".length());
                    if(defaultParams.get("X_AXIS") != null) {
                        data = executeSqlRepository.executeSql(sqlQuery, defaultParams);
                    }else{
                        data = executeSqlRepository.executeSql(query.getQueryData(), defaultParams);
                    }
                    data = processChartDataTitle(data, result);
                    query.setData(data);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new BadRequestAlertException(Translator.toLocale("error.configchart.sqlerror"), ENTITY_NAME, "error.configchart.sqlerror");
                }
            }
        }
        List<CatGraphKpiDTO> kpiInfos = new ArrayList<>();
        for (Map.Entry<ConfigQueryChartDTO, List<ConfigChartItemDTO>> entry : mergeQueries.entrySet()) {
            for (ConfigChartItemDTO chartItem : entry.getValue()) {
                SaveChartItemDTO saveItem = new SaveChartItemDTO(chartItem);
                List<CatGraphKpiDTO> temKpiInfos = getKpiInfoForItemOfResultChart(result.getFilterParams(), saveItem, DataUtil.isNullOrEmpty(entry.getKey().getData()) ? null : entry.getKey().getData().get(0));
                for (CatGraphKpiDTO kpi: temKpiInfos) {
                    if (kpiInfos.stream().noneMatch(k -> kpi.getKpiId().equals(k.getKpiId()))) {
                        kpiInfos.add(kpi);
                    }
                }
                ChartDetailDTO detail = new ChartDetailDTO(chartItem.getTypeChart(), chartItem.getOrderIndex(), entry.getKey().getData(),
                    chartItem.getDisplayConfigs(), chartItem.getQuery(), !DataUtil.isNullOrEmpty(temKpiInfos) ? temKpiInfos.get(0) : null, kpiInfos);
                details.add(detail);
            }
        }
        List<CatGraphKpiDTO> finalKpiInfos = kpiInfos;
        details.forEach(d -> d.setKpiInfos(finalKpiInfos));
        details.sort((a, b) -> {
            if (a.getOrderIndex() == null && b.getOrderIndex() != null) return 1;
            if (a.getOrderIndex() != null && b.getOrderIndex() == null) return -1;
            if (a.getOrderIndex() == null && b.getOrderIndex() == null) return 0;
            return a.getOrderIndex().compareTo(b.getOrderIndex());
        });
        result.setDetails(details);
        result.setTitleChart(processChartTitle(result));
        return result;
    }

    @Override
    public SaveChartItemDTO generateInputCondition(SaveChartItemDTO item, ConfigQueryChartDTO query, List<SaveDisplayQueryDTO> displayQueries) throws JsonProcessingException {
        SaveChartItemDTO rs = new SaveChartItemDTO(item);
        if (StringUtils.isEmpty(query.getQueryData())) return rs;
        String sql = query.getQueryData();
        ObjectMapper om = new ObjectMapper();
        List<SaveKpiInfoDTO> kpiInfos = new ArrayList<>();
        List<SaveOrderByDTO> orderBys = new ArrayList<>();
        List<SaveInputParamDTO> params = new ArrayList<>();
        Map<String, Object> defaultValues = StringUtils.isNotEmpty(query.getDefaultValue()) ? om.readValue(query.getDefaultValue(), Map.class) : new HashMap<>();
        defaultValues = mergeParams(defaultValues, null);
        Map<String, List<Map<String, Object>>> tables = new TreeMap<>();
        List<Map<String, Object>> allColumns;
        List<SaveDisplayQueryDTO> outColumns = new ArrayList<>();
        try {
            if (defaultValues != null && !defaultValues.isEmpty() && sql.contains(":" + Constants.TABLE_NAME_PARAM)) {
                if (defaultValues.containsKey(Constants.TABLE_NAME_PARAM)) {
                    sql = sql.replace(":" + Constants.TABLE_NAME_PARAM, (String) defaultValues.get(Constants.TABLE_NAME_PARAM));
                } else if (!DataUtil.isNullOrEmpty(item.getKpiInfos())) {
                    String tableName = item.getKpiInfos().get(0).getTableName();
                    sql = sql.replace(":" + Constants.TABLE_NAME_PARAM, tableName);
                    defaultValues.put(Constants.TABLE_NAME_PARAM, tableName);
                } else {
                    sql = sql.replace(":" + Constants.TABLE_NAME_PARAM, Constants.DEFAULT_TABLE_NAME);
                    defaultValues.put(Constants.TABLE_NAME_PARAM, Constants.DEFAULT_TABLE_NAME);
                }
            }
            Statement stmt = CCJSqlParserUtil.parse(sql);
            if (stmt instanceof Select) {
                Select select = (Select) stmt;
                Map<String, String> aliases = sqlParserRepository.getTableAlias(select);
                List<String> tableNames = sqlParserRepository.getTableNames(select);
                allColumns = sqlParserRepository.getAllColumns(aliases, tableNames, tables);
                SelectBody selectBody = select.getSelectBody();
                if (selectBody instanceof PlainSelect) {
                    PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                    // analyze limit
                    rs.setLimit(sqlParserRepository.analyzeLimit(plainSelect.getLimit()));
                    // analyze column
                    displayQueries = sqlParserRepository.analyzeColumns(displayQueries, plainSelect, aliases, tables);
                    outColumns = sqlParserRepository.analyzeColumns(null, plainSelect, aliases, tables);
                    // analyze order by
                    orderBys = sqlParserRepository.analyzeOrderBy(plainSelect.getOrderByElements(), tables);
                    // analyze where
                    Expression whCls = plainSelect.getWhere();
                    List<CatGraphKpiDTO> kpis = new ArrayList<>();
                    if (whCls != null) {
                        params = processWhCls(whCls, defaultValues, tables);
                        params = params.stream().filter(Objects::nonNull).collect(Collectors.toList());
                        kpis = sqlParserRepository.parseKpiFromParam(params, defaultValues);
                    }
                    params = params.stream().filter(p -> DataUtil.isNullOrEmpty(p.getFieldName()) || !p.getFieldName().contains(Constants.KPI_ID_FIELD.toLowerCase())).collect(Collectors.toList());
                    tableNames = tableNames.stream().filter(t -> !t.contains("cat_graph_kpi")).collect(Collectors.toList());
                    for (String tableName : tableNames) {
                        SaveKpiInfoDTO kpiInfo = new SaveKpiInfoDTO(tableName, kpis);
                        kpiInfos.add(kpiInfo);
                    }
                }

//                else {
//                    throw new BadRequestAlertException(Translator.toLocale("error.configchart.analyzeNeedUpdate"), ENTITY_NAME, "error.configchart.analyzeNeedUpdate");
//                }
            } else {
                throw new BadRequestAlertException(Translator.toLocale("error.configchart.analyzeNeedUpdate"), ENTITY_NAME, "error.configchart.analyzeNeedUpdate");
            }
        } catch (JSQLParserException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestAlertException(Translator.toLocale("error.configchart.sqlerror"), ENTITY_NAME, "error.configchart.sqlerror");
        }
        query.setDefaultValue(mapper.writeValueAsString(defaultValues));
        rs.setColumns(displayQueries);
        rs.setKpiInfos(DataUtil.isNullOrEmpty(kpiInfos) || kpiInfos.stream().allMatch(k -> DataUtil.isNullOrEmpty(k.getKpis())) ? item.getKpiInfos() : kpiInfos);
        rs.setOrderBys(DataUtil.isNullOrEmpty(orderBys) ? item.getOrderBys() : orderBys);
        rs.setParams(DataUtil.isNullOrEmpty(params) ? item.getParams() : params);
        rs.setCustomizeSql(query.getQueryData());
        rs.setCustomizeMaxPrdIdSql(query.getQueryMaxPrdId());
        rs.setAllColumns(allColumns);
        rs.setOutColumns(outColumns);
        rs.setQuery(query);
        return rs;
    }

    @Override
    public ConfigQueryChartDTO buildQuery(List<SaveChartItemDTO> items, boolean hasSave, boolean overview) throws JsonProcessingException {
        if (DataUtil.isNullOrEmpty(items)) return null;
        Map<String, SaveKpiInfoDTO> tableKpiMapping = new HashMap<>();
        for (SaveChartItemDTO item : items) {
            for (SaveKpiInfoDTO kpi : item.getKpiInfos()) {
                if (tableKpiMapping.containsKey(kpi.getTableName())) {
                    SaveKpiInfoDTO mergedKpi = tableKpiMapping.get(kpi.getTableName());
                    List<CatGraphKpiDTO> kpis = mergedKpi.getKpis();
                    List<Long> existedKpiIds = kpis.stream().map(CatGraphKpiDTO::getKpiId).collect(Collectors.toList());
                    kpis.addAll(kpi.getKpis().stream().filter(k -> !existedKpiIds.contains(k.getKpiId())).collect(Collectors.toList()));
                    mergedKpi.setKpis(kpis);
                    tableKpiMapping.put(kpi.getTableName(), mergedKpi);
                } else {
                    tableKpiMapping.put(kpi.getTableName(), kpi);
                }
            }
        }
        SaveChartItemDTO firstItem = items.get(0);
        String whCls = StringUtils.EMPTY;
        String maxPrdIdWhCls = StringUtils.EMPTY;
        Map<String, Object> defaultValues = new HashMap<>();
        List<String> sqlQueries = new ArrayList<>();
        List<String> maxPrdSqlQueries = new ArrayList<>();
        boolean joinCatGraphKpi = items.stream().anyMatch(i -> i.getJoinCatGraphKpi() != null && i.getJoinCatGraphKpi());

        if (StringUtils.isNotEmpty(firstItem.getCustomizeSql())) {
            Pattern r = Pattern.compile("^:[A-Z]+$");

            Map<String, Object> finalDefaultValues = defaultValues;
            firstItem.getParams().forEach(p -> {
                if (r.matcher(p.getValue()).find()) {
                    p.setFilterParam(true);
                }
                if (p.getFilterParam() != null && p.getFilterParam())
                    finalDefaultValues.put(p.getParamName(), p.getValueDefault());
            });
            defaultValues = finalDefaultValues;
        } else {
            for (String tableName : tableKpiMapping.keySet()) {
                StringBuilder sql = new StringBuilder();
                StringBuilder maxPrdSql = new StringBuilder();
                String orderByStr = createOrderBy(firstItem.getOrderBys(), tableName, overview);
                SaveKpiInfoDTO kpiInfo = tableKpiMapping.get(tableName);
                List<SaveDisplayQueryDTO> columns = createDisplayQueries(items, tableName, overview);
                Map<String, Object> retWhCls = createWhCls(items, tableName, overview);
                if (!DataUtil.isNullOrEmpty(retWhCls)) {
                    whCls = (String) retWhCls.get("whCls");
                    maxPrdIdWhCls = (String) retWhCls.get("maxPrdIdWhCls");
                    defaultValues = (Map<String, Object>) retWhCls.get("defaultValues");
                }

                if (DataUtil.isNullOrEmpty(kpiInfo.getKpis())) continue;
                List<String> fields = columns.stream().map(SaveDisplayQueryDTO::getFieldSql).collect(Collectors.toList());
                sql.append(" select ").append(StringUtils.join(fields, ",")).append(" from ").append(overview ? ":" + Constants.TABLE_NAME_PARAM : tableName);
                maxPrdSql.append(" select prd_id from ").append(overview ? ":" + Constants.TABLE_NAME_PARAM : tableName);
                if (overview) {
                    defaultValues.put(Constants.TABLE_NAME_PARAM, tableName);
                    sql.append(" as ").append(Constants.DATA_TABLE_ALIAS);
                    maxPrdSql.append(" as ").append(Constants.DATA_TABLE_ALIAS);
                }
                tableName = overview ? Constants.DATA_TABLE_ALIAS : tableName;
                if (joinCatGraphKpi) {
                    sql.append(" left outer join cat_graph_kpi ").append(overview ? Constants.KPI_TABLE_ALIAS : "").append(" on ").append(tableName).append(".kpi_id = ").append(overview ? Constants.KPI_TABLE_ALIAS : "cat_graph_kpi").append(".kpi_id ");
                }
                sql.append(whCls).append(" and " + tableName + ".kpi_id in (:").append(Constants.KPI_IDS_PARAM).append(") ");
                maxPrdSql.append(maxPrdIdWhCls).append(" and " + tableName + ".kpi_id in (:").append(Constants.KPI_IDS_PARAM).append(") ");
                if (StringUtils.isNotEmpty(orderByStr)) {
                    sql.append(orderByStr);
                }
                if (StringUtils.isNotEmpty(firstItem.getLimit()))
                    sql.append(" limit ").append(firstItem.getLimit().replaceAll("(?i)limit", "").trim());
                sqlQueries.add(sql.toString());
                maxPrdSqlQueries.add(maxPrdSql.toString());
            }
        }
        ConfigQueryChartDTO rs = new ConfigQueryChartDTO(StringUtils.isEmpty(firstItem.getCustomizeSql()) ? StringUtils.join(sqlQueries, " union ") : firstItem.getCustomizeSql()
            , StringUtils.isEmpty(firstItem.getCustomizeMaxPrdIdSql()) ? "select max(prd_id) from ( " + StringUtils.join(maxPrdSqlQueries, " union ") + ") max_prd" : firstItem.getCustomizeMaxPrdIdSql()
            , mapper.writeValueAsString(defaultValues)
            , Constants.STATUS_ACTIVE);
        if (hasSave) rs = configQueryChartService.save(rs);
        return rs;
    }

    private String createOrderBy(List<SaveOrderByDTO> orderBys, String tableName, boolean overview) {
        StringBuilder orderByStr = new StringBuilder();
        if (!DataUtil.isNullOrEmpty(orderBys)) {
            orderByStr.append(" order by ");
            int i = 0;
            for (SaveOrderByDTO order : orderBys) {
                if (StringUtils.isEmpty(order.getSortDir())) {
                    order.setSortDir("asc");
                }
                if (StringUtils.isNotEmpty(order.getValue())) {
                    if (overview) {
                        orderByStr.append(order.getValue().replace(tableName + ".", Constants.DATA_TABLE_ALIAS + ".")
                            .replace("cat_graph_kpi.", Constants.KPI_TABLE_ALIAS + "."));
                    } else {
                        orderByStr.append(
                            order.getValue().replace(Constants.DATA_TABLE_ALIAS + ".", order.getValue().contains(tableName) ? "" : tableName + ".")
                                .replace(Constants.KPI_TABLE_ALIAS + ".", (order.getValue().contains("cat_graph_kpi") ? "" : "cat_graph_kpi.")));
                    }
                    orderByStr.append(" ").append(order.getSortDir());
                }
                if (orderBys.size() > 1 && i < orderBys.size() - 1) {
                    orderByStr.append(", ");
                }
                i++;
            }
        }
        return orderByStr.toString();
    }

    private List<SaveDisplayQueryDTO> createDisplayQueries(List<SaveChartItemDTO> items, String tableName, boolean overview) {
        List<SaveDisplayQueryDTO> columns = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            SaveChartItemDTO item = items.get(i);
            List<SaveDisplayQueryDTO> colItems = item.getColumns();
            if (StringUtils.isNotEmpty(item.getCustomizeSql())) {
                columns.addAll(colItems);
                continue;
            }
            for (int j = 0; j < colItems.size(); j++) {
                SaveDisplayQueryDTO column = colItems.get(j);
                column.setColumnQuery("item" + i + "_col" + j);
                if (DataUtil.isNullOrEmpty(column.getValues())) continue;
                StringBuilder field = new StringBuilder();
                StringBuilder value = new StringBuilder();

                for (int k = 0; k < column.getValues().size(); k++) {
                    if (k != 0) value.append(", ");
                    SaveDisplayQueryValueDTO val = column.getValues().get(k);

                    if (Constants.FIELD_TYPE.equals(val.getType())) {
                        if (overview) {
                            value.append(val.getValue().replace(tableName + ".", Constants.DATA_TABLE_ALIAS + ".")
                                .replace("cat_graph_kpi.", Constants.KPI_TABLE_ALIAS + "."));
                        } else {
                            value.append(
                                val.getValue().replace(Constants.DATA_TABLE_ALIAS + ".", val.getValue().contains(tableName) ? "" : tableName + ".")
                                    .replace(Constants.KPI_TABLE_ALIAS + ".", (val.getValue().contains("cat_graph_kpi") ? "" : "cat_graph_kpi.")));
                        }
                    } else {
                        value.append(val.getValue());
                    }
                }
                if (column.getValues().size() > 1) {
                    value.insert(0, " CONCAT(");
                    value.append(") ");
                }
                field.append(value);
                field.append(" as ").append(column.getColumnQuery());
                column.setFieldSql(field.toString());
                columns.add(column);
            }
        }
        return columns;
    }

    private Map<String, Object> createWhCls(List<SaveChartItemDTO> items, String tableName, boolean overview) {
        if (DataUtil.isNullOrEmpty(items)) return new HashMap<>();
        SaveChartItemDTO firstItem = items.get(0);
        boolean hasMaxPrdIdQuery = firstItem.getParams().stream().anyMatch(p -> StringUtils.isNotEmpty(p.getValueDefault()) && p.getValueDefault().contains(Constants.PARAM_CHART_DEFAULT.MAX_DATE));

        StringBuilder whCls = new StringBuilder(" where 1 = 1 ");
        StringBuilder maxPrdIdWhCls = new StringBuilder(" where 1 = 1 ");
        Map<String, Object> defaultValues = new HashMap<>();

        Pattern r = Pattern.compile("^:[A-Z]+$");
        List<CatItemDTO> paramCatItems = catItemService.findAll(null, new String[]{Constants.PARAM_CHART_CATITEM}, null, null, null, null);
        for (SaveInputParamDTO param : firstItem.getParams()) {
            if (StringUtils.isEmpty(param.getValue())) continue;

            if (r.matcher(param.getValue()).find()) {
                param.setFilterParam(true);
            }
            if (paramCatItems.stream().anyMatch(c -> param.getValue().equals(c.getItemValue()))) {
                param.setFilterParam(true);
            }
            String value = param.getValue();
            if (StringUtils.isNotEmpty(param.getOperator()) && (
                Constants.IN_OPERATOR.equals(param.getOperator().toUpperCase()) ||
                    Constants.NOT_IN_OPERATOR.equals(param.getOperator().toUpperCase()))) {
                value = String.format("(%s)", param.getValue());
            }
            if (param.getFilterParam() != null && param.getFilterParam()) {
                if (StringUtils.isEmpty(param.getValueDefault())) {
                    throw new BadRequestAlertException(Translator.toLocale("error.configchart.defaultvaluenull", new Object[]{param.getValue()}), ENTITY_NAME, "error.chart.defaultvaluenull");
                }
                value = param.getValue().toUpperCase();
                if (StringUtils.isNotEmpty(param.getOperator()) && (
                    Constants.IN_OPERATOR.equals(param.getOperator().toUpperCase()) ||
                        Constants.NOT_IN_OPERATOR.equals(param.getOperator().toUpperCase()))
                    && StringUtils.isNotEmpty(param.getValueDefault())) {
                    defaultValues.put(param.getValue().startsWith(":") ? value.substring(1) : value, param.getValueDefault().split(","));
                } else {
                    defaultValues.put(param.getValue().startsWith(":") ? value.substring(1) : value, param.getValueDefault());
                }
            }
            String colName = param.getFieldName().replace(Constants.DATA_TABLE_ALIAS + ".", "").replace(tableName + ".", "");
            colName = (overview ? Constants.DATA_TABLE_ALIAS + "." : tableName + ".") + colName;
            whCls.append(" and ")
                .append(colName)
                .append(" ")
                .append(param.getOperator())
                .append(" ")
                .append(value);

            if (hasMaxPrdIdQuery && StringUtils.isNotEmpty(param.getFieldName())
                && !(Constants.DATA_PRD_ID.equals(param.getFieldName()) ||
                (param.getFieldName().contains(".") &&
                    Constants.DATA_PRD_ID.equals(param.getFieldName().substring(param.getFieldName().lastIndexOf(".") + 1).toLowerCase())))) {
                maxPrdIdWhCls.append(" and ")
                    .append(colName)
                    .append(" ")
                    .append(param.getOperator())
                    .append(" ")
                    .append(value);
            }
        }
        List<Long> kpiIds = items.stream()
            .flatMap(i -> i.getKpiInfos().stream())
            .flatMap(k -> k.getKpis().stream())
            .map(CatGraphKpiDTO::getKpiId).distinct()
            .collect(Collectors.toList());
        defaultValues.put(Constants.KPI_IDS_PARAM, kpiIds);

        Map<String, Object> rs = new HashMap<>();
        rs.put("whCls", whCls.toString());
        rs.put("maxPrdIdWhCls", maxPrdIdWhCls.toString());
        rs.put("defaultValues", defaultValues);
        return rs;
    }

    private Map<String, Object> mergeParams(Map<String, Object> defaultParams, Map<String, Object> params) {
        if (DataUtil.isNullOrEmpty(defaultParams) && DataUtil.isNullOrEmpty(params)) return new HashMap<>();
        if (DataUtil.isNullOrEmpty(defaultParams)) {
            return params;
        }
        if (DataUtil.isNullOrEmpty(params)) {
            params = new HashMap<>();
        }
        Map<String, Object> temp = new HashMap<>();
        for (String key : defaultParams.keySet()) {
            temp.put(key.toUpperCase(), defaultParams.get(key));
        }
        //defaultParams;
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String key : params.keySet()) {
                temp.put(key, params.get(key));
            }
        }
        return temp;
    }

    private void caclTimeFilter(Map<String, Object> params, ConfigChartDTO chart, ConfigQueryChartDTO queryChart) throws ParseException, JsonProcessingException {
        Map<String, Object> defaultParamsTemp = mapper.readValue(queryChart.getDefaultValue(), Map.class);
        Map<String, Object> defaultParams = new HashMap<>();
        for (String key : defaultParamsTemp.keySet()) {
            defaultParams.put(key.toUpperCase(), defaultParamsTemp.get(key));
        }
        Integer timeType = chart.getTimeTypeDefault();
        if (params.containsKey(Constants.TIME_TYPE_PARAM)) {
            timeType = DataUtil.safeToInt(params.get(Constants.TIME_TYPE_PARAM));
        }
        Map<String, String> paramDefault = getParamDefault(params);
        Integer currentDate = DataUtil.getDateInt(DbUtils.getSysDate(manager), Constants.DATE_FORMAT_YYYYMMDD);
        if (currentDate == null) currentDate = DataUtil.getDateInt(new Date(), Constants.DATE_FORMAT_YYYYMMDD);
        Integer maxPrdId = currentDate;
        Integer maxPrdIdTimeTypeMonth = currentDate;
        Integer toDate = DataUtil.safeToInt(params.get(Constants.TO_DATE_PARAM));
        if (DataUtil.isDate(toDate.toString(), Constants.DATE_FORMAT_YYYYMMDD)) {
            maxPrdId = toDate;
            currentDate = toDate;
        } else if (StringUtils.isNotEmpty(queryChart.getQueryMaxPrdId())) {
            try {
                maxPrdId = executeSqlRepository.getMaxPrdId(queryChart.getQueryMaxPrdId(), params);
                maxPrdId = maxPrdId == null ? currentDate : maxPrdId;

                Map<String, Object>paramsTemp = SerializationUtils.clone(new HashMap<>(params)) ;
                paramsTemp.put(Constants.TIME_TYPE_PARAM, String.valueOf(Constants.TIME_TYPE_MONTH));
                maxPrdIdTimeTypeMonth = executeSqlRepository.getMaxPrdId(queryChart.getQueryMaxPrdId(), paramsTemp);
                maxPrdIdTimeTypeMonth = maxPrdIdTimeTypeMonth == null ? currentDate : maxPrdIdTimeTypeMonth;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BadRequestAlertException(Translator.toLocale("error.configchart.querymaxprdiderror"), ENTITY_NAME, "error.configchart.querymaxprdiderror");
            }
        }

        // additional param MAX_DATE_TIME_TYPE_MONTH
        params.put(Constants.PARAM_CHART_DEFAULT.MAX_DATE_TIME_TYPE_MONTH, String.valueOf(maxPrdIdTimeTypeMonth));
        //end
        for (Map.Entry<String, String> entry : paramDefault.entrySet()) {
            if (entry.getValue().equals(Constants.PARAM_CHART_DEFAULT.MAX_DATE)) {
                params.put(entry.getKey(), String.valueOf(maxPrdId));
            }
            if (Arrays.asList(Constants.PARAM_CHART_DEFAULT.MAX_DATE_NDATE, Constants.PARAM_CHART_DEFAULT.MAX_DATE_NMONTH,
                Constants.PARAM_CHART_DEFAULT.MAX_DATE_NQUAR, Constants.PARAM_CHART_DEFAULT.MAX_DATE_NYEAR).contains(entry.getValue())) {
                int rangeTime = getRangeTimeFromMaxDate(entry.getValue());
                Integer date = DataUtil.getAbsoluteDate(maxPrdId, rangeTime, timeType);
                params.put(entry.getKey(), String.valueOf(date));
            }
            if (Constants.PARAM_CHART_DEFAULT.START_OF_YEAR.equals(entry.getValue())) {
                Integer date = DataUtil.getAbsoluteFirstDateOfYear(maxPrdId);
                params.put(entry.getKey(), String.valueOf(date));
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE.equals(entry.getValue())) {
                params.put(entry.getKey(), String.valueOf(currentDate));
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME.equals(entry.getValue())) {
                Integer date = DataUtil.getAbsoluteDate(currentDate, chart.getRelativeTime(), timeType);
                params.put(entry.getKey(), String.valueOf(date));
            }
            if (Arrays.asList(Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NDATE, Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NMONTH,
                Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NQUAR, Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NYEAR)
                .contains(entry.getValue())) {
                Integer date = DataUtil.getAbsoluteDate(currentDate, chart.getRelativeTime(), timeType);
                int rangeTime = getRangeTimeFromMaxDate(entry.getValue());
                date = DataUtil.getAbsoluteDate(date, rangeTime, timeType);
                params.put(entry.getKey(), String.valueOf(date));
            }
        }
        if (params.containsKey(Constants.FROM_DATE_PARAM)) {
            if (!DataUtil.isDate(String.valueOf(params.get(Constants.FROM_DATE_PARAM)), Constants.DATE_FORMAT_YYYYMMDD)) {
                if (0 == toDate) toDate = currentDate;

                if (Arrays.asList(Constants.PARAM_CHART_DEFAULT.MAX_DATE, Constants.PARAM_CHART_DEFAULT.CURRENT_DATE,
                    Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME).contains(defaultParams.get(Constants.TO_DATE_PARAM))) {
                    int rangeTime = DataUtil.safeToInt(params.get(Constants.FROM_DATE_PARAM));
                    if (0 == rangeTime) rangeTime = Constants.DEFAULT_RANGE_TIME;
                    params.put(Constants.FROM_DATE_PARAM, String.valueOf(DataUtil.getAbsoluteDate(toDate, rangeTime, timeType)));
                }
                Integer fromDate = DataUtil.safeToInt(params.get(Constants.FROM_DATE_PARAM));
                if (fromDate > toDate) {
                    params.put(Constants.FROM_DATE_PARAM, String.valueOf(toDate));
                    params.put(Constants.TO_DATE_PARAM, String.valueOf(fromDate));
                } else {
                    params.put(Constants.FROM_DATE_PARAM, String.valueOf(fromDate));
                    params.put(Constants.TO_DATE_PARAM, String.valueOf(toDate));
                }
            }
        }
    }

    private String processChartTitle(ChartResultDTO chart) {
        if (StringUtils.isEmpty(chart.getTitleChart())) return StringUtils.EMPTY;
        Map<String, Object> params = chart.getFilterParams();
        String title = chart.getTitleChart();

        if (!DataUtil.isNullOrEmpty(params)) {
            Integer timeType = chart.getTimeTypeDefault();
            if (params.containsKey(Constants.TIME_TYPE_PARAM)) {
                timeType = DataUtil.safeToInt(params.get(Constants.TIME_TYPE_PARAM).toString());
            }
            for (String key : params.keySet()) {
                if (title.contains(":" + key)) {
                    String value = String.valueOf(params.get(key));
                    if (Constants.TO_DATE_PARAM.equals(key) || Constants.FROM_DATE_PARAM.equals(key)) {
                        value = DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "dd/MM/yyyy");
                        if (Constants.TIME_TYPE_YEAR.equals(timeType)) {
                            value = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy"));
                        } else if (Constants.TIME_TYPE_QUARTER.equals(timeType)) {
                            value = String.format("%s", DataUtil.formatQuarterPattern(DataUtil.safeToInt(params.get(key))));
                        } else if (Constants.TIME_TYPE_MONTH.equals(timeType)) {
                            value = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM/yyyy"));
                        }
                    }
                    title = title.replaceAll(":" + key, value);
                }
                if(Constants.TO_DATE_PARAM.equals(key)){
                    Set<String> additionalParams = new HashSet<>(Arrays.asList(Constants.BACK_DATE_PARAM, Constants.COUNT_PARAM, Constants.TO_YEAR_PARAM, Constants.TO_QUARTER_PARAM, Constants.TO_MONTH_PARAM, Constants.BACK_YEAR_PARAM, Constants.BACK_MONTH_PARAM, Constants.BACK_QUARTER_PARAM));
                    // co params TODATE => thuc hien them cac params khac : BACKDATE, COUNT, TOYEAR, TOMONTH, TOQUARTER, BACKYEAR, BACKMONTH, BACKQUARTER
                    for(String additionalKey : additionalParams){
                        if(title.contains(":" + additionalKey)){
                            String value2 = "";
                            if(Constants.BACK_DATE_PARAM.equals(additionalKey)){
                                value2 = DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "dd/MM/yyyy", -1 , timeType);
                                if (Constants.TIME_TYPE_YEAR.equals(timeType)) {
                                    value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy", -1, Constants.TIME_TYPE_YEAR));
                                } else if (Constants.TIME_TYPE_QUARTER.equals(timeType)) {
                                    value2 = String.format("%s", DataUtil.formatQuarterPattern(DataUtil.safeToInt(params.get(key)), -1, Constants.TIME_TYPE_QUARTER));
                                } else if (Constants.TIME_TYPE_MONTH.equals(timeType)) {
                                    value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM/yyyy", -1, Constants.TIME_TYPE_MONTH));
                                }
                            }
                            if(Constants.COUNT_PARAM.equals(additionalKey)){
                                if (Constants.TIME_TYPE_QUARTER.equals(timeType)) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                        Date date = sdf.parse(params.get(key).toString());
                                        value2 = String.format("%s", date.getMonth() / 3 + 1);
                                    } catch (Exception ex) {
                                        log.error(ex.getMessage(), ex);
                                    }
                                } else if (Constants.TIME_TYPE_MONTH.equals(timeType)) {
                                    value2 = String.format("%d",Integer.valueOf(DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM")));
                                }
                            }
                            if(Constants.TO_YEAR_PARAM.equals(additionalKey)){
                                value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy"));
                            }
                            if(Constants.TO_QUARTER_PARAM.equals(additionalKey)){
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                    Date date = sdf.parse(params.get(key).toString());
                                    value2 = String.format("%s", date.getMonth() / 3 + 1 );
                                } catch (Exception ex) {
                                    log.error(ex.getMessage(), ex);
                                }
                            }
                            if(Constants.TO_MONTH_PARAM.equals(additionalKey)){
                                value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM"));
                            }

                            if(Constants.BACK_YEAR_PARAM.equals(additionalKey)){
                                value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy", -1, Constants.TIME_TYPE_YEAR));
                            }
                            if(Constants.BACK_QUARTER_PARAM.equals(additionalKey)){
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                    Date date = sdf.parse(params.get(key).toString());
                                    date = DataUtil.getAbsoluteDate(date, -1 , Constants.TIME_TYPE_QUARTER);
                                    value2 = String.format("%s", date.getMonth() / 3 + 1);
                                } catch (Exception ex) {
                                    log.error(ex.getMessage(), ex);
                                }
                            }
                            if(Constants.BACK_MONTH_PARAM.equals(additionalKey)){
                                value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM", -1, Constants.TIME_TYPE_MONTH));
                            }
                            title = title.replaceAll(":" + additionalKey, value2);
                        }
                    }
                }

                if(Constants.PARAM_CHART_DEFAULT.MAX_DATE_TIME_TYPE_MONTH.equals(key)){
                    // them param MAX_MONTH,.. la thang/quy/nam lon nhat theo du lieu
                    Set<String> additionalParams = new HashSet<>(Arrays.asList(Constants.MAX_MONTH_PARAM, Constants.MAX_QUARTER_PARAM, Constants.MAX_YEAR_PARAM ));
                    for(String additionalKey : additionalParams){
                        if(title.contains(":" + additionalKey)){
                            String value2 = "";
                            if(Constants.MAX_YEAR_PARAM.equals(additionalKey)){
                                value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy"));
                            }
                            if(Constants.MAX_QUARTER_PARAM.equals(additionalKey)){
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                    Date date = sdf.parse(params.get(key).toString());
                                    value2 = String.format("%s", date.getMonth() / 3 + 1 );
                                } catch (Exception ex) {
                                    log.error(ex.getMessage(), ex);
                                }
                            }
                            if(Constants.MAX_MONTH_PARAM.equals(additionalKey)){
                                value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM"));
                            }
                            title = (title.replaceAll(":" + additionalKey, value2));
                        }
                    }
                }
            }
        }
        return title;
    }

    private List<Object> processChartDataTitle(List<Object> datas, ChartResultDTO chart) {
    //  replace cac params trong title ban do = cac gia tri
        Map<String, Object> params = chart.getFilterParams();
        Integer timeType = chart.getTimeTypeDefault();

        if (!DataUtil.isNullOrEmpty(params)) {
            if (params.containsKey(Constants.TIME_TYPE_PARAM)) {
                timeType = DataUtil.safeToInt(params.get(Constants.TIME_TYPE_PARAM).toString());
            }
            Integer finalTimeType = timeType;
            datas = datas.stream().map(data -> {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> result = mapper.convertValue(data, new TypeReference<Map<String, Object>>() {});
                for(Map.Entry<String, Object> entry : result.entrySet()){
                    if(DataUtil.isNullOrEmpty(entry.getValue())){
                        continue;
                    }
                    for (String key : params.keySet()) {
                        if (entry.getValue().toString().contains(":" + key)) {
                            String value = String.valueOf(params.get(key));
                            if (Constants.TO_DATE_PARAM.equals(key) || Constants.FROM_DATE_PARAM.equals(key)) {
                                value = DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "dd/MM/yyyy");
                                if (Constants.TIME_TYPE_YEAR.equals(finalTimeType)) {
                                    value = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy"));
                                } else if (Constants.TIME_TYPE_QUARTER.equals(finalTimeType)) {
                                    value = String.format("%s", DataUtil.formatQuarterPattern(DataUtil.safeToInt(params.get(key))));
                                } else if (Constants.TIME_TYPE_MONTH.equals(finalTimeType)) {
                                    value = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM/yyyy"));
                                }
                            }
                            entry.setValue(entry.getValue().toString().replaceAll(":" + key, value));
                        }
                        if(Constants.TO_DATE_PARAM.equals(key)){
                            Set<String> additionalParams = new HashSet<>(Arrays.asList(Constants.BACK_DATE_PARAM, Constants.COUNT_PARAM, Constants.TO_YEAR_PARAM, Constants.TO_QUARTER_PARAM, Constants.TO_MONTH_PARAM, Constants.BACK_YEAR_PARAM, Constants.BACK_MONTH_PARAM, Constants.BACK_QUARTER_PARAM ));
                            // co params TODATE => thuc hien them cac params khac : BACKDATE, COUNT, TOYEAR, TOMONTH, TOQUARTER
                            for(String additionalKey : additionalParams){
                                if(entry.getValue().toString().contains(":" + additionalKey)){
                                    String value2 = "";
                                    if(Constants.BACK_DATE_PARAM.equals(additionalKey)){
                                        value2 = DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "dd/MM/yyyy", -1 , finalTimeType);
                                        if (Constants.TIME_TYPE_YEAR.equals(finalTimeType)) {
                                            value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy", -1, Constants.TIME_TYPE_YEAR));
                                        } else if (Constants.TIME_TYPE_QUARTER.equals(finalTimeType)) {
                                            value2 = String.format("%s", DataUtil.formatQuarterPattern(DataUtil.safeToInt(params.get(key)), -1, Constants.TIME_TYPE_QUARTER));
                                        } else if (Constants.TIME_TYPE_MONTH.equals(finalTimeType)) {
                                            value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM/yyyy", -1, Constants.TIME_TYPE_MONTH));
                                        }
                                    }
                                    if(Constants.COUNT_PARAM.equals(additionalKey)){
                                        if (Constants.TIME_TYPE_QUARTER.equals(finalTimeType)) {
                                            try {
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                                Date date = sdf.parse(params.get(key).toString());
                                                value2 = String.format("%s", date.getMonth() / 3 + 1);
                                            } catch (Exception ex) {
                                                log.error(ex.getMessage(), ex);
                                            }
                                        } else if (Constants.TIME_TYPE_MONTH.equals(finalTimeType)) {
                                            value2 = String.format("%d",Integer.valueOf(DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM")));
                                        }
                                    }
                                    if(Constants.TO_YEAR_PARAM.equals(additionalKey)){
                                        value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy"));
                                    }
                                    if(Constants.TO_QUARTER_PARAM.equals(additionalKey)){
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                            Date date = sdf.parse(params.get(key).toString());
                                            value2 = String.format("%s", date.getMonth() / 3 + 1 );
                                        } catch (Exception ex) {
                                            log.error(ex.getMessage(), ex);
                                        }
                                    }
                                    if(Constants.TO_MONTH_PARAM.equals(additionalKey)){
                                        value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM"));
                                    }

                                    if(Constants.BACK_YEAR_PARAM.equals(additionalKey)){
                                        value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy", -1, Constants.TIME_TYPE_YEAR));
                                    }
                                    if(Constants.BACK_QUARTER_PARAM.equals(additionalKey)){
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                            Date date = sdf.parse(params.get(key).toString());
                                            date = DataUtil.getAbsoluteDate(date, -1 , Constants.TIME_TYPE_QUARTER);
                                            value2 = String.format("%s", date.getMonth() / 3 + 1);
                                        } catch (Exception ex) {
                                            log.error(ex.getMessage(), ex);
                                        }
                                    }
                                    if(Constants.BACK_MONTH_PARAM.equals(additionalKey)){
                                        value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM", -1, Constants.TIME_TYPE_MONTH));
                                    }
                                    entry.setValue(entry.getValue().toString().replaceAll(":" + additionalKey, value2));
                                }
                            }
                        }

                        if(Constants.PARAM_CHART_DEFAULT.MAX_DATE_TIME_TYPE_MONTH.equals(key)){
                            // them param MAX_MONTH,.. la thang/quy/nam lon nhat theo du lieu
                            Set<String> additionalParams = new HashSet<>(Arrays.asList(Constants.MAX_MONTH_PARAM, Constants.MAX_QUARTER_PARAM, Constants.MAX_YEAR_PARAM ));
                            for(String additionalKey : additionalParams){
                                if(entry.getValue().toString().contains(":" + additionalKey)){
                                    String value2 = "";
                                    if(Constants.MAX_YEAR_PARAM.equals(additionalKey)){
                                        value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "yyyy"));
                                    }
                                    if(Constants.MAX_QUARTER_PARAM.equals(additionalKey)){
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                            Date date = sdf.parse(params.get(key).toString());
                                            value2 = String.format("%s", date.getMonth() / 3 + 1 );
                                        } catch (Exception ex) {
                                            log.error(ex.getMessage(), ex);
                                        }
                                    }
                                    if(Constants.MAX_MONTH_PARAM.equals(additionalKey)){
                                        value2 = String.format("%s", DataUtil.formatDatePattern(DataUtil.safeToInt(params.get(key)), "MM"));
                                    }
                                    entry.setValue(entry.getValue().toString().replaceAll(":" + additionalKey, value2));
                                }
                            }
                        }
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }
        return datas;
    }

    private Integer getTimeTypeDefault(ConfigChartDTO chartDTO, Map<String, Object> params, SaveChartItemDTO item) {
        if (chartDTO.getTimeTypeDefault() == null && (params == null || !params.containsKey(Constants.TIME_TYPE_PARAM))) {
            if (!DataUtil.isNullOrEmpty(item.getParams())) {
                Optional<SaveInputParamDTO> timeTypeParamOpt = item.getParams().stream()
                    .filter(p -> Constants.DATA_TIME_TYPE.equals(p.getFieldName())
                        || (p.getFieldName().contains(".") && Constants.DATA_TIME_TYPE.equals(p.getFieldName().substring(p.getFieldName().lastIndexOf(".") + 1))))
                    .findFirst();
                if (timeTypeParamOpt.isPresent()) {
                    int timeTypeDefault = StringUtils.isNotEmpty(timeTypeParamOpt.get().getValueDefault())
                        ? DataUtil.safeToInt(timeTypeParamOpt.get().getValueDefault())
                        : DataUtil.safeToInt(timeTypeParamOpt.get().getValue());
                    if (timeTypeDefault != 0)
                        return timeTypeDefault;
                    chartDTO.setTimeTypeDefault(timeTypeDefault);
                }

            }
        }
        return null;
    }

    private Map<String, String> getParamDefault(Map<String, Object> params) {
        Map<String, String> paramDefault = new HashMap<>();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (Constants.PARAM_CHART_DEFAULT.MAX_DATE.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.MAX_DATE);
            }
            if (Constants.PARAM_CHART_DEFAULT.MAX_DATE_NDATE.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.MAX_DATE_NDATE);
            }
            if (Constants.PARAM_CHART_DEFAULT.MAX_DATE_NMONTH.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.MAX_DATE_NMONTH);
            }
            if (Constants.PARAM_CHART_DEFAULT.MAX_DATE_NQUAR.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.MAX_DATE_NQUAR);
            }
            if (Constants.PARAM_CHART_DEFAULT.MAX_DATE_NYEAR.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.MAX_DATE_NYEAR);
            }
            if (Constants.PARAM_CHART_DEFAULT.START_OF_YEAR.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.START_OF_YEAR);
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.CURRENT_DATE);
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME);
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NMONTH.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NMONTH);
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NQUAR.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NQUAR);
            }
            if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NYEAR.equals(entry.getValue())) {
                paramDefault.put(entry.getKey(), Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NYEAR);
            }
        }
        return paramDefault;
    }

    private int getRangeTimeFromMaxDate(String configStr) {
        Optional<CatItemDTO> catItemDTO = Optional.empty();
        int rangeTime = Constants.DEFAULT_RANGE_TIME;
        if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NDATE.equals(configStr)
            || Constants.PARAM_CHART_DEFAULT.MAX_DATE_NDATE.equals(configStr)) {
            catItemDTO = catItemService.findByCode(Constants.NDATE_CATITEM);
        }
        if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NMONTH.equals(configStr)
            || Constants.PARAM_CHART_DEFAULT.MAX_DATE_NMONTH.equals(configStr)) {
            catItemDTO = catItemService.findByCode(Constants.NMONTH_CATITEM);
        }
        if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NQUAR.equals(configStr)
            || Constants.PARAM_CHART_DEFAULT.MAX_DATE_NQUAR.equals(configStr)) {
            catItemDTO = catItemService.findByCode(Constants.NQUAR_CATITEM);
        }
        if (Constants.PARAM_CHART_DEFAULT.CURRENT_DATE_RELATIVE_TIME_NYEAR.equals(configStr)
            || Constants.PARAM_CHART_DEFAULT.MAX_DATE_NYEAR.equals(configStr)) {
            catItemDTO = catItemService.findByCode(Constants.NYEAR_CATITEM);
        }
        if (catItemDTO.isPresent()) {
            rangeTime = DataUtil.safeToInt(catItemDTO.get().getItemValue());
        }
        return rangeTime;
    }

    private List<CatGraphKpiDTO> getKpiInfoForItemOfResultChart(Map<String, Object> filterParams, SaveChartItemDTO saveItem, Object firstRecord) throws JsonProcessingException {

        List<Long> kpiIds;
        List<CatGraphKpiDTO> kpis = new ArrayList<>();
//        catGraphKpiService.findByKpiIds(kpiIds);
        if (!DataUtil.isNullOrEmpty(filterParams) && filterParams.containsKey(Constants.KPI_IDS_PARAM)) {
            Object kpiParamIds = filterParams.get(Constants.KPI_IDS_PARAM);
            if (!DataUtil.isNullOrEmpty(kpiParamIds)) {
                kpiIds = new ArrayList<>(Arrays.asList(mapper.readValue(mapper.writeValueAsString(kpiParamIds), Long[].class)));
                kpis = catGraphKpiService.findByKpiIds(kpiIds);
            }
        }
        kpiIds = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(saveItem.getKpiInfos())) {
            for (SaveKpiInfoDTO kpiInfo : saveItem.getKpiInfos()) {
                if (!DataUtil.isNullOrEmpty(kpiInfo.getKpis())) {
                    for (CatGraphKpiDTO kpiItem : kpiInfo.getKpis()) {
                        kpiIds.add(kpiItem.getKpiId());
                    }
                }
            }
        }

        if (firstRecord != null) {
            Map firstRecordMap = mapper.convertValue(firstRecord, Map.class);
            if (!DataUtil.isNullOrEmpty(firstRecordMap)) {
                if (DataUtil.isNullOrEmpty(saveItem.getKpiInfos()) || DataUtil.isNullOrEmpty(saveItem.getKpiInfos().get(0).getKpis())) {
                    if (firstRecordMap.containsKey(Constants.KPI_ID_FIELD)) {
                        Long kpiId = Long.parseLong(firstRecordMap.get(Constants.KPI_ID_FIELD).toString());
                        kpiIds.add(kpiId);
                    }
                }
            }
        }

        if (kpis == null) kpis = new ArrayList<>();
        kpis.addAll(catGraphKpiService.findByKpiIds(kpiIds));

        if (firstRecord != null) {
            Map firstRecordMap = mapper.convertValue(firstRecord, Map.class);
            if (!DataUtil.isNullOrEmpty(firstRecordMap)) {
                Optional<ConfigDisplayQueryDTO> unitDisplayConfig = saveItem.getDisplayConfigs().stream()
                    .filter(d -> Constants.UNIT_DISPLAY.equals(d.getColumnChart())
                        || Constants.UNIT_DISPLAY.toLowerCase().equals(d.getColumnChart()))
                    .findFirst();
                if (unitDisplayConfig.isPresent() && firstRecordMap.containsKey(unitDisplayConfig.get().getColumnQuery())) {
                    kpis.forEach(k -> {
                        k.setUnitName(String.valueOf(firstRecordMap.get(unitDisplayConfig.get().getColumnQuery())));
                    });
                }
            }
        }
        return kpis;
    }

    private List<SaveInputParamDTO> processWhCls(Expression whCls, Map<String, Object> defaultValues, Map<String, List<Map<String, Object>>> tables) {
        List<SaveInputParamDTO> params = new ArrayList<>();
        do {
            Expression left = ((BinaryExpression) whCls).getLeftExpression();
            Expression right = ((BinaryExpression) whCls).getRightExpression();
            if (!(left instanceof BinaryExpression) && !(right instanceof BinaryExpression)) {
                params.add(sqlParserRepository.analyzeWhCls(whCls, defaultValues, tables));
            } else {
                params.add(sqlParserRepository.analyzeWhCls(right != null ? right : left, defaultValues, tables));
            }
            whCls = left;
        } while (whCls instanceof BinaryExpression);
        return params;
    }
}
