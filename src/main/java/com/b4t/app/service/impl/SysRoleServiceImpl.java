package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DateUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.repository.SysUserRoleRepository;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.SysRoleService;
import com.b4t.app.domain.SysRole;
import com.b4t.app.repository.SysRoleRepository;
import com.b4t.app.service.dto.SysRoleDTO;
import com.b4t.app.service.mapper.SysRoleMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

/**
 * Service Implementation for managing {@link SysRole}.
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    private final Logger log = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    private final SysRoleRepository sysRoleRepository;

    private final SysUserRoleRepository sysUserRoleRepository;

    private final SysRoleMapper sysRoleMapper;

    private final UserRepository userRepository;

    public SysRoleServiceImpl(SysRoleRepository sysRoleRepository, SysUserRoleRepository sysUserRoleRepository, SysRoleMapper sysRoleMapper, UserRepository userRepository) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysRoleMapper = sysRoleMapper;
        this.userRepository = userRepository;
    }
    @Override
    public List<SysRoleDTO> getAllRole() {
        List<SysRole>  rs = sysRoleRepository.getAllRole(SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null));
        List<SysRoleDTO> rsDTOs = sysRoleMapper.toDto(rs);
        return rsDTOs;
    }

    public static String validateKeySearch(String str){
        return str.replaceAll("&", "&&").replaceAll("%", "&%").replaceAll("_", "&_");
    }

    @Override
    public Page<SysRoleDTO> doSearch(String code, String name, Integer status, Pageable pageable) {
        Page<Object[]> rs = sysRoleRepository.doSearch(DataUtil.makeLikeParam(code), DataUtil.makeLikeParam(name), status, pageable);
        return rs.map(obj -> {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            sysRoleDTO.setId(DataUtil.safeToLong(obj[0]));
            sysRoleDTO.setCode(DataUtil.safeToString(obj[2]));
            sysRoleDTO.setName(DataUtil.safeToString(obj[1]));
            sysRoleDTO.setDescription(DataUtil.safeToString(obj[3]));
            sysRoleDTO.setStatus(DataUtil.safeToInt(obj[4]));
            String updateTimeTmp = DataUtil.safeToString(obj[5]);
            if(!DataUtil.isNullOrEmpty(updateTimeTmp)){
                sysRoleDTO.setUpdateTime(DateUtil.stringToDate(updateTimeTmp, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).toInstant());
            }
            sysRoleDTO.setTenantId(DataUtil.safeToLong(obj[6]));
            sysRoleDTO.setDefaultModule(DataUtil.safeToLong(obj[7]));
            sysRoleDTO.setPriorityLevel(DataUtil.safeToLong(obj[8]));
            sysRoleDTO.setDefaultModuleName(DataUtil.safeToString(obj[9]));
            return sysRoleDTO;
        });
    }

    @Override
    public SysRoleDTO save(SysRoleDTO sysRoleDTO) {
        log.debug("Request to save SysRole : {}", sysRoleDTO);
        Long tenantId = SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null);
        if (sysRoleDTO.getId() != null) {
            if (sysRoleRepository.findSysRoleById(sysRoleDTO.getId()).size() == 0) {
                throw new BadRequestAlertException(Translator.toLocale("role.notexist"), "sysRole", "role.notexist");
            }
        }
        if (sysRoleRepository.getSysRoleByCodeAndId(sysRoleDTO.getCode(), sysRoleDTO.getId()) != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.sysRole.existCode"), "sysRole", "sysRole.existCode");
        }

        if(sysUserRoleRepository.findByRoleId(sysRoleDTO.getId()).size()>0 && sysRoleDTO.getStatus()==0){
            throw new BadRequestAlertException(Translator.toLocale("error.sysUserRole.dontLock"), "sysUserRole", "error.sysUserRole.findUserId");
        }
        sysRoleDTO.setUpdateTime(Instant.now());
        sysRoleDTO.setTenantId(tenantId);
        SysRole sysRole = sysRoleMapper.toEntity(sysRoleDTO);
        sysRole = sysRoleRepository.save(sysRole);
        return sysRoleMapper.toDto(sysRole);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SysRole : {}", id);
        if (sysRoleRepository.findSysRoleById(id).size() == 0) {
            throw new BadRequestAlertException(Translator.toLocale("role.notexist"), "sysRole", "role.notexist");
        }
        if(sysUserRoleRepository.findByRoleId(id).size() > 0){
            throw new BadRequestAlertException(Translator.toLocale("error.sysUserRole.findUserId"), "sysUserRole", "error.sysUserRole.findUserId");
        }
        sysRoleRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = {BadRequestAlertException.class, IllegalArgumentException.class})
    public void deleteMultiple(List<SysRoleDTO> sysRoleDTOList) {
        if (DataUtil.isNullOrEmpty(sysRoleDTOList))
            throw new BadRequestAlertException(Translator.toLocale("error.sysRole.listItemIsEmpty"), "sysRole", "sysRole.listItemIsEmpty");
        sysRoleDTOList.forEach(item -> delete(item.getId()));
    }

    @Override
    public List<SysRole> findAllByDefaultModule(Long defaultModule) {
        return sysRoleRepository.findAllByDefaultModule(defaultModule);
    }
}
