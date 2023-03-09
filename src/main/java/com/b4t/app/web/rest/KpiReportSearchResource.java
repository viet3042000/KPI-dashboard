package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.KpiReportSearchService;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.dto.ReportKpiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class KpiReportSearchResource {
    private final Logger log = LoggerFactory.getLogger(KpiReportResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private KpiReportSearchService kpiReportSearchService;

    @Autowired
    CatItemService catItemService;


    @PostMapping("/kpi-report-search/search-condition")
    public ResponseEntity<List<ComboDTO>> getAllConfigCharts(@RequestBody ReportKpiDTO reportKpiSearchDTO, String keyword) throws Exception {
        List<ComboDTO> result = new ArrayList<>();
        List<CompletableFuture<List<ComboDTO>>> lstFutureCombo = new ArrayList<>();

        if (!DataUtil.isNullOrEmpty(keyword)) {
            //Tim kiem doi tuong
            Map<String, Object> mapTable = new HashMap<>();
            catItemService.findCatItemByCategoryId(Constants.CATEGORY.DOMAIN_TABLE).stream().map(CatItemDTO::getItemValue).forEach(e -> {
                mapTable.put(e, new ArrayList<>());
            });
            reportKpiSearchDTO.setMapTable(mapTable);
            lstFutureCombo.add(kpiReportSearchService.findHashTag(keyword));

            //Tim kiem theo chi tieu
            lstFutureCombo.add(kpiReportSearchService.onSearchKpi(keyword));

            //Tim kiem theo doi tuong
            lstFutureCombo.add(kpiReportSearchService.findObject(reportKpiSearchDTO, keyword));
        }
        CompletableFuture.allOf(lstFutureCombo.toArray(new CompletableFuture[lstFutureCombo.size()])).join();
        for(CompletableFuture<List<ComboDTO>> future : lstFutureCombo) {
            List<ComboDTO> resultCombo = future.get();
            if(!DataUtil.isNullOrEmpty(resultCombo)) {
                result.addAll(resultCombo);
            }
        }
        return ResponseEntity.ok().body(result);
    }

}
