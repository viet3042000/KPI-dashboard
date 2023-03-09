package com.b4t.app.service;

import com.b4t.app.service.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jsqlparser.JSQLParserException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface BuildChartService {

    ChartResultDTO getChartResult(ConfigChartDTO chartDTO, List<ConfigChartItemDTO> chartItems, ChartParamDTO params) throws JsonProcessingException, ParseException;

    List<Object> getDescriptionOfTable(String tableName);

    List<Map<String, Object>> getDescriptionOfTableToMap(String tableName);

    File getFile(String fileName);

    ChartResultDTO saveChart(SaveChartDTO dto) throws IOException, ParseException;

    ChartResultDTO preview(SaveChartDTO dto) throws JsonProcessingException, ParseException;

    SaveChartItemDTO generateInputCondition(SaveChartItemDTO item, ConfigQueryChartDTO query, List<SaveDisplayQueryDTO> columns) throws JsonProcessingException;

    ChartMapDTO getChartMapsData(ChartMapParramDTO chartMapParramDTO);

    Object getMaxTime(ChartMapParramDTO chartMapParramDTO);

    List<RangeColorDTO> getRangeColor(RangeColorDTO rangeColorDTO);

    Long getScreenIdMap(Long profileId);
}
