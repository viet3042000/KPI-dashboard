package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.ExcelUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.repository.ConfigReportImportRepository;
import com.b4t.app.repository.ConfigReportRepository;
import com.b4t.app.repository.impl.DataLogRepositoryImpl;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.ConfigReportColumnMapper;
import com.b4t.app.service.mapper.ConfigReportMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Service Implementation for managing {@link ConfigReport}.
 */
@Service
public class ConfigReportServiceImpl implements ConfigReportService {

    private static final String ENTITY_NAME = "configReport";
    private final Logger log = LoggerFactory.getLogger(ConfigReportServiceImpl.class);

    private final ConfigReportRepository configReportRepository;
    private final ConfigReportColumnService configReportColumnService;
    private final ConfigReportMapper configReportMapper;
    private final ConfigReportColumnMapper configReportColumnMapper;
    private final ConfigReportImportRepository configReportImportRepository;
    private final FlagRunQueryKpiService flagRunQueryKpiService;
    private final CommonService commonService;
    private final DataLogRepositoryImpl dataLogRepositoryImpl;
    private final UserService userService;

    private final EntityManager entityManager;
    //    private final DataLogRepository dataLogRepository;
    final CatItemRepository catItemRepository;
    Map<String, String> mapInputLevel = new HashMap<>();
    Map<String, String> mapTimeType = new HashMap<>();

    private final ExcelUtils excelUtils;
    @Autowired
    ConfigReportUtilsService configReportUtilsService;


    public ConfigReportServiceImpl(ConfigReportRepository configReportRepository, ConfigReportMapper configReportMapper,
                                   EntityManager entityManager, CatItemRepository catItemRepository,
                                   ConfigReportColumnService configReportColumnService, ConfigReportColumnMapper configReportColumnMapper,
                                   ConfigReportImportRepository configReportImportRepository, ExcelUtils excelUtils,
                                   FlagRunQueryKpiService flagRunQueryKpiService, CommonService commonService,
                                   DataLogRepositoryImpl dataLogRepositoryImpl, UserService userService) {
        this.configReportRepository = configReportRepository;
        this.configReportMapper = configReportMapper;
        this.entityManager = entityManager;
        this.catItemRepository = catItemRepository;
        this.configReportImportRepository = configReportImportRepository;
        this.commonService = commonService;
//        this.dataLogRepository = dataLogRepository;
        this.dataLogRepositoryImpl = dataLogRepositoryImpl;
        this.userService = userService;

        List<CatItem> lstCatItem = catItemRepository.findCatItemByCategoryIdAndStatus(Constants.CATEGORY.INPUT_LEVEL, Constants.STATUS_ACTIVE);
        if (!DataUtil.isNullOrEmpty(lstCatItem)) {
            mapInputLevel = lstCatItem.stream().collect(Collectors.toMap(CatItem::getItemValue, CatItem::getItemName, (e1, e2) -> e1));
        }

        List<CatItem> lstCatItemTimeType = catItemRepository.findCatItemByCategoryIdAndStatus(Constants.CATEGORY.TIME_TYPE, Constants.STATUS_ACTIVE);
        if (!DataUtil.isNullOrEmpty(lstCatItemTimeType)) {
            mapTimeType = lstCatItemTimeType.stream().collect(Collectors.toMap(CatItem::getItemValue, CatItem::getItemName, (e1, e2) -> e1));
        }
        this.configReportColumnService = configReportColumnService;
        this.configReportColumnMapper = configReportColumnMapper;
        this.excelUtils = excelUtils;
        this.flagRunQueryKpiService = flagRunQueryKpiService;
    }


    private Map<String, String> getMapByCategory(String categoryCode) {
        Map<String, String> mapDomain = new HashMap<>();
        List<CatItem> lstCatItem = catItemRepository.getCatItemByCategoryCode(categoryCode);
        if (!DataUtil.isNullOrEmpty(lstCatItem)) {
            mapDomain = lstCatItem.stream().collect(Collectors.toMap(CatItem::getItemValue, CatItem::getItemName, (e1, e2) -> e1));
        }
        return mapDomain;
    }

    /**
     * Save a configReport.
     *
     * @param configReportDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigReportDTO save(ConfigReportDTO configReportDTO) {
        log.debug("Request to save ConfigReport : {}", configReportDTO);
        ConfigReport configReport = configReportMapper.toEntity(configReportDTO);
        configReport = configReportRepository.save(configReport);
        return configReportMapper.toDto(configReport);
    }

    private ConfigReportColumnDTO getPrimaryKeyColumn(ConfigReportDTO configReportDTO) {
        List<ConfigReportColumnDTO> lstPrimaryKeyCol = findPrimaryKeyColumnOfTableShowEnable(configReportDTO.getDatabaseName() + "." + configReportDTO.getTableName());
        ConfigReportColumnDTO primaryKeyColumn = DataUtil.getPrimaryKeyColumn(lstPrimaryKeyCol);
        return primaryKeyColumn;
    }

    /**
     * Luu them moi cau hinh
     *
     * @param configReportDetailDTO
     * @param actor
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigReportDetailDTO save(ConfigReportDetailDTO configReportDetailDTO, String actor) {
        ConfigReportDTO configReportDTO = configReportDetailDTO.getConfigReport();
        configReportDTO.setCreator(actor);
        configReportDTO.setUpdateTime(Instant.now());
        configReportDTO.setStatus(Constants.STATUS_ACTIVE.intValue());

        //Lay thong tin cot khoa chinh cua bang
        ConfigReportColumnDTO primaryKeyColumn = getPrimaryKeyColumn(configReportDTO);
        //Danh sach cac cot cap nhat
        List<ConfigReportColumnDTO> configReportColumns = configReportDetailDTO.getConfigReportColumns();

        //Validate cac cot mapping
        validateColumn(configReportDTO, configReportColumns);

        ConfigReportDTO configReport = save(configReportDTO);

        //Them cot khoa de luu vao bang cau hinh
        if (primaryKeyColumn != null) {
            if (!configReportColumns.stream().filter(e -> "id".equalsIgnoreCase(e.getColumnName())).findFirst().isPresent()) {
                configReportColumns.add(primaryKeyColumn);
            }
        }

        configReportColumns = configReportColumns.stream().peek(column ->
        {
            column.setReportId(configReport.getId());
            column.setCreator(actor);
            column.setUpdateTime(Instant.now());
        }).collect(Collectors.toList());
        configReportColumns = configReportColumnService.saveAll(configReportColumns);

        ConfigReportDetailDTO result = new ConfigReportDetailDTO();
        result.setConfigReport(configReport);
        result.setConfigReportColumns(configReportColumns);
        return result;
    }

    /**
     * Lay danh sach cac cot cua bang table
     *
     * @param table
     * @return
     */
    public List<ConfigReportColumnDTO> findAllColumnOfTable(String table) {
        AtomicInteger atomicInteger = new AtomicInteger();
        return configReportImportRepository.getFullDescriptionOfTable(table)
            .stream().map(column -> {
                ConfigReportColumnDTO columnDTO = new ConfigReportColumnDTO();
                columnDTO.setColumnName(DataUtil.safeToString(column[0]).toLowerCase());
                columnDTO.setDataType(DataUtil.safeToString(column[1]));
                String nullable = DataUtil.safeToString(column[3]);
                columnDTO.setIsRequire("YES".equalsIgnoreCase(nullable) ? 0 : 1);
                columnDTO.setKey(DataUtil.safeToString(column[4]));
                columnDTO.setDefaultValue(DataUtil.safeToString(column[5]));
                columnDTO.setExtra(DataUtil.safeToString(column[6]));
                columnDTO.setTitle(DataUtil.safeToString(column[8], DataUtil.safeToString(column[0])));
                columnDTO.setIsShow(Constants.IS_SHOW);
                columnDTO.setPos(atomicInteger.getAndIncrement());
                return columnDTO;
            }).peek(e -> {
                String patternStr = "^[a-z0-9]*\\((\\d+)\\s*[a-z0-9]*\\)$";
                Pattern pattern = Pattern.compile(patternStr);
                Matcher match = pattern.matcher(e.getDataType());
                if (match.find()) {
                    String maxLength = match.group(1);
                    e.setMaxLength(Integer.valueOf(maxLength));
                }
                if (e.getDataType().contains(Constants.DATA_DB_TYPE.VARCHAR)) {
                    e.setDataType(Constants.DATA_TYPE.STRING);
                } else if (e.getDataType().contains(Constants.DATA_DB_TYPE.BIGINT)) {
                    e.setDataType(Constants.DATA_TYPE.BIGINT);
                } else if (e.getDataType().contains(Constants.DATA_DB_TYPE.DATE) || e.getDataType().contains(Constants.DATA_DB_TYPE.DATETIME) || e.getDataType().contains(Constants.DATA_DB_TYPE.TIMESTAMP)) {
                    e.setDataType(Constants.DATA_TYPE.DATE);
                } else if (e.getDataType().contains(Constants.DATA_DB_TYPE.DOUBLE) || e.getDataType().contains(Constants.DATA_DB_TYPE.FLOAT)) {
                    e.setDataType(Constants.DATA_TYPE.DOUBLE);
                } else if (e.getDataType().contains(Constants.DATA_DB_TYPE.INT)) {
                    e.setDataType(Constants.DATA_TYPE.INT);
                } else if (e.getDataType().contains(Constants.DATA_DB_TYPE.LONG)) {
                    e.setDataType(Constants.DATA_TYPE.LONG);
                }
            }).collect(Collectors.toList());
    }

    /**
     * Lay danh sach cac cot can hien thi cho nguoi dung cau hinh
     *
     * @param table
     * @return
     */
    public List<ConfigReportColumnDTO> findColumnOfTableShowEnable(String table) {
        List<ConfigReportColumnDTO> lstColumn = findAllColumnOfTable(table).stream()
            .filter(e -> (!Constants.PRIMARY_KEY.equalsIgnoreCase(e.getKey()) || !Constants.AUTO_INCREMENT.equalsIgnoreCase(e.getExtra())))
            .collect(Collectors.toList());
        return lstColumn;
    }

    public List<ConfigReportColumnDTO> findPrimaryKeyColumnOfTableShowEnable(String table) {
        List<ConfigReportColumnDTO> lstColumn = findAllColumnOfTable(table).stream()
            .filter(e -> (Constants.PRIMARY_KEY.equalsIgnoreCase(e.getKey()) && Constants.AUTO_INCREMENT.equalsIgnoreCase(e.getExtra())))
            .collect(Collectors.toList());
        return lstColumn;
    }

    /**
     * Validate thong tin
     *
     * @param configReportDTO
     * @param configReportColumns
     * @throws BadRequestAlertException
     */
    private void validateColumn(ConfigReportDTO configReportDTO, List<ConfigReportColumnDTO> configReportColumns) throws BadRequestAlertException {
        List<ConfigReportColumnDTO> lstColumInfo = findColumnOfTableShowEnable(configReportDTO.getDatabaseName() + "." + configReportDTO.getTableName());

        Map<String, ConfigReportColumnDTO> mapColumnInfo = lstColumInfo.stream().collect(Collectors.toMap(ConfigReportColumnDTO::getColumnName, Function.identity()));
        for (ConfigReportColumnDTO columnDTO : configReportColumns) {
            //Validate ten cot cau hinh phai co trong database
            if (!Integer.valueOf(1).equals(columnDTO.getIsPrimaryKey()) && !mapColumnInfo.containsKey(columnDTO.getColumnName())) {
                throw new BadRequestAlertException(columnDTO.getColumnName() + " " + Translator.toLocale("error.configReport.columnNameNotExist"), ENTITY_NAME, "configReport.columnNameNotExist");
            }
            //Validate kieu du lieu cua cau hinh phai map voi kieu du lieu cua cot trong bang du lieu
            ConfigReportColumnDTO col = mapColumnInfo.get(columnDTO.getColumnName());
            if (col != null && !col.getDataType().equals(columnDTO.getDataType())) {
                throw new BadRequestAlertException(columnDTO.getColumnName() + " " + Translator.toLocale("error.configReport.mustBeType") + col.getDataType(), ENTITY_NAME, "configReport.mustBeType");
            }
        }

        //Validate cac cot bat buoc trong DB thi phai duoc cau hinh
        Map<String, ConfigReportColumnDTO> mapColumnConfig = configReportColumns.stream().collect(Collectors.toMap(ConfigReportColumnDTO::getColumnName, Function.identity()));
        for (ConfigReportColumnDTO column : lstColumInfo) {
            if (!"id".equalsIgnoreCase(column.getColumnName()) && Constants.IS_REQUIRED.equals(column.getIsRequire()) && !mapColumnConfig.containsKey(column.getColumnName())) {
                throw new BadRequestAlertException(column.getColumnName() + " " + Translator.toLocale("error.configReport.needToConfig") + column.getDataType(), ENTITY_NAME, "configReport.needToConfig");
            }
        }

        //Validate can 1 cot luu thong tin thoi gian
        List lstColumnTime = configReportColumns.stream().filter(e -> Constants.IS_TIME_COLUMN.equals(e.getIsTimeColumn())).collect(Collectors.toList());
        if (lstColumnTime == null || lstColumnTime.isEmpty()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.needATimeColumn"), ENTITY_NAME, "configReport.needATimeColumn");
        }
        if (lstColumnTime.size() > 1) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.onlyOneTimeColumn"), ENTITY_NAME, "configReport.onlyOneTimeColumn");

        }
    }


    /**
     * Save truong hop cap nhat cau hinh
     *
     * @param configReportDetailDTO
     * @param actor
     * @return
     * @throws BadRequestAlertException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigReportDetailDTO saveUpdate(ConfigReportDetailDTO configReportDetailDTO, String actor) throws BadRequestAlertException {
        ConfigReportDTO configReportDTO = configReportDetailDTO.getConfigReport();
        configReportDTO.setCreator(actor);
        configReportDTO.setUpdateTime(Instant.now());

        //Lay thong tin cot khoa chinh cua bang
        ConfigReportColumnDTO primaryKeyColumn = getPrimaryKeyColumn(configReportDTO);

        //Danh sach cac cot cap nhat
        List<ConfigReportColumnDTO> configReportColumns = configReportDetailDTO.getConfigReportColumns();

        //Validate cac cot mapping
        validateColumn(configReportDTO, configReportColumns);

        // Tu them cot khoa chinh vao danh sach cap nhat
        if (primaryKeyColumn == null) {
            primaryKeyColumn = commonService.createColumn("id", "id", Constants.DATA_TYPE.BIGINT, 1, 1, 0, 1, 20, 0);
            primaryKeyColumn.setColumnUnique(0);
        }

        configReportColumns.add(primaryKeyColumn);


        Set<Long> lstColumnId = configReportColumns.stream().filter(e -> !DataUtil.isNullOrZero(e.getId())).map(ConfigReportColumnDTO::getId).collect(Collectors.toSet());

        //Lay danh sach cot cot hien tai
        List<ConfigReportColumnDTO> lstColumnCurrent = configReportColumnService.findAllByReportId(configReportDTO.getId());
        //Lay danh sach cac cot can xoa
        List<ConfigReportColumn> lstDelete = lstColumnCurrent.stream().filter(e -> !lstColumnId.contains(e.getId())).map(configReportColumnMapper::toEntity).collect(Collectors.toList());

        //Cap nhat bang config_report
        ConfigReportDTO configReport = save(configReportDTO);

        //Xoa cac column cu
        if (!DataUtil.isNullOrEmpty(lstDelete)) {
            configReportColumnService.delete(lstDelete);
        }

        //Cap nhat cac column moi
        configReportColumns = configReportColumns.stream().peek(column ->
        {
            column.setReportId(configReport.getId());
            column.setCreator(actor);
            column.setUpdateTime(Instant.now());
            column.setStatus(Constants.STATUS_ACTIVE.intValue());
        }).collect(Collectors.toList());
        configReportColumns = configReportColumnService.saveAll(configReportColumns);

        ConfigReportDetailDTO result = new ConfigReportDetailDTO();
        result.setConfigReport(configReport);
        result.setConfigReportColumns(configReportColumns);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApproveStatus(ConfigReportDetailDTO configReportDetailDTO) {
        StringBuilder sb = new StringBuilder("");
        sb.append("update ").append(configReportDetailDTO.getConfigReport().getDatabaseName()).append(".").append(configReportDetailDTO.getConfigReport().getTableName());
        sb.append(" set status = 2;");

        Query query = entityManager.createNativeQuery(sb.toString());


        query.executeUpdate();
        entityManager.flush();
    }


    @Transactional(readOnly = true)
    public Page<ConfigReportDTO> findAllCondition(ConfigReportForm configReport, Pageable pageable) {
        Map<Integer, String> mapStatus = com.b4t.app.commons.StringUtils.getMapStatus();
        Map<String, String> mapDomain = getMapByCategory(Constants.CATEGORY.DOMAIN_CATEOGRY);
        Map<String, String> mapUnit = getMapByCategory(Constants.CATEGORY.UNIT_IMPORT_CATEOGRY);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigReport> criteria = cb.createQuery(ConfigReport.class);
        Root<ConfigReport> root = criteria.from(ConfigReport.class);

        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<ConfigReport> rootCnt = countCrit.from(ConfigReport.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesCount = new ArrayList<>();

        // Tim kiem IN linh vuc
        if (!DataUtil.isNullOrEmpty(configReport.getLstDomainCode())) {
            Predicate predicate = root.get(ConfigReport_.DOMAIN_CODE).in(configReport.getLstDomainCode());
            predicates.add(predicate);
            predicatesCount.add(predicate);
        }

        // Tim kiem like tieu de
        if (StringUtils.isNotEmpty(configReport.getTitle())) {
            String title = DataUtil.makeLikeParam(configReport.getTitle());
            Predicate predicate = cb.like(cb.lower(root.get(ConfigReport_.TITLE)), title, Constants.DEFAULT_ESCAPE_CHAR);
            predicates.add(predicate);
            predicatesCount.add(predicate);
        }

        // Tim kiem EQUAL timeType
        if (StringUtils.isNotEmpty(configReport.getTimeType())) {
            predicates.add(cb.equal(root.get(ConfigReport_.TIME_TYPE), configReport.getTimeType()));
            predicatesCount.add(cb.equal(root.get(ConfigReport_.TIME_TYPE), configReport.getTimeType()));
        }

        // Tim kiem EQUAL status
        if (!DataUtil.isNullObject(configReport.getStatus())) {
            predicates.add(cb.equal(root.get(ConfigReport_.STATUS), configReport.getStatus()));
            predicatesCount.add(cb.equal(root.get(ConfigReport_.STATUS), configReport.getStatus()));
        }
        // Tim kiem EQUAL Databasename
        if (!DataUtil.isNullObject(configReport.getDatabaseName())) {
            predicates.add(cb.equal(root.get(ConfigReport_.DATABASE_NAME), configReport.getDatabaseName()));
            predicatesCount.add(cb.equal(root.get(ConfigReport_.DATABASE_NAME), configReport.getDatabaseName()));
        }
        // Tim kiem EQUAL TableName
        if (!DataUtil.isNullObject(configReport.getTableName())) {
            predicates.add(cb.equal(root.get(ConfigReport_.TABLE_NAME), configReport.getTableName()));
            predicatesCount.add(cb.equal(root.get(ConfigReport_.TABLE_NAME), configReport.getTableName()));
        }
        // Tim kiem EQUAL UNIT
        if (!DataUtil.isNullObject(configReport.getUnit())) {
            predicates.add(cb.equal(root.get(ConfigReport_.UNIT), configReport.getUnit()));
            predicatesCount.add(cb.equal(root.get(ConfigReport_.UNIT), configReport.getUnit()));
        }


        criteria.select(root);
        countCrit.select(cb.count(rootCnt));

        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        List<ConfigReport> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        countCrit.where(cb.and(predicatesCount.toArray(new Predicate[predicatesCount.size()])));
        Long count = entityManager.createQuery(countCrit).getSingleResult();

        List<ConfigReportDTO> rsDTOs = rs.stream().map(configReportMapper::toDto).peek(
            configReportDTO -> {
                configReportDTO.setInputLevelName(mapInputLevel.get(configReportDTO.getInputLevel()));
                configReportDTO.setTimeTypeName(mapTimeType.get(configReportDTO.getTimeType()));
                configReportDTO.setStatusName(mapStatus.get(configReportDTO.getStatus()));
                if (mapDomain != null && mapDomain.containsKey(configReportDTO.getDomainCode())) {
                    configReportDTO.setDomainName(mapDomain.get(configReportDTO.getDomainCode()));
                }
                if (mapUnit != null && mapUnit.containsKey(configReportDTO.getUnit())) {
                    configReportDTO.setUnitName(mapUnit.get(configReportDTO.getUnit()));
                }
            }
        ).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigReportDTO> findMainData(ConfigReportForm configReport) {
        List<ConfigReport> configReports = configReportRepository.findAll();
        List<ConfigReport> rs = new ArrayList<>();
        List<ConfigReport> rsTmp = new ArrayList<>();
        Map<Integer, String> mapStatus = com.b4t.app.commons.StringUtils.getMapStatus();
        Map<String, String> mapDomain = getMapByCategory(Constants.CATEGORY.DOMAIN_CATEOGRY);
        Map<String, String> mapUnit = getMapByCategory(Constants.CATEGORY.UNIT_IMPORT_CATEOGRY);
        for (int i = 0; i < configReports.size(); i++) {
            if (configReports.get(i).getMainData() != null && configReports.get(i).getMainData() == 1) {
                rs.add(configReports.get(i));
            }
            rsTmp.add(configReports.get(i));
        }
        if (rs.size() == 0) {
            rs = rsTmp;
        }
        List<ConfigReportDTO> rsDTOs = rs.stream().map(configReportMapper::toDto).peek(
            configReportDTO -> {
                configReportDTO.setInputLevelName(mapInputLevel.get(configReportDTO.getInputLevel()));
                configReportDTO.setTimeTypeName(mapTimeType.get(configReportDTO.getTimeType()));
                configReportDTO.setStatusName(mapStatus.get(configReportDTO.getStatus()));
                if (mapDomain != null && mapDomain.containsKey(configReportDTO.getDomainCode())) {
                    configReportDTO.setDomainName(mapDomain.get(configReportDTO.getDomainCode()));
                }
                if (mapUnit != null && mapUnit.containsKey(configReportDTO.getUnit())) {
                    configReportDTO.setUnitName(mapUnit.get(configReportDTO.getUnit()));
                }
            }
        ).collect(Collectors.toList());

        Collections.sort(rsDTOs, new Comparator<ConfigReportDTO>() {
            @Override
            public int compare(ConfigReportDTO o1, ConfigReportDTO o2) {
                return o2.getUpdateTime().compareTo(o1.getUpdateTime());
            }
        });
        return rsDTOs;
    }

    /**
     * Get one configReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigReportDTO> findOne(Long id) {
        log.debug("Request to get ConfigReport : {}", id);
        return configReportRepository.findById(id)
            .map(configReportMapper::toDto);
    }

    /**
     * Delete the configReport by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete ConfigReport : {}", id);
        configReportRepository.deleteById(id);
        List<ConfigReportColumn> configReportColumns = configReportColumnService.findAllByReportIdEquals(id);
        if (!DataUtil.isNullOrEmpty(configReportColumns)) {
            configReportColumnService.delete(configReportColumns);
        }
    }

    /**
     * Kiem tra xem bang da duoc cau hinh hay chua
     *
     * @param databaseName
     * @param tableName
     * @return
     */
    @Override
    public Optional<ConfigReport> findByDatabaseAndTable(String databaseName, String tableName) {
        return configReportRepository.findFirstByDatabaseNameIgnoreCaseAndTableNameIgnoreCase(databaseName, tableName);
    }


    /**
     * Tai file template import
     *
     * @param reportId
     * @return
     */
    @Override
    public File downloadTemplate(Long reportId) {
        File file;
        Optional<ConfigReportDTO> configReportDTO = configReportRepository.findById(reportId).map(configReportMapper::toDto);
        if (!configReportDTO.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNotExisted"), ENTITY_NAME, "configReport.idNotExisted");
        }
        ConfigReportDTO configReport = configReportDTO.get();
        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(reportId);

        List<ConfigReportColumnDTO> lstColumnCurrent = DataUtil.getListShowColumByReportIdIgnoreKey(lstAllColumn);
        ConfigReportDTO configReportDTOFormInput = configReportUtilsService.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTOFormInput.getformInput();
        if (Constants.formInputYear.equals(formInput)) {
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_1", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 1"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_2", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 2"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_3", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 3"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_I", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý I"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_4", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 4"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_5", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 5"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_6", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 6"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_II", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý II"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_7", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 7"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_8", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 8"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_9", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 9"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_III", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý III"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_10", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 10"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_11", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 11"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_12", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 12"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_IV", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý IV"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("year", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Cả năm"));
            lstColumnCurrent.remove(0);
            lstColumnCurrent.remove(0);
        }

        List<ExcelColumn> lstColumn = DataUtil.getListExcelColumn(lstColumnCurrent);


        try {
            file = excelUtils.downloadTemplate(lstColumn, configReport.getTitle(),
                configReport.getTableName(), Constants.FILE_IMPORT.START_ROW, Constants.FILE_IMPORT.START_COL);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestAlertException(Translator.toLocale("error.title"), ENTITY_NAME, "title");
        }
        return file;
    }

    /**
     * API cung cap de import du lieu
     *
     * @param configReportImportInputDTO
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ConfigReportImportDetailDTO updateDataApi(ConfigReportImportDetailDTO configReportImportInputDTO) throws Exception {
        String userName = SecurityUtils.getCurrentUserLogin().orElseGet(null);
        ConfigReport configReport = configReportRepository.getOne(configReportImportInputDTO.getConfigReportImportDTO().getReportId());
        Optional<User> user = userService.getUserByLogin(userName);
//        if(user.get().getUnitLeader() == 0){
//            for (Map<String,String> lst:configReportImportInputDTO.getConfigReportDataImport().getMapData()) {
//                lst.put("status", "0");
//            };
//        }
        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(configReportImportInputDTO.getConfigReportImportDTO().getReportId());

        //Danh sach cac cot show len cho nguoi dung nhap de Import
        List<ConfigReportColumnDTO> lstShowColumnConfig = DataUtil.getListShowColumByReportId(lstAllColumn);


        ConfigReportImportDetailDTO result = updateData(configReportImportInputDTO.getConfigReportImportDTO(),
            configReportImportInputDTO.getConfigReportDataImport(), configReport, lstShowColumnConfig, lstAllColumn, false);
        return result;
    }

    /**
     * Function import du lieu
     *
     * @param configReportImportDTO
     * @param configReportDataImport
     * @param configReport
     * @return
     * @throws Exception
     */
    public ConfigReportImportDetailDTO updateData(ConfigReportImportDTO configReportImportDTO, ConfigReportDataImport configReportDataImport,
                                                  ConfigReport configReport, List<ConfigReportColumnDTO> lstShowColumnConfig,
                                                  List<ConfigReportColumnDTO> lstAllColumn, boolean isImport) throws Exception {
        ConfigReportImportDetailDTO result = new ConfigReportImportDetailDTO();
        String actor = SecurityUtils.getCurrentUserLogin().get();

        //Danh sach cot cot hidden
        List<ConfigReportColumnDTO> lstHiddenColumnConfig = DataUtil.getListHiddenColumByReportId(lstAllColumn);
        List<ConfigReportColumnDTO> lstAllColumnIgnoreKey = lstAllColumn.stream().filter(e -> !Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey())).collect(Collectors.toList());
        Optional<ConfigReportColumnDTO> columnKey = lstAllColumn.stream().filter(e -> Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey())).findFirst();

        long reportId = configReportImportDTO.getReportId();
        ConfigReportDTO configReportDTO = configReportUtilsService.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTO.getformInput();
        if (isImport) {
            if (Constants.formInputYear.equals(formInput)) {
//                lstShowColumnConfig.add(new ConfigReportColumnDTO("dept_permission_code", 0, "admin", "STRING", 0, 1, 0, 0, 650, 0, reportId, 1, "Mã phân quyền đơn vị"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_1", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 1"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_2", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 2"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_3", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 3"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("quy_I", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý I"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_4", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 4"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_5", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 5"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_6", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 6"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("quy_II", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý II"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_7", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 7"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_8", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 8"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_9", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 9"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("quy_III", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý III"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_10", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 10"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_11", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 11"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("thang_12", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 12"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("quy_IV", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý IV"));
                lstShowColumnConfig.add(new ConfigReportColumnDTO("year", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Cả năm"));
                lstShowColumnConfig.remove(0);
                lstShowColumnConfig.remove(0);
            }
            DataUtil.validateTemplate(lstShowColumnConfig, configReportDataImport.getHeaders(), ENTITY_NAME, configReportDataImport.getLstData());
        }
        String selectedYear = DataUtil.getTimeValue(Date.from(configReportImportDTO.getImportTime()), Integer.valueOf(configReport.getTimeType()));
        if (Constants.formInputYear.equals(formInput) && !isImport) {
            Map<String, String> mapYear = new HashMap<>();
            configReportDataImport.getMapData().add(mapYear);
            mapYear.put("selectedYear", selectedYear);
        }
        Set<Object> setTimeColumn = new TreeSet<>();
        Map<String, ConfigReportColumnDTO> mapColumn = lstShowColumnConfig.stream().collect(Collectors.toMap(ConfigReportColumnDTO::getColumnName, Function.identity(), (o1, o2) -> o1));

        List<Map<String, String>> lstMapData = collectData(mapColumn,
            configReportDataImport.getMapData(), lstHiddenColumnConfig, configReport, configReportImportDTO, setTimeColumn, actor);
        List<Map<String, String>> lstMapDataOK = lstMapData.stream().filter(e -> e.get(Constants.RESULT_VALIDATE_MSG).equalsIgnoreCase(Constants.VALIDATE_OK)).collect(Collectors.toList());
        List<Map<String, String>> lstMapDataError = lstMapData.stream().filter(e -> !e.get(Constants.RESULT_VALIDATE_MSG).equalsIgnoreCase(Constants.VALIDATE_OK)).collect(Collectors.toList());

        if (configReportDataImport.getMapData().get(0).containsKey("thang_1")) {
            lstMapDataOK = configReportDataImport.getMapData();
            lstMapDataError = new ArrayList<>();
        }
        List<Map<String, String>> lstData = new ArrayList<>();
        if (Constants.formInputYear.equals(formInput) && isImport) {
            Map<String, String> mapImportItem = new LinkedHashMap<>();
            for (int i = 0; i < configReportDataImport.getLstData().size(); i++) {
                for (int j = 0; j < lstShowColumnConfig.size(); j++) {
                    mapImportItem.put(lstShowColumnConfig.get(j).getColumnName(), configReportDataImport.getLstData().get(i).get(j));
                }
                lstData.add(mapImportItem);
                mapImportItem = new LinkedHashMap<>();
            }
            Map<String, String> mapYear = new HashMap<>();
            mapYear.put("selectedYear", selectedYear);
            lstData.add(mapYear);
            lstMapDataOK = lstData;
            lstMapDataError = new ArrayList<>();
        }

        if (DataUtil.isNullOrEmpty(lstMapDataError)) {
            List<Map<String, String>> lstMapDataInsert;
            List<Map<String, String>> lstMapDataUpdate = null;
            if (columnKey.isPresent()) {
                if (configReportDataImport.getMapData().get(0).containsKey("thang_1")) {
                    lstMapDataUpdate = configReportDataImport.getMapData();
                    lstMapDataInsert = configReportDataImport.getMapData();

                } else {
                    ConfigReportColumnDTO keyColumn = columnKey.get();
                    lstMapDataUpdate = lstMapDataOK.stream().filter(e -> !DataUtil.isNullOrEmpty(e.get(keyColumn.getColumnName()))).collect(Collectors.toList());
                    lstMapDataInsert = lstMapDataOK.stream().filter(e -> DataUtil.isNullOrEmpty(e.get(keyColumn.getColumnName()))).collect(Collectors.toList());
                }
            } else {
                lstMapDataInsert = lstMapDataOK;
            }
            if (!Constants.formInputYear.equals(formInput)) {
                if (!DataUtil.isNullOrEmpty(lstMapDataUpdate)) {
                    configReportImportRepository.importUpdateData(lstMapDataUpdate, lstAllColumnIgnoreKey, columnKey.get(), configReport);
//                    dataLogRepositoryImpl.insertDataLogUpdate(lstMapDataUpdate, lstAllColumnIgnoreKey, configReport, 2, lstOldData);
                }
            } else {
                if (!DataUtil.isNullOrEmpty(lstMapDataUpdate)) {
                    configReportImportRepository.importUpdateDataFormInput(lstMapDataUpdate, lstAllColumnIgnoreKey, configReport);
                }

            }
            if (!Constants.formInputYear.equals(formInput)) {
                if (!DataUtil.isNullOrEmpty(lstMapDataInsert)) {
                    log.info("Total row validate OK:" + lstMapDataOK.size() + " Total row validate Error:" + lstMapDataError.size());
                    configReportImportRepository.importData(lstMapDataInsert, lstAllColumnIgnoreKey, configReport);
//                    dataLogRepositoryImpl.insertDataLogAdd(lstMapDataInsert, lstAllColumnIgnoreKey, configReport, 1);
                }
            } else {
                if (!DataUtil.isNullOrEmpty(lstMapDataInsert) && isImport) {
                    configReportImportRepository.importUpdateDataFormInputExcel(lstMapDataInsert, lstAllColumnIgnoreKey, configReport, selectedYear);
                }
            }
            configReportImportDTO.setResult("success");
        } else {
            configReportImportDTO.setResult("error");
        }

        //Truong hop import du lieu thanh cong
        if (DataUtil.isNullOrEmpty(lstMapDataError)) {
            FlagRunQueryKpiDTO flagDTO = new FlagRunQueryKpiDTO();
            flagDTO.setTableName(configReport.getDatabaseName() + "." + configReport.getTableName());
            if (configReportImportDTO.getImportTime() != null) {
                flagDTO.setPrdId(Long.valueOf(DataUtil.dateToString(Date.from(configReportImportDTO.getImportTime()), "yyyyMMdd")));
            } else if (!setTimeColumn.isEmpty()) {
                Object[] lstTime = setTimeColumn.toArray();
                Object timePrdId = lstTime[0];
                flagDTO.setPrdId(DataUtil.buildPrdId(timePrdId, configReport.getTimeType()));
            }
            flagRunQueryKpiService.save(flagDTO);

        }
        result.setConfigReportImportDTO(configReportImportDTO);
        ConfigReportDataImport dataImport = new ConfigReportDataImport();
        dataImport.setMapData(lstMapData);
        result.setConfigReportDataImport(dataImport);
        return result;
    }

    /**
     * Import du lieu tu file
     *
     * @param configReportImportDTO
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public File importFile(ConfigReportImportDTO configReportImportDTO) throws Exception {
        ConfigReport configReport = configReportRepository.getOne(configReportImportDTO.getReportId());
        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(configReportImportDTO.getReportId())
            .stream().filter(e -> !Constants.IS_PRIMARY_KEY.equals(e.getIsPrimaryKey())).collect(Collectors.toList());

        //Danh sach cac cot show len cho nguoi dung nhap de Import
        List<ConfigReportColumnDTO> lstShowColumnConfig = DataUtil.getListShowColumByReportIdIgnoreKey(lstAllColumn);
        Map<String, String> mapTitleColumn = lstShowColumnConfig.stream().collect(Collectors.toMap(ConfigReportColumnDTO::getTitle, ConfigReportColumnDTO::getColumnName));

        ConfigReportDataImport configReportDataImport = excelUtils.readImportFile(configReportImportDTO.getImportFile(), Constants.FILE_IMPORT.START_ROW, Constants.FILE_IMPORT.START_COL, mapTitleColumn);

        ConfigReportDataForm configReportDataForm = new ConfigReportDataForm();
        configReportDataForm.setReportId(configReportImportDTO.getReportId());
        configReportDataForm.setDataTime(configReportImportDTO.getImportTime());

//        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
//        ConfigReportDataDTO configReportDataDTO = this.findDataConfigReport(configReportDataForm, pageable);

        ConfigReportImportDetailDTO configReportImportResultDTO = this.updateData(configReportImportDTO, configReportDataImport, configReport, lstShowColumnConfig, lstAllColumn, true);
        File fileResult = excelUtils.writeImportFileResult(configReportImportDTO.getImportFile(), Constants.FILE_IMPORT.START_ROW, Constants.FILE_IMPORT.START_COL, configReportImportResultDTO.getConfigReportDataImport().getMapData(), configReport.getTableName());

        return fileResult;
    }

    /**
     * @param mapColumn
     * @param lstData
     * @param lstHiddenColumnConfig
     * @param configReport
     * @param configReportImportDTO
     * @return
     */
    private List<Map<String, String>> collectData(Map<String, ConfigReportColumnDTO> mapColumn, List<Map<String, String>> lstData,
                                                  List<ConfigReportColumnDTO> lstHiddenColumnConfig, ConfigReport configReport,
                                                  ConfigReportImportDTO configReportImportDTO, Set<Object> setTimeColumn, String actor) {
        List<Map<String, String>> lstMapData = new ArrayList<>();

        ConfigReportColumnDTO configColumn;
        for (Map<String, String> rowData : lstData) {
            StringBuilder error = new StringBuilder("");
            String validate;
            Map<String, String> mapData = new HashMap<>();
            log.info("size of rowData:" + rowData.size() + " size of lstColumnConfig:" + mapColumn.size());

            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                String value = entry.getValue();
                configColumn = mapColumn.get(entry.getKey());
                if (configColumn != null) {
//                    if (configColumn.getIsPrimaryKey() != null && configColumn.getIsPrimaryKey() == 1) {
//                        continue;
//                    }
                    if (DataUtil.isNullOrEmpty(value) && !DataUtil.isNullOrEmpty(configColumn.getDefaultValue()) &&
                        !Constants.IS_REQUIRED.equals(configColumn.getIsRequire())) {
                        value = configColumn.getDefaultValue();
                    }
                    if (Constants.IS_TIME_COLUMN.equals(configColumn.getIsTimeColumn())) {
                        setTimeColumn.add(value);
                    }
                    if (Constants.DATA_DB_TYPE.TIMESTAMP.equalsIgnoreCase(configColumn.getColumnName())) {
                        mapData.put(configColumn.getColumnName(), String.valueOf(new Date().getTime()));
                    } else {
                        mapData.put(configColumn.getColumnName(), value);
                    }

                    if (Constants.UPDATE_TIME.equalsIgnoreCase(configColumn.getColumnName()) && Constants.DATA_TYPE.DATE.equals(configColumn.getDataType())) {
                        value = DataUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss");
                        mapData.put(configColumn.getColumnName(), value);
                    }
                    if (Constants.UPDATE_USER.equalsIgnoreCase(configColumn.getColumnName())) {
                        value = actor;
                        mapData.put(configColumn.getColumnName(), value);
                    }

                    validate = DataUtil.validateValue(value, configColumn);
                    if (!Constants.VALIDATE_OK.equalsIgnoreCase(validate)) {
                        error.append(validate).append(";");
                    }
                } else {
                    error.append(Translator.toLocale("error.configReport.couldNotFound") + " " + entry.getKey()).append(";");
                }
            }

            if (DataUtil.isNullOrEmpty(error)) {
                error.append(Constants.VALIDATE_OK);
            } else {
                error = error.deleteCharAt(error.length() - 1);
            }
            mapData.put(Constants.RESULT_VALIDATE_MSG, error.toString());
            if (Constants.VALIDATE_OK.equalsIgnoreCase(error.toString())) {
                if (!DataUtil.isNullOrEmpty(lstHiddenColumnConfig)) {
                    lstHiddenColumnConfig.forEach(e -> {
                        if (Constants.IS_TIME_COLUMN.equals(e.getIsTimeColumn())) {
                            mapData.put(e.getColumnName(), configReportUtilsService.getTimeValue(configReportImportDTO, configReport));
                        }
                        if (Constants.DATA_DB_TYPE.TIMESTAMP.equalsIgnoreCase(e.getColumnName())) {
                            mapData.put(e.getColumnName(), String.valueOf(new Date().getTime()));
                        }
                        if (Constants.UPDATE_TIME.equalsIgnoreCase(e.getColumnName()) && Constants.DATA_TYPE.DATE.equals(e.getDataType())) {
                            mapData.put(e.getColumnName(), DataUtil.dateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
                        }
                        if (Constants.UPDATE_USER.equalsIgnoreCase(e.getColumnName())) {
                            mapData.put(e.getColumnName(), actor);
                        }
                    });
                }
            }

            lstMapData.add(mapData);
        }
        return lstMapData;
    }


    /**
     * Tim kiem phan trang
     *
     * @param configReportDataForm
     * @param lstAllColumn
     * @param pageable
     * @return
     */
    private Page<Object> findDataConfigReportPaging(ConfigReportDataForm configReportDataForm, List<ConfigReportColumnDTO> lstAllColumn, Pageable pageable) {
        //Lay thong tin cau hinh
        ConfigReport configReport = configReportRepository.getOne(configReportDataForm.getReportId());

        ConfigReportColumnDTO primaryKey = getPrimaryKeyColumn(configReportMapper.toDto(configReport));
        String primaryKeyColumn = null;
        if (primaryKey != null) {
            primaryKeyColumn = primaryKey.getColumnName();
        }
        Page<Object> pageObj = null;
        //Lay cac bang cau hinh luu time
        ConfigReportColumnDTO columnTime = configReportUtilsService.getColumTimeName(configReportDataForm.getReportId());
        long reportId = configReportDataForm.getReportId();
        ConfigReportDTO configReportDTO = configReportUtilsService.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTO.getformInput();
        if (!Constants.formInputYear.equals(formInput)) {
            pageObj = configReportImportRepository.findDataConfigReport(configReportDataForm, lstAllColumn, configReport, pageable, columnTime.getColumnName(), primaryKeyColumn);
        } else {
            pageObj = configReportImportRepository.findDataConfigReportFormInput(configReportDataForm, lstAllColumn, configReport, pageable, columnTime.getColumnName(), primaryKeyColumn);
        }
        return pageObj;
    }

    /**
     * Tim kiem phan trang du lieu
     *
     * @param configReportDataForm
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigReportDataDTO findDataConfigReport(ConfigReportDataForm configReportDataForm, Pageable pageable) {
        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(configReportDataForm.getReportId());
        lstAllColumn = DataUtil.getListShowColumByReportId(lstAllColumn);

        Long reportId = lstAllColumn.get(0).getReportId();

        Page<Object> pageObj = findDataConfigReportPaging(configReportDataForm, lstAllColumn, pageable);
        ConfigReportDTO configReportDTO = configReportUtilsService.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTO.getformInput();
        if (Constants.formInputYear.equals(formInput)) {

            lstAllColumn.add(new ConfigReportColumnDTO("thang_1", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 1"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_2", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 2"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_3", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 3"));
            lstAllColumn.add(new ConfigReportColumnDTO("quy_I", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Quý I"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_4", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 4"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_5", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 5"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_6", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 6"));
            lstAllColumn.add(new ConfigReportColumnDTO("quy_II", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Quý II"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_7", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 7"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_8", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 8"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_9", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 9"));
            lstAllColumn.add(new ConfigReportColumnDTO("quy_III", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Quý III"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_10", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 10"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_11", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 11"));
            lstAllColumn.add(new ConfigReportColumnDTO("thang_12", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Tháng 12"));
            lstAllColumn.add(new ConfigReportColumnDTO("quy_IV", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Quý IV"));
            lstAllColumn.add(new ConfigReportColumnDTO("year", 0, "admin", "DOUBLE", 0, 0, 1, 0, 20, 0, reportId, 1, "Cả năm"));
        }
        ConfigReportDataDTO result = new ConfigReportDataDTO();

        result.setLstColumn(lstAllColumn);
        result.setPageObj(pageObj);
        result.setLstObj(pageObj.getContent());

        return result;
    }

    public ConfigReportDataDTO getConfigReport(Long reportId) {
        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(reportId);
        lstAllColumn = DataUtil.getListShowColumByReportId(lstAllColumn);
        List<ConfigReportColumnDTO> lstRef = lstAllColumn.stream().filter(e -> !DataUtil.isNullOrEmpty(e.getRefData())).collect(Collectors.toList());
        Map<String, Object> mapRef = null;
        try {
            mapRef = configReportImportRepository.buildRefData(lstRef);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        List<Object> objectList = new ArrayList<>();
        ConfigReportDetailDTO configReportDetailDTO = configReportUtilsService.getReportDetail(reportId).get();
        objectList.add(configReportDetailDTO.getConfigReport());


        ConfigReportColumnDTO configReportColumnDTO = new ConfigReportColumnDTO();
        for (ConfigReportColumnDTO configReportColumn : lstAllColumn) {
            if (configReportColumn.getIsShow() == 0) {
                configReportColumnDTO = configReportColumn;
            }
        }
        lstAllColumn.remove(configReportColumnDTO);
        ConfigReportDataDTO result = new ConfigReportDataDTO();
        result.setLstColumn(lstAllColumn);
        result.setMapRef(mapRef);
        result.setLstObj(objectList);
        return result;
    }

    /**
     * Export ket qua tim kiem theo thoi gian
     *
     * @param configReportDataForm
     * @return
     * @throws Exception
     */
    @Override
    public File exportDataConfigReports(ConfigReportDataForm configReportDataForm) throws Exception {
        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(configReportDataForm.getReportId());
        lstAllColumn = DataUtil.getListShowColumByReportIdIgnoreKey(lstAllColumn);
        List<ConfigReportColumnDTO> lstColumnCurrent = new ArrayList<>(lstAllColumn);
        Long reportId = lstAllColumn.get(0).getReportId();
        ConfigReportDTO configReportDTO = configReportUtilsService.getReportDetail(reportId).get().getConfigReport();
        String formInput = configReportDTO.getformInput();
        if (Constants.formInputYear.equals(formInput)) {
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_1", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 1"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_2", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 2"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_3", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 3"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_I", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý I"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_4", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 4"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_5", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 5"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_6", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 6"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_II", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý II"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_7", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 7"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_8", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 8"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_9", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 9"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_III", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý III"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_10", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 10"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_11", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 11"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("thang_12", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Tháng 12"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("quy_IV", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Quý IV"));
            lstColumnCurrent.add(new ConfigReportColumnDTO("year", 0, "admin", "DOUBLE", 0, 1, 1, 0, 20, 0, reportId, 1, "Cả năm"));
        }
        List<ExcelColumn> lstColumn = DataUtil.getListExcelColumn(lstColumnCurrent);

        ConfigReport configReport = configReportRepository.findById(configReportDataForm.getReportId()).get();

        //Lay cac bang cau hinh luu time
        ConfigReportColumnDTO columnTime = configReportUtilsService.getColumTimeName(configReportDataForm.getReportId());

        ConfigReportColumnDTO primaryKey = getPrimaryKeyColumn(configReportMapper.toDto(configReport));
        String primaryKeyColumn = null;
        if (primaryKey != null) {
            primaryKeyColumn = primaryKey.getColumnName();
        }
        List<Object> lstData = null;
        List<Object> lstDataTmp = new ArrayList<>();
        if (Constants.formInputYear.equals(formInput)) {
            lstData = configReportImportRepository.findDataConfigReportFormInput(configReportDataForm, DataUtil.getListShowColumByReportId(configReportColumnService.findAllByReportId(configReportDataForm.getReportId())), configReport, null, columnTime.getColumnName(), false, primaryKeyColumn);




            for (int i = 0; i < lstData.size(); i++) {
                Object[] lstObj = Arrays.stream((Object[]) lstData.get(i)).toArray();
                lstObj = removeTheElement(lstObj, 0);
                lstObj = removeTheElement(lstObj, 0);
                lstObj = removeTheElement(lstObj, 1);
                lstDataTmp.add(lstObj);
            }
            lstColumn.remove(0);
            lstColumn.remove(0);
            lstData = lstDataTmp;

        } else {
            lstData = configReportImportRepository.findDataConfigReport(configReportDataForm, lstColumnCurrent, configReport, null, columnTime.getColumnName(), false, primaryKeyColumn);
        }

        File file = excelUtils.exportData(lstColumn, lstData, configReport.getTitle(), "export_" + configReport.getTableName(), Constants.FILE_IMPORT.START_ROW, Constants.FILE_IMPORT.START_COL);
        return file;
    }

    public Object[] removeTheElement(Object[] arr, int index) {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null || index < 0
            || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        Object[] anotherArray = new Object[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
    }

    /**
     * Xoa du lieu bang theo thoi gian
     *
     * @param configReportDataForm
     */
    @Override
    public void deleteDataConfigReports(ConfigReportDataForm configReportDataForm) {
        Optional<ConfigReport> configReport = configReportRepository.findById(configReportDataForm.getReportId());
        if (!configReport.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNotExisted"), ENTITY_NAME, "configReport.idNotExisted");
        }

        ConfigReportColumnDTO columnTime = configReportUtilsService.getColumTimeName(configReportDataForm.getReportId());

        try {
            configReportImportRepository.deleteDataConfigReports(configReportDataForm, columnTime.getColumnName(), configReport.get());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.deleteCantSuccess"), ENTITY_NAME, "configReport.deleteCantSuccess");

        }
    }

    /**
     * Xoa du lieu bang theo thoi gian
     *
     * @param configReportDataForm
     */
    @Override
    public void deleteDataConfigReportsFormInput(ConfigReportDataForm configReportDataForm) {
        Optional<ConfigReport> configReport = configReportRepository.findById(configReportDataForm.getReportId());
        if (!configReport.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNotExisted"), ENTITY_NAME, "configReport.idNotExisted");
        }

        ConfigReportColumnDTO columnTime = configReportUtilsService.getColumTimeName(configReportDataForm.getReportId());

        try {
            configReportImportRepository.deleteDataConfigReportsFormInput(configReportDataForm, columnTime.getColumnName(), configReport.get());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.deleteCantSuccess"), ENTITY_NAME, "configReport.deleteCantSuccess");

        }
    }

    /**
     * Xoa row nguoi dung chon tren grid
     *
     * @param configReportDataForm
     */
    @Override
    public void deleteRowDataConfigReports(ConfigReportImportForm configReportDataForm) throws Exception {
        Optional<ConfigReport> configReport = configReportRepository.findById(configReportDataForm.getReportId());
        if (!configReport.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNotExisted"), ENTITY_NAME, "configReport.idNotExisted");
        }
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport.get());
        ConfigReportColumnDTO primaryKey = getPrimaryKeyColumn(configReportDTO);
        if (!configReportDataForm.getMapValue().containsKey(primaryKey.getColumnName())) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.notContainId"), ENTITY_NAME, "configReport.notContainId");
        }

        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(configReportDataForm.getReportId());

        //Danh sach cac cot show len cho nguoi dung nhap de Import
        List<ConfigReportColumnDTO> lstShowColumnConfig = DataUtil.getListShowColumByReportId(lstAllColumn);
        Map<String, String> mapLogDelete = configReportImportRepository.deleteRowDataConfigReports(configReportDTO, configReportDataForm.getMapValue(), primaryKey);
//        dataLogRepositoryImpl.insertDataLogDelete(configReportDataForm.getMapValue(), configReportDTO, 3, mapLogDelete, lstAllColumn);

    }

    @Override
    public ConfigReportImportForm updateRowDataConfigReports(ConfigReportImportForm configReportDataForm) throws Exception {
        Optional<ConfigReport> configReport = configReportRepository.findById(configReportDataForm.getReportId());
        if (!configReport.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.configReport.idNotExisted"), ENTITY_NAME, "configReport.idNotExisted");
        }
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport.get());

        List<ConfigReportColumnDTO> lstAllColumn = configReportColumnService.findAllByReportId(configReportDataForm.getReportId());
        List<ConfigReportColumnDTO> lstColumn = DataUtil.getListColumByReportIdIgnoreKey(lstAllColumn);

        ConfigReportColumnDTO primaryKey = getPrimaryKeyColumn(configReportDTO);

        ConfigReportColumnDTO timeColumnName = configReportUtilsService.getColumTimeName(configReportDataForm.getReportId());

        if (timeColumnName != null && !Constants.IS_SHOW.equals(timeColumnName.getIsShow())
            && (!configReportDataForm.getMapValue().containsKey(timeColumnName.getColumnName()) || DataUtil.isNullOrEmpty(configReportDataForm.getMapValue().get(timeColumnName.getColumnName())))) {
            if (DataUtil.isNullOrEmpty(configReportDataForm.getDataTime())) {
                throw new BadRequestAlertException(Translator.toLocale("error.configReport.dataTimeNull"), ENTITY_NAME, "configReport.dataTimeNull");
            }
            String timeValue = DataUtil.getTimeValue(Date.from(configReportDataForm.getDataTime()), Integer.valueOf(configReportDTO.getTimeType()));
            configReportDataForm.getMapValue().put(timeColumnName.getColumnName(), timeValue);
        }

        DataUtil.updateRowDataConfigReportsDefaultValue(lstColumn, configReportDataForm);


        configReportUtilsService.validateUpdateRowDataConfigReports(lstColumn, configReportDataForm.getMapValue());

        String keyColumn = primaryKey.getColumnName();
        Object keyValue = configReportDataForm.getMapValue().get(keyColumn);


        if (!configReportDataForm.getMapValue().containsKey(primaryKey.getColumnName())
            || DataUtil.isNullObject(keyValue)) {
            //Truong hop insert ban ghi
            configReportDataForm.getMapValue().remove(keyColumn);
            configReportImportRepository.insertRowDataConfigReports(configReportDTO, configReportDataForm.getMapValue(), primaryKey.getColumnName());
            int id = configReportImportRepository.getIdPrimaryKey(configReportDTO, configReportDataForm.getMapValue(), primaryKey.getColumnName());
            configReportDataForm.getMapValue().put(primaryKey.getColumnName(), id);
        } else {
            //Truong hop cap nhat bai ban

            configReportDataForm.getMapValue().remove(keyColumn);
            configReportImportRepository.updateRowDataConfigReports(configReportDTO, configReportDataForm.getMapValue(), primaryKey, keyColumn, keyValue);
            configReportDataForm.getMapValue().put(keyColumn, keyValue);
        }

        try {
            if (timeColumnName != null) {
                Object timePrdId = configReportDataForm.getMapValue().get(timeColumnName.getColumnName());
                FlagRunQueryKpiDTO flagDTO = new FlagRunQueryKpiDTO();
                flagDTO.setTableName(configReportDTO.getDatabaseName() + "." + configReportDTO.getTableName());
                flagDTO.setPrdId(DataUtil.buildPrdId(timePrdId, configReportDTO.getTimeType()));
                flagRunQueryKpiService.save(flagDTO);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return configReportDataForm;
    }

    @Override
    public ConfigReportDataDTO findDataConfigReportFormInput(ConfigReportDataForm configReportDataForm, Pageable pageable) {
        return null;
    }
}
