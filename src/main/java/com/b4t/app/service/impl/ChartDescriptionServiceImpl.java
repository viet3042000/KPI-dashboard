package com.b4t.app.service.impl;

import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ChartDescriptionService;
import com.b4t.app.domain.ChartDescription;
import com.b4t.app.repository.ChartDescriptionRepository;
import com.b4t.app.service.dto.ChartDescriptionDTO;
import com.b4t.app.service.mapper.ChartDescriptionCustomMapper;
import com.b4t.app.service.mapper.ChartDescriptionMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ChartDescription}.
 */
@Service
@Transactional
public class ChartDescriptionServiceImpl implements ChartDescriptionService {

    private final Logger log = LoggerFactory.getLogger(ChartDescriptionServiceImpl.class);

    private final ChartDescriptionRepository chartDescriptionRepository;

    private final ChartDescriptionCustomMapper chartDescriptionCustomMapper;

    private static final String ENTITY_NAME = "Chart Description";

    public ChartDescriptionServiceImpl(ChartDescriptionRepository chartDescriptionRepository, ChartDescriptionCustomMapper chartDescriptionCustomMapper) {
        this.chartDescriptionRepository = chartDescriptionRepository;
        this.chartDescriptionCustomMapper = chartDescriptionCustomMapper;
    }

    /**
     * Save a chartDescription.
     *
     * @param chartDescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChartDescriptionDTO createNew(ChartDescriptionDTO chartDescriptionDTO) {
        log.debug("Request to create new ChartDescription : {}", chartDescriptionDTO);
        String login = SecurityUtils.getCurrentUserLogin().get();
        chartDescriptionDTO.setCreatedBy(login);
        chartDescriptionDTO.setCreatedTime(Instant.now());
        chartDescriptionDTO.setModifiedBy(login);
        chartDescriptionDTO.setModifiedTime(Instant.now());
        chartDescriptionDTO.setStatus(1);
        ChartDescription chartDescription = chartDescriptionCustomMapper.chartDescriptionDTOToChartDescription(chartDescriptionDTO);
        chartDescription = chartDescriptionRepository.save(chartDescription);
        return chartDescriptionCustomMapper.chartDescriptionToChartDescriptionDTO(chartDescription);
    }

    /**
     * Save a chartDescription.
     *
     * @param chartDescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChartDescriptionDTO save(ChartDescriptionDTO chartDescriptionDTO) {
        log.debug("Request to save ChartDescription : {}", chartDescriptionDTO);
        ChartDescription chartDescription1 = chartDescriptionRepository.findByChartIdAndPrdIdAndTimeTypeAndStatus(chartDescriptionDTO.getChartId(), chartDescriptionDTO.getPrdId(), chartDescriptionDTO.getTimeType(), 1);
        if(chartDescription1 == null){
            return createNew(chartDescriptionDTO);
        }
        String login = SecurityUtils.getCurrentUserLogin().get();
        chartDescriptionDTO.setModifiedBy(login);
        chartDescriptionDTO.setModifiedTime(Instant.now());
        ChartDescription chartDescription = chartDescriptionCustomMapper.chartDescriptionDTOToChartDescription(chartDescriptionDTO);
        chartDescription.setId(chartDescription1.getId());
        chartDescription.setCreatedBy(chartDescription1.getCreatedBy());
        chartDescription.setCreatedTime(chartDescription1.getCreatedTime());
        chartDescription.setStatus(1);
        chartDescription = chartDescriptionRepository.save(chartDescription);
        return chartDescriptionCustomMapper.chartDescriptionToChartDescriptionDTO(chartDescription);
    }

    /**
     * Get all the chartDescriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChartDescriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChartDescriptions");
        return chartDescriptionRepository.findAllByStatus(1, pageable)
            .map(chartDescriptionCustomMapper::chartDescriptionToChartDescriptionDTO);
    }

    /**
     *
     * @param chartId
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChartDescriptionDTO> findAllByChartId(Long chartId, Pageable pageable) {
        log.debug("Request to get all ChartDescriptions by Chart Id");
        String actor = SecurityUtils.getCurrentUserLogin().get();
        return chartDescriptionRepository.findAllByChartIdAndStatus(chartId, 1, pageable)
            .map(chartDescriptionCustomMapper::chartDescriptionToChartDescriptionDTO);
    }


    /**
     * Get one chartDescription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChartDescriptionDTO> findOne(Long id) {
        log.debug("Request to get ChartDescription : {}", id);
        return chartDescriptionRepository.findById(id)
            .map(chartDescriptionCustomMapper::chartDescriptionToChartDescriptionDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ChartDescriptionDTO findByChartIdAndPrdIdAndTimeType(Long chartId, Integer prdId, Integer timeType){
        return chartDescriptionCustomMapper.chartDescriptionToChartDescriptionDTO(chartDescriptionRepository.findByChartIdAndPrdIdAndTimeTypeAndStatus(chartId, prdId, timeType, 1));
    }

    /**
     * Delete the chartDescription by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChartDescription : {}", id);
        ChartDescription chartDescription = chartDescriptionRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("Chart description not exists", ENTITY_NAME, "Not exists"));
        chartDescription.setStatus(0);
        chartDescriptionRepository.save(chartDescription);
    }
}
