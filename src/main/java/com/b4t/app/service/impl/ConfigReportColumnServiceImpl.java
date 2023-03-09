package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.service.CommonService;
import com.b4t.app.service.ConfigReportColumnService;
import com.b4t.app.domain.ConfigReportColumn;
import com.b4t.app.repository.ConfigReportColumnRepository;
import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.dto.ConfigReportDetailDTO;
import com.b4t.app.service.mapper.ConfigReportColumnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigReportColumn}.
 */
@Service
@Transactional
public class ConfigReportColumnServiceImpl implements ConfigReportColumnService {

    private final Logger log = LoggerFactory.getLogger(ConfigReportColumnServiceImpl.class);

    private final ConfigReportColumnRepository configReportColumnRepository;

    private final ConfigReportColumnMapper configReportColumnMapper;
    private final CommonService commonService;


    public ConfigReportColumnServiceImpl(ConfigReportColumnRepository configReportColumnRepository, ConfigReportColumnMapper configReportColumnMapper, CommonService commonService) {
        this.configReportColumnRepository = configReportColumnRepository;
        this.configReportColumnMapper = configReportColumnMapper;
        this.commonService = commonService;
    }

    @Override
    public List<ConfigReportColumnDTO> saveAll(List<ConfigReportColumnDTO> configReportColumnDTOs) {
        log.debug("Request to save configReportColumnDTOs : {}", configReportColumnDTOs);
        List<ConfigReportColumn> configReportColumn = configReportColumnMapper.toEntity(configReportColumnDTOs);
        configReportColumn = configReportColumnRepository.saveAll(configReportColumn);
        return configReportColumnMapper.toDto(configReportColumn);
    }

    /**
     * Save a configReportColumn.
     *
     * @param configReportColumnDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigReportColumnDTO save(ConfigReportColumnDTO configReportColumnDTO) {
        log.debug("Request to save ConfigReportColumn : {}", configReportColumnDTO);
        ConfigReportColumn configReportColumn = configReportColumnMapper.toEntity(configReportColumnDTO);
        configReportColumn = configReportColumnRepository.save(configReportColumn);
        return configReportColumnMapper.toDto(configReportColumn);
    }

    /**
     * Get all the configReportColumns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigReportColumnDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigReportColumns");
        return configReportColumnRepository.findAll(pageable)
            .map(configReportColumnMapper::toDto);
    }

    /**
     * Get one configReportColumn by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigReportColumnDTO> findOne(Long id) {
        log.debug("Request to get ConfigReportColumn : {}", id);
        return configReportColumnRepository.findById(id)
            .map(configReportColumnMapper::toDto);
    }

    /**
     * Delete the configReportColumn by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigReportColumn : {}", id);
        configReportColumnRepository.deleteById(id);
    }

    @Override
    public void delete(List<ConfigReportColumn> configReportColumns) {
        configReportColumnRepository.deleteAll(configReportColumns);
    }

    /**
     * Lay tat cac column cua config_report
     * @param reportId
     * @return
     */
    @Override
    public List<ConfigReportColumn> findAllByReportIdEquals(Long reportId) {
        return configReportColumnRepository.findAllByReportIdEquals(reportId);
    }

    /**
     * Lay danh sach cac config_report_column
     * @param reportId
     * @return
     */
    @Override
    public List<ConfigReportColumnDTO> findAllByReportId(Long reportId) {
        return findAllByReportIdEquals(reportId).stream().map(configReportColumnMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConfigReportColumnDTO> findAllByIsTimeColumnEquals(Integer isTimeColumn, Long reportId) {
        return configReportColumnRepository.findAllByIsTimeColumnEqualsAndReportIdEquals(isTimeColumn, reportId).stream().map(configReportColumnMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void compareColumn(ConfigReportDetailDTO configReportDetailDTO,  List<ConfigReportColumnDTO> lstColumnDb) {
        List<ConfigReportColumnDTO> lstColumnUpdate = configReportDetailDTO.getConfigReportColumns();
        List<ConfigReportColumnDTO> lstUpdate = new ArrayList<>();
        List<ConfigReportColumnDTO> lstAdd = new ArrayList<>();
        List<ConfigReportColumnDTO> lstDel = new ArrayList<>();
        List<String> lstColumnNameDb = lstColumnDb.stream().map(ConfigReportColumnDTO::getColumnName).collect(Collectors.toList());
        List<String> lstColumnNameUpdate = lstColumnUpdate.stream().map(ConfigReportColumnDTO::getColumnName).collect(Collectors.toList());
        for(ConfigReportColumnDTO column : lstColumnUpdate) {
            if(lstColumnNameDb.contains(column.getColumnName()) && !lstColumnDb.contains(column)) {
                lstUpdate.add(column);
            }
            if(!lstColumnNameDb.contains(column.getColumnName())) {
                lstAdd.add(column);
            }
        }
        for(ConfigReportColumnDTO columnDTO: lstColumnDb) {
            if(!lstColumnNameUpdate.contains(columnDTO.getColumnName()) && !"id".equalsIgnoreCase(columnDTO.getColumnName())) {
                lstDel.add(columnDTO);
            }
        }
        if (!DataUtil.isNullOrEmpty(lstUpdate)) {
            lstUpdate.forEach(e -> {
                commonService.alterModifyColumn(configReportDetailDTO, e);
            });
        }
        if(!DataUtil.isNullOrEmpty(lstAdd)) {
            lstAdd.forEach(e-> {
                commonService.alterAddColumn(configReportDetailDTO,e);
            });
        }
        if(!DataUtil.isNullOrEmpty(lstDel)) {
            lstDel.forEach(e-> {
                commonService.alterNotRequired(configReportDetailDTO,e);
            });
        }
        List<String> lstUniqueCol = configReportDetailDTO.getConfigReportColumns().stream().
            filter(e -> Integer.valueOf(1).equals(e.getColumnUnique())).map(ConfigReportColumnDTO::getColumnName).collect(Collectors.toList());
        List<String> lstUniqueColDb =  lstColumnDb.stream().filter(e-> Integer.valueOf(1).equals(e.getColumnUnique()))
            .map(ConfigReportColumnDTO::getColumnName).collect(Collectors.toList());

        List<String> lstUniqueMore = new ArrayList<>();
        if(!DataUtil.isNullOrEmpty(lstUniqueCol)) {
            lstUniqueMore = lstUniqueCol.stream().filter(e -> !lstUniqueColDb.contains(e)).collect(Collectors.toList());
        }
        if(DataUtil.isNullOrEmpty(lstUniqueMore) && !DataUtil.isNullOrEmpty(lstUniqueColDb)) {
            lstUniqueMore = lstUniqueColDb.stream().filter(e-> !lstUniqueCol.contains(e)).collect(Collectors.toList());
        }

        if(!DataUtil.getSize(lstUniqueCol).equals(DataUtil.getSize(lstUniqueColDb)) || !DataUtil.isNullOrEmpty(lstUniqueMore)) {
            //Truong hop cap nhat lai unique constraint
            commonService.dropUniqueConstraint(configReportDetailDTO);
        }
        String lstColumnUnique = configReportDetailDTO.getConfigReportColumns().stream().
            filter(e -> Integer.valueOf(1).equals(e.getColumnUnique())).map(ConfigReportColumnDTO::getColumnName).collect(Collectors.joining(","));

        if(!DataUtil.isNullOrEmpty(lstUniqueMore) && !DataUtil.isNullOrEmpty(lstColumnUnique)) {
            commonService.addUniqueConstraint(configReportDetailDTO, lstColumnUnique);
        }
    }
}
