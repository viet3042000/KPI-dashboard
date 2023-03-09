package com.b4t.app.service.impl;

import com.b4t.app.service.SysRoleModuleService;
import com.b4t.app.domain.SysRoleModule;
import com.b4t.app.repository.SysRoleModuleRepository;
import com.b4t.app.service.dto.SysRoleModuleDTO;
import com.b4t.app.service.mapper.SysRoleModuleMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SysRoleModule}.
 */
@Service
public class SysRoleModuleServiceImpl implements SysRoleModuleService {

    private final Logger log = LoggerFactory.getLogger(SysRoleModuleServiceImpl.class);

    @Autowired
    private SysRoleModuleRepository sysRoleModuleRepository;

    @Autowired
    private SysRoleModuleMapper sysRoleModuleMapper;

    @Override
    public List<SysRoleModuleDTO> getTreeByRoleId(Long id) {
        return sysRoleModuleRepository.getTreeByRoleId(id);
    }

    @Override
    public void updateRoleModule(SysRoleModuleDTO roleModuleDTO) {
        List<SysRoleModule> originalData = sysRoleModuleRepository.getAllByRoleId(roleModuleDTO.getRoleId());
        List<SysRoleModuleDTO> selected = roleModuleDTO.getList();
        Iterator<SysRoleModule> i = originalData.iterator();
        while (i.hasNext()) {
            Iterator<SysRoleModuleDTO> dtoIterator = selected.iterator();
            SysRoleModule origin = i.next();
            boolean isUncheck = true;
            while (dtoIterator.hasNext()) {
                SysRoleModuleDTO select = dtoIterator.next();
                if (ObjectUtils.equals(select.getActionCode(), origin.getActionCode()) && ObjectUtils.equals(select.getModuleCode(), origin.getModuleCode()) ) {
                    dtoIterator.remove();
                    isUncheck = false;
                    break;
                }
            }
            if (!isUncheck) {
                i.remove();
            }
        }
        long start = System.currentTimeMillis();
        sysRoleModuleRepository.deleteInBatch(originalData);
        log.info("Time delete: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        sysRoleModuleRepository.saveAll(selected.stream().map(SysRoleModuleDTO::toUpdateModel).collect(Collectors.toList()));
        log.info("Time save: " + (System.currentTimeMillis() - start));
    }

//    /**
//     * Save a sysRoleModule.
//     *
//     * @param sysRoleModuleDTO the entity to save.
//     * @return the persisted entity.
//     */
//    @Override
//    public SysRoleModuleDTO save(SysRoleModuleDTO sysRoleModuleDTO) {
//        log.debug("Request to save SysRoleModule : {}", sysRoleModuleDTO);
//        SysRoleModule sysRoleModule = sysRoleModuleMapper.toEntity(sysRoleModuleDTO);
//        sysRoleModule = sysRoleModuleRepository.save(sysRoleModule);
//        return sysRoleModuleMapper.toDto(sysRoleModule);
//    }
//
//    /**
//     * Get all the sysRoleModules.
//     *
//     * @param pageable the pagination information.
//     * @return the list of entities.
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public Page<SysRoleModuleDTO> findAll(Pageable pageable) {
//        log.debug("Request to get all SysRoleModules");
//        return sysRoleModuleRepository.findAll(pageable)
//            .map(sysRoleModuleMapper::toDto);
//    }
//
//
//    /**
//     * Get one sysRoleModule by id.
//     *
//     * @param id the id of the entity.
//     * @return the entity.
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<SysRoleModuleDTO> findOne(Long id) {
//        log.debug("Request to get SysRoleModule : {}", id);
//        return sysRoleModuleRepository.findById(id)
//            .map(sysRoleModuleMapper::toDto);
//    }
//
//    /**
//     * Delete the sysRoleModule by id.
//     *
//     * @param id the id of the entity.
//     */
//    @Override
//    public void delete(Long id) {
//        log.debug("Request to delete SysRoleModule : {}", id);
//        sysRoleModuleRepository.deleteById(id);
//    }


    @Override
    public List<SysRoleModuleDTO> findModuleByUser(Long userId) {
        return sysRoleModuleRepository.findModuleByUser(userId).stream()
            .map(sysRoleModuleMapper::toDto).collect(Collectors.toList());
    }
}
