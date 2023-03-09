package com.b4t.app.repository;

import com.b4t.app.domain.RpReport;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the RpReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RpReportRepository extends JpaRepository<RpReport, Long> {

    @Override
    List<RpReport> findAll();
}
