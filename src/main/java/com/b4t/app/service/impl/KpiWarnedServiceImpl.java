package com.b4t.app.service.impl;

import com.b4t.app.service.KpiWarnedService;
import com.b4t.app.domain.KpiWarned;
import com.b4t.app.repository.KpiWarnedRepository;
import com.b4t.app.service.dto.KpiWarnedDTO;
import com.b4t.app.service.mapper.KpiWarnedMapper;
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
 * Service Implementation for managing {@link KpiWarned}.
 */
@Service
@Transactional
public class KpiWarnedServiceImpl implements KpiWarnedService {

    private final Logger log = LoggerFactory.getLogger(KpiWarnedServiceImpl.class);

    private final KpiWarnedRepository kpiWarnedRepository;

    private final KpiWarnedMapper kpiWarnedMapper;

    public KpiWarnedServiceImpl(KpiWarnedRepository kpiWarnedRepository, KpiWarnedMapper kpiWarnedMapper) {
        this.kpiWarnedRepository = kpiWarnedRepository;
        this.kpiWarnedMapper = kpiWarnedMapper;
    }

    /**
     * Save a kpiWarned.
     *
     * @param kpiWarnedDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public KpiWarnedDTO save(KpiWarnedDTO kpiWarnedDTO) {
        log.debug("Request to save KpiWarned : {}", kpiWarnedDTO);
        KpiWarned kpiWarned = kpiWarnedMapper.toEntity(kpiWarnedDTO);
        kpiWarned = kpiWarnedRepository.save(kpiWarned);
        return kpiWarnedMapper.toDto(kpiWarned);
    }

    /**
     * Get all the kpiWarneds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<KpiWarnedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KpiWarneds");
        return kpiWarnedRepository.findAll(pageable)
            .map(kpiWarnedMapper::toDto);
    }


    /**
     * Get one kpiWarned by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<KpiWarnedDTO> findOne(Long id) {
        log.debug("Request to get KpiWarned : {}", id);
        return kpiWarnedRepository.findById(id)
            .map(kpiWarnedMapper::toDto);
    }

    /**
     * Delete the kpiWarned by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete KpiWarned : {}", id);
        kpiWarnedRepository.deleteById(id);
    }

    public List<KpiWarnedDTO> findAllByKpiId(Long kpiId, Long timeType) {
        return kpiWarnedRepository.findAllByKpiIdAndTimeType(kpiId, timeType).stream().map(bean -> kpiWarnedMapper.toDto(bean)).collect(Collectors.toList());
    }
}
