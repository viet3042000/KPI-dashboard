package com.b4t.app.repository;

import com.b4t.app.domain.ConfigChartItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigChartItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigChartItemRepository extends JpaRepository<ConfigChartItem, Long> {

    List<ConfigChartItem> findAllByChartIdAndStatus(Long chartId, Long status);

    List<ConfigChartItem> findAllByChartIdInAndStatus(List<Long> chartIds, Long status);
}
