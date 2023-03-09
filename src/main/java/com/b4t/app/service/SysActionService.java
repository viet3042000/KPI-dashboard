package com.b4t.app.service;

import com.b4t.app.service.dto.SysActionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.SysAction}.
 */
public interface SysActionService {

    /**
     * Save a sysAction.
     *
     * @param sysActionDTO the entity to save.
     * @return the persisted entity.
     */
    SysActionDTO save(SysActionDTO sysActionDTO);

    /**
     * Get all the sysActions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SysActionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" sysAction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SysActionDTO> findOne(Long id);

    /**
     * Delete the "id" sysAction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<SysActionDTO> doSearch(String code, String name, Integer status, Pageable pageable);

    List<SysActionDTO> findAllByCode(List<String> actionCodes);

    List<SysActionDTO> findAllByUserName(String userName);

    List<SysActionDTO> getAll(SysActionDTO sysActionDTO);

    void deleteMultiple(List<SysActionDTO> sysActionDTOList);

    List<SysActionDTO> findAllByTenantId();
}
