package com.b4t.app.service.impl;

import com.b4t.app.service.IctRptGraphService;
import com.b4t.app.domain.IctRptGraph;
import com.b4t.app.repository.IctRptGraphRepository;
import com.b4t.app.service.dto.IctRptGraphDTO;
import com.b4t.app.service.mapper.IctRptGraphMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link IctRptGraph}.
 */
@Service
@Transactional
public class IctRptGraphServiceImpl implements IctRptGraphService {

    private final Logger log = LoggerFactory.getLogger(IctRptGraphServiceImpl.class);

    private final IctRptGraphRepository ictRptGraphRepository;

    private final IctRptGraphMapper ictRptGraphMapper;

    public IctRptGraphServiceImpl(IctRptGraphRepository ictRptGraphRepository, IctRptGraphMapper ictRptGraphMapper) {
        this.ictRptGraphRepository = ictRptGraphRepository;
        this.ictRptGraphMapper = ictRptGraphMapper;
    }

    /**
     * Save a ictRptGraph.
     *
     * @param ictRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public IctRptGraphDTO save(IctRptGraphDTO ictRptGraphDTO) {
        log.debug("Request to save IctRptGraph : {}", ictRptGraphDTO);
        IctRptGraph ictRptGraph = ictRptGraphMapper.toEntity(ictRptGraphDTO);
        ictRptGraph = ictRptGraphRepository.save(ictRptGraph);
        return ictRptGraphMapper.toDto(ictRptGraph);
    }

    /**
     * Get all the ictRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IctRptGraphDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IctRptGraphs");
        return ictRptGraphRepository.findAll(pageable)
            .map(ictRptGraphMapper::toDto);
    }

    /**
     * Get one ictRptGraph by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IctRptGraphDTO> findOne(Long id) {
        log.debug("Request to get IctRptGraph : {}", id);
        return ictRptGraphRepository.findById(id)
            .map(ictRptGraphMapper::toDto);
    }

    /**
     * Delete the ictRptGraph by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IctRptGraph : {}", id);
        ictRptGraphRepository.deleteById(id);
    }
}
