package com.b4t.app.repository;

import com.b4t.app.domain.ConfigChart;

import com.b4t.app.domain.ConfigChartDetail;
import com.b4t.app.domain.ConfigChartFull;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ConfigChart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigChartRepository extends JpaRepository<ConfigChart, Long> {

    Optional<ConfigChart> findFirstByChartCodeAndStatus(String code, Long status);

    @Query(value = "SELECT new ConfigChartDetail(i.queryId, qc.queryData, qc.defaultValue, i.inputCondition, c.timeTypeDefault, cd.columnQuery) " +
        " FROM ConfigChart c left join ConfigChartItem i on c.id = i.chartId and i.status = 1" +
        " left join ConfigQueryChart qc on i.queryId = qc.id and qc.status = 1 " +
        " left join ConfigDisplayQuery cd on cd.itemChartId = i.id and cd.status = 1 and cd.columnChart = 'Y_AXIS' " +
        " WHERE c.id = ?1")
    List<ConfigChartDetail> findByChartId(Long chartId);

    List<ConfigChart> findByIdInAndStatus(List<Long> ids, Long status);

    @Query(value = "select new ConfigChartFull (a.id, a.chartName, i.itemName, b.areaId, c.screenId, p.id) " +
        " FROM ConfigChart a left join CatItem i on a.domainCode = i.itemCode and i.categoryCode = 'DOMAIN' and i.status = 1 " +
        " INNER JOIN ConfigMapChartArea b on a.id = b.chartId and b.status = 1 " +
        " INNER JOIN ConfigArea c on b.areaId = c.id and c.status = 1 " +
        " INNER JOIN ConfigScreen s on c.screenId = s.id and s.status = 1" +
        " INNER JOIN ConfigProfile p on s.profileId = p.id and p.status = 1" +
        " WHERE p.id = :profileId and a.status = 1 " +
        " AND (:keyword is null or lower(a.chartName) like %:keyword%  escape '&')" +
        " ORDER BY a.domainCode, a.chartName  ")
    List<ConfigChartFull> onSearchChartByProfile(@Param("profileId") Long profileId, @Param("keyword") String keyword);

//    List<ConfigChart> findAllByIdInAndStatus(List<Long> chartIds, Long status);
}
