package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class BuildChartResource {
    private final Logger log = LoggerFactory.getLogger(BuildChartResource.class);

    private static final String ENTITY_NAME = "configChart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private BuildChartService buildChartService;

    @Autowired
    private ConfigChartService configChartService;

    @Autowired
    private ConfigChartItemService configChartItemService;

    @Autowired
    private ConfigQueryChartService configQueryChartService;

    @Autowired
    private ConfigDisplayQueryService configDisplayQueryService;

    @Autowired
    private CustomService customService;

    public BuildChartResource(BuildChartService buildChartService) {
        this.buildChartService = buildChartService;
    }

    @GetMapping("/get-chart-result/{id}")
    public ResponseEntity<ChartResultDTO> getData(@PathVariable Long id, ChartParamDTO params) throws JsonProcessingException, ParseException {
        Date startTime = new Date();
        log.info("start process request get-chart-result/" + id + " " + startTime);

        Optional<ConfigChartDTO> chartOpt = configChartService.findOne(id);
        if (!chartOpt.isPresent() || Constants.STATUS_DISABLED.equals(chartOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "isnotexisted");
        }
        ConfigChartDTO chart = chartOpt.get();
        List<ConfigChartItemDTO> chartItems = configChartItemService.findByChartId(id);
        if (!DataUtil.isNullOrEmpty(chartItems)) {
            List<Long> queryIds = chartItems.stream().map(ConfigChartItemDTO::getQueryId).distinct().collect(Collectors.toList());
            List<ConfigQueryChartDTO> queries = configQueryChartService.findByIds(queryIds);
            List<Long> chartItemIds = chartItems.stream().map(ConfigChartItemDTO::getId).collect(Collectors.toList());
            List<ConfigDisplayQueryDTO> displayQueries = configDisplayQueryService.findByChartItemIds(chartItemIds);

            chartItems = chartItems.stream().peek(i -> {
                Optional<ConfigQueryChartDTO> query = queries.stream().filter(q -> q.getId().equals(i.getQueryId())).findFirst();
                query.ifPresent(i::setQuery);
                List<ConfigDisplayQueryDTO> displayConfigs = displayQueries.stream()
                    .filter(dq -> i.getId().equals(dq.getItemChartId()) && Constants.STATUS_ACTIVE.equals(dq.getStatus())).collect(toList());

                if (!DataUtil.isNullOrEmpty(displayConfigs)) {
                    i.setDisplayConfigs(displayConfigs);
                }
            }).collect(Collectors.toList());
        }
        ChartResultDTO result = buildChartService.getChartResult(chart, chartItems, params);
        if (chart.getChartIdNextto() != null) {
            Optional<ConfigChartDTO> nextToChartOpt = configChartService.findOne(chart.getChartIdNextto());
            if (!nextToChartOpt.isPresent() || Constants.STATUS_DISABLED.equals(nextToChartOpt.get().getStatus())) {
                result.setChartIdNextto(null);
            }
        }
        result.getDetails().forEach(res -> {
            res.getQuery().setQueryMaxPrdId(null);
            res.getQuery().setQueryData(null);
        });
        log.info("end process: " + (new Date().getTime() - startTime.getTime()) + "ms");

        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    @GetMapping("/build-chart/get-description/{tableName}")
    public ResponseEntity<List<Object>> getDescriptionTable(@PathVariable String tableName) throws URISyntaxException {
        List result = buildChartService.getDescriptionOfTable(tableName);
        return ResponseEntity.created(new URI("/build-chart/get-description/" + tableName))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, String.valueOf(result)))
            .body(result);
    }

    @GetMapping("/get-table-description")
    public ResponseEntity<List<Object>> getDescriptionTableToMap(String tableName) {
        List result = buildChartService.getDescriptionOfTableToMap(tableName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-time-type-by-kpis")
    public ResponseEntity<List<CatItemDTO>> getTimeTypeByKpis(Long[] kpiIds) {
        List<CatItemDTO> result = customService.getTimeTypeByKpis(kpiIds);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/analyze-sql")
    public ResponseEntity<SaveChartItemDTO> analyzeSql(@RequestBody SaveChartItemDTO dto) throws JsonProcessingException {
//        ConfigChartItemDTO chartItem, ConfigQueryChartDTO query, List<SaveDisplayQueryDTO> columns
        ConfigQueryChartDTO query = dto.getQuery() != null ? dto.getQuery() : new ConfigQueryChartDTO();
        query.setQueryData(dto.getCustomizeSql());
        query.setQueryMaxPrdId(dto.getCustomizeMaxPrdIdSql());
        SaveChartItemDTO result = buildChartService.generateInputCondition(dto, query, dto.getColumns());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-data-chart-maps")
    public ResponseEntity<ChartMapDTO> getDataChartMaps(ChartMapParramDTO chartMapParramDTO) {
        ChartMapDTO result = buildChartService.getChartMapsData(chartMapParramDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-max-time-of-kpi")
    public ResponseEntity<Object> getMaxTime(ChartMapParramDTO chartMapParramDTO) {
        Object object = buildChartService.getMaxTime(chartMapParramDTO);
        return ResponseEntity.ok().body(object);
    }

    @GetMapping("/get-range-of-color")
    public ResponseEntity<List<RangeColorDTO>> getRangeColor(RangeColorDTO rangeColorDTO) {
        List<RangeColorDTO> lst = buildChartService.getRangeColor(rangeColorDTO);
        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("/get-screen-maps-id/{profileId}")
    public ResponseEntity<Long> getScreenIdMap(@PathVariable Long profileId) throws URISyntaxException {
        Long screenId = buildChartService.getScreenIdMap(profileId);
        return ResponseEntity.created(new URI("/get-screen-maps-id/" + profileId)).body(screenId);
    }

}
