package com.b4t.app.web.rest;

import com.b4t.app.service.CatItemService;
import com.b4t.app.service.ReportMappingService;
import com.b4t.app.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class ReportMapingResource {
    private final Logger log = LoggerFactory.getLogger(ReportMapingResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ReportMappingService reportMappingService;

    @Autowired
    CatItemService catItemService;

    @PostMapping("/report-mapping/kpi")
    public ResponseEntity<List<KpiReportMappingDTO>> findKpi(@RequestBody ReportMappingSearchDTO reportMappingSearchDTO) {
        List<KpiReportMappingDTO> result = reportMappingService.onSearchKpi(reportMappingSearchDTO.getKeyword());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/report-mapping/object")
    public ResponseEntity<List<ObjectReportDTO>> findObject(@RequestBody ReportMappingSearchDTO reportMappingSearchDTO) throws Exception {
        List<ObjectReportDTO> result = reportMappingService.findObject(reportMappingSearchDTO.getKeyword(), reportMappingSearchDTO.getKpiId(), reportMappingSearchDTO.getTableName());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/report-mapping/mapping")
    public ResponseEntity<List<Map<String, Object>>> getMappingData(@RequestBody ObjectMappingDTO[] objectMappingDTO) {
        List<Map<String, Object>> rsData = new ArrayList<>();
        for (ObjectMappingDTO object: objectMappingDTO) {
            Map<String, Object> rs = new HashMap<>();
            String result = reportMappingService.getMappingData(object.getColName(), object.getTable_name_dashboard(), object.getObjectDashboardId(), object.getPrdId(), object.getKpiDashboardId(), object.getTimeType());
            rs.put("result", result);
            rs.put("cellName", object.getCellName());
            rs.put("prdId", object.getPrdId());
            rsData.add(rs);
        }
        return ResponseEntity.ok().body(rsData);
    }
}
