package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.domain.KpiReport;
import com.b4t.app.repository.CatKpiSynonymRepository;
import com.b4t.app.repository.KpiReportRepository;
import com.b4t.app.repository.KpiReportSearchESRepository;
import com.b4t.app.repository.KpiReportSearchRepository;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.KpiReportSearchESService;
import com.b4t.app.service.KpiReportSearchService;
import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.dto.ReportKpiDTO;
import com.b4t.app.service.dto.TreeValue;
import com.b4t.app.service.mapper.CatKpiReportMapper;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class KpiReportSearchServiceImpl implements KpiReportSearchService {
    private static final Logger logger = LoggerFactory.getLogger(KpiReportSearchServiceImpl.class);

    @Autowired
    KpiReportSearchRepository kpiReportSearchRepository;

    @Autowired
    CatKpiReportMapper catKpiReportMapper;

    @Autowired
    CatItemService catItemService;

    @Autowired
    KpiReportSearchESRepository kpiReportSearchESRepository;

    @Autowired
    CatKpiSynonymRepository catKpiSynonymRepository;

    @Autowired
    KpiReportRepository kpiReportRepository;

    @Autowired
    KpiReportSearchESService kpiReportSearchESService;


    @Override
    @Async
    public CompletableFuture<List<ComboDTO>> findHashTag(String keyword) {
        List<ComboDTO> result = catKpiSynonymRepository.findAllBySynonymLike(keyword).stream().map(e -> {
            ComboDTO comboDTO = new ComboDTO("tag#" + e, e.toString(), "hash-outline");
            return comboDTO;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Async
    public CompletableFuture<List<ComboDTO>> onSearchKpi(String keyword) {
        List<ComboDTO> result = kpiReportSearchRepository.onSearchKpi(keyword, null, 50).stream()
            .map(e -> {
                ComboDTO comboDTO = new ComboDTO("kpi#" + e.getKpiId(), e.getKpiName(), "trending-up-outline");
                return comboDTO;
            }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Async
    public CompletableFuture<List<ComboDTO>> findObject(ReportKpiDTO reportKpiDTO, String keyword) throws Exception {

        List<ComboDTO> lstObj;
        try {
            lstObj = kpiReportSearchESService.onSearchObject(keyword).stream().map(e -> {
                ComboDTO comboDTO = new ComboDTO("object#" + e.getObjCodeFull(), e.getObjNameFull(), "people-outline");
                return comboDTO;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            lstObj = kpiReportRepository.findObjectReportForTree(reportKpiDTO, keyword).stream().map(e -> {
                ComboDTO comboDTO = new ComboDTO("object#" + e.getObjectCode(), e.getObjectName(), "people-outline");
                return comboDTO;
            }).collect(Collectors.toList());
        }
        return CompletableFuture.completedFuture(lstObj);
    }

    @Override
    public List<TreeValue> onSearchKpi(String keyword, List<String> lstKpi, Integer size) {
        return kpiReportSearchRepository.onSearchKpi(keyword, lstKpi, size).stream().map(catKpiReportMapper::toTreeValue).collect(Collectors.toList());
    }

    @Override
    public List<TreeValue> onSearchKpiHashTag(List<String> lstTag) {
        return kpiReportSearchRepository.onSearchKpiHashTag(lstTag).stream().map(catKpiReportMapper::toTreeValue).collect(Collectors.toList());
    }

    public ReportKpiDTO buildCondition(List<String> lstCondition) {
        ReportKpiDTO reportKpiDTO = new ReportKpiDTO();
        if (!DataUtil.isNullOrEmpty(lstCondition)) {
            //Build TimeType
            List<String> lstTimeType = lstCondition.stream().filter(e -> e.startsWith("timeType")).collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(lstTimeType)) {
                reportKpiDTO.setTimeType(lstTimeType.get(0).replace("timeType#", ""));
            }
            //Build InputLevel
            List<String> lstInputLevel = lstCondition.stream().filter(e -> e.startsWith("inputLevel"))
                .map(e -> e.replace("inputLevel#", ""))
                .collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(lstInputLevel)) {
                reportKpiDTO.setInputLevels(lstInputLevel);
            }
            //Build Object
            List<String> lstObject = lstCondition.stream().filter(e -> e.startsWith("object"))
                .map(e -> e.replace("object#", ""))
                .collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(lstObject)) {
                reportKpiDTO.setObjects(lstObject);
            }
            //Build Kpi
            List<TreeValue> lstKpiAll = new ArrayList<>();
            if (!DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds())) {
                lstKpiAll.addAll(reportKpiDTO.getKpiIds());
            }

            List<String> lstKpi = lstCondition.stream().filter(e -> e.startsWith("kpi"))
                .map(e -> e.replace("kpi#", ""))
                .collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(lstKpi)) {
                List<TreeValue> lstTreeKpi = this.onSearchKpi(null, lstKpi, null);
                if (!DataUtil.isNullOrEmpty(lstTreeKpi)) {
                    lstKpiAll.addAll(lstTreeKpi);
                }
            }
            List<String> lstTag = lstCondition.stream().filter(e -> e.startsWith("tag"))
                .map(e -> e.replace("tag#", ""))
                .collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(lstTag)) {
                reportKpiDTO.setHaveKpiTag(true);
                List<TreeValue> lstTreeKpi = this.onSearchKpiHashTag(lstTag);
                if (!DataUtil.isNullOrEmpty(lstTreeKpi)) {
                    lstKpiAll.addAll(lstTreeKpi);
                }
            }

            if (!DataUtil.isNullOrEmpty(lstKpiAll)) {
                reportKpiDTO.setKpiIds(lstKpiAll);
            }

        }
        return reportKpiDTO;
    }

    public Page<KpiReport> search(String queryText, int pageNum, int size) {
        QueryBuilder builder;
        PageRequest pageable;

        if (!DataUtil.isNullOrEmpty(queryText)) {
            pageable = PageRequest.of(pageNum, size);
            builder = QueryBuilders.multiMatchQuery(queryText, "kpi_name", "obj_name");
            ((MultiMatchQueryBuilder) builder).operator(Operator.OR);
            ((MultiMatchQueryBuilder) builder).type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        } else {
            builder = QueryBuilders.matchAllQuery();
            pageable = PageRequest.of(pageNum, size, Sort.by("modification_time").descending());
        }
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(builder);
        nativeSearchQueryBuilder.withPageable(pageable);
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        Page<KpiReport> page = kpiReportSearchESRepository.search(query);
        return page;
    }
}
