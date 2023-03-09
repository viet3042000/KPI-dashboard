package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMapGroupChartArea;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ConfigMapGroupChartArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapGroupChartAreaRepository extends JpaRepository<ConfigMapGroupChartArea, Long> {

}
