package com.b4t.app.service.impl;

import com.b4t.app.service.ItRptGraphService;
import com.b4t.app.domain.ItRptGraph;
import com.b4t.app.repository.ItRptGraphRepository;
import com.b4t.app.service.dto.ItRptGraphDTO;
import com.b4t.app.service.mapper.ItRptGraphMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ItRptGraph}.
 */
@Service
@Transactional
public class ItRptGraphServiceImpl implements ItRptGraphService {

    private final Logger log = LoggerFactory.getLogger(ItRptGraphServiceImpl.class);

    private final ItRptGraphRepository itRptGraphRepository;

    private final ItRptGraphMapper itRptGraphMapper;

    public ItRptGraphServiceImpl(ItRptGraphRepository itRptGraphRepository, ItRptGraphMapper itRptGraphMapper) {
        this.itRptGraphRepository = itRptGraphRepository;
        this.itRptGraphMapper = itRptGraphMapper;
    }

    /**
     * Save a itRptGraph.
     *
     * @param itRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ItRptGraphDTO save(ItRptGraphDTO itRptGraphDTO) {
        log.debug("Request to save ItRptGraph : {}", itRptGraphDTO);
        ItRptGraph itRptGraph = itRptGraphMapper.toEntity(itRptGraphDTO);
        itRptGraph = itRptGraphRepository.save(itRptGraph);
        return itRptGraphMapper.toDto(itRptGraph);
    }

    /**
     * Get all the itRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ItRptGraphDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItRptGraphs");
        return itRptGraphRepository.findAll(pageable)
            .map(itRptGraphMapper::toDto);
    }

    /**
     * Get one itRptGraph by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ItRptGraphDTO> findOne(Long id) {
        log.debug("Request to get ItRptGraph : {}", id);
        return itRptGraphRepository.findById(id)
            .map(itRptGraphMapper::toDto);
    }

    /**
     * Delete the itRptGraph by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItRptGraph : {}", id);
        itRptGraphRepository.deleteById(id);
    }
}
