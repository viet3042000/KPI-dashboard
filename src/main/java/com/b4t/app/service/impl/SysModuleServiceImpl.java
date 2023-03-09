package com.b4t.app.service.impl;


import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.SysModule;
import com.b4t.app.repository.SysModuleActionRepository;
import com.b4t.app.repository.SysModuleRepository;
import com.b4t.app.repository.SysRoleModuleRepository;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.SysModuleService;
import com.b4t.app.service.SysRoleService;
import com.b4t.app.service.dto.ParentModule;
import com.b4t.app.service.dto.SysModuleDTO;
import com.b4t.app.service.mapper.SysModuleMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.google.common.collect.Ordering;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SysModule}.
 */
@Service
@Transactional
public class SysModuleServiceImpl implements SysModuleService {

    private final Logger log = LoggerFactory.getLogger(SysModuleServiceImpl.class);

    private final SysModuleRepository sysModuleRepository;

    private final SysRoleModuleRepository sysRoleModuleRepository;

    private final SysModuleActionRepository sysModuleActionRepository;

    private final SysModuleMapper sysModuleMapper;

    private final UserRepository userRepository;

    private final SysRoleService sysRoleService;

    private static final String ENTITY_NAME = "sysModule";

    private static final Integer STATUS_DISABLE = 0;

    public SysModuleServiceImpl(SysModuleRepository sysModuleRepository, SysRoleModuleRepository sysRoleModuleRepository, SysModuleActionRepository sysModuleActionRepository, SysModuleMapper sysModuleMapper, UserRepository userRepository, SysRoleService sysRoleService) {
        this.sysModuleRepository = sysModuleRepository;
        this.sysRoleModuleRepository = sysRoleModuleRepository;
        this.sysModuleActionRepository = sysModuleActionRepository;
        this.sysModuleMapper = sysModuleMapper;
        this.userRepository = userRepository;
        this.sysRoleService = sysRoleService;
    }

    @Override
    public List<SysModuleDTO> doSearch(String code, String name, Integer status, Long parentId) {
        //List<SysModule> rs = sysModuleRepository.doSearch(convertCode, convertName, status, parentId);
        Collator coll = Collator.getInstance();
        coll.setStrength(Collator.PRIMARY);
        List<SysModule> rs2 = searchAndBuild(code, name, status, parentId);
        Comparator<SysModule> compareSysModule = Comparator.comparing(SysModule::getPosition, Ordering.natural().nullsFirst())
                                                            .thenComparing(SysModule::getName,coll)
                                                            .thenComparing(SysModule::getCode, coll);
        rs2.sort(compareSysModule);
        return sysModuleMapper.toDto(rs2);
    }

    @Override
    public List<ParentModule> getParent() {
        Long tenantId = SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null);
        return sysModuleRepository.getParent(tenantId)
                .stream().map(e -> {
                    ParentModule dto = new ParentModule();
                    dto.setParentId(DataUtil.safeToLong(e[0]));
                    dto.setParentName(DataUtil.safeToString(e[1]));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SysModule> getTreeParent(Integer status) {
        Long tenantId = SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null);
        Collator coll = Collator.getInstance();
        coll.setStrength(Collator.PRIMARY);
        Comparator<SysModule> compareSysModule = Comparator.comparing(SysModule::getPosition, Ordering.natural().nullsFirst())
            .thenComparing(SysModule::getName, coll)
            .thenComparing(SysModule::getCode, coll);
        List<SysModule> rs = sysModuleRepository.getTreeParent(tenantId, status);
        rs.sort(compareSysModule);
        return rs;
    }



    @Override
    public SysModuleDTO save(SysModuleDTO sysModuleDTO) {
        log.debug("Request to save SysModule : {}", sysModuleDTO);
        if (sysModuleDTO.getId() != null)
            if (sysModuleRepository.findAllById(sysModuleDTO.getId()).size() == 0)
                throw new BadRequestAlertException(Translator.toLocale("error.sysModule.notExist"), ENTITY_NAME, "sysModule.notExist");

        if (sysModuleDTO.getParentId() != null && !sysModuleDTO.getParentId().equals(0L))
            if (sysModuleRepository.findAllById(sysModuleDTO.getParentId()).size() == 0)
                throw new BadRequestAlertException(Translator.toLocale("error.sysModule.notExistParent"), ENTITY_NAME, "sysModule.notExistParent");


        if (sysModuleRepository.getSysModuleByCodeAndId(sysModuleDTO.getCode(), sysModuleDTO.getId()) != null)
            throw new BadRequestAlertException(Translator.toLocale("error.sysModule.existCode"), ENTITY_NAME, "sysModule.existCode");

        if (sysModuleDTO.getParentId() != null)
            if (sysModuleRepository.findParentInvalid(sysModuleDTO.getId(), sysModuleDTO.getParentId()).size() > 0)
                throw new BadRequestAlertException(Translator.toLocale("error.sysModule.parentInvalid"), ENTITY_NAME, "sysModule.parentInvalid");

        if(STATUS_DISABLE.equals(sysModuleDTO.getStatus()))
            if(!DataUtil.isNullOrEmpty(sysRoleService.findAllByDefaultModule(sysModuleDTO.getId())))
                throw new BadRequestAlertException(Translator.toLocale("error.sysModule.inUse"), ENTITY_NAME, "sysModule.parentInvalid");

        sysModuleDTO.setTenantId(SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null));
        Date date = new Date();
        sysModuleDTO.setUpdateTime(date.toInstant());
        SysModule sysModule = sysModuleMapper.toEntity(sysModuleDTO);
        sysModule = sysModuleRepository.save(sysModule);
        return sysModuleMapper.toDto(sysModule);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SysModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SysModules");
        return sysModuleRepository.findAll(pageable)
                .map(sysModuleMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SysModuleDTO> findOne(Long id) {
        log.debug("Request to get SysModule : {}", id);
        return sysModuleRepository.findById(id)
                .map(sysModuleMapper::toDto);
    }

    @Override
    public void delete(Long id, String moduleCode) {
        log.debug("Request to delete SysModule : {}", id);
        if (sysModuleRepository.findAllById(id).size() == 0)
            throw new BadRequestAlertException(Translator.toLocale("error.sysModule.notExist"), ENTITY_NAME, "sysModule.notExist");

        if (sysRoleModuleRepository.findByModuleCode(moduleCode).size() > 0)
            throw new BadRequestAlertException(Translator.toLocale("error.sysModule.findRole"), ENTITY_NAME, "sysModule.findRole");

        if (sysModuleRepository.findAllByParentId(id).size() > 0)
            throw new BadRequestAlertException(Translator.toLocale("error.sysModule.childrenExist"), ENTITY_NAME, "sysModule.childrenExist");

        if(!DataUtil.isNullOrEmpty(sysRoleService.findAllByDefaultModule(id)))
            throw new BadRequestAlertException(Translator.toLocale("error.sysModule.inUse"), ENTITY_NAME, "sysModule.parentInvalid");

        sysModuleRepository.deleteById(id);
        sysModuleActionRepository.deleteByModuleId(id);

    }

    @Override
    @Transactional(rollbackFor = {BadRequestAlertException.class, IllegalArgumentException.class})
    public void deleteMultiple(List<SysModuleDTO> sysModuleDTOS) {
        if (DataUtil.isNullOrEmpty(sysModuleDTOS))
            throw new BadRequestAlertException(Translator.toLocale("error.sysModule.listItemIsEmpty"), ENTITY_NAME, "sysModule.listItemIsEmpty");
        sysModuleDTOS.forEach(item -> delete(item.getId(), item.getCode()));
    }

    public List<SysModule> searchAndBuild(String code, String name, Integer status, Long parentId){
        String convertCode = null, convertName = null;

        if(StringUtils.isNotEmpty(code) && StringUtils.isNotBlank(code))
            convertCode = DataUtil.makeLikeParam(code);

        if(StringUtils.isNotEmpty(name) && StringUtils.isNotBlank(name))
            convertName = DataUtil.makeLikeParam(name);

        List<SysModule> condition = sysModuleRepository.findByParamSearch(convertCode,convertName,status,parentId);

//        if(StringUtils.isNotEmpty(code) && StringUtils.isNotBlank(code))
//            condition = condition.stream().filter( item -> filterSearch(code, item.getCode())).collect(Collectors.toList());
//
//        if(StringUtils.isNotEmpty(name) && StringUtils.isNotBlank(name))
//            condition = condition.stream().filter( item -> filterSearch(name, item.getName())).collect(Collectors.toList());

        Set<SysModule> result = findChild(condition);
        result.addAll(findParent(condition));
        result.addAll(condition);
        return new ArrayList<>(result);
    }

    public List<Long> getListParentId(List<SysModule> input){
        return input.stream().map(SysModule::getParentId).collect(Collectors.toList());
    }

    public List<Long> getListId(List<SysModule> input){
        return input.stream().map(SysModule::getId).collect(Collectors.toList());
    }

    public Set<SysModule> findChild(List<SysModule> input){
        boolean isLoop = true;
        Set<SysModule> rs = new HashSet<>();
        while (isLoop){
            List<Long> parentIds = getListId(input);
            List<SysModule> temp = sysModuleRepository.findAllByParentIdIsIn(parentIds);
            if(DataUtil.isNullOrEmpty(temp)){
                isLoop = false;
            }else{
                rs.addAll(temp);
                input = temp;
            }
        }
        return rs;
    }

    public Set<SysModule> findParent(List<SysModule> input){
        boolean isLoop = true;
        Set<SysModule> rs = new HashSet<>();
        while (isLoop){
            List<Long> ids = getListParentId(input);
            List<SysModule> temp = sysModuleRepository.findAllByIdIsIn(ids);
            if(DataUtil.isNullOrEmpty(temp)){
                isLoop = false;
            }else{
                rs.addAll(temp);
                input = temp;
            }
        }
        return rs;
    }

    public boolean filterSearch(String input, String check){
        if(StringUtils.isNotEmpty(input) && StringUtils.isNotBlank(input))
            return StringUtils.containsIgnoreCase(check, input);
        return true;
    }

}
