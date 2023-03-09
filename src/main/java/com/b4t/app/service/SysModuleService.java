package com.b4t.app.service;


import com.b4t.app.domain.SysModule;
import com.b4t.app.service.dto.ParentModule;
import com.b4t.app.service.dto.SysActionDTO;
import com.b4t.app.service.dto.SysModuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.SysModule}.
 */
public interface SysModuleService {

    List<SysModuleDTO> doSearch(String code, String name, Integer status, Long parentId);

    List<ParentModule> getParent();

    List<SysModule> getTreeParent(Integer status);

    /**
     * Save a sysModule.
     *
     * @param sysModuleDTO the entity to save.
     * @return the persisted entity.
     */
    SysModuleDTO save(SysModuleDTO sysModuleDTO);

    /**
     * Get all the sysModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SysModuleDTO> findAll(Pageable pageable);


    /**
     * Get the "id" sysModule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SysModuleDTO> findOne(Long id);

    /**
     * Delete the "id" sysModule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, String moduleCode);

    void deleteMultiple(List<SysModuleDTO> sysModuleDTOS);
}
