package com.b4t.app.web.rest;

import com.b4t.app.commons.ExcelUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.ConfigMapKpiIdMaLv;
import com.b4t.app.service.ConfigMapKpiIdMaLvService;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.ConfigMapKpiIdMaLvDTO;
import com.b4t.app.service.dto.ConfigMapKpiQueryDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ConfigMapKpiIdMaLvResource {
    public static final String REST_REQUEST_TO_GET_A_PAGE_OF_CAT_GRAPH_KPIS = "REST request to get a page of CatGraphKpis";
    private final Logger log = LoggerFactory.getLogger(ConfigMapKpiIdMaLvResource.class);

    private static final String ENTITY_NAME = "configKpiIdMaLv";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    private final ConfigMapKpiIdMaLvService configMapKpiIdMaLvService;

    @Autowired
    ExcelUtils excelUtils;


    public ConfigMapKpiIdMaLvResource(ConfigMapKpiIdMaLvService configKpiIdMaLvService) {
        this.configMapKpiIdMaLvService = configKpiIdMaLvService;
    }
    @GetMapping("/config-map-kpi-id-ma-lv/{id}")
    public ResponseEntity<ConfigMapKpiIdMaLvDTO> getConfigMapKpiIdMaLv(@PathVariable Long id) {
        log.debug("REST request to get CatGraphKpi : {}", id);
        Optional<ConfigMapKpiIdMaLvDTO> configMapKpiIdMaLvDTO = configMapKpiIdMaLvService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapKpiIdMaLvDTO);
    }

    @GetMapping("/config-map-kpi-id-ma-lv-by-kpi-id/{id}")
    public ResponseEntity<List<ConfigMapKpiIdMaLvDTO>> getConfigMapKpiIdMaLvByKpiId(@PathVariable Long id) {
        log.debug("REST request to get CatGraphKpi : {}", id);
        List<ConfigMapKpiIdMaLvDTO> configMapKpiIdMaLvDTO = configMapKpiIdMaLvService.findByKpiId(id);
        HttpHeaders headers = HeaderUtil.createAlert(ENTITY_NAME, "object", "Query ok");
        return ResponseEntity.ok().body(configMapKpiIdMaLvDTO);
    }

    @PostMapping("/config-map-kpi-id-ma-lv")
    public ResponseEntity<ConfigMapKpiIdMaLvDTO> createConfigMapKpiIdMaLv(@RequestBody ConfigMapKpiIdMaLvDTO configMapKpiIdMaLvDTO) throws URISyntaxException {
        ConfigMapKpiIdMaLvDTO result = configMapKpiIdMaLvService.save(configMapKpiIdMaLvDTO);
        return null;
    }

    @PutMapping("/config-map-kpi-id-ma-lv")
    public ResponseEntity<ConfigMapKpiIdMaLvDTO> updateConfigMapKpiIdMaLv(@RequestBody ConfigMapKpiIdMaLvDTO configMapKpiIdMaLvDTO) throws URISyntaxException {
        ConfigMapKpiIdMaLvDTO result = configMapKpiIdMaLvService.save(configMapKpiIdMaLvDTO);
        return ResponseEntity.created(new URI("/api/config-map-kpi-id-ma-lv-by-kpi-id/" + result.getKpiId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, String.valueOf(result.getId())))
            .body(result);
    }
}
