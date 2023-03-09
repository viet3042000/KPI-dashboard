package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.BaseRptGraph;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.domain.ObjectRptGraphES;
import com.b4t.app.service.KpiReportSearchESService;
import com.b4t.app.service.dto.ReportKpiDTO;
import com.b4t.app.service.dto.TreeValue;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class KpiReportSearchESServiceImpl implements KpiReportSearchESService {

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    private RestHighLevelClient client;


    @Override
    public BaseRptGraph getLastData(ReportKpiDTO reportKpiDTO) throws Exception {
        BaseRptGraph baseRptGraph = null;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery();
        List<String> listKpi = reportKpiDTO.getKpiIds().stream().map(TreeValue::getId).collect(Collectors.toList());
        ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery(Constants.BASE_RPT_GRAPH.KPI_ID, listKpi));
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getObjects())) {
            ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery("objCodeFull.keyword", reportKpiDTO.getObjects()));
        }

        List<CompositeValuesSourceBuilder<?>> sources = new ArrayList<>();
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.PRD_ID).field(Constants.BASE_RPT_GRAPH.PRD_ID).order("desc"));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.INPUT_LEVEL).field(Constants.BASE_RPT_GRAPH.INPUT_LEVEL).order("asc"));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.TIME_TYPE).field(Constants.BASE_RPT_GRAPH.TIME_TYPE));
        CompositeAggregationBuilder compositeAggregationBuilder = new CompositeAggregationBuilder("data", sources);
        sourceBuilder.query(queryBuilder);
        sourceBuilder.size(0);
        sourceBuilder.aggregation(compositeAggregationBuilder);
        SearchRequest searchRequest = new SearchRequest(Constants.ES.BASE_RPT_GRAPH);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ParsedComposite parsedComposite = searchResponse.getAggregations().get("data");
        List<ParsedComposite.ParsedBucket> list = parsedComposite.getBuckets();
        if (!DataUtil.isNullOrEmpty(list)) {
            ParsedComposite.ParsedBucket composite = list.get(0);
            composite.getKeyAsString();
            baseRptGraph = entityMapper.readObject(composite.getKey(), BaseRptGraph.class);
        }
        return baseRptGraph;
    }


    public Page<BaseRptGraphES> onSearch(ReportKpiDTO reportKpiDTO, Pageable pageable) throws Exception {
        List<BaseRptGraphES> baseRptGraph = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery();
        List<String> listKpi = reportKpiDTO.getKpiIds().stream().map(TreeValue::getId).collect(Collectors.toList());
        ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery(Constants.BASE_RPT_GRAPH.KPI_ID, listKpi));
        if(!DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels())) {
            ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery(Constants.BASE_RPT_GRAPH.INPUT_LEVEL, reportKpiDTO.getInputLevels()));
        }
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getObjects())) {
            ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery("objCodeFull.keyword", reportKpiDTO.getObjects()));
        }
        ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termQuery(Constants.BASE_RPT_GRAPH.TIME_TYPE, reportKpiDTO.getTimeType()));

        RangeQueryBuilder rangQuery = null;
        if (Constants.VALUE_TYPE.VAL.equalsIgnoreCase(reportKpiDTO.getValueType())) {
            rangQuery = QueryBuilders.rangeQuery("val");
        } else if (Constants.VALUE_TYPE.VAL_ACC.equalsIgnoreCase(reportKpiDTO.getValueType())) {
            rangQuery = QueryBuilders.rangeQuery("valAcc");
        } else if (Constants.VALUE_TYPE.VAL_TOTAL.equalsIgnoreCase(reportKpiDTO.getValueType())) {
            rangQuery = QueryBuilders.rangeQuery("valTotal");
        }
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getFromValue()) && rangQuery != null) {
            rangQuery.from(reportKpiDTO.getFromValue());
        }
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getToValue())  && rangQuery != null) {
            rangQuery.to(reportKpiDTO.getToValue());
        }
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getFromValue()) || !DataUtil.isNullOrEmpty(reportKpiDTO.getToValue())) {
            ((BoolQueryBuilder) queryBuilder).filter(rangQuery);
        }

        if (reportKpiDTO.getFromDate() != null || reportKpiDTO.getToDate() != null) {
            RangeQueryBuilder prdIdRange = QueryBuilders.rangeQuery("prdId");
            if (reportKpiDTO.getFromDate() != null) {
                prdIdRange.from(DataUtil.getDateInt(reportKpiDTO.getFromDate(), Constants.DATE_FORMAT_YYYYMMDD));
            }
            if (reportKpiDTO.getToDate() != null) {
                prdIdRange.to(DataUtil.getDateInt(reportKpiDTO.getToDate(), Constants.DATE_FORMAT_YYYYMMDD));
            }
            ((BoolQueryBuilder) queryBuilder).filter(prdIdRange);
        }

        sourceBuilder.query(queryBuilder);
        if (pageable != null) {
            sourceBuilder.from(Long.valueOf(pageable.getOffset()).intValue());
            sourceBuilder.size(pageable.getPageSize());
        }
        sourceBuilder.sort(Constants.BASE_RPT_GRAPH.PRD_ID, SortOrder.DESC);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest(Constants.ES.BASE_RPT_GRAPH);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] result = searchResponse.getHits().getHits();
        long total = searchResponse.getHits().getTotalHits();
        for (SearchHit searchHit : result) {
            BaseRptGraphES baseRpt = entityMapper.mapToObject(searchHit.getSourceAsString(), BaseRptGraphES.class);
            baseRptGraph.add(baseRpt);
        }
        return new PageImpl<>(baseRptGraph, pageable, total);
    }

    @Override
    public List<ObjectRptGraphES> onSearchObject(String keyword) throws Exception {
        List<ObjectRptGraphES> lstObj = new ArrayList<>();

        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("objNameUnsigned", keyword);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(100);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest(Constants.ES.OBJ_RPT_GRAPH);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            ObjectRptGraphES obj = entityMapper.mapToObject(searchHit.getSourceAsString(), ObjectRptGraphES.class);
            lstObj.add(obj);
        }
        return lstObj;
    }

    @Override
    public List<ObjectRptGraphES> getAllObject(ReportKpiDTO reportKpiDTO) throws Exception {
        List<ObjectRptGraphES> lstObj = new ArrayList<>();

        QueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getKpiIds())) {
            List<String> listKpi = reportKpiDTO.getKpiIds().stream().map(TreeValue::getId).collect(Collectors.toList());
            ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery(Constants.BASE_RPT_GRAPH.KPI_ID, listKpi));
        }
        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getInputLevels())) {
            ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termsQuery(Constants.BASE_RPT_GRAPH.INPUT_LEVEL, reportKpiDTO.getInputLevels()));
        }

        if (!DataUtil.isNullOrEmpty(reportKpiDTO.getTimeType())) {
            ((BoolQueryBuilder) queryBuilder).filter(QueryBuilders.termQuery(Constants.BASE_RPT_GRAPH.TIME_TYPE, reportKpiDTO.getTimeType()));
        }


        List<CompositeValuesSourceBuilder<?>> sources = new ArrayList<>();
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.OBJ_CODE).field("objCode.keyword"));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.OBJ_NAME).field("objName.keyword"));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.OBJ_CODE_FULL).field("objCodeFull.keyword"));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.PARENT_NAME).field("parentName.keyword").missingBucket(true));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.PARENT_CODE).field("parentCode.keyword").missingBucket(true));
        sources.add(new TermsValuesSourceBuilder(Constants.BASE_RPT_GRAPH.INPUT_LEVEL).field(Constants.BASE_RPT_GRAPH.INPUT_LEVEL));
        CompositeAggregationBuilder compositeAggregationBuilder = new CompositeAggregationBuilder("objects", sources).size(10000);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        sourceBuilder.query(queryBuilder);
        sourceBuilder.aggregation(compositeAggregationBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(100);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest(Constants.ES.BASE_RPT_GRAPH);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        ParsedComposite parsedComposite = searchResponse.getAggregations().get("objects");
        List<ParsedComposite.ParsedBucket> list = parsedComposite.getBuckets();
        if (!DataUtil.isNullOrEmpty(list)) {
            for (ParsedComposite.ParsedBucket composite : list) {
                ObjectRptGraphES obj = entityMapper.readObject(composite.getKey(), ObjectRptGraphES.class);
                lstObj.add(obj);
            }
        }
        return lstObj;
    }
}
