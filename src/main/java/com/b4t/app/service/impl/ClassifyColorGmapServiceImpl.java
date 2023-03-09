package com.b4t.app.service.impl;

import com.b4t.app.service.ClassifyColorGmapService;
import com.b4t.app.domain.ClassifyColorGmap;
import com.b4t.app.repository.ClassifyColorGmapRepository;
import com.b4t.app.service.dto.ClassifyColorGmapDTO;
import com.b4t.app.service.mapper.ClassifyColorGmapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassifyColorGmap}.
 */
@Service
@Transactional
public class ClassifyColorGmapServiceImpl implements ClassifyColorGmapService {

    private final Logger log = LoggerFactory.getLogger(ClassifyColorGmapServiceImpl.class);

    private final ClassifyColorGmapRepository classifyColorGmapRepository;

    private final ClassifyColorGmapMapper classifyColorGmapMapper;

    public ClassifyColorGmapServiceImpl(ClassifyColorGmapRepository classifyColorGmapRepository, ClassifyColorGmapMapper classifyColorGmapMapper) {
        this.classifyColorGmapRepository = classifyColorGmapRepository;
        this.classifyColorGmapMapper = classifyColorGmapMapper;
    }

    /**
     * Save a classifyColorGmap.
     *
     * @param classifyColorGmapDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ClassifyColorGmapDTO save(ClassifyColorGmapDTO classifyColorGmapDTO) {
        log.debug("Request to save ClassifyColorGmap : {}", classifyColorGmapDTO);
        ClassifyColorGmap classifyColorGmap = classifyColorGmapMapper.toEntity(classifyColorGmapDTO);
        classifyColorGmap = classifyColorGmapRepository.save(classifyColorGmap);
        return classifyColorGmapMapper.toDto(classifyColorGmap);
    }

    /**
     * Get all the classifyColorGmaps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClassifyColorGmapDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassifyColorGmaps");
        return classifyColorGmapRepository.findAll(pageable)
            .map(classifyColorGmapMapper::toDto);
    }

    /**
     * Get one classifyColorGmap by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClassifyColorGmapDTO> findOne(Long id) {
        log.debug("Request to get ClassifyColorGmap : {}", id);
        return classifyColorGmapRepository.findById(id)
            .map(classifyColorGmapMapper::toDto);
    }

    /**
     * Delete the classifyColorGmap by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassifyColorGmap : {}", id);
        classifyColorGmapRepository.deleteById(id);
    }
}
