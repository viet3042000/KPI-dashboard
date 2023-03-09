package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.SysModuleAction;
import com.b4t.app.repository.SysModuleActionRepository;
import com.b4t.app.service.SysModuleActionService;
import com.b4t.app.service.dto.SysModuleActionDTO;
import com.b4t.app.service.mapper.SysModuleActionMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing {@link SysModuleAction}.
 */
@Service
@Transactional
public class SysModuleActionServiceImpl implements SysModuleActionService {

    private final Logger log = LoggerFactory.getLogger(SysModuleActionServiceImpl.class);

    private final SysModuleActionRepository sysModuleActionRepository;

    private final SysModuleActionMapper sysModuleActionMapper;

    public SysModuleActionServiceImpl(SysModuleActionRepository sysModuleActionRepository, SysModuleActionMapper sysModuleActionMapper) {
        this.sysModuleActionRepository = sysModuleActionRepository;
        this.sysModuleActionMapper = sysModuleActionMapper;
    }

    @Override
    public SysModuleActionDTO save(SysModuleActionDTO sysModuleActionDTO) {
        log.debug("Request to insert SysModuleAction : {}", sysModuleActionDTO);
        Date date = new Date();
        sysModuleActionDTO.setUpdateTime(date.toInstant());
        SysModuleAction sysModuleAction = sysModuleActionMapper.toEntity(sysModuleActionDTO);
        sysModuleAction = sysModuleActionRepository.save(sysModuleAction);
        return sysModuleActionMapper.toDto(sysModuleAction);
    }

    @Override
    public List<SysModuleAction> getAllByModuleId(Long id) {
        return this.sysModuleActionRepository.getAllByModuleId(id);
    }

    @Override
    public void detele(long id) {
        this.sysModuleActionRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = {BadRequestAlertException.class, IllegalArgumentException.class})
    public void deteleMultiple(List<SysModuleAction> listDelete) {
        listDelete.forEach(item -> detele(item.getId()));
    }

    @Override
    @Transactional(rollbackFor = {BadRequestAlertException.class, IllegalArgumentException.class, URISyntaxException.class})
    public void saveMultiple(List<SysModuleActionDTO> sysModuleActionDTOS) {
        sysModuleActionDTOS.forEach(this::save);
    }

}
