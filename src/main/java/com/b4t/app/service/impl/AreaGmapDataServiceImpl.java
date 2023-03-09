package com.b4t.app.service.impl;

import com.b4t.app.config.Constants;
import com.b4t.app.service.AreaGmapDataService;
import com.b4t.app.domain.AreaGmapData;
import com.b4t.app.repository.AreaGmapDataRepository;
import com.b4t.app.service.dto.AreaAlarmDTO;
import com.b4t.app.service.dto.AreaGmapDataDTO;
import com.b4t.app.service.mapper.AreaGmapDataMapper;
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
 * Service Implementation for managing {@link AreaGmapData}.
 */
@Service
@Transactional
public class AreaGmapDataServiceImpl implements AreaGmapDataService {

    private final Logger log = LoggerFactory.getLogger(AreaGmapDataServiceImpl.class);

    private final AreaGmapDataRepository areaGmapDataRepository;

    private final AreaGmapDataMapper areaGmapDataMapper;

    public AreaGmapDataServiceImpl(AreaGmapDataRepository areaGmapDataRepository, AreaGmapDataMapper areaGmapDataMapper) {
        this.areaGmapDataRepository = areaGmapDataRepository;
        this.areaGmapDataMapper = areaGmapDataMapper;
    }

    /**
     * Save a areaGmapData.
     *
     * @param areaGmapDataDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AreaGmapDataDTO save(AreaGmapDataDTO areaGmapDataDTO) {
        log.debug("Request to save AreaGmapData : {}", areaGmapDataDTO);
        AreaGmapData areaGmapData = areaGmapDataMapper.toEntity(areaGmapDataDTO);
        areaGmapData = areaGmapDataRepository.save(areaGmapData);
        return areaGmapDataMapper.toDto(areaGmapData);
    }

    /**
     * Get all the areaGmapData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AreaGmapDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AreaGmapData");
        return areaGmapDataRepository.findAll(pageable)
            .map(areaGmapDataMapper::toDto);
    }

    /**
     * Get one areaGmapData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AreaGmapDataDTO> findOne(Long id) {
        log.debug("Request to get AreaGmapData : {}", id);
        return areaGmapDataRepository.findById(id)
            .map(areaGmapDataMapper::toDto);
    }

    /**
     * Delete the areaGmapData by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AreaGmapData : {}", id);
        areaGmapDataRepository.deleteById(id);
    }

    public List<AreaGmapDataDTO> findAllByParentCodeAndStatus(AreaGmapDataDTO areaGmapDataDTO) {
        return areaGmapDataRepository.findAllByParentCodeAndStatus(areaGmapDataDTO.getParentCode(), Constants.STATUS_ACTIVE).stream().map(areaGmapDataMapper::toDto).collect(Collectors.toList());
    }

    public List<AreaGmapDataDTO> findByObjectCode(AreaGmapDataDTO areaGmapDataDTO) {
        return areaGmapDataRepository.findAllByObjectCodeAndStatus(areaGmapDataDTO.getObjectCode(), Constants.STATUS_ACTIVE).stream().map(areaGmapDataMapper::toDto).collect(Collectors.toList());
    }

    public List<AreaAlarmDTO> getAreaAlarm(AreaGmapDataDTO areaGmapDataDTO) {
        List<AreaAlarmDTO> results = new ArrayList<>();
        List<AreaGmapDataDTO> lstArea = areaGmapDataRepository.findAllByParentCodeAndStatus(areaGmapDataDTO.getParentCode(), Constants.STATUS_ACTIVE).stream().map(areaGmapDataMapper::toDto).collect(Collectors.toList());
        AreaAlarmDTO areaAlarmDTO;
        for (AreaGmapDataDTO areaGmapData : lstArea) {
            areaAlarmDTO = new AreaAlarmDTO(areaGmapData);
            areaAlarmDTO.setAlarmLevel(1L);
            results.add(areaAlarmDTO);
        }
        return results;
    }
}
