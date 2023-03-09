package com.b4t.app.service.impl;

import com.b4t.app.service.SecurRptGraphService;
import com.b4t.app.domain.SecurRptGraph;
import com.b4t.app.repository.SecurRptGraphRepository;
import com.b4t.app.service.dto.SecurRptGraphDTO;
import com.b4t.app.service.mapper.SecurRptGraphMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SecurRptGraph}.
 */
@Service
@Transactional
public class SecurRptGraphServiceImpl implements SecurRptGraphService {

    private final Logger log = LoggerFactory.getLogger(SecurRptGraphServiceImpl.class);

    private final SecurRptGraphRepository securRptGraphRepository;

    private final SecurRptGraphMapper securRptGraphMapper;

    public SecurRptGraphServiceImpl(SecurRptGraphRepository securRptGraphRepository, SecurRptGraphMapper securRptGraphMapper) {
        this.securRptGraphRepository = securRptGraphRepository;
        this.securRptGraphMapper = securRptGraphMapper;
    }

    /**
     * Save a securRptGraph.
     *
     * @param securRptGraphDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SecurRptGraphDTO save(SecurRptGraphDTO securRptGraphDTO) {
        log.debug("Request to save SecurRptGraph : {}", securRptGraphDTO);
        SecurRptGraph securRptGraph = securRptGraphMapper.toEntity(securRptGraphDTO);
        securRptGraph = securRptGraphRepository.save(securRptGraph);
        return securRptGraphMapper.toDto(securRptGraph);
    }

    /**
     * Get all the securRptGraphs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SecurRptGraphDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SecurRptGraphs");
        return securRptGraphRepository.findAll(pageable)
            .map(securRptGraphMapper::toDto);
    }

    /**
     * Get one securRptGraph by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SecurRptGraphDTO> findOne(Long id) {
        log.debug("Request to get SecurRptGraph : {}", id);
        return securRptGraphRepository.findById(id)
            .map(securRptGraphMapper::toDto);
    }

    /**
     * Delete the securRptGraph by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SecurRptGraph : {}", id);
        securRptGraphRepository.deleteById(id);
    }
}
