package com.b4t.app.repository;

import com.b4t.app.domain.KpiWarned;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the KpiWarned entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KpiWarnedRepository extends JpaRepository<KpiWarned, Long> {
    List<KpiWarned> findAllByKpiIdAndTimeType(Long kpiId, Long timeType);
}
