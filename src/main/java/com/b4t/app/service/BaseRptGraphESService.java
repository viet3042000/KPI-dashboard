package com.b4t.app.service;

import com.b4t.app.domain.BaseRptGraphES;

import java.util.List;

public interface BaseRptGraphESService {
    void pushDataToElasticsearch(List<BaseRptGraphES> baseRptGraphs) throws Exception;
}
