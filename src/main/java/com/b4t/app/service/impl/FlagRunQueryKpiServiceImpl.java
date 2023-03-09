package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.service.FlagRunQueryKpiService;
import com.b4t.app.domain.FlagRunQueryKpi;
import com.b4t.app.repository.FlagRunQueryKpiRepository;
import com.b4t.app.service.dto.FlagRunQueryKpiDTO;
import com.b4t.app.service.mapper.FlagRunQueryKpiMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link FlagRunQueryKpi}.
 */
@Service
@Transactional
public class FlagRunQueryKpiServiceImpl implements FlagRunQueryKpiService {

    private final Logger log = LoggerFactory.getLogger(FlagRunQueryKpiServiceImpl.class);

    private final FlagRunQueryKpiRepository flagRunQueryKpiRepository;

    private final FlagRunQueryKpiMapper flagRunQueryKpiMapper;

    private static final String ENTITY_NAME = "flagRunQueryKpi";

    public FlagRunQueryKpiServiceImpl(FlagRunQueryKpiRepository flagRunQueryKpiRepository, FlagRunQueryKpiMapper flagRunQueryKpiMapper) {
        this.flagRunQueryKpiRepository = flagRunQueryKpiRepository;
        this.flagRunQueryKpiMapper = flagRunQueryKpiMapper;
    }

    /**
     * Save a flagRunQueryKpi.
     *
     * @param flagRunQueryKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FlagRunQueryKpiDTO save(FlagRunQueryKpiDTO flagRunQueryKpiDTO) {
        if (validateDuplicateFlag(flagRunQueryKpiDTO)) {
            return null;
        }
        log.debug("Request to save FlagRunQueryKpi : {}", flagRunQueryKpiDTO);
        FlagRunQueryKpi flagRunQueryKpi = flagRunQueryKpiMapper.toEntity(flagRunQueryKpiDTO);
        flagRunQueryKpi.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        flagRunQueryKpi.setStatus(Constants.STATUS_ACTIVE);
        flagRunQueryKpi.setTableName(flagRunQueryKpi.getTableName().toUpperCase());
        flagRunQueryKpi = flagRunQueryKpiRepository.save(flagRunQueryKpi);
        return flagRunQueryKpiMapper.toDto(flagRunQueryKpi);
    }

    public boolean validateDuplicateFlag(FlagRunQueryKpiDTO flagRunQueryKpiDTO) {
        Optional<FlagRunQueryKpi> opt;
        if (flagRunQueryKpiDTO.getKpiId() == null) {
            opt = flagRunQueryKpiRepository.findFirstByPrdIdAndTableNameAndStatus(flagRunQueryKpiDTO.getPrdId(), flagRunQueryKpiDTO.getTableName(), Constants.STATUS_ACTIVE);
        } else {
            opt = flagRunQueryKpiRepository.findFirstByPrdIdAndKpiIdAndTableNameAndStatus(flagRunQueryKpiDTO.getPrdId(), flagRunQueryKpiDTO.getKpiId(), flagRunQueryKpiDTO.getTableName(), Constants.STATUS_ACTIVE);
        }
        if (opt.isPresent()) {
            log.info("duplidate: " + opt.get().getTableName() + "," + flagRunQueryKpiDTO.getPrdId());
            return true;
        }
        return false;
    }

    public List<FlagRunQueryKpiDTO> saveAll(List<FlagRunQueryKpiDTO> flagRunQueryKpiDTOs) {
        if (DataUtil.isNullOrEmpty(flagRunQueryKpiDTOs)) return null;
        List<FlagRunQueryKpiDTO> listInsert = new ArrayList<>();
        flagRunQueryKpiDTOs.forEach(bean -> {
            if (!validateDuplicateFlag(bean)) {
                bean.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
                bean.setStatus(Constants.STATUS_ACTIVE);
                bean.setTableName(bean.getTableName().toUpperCase());
                listInsert.add(bean);
            }
        });
        List<FlagRunQueryKpi> lsBean = flagRunQueryKpiMapper.toEntity(listInsert);
        List<FlagRunQueryKpi> results = flagRunQueryKpiRepository.saveAll(lsBean);
        return flagRunQueryKpiMapper.toDto(results);
    }

    /**
     * Get all the flagRunQueryKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FlagRunQueryKpiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlagRunQueryKpis");
        return flagRunQueryKpiRepository.findAll(pageable)
            .map(flagRunQueryKpiMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<FlagRunQueryKpiDTO> findAllByStatus(Long status, Pageable pageable) {
        log.debug("Request to get all FlagRunQueryKpis");
        return flagRunQueryKpiRepository.findAllByStatus(status, pageable)
            .map(flagRunQueryKpiMapper::toDto);
    }

    /**
     * Get one flagRunQueryKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FlagRunQueryKpiDTO> findOne(Long id) {
        log.debug("Request to get FlagRunQueryKpi : {}", id);
        return flagRunQueryKpiRepository.findById(id)
            .map(flagRunQueryKpiMapper::toDto);
    }

    /**
     * Delete the flagRunQueryKpi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FlagRunQueryKpi : {}", id);
        flagRunQueryKpiRepository.deleteById(id);
    }
}
