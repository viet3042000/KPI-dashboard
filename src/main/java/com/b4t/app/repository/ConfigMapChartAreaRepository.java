package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMapChartArea;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigMapChartArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapChartAreaRepository extends JpaRepository<ConfigMapChartArea, Long> {

    void deleteAllByAreaIdIn(List<Long> areaIds);
}
