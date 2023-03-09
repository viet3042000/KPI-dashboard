package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.StringUtils;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.BaseRptGraphES;
import com.b4t.app.domain.ObjectRptGraphES;
import com.b4t.app.service.BaseRptGraphESService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class BaseRptGraphESServiceImpl implements BaseRptGraphESService {
    private static final Logger logger = LoggerFactory.getLogger(BaseRptGraphESServiceImpl.class);
//    private final String TYPE = "_doc";
    private static final int SIZE = 100; // Number of element to Insert or Delete
    private static final int TIME_OUT = 60; //seconds
    @Autowired
    EntityMapper entityMapper;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void pushDataToElasticsearch(List<BaseRptGraphES> baseRptGraphs) throws Exception {
        //Luu RPT_GRAPH vao elasticsearch
        this.pushRptGraphToElasticsearch(baseRptGraphs);

        //Luu doi tuong vao elasticsearch
        this.pushObjectToElasticsearch(baseRptGraphs);
    }

    private ActionListener<BulkResponse> createListener() {
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                logger.info("save to elasticsearch:" + bulkResponse.getItems().length + " Items");
//                for (BulkItemResponse bulkItemResponse : bulkResponse) {
//                    DocWriteResponse itemResponse = bulkItemResponse.getResponse();
//                    switch (bulkItemResponse.getOpType()) {
//                        case INDEX:
//                        case CREATE:
//                            IndexResponse indexResponse = (IndexResponse) itemResponse;
//                            logger.debug("create object to index:" + indexResponse.toString());
//                            break;
//                    }
//                }
            }

            @Override
            public void onFailure(Exception e) {
                logger.error(e.getMessage(), e);
            }
        };
        return listener;
    }

    public void pushRptGraphToElasticsearch(List<BaseRptGraphES> baseRptGraphs) {
        try {
            ActionListener<BulkResponse> listener = createListener();
            int size = baseRptGraphs.size() > SIZE ? SIZE : baseRptGraphs.size();
            List<List<BaseRptGraphES>> lstSub = Lists.partition(baseRptGraphs, size);
//            List<BaseRptGraphES> lstData = lstSub.stream().flatMap(Collection::stream).collect(toList());
            int total = 0;
            for (List<BaseRptGraphES> lstData : lstSub) {
                BulkRequest request = new BulkRequest();
                request.timeout(new TimeValue(TIME_OUT, TimeUnit.SECONDS));
                for (BaseRptGraphES baseDTO : lstData) {
                    Double rate = DataUtil.isNullOrZero(baseDTO.getRate()) ? 1.0 : baseDTO.getRate();
                    if (!DataUtil.isNullOrEmpty(baseDTO.getVal())) {
                        baseDTO.setVal(Math.round((baseDTO.getVal() / rate)*100.0)/100.0);
                    }
                    if (!DataUtil.isNullOrEmpty(baseDTO.getValAcc())) {
                        baseDTO.setValAcc(Math.round((baseDTO.getValAcc() / rate)*100.0)/100.0);
                    }
                    if (!DataUtil.isNullOrEmpty(baseDTO.getValTotal())) {
                        baseDTO.setValTotal(Math.round((baseDTO.getValTotal() / rate)*100.0)/100.0);
                    }
                    if (!DataUtil.isNullOrEmpty(baseDTO.getValPlanYear())) {
                        baseDTO.setValPlanYear(Math.round((baseDTO.getValPlanYear() / rate)*100.0)/100.0);
                    }
                    IndexRequest indexRequest = new IndexRequest(Constants.ES.BASE_RPT_GRAPH, Constants.ES.TYPE);
                    indexRequest.id(getId(baseDTO));
                    indexRequest.source(new ObjectMapper().writeValueAsString(baseDTO), XContentType.JSON);
                    request.add(indexRequest);
                }
                client.bulkAsync(request, RequestOptions.DEFAULT, listener);
                total = total + lstData.size();
                logger.info("Saved " + total + " RPT_GRAPH");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    public void pushObjectToElasticsearch(List<BaseRptGraphES> baseRptGraphs) throws Exception {
        try {
            List<ObjectRptGraphES> lstObj = baseRptGraphs.stream().map(e -> {
                ObjectRptGraphES obj = new ObjectRptGraphES();
                obj.setObjCode(e.getObjCode());
                obj.setObjCodeFull(e.getObjCodeFull());
                obj.setObjName(e.getObjName());
                obj.setObjNameFull(e.getObjNameFull());
                obj.setObjNameUnsigned(e.getObjNameUnsigned());
                obj.setParentCode(e.getParentCode());
                obj.setParentName(e.getParentName());
                return obj;
            }).filter(StringUtils.distinctByKey(p -> p.getObjCodeFull())).collect(toList());
            ActionListener<BulkResponse> listener = createListener();

            int size = lstObj.size() > SIZE ? SIZE : lstObj.size();
            List<List<ObjectRptGraphES>> lstSub = Lists.partition(lstObj, size);
            int total = 0;
            for (List<ObjectRptGraphES> lstData : lstSub) {
                BulkRequest request = new BulkRequest();
                request.timeout(new TimeValue(TIME_OUT, TimeUnit.SECONDS));
                for (ObjectRptGraphES obj : lstData) {
                    IndexRequest indexRequest = new IndexRequest(Constants.ES.OBJ_RPT_GRAPH, Constants.ES.TYPE);
                    indexRequest.id(getIdObj(obj));
                    indexRequest.source(new ObjectMapper().writeValueAsString(obj), XContentType.JSON);
                    request.add(indexRequest);
                }
                client.bulkAsync(request, RequestOptions.DEFAULT, listener);
                total = total + lstData.size();
                logger.info("Saved " + total + " Object");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    private String getIdObj(ObjectRptGraphES obj) {
        StringBuilder sb = new StringBuilder();
        if (!DataUtil.isNullOrEmpty(obj.getParentCode())) {
            sb.append(obj.getParentCode()).append("-");
        }
        sb.append(obj.getObjCode());
        return sb.toString();
    }

    private String getId(BaseRptGraphES baseDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseDTO.getDomainCode());
        sb.append("_").append(baseDTO.getPrdId());
        sb.append("_").append(baseDTO.getKpiId());
        sb.append("_").append(baseDTO.getInputLevel());
        sb.append("_").append(baseDTO.getTimeType());
        sb.append("_");
        if (!DataUtil.isNullOrEmpty(baseDTO.getParentCode())) {
            sb.append(baseDTO.getParentCode()).append("-");
        }
        sb.append(baseDTO.getObjCode());
        return sb.toString();
    }
}
