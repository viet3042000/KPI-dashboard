package com.b4t.app.repository;

import com.b4t.app.service.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Spring Data  repository for the ConfigChart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuildChartRepository {

    List<Object> getDescriptionOfTable(String tableName);

    List<Map<String, Object>> getDescriptionOfTableToMap(String tableName);

    ConfigQueryChartDTO buildQuery(List<SaveChartItemDTO> items, boolean hasSave, boolean overview) throws JsonProcessingException;

    ChartResultDTO getChartResult(ConfigChartDTO chartDTO, List<ConfigChartItemDTO> chartItems, ChartParamDTO filterParamsObj) throws JsonProcessingException, ParseException;

    SaveChartItemDTO generateInputCondition(SaveChartItemDTO item, ConfigQueryChartDTO query, List<SaveDisplayQueryDTO> columns) throws JsonProcessingException;
}
