package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatGraphKpi;
import com.b4t.app.domain.CatItem;
import com.b4t.app.domain.ConfigChart_;
import com.b4t.app.repository.CatGraphKpiRepository;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.ConfigChart}.
 */
@RestController
@RequestMapping("/api")
public class ConfigChartResource {

    private final Logger log = LoggerFactory.getLogger(ConfigChartResource.class);

    private static final String ENTITY_NAME = "configChart";
    private static final String VALIDATE_ITEM_NULL = "error.configchart.itemnull";
    private static final String VALIDATE_NO_EXISTED = "error.isnotexisted";
    private static final String VALIDATE_OVERVIEW_CHART = "error.configchart.overviewerror";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigChartService configChartService;
    private final ConfigChartItemService configChartItemService;
    private final BuildChartService buildChartService;
    private final ConfigQueryChartService configQueryChartService;
    private final ConfigDisplayQueryService configDisplayQueryService;
    private final CatGraphKpiRepository catGraphKpiRepository;
    private final CatItemRepository catItemRepository;
    public ConfigChartResource(
        ConfigChartService configChartService,
        ConfigChartItemService configChartItemService,
        BuildChartService buildChartService,
        ConfigQueryChartService configQueryChartService,
        ConfigDisplayQueryService configDisplayQueryService, CatGraphKpiRepository catGraphKpiRepository, CatItemRepository catItemRepository) {
        this.configChartService = configChartService;
        this.configChartItemService = configChartItemService;
        this.buildChartService = buildChartService;
        this.configQueryChartService = configQueryChartService;
        this.configDisplayQueryService = configDisplayQueryService;
        this.catGraphKpiRepository = catGraphKpiRepository;
        this.catItemRepository = catItemRepository;
    }

    /**
     * {@code POST  /config-charts} : Create a new configChart.
     *
     * @param configChartDTO the configChartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configChartDTO, or with status {@code 400 (Bad Request)} if the configChart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/config-charts")
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<ConfigChartDTO> createConfigChart(@RequestBody SaveChartDTO configChartDTO) throws Exception {
        return this.createChartConfig(configChartDTO);
    }
    @PostMapping(value = "/config-charts-icon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<ConfigChartDTO> createConfigChartIcon(@ModelAttribute SaveChartDTO configChartDTO) throws Exception {
        return this.createChartConfig(configChartDTO);
    }
    private ResponseEntity<ConfigChartDTO> createChartConfig(SaveChartDTO configChartDTO) throws Exception {
        log.debug("REST request to save ConfigChart : {}", configChartDTO);
        if (configChartDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        if (Constants.MAP_CHART_TYPE.equals(configChartDTO.getTypeChart())) {
            ConfigChartDTO result = buildChartService.saveChart(configChartDTO);
            return ResponseEntity.created(new URI("/api/config-charts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, String.valueOf(result.getId())))
                .body(result);
        }
        if (!configChartDTO.getTypeChart().equals(Constants.OUTPUT_SEARCH.ICON_CHART) && DataUtil.isNullOrEmpty(configChartDTO.getItems())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_ITEM_NULL), ENTITY_NAME, VALIDATE_ITEM_NULL);
        }
        if (Constants.OVERVIEW_DOMAIN_CODE.equals(configChartDTO.getDomainCode())) {
            List<String> tableNames = configChartDTO.getItems().stream()
                .map(i -> i.getKpiInfos().stream().map(SaveKpiInfoDTO::getTableName).collect(Collectors.toList()))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

            if (tableNames.size() > 1) {
                throw new BadRequestAlertException(Translator.toLocale(VALIDATE_OVERVIEW_CHART), ENTITY_NAME, VALIDATE_OVERVIEW_CHART);
            }
        }
        if (!configChartDTO.getTypeChart().equals(Constants.OUTPUT_SEARCH.ICON_CHART)) {
            for (int i = 0; i < configChartDTO.getItems().size(); i++) {
                SaveChartItemDTO item = configChartDTO.getItems().get(i);
                if (DataUtil.isNullOrEmpty(item.getColumns())) {
                    throw new BadRequestAlertException(Translator.toLocale("error.configchart.columnnull"), ENTITY_NAME, "error.configchart.columnnull");
                }
                if (item.getColumns().stream().anyMatch(c -> Constants.COMMON_YES.equals(c.getIsRequire()) &&
                    (DataUtil.isNullOrEmpty(c.getValues()) || c.getValues().stream().allMatch(v -> StringUtils.isEmpty(v.getValue()))))) {
                    throw new BadRequestAlertException(Translator.toLocale("error.configchart.columnvaluerequired", new Object[]{i + 1}), ENTITY_NAME, "error.configchart.columnvaluerequired");
                }
            }
        }
        configChartDTO.setStatus(Constants.STATUS_ACTIVE);
        ConfigChartDTO result = buildChartService.saveChart(configChartDTO);
        return ResponseEntity.created(new URI("/api/config-charts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, String.valueOf(result.getId())))
            .body(result);
    }

    /**
     * {@code PUT  /config-charts} : Updates an existing configChart.
     *
     * @param configChartDTO the configChartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configChartDTO,
     * or with status {@code 400 (Bad Request)} if the configChartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configChartDTO couldn't be updated.
     */
    @PostMapping(value = "/config-charts-update")
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<ConfigChartDTO> updateConfigChart(@RequestBody SaveChartDTO configChartDTO) throws Exception {
        return this.updateChartConfig(configChartDTO);
    }
    @PostMapping(value = "/config-charts-update-icon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<ConfigChartDTO> updateConfigChartIcon(@ModelAttribute SaveChartDTO configChartDTO) throws Exception {
        return this.updateChartConfig(configChartDTO);
    }

    private ResponseEntity<ConfigChartDTO> updateChartConfig(SaveChartDTO configChartDTO) throws Exception {
        log.debug("REST request to update ConfigChart : {}", configChartDTO);
        if (configChartDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ConfigChart_.ID, "error.idnull");
        }
        Optional<ConfigChartDTO> ckOpt = configChartService.findOne(configChartDTO.getId());
        if (ckOpt.isPresent() && !ckOpt.get().getUpdateTime().equals(configChartDTO.getUpdateTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notlastestdata"), ENTITY_NAME, "error.notlastestdata");
        }
        if (!configChartDTO.getTypeChart().equals(Constants.OUTPUT_SEARCH.ICON_CHART)) {
            if (DataUtil.isNullOrEmpty(configChartDTO.getItems())) {
                throw new BadRequestAlertException(Translator.toLocale(VALIDATE_ITEM_NULL), ENTITY_NAME, VALIDATE_ITEM_NULL);
            }
        }
        if (Constants.OVERVIEW_DOMAIN_CODE.equals(configChartDTO.getDomainCode())) {
            List<String> tableNames = configChartDTO.getItems().stream()
                .map(i -> i.getKpiInfos().stream().map(SaveKpiInfoDTO::getTableName).collect(Collectors.toList()))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

            if (tableNames.size() > 1) {
                throw new BadRequestAlertException(Translator.toLocale(VALIDATE_OVERVIEW_CHART), ENTITY_NAME, VALIDATE_OVERVIEW_CHART);
            }
        }
        configChartDTO.setStatus(Constants.STATUS_ACTIVE);

        ConfigChartDTO result = buildChartService.saveChart(configChartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configChartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /config-charts} : get all the configCharts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configCharts in body.
     */
    @GetMapping("/config-charts/get-all")
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<List<ConfigChartDTO>> getAllConfigCharts(String keyword, String typeChart,
                                                                   Long[] groupChartIds, String[] domainCodes, String[] groupKpiCodes, Integer timeTypeDefault,
                                                                   Long status, Integer childChart, Long[] listChartNotIn, Long[] listChartIn,  Pageable pageable) {
        log.debug("REST request to get a page of ConfigCharts");
        Page<ConfigChartDTO> page = configChartService.findAll(keyword, typeChart, groupChartIds, domainCodes, groupKpiCodes,
            timeTypeDefault, status, childChart, listChartNotIn, listChartIn, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/config-charts/get-all-assign")
    public ResponseEntity<List<ConfigChartDTO>> getAllConfigCharts(Long[] listChartIn,  Pageable pageable) {
        log.debug("REST request to get a page of ConfigCharts");
        List<ConfigChartDTO> lstChart = new ArrayList<>();
        if (listChartIn != null && listChartIn.length > 0) {
            lstChart = configChartService.findByIds(Arrays.asList(listChartIn));
        }
        return ResponseEntity.ok().body(lstChart);
    }

    @GetMapping("/config-charts/get-all-by-ids")
    public ResponseEntity<List<ConfigChartDTO>> getAllConfigChartByIds(@RequestParam(value = "chartIds") List<Long> chartIds) {
        List<ConfigChartDTO> results = configChartService.findByIds(chartIds);
        return ResponseEntity.ok().body(results);
    }

    /**
     * {@code GET  /config-charts/:id} : get the "id" configChart.
     *
     * @param id the id of the configChartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configChartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/config-charts/{id}")
    public ResponseEntity<SaveChartDTO> getConfigChart(@PathVariable Long id) {
        log.debug("REST request to get ConfigChart : {}", id);
        Optional<ConfigChartDTO> configChartDTO = configChartService.findOne(id);
        if (!configChartDTO.isPresent() || Constants.STATUS_DISABLED.equals(configChartDTO.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_NO_EXISTED), ENTITY_NAME, VALIDATE_NO_EXISTED);
        }
        SaveChartDTO rs = new SaveChartDTO(configChartDTO.get());
        if (rs.getChartIdNextto() != null) {
            Optional<ConfigChartDTO> chartNextTo = configChartService.findOne(rs.getChartIdNextto());
            if (chartNextTo.isPresent() && !Constants.STATUS_DISABLED.equals(chartNextTo.get().getStatus())) {
                rs.setChartNextto(chartNextTo.get());
            } else {
                rs.setChartIdNextto(null);
            }
        }
        //get items
        List<ConfigChartItemDTO> chartItems = configChartItemService.findByChartId(rs.getId());
        if (!DataUtil.isNullOrEmpty(chartItems)) {
            List<Long> itemIds = chartItems.stream().map(ConfigChartItemDTO::getId).collect(Collectors.toList());
            List<Long> queryIds = chartItems.stream().map(ConfigChartItemDTO::getQueryId).filter(Objects::nonNull).collect(Collectors.toList());
            List<ConfigQueryChartDTO> queries = new ArrayList<>();
            if (!DataUtil.isNullOrEmpty(queryIds))
                queries = configQueryChartService.findByIds(queryIds);
            List<ConfigDisplayQueryDTO> displayConfigs = configDisplayQueryService.findByChartItemIds(itemIds);
            List<ConfigQueryChartDTO> finalQueries = queries;
            List<SaveChartItemDTO> items = chartItems.stream()
                .map(i -> {
                    try {

                        String inputCondition = i.getInputCondition();
                        SaveChartItemDTO chartDTO;
                        String json;
                        if(inputCondition != null && !inputCondition.isEmpty()){
                            Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new JsonDeserializer() {
                                @Override
                                public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                                    throws JsonParseException {
                                    return Instant.parse(json.getAsString());
                                }
                            }).create();
                            chartDTO = gson.fromJson(inputCondition, SaveChartItemDTO.class);
                            for (SaveKpiInfoDTO kpiInfo : chartDTO.getKpiInfos()) {
                                for (CatGraphKpiDTO kpi : kpiInfo.getKpis()) {
                                    Optional<CatGraphKpi> catGraphKpi = catGraphKpiRepository.findFirstByKpiIdAndStatus(kpi.getKpiId(), 1l);
                                    CatItem catItem = catItemRepository.findDomainByGroupKpiCode(catGraphKpi.get().getGroupKpiCode());
                                    kpi.setDomainName(catItem.getItemName());
                                    kpi.setDomainCode(catItem.getItemCode());
                                }
                            }
                            json = gson.toJson(chartDTO);
                            i.setInputCondition(json);
                        }
                        SaveChartItemDTO saveItem = new SaveChartItemDTO(i);
                        Optional<ConfigQueryChartDTO> query = finalQueries.stream().filter(q -> q.getId().equals(i.getQueryId())).findFirst();
                        if (StringUtils.isNotEmpty(i.getInputCondition())) {
                            if (query.isPresent()) saveItem.setQuery(query.get());
                            return saveItem;
                        }
                        if (!query.isPresent()) return saveItem;
                        List<SaveDisplayQueryDTO> columns = displayConfigs.stream().filter(c -> i.getId().equals(c.getItemChartId())).map(SaveDisplayQueryDTO::new).collect(Collectors.toList());
                        saveItem = buildChartService.generateInputCondition(saveItem, query.get(), columns);
                        saveItem.setQuery(query.get());
                        return saveItem;
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            rs.setItems(items);
        }

        return ResponseEntity.ok().body(rs);
    }

    @GetMapping("/config-charts/view-chart-icon/{name}")
    public ResponseEntity<Resource> viewImage(@PathVariable String name) throws Exception {
        File fileOut = buildChartService.getFile(name);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileOut));
        return ResponseEntity.ok()
            .contentLength(fileOut.length())
            .header("filename", fileOut.getName())
            .contentType(MediaType.parseMediaType("image/jpeg"))
            .body(resource);
    }

    /**
     * {@code DELETE  /config-charts/:id} : delete the "id" configChart.
     *
     * @param id the id of the configChartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-charts/{id}")
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<Void> deleteConfigChart(@PathVariable Long id) {
        log.debug("REST request to delete ConfigChart : {}", id);
        Optional<ConfigChartDTO> configChartDTO = configChartService.findOne(id);
        if (!configChartDTO.isPresent() || Constants.STATUS_DISABLED.equals(configChartDTO.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_NO_EXISTED), ENTITY_NAME, VALIDATE_NO_EXISTED);
        }
        configChartService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/config-charts/delete-all")
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<Void> deleteConfigChart(@RequestBody Long[] ids) {
        log.debug("REST request to delete ConfigChart : {}", ids);
        if (DataUtil.isNullOrEmpty(ids))
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        List<ConfigChartDTO> charts = configChartService.findByIds(Arrays.asList(ids));
        if (DataUtil.isNullOrEmpty(charts) || charts.stream().anyMatch(c -> Constants.STATUS_DISABLED.equals(c.getStatus()))) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_NO_EXISTED), ENTITY_NAME, VALIDATE_NO_EXISTED);
        }
        configChartService.delete(charts);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, ids.toString())).build();
    }

    @GetMapping("/config-charts/get-chart-detail/{chartId}")
    public ResponseEntity<List<ConfigChartDetailDTO>> findByChartId(@PathVariable Long chartId) {
        List<ConfigChartDetailDTO> lstConfig = configChartService.findByChartId(chartId);
        return ResponseEntity.ok().body(lstConfig);
    }

    @PostMapping("/config-charts/preview")
    @Transactional(noRollbackFor = Exception.class)
    public ResponseEntity<ChartResultDTO> previewConfigChart(@RequestBody SaveChartDTO configChartDTO) throws JsonProcessingException, ParseException {
        log.debug("REST request to save ConfigChart : {}", configChartDTO);
        if (DataUtil.isNullOrEmpty(configChartDTO.getItems())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_ITEM_NULL), ENTITY_NAME, VALIDATE_ITEM_NULL);
        }
        if (Constants.MAP_CHART_TYPE.equals(configChartDTO.getTypeChart())) {
            return ResponseEntity.ok().body(new ChartResultDTO(configChartDTO));
        }

        if (Constants.OVERVIEW_DOMAIN_CODE.equals(configChartDTO.getDomainCode())) {
            List<String> tableNames = configChartDTO.getItems().stream()
                .map(i -> i.getKpiInfos().stream().map(SaveKpiInfoDTO::getTableName).collect(Collectors.toList()))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

            if (tableNames.size() > 1) {
                throw new BadRequestAlertException(Translator.toLocale(VALIDATE_OVERVIEW_CHART), ENTITY_NAME, VALIDATE_OVERVIEW_CHART);
            }
        }
        configChartDTO.setStatus(Constants.STATUS_ACTIVE);
        ChartResultDTO result = buildChartService.preview(configChartDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/config-charts/clone/{id}")
    @PreAuthorize("hasPermission('CHART', '*')")
    public ResponseEntity<SaveChartDTO> cloneConfigChart(@PathVariable Long id) {
        log.debug("REST request to copy ConfigChart : {}", id);
        Optional<ConfigChartDTO> ckOpt = configChartService.findOne(id);
        if (!ckOpt.isPresent() || Constants.STATUS_DISABLED.equals(ckOpt.get().getStatus())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_NO_EXISTED), ENTITY_NAME, "isnotexisted");
        }
        SaveChartDTO rs = configChartService.clone(id);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(rs);

    }

    @PostMapping("/config-charts/search-chart")
    public ResponseEntity<List<ComboDTO>> onSearchChart(Long profileId, String keyword) throws Exception {
        List<ComboDTO> result = configChartService.onSearchChart(profileId, keyword);
        return ResponseEntity.ok().body(result);
    }

}
