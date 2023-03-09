package com.b4t.app.repository;

import com.b4t.app.domain.ConfigQueryChart;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigQueryChart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigQueryChartRepository extends JpaRepository<ConfigQueryChart, Long> {

    List<ConfigQueryChart> findByIdIn(List<Long> ids);

}
