package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.FtpUtils;
import com.b4t.app.commons.KmeanCluster;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.BieumauKehoachchitieu;
import com.b4t.app.domain.ConfigChart;
import com.b4t.app.repository.BieumauKehoachchitieuRepository;
import com.b4t.app.repository.BuildChartRepository;
import com.b4t.app.repository.ChartMapRepository;
import com.b4t.app.repository.ConfigChartRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.ConfigChartMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildChartServiceImpl implements BuildChartService {

    public static final String CLASSIFY_COLOR_DEFAULT = "CLASSIFY_COLOR_DEFAULT";
    private final BuildChartRepository buildChartRepository;

    private final ConfigDisplayQueryService configDisplayQueryService;

    private final ConfigChartItemService configChartItemService;

    private final ConfigChartService configChartService;

    private final ConfigChartRepository configChartRepository;

    private final ConfigChartMapper configChartMapper;

    private final BieumauKehoachchitieuRepository bieumauKehoachchitieuRepository;
    private final FtpUtils fptUtils;
    @Autowired
    private ChartMapRepository chartMapRepository;
    @Autowired
    private KmeanCluster kmeanCluster;
    private final HttpServletRequest request;
    private final Environment env;
    @Autowired
    private CatItemService catItemService;

    private final Logger log = LoggerFactory.getLogger(BuildChartServiceImpl.class);

    public BuildChartServiceImpl(
        BuildChartRepository buildChartRepository,
        ConfigDisplayQueryService configDisplayQueryService,
        ConfigChartItemService configChartItemService,
        ConfigChartService configChartService,
        ConfigChartRepository configChartRepository,
        ConfigChartMapper configChartMapper,
        BieumauKehoachchitieuRepository bieumauKehoachchitieuRepository, FtpUtils fptUtils, HttpServletRequest request, Environment env) {
        this.buildChartRepository = buildChartRepository;
        this.configDisplayQueryService = configDisplayQueryService;
        this.configChartItemService = configChartItemService;
        this.configChartService = configChartService;
        this.configChartRepository = configChartRepository;
        this.configChartMapper = configChartMapper;
        this.bieumauKehoachchitieuRepository = bieumauKehoachchitieuRepository;
        this.fptUtils = fptUtils;
        this.request = request;
        this.env = env;
    }

    @Override
    public ChartResultDTO getChartResult(ConfigChartDTO chartDTO, List<ConfigChartItemDTO> chartItems, ChartParamDTO params) throws JsonProcessingException, ParseException {
        ChartResultDTO chartResult = buildChartRepository.getChartResult(chartDTO, chartItems, params);
        if (!chartDTO.getTypeChart().equals(Constants.OUTPUT_SEARCH.ICON_CHART)) {
            List<Long> kpiIds = chartResult.getDetails().stream()
                .map(d -> d.getKpiInfos().stream().map(CatGraphKpiDTO::getKpiId).collect(Collectors.toList()))
                .flatMap(List::stream).distinct().collect(Collectors.toList());
            List<BieumauKehoachchitieu> plans = bieumauKehoachchitieuRepository.findLastestPlan(kpiIds);
            chartResult.getDetails().forEach(d -> {
                Optional<BieumauKehoachchitieu> plan = plans.stream().filter(p -> p.getKpiId().equals(d.getKpiInfo().getKpiId())).findFirst();
                plan.ifPresent(p -> d.getKpiInfo().setPlan(p));
                d.getKpiInfos().forEach(k -> {
                    Optional<BieumauKehoachchitieu> plan2 = plans.stream().filter(p -> p.getKpiId().equals(k.getKpiId())).findFirst();
                    plan2.ifPresent(k::setPlan);
                });
            });
        }
        return chartResult;
    }

    @Override
    public List<Object> getDescriptionOfTable(String tableName) {
        return buildChartRepository.getDescriptionOfTable(tableName);
    }

    @Override
    public List<Map<String, Object>> getDescriptionOfTableToMap(String tableName) {
        return buildChartRepository.getDescriptionOfTableToMap(tableName);
    }

    /*public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
*/
    private void saveFile(String fileName, String path, MultipartFile multipartFile) throws IOException {
        String rootPath = request.getServletContext().getRealPath("/");
        if (!DataUtil.isNullOrEmpty(env.getProperty("filesystem.rootpath"))) {
            rootPath = env.getProperty("filesystem.rootpath");
        }
        Path uploadPath = Paths.get(rootPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

        /*if (!rootPath.endsWith("/") && !filePath.startsWith("/")) rootPath += "/";
        java.io.File file = new java.io.File(rootPath + filePath + multipartFile.getOriginalFilename().split());
        java.io.File folder = file.getParentFile();
        if (!folder.exists())
            folder.mkdirs();
        FileCopyUtils.copy(multipartFile.getBytes(), file);
        String uploadDir = "user-photos/" + savedUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        *//*try {
            fptUtils.uploadFile(fileName, filePath, multipartFile.getInputStream());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }*/

    }

    public File getFile(String fileName) {
        String rootPath = request.getServletContext().getRealPath("/");
        if (!DataUtil.isNullOrEmpty(env.getProperty("filesystem.rootpath"))) {
            rootPath = env.getProperty("filesystem.rootpath");
        }
        java.io.File fileOut = new File(rootPath + "/" + fileName);
        return fileOut;
    }

    @Override
    @Transactional(rollbackFor = {BadRequestAlertException.class, Exception.class})
    public ChartResultDTO saveChart(SaveChartDTO dto) throws IOException {
        if (dto.getId() != null) {
            configChartService.delete(dto.getId());
        }
        if (dto.getImage() != null) {
            String extension = FilenameUtils.getExtension(dto.getImage().getOriginalFilename());
            String fileName = com.b4t.app.commons.StringUtils.getSafeFileName(dto.getChartCode().toLowerCase()) + "." + extension;
//            saveFile(fileName, dto.getImage());
            saveFile(fileName, "icon_chart", dto.getImage());
            dto.setChartUrl(fileName);
        }
        ConfigChart configChart = configChartMapper.toEntity(dto);
        configChart.setStatus(Constants.STATUS_ACTIVE);
        configChart.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configChart.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configChart = configChartRepository.save(configChart);
        ConfigChartDTO configChartDTO = configChartMapper.toDto(configChart);
        dto.setId(configChartDTO.getId());
        if (Constants.MAP_CHART_TYPE.equals(dto.getTypeChart())) {
            ConfigChartItemDTO chartItem = new ConfigChartItemDTO();
            chartItem.setTypeChart(Constants.ITEM_MAP_CHART_TYPE);
            chartItem.setChartId(configChartDTO.getId());
            chartItem.setStatus(Constants.STATUS_ACTIVE);
            configChartItemService.save(chartItem);
            return new ChartResultDTO(dto);
        }

        boolean overview = Constants.OVERVIEW_DOMAIN_CODE.equals(dto.getDomainCode());
        Map<SaveChartItemDTO, List<SaveChartItemDTO>> mergedMap = mergeQueries(dto);
        for (List<SaveChartItemDTO> items : mergedMap.values()) {
            ConfigQueryChartDTO query = buildChartRepository.buildQuery(items, true, overview);
            items.forEach(i -> {
                try {
                    ConfigChartItemDTO chartItem = i.toDto();
                    chartItem.setQuery(query);
                    chartItem.setStatus(Constants.STATUS_ACTIVE);
                    List<ConfigDisplayQueryDTO> configDisplayQueryDtos = i.getColumns().stream()
                        .filter(c -> !DataUtil.isNullOrEmpty(c.getValues()) &&
                            c.getValues().stream().anyMatch(v -> StringUtils.isNotEmpty(v.getValue())))
                        .collect(Collectors.toList());

                    chartItem.setDisplayConfigs(configDisplayQueryDtos);
                    if (query != null) {
                        chartItem.setQueryId(query.getId());
                        chartItem.setChartId(dto.getId());
                        chartItem = configChartItemService.save(chartItem);
                        ConfigChartItemDTO finalChartItem = chartItem;
                        configDisplayQueryDtos = i.getColumns().stream()
                            .peek(c -> {
                                c.setItemChartId(finalChartItem.getId());
                                c.setStatus(Constants.STATUS_ACTIVE);
                            }).collect(Collectors.toList());
                        configDisplayQueryService.saveAll(configDisplayQueryDtos);

                    }
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }

        return new ChartResultDTO(dto);
    }

    private void saveFile(String fileName, MultipartFile multipartFile) throws IOException {

        try {
            fptUtils.uploadFile(fileName, multipartFile.getInputStream());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

    }

    @Override
    public ChartResultDTO preview(SaveChartDTO dto) throws JsonProcessingException, ParseException {
        Map<SaveChartItemDTO, List<SaveChartItemDTO>> mergedMap = mergeQueries(dto);
        List<ConfigChartItemDTO> allChartItems = new ArrayList<>();
        for (List<SaveChartItemDTO> items : mergedMap.values()) {
            ConfigQueryChartDTO query = buildChartRepository.buildQuery(items, false, Constants.OVERVIEW_DOMAIN_CODE.equals(dto.getDomainCode()));
            items.forEach(i -> {
                try {
                    ConfigChartItemDTO chartItem = i.toDto();
                    chartItem.setQuery(query);
                    chartItem.setStatus(Constants.STATUS_ACTIVE);
                    List<ConfigDisplayQueryDTO> configDisplayQueryDtos = i.getColumns().stream()
                        .filter(c -> !DataUtil.isNullOrEmpty(c.getValues()) &&
                            c.getValues().stream().anyMatch(v -> StringUtils.isNotEmpty(v.getValue())))
                        .collect(Collectors.toList());

                    chartItem.setDisplayConfigs(configDisplayQueryDtos);
                    allChartItems.add(chartItem);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }

        return getChartResult(dto, allChartItems, null);
    }

    @Override
    public SaveChartItemDTO generateInputCondition(SaveChartItemDTO item, ConfigQueryChartDTO query, List<SaveDisplayQueryDTO> columns) throws JsonProcessingException {
        return buildChartRepository.generateInputCondition(item, query, columns);
    }

    private Map<SaveChartItemDTO, List<SaveChartItemDTO>> mergeQueries(SaveChartDTO dto) {
        Map<SaveChartItemDTO, List<SaveChartItemDTO>> rs = new HashMap<>();
        if (DataUtil.isNullOrEmpty(dto.getItems())) return rs;
        long idx = 0;
        for (SaveChartItemDTO i : dto.getItems()) {
            i.setId(null);
            i.setOrderIndex(idx);
            idx++;
            if (StringUtils.isEmpty(i.getCustomizeSql())) {
                if (DataUtil.isNullOrEmpty(i.getKpiInfos())) continue;

                List<String> tableNames = i.getKpiInfos().stream().map(SaveKpiInfoDTO::getTableName).distinct().collect(Collectors.toList());
                if (tableNames.size() > 1) {
                    rs.put(i, new ArrayList<>(Collections.singletonList(i)));
                    continue;
                }
            }
            boolean existed = false;
            for (SaveChartItemDTO key : rs.keySet()) {
                if (key.canMerge(i)) {
                    existed = true;
                    List<SaveChartItemDTO> existedItems = rs.get(key);
                    existedItems.add(i);
                    break;
                }
            }
            if (!existed) {
                rs.put(i, new ArrayList<>(Collections.singletonList(i)));
            }
        }
        return rs;
    }

    public ChartMapDTO getChartMapsData(ChartMapParramDTO chartMapParramDTO) {
        List<DataChartMapDTO> results = chartMapRepository.getChartMapsData(chartMapParramDTO);
        if (DataUtil.isNullOrEmpty(results)) return new ChartMapDTO(null, null);
        List<Double> lstValue = results.stream().map(DataChartMapDTO::getValue).collect(Collectors.toList());

        Comparator<DataChartMapDTO> comparator = Comparator.comparing(DataChartMapDTO::getValue);
//        Comparator<DataChartMapDTO> comparator = (o1, o2) -> o2.getValue().compareTo(o1.getValue());
        Comparator<ClusterDTO> comparator1 = Comparator.comparing(o -> o.getMaxValue() + o.getMinValue());
        results = results.stream().sorted(comparator).collect(Collectors.toList());
        int numCluster = 5;
        List<CatItemDTO> lstCat = catItemService.getCatItemByCategoryCode(CLASSIFY_COLOR_DEFAULT);
        if (!DataUtil.isNullOrEmpty(lstCat)) {
            numCluster = DataUtil.safeToInt(lstCat.get(0).getItemValue()) + 1;
        }
        List<ClusterDTO> lstCuster = kmeanCluster.getCluster(lstValue, numCluster);
        lstCuster = lstCuster.stream().sorted(comparator1).collect(Collectors.toList());
//        lstCuster.sort(Comparator.comparing(ClusterDTO::getMaxValue));
        ChartMapDTO chartMapDTO = new ChartMapDTO(results, lstCuster);
        return chartMapDTO;
    }

    public Object getMaxTime(ChartMapParramDTO chartMapParramDTO) {
        Object result = chartMapRepository.getMaxTime(chartMapParramDTO);
        return result;
    }

    public List<RangeColorDTO> getRangeColor(RangeColorDTO rangeColorDTO) {
        return chartMapRepository.getRangeColor(rangeColorDTO);
    }

    public Long getScreenIdMap(Long profileId) {
        return chartMapRepository.getScreenIdMap(profileId);
    }


}
