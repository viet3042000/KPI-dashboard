package com.b4t.app.service;

import com.b4t.app.domain.SysRole;
import com.b4t.app.service.dto.SysRoleDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.SysRole}.
 */
public interface SysRoleService {
    List<SysRoleDTO> getAllRole();

    Page<SysRoleDTO> doSearch( String code,  String name, Integer status, Pageable pageable);

    /**
     * Save a sysRole.
     *
     * @param sysRoleDTO the entity to save.
     * @return the persisted entity.
     */
    SysRoleDTO save(SysRoleDTO sysRoleDTO);


    /**
     * Delete the "id" sysRole.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteMultiple(List<SysRoleDTO> sysRoleDTOList);

    List<SysRole> findAllByDefaultModule(Long defaultModule);
}
