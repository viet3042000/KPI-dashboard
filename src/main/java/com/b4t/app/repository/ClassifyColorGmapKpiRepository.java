package com.b4t.app.repository;

import com.b4t.app.domain.ClassifyColorGmapKpi;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClassifyColorGmapKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassifyColorGmapKpiRepository extends JpaRepository<ClassifyColorGmapKpi, Long> {
}
