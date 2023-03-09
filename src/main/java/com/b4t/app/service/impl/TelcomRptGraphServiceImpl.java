package com.b4t.app.service.impl;

import com.b4t.app.service.TelcomRptGraphService;
import com.b4t.app.domain.TelcomRptGraph;
import com.b4t.app.repository.TelcomRptGraphRepository;
import com.b4t.app.service.dto.TelcomRptGraphDTO;
import com.b4t.app.service.mapper.TelcomRptGraphMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TelcomRptGraph}.
 */
@Service
@Transactional
public class TelcomRptGraphServiceImpl implements TelcomRptGraphService {

    private final Logger log = LoggerFactory.getLogger(TelcomRptGraphServiceImpl.class);

    private final TelcomRptGraphRepository telcomRptGraphRepository;

    private final TelcomRptGraphMapper telcomRptGraphMapper;

    public TelcomRptGraphServiceImpl(TelcomRptGraphRepository telcomRptGraphRepository, TelcomRptGraphMapper telcomRptGraphMapper) {
        this.telcomRptGraphRepository = telcomRptGraphRepository;
        this.telcomRptGraphMapper = telcomRptGraphMapper;
    }

    /**
     * Save a telcomRptGraph.
     *
     * @param telcomRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TelcomRptGraphDTO save(TelcomRptGraphDTO telcomRptGraphDTO) {
        log.debug("Request to save TelcomRptGraph : {}", telcomRptGraphDTO);
        TelcomRptGraph telcomRptGraph = telcomRptGraphMapper.toEntity(telcomRptGraphDTO);
        telcomRptGraph = telcomRptGraphRepository.save(telcomRptGraph);
        return telcomRptGraphMapper.toDto(telcomRptGraph);
    }

    /**
     * Get all the telcomRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelcomRptGraphDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TelcomRptGraphs");
        return telcomRptGraphRepository.findAll(pageable)
            .map(telcomRptGraphMapper::toDto);
    }

    /**
     * Get one telcomRptGraph by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TelcomRptGraphDTO> findOne(Long id) {
        log.debug("Request to get TelcomRptGraph : {}", id);
        return telcomRptGraphRepository.findById(id)
            .map(telcomRptGraphMapper::toDto);
    }

    /**
     * Delete the telcomRptGraph by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TelcomRptGraph : {}", id);
        telcomRptGraphRepository.deleteById(id);
    }
}
