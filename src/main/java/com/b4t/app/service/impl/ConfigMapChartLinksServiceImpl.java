package com.b4t.app.service.impl;

import com.b4t.app.service.ConfigMapChartLinksService;
import com.b4t.app.domain.ConfigMapChartLinks;
import com.b4t.app.repository.ConfigMapChartLinksRepository;
import com.b4t.app.service.dto.ConfigMapChartLinksDTO;
import com.b4t.app.service.mapper.ConfigMapChartLinksMapper;
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
 * Service Implementation for managing {@link ConfigMapChartLinks}.
 */
@Service
@Transactional
public class ConfigMapChartLinksServiceImpl implements ConfigMapChartLinksService {

    private final Logger log = LoggerFactory.getLogger(ConfigMapChartLinksServiceImpl.class);

    private final ConfigMapChartLinksRepository configMapChartLinksRepository;

    private final ConfigMapChartLinksMapper configMapChartLinksMapper;

    public ConfigMapChartLinksServiceImpl(ConfigMapChartLinksRepository configMapChartLinksRepository, ConfigMapChartLinksMapper configMapChartLinksMapper) {
        this.configMapChartLinksRepository = configMapChartLinksRepository;
        this.configMapChartLinksMapper = configMapChartLinksMapper;
    }

    @Override
    public List<ConfigMapChartLinksDTO> findAllByScreenId(Long screenId) {
        return configMapChartLinksRepository.findAllByScreenId(screenId)
            .stream().map(configMapChartLinksMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConfigMapChartLinksDTO> saveAll(List<ConfigMapChartLinksDTO> configMapChartLinksDTOList) {
        log.debug("Request to save list ConfigMapChartLinksDTO");
        List<ConfigMapChartLinks> lstData = configMapChartLinksMapper.toEntity(configMapChartLinksDTOList);
        lstData = configMapChartLinksRepository.saveAll(lstData);
        return configMapChartLinksMapper.toDto(lstData);
    }

    /**
     * Save a configMapChartLinks.
     *
     * @param configMapChartLinksDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMapChartLinksDTO save(ConfigMapChartLinksDTO configMapChartLinksDTO) {
        log.debug("Request to save ConfigMapChartLinks : {}", configMapChartLinksDTO);
        ConfigMapChartLinks configMapChartLinks = configMapChartLinksMapper.toEntity(configMapChartLinksDTO);
        configMapChartLinks = configMapChartLinksRepository.save(configMapChartLinks);
        return configMapChartLinksMapper.toDto(configMapChartLinks);
    }

    /**
     * Get all the configMapChartLinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMapChartLinksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigMapChartLinks");
        return configMapChartLinksRepository.findAll(pageable)
            .map(configMapChartLinksMapper::toDto);
    }


    /**
     * Get one configMapChartLinks by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMapChartLinksDTO> findOne(Long id) {
        log.debug("Request to get ConfigMapChartLinks : {}", id);
        return configMapChartLinksRepository.findById(id)
            .map(configMapChartLinksMapper::toDto);
    }

    /**
     * Delete the configMapChartLinks by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMapChartLinks : {}", id);
        configMapChartLinksRepository.deleteById(id);
    }

    public void deleteAllByChartMapId(Long chartMapId) {
        configMapChartLinksRepository.deleteAllByChartMapId(chartMapId);
    }
}
