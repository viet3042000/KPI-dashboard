package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.repository.CatGraphKpiCustomRepository;
import com.b4t.app.repository.SqlParserRepository;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SqlParserRepositoryImpl implements SqlParserRepository {
    private final Logger log = LoggerFactory.getLogger(SqlParserRepositoryImpl.class);
    private final static String FIELD = "Field";
    private final static String DISPLAY_NAME = "DisplayName";
    private final CatGraphKpiCustomRepository catGraphKpiCustomRepository;
    private final EntityManager em;

    public SqlParserRepositoryImpl(
        CatGraphKpiCustomRepository catGraphKpiCustomRepository,
        EntityManager em) {
        this.catGraphKpiCustomRepository = catGraphKpiCustomRepository;
        this.em = em;
    }

    @Override
    public Map<String, String> getTableAlias(Select stmt) {
        Map<String, String> aliases = new TreeMap<>();
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        SelectDeParser deparser = new SelectDeParser(expressionDeParser, buffer) {
            @Override
            public void visit(Table table) {
                String currentTableName = table.getName();
                if (table.getAlias() != null && !"".equals(table.getAlias().getName()))
                    aliases.put(table.getAlias().getName().toLowerCase(), currentTableName.toLowerCase());
                this.getBuffer().append(table);
            }
        };
        expressionDeParser.setSelectVisitor(deparser);
        expressionDeParser.setBuffer(buffer);
        StatementDeParser sdp = new StatementDeParser(expressionDeParser, deparser, buffer);
        stmt.accept(sdp);
        return aliases;
    }

    @Override
    public List<String> getTableNames(Select stmt) {
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder.getTableList(stmt);
    }

    @Override
    public List<SaveDisplayQueryDTO> analyzeColumns(
        List<SaveDisplayQueryDTO> displayQueries, PlainSelect plainSelect,
        Map<String, String> aliases, Map<String, List<Map<String, Object>>> tables) {
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        List<SaveDisplayQueryDTO> rs = new ArrayList<>();

        if (!DataUtil.isNullOrEmpty(selectItems)) {
            for (SelectItem selectItem : selectItems) {
                if (selectItem instanceof AllColumns) {
                    for (Map.Entry<String, List<Map<String, Object>>> tableEntry : tables.entrySet()) {
                        rs.addAll(analyzeAllColumns(displayQueries, tableEntry.getKey(), tableEntry.getValue()));
                    }
                } else if (selectItem instanceof AllTableColumns) {
                    AllTableColumns allTableCols = (AllTableColumns) selectItem;
                    Table table = allTableCols.getTable();
                    if (!tables.containsKey(table.getName())) continue;
                    String tableName = table.getName();
                    List<Map<String, Object>> columns = tables.get(table.getName());
                    rs.addAll(analyzeAllColumns(displayQueries, tableName, columns));
                } else {
                    SelectExpressionItem sei = (SelectExpressionItem) selectItem;
                    Expression expression = sei.getExpression();
                    String columnQuery = StringUtils.EMPTY;
                    if (expression instanceof Column) {
                        columnQuery = ((Column) expression).getColumnName();
                    }
                    Alias alias = sei.getAlias();
                    if (alias != null) {
                        columnQuery = alias.getName();
                    }
                    List<SaveDisplayQueryValueDTO> values = analyzeValueOfColumn(columnQuery, expression, tables);
                    if (displayQueries == null) {
                        SaveDisplayQueryDTO item = new SaveDisplayQueryDTO();
                        item.setColumnQuery(columnQuery);
                        item.setValues(values);
                        rs.add(item);
                    } else {
                        for (SaveDisplayQueryDTO displayQuery : displayQueries) {
                            if (StringUtils.isNotEmpty(displayQuery.getColumnQuery())
                                && columnQuery.toLowerCase().equals(displayQuery.getColumnQuery().toLowerCase())) {
                                displayQuery.setValues(values);
                            }
                        }
                    }
                }
            }
        }
        if (displayQueries == null) return rs;
        return displayQueries;
    }

    private List<SaveDisplayQueryValueDTO> analyzeValueOfColumn(
        String columnQuery, Expression expression,
        Map<String, List<Map<String, Object>>> tables) {
        List<SaveDisplayQueryValueDTO> values = new ArrayList<>();

        if (expression instanceof Column) {
            SaveDisplayQueryValueDTO value = new SaveDisplayQueryValueDTO();
            if (StringUtils.isNotEmpty(columnQuery)) {
                value.setValue(((Column) expression).getColumnName());
                value.setLabel(columnQuery);
                value.setType(Constants.FIELD_TYPE);
            } else {
                value = getValueFromColumn((Column) expression, tables);
            }
            values.add(value);
        } else {
            if (expression instanceof Function) {
                String name = ((Function) expression).getName();
                if ("concat".equals(name.toLowerCase())) {
                    ExpressionList paramExp = ((Function) expression).getParameters();
                    if (paramExp != null && !DataUtil.isNullOrEmpty(paramExp.getExpressions())) {
                        values = paramExp.getExpressions().stream()
                            .map(p -> {
                                SaveDisplayQueryValueDTO v = new SaveDisplayQueryValueDTO();
                                if (p instanceof Column) {
                                    if (StringUtils.isNotEmpty(columnQuery)) {
                                        v.setValue(((Column) p).getColumnName());
                                        v.setLabel(columnQuery);
                                        v.setType(Constants.FIELD_TYPE);
                                    } else {
                                        v = getValueFromColumn((Column) p, tables);
                                    }
                                }
                                if (p instanceof StringValue) {
                                    v.setType(Constants.TEXT_TYPE);
                                    v.setValue(((StringValue) p).getValue().toLowerCase());
                                    v.setLabel(p.toString());
                                }
                                if (p instanceof Function) {
                                    v.setType(Constants.FUNCTION_TYPE);
                                    v.setLabel(p.toString());
                                    v.setValue(p.toString());
                                    v.setFunction(p.toString());
                                }
                                return v;
                            }).collect(Collectors.toList());
                    }
                } else {
                    SaveDisplayQueryValueDTO value = new SaveDisplayQueryValueDTO();
                    value.setType(Constants.FUNCTION_TYPE);
                    value.setFunction(expression.toString());
                    value.setLabel(StringUtils.isNotEmpty(columnQuery) ? columnQuery : expression.toString());
                    value.setValue(expression.toString());
                    values.add(value);
                }
            } else {
                SaveDisplayQueryValueDTO value = new SaveDisplayQueryValueDTO();
                value.setType(Constants.TEXT_TYPE);
                value.setLabel(StringUtils.isNotEmpty(columnQuery) ? columnQuery : expression.toString());
                value.setValue(expression.toString());
                values.add(value);
            }
        }
        return values;
    }

    private String getColumnLabel(String columnName, Map<String, List<Map<String, Object>>> tables) {
        String columnLabel = StringUtils.EMPTY;
        String finalColumnName = columnName.toLowerCase();
        Optional<Map.Entry<String, List<Map<String, Object>>>> table = tables.entrySet().stream()
            .filter(i -> i.getValue().stream()
                .anyMatch(c -> finalColumnName.equals(((String) c.get(FIELD)).toLowerCase())))
            .findFirst();
        if (table.isPresent()) {
            Optional<Map<String, Object>> columnTable = table.get().getValue().stream()
                .filter(c -> finalColumnName.equals(((String) c.get(FIELD)).toLowerCase()))
                .findFirst();
            if (columnTable.isPresent())
                columnLabel = (String) columnTable.get().get(DISPLAY_NAME);

            columnName = table.get().getKey() + "." + finalColumnName;
        }
        return StringUtils.isNotEmpty(columnLabel) ? columnLabel : columnName;
    }

    @Override
    public String getFullColumnName(String columnName, Map<String, List<Map<String, Object>>> tables) {
        String finalColumnName = columnName.toLowerCase().replace(Constants.DATA_TABLE_ALIAS + ".", "")
            .replace(Constants.KPI_TABLE_ALIAS + ".", "");

        Optional<Map.Entry<String, List<Map<String, Object>>>> table = tables.entrySet().stream()
            .filter(i -> i.getValue().stream()
                .anyMatch(c -> c.get(FIELD) != null && ((String) c.get(FIELD)).toLowerCase().endsWith(finalColumnName)))
            .findFirst();
        if (table.isPresent()) {
            columnName = table.get().getKey() + "." + finalColumnName;
        }
        return columnName;
    }

    private SaveDisplayQueryValueDTO getValueFromColumn(Column column, Map<String, List<Map<String, Object>>> tables) {
        SaveDisplayQueryValueDTO value = new SaveDisplayQueryValueDTO();
        String columnName = column.getColumnName();
        String tableName = StringUtils.EMPTY;

        String finalColumnName = columnName;
        Optional<Map.Entry<String, List<Map<String, Object>>>> table = tables.entrySet().stream()
            .filter(i -> i.getValue().stream()
                .anyMatch(c -> finalColumnName.toLowerCase().equals(((String) c.get(FIELD)).toLowerCase().replace(i.getKey() + ".", ""))))
            .findFirst();
        if (table.isPresent()) {
            tableName = table.get().getKey();
        }

        if (StringUtils.isNotEmpty(tableName)) {
            value.setType(Constants.FIELD_TYPE);
            String columnLabel = getColumnLabel(columnName, tables);
            columnName = tableName + "." + column.getColumnName().toLowerCase();
            value.setLabel(StringUtils.isNotEmpty(columnLabel) ? columnLabel : columnName);
            value.setValue(columnName);
        } else {
            value.setValue(columnName);
            value.setLabel(columnName);
        }

        return value;
    }

    @Override
    public SaveInputParamDTO analyzeWhCls(Expression exp, Map<String, Object> defaultValues, Map<String, List<Map<String, Object>>> tables) {

        SaveInputParamDTO rs = new SaveInputParamDTO();
        if ("1=1".equals(exp.toString().replaceAll(" ", "")))
            return null;
        if (!(exp instanceof ComparisonOperator) && !(exp instanceof InExpression)) return null;
        if (exp instanceof ComparisonOperator) {
            ComparisonOperator compareExp = (ComparisonOperator) exp;
            rs.setOperator(compareExp.getStringExpression());
            Expression left = compareExp.getLeftExpression();
            Expression right = compareExp.getRightExpression();
            String colName = StringUtils.EMPTY;
            if (left instanceof Column) {
                colName = ((Column) left).getColumnName();
            }
            if (right instanceof Column) {
                colName = ((Column) right).getColumnName();
            }
            if (StringUtils.isEmpty(colName)) return null;
            rs.setFieldName(getFullColumnName(colName, tables));
            String paramName = StringUtils.EMPTY;
            if (left instanceof JdbcNamedParameter) {
                paramName = ((JdbcNamedParameter) left).getName();
            }
            if (right instanceof JdbcNamedParameter) {
                paramName = ((JdbcNamedParameter) right).getName();
            }
            if (StringUtils.isNotEmpty(paramName)) {
                rs.setParamName(paramName);
                rs.setValue(":" + paramName);
                if (!DataUtil.isNullOrEmpty(defaultValues) && defaultValues.containsKey(paramName)) {
                    rs.setValueDefault(String.valueOf(defaultValues.get(paramName)));
                }
                rs.setFilterParam(true);
            }
            if (left instanceof LongValue || left instanceof StringValue) {
                rs.setValue(left.toString());
            }
            if (right instanceof LongValue || right instanceof StringValue) {
                rs.setValue(right.toString());
            }
        }
        if (exp instanceof InExpression) {
            InExpression inExp = (InExpression) exp;
            Expression left = inExp.getLeftExpression();
            String colName = StringUtils.EMPTY;
            if (left instanceof Column) {
                colName = ((Column) left).getColumnName();
            }
            if (StringUtils.isEmpty(colName)) return null;
            rs.setOperator(Constants.IN_OPERATOR);
            if (inExp.isNot()) {
                rs.setOperator(Constants.NOT_IN_OPERATOR);
            }
            rs.setParamName(colName);
            rs.setFieldName(getFullColumnName(colName, tables));
            ItemsList list = inExp.getLeftItemsList();
            if (list == null)
                list = inExp.getRightItemsList();
            if (list != null) {
                if (list instanceof ExpressionList) {
                    List<String> exps = ((ExpressionList) list).getExpressions().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                    rs.setValue(StringUtils.join(exps, ","));
                } else {
                    rs.setValue(list.toString());
                }
            }
        }
        return rs;
    }

    @Override
    public List<CatGraphKpiDTO> parseKpiFromParam(List<SaveInputParamDTO> params, Map<String, Object> defaultValues) {
        List<CatGraphKpiDTO> kpis = new ArrayList<>();
        Optional<SaveInputParamDTO> kpiParam = params.stream()
            .filter(p -> !DataUtil.isNullOrEmpty(p.getFieldName()) && p.getFieldName().contains(Constants.KPI_ID_FIELD.toLowerCase()))
            .findFirst();

        if (kpiParam.isPresent()) {
            List<Long> kpiIds = new ArrayList<>();
            if ((":" + Constants.KPI_IDS_PARAM).equals(kpiParam.get().getValue())) {
                List<Object> defaultKpiIds = (List<Object>) defaultValues.get(Constants.KPI_IDS_PARAM);
                if (!DataUtil.isNullOrEmpty(defaultKpiIds))
                    kpiIds = defaultKpiIds.stream().map(DataUtil::safeToLong).collect(Collectors.toList());
            } else if (StringUtils.isNotEmpty(kpiParam.get().getValue())) {
                kpiIds = Arrays.stream(kpiParam.get().getValue().split(","))
                    .map(i -> DataUtil.safeToLong(i.trim())).collect(Collectors.toList());
            }
            kpiIds = kpiIds.stream().distinct().collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(kpiIds)) {
                kpis = catGraphKpiCustomRepository.findAll(null, kpiIds, PageRequest.of(0, kpiIds.size())).getContent();
                kpis = kpis.stream()
                    .peek(k -> k.setKpiName(String.format("%s_%s", k.getKpiId(), k.getKpiName())))
                    .collect(Collectors.toList());
            }
        }

        return kpis;
    }

    @Override
    public List<SaveOrderByDTO> analyzeOrderBy(List<OrderByElement> orderByElms, Map<String, List<Map<String, Object>>> tables) {
        List<SaveOrderByDTO> orderBys = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(orderByElms)) {
            orderBys = orderByElms.stream().map(o -> {
                SaveOrderByDTO dto = new SaveOrderByDTO();
                Expression exp = o.getExpression();
                if (exp instanceof Column) {
                    dto.setValue(getFullColumnName(((Column) exp).getColumnName(), tables));
                    dto.setType(Constants.FIELD_TYPE);
                    dto.setLabel(getColumnLabel(((Column) exp).getColumnName(), tables));
                } else if (exp instanceof Function) {
                    dto.setLabel(exp.toString());
                    dto.setFunction(exp.toString());
                    dto.setType(Constants.FUNCTION_TYPE);
                } else {
                    dto.setValue(exp.toString());
                    dto.setLabel(exp.toString());
                    dto.setType(Constants.TEXT_TYPE);
                }

                dto.setSortDir(o.isAsc() ? "asc" : "desc");
                return dto;
            }).collect(Collectors.toList());
        }

        return orderBys;
    }

    private List<SaveDisplayQueryDTO> analyzeAllColumns(List<SaveDisplayQueryDTO> displayQueries, String tableName, List<Map<String, Object>> columns) {
        List<SaveDisplayQueryDTO> rs = new ArrayList<>();

        for (Map<String, Object> column : columns) {
            String columnQuery = (String) column.get(FIELD);
            SaveDisplayQueryValueDTO value = new SaveDisplayQueryValueDTO();
            value.setLabel((String) column.get(DISPLAY_NAME));
            value.setType(Constants.FIELD_TYPE);
            value.setValue(tableName + "." + columnQuery);
            if (displayQueries == null) {
                SaveDisplayQueryDTO item = new SaveDisplayQueryDTO();
                item.setColumnQuery(tableName + "." + columnQuery);
                item.setValues(Collections.singletonList(value));
                rs.add(item);
            } else {
                for (SaveDisplayQueryDTO displayQuery : displayQueries) {
                    if (StringUtils.isEmpty(displayQuery.getColumnQuery())) continue;
                    if (columnQuery.toLowerCase().equals(displayQuery.getColumnQuery().toLowerCase())) {
                        displayQuery.setValues(Collections.singletonList(value));
                    }
                }
            }
        }
        return displayQueries == null ? rs : displayQueries;
    }

    @Override
    public String analyzeLimit(Limit limit) {
        if (limit != null && !limit.isLimitNull()) {
            return limit.toString().replaceAll("(?i)limit", "").trim();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<Map<String, Object>> getAllColumns(Map<String, String> aliases, List<String> tableNames, Map<String, List<Map<String, Object>>> tables) {
        List<Map<String, Object>> allColumns = new ArrayList<>();
        for (String tableName : tableNames) {
            Optional<String> alias = aliases.entrySet().stream()
                .filter(e -> tableName.equals(e.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
            try {
                List<Map<String, Object>> desColumns = getDescriptionOfTableToMap(tableName);
                desColumns = desColumns.stream()
                    .peek(c -> c.put(FIELD, alias.orElse(tableName) + "." + c.get(FIELD)))
                    .collect(Collectors.toList());
                allColumns.addAll(desColumns);
                tables.put(alias.orElse(tableName), desColumns);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BadRequestAlertException(Translator.toLocale("error.configchart.tablenotexisted", new Object[]{tableName}),
                    "configchart", "error.configchart.tablenotexisted");
            }
        }

        return allColumns;
    }

    public List<Map<String, Object>> getDescriptionOfTableToMap(String tableName) {
        Query q = em.createNativeQuery(" SHOW FULL COLUMNS FROM " + tableName);
        org.hibernate.query.Query hibernateQuery = q.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(new ResultTransformer() {
                @Override
                public Map<String, Object> transformTuple(Object[] rowData, String[] aliasNames) {
                    Map<String, Object> row = new HashMap<>();

                    for (int i = 0; i < aliasNames.length; i++) {
                        String value = rowData[i] == null ? null : String.valueOf(rowData[i]);
                        if (FIELD.equals(aliasNames[i])) {
                            if (value == null || StringUtils.isEmpty(value)) continue;
                            value = value.toLowerCase();
                            row.put(aliasNames[i], value);
                            value = "label.column." + value;
                            row.put(DISPLAY_NAME, Translator.toLocaleWithDefault(value, value));
                        } else if ("Type".equals(aliasNames[i]) || "Comment".equals(aliasNames[i])) {
                            row.put(aliasNames[i], value);
                        }
                    }
                    String displayName = (String) row.get(DISPLAY_NAME);
                    if (displayName.startsWith("label.column."))
                        if (StringUtils.isNotEmpty((String) row.get("Comment")))
                            row.put(DISPLAY_NAME, row.get("Comment"));
                        else
                            row.put(DISPLAY_NAME, row.get(FIELD));
                    return row;
                }

                @Override
                public List<Map<String, Object>> transformList(List list) {
                    return DataUtil.isNullOrEmpty(list)
                        ? new ArrayList<>()
                        : (List<Map<String, Object>>) list.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                }
            });

        return (List<Map<String, Object>>) hibernateQuery.list();
    }
}
