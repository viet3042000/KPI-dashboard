package com.b4t.app.service.impl;

import com.b4t.app.service.CatKpiSynonymService;
import com.b4t.app.domain.CatKpiSynonym;
import com.b4t.app.repository.CatKpiSynonymRepository;
import com.b4t.app.service.dto.CatKpiSynonymDTO;
import com.b4t.app.service.mapper.CatKpiSynonymMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CatKpiSynonym}.
 */
@Service
@Transactional
public class CatKpiSynonymServiceImpl implements CatKpiSynonymService {

    private final Logger log = LoggerFactory.getLogger(CatKpiSynonymServiceImpl.class);

    private final CatKpiSynonymRepository catKpiSynonymRepository;

    private final CatKpiSynonymMapper catKpiSynonymMapper;

    public CatKpiSynonymServiceImpl(CatKpiSynonymRepository catKpiSynonymRepository, CatKpiSynonymMapper catKpiSynonymMapper) {
        this.catKpiSynonymRepository = catKpiSynonymRepository;
        this.catKpiSynonymMapper = catKpiSynonymMapper;
    }

    /**
     * Save a catKpiSynonym.
     *
     * @param catKpiSynonymDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CatKpiSynonymDTO save(CatKpiSynonymDTO catKpiSynonymDTO) {
        log.debug("Request to save CatKpiSynonym : {}", catKpiSynonymDTO);
        CatKpiSynonym catKpiSynonym = catKpiSynonymMapper.toEntity(catKpiSynonymDTO);
        catKpiSynonym = catKpiSynonymRepository.save(catKpiSynonym);
        return catKpiSynonymMapper.toDto(catKpiSynonym);
    }

    /**
     * Get all the catKpiSynonyms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CatKpiSynonymDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatKpiSynonyms");
        return catKpiSynonymRepository.findAll(pageable)
            .map(catKpiSynonymMapper::toDto);
    }


    /**
     * Get one catKpiSynonym by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CatKpiSynonymDTO> findOne(Long id) {
        log.debug("Request to get CatKpiSynonym : {}", id);
        return catKpiSynonymRepository.findById(id)
            .map(catKpiSynonymMapper::toDto);
    }

    /**
     * Delete the catKpiSynonym by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatKpiSynonym : {}", id);
        catKpiSynonymRepository.deleteById(id);
    }

    @Override
    public List<CatKpiSynonymDTO> findAllByKpiId(Long kpiId) {
        return catKpiSynonymRepository.findAllByKpiId(kpiId).stream()
            .map(catKpiSynonymMapper::toDto).collect(Collectors.toList());
    }
}
