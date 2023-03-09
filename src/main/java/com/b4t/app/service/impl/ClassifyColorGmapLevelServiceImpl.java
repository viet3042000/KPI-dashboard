package com.b4t.app.service.impl;

import com.b4t.app.service.ClassifyColorGmapLevelService;
import com.b4t.app.domain.ClassifyColorGmapLevel;
import com.b4t.app.repository.ClassifyColorGmapLevelRepository;
import com.b4t.app.service.dto.ClassifyColorGmapLevelDTO;
import com.b4t.app.service.mapper.ClassifyColorGmapLevelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassifyColorGmapLevel}.
 */
@Service
@Transactional
public class ClassifyColorGmapLevelServiceImpl implements ClassifyColorGmapLevelService {

    private final Logger log = LoggerFactory.getLogger(ClassifyColorGmapLevelServiceImpl.class);

    private final ClassifyColorGmapLevelRepository classifyColorGmapLevelRepository;

    private final ClassifyColorGmapLevelMapper classifyColorGmapLevelMapper;

    public ClassifyColorGmapLevelServiceImpl(ClassifyColorGmapLevelRepository classifyColorGmapLevelRepository, ClassifyColorGmapLevelMapper classifyColorGmapLevelMapper) {
        this.classifyColorGmapLevelRepository = classifyColorGmapLevelRepository;
        this.classifyColorGmapLevelMapper = classifyColorGmapLevelMapper;
    }

    /**
     * Save a classifyColorGmapLevel.
     *
     * @param classifyColorGmapLevelDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ClassifyColorGmapLevelDTO save(ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO) {
        log.debug("Request to save ClassifyColorGmapLevel : {}", classifyColorGmapLevelDTO);
        ClassifyColorGmapLevel classifyColorGmapLevel = classifyColorGmapLevelMapper.toEntity(classifyColorGmapLevelDTO);
        classifyColorGmapLevel = classifyColorGmapLevelRepository.save(classifyColorGmapLevel);
        return classifyColorGmapLevelMapper.toDto(classifyColorGmapLevel);
    }

    /**
     * Get all the classifyColorGmapLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClassifyColorGmapLevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassifyColorGmapLevels");
        return classifyColorGmapLevelRepository.findAll(pageable)
            .map(classifyColorGmapLevelMapper::toDto);
    }

    /**
     * Get one classifyColorGmapLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClassifyColorGmapLevelDTO> findOne(Long id) {
        log.debug("Request to get ClassifyColorGmapLevel : {}", id);
        return classifyColorGmapLevelRepository.findById(id)
            .map(classifyColorGmapLevelMapper::toDto);
    }

    /**
     * Delete the classifyColorGmapLevel by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassifyColorGmapLevel : {}", id);
        classifyColorGmapLevelRepository.deleteById(id);
    }
}
