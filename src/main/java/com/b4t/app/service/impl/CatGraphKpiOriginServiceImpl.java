package com.b4t.app.service.impl;

import com.b4t.app.service.CatGraphKpiOriginService;
import com.b4t.app.domain.CatGraphKpiOrigin;
import com.b4t.app.repository.CatGraphKpiOriginRepository;
import com.b4t.app.service.dto.CatGraphKpiOriginDTO;
import com.b4t.app.service.mapper.CatGraphKpiOriginMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CatGraphKpiOrigin}.
 */
@Service
@Transactional
public class CatGraphKpiOriginServiceImpl implements CatGraphKpiOriginService {

    private final Logger log = LoggerFactory.getLogger(CatGraphKpiOriginServiceImpl.class);

    private final CatGraphKpiOriginRepository catGraphKpiOriginRepository;

    private final CatGraphKpiOriginMapper catGraphKpiOriginMapper;

    public CatGraphKpiOriginServiceImpl(CatGraphKpiOriginRepository catGraphKpiOriginRepository, CatGraphKpiOriginMapper catGraphKpiOriginMapper) {
        this.catGraphKpiOriginRepository = catGraphKpiOriginRepository;
        this.catGraphKpiOriginMapper = catGraphKpiOriginMapper;
    }

    /**
     * Save a catGraphKpiOrigin.
     *
     * @param catGraphKpiOriginDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CatGraphKpiOriginDTO save(CatGraphKpiOriginDTO catGraphKpiOriginDTO) {
        log.debug("Request to save CatGraphKpiOrigin : {}", catGraphKpiOriginDTO);
        CatGraphKpiOrigin catGraphKpiOrigin = catGraphKpiOriginMapper.toEntity(catGraphKpiOriginDTO);
        catGraphKpiOrigin = catGraphKpiOriginRepository.save(catGraphKpiOrigin);
        return catGraphKpiOriginMapper.toDto(catGraphKpiOrigin);
    }

    /**
     * Get all the catGraphKpiOrigins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CatGraphKpiOriginDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatGraphKpiOrigins");
        return catGraphKpiOriginRepository.findAll(pageable)
            .map(catGraphKpiOriginMapper::toDto);
    }


    /**
     * Get one catGraphKpiOrigin by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CatGraphKpiOriginDTO> findOne(Long id) {
        log.debug("Request to get CatGraphKpiOrigin : {}", id);
        return catGraphKpiOriginRepository.findById(id)
            .map(catGraphKpiOriginMapper::toDto);
    }

    /**
     * Delete the catGraphKpiOrigin by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatGraphKpiOrigin : {}", id);
        catGraphKpiOriginRepository.deleteById(id);
    }

    @Override
    public Page<CatGraphKpiOriginDTO> onSearch(CatGraphKpiOriginDTO catGraphKpiOriginDTO, Pageable pageable){
        return catGraphKpiOriginRepository.onSearch(catGraphKpiOriginDTO.getKpiOriginName(), catGraphKpiOriginDTO.getKpiOriginCode(), catGraphKpiOriginDTO.getDomainCode(), catGraphKpiOriginDTO.getTimeType(), catGraphKpiOriginDTO.getStatus(), pageable)
            .map(catGraphKpiOriginMapper::toDto);
    }
}
