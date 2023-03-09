package com.b4t.app.service.impl;


import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.SysAction;
import com.b4t.app.repository.SysActionRepository;
import com.b4t.app.repository.SysModuleActionRepository;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.SysActionService;
import com.b4t.app.service.dto.SysActionDTO;
import com.b4t.app.service.mapper.SysActionMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SysAction}.
 */
@Service
@Transactional
public class SysActionServiceImpl implements SysActionService {

    private final Logger log = LoggerFactory.getLogger(SysActionServiceImpl.class);

    private final SysActionRepository sysActionRepository;

    private final SysModuleActionRepository sysModuleActionRepository;

    private final SysActionMapper sysActionMapper;

    private final UserRepository userRepository;

    private static final String ENTITY_NAME = "sysAction";

    public SysActionServiceImpl(SysActionRepository sysActionRepository, SysModuleActionRepository sysModuleActionRepository, SysActionMapper sysActionMapper, UserRepository userRepository) {
        this.sysActionRepository = sysActionRepository;
        this.sysModuleActionRepository = sysModuleActionRepository;
        this.sysActionMapper = sysActionMapper;
        this.userRepository = userRepository;
    }

    @Override
    public SysActionDTO save(SysActionDTO sysActionDTO) {
        log.debug("Request to save SysAction : {}", sysActionDTO);
        sysActionDTO.setTenantId(SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null));
        if (sysActionDTO.getId() != null)
            if (sysActionRepository.findAllById(sysActionDTO.getId()).size() == 0)
                throw new BadRequestAlertException(Translator.toLocale("error.sysAction.notExist"), ENTITY_NAME, "sysAction.notExist");

        if (sysActionRepository.getSysActionByCodeAndId(sysActionDTO.getCode(), sysActionDTO.getId()) != null)
            throw new BadRequestAlertException(Translator.toLocale("error.sysAction.existCode"), ENTITY_NAME, "sysAction.existCode");

        if (sysActionDTO.getId() != null)
            if (sysModuleActionRepository.findByActionId(sysActionDTO.getId()).size() > 0
                    && sysActionDTO.getStatus().equals(0))
                throw new BadRequestAlertException(Translator.toLocale("error.sysAction.findModuleAction"), ENTITY_NAME, "sysAction.findModuleAction");

//        if (sysActionRepository.getSysActionByNameAndId(sysActionDTO.getName(), sysActionDTO.getId()) != null)
//            throw new BadRequestAlertException(Translator.toLocale("error.sysAction.existName"), ENTITY_NAME, "sysAction.existName");

        Date updateTime = new Date();
        sysActionDTO.setUpdateTime(updateTime.toInstant());
        SysAction sysAction = sysActionMapper.toEntity(sysActionDTO);
        sysAction = sysActionRepository.save(sysAction);
        return sysActionMapper.toDto(sysAction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SysActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SysActions");
        return sysActionRepository.findAll(pageable)
            .map(sysActionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SysActionDTO> findOne(Long id) {
        log.debug("Request to get SysAction : {}", id);
        return sysActionRepository.findById(id)
            .map(sysActionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SysAction : {}", id);
        if (sysActionRepository.findAllById(id).size() == 0)
            throw new BadRequestAlertException(Translator.toLocale("error.sysAction.notExist"), ENTITY_NAME, "sysAction.notExist");

        if (sysModuleActionRepository.findByActionId(id).size() > 0)
            throw new BadRequestAlertException(Translator.toLocale("error.sysAction.findModuleAction"), ENTITY_NAME, "sysAction.findModuleAction");

        sysActionRepository.deleteById(id);
    }

    @Override
    public Page<SysActionDTO> doSearch(String code, String name, Integer status, Pageable pageable) {
        if(StringUtils.isNotEmpty(code) && StringUtils.isNotBlank(code))
            code = DataUtil.makeLikeParam(code);

        if(StringUtils.isNotEmpty(name) && StringUtils.isNotBlank(name))
            name = DataUtil.makeLikeParam(name);
        Page<SysAction> rs = sysActionRepository.doSearch(code, name, status, pageable);
        List<SysActionDTO> rsDTOs = sysActionMapper.toDto(rs.getContent());
        return new PageImpl<>(rsDTOs,pageable,rs.getTotalElements());
    }

    @Override
    public List<SysActionDTO> findAllByCode(List<String> actionCodes) {
        return sysActionMapper.toDto(sysActionRepository.findAllByCodeIn(actionCodes));
    }

    @Override
    public List<SysActionDTO> findAllByUserName(String userName) {
        return sysActionMapper.toDto(sysActionRepository.findAllByUserName(userName));
    }

    @Override
    public List<SysActionDTO> getAll(SysActionDTO sysActionDTO) {
        return sysActionMapper.toDto(sysActionRepository.getAll(DataUtil.makeLikeParam(sysActionDTO.getCode())));
    }

    @Override
    @Transactional(rollbackFor = {BadRequestAlertException.class, IllegalArgumentException.class})
    public void deleteMultiple(List<SysActionDTO> sysActionDTOList) {
        if (DataUtil.isNullOrEmpty(sysActionDTOList))
            throw new BadRequestAlertException(Translator.toLocale("error.sysAction.listItemIsEmpty"), ENTITY_NAME, "sysAction.listItemIsEmpty");
        sysActionDTOList.forEach(item -> delete(item.getId()));
    }

    @Override
    public List<SysActionDTO> findAllByTenantId() {
        return sysActionRepository.findAllByTenantId(SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null)).stream().
            map(sysActionMapper::toDto).collect(Collectors.toList());
    }
}
