package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DateUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigQueryKpiService;
import com.b4t.app.service.MonitorQueryKpiService;
import com.b4t.app.service.RpReportService;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigQueryKpi}.
 */
@Service
@Transactional
public class ConfigQueryKpiServiceImpl implements ConfigQueryKpiService {

    private final Logger log = LoggerFactory.getLogger(ConfigQueryKpiServiceImpl.class);

    private final ConfigQueryKpiRepository configQueryKpiRepository;

    private final ConfigQueryKpiMapper configQueryKpiMapper;
    private final MonitorQueryKpiService monitorQueryKpiService;
    private final RpReportService rpReportService;

    private static final String ENTITY_NAME = "configQueryKpi";


    @Autowired
    ConfigInputTableQueryKpiRepository configInputTableQueryKpiRepository;

    @Autowired
    ConfigInputKpiQueryRepository configInputKpiQueryRepository;

    @Autowired
    ConfigQueryKpiResultMapper configQueryKpiResultMapper;

    @Autowired
    ConfigInputTableQueryKpiMapper configInputTableQueryKpiMapper;

    @Autowired
    ConfigInputKpiQueryMapper configInputKpiQueryMapper;

    @Autowired
    ConfigColumnQueryKpiMapper configColumnQueryKpiMapper;

    @Autowired
    ConfigMapKpiQueryMapper configMapKpiQueryMapper;

    @Autowired
    ConfigMapKpiQueryRepository configMapKpiQueryRepository;

    @Autowired
    ConfigColumnQueryKpiRepository configColumnQueryKpiRepository;

    @Autowired
    MonitorQueryKpiRepository monitorQueryKpiRepository;

    @Autowired
    CustomRepository customRepository;

    public ConfigQueryKpiServiceImpl(ConfigQueryKpiRepository configQueryKpiRepository, ConfigQueryKpiMapper configQueryKpiMapper, MonitorQueryKpiService monitorQueryKpiService, RpReportService rpReportService) {
        this.configQueryKpiRepository = configQueryKpiRepository;
        this.configQueryKpiMapper = configQueryKpiMapper;
        this.monitorQueryKpiService = monitorQueryKpiService;
        this.rpReportService = rpReportService;
    }

    /**
     * Save a configQueryKpi.
     *
     * @param configQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigQueryKpiDTO save(ConfigQueryKpiDTO configQueryKpiDTO) {
        log.debug("Request to save ConfigQueryKpi : {}", configQueryKpiDTO);
        ConfigQueryKpi configQueryKpi = configQueryKpiMapper.toEntity(configQueryKpiDTO);
        configQueryKpi = configQueryKpiRepository.save(configQueryKpi);
        return configQueryKpiMapper.toDto(configQueryKpi);
    }

    /**
     * Tao moi config_query_kpi
     *
     * @param configQueryKpiDTO
     * @param actor
     * @param status
     * @param currentDate
     * @return
     * @throws Exception
     */
    private ConfigQueryKpi addConfigQueryKpi(ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor, Integer status, Instant currentDate) throws Exception {
        ConfigQueryKpi configQueryKpi = new ConfigQueryKpi();
        this.updateConfigQueryKpiData(configQueryKpi, configQueryKpiDTO, actor, status, currentDate);
        configQueryKpi = configQueryKpiRepository.save(configQueryKpi);
        return configQueryKpi;
    }

    private ConfigQueryKpi updateConfigQueryKpi(ConfigQueryKpi configQueryKpi, ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor, Integer status, Instant currentDate) throws Exception {

        this.updateConfigQueryKpiData(configQueryKpi, configQueryKpiDTO, actor, status, currentDate);
        configQueryKpi = configQueryKpiRepository.save(configQueryKpi);
        return configQueryKpi;
    }

    /**
     * Update cac truong thong tin cua configQueryKpi
     *
     * @param configQueryKpi
     * @param configQueryKpiDTO
     * @param actor
     * @param status
     * @param currentDate
     */
    private void updateConfigQueryKpiData(ConfigQueryKpi configQueryKpi, ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor, Integer status, Instant currentDate) {
        configQueryKpi.setTimeType(Integer.valueOf(configQueryKpiDTO.getConfigQueryKpi().getTimeType()));
        configQueryKpi.setInputLevel(Long.valueOf(configQueryKpiDTO.getConfigQueryKpi().getInputLevel()));

        if(!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryKpi().getListParentInputLevel())) {
            configQueryKpi.setListParentInputLevel(String.join(",", configQueryKpiDTO.getConfigQueryKpi().getListParentInputLevel()));
        } else {
            configQueryKpi.setListParentInputLevel("");
        }
        configQueryKpi.setQueryData(configQueryKpiDTO.getConfigQueryKpi().getQueryData());
        configQueryKpi.setQueryCheckData(configQueryKpiDTO.getConfigQueryKpi().getQueryCheckData());

        configQueryKpi.setStatus(status);
        configQueryKpi.setDescription(configQueryKpiDTO.getConfigQueryKpi().getDescription());
        configQueryKpi.setReportId(configQueryKpiDTO.getConfigQueryKpi().getReportId());
        configQueryKpi.setUpdateTime(currentDate);
        configQueryKpi.setUpdateUser(actor);
    }

    private Integer getConfigStatus(ConfigQueryKpiDetailDTO configQueryKpiDTO) {
        Integer status;
        if (configQueryKpiDTO.getConfigQueryKpi().isStatus()) {
            status = Constants.STATUS_ACTIVE.intValue();
        } else {
            status = Constants.STATUS_DISABLED.intValue();
        }
        return status;
    }

    /**
     * Tạo mới tổng hợp chỉ tiêu + tạo mới tiến trình tổng hợp chỉ tiêu
     * @param configQueryKpiDTO
     * @param actor
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigQueryKpiDetailDTO save(ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor) throws Exception {
        Integer status = getConfigStatus(configQueryKpiDTO);
        Instant currentDate = Instant.now();

        // Insert config_query_kpi
        ConfigQueryKpi configQueryKpi = addConfigQueryKpi(configQueryKpiDTO, actor, status, currentDate);

        Integer queryId = configQueryKpi.getId().intValue();
        configQueryKpiDTO.getConfigQueryKpi().setId(configQueryKpi.getId());

        // Insert config_input_table_query_kpi
        if(!configQueryKpiDTO.getConfigQueryKpi().getTableSource().contains(Constants.DATA_TABLE.TABLE_KEHOACHCHITIEU)
            && !configQueryKpiDTO.getConfigQueryKpi().getTableSource().contains(Constants.DATA_TABLE.TABLE_KEHOACHCHITIEU.toLowerCase())) {
            configQueryKpiDTO.getConfigQueryKpi().getTableSource().add(Constants.DATA_TABLE.TABLE_KEHOACHCHITIEU);
        }
        if (!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryKpi().getTableSource())) {
            List<ConfigInputTableQueryKpi> lstConfigInputTableQueryKpi = configQueryKpiDTO.getConfigQueryKpi().getTableSource().stream().
                map(e -> {
                    ConfigInputTableQueryKpi configInputTableQueryKpi = new ConfigInputTableQueryKpi();
                    configInputTableQueryKpi.setQueryKpiId(queryId);
                    configInputTableQueryKpi.setTableSource(e);
                    configInputTableQueryKpi.setStatus(status);
                    configInputTableQueryKpi.setUpdateTime(currentDate);
                    return configInputTableQueryKpi;
                }).collect(Collectors.toList());
            configInputTableQueryKpiRepository.saveAll(lstConfigInputTableQueryKpi);
        }

        // Insert config_input_kpi_query
        if (!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryKpi().getInputKpi())) {
            List<ConfigInputKpiQuery> lstConfigInputKpiQuery = configQueryKpiDTO.getConfigQueryKpi().getInputKpi().stream().map(e -> {
                ConfigInputKpiQuery configInputKpiQuery = new ConfigInputKpiQuery();
                configInputKpiQuery.setQueryKpiId(queryId);
                configInputKpiQuery.setKpiId(String.valueOf(e));
                configInputKpiQuery.setStatus(status);
                configInputKpiQuery.setUpdateTime(currentDate);
                return configInputKpiQuery;
            }).collect(Collectors.toList());
            configInputKpiQueryRepository.saveAll(lstConfigInputKpiQuery);
        }

        // Insert config_map_kpi_query, config_column_query_kpi
        if (!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryColum())) {
            Map<Integer, Integer> mapKpiStatus = new HashMap<>();
            configQueryKpiDTO.getConfigQueryColum().stream()
                .collect(Collectors.groupingBy(ConfigQueryKpiColumn::getKpi, Collectors.toList())).forEach((key, value) -> {
                Integer statusValue = value.stream().max(Comparator.comparing(ConfigQueryKpiColumn::getStatus)).map(ConfigQueryKpiColumn::getStatus).get();
                mapKpiStatus.put(key, statusValue);
            });

            Map<Integer, Long> mapKpiId = new HashMap<>();
            mapKpiStatus.forEach((kpi, statusKpi) -> {
                ConfigMapKpiQuery configMapKpiQuery = new ConfigMapKpiQuery();
                configMapKpiQuery.setQueryKpiId(queryId);
                configMapKpiQuery.setKpiId(kpi);
                configMapKpiQuery.setTableDestination(configQueryKpiDTO.getConfigQueryKpi().getTableDestination());
                configMapKpiQuery.setStatus(statusKpi);
                configMapKpiQuery.setUpdateTime(currentDate);
                configMapKpiQuery.setUpdateUser(actor);
                configMapKpiQuery = configMapKpiQueryRepository.save(configMapKpiQuery);
                mapKpiId.put(kpi, configMapKpiQuery.getId());
            });

            List<ConfigColumnQueryKpi> lstColumn = configQueryKpiDTO.getConfigQueryColum()
                .stream()
                .map(e -> new ConfigColumnQueryKpi(
                mapKpiId.get(e.getKpi()),
                e.getAliasQuery(),
                e.getDataType(),
                e.getColumnInDestinationTable(),
                e.getStatus(),
                currentDate,
                actor))
                .collect(Collectors.toList());

            configColumnQueryKpiRepository.saveAll(lstColumn);

        }

        // Insert du lieu vao bang monitor_query_kpi
        MonitorQueryKpi monitorQueryKpi = new MonitorQueryKpi();
        monitorQueryKpi.setQueryKpiId(queryId);
        monitorQueryKpi.setStatus(0);
        monitorQueryKpi.setUpdateTime(currentDate);
        Date firstDateOfMonth = DateUtil.getFirstDateOfMonth(new Date());
        monitorQueryKpi.setPriority(0);
        monitorQueryKpi.setRunTimeSucc(firstDateOfMonth.toInstant());
        monitorQueryKpiRepository.save(monitorQueryKpi);

        return configQueryKpiDTO;
    }

    /**
     * Update config_query_kpi
     *
     * @param configQueryKpiDTO
     * @param actor
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigQueryKpiDetailDTO update(ConfigQueryKpiDetailDTO configQueryKpiDTO, String actor) throws Exception {
        if(!configQueryKpiDTO.getConfigQueryKpi().getTableSource().contains(Constants.DATA_TABLE.TABLE_KEHOACHCHITIEU)
            && !configQueryKpiDTO.getConfigQueryKpi().getTableSource().contains(Constants.DATA_TABLE.TABLE_KEHOACHCHITIEU.toLowerCase())) {
            configQueryKpiDTO.getConfigQueryKpi().getTableSource().add(Constants.DATA_TABLE.TABLE_KEHOACHCHITIEU);
        }
        Integer status = getConfigStatus(configQueryKpiDTO);
        Instant currentDate = Instant.now();
        ConfigQueryKpi configQueryKpi = configQueryKpiRepository.getOne(configQueryKpiDTO.getConfigQueryKpi().getId());
        Long reportId = configQueryKpi.getReportId();
        this.updateConfigQueryKpi(configQueryKpi, configQueryKpiDTO, actor, status, currentDate);
        Integer queryId = configQueryKpiDTO.getConfigQueryKpi().getId().intValue();
        //Cap nhat du lieu bang nguon
        this.updateConfigInputTableQueryKpi(configQueryKpiDTO, queryId, status, currentDate);

        // Cap nhat config_input_kpi_query
        this.updateConfigInputKpiQuery(configQueryKpiDTO, queryId, status, currentDate);

        // cap nhat config_map_kpi_query, config_column_query_kpi
        if (!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryColum())) {
            Map<Integer, Integer> mapKpiStatus = new HashMap<>();
            configQueryKpiDTO.getConfigQueryColum().stream()
                .collect(Collectors.groupingBy(ConfigQueryKpiColumn::getKpi, Collectors.toList())).forEach((key, value) -> {
                Integer statusValue = value.stream().max(Comparator.comparing(ConfigQueryKpiColumn::getStatus)).map(ConfigQueryKpiColumn::getStatus).get();
                mapKpiStatus.put(key, statusValue);
            });

            List<ConfigColumnQueryKpiDetail> lstCurrentColumns = this.findAllConfigColumnQueryKpiByQueryId(queryId);
            Set<String> lstCurrentCol = lstCurrentColumns.stream().map(e -> e.getKpiId() + "_" + e.getColumnDestination()).collect(Collectors.toSet());


            //Lay cac KPI da duoc cau hinh
            Set<Integer> kpis = configQueryKpiDTO.getConfigQueryColum().stream().map(ConfigQueryKpiColumn::getKpi).collect(Collectors.toSet());
            List<ConfigMapKpiQuery> lstConfigKpi = configMapKpiQueryRepository.findAllByQueryKpiId(queryId);
            Set<Integer> currentKpi = lstConfigKpi.stream().map(ConfigMapKpiQuery::getKpiId).collect(Collectors.toSet());

            Set<Integer> kpiInsert = kpis.stream().filter(e -> currentKpi == null || !currentKpi.contains(e)).collect(Collectors.toSet());
            List<ConfigMapKpiQuery> kpiUpdate = lstConfigKpi.stream().filter(e -> kpis != null && kpis.contains(e.getKpiId())).collect(Collectors.toList());
            List<ConfigMapKpiQuery> kpiDelete = lstConfigKpi.stream().filter(e -> !kpis.contains(e.getKpiId())).collect(Collectors.toList());

            // Update ConfigMapKpiQuery
            Map<Integer, Long> mapKpiId = this.updateConfigMapKpiQuery(queryId, configQueryKpiDTO, kpiInsert, kpiUpdate, kpiDelete, actor, status, currentDate, mapKpiStatus);

            //update ConfigColumnQueryKpi
            this.updateConfigQueryKpiColumn(configQueryKpiDTO, mapKpiId, lstCurrentCol, lstCurrentColumns, actor, status, currentDate);

            if(configQueryKpiDTO.getConfigQueryKpi().getReportId() != null) {
                Optional<RpReportDTO> reportDTO =  rpReportService.findOne(configQueryKpiDTO.getConfigQueryKpi().getReportId());
                monitorQueryKpiService.updateRunTimeReport(queryId, reportDTO.get().getPrdId(), reportDTO.get().getTimeType());
            } else if (reportId != null) {
                //Truong hop DL cu co reportId, du lieu moi ko cau hinh reportId thi xoa runTimeReport trong bang monitor
                monitorQueryKpiService.clearRunTimeReport(queryId);
            }
        }
        return configQueryKpiDTO;
    }

    private void updateConfigQueryKpiColumn(ConfigQueryKpiDetailDTO configQueryKpiDTO, Map<Integer, Long> mapKpiId, Set<String> lstCurrentCol, List<ConfigColumnQueryKpiDetail> lstCurrentColumns,
                                            String actor, Integer status, Instant currentDate) {
        List<ConfigQueryKpiColumn> lstColumn = configQueryKpiDTO.getConfigQueryColum();
        Map<String, ConfigQueryKpiColumn> columsUpdate = lstColumn.stream().collect(Collectors.toMap(ConfigQueryKpiColumn::getKeyGroup, Function.identity()));

        List<ConfigColumnQueryKpi> lstColumnInsert = lstColumn.stream()
            .filter(e -> lstCurrentCol == null || !lstCurrentCol.contains(e.getKpi() + "_" + e.getColumnInDestinationTable()))
            .map(e -> {
                ConfigColumnQueryKpi configColumnQueryKpi = new ConfigColumnQueryKpi();
                configColumnQueryKpi.setMapKpiQueryId(mapKpiId.get(e.getKpi()));
                configColumnQueryKpi.setColumnQuery(e.getAliasQuery());
                configColumnQueryKpi.setDataType(e.getDataType());
                configColumnQueryKpi.setColumnDestination(e.getColumnInDestinationTable());
                configColumnQueryKpi.setStatus(e.getStatus());
                configColumnQueryKpi.setUpdateTime(currentDate);
                configColumnQueryKpi.setUpdateUser(actor);
                return configColumnQueryKpi;
            }).collect(Collectors.toList());
        configColumnQueryKpiRepository.saveAll(lstColumnInsert);
        List<ConfigColumnQueryKpi> lstColumnUpdate = lstCurrentColumns.stream().filter(e -> columsUpdate.containsKey(e.groupKey())).map(e -> {
            Optional<ConfigColumnQueryKpi> configColum = configColumnQueryKpiRepository.findById(e.getId());
            ConfigQueryKpiColumn column = columsUpdate.get(e.groupKey());

            if (configColum.isPresent()) {
                ConfigColumnQueryKpi model = configColum.get();
                model.setColumnQuery(column.getAliasQuery());
                model.setDataType(column.getDataType());
                model.setColumnQuery(column.getAliasQuery());
                model.setStatus(column.getStatus());
                model.setUpdateTime(currentDate);
                model.setUpdateUser(actor);
                return model;
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        configColumnQueryKpiRepository.saveAll(lstColumnUpdate);

        List<ConfigColumnQueryKpi> lstDelete = lstCurrentColumns.stream().filter(e -> !columsUpdate.containsKey(e.groupKey())).map(e -> {
            Optional<ConfigColumnQueryKpi> configColum = configColumnQueryKpiRepository.findById(e.getId());
            if (configColum.isPresent()) {
                return configColum.get();
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        configColumnQueryKpiRepository.deleteAll(lstDelete);
    }

    private Map<Integer, Long> updateConfigMapKpiQuery(Integer queryId, ConfigQueryKpiDetailDTO configQueryKpiDTO, Set<Integer> kpiInsert,
                                                       List<ConfigMapKpiQuery> kpiUpdate, List<ConfigMapKpiQuery> kpiDelete,
                                                       String actor, Integer status, Instant currentDate, Map<Integer, Integer> mapKpiStatus) {
        Map<Integer, Long> mapKpiId = new HashMap<>();
        kpiInsert.forEach(kpi -> {
            ConfigMapKpiQuery configMapKpiQuery = new ConfigMapKpiQuery();
            configMapKpiQuery.setQueryKpiId(queryId);
            configMapKpiQuery.setKpiId(kpi);
            configMapKpiQuery.setTableDestination(configQueryKpiDTO.getConfigQueryKpi().getTableDestination());
            configMapKpiQuery.setStatus(mapKpiStatus.get(kpi));
            configMapKpiQuery.setUpdateTime(currentDate);
            configMapKpiQuery.setUpdateUser(actor);
            configMapKpiQuery = configMapKpiQueryRepository.save(configMapKpiQuery);
            mapKpiId.put(kpi, configMapKpiQuery.getId());
        });
        kpiUpdate.forEach(kpi -> {
            kpi.setStatus(mapKpiStatus.get(kpi.getKpiId()));
            kpi.setUpdateTime(currentDate);
            kpi.setUpdateUser(actor);
            mapKpiId.put(kpi.getKpiId(), kpi.getId());
        });
        configMapKpiQueryRepository.saveAll(kpiUpdate);

        if (kpiDelete != null) {
            configMapKpiQueryRepository.deleteAll(kpiDelete);
        }
        return mapKpiId;
    }

    private void updateConfigInputTableQueryKpi(ConfigQueryKpiDetailDTO configQueryKpiDTO, Integer queryId, Integer status, Instant currentDate) {
        //Danh sach cac bang nguon
        List<ConfigInputTableQueryKpi> lstCurrentConfigInputTable = configInputTableQueryKpiRepository.findAllByQueryKpiId(queryId);
        Set<String> mapCurrentTable = lstCurrentConfigInputTable.stream().map(ConfigInputTableQueryKpi::getTableSource).collect(Collectors.toSet());

        // Insert config_input_table_query_kpi
        if (!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryKpi().getTableSource())) {
            Set<String> mapUpdateTable = configQueryKpiDTO.getConfigQueryKpi().getTableSource().stream().collect(Collectors.toSet());

            List<ConfigInputTableQueryKpi> lstTableInsert = configQueryKpiDTO.getConfigQueryKpi().getTableSource().stream().
                filter(e -> !mapCurrentTable.contains(e)).map(e -> {
                ConfigInputTableQueryKpi configInputTableQueryKpi = new ConfigInputTableQueryKpi();
                configInputTableQueryKpi.setQueryKpiId(queryId);
                configInputTableQueryKpi.setTableSource(e);
                configInputTableQueryKpi.setStatus(status);
                configInputTableQueryKpi.setUpdateTime(currentDate);
                return configInputTableQueryKpi;
            }).collect(Collectors.toList());
            configInputTableQueryKpiRepository.saveAll(lstTableInsert);

            List<ConfigInputTableQueryKpi> lstTableUpdate = lstCurrentConfigInputTable.stream().filter(e -> mapUpdateTable != null && mapUpdateTable.contains(e.getTableSource())).peek(e -> {
                e.setStatus(status);
                e.setUpdateTime(currentDate);
            }).collect(Collectors.toList());
            configInputTableQueryKpiRepository.saveAll(lstTableUpdate);

            List<ConfigInputTableQueryKpi> lstTableDelete = lstCurrentConfigInputTable.stream()
                .filter(e -> mapUpdateTable != null && !mapUpdateTable.contains(e.getTableSource())).collect(Collectors.toList());
            configInputTableQueryKpiRepository.deleteAll(lstTableDelete);
        }
    }

    private void updateConfigInputKpiQuery(ConfigQueryKpiDetailDTO configQueryKpiDTO, Integer queryId, Integer status, Instant currentDate) {
        List<ConfigInputKpiQuery> lstCurrentInputKpi = configInputKpiQueryRepository.findAllByQueryKpiId(queryId);
        Set<String> mapCurrentInputKpi = lstCurrentInputKpi.stream().map(ConfigInputKpiQuery::getKpiId).collect(Collectors.toSet());

        if (!DataUtil.isNullOrEmpty(configQueryKpiDTO.getConfigQueryKpi().getInputKpi())) {
            Set<String> mapUpdateKpi = configQueryKpiDTO.getConfigQueryKpi().getInputKpi().stream().map(e -> e.toString()).collect(Collectors.toSet());

            List<ConfigInputKpiQuery> lstInputKpiInsert = configQueryKpiDTO.getConfigQueryKpi().getInputKpi().stream().
                filter(e -> DataUtil.isNullOrEmpty(mapCurrentInputKpi) || !mapCurrentInputKpi.contains(e.toString())).map(e -> {
                ConfigInputKpiQuery configInputKpiQuery = new ConfigInputKpiQuery();
                configInputKpiQuery.setQueryKpiId(queryId);
                configInputKpiQuery.setKpiId(String.valueOf(e));
                configInputKpiQuery.setStatus(status);
                configInputKpiQuery.setUpdateTime(currentDate);
                return configInputKpiQuery;
            }).collect(Collectors.toList());
            configInputKpiQueryRepository.saveAll(lstInputKpiInsert);

            List<ConfigInputKpiQuery> lstInputKpiUpdate = lstCurrentInputKpi.stream()
                .filter(e -> mapUpdateKpi != null && mapUpdateKpi.contains(e.getKpiId())).peek(e -> {
                    e.setStatus(status);
                    e.setUpdateTime(currentDate);
                }).collect(Collectors.toList());
            configInputKpiQueryRepository.saveAll(lstInputKpiUpdate);

            List<ConfigInputKpiQuery> lstInputKpiDelete = lstCurrentInputKpi.stream()
                .filter(e -> !mapUpdateKpi.contains(e.getKpiId())).collect(Collectors.toList());
            configInputKpiQueryRepository.deleteAll(lstInputKpiDelete);

        } else {
            configInputKpiQueryRepository.deleteAll(lstCurrentInputKpi);
        }
    }

    /**
     * Get all the configQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigQueryKpiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigQueryKpis");
        return configQueryKpiRepository.findAll(pageable)
            .map(configQueryKpiMapper::toDto);
    }

    @Override
    public Page<ConfigQueryKpiResultDTO> findConfigQueryKpis(ConfigQueryKpiSearch searchForm, Pageable pageable) {
        Page<ConfigQueryKpiResultDTO> page = configQueryKpiRepository.findAllByDomainAndKpiAndStatus(searchForm.getDomainCode(),
            searchForm.getKpiId(), searchForm.getStatus(), DataUtil.makeLikeParam(searchForm.getQueryId()), pageable).map(e -> this.objectToConfigQueryKpiResultDTO(e));
        return page;
    }

    private ConfigQueryKpiResultDTO objectToConfigQueryKpiResultDTO(Object[] obj) {
        ConfigQueryKpiResultDTO dto = new ConfigQueryKpiResultDTO();
        dto.setQueryKpiId(DataUtil.safeToLong(obj[0]));
        dto.setTimeTypeName(DataUtil.safeToString(obj[1]));
        dto.setInputLevelName(DataUtil.safeToString(obj[2]));
        dto.setQueryData(DataUtil.safeToString(obj[3]));
        dto.setQueryCheckData(DataUtil.safeToString(obj[4]));
        dto.setListParentInputLevel(DataUtil.safeToString(obj[5]));
        dto.setDescription(DataUtil.safeToString(obj[6]));
        dto.setUpdateTime(DataUtil.toInstant(obj[7]));
        dto.setUpdateUser(DataUtil.safeToString(obj[8]));
        dto.setTableDestination(DataUtil.safeToString(obj[9]));
        dto.setListTableSource(DataUtil.safeToString(obj[10]));
        return dto;
    }


    /**
     * Get one configQueryKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigQueryKpiDTO> findOne(Long id) {
        log.debug("Request to get ConfigQueryKpi : {}", id);
        return configQueryKpiRepository.findById(id)
            .map(configQueryKpiMapper::toDto);
    }

    @Override
    public Optional<ConfigQueryKpiDetailDTO> findConfigById(Long id) {
        ConfigQueryKpiDetailDTO dto = new ConfigQueryKpiDetailDTO();
        ConfigQueryKpi configQueryKpi = configQueryKpiRepository.getOne(id);
        ConfigQueryKpiForm form = new ConfigQueryKpiForm();
        form.setId(id);
        form.setTimeType(configQueryKpi.getTimeType().toString());
        form.setInputLevel(configQueryKpi.getInputLevel().toString());
        if (!DataUtil.isNullOrEmpty(configQueryKpi.getListParentInputLevel())) {
            form.setListParentInputLevel(Arrays.asList(configQueryKpi.getListParentInputLevel().split(",")));
        }
        if (Constants.STATUS_ACTIVE.intValue() == configQueryKpi.getStatus()) {
            form.setStatus(true);
        } else {
            form.setStatus(false);
        }

        form.setDescription(configQueryKpi.getDescription());
        form.setQueryCheckData(configQueryKpi.getQueryCheckData());
        form.setQueryData(configQueryKpi.getQueryData());
        form.setReportId(configQueryKpi.getReportId());


        // Danh sach bang nguon
        List<String> lstInputTable = configInputTableQueryKpiRepository.findAllByQueryKpiId(id.intValue()).stream().
            map(e -> e.getTableSource().toUpperCase()).collect(Collectors.toList());
        form.setTableSource(lstInputTable);

        // Danh sach chi tieu dau vao
        List<Integer> lstKpiInput = configInputKpiQueryRepository.findAllByQueryKpiId(id.intValue()).stream().
            map(e -> Integer.valueOf(e.getKpiId())).collect(Collectors.toList());
        form.setInputKpi(lstKpiInput);
        Optional<ConfigMapKpiQuery> configMapKpiQuery = configMapKpiQueryRepository.findAllByQueryKpiId(id.intValue()).stream().findFirst();
        if (configMapKpiQuery.isPresent()) {
            form.setTableDestination(configMapKpiQuery.get().getTableDestination().toUpperCase());
        }

        dto.setConfigQueryKpi(form);
        List<ConfigQueryKpiColumn> lstColumn = this.findAllColumnByQueryId(id.intValue());
        dto.setConfigQueryColum(lstColumn);

        return Optional.of(dto);
    }

    /**
     * Delete the configQueryKpi by id.
     *
     * @param id the id of the entity.
     */
    public void deleteTemp(Long id) {
        log.debug("Request to delete ConfigQueryKpi : {}", id);
        String actor = SecurityUtils.getCurrentUserLogin().get();
        ConfigQueryKpi configQueryKpi = configQueryKpiRepository.getOne(id);
        Integer status = 0;
        Instant currentTime = Instant.now();
        if (configQueryKpi != null) {
            //Cap nhat bang config_query_kpi
            configQueryKpi.setUpdateTime(currentTime);
            configQueryKpi.setUpdateUser(actor);
            configQueryKpi.setStatus(status);
            configQueryKpiRepository.save(configQueryKpi);

            // Cap nhat bang config_map_kpi_query
            List<ConfigMapKpiQuery> lstConfigMapKpiQuery = configMapKpiQueryRepository.findAllByQueryKpiId(id.intValue()).stream().
                peek(e -> {
                    e.setUpdateUser(actor);
                    e.setUpdateTime(currentTime);
                    e.setStatus(status);
                }).collect(Collectors.toList());
            configMapKpiQueryRepository.saveAll(lstConfigMapKpiQuery);

            //Cap nhat bang config_column_query_kpi
            List<ConfigColumnQueryKpi> lstConfigColumnQuery = configColumnQueryKpiRepository.findAllByQueryId(id.intValue()).stream().
                peek(e -> {
                    e.setUpdateUser(actor);
                    e.setUpdateTime(currentTime);
                    e.setStatus(status);
                }).collect(Collectors.toList());
            configColumnQueryKpiRepository.saveAll(lstConfigColumnQuery);

            //Update du lieu bang config_input_table_query_kpi
            List<ConfigInputTableQueryKpi> lstConfigInputTableQuery = configInputTableQueryKpiRepository.findAllByQueryKpiId(id.intValue()).stream().
                peek(e -> {
                    e.setUpdateTime(currentTime);
                    e.setStatus(status);
                }).collect(Collectors.toList());
            configInputTableQueryKpiRepository.saveAll(lstConfigInputTableQuery);

            //update du lieu bang
            List<ConfigInputKpiQuery> lstConfigInputKpi = configInputKpiQueryRepository.findAllByQueryKpiId(id.intValue()).stream().
                peek(e -> {
                    e.setStatus(status);
                    e.setUpdateTime(currentTime);
                }).collect(Collectors.toList());
            configInputKpiQueryRepository.saveAll(lstConfigInputKpi);
        }
    }
    public void delete(Long id) {
        log.debug("Request to delete ConfigQueryKpi : {}", id);
        ConfigQueryKpi configQueryKpi = configQueryKpiRepository.getOne(id);

        if (configQueryKpi != null) {
            configQueryKpiRepository.delete(configQueryKpi);

            //Cap nhat bang config_column_query_kpi
            List<ConfigColumnQueryKpi> lstConfigColumnQuery = configColumnQueryKpiRepository.findAllByQueryId(id.intValue());
            configColumnQueryKpiRepository.deleteAll(lstConfigColumnQuery);

            // Cap nhat bang config_map_kpi_query
            List<ConfigMapKpiQuery> lstConfigMapKpiQuery = configMapKpiQueryRepository.findAllByQueryKpiId(id.intValue());
            configMapKpiQueryRepository.deleteAll(lstConfigMapKpiQuery);

            //Update du lieu bang config_input_table_query_kpi
            List<ConfigInputTableQueryKpi> lstConfigInputTableQuery = configInputTableQueryKpiRepository.findAllByQueryKpiId(id.intValue());
            configInputTableQueryKpiRepository.deleteAll(lstConfigInputTableQuery);

            //update du lieu bang
            List<ConfigInputKpiQuery> lstConfigInputKpi = configInputKpiQueryRepository.findAllByQueryKpiId(id.intValue());
            configInputKpiQueryRepository.deleteAll(lstConfigInputKpi);

            monitorQueryKpiRepository.findFirstByQueryKpiIdEquals(id.intValue()).ifPresent(e->{
                monitorQueryKpiRepository.delete(e);
            });

        }
    }

    /**
     * Sao chep cau hinh
     *
     * @param id
     */
    @Override
    public void clone(Long id) throws Exception {
        String actor = SecurityUtils.getCurrentUserLogin().get();
        Optional<ConfigQueryKpiDTO> configQueryKpi = configQueryKpiRepository.findById(id).map(configQueryKpiMapper::toDto);
        Instant currentTime = Instant.now();
        Integer status = 0;
        if (!configQueryKpi.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configQueryKpi.queryIdNotExist"), ENTITY_NAME, "configQueryKpi.queryIdNotExist");
        }
        //Clone config_query_kpi
        ConfigQueryKpiDTO configQueryDTO = configQueryKpi.get();
        configQueryDTO.setQueryKpiId(null);
        configQueryDTO.setStatus(status);
        configQueryDTO.setUpdateUser(actor);
        configQueryDTO.setUpdateTime(currentTime);

        ConfigQueryKpi configQuery = configQueryKpiMapper.toEntity(configQueryDTO);

        configQuery = configQueryKpiRepository.save(configQuery);
        Integer queryId = configQuery.getId().intValue();

        //clone config_input_table_query_kpi
        List<ConfigInputTableQueryKpiDTO> lstConfigInputTableQuery = configInputTableQueryKpiRepository.findAllByQueryKpiId(id.intValue()).stream().
            map(configInputTableQueryKpiMapper::toDto).
            peek(e -> {
                e.setId(null);
                e.setQueryKpiId(queryId);
                e.setUpdateTime(currentTime);
                e.setStatus(status);
            }).collect(Collectors.toList());
        configInputTableQueryKpiRepository.saveAll(configInputTableQueryKpiMapper.toEntity(lstConfigInputTableQuery));

        //clone config_input_kpi_query
        List<ConfigInputKpiQueryDTO> lstConfigInputKpi = configInputKpiQueryRepository.findAllByQueryKpiId(id.intValue()).stream().
            map(configInputKpiQueryMapper::toDto).
            peek(e -> {
                e.setId(null);
                e.setQueryKpiId(queryId);
                e.setStatus(status);
                e.setUpdateTime(currentTime);
            }).collect(Collectors.toList());
        configInputKpiQueryRepository.saveAll(configInputKpiQueryMapper.toEntity(lstConfigInputKpi));

        List<ConfigColumnQueryKpiDTO> lstConfigColumnQuery = configColumnQueryKpiRepository.findAllByQueryId(id.intValue()).stream()
            .map(configColumnQueryKpiMapper::toDto).collect(Collectors.toList());
        Map<Long, List<ConfigColumnQueryKpiDTO>> mapGroupConfigColumn = lstConfigColumnQuery.stream().collect(Collectors.groupingBy(ConfigColumnQueryKpiDTO::getMapKpiQueryId, Collectors.toList()));

        List<ConfigMapKpiQueryDTO> lstConfigMapKpiQuery = configMapKpiQueryRepository.findAllByQueryKpiId(id.intValue()).stream()
            .map(configMapKpiQueryMapper::toDto).collect(Collectors.toList());
        for (ConfigMapKpiQueryDTO configMapKpiQueryDTO : lstConfigMapKpiQuery) {
            //Lay danh sach cac column query truoc
            List<ConfigColumnQueryKpiDTO> lstConfigColumn = mapGroupConfigColumn.get(configMapKpiQueryDTO.getId());

            configMapKpiQueryDTO.setId(null);
            configMapKpiQueryDTO.setQueryKpiId(queryId);
            configMapKpiQueryDTO.setStatus(status);
            configMapKpiQueryDTO.setUpdateTime(currentTime);
            configMapKpiQueryDTO.setUpdateUser(actor);

            ConfigMapKpiQuery configMapKpiQuery = configMapKpiQueryMapper.toEntity(configMapKpiQueryDTO);
            configMapKpiQuery = configMapKpiQueryRepository.save(configMapKpiQuery);
            Long mapKpiQuery = configMapKpiQuery.getId();
            if (!DataUtil.isNullOrEmpty(lstConfigColumn)) {
                lstConfigColumn.forEach(e -> {
                    e.setId(null);
                    e.setStatus(status);
                    e.setUpdateTime(currentTime);
                    e.setUpdateUser(actor);
                    e.setMapKpiQueryId(mapKpiQuery);
                });
                configColumnQueryKpiRepository.saveAll(configColumnQueryKpiMapper.toEntity(lstConfigColumn));
            }
        }

        // Insert du lieu vao bang monitor_query_kpi
        MonitorQueryKpi monitorQueryKpi = new MonitorQueryKpi();
        monitorQueryKpi.setQueryKpiId(queryId);
        monitorQueryKpi.setStatus(0);
        monitorQueryKpi.setUpdateTime(currentTime);
        monitorQueryKpiRepository.save(monitorQueryKpi);

    }

    @Override
    public Page<ComboDTO> findAllKpiOutput(String kpiId, Pageable pageable) {
        return configQueryKpiRepository.findAllKpiOutput(kpiId, pageable).map(e -> this.objectToKpi(e));
    }

    @Override
    public Page<ComboDTO> findAllKpiInput(String kpiId, Pageable pageable) {
        return configQueryKpiRepository.findAllKpiInput(kpiId, pageable).map(e -> this.objectToKpi(e));
    }

    private ComboDTO objectToKpi(Object[] obj) {
        ComboDTO dto = new ComboDTO();
        dto.setValue(obj[0]);
        dto.setLabel(DataUtil.safeToString(obj[1]));
        dto.setStatus(obj[2]);
        return dto;
    }
    private ComboDTO objectToKpiSourceTable(Object[] obj) {
        ComboDTO dto = new ComboDTO();
        dto.setValue(obj[0]);
        dto.setLabel(DataUtil.safeToString(obj[1]));
        return dto;
    }

    @Override
    public List<ComboDTO> findAllKpiSourceTable(List<String> lstTableSource) {
        return configQueryKpiRepository.findAllKpiSourceTable(lstTableSource).stream().map(e -> this.objectToKpiSourceTable(e)).collect(Collectors.toList());
    }


    @Override
    public List<ConfigQueryKpiColumn> findAllColumnByQueryId(Integer queryId) {
        return configColumnQueryKpiRepository.findAllColumnByQueryId(queryId).stream().
            map(e -> {
                ConfigQueryKpiColumn dto = new ConfigQueryKpiColumn();
                dto.setKpi(DataUtil.safeToInt(e[0]));
                dto.setAliasQuery(DataUtil.safeToString(e[1]));
                dto.setColumnInDestinationTable(DataUtil.safeToString(e[2]));
                dto.setDataType(DataUtil.safeToString(e[3]));
                dto.setStatus(DataUtil.safeToInt(e[4]));
                return dto;
            }).collect(Collectors.toList());
    }

    public List<ConfigColumnQueryKpiDetail> findAllConfigColumnQueryKpiByQueryId(Integer queryId) {
        return configColumnQueryKpiRepository.findAllConfigColumnQueryKpiByQueryId(queryId).stream().
            map(e -> {
                ConfigColumnQueryKpiDetail dto = new ConfigColumnQueryKpiDetail();
                dto.setKpiId(DataUtil.safeToInt(e[0]));
                dto.setColumnQuery(DataUtil.safeToString(e[1]));
                dto.setColumnDestination(DataUtil.safeToString(e[2]));
                dto.setDataType(DataUtil.safeToString(e[3]));
                dto.setMapKpiQueryId(DataUtil.safeToLong(e[4]));
                dto.setId(DataUtil.safeToLong(e[5]));
                return dto;
            }).collect(Collectors.toList());
    }

    @Override
    public void checkQueryData(String query) throws Exception {
        boolean result = customRepository.checkQueryData(query, new Date());
        if(!result) {
            throw new BadRequestAlertException(Translator.toLocale("error.configQueryKpi.queryDataNotExecute"), ENTITY_NAME, "configQueryKpi.queryIdNotExist");
        }
    }

    @Override
    public List<ConfigQueryKpiDTO> findAllByReportId(Long reportId) {
        return configQueryKpiRepository.findAllByReportId(reportId).stream()
            .map(e -> {
                ConfigQueryKpiDTO dto = configQueryKpiMapper.toDto(e);
                dto.setQueryKpiId(e.getId());
                return dto;
            }).collect(Collectors.toList());
    }
}
