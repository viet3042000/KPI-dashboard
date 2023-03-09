package com.b4t.app.service;



import com.b4t.app.domain.SysModuleAction;
import com.b4t.app.service.dto.SysModuleActionDTO;

import java.util.List;

/**
 * Service Interface for managing {@link com.b4t.app.domain.SysModuleAction}.
 */
public interface SysModuleActionService {

    /**
     * Save a sysModuleAction.
     *
     * @param sysModuleActionDTO the entity to save.
     * @return the persisted entity.
     */
    SysModuleActionDTO save(SysModuleActionDTO sysModuleActionDTO);

    List<SysModuleAction> getAllByModuleId(Long id);

    void detele(long id);

    void deteleMultiple(List<SysModuleAction> listDelete);

    void saveMultiple(List<SysModuleActionDTO> sysModuleActionDTOS);

}
