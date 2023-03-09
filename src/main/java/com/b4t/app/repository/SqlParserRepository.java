package com.b4t.app.repository;

import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.SaveDisplayQueryDTO;
import com.b4t.app.service.dto.SaveInputParamDTO;
import com.b4t.app.service.dto.SaveOrderByDTO;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.List;
import java.util.Map;

public interface SqlParserRepository {

    Map<String, String> getTableAlias(Select stmt);

    List<String> getTableNames(Select stmt);

    List<SaveDisplayQueryDTO> analyzeColumns(
        List<SaveDisplayQueryDTO> displayQueries, PlainSelect plainSelect,
        Map<String, String> aliases, Map<String, List<Map<String, Object>>> tables);

    SaveInputParamDTO analyzeWhCls(
        Expression exp, Map<String, Object> defaultValues,
        Map<String, List<Map<String, Object>>> tables);

    List<CatGraphKpiDTO> parseKpiFromParam(List<SaveInputParamDTO> params, Map<String, Object> defaultValues);

    List<SaveOrderByDTO> analyzeOrderBy(List<OrderByElement> orderByElms, Map<String, List<Map<String, Object>>> tables);

    String getFullColumnName(String columnName, Map<String, List<Map<String, Object>>> tables);

    String analyzeLimit(Limit limit);

    List<Map<String, Object>> getAllColumns(Map<String, String> aliases, List<String> tableNames, Map<String, List<Map<String, Object>>> tables);

    List<Map<String, Object>> getDescriptionOfTableToMap(String tableName);
}
