package com.b4t.app.service.impl;

import com.b4t.app.service.ClassifyColorGmapKpiService;
import com.b4t.app.domain.ClassifyColorGmapKpi;
import com.b4t.app.repository.ClassifyColorGmapKpiRepository;
import com.b4t.app.service.dto.ClassifyColorGmapKpiDTO;
import com.b4t.app.service.mapper.ClassifyColorGmapKpiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassifyColorGmapKpi}.
 */
@Service
@Transactional
public class ClassifyColorGmapKpiServiceImpl implements ClassifyColorGmapKpiService {

    private final Logger log = LoggerFactory.getLogger(ClassifyColorGmapKpiServiceImpl.class);

    private final ClassifyColorGmapKpiRepository classifyColorGmapKpiRepository;

    private final ClassifyColorGmapKpiMapper classifyColorGmapKpiMapper;

    public ClassifyColorGmapKpiServiceImpl(ClassifyColorGmapKpiRepository classifyColorGmapKpiRepository, ClassifyColorGmapKpiMapper classifyColorGmapKpiMapper) {
        this.classifyColorGmapKpiRepository = classifyColorGmapKpiRepository;
        this.classifyColorGmapKpiMapper = classifyColorGmapKpiMapper;
    }

    /**
     * Save a classifyColorGmapKpi.
     *
     * @param classifyColorGmapKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ClassifyColorGmapKpiDTO save(ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO) {
        log.debug("Request to save ClassifyColorGmapKpi : {}", classifyColorGmapKpiDTO);
        ClassifyColorGmapKpi classifyColorGmapKpi = classifyColorGmapKpiMapper.toEntity(classifyColorGmapKpiDTO);
        classifyColorGmapKpi = classifyColorGmapKpiRepository.save(classifyColorGmapKpi);
        return classifyColorGmapKpiMapper.toDto(classifyColorGmapKpi);
    }

    /**
     * Get all the classifyColorGmapKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClassifyColorGmapKpiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassifyColorGmapKpis");
        return classifyColorGmapKpiRepository.findAll(pageable)
            .map(classifyColorGmapKpiMapper::toDto);
    }

    /**
     * Get one classifyColorGmapKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClassifyColorGmapKpiDTO> findOne(Long id) {
        log.debug("Request to get ClassifyColorGmapKpi : {}", id);
        return classifyColorGmapKpiRepository.findById(id)
            .map(classifyColorGmapKpiMapper::toDto);
    }

    /**
     * Delete the classifyColorGmapKpi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassifyColorGmapKpi : {}", id);
        classifyColorGmapKpiRepository.deleteById(id);
    }
}
