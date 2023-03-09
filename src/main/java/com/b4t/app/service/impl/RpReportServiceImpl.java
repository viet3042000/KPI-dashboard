package com.b4t.app.service.impl;

import com.b4t.app.service.RpReportService;
import com.b4t.app.domain.RpReport;
import com.b4t.app.repository.RpReportRepository;
import com.b4t.app.service.dto.RpReportDTO;
import com.b4t.app.service.mapper.RpReportMapper;
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
 * Service Implementation for managing {@link RpReport}.
 */
@Service
@Transactional
public class RpReportServiceImpl implements RpReportService {

    private final Logger log = LoggerFactory.getLogger(RpReportServiceImpl.class);

    private final RpReportRepository rpReportRepository;

    private final RpReportMapper rpReportMapper;

    public RpReportServiceImpl(RpReportRepository rpReportRepository, RpReportMapper rpReportMapper) {
        this.rpReportRepository = rpReportRepository;
        this.rpReportMapper = rpReportMapper;
    }

    @Override
    public RpReportDTO save(RpReportDTO rpReportDTO) {
        log.debug("Request to save RpReport : {}", rpReportDTO);
        RpReport rpReport = rpReportMapper.toEntity(rpReportDTO);
        rpReport = rpReportRepository.save(rpReport);
        return rpReportMapper.toDto(rpReport);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RpReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RpReports");
        return rpReportRepository.findAll(pageable)
            .map(rpReportMapper::toDto);
    }

    @Override
    public List<RpReportDTO> findAllReport() {
        return rpReportRepository.findAll().stream()
            .map(rpReportMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RpReportDTO> findOne(Long id) {
        log.debug("Request to get RpReport : {}", id);
        return rpReportRepository.findById(id)
            .map(rpReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RpReport : {}", id);
        rpReportRepository.deleteById(id);
    }
}
