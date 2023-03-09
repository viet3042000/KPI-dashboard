package com.b4t.app.repository;

import com.b4t.app.domain.ChartDescription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ChartDescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChartDescriptionRepository extends JpaRepository<ChartDescription, Long> {
    Optional<ChartDescription> findById(Long id);
    Page<ChartDescription> findAllByStatus(Integer status, Pageable pageable);
    Page<ChartDescription> findAllByChartIdAndStatus(Long chartId, Integer status, Pageable pageable);
    ChartDescription findByChartIdAndPrdIdAndTimeTypeAndStatus(Long chartId, Integer prdId, Integer timeType, Integer status);
}
