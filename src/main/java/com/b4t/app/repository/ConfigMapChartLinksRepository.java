package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMapChartLinks;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigMapChartLinks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapChartLinksRepository extends JpaRepository<ConfigMapChartLinks, Long> {
    void deleteAllByChartMapId(Long chartMapId);

    void deleteAllByScreenId(Long chartMapId);

    @Query(value = "select chart_link_id from config_map_chart_links where chart_map_id = :chartMapId ", nativeQuery = true)
    List<Long> getListChartMapId(@Param("chartMapId") Long chartMapId);

    @Query(value = "select chart_link_id from config_map_chart_links where chart_map_id = :chartMapId and screen_id = :screenId ", nativeQuery = true)
    List<Long> getListChartMapIdAndScreenId(@Param("chartMapId") Long chartMapId, @Param("screenId") Long screenId);

    List<ConfigMapChartLinks> findAllByScreenId(Long screenId);
}
