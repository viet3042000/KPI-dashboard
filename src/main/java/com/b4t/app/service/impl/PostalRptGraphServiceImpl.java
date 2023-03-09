package com.b4t.app.service.impl;

import com.b4t.app.service.PostalRptGraphService;
import com.b4t.app.domain.PostalRptGraph;
import com.b4t.app.repository.PostalRptGraphRepository;
import com.b4t.app.service.dto.PostalRptGraphDTO;
import com.b4t.app.service.mapper.PostalRptGraphMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PostalRptGraph}.
 */
@Service
@Transactional
public class PostalRptGraphServiceImpl implements PostalRptGraphService {

    private final Logger log = LoggerFactory.getLogger(PostalRptGraphServiceImpl.class);

    private final PostalRptGraphRepository postalRptGraphRepository;

    private final PostalRptGraphMapper postalRptGraphMapper;

    public PostalRptGraphServiceImpl(PostalRptGraphRepository postalRptGraphRepository, PostalRptGraphMapper postalRptGraphMapper) {
        this.postalRptGraphRepository = postalRptGraphRepository;
        this.postalRptGraphMapper = postalRptGraphMapper;
    }

    /**
     * Save a postalRptGraph.
     *
     * @param postalRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PostalRptGraphDTO save(PostalRptGraphDTO postalRptGraphDTO) {
        log.debug("Request to save PostalRptGraph : {}", postalRptGraphDTO);
        PostalRptGraph postalRptGraph = postalRptGraphMapper.toEntity(postalRptGraphDTO);
        postalRptGraph = postalRptGraphRepository.save(postalRptGraph);
        return postalRptGraphMapper.toDto(postalRptGraph);
    }

    /**
     * Get all the postalRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostalRptGraphDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostalRptGraphs");
        return postalRptGraphRepository.findAll(pageable)
            .map(postalRptGraphMapper::toDto);
    }

    /**
     * Get one postalRptGraph by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PostalRptGraphDTO> findOne(Long id) {
        log.debug("Request to get PostalRptGraph : {}", id);
        return postalRptGraphRepository.findById(id)
            .map(postalRptGraphMapper::toDto);
    }

    /**
     * Delete the postalRptGraph by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostalRptGraph : {}", id);
        postalRptGraphRepository.deleteById(id);
    }

    public List getDescriptionOfTable() {
        return postalRptGraphRepository.getDescriptionOfTable();
    }
}
