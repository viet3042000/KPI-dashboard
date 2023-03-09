package com.b4t.app.repository;

import com.b4t.app.domain.SysModuleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SysModuleAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysModuleActionRepository extends JpaRepository<SysModuleAction, Long> {

    List<SysModuleAction> getAllByModuleId(Long id);

    void deleteById(long id);
    void deleteByModuleId(long id);

    List<SysModuleAction> findByActionId(Long id);
}
