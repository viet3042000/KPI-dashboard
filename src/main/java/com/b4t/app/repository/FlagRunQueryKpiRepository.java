package com.b4t.app.repository;

import com.b4t.app.domain.FlagRunQueryKpi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the FlagRunQueryKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlagRunQueryKpiRepository extends JpaRepository<FlagRunQueryKpi, Long> {
    Page<FlagRunQueryKpi> findAllByStatus(Long status, Pageable pageable);
    Optional<FlagRunQueryKpi> findFirstByPrdIdAndTableNameAndStatus(Long prdId, String tableName, Long status);
    Optional<FlagRunQueryKpi> findFirstByPrdIdAndKpiIdAndTableNameAndStatus(Long prdId, Long kpiId, String tableName, Long status);
}
