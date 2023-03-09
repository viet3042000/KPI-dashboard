package com.b4t.app.repository;

import com.b4t.app.domain.CatGroupChart;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the CatGroupChart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatGroupChartRepository extends JpaRepository<CatGroupChart, Long> {
    Optional<CatGroupChart> findFirstByGroupCodeAndStatus(String groupCode, Long status);
}
