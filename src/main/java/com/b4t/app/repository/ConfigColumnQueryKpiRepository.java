package com.b4t.app.repository;
import com.b4t.app.domain.ConfigColumnQueryKpi;
import com.b4t.app.service.dto.ConfigQueryKpiColumn;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ConfigColumnQueryKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigColumnQueryKpiRepository extends JpaRepository<ConfigColumnQueryKpi, Long> {

    @Query(value = " select a.KPI_ID ,b.COLUMN_QUERY, b.COLUMN_DESTINATION , b.DATA_TYPE, b.STATUS " +
        " from config_map_kpi_query a, config_column_query_kpi  b " +
        " where a.id  = b.MAP_KPI_QUERY_ID and a.QUERY_KPI_ID  = :queryId ", nativeQuery = true)
    List<Object[]> findAllColumnByQueryId(@Param("queryId") Integer queryId);

    @Query(value = "select a from ConfigColumnQueryKpi a where a.mapKpiQueryId in (select b.id from ConfigMapKpiQuery b where b.queryKpiId = :queryId)")
    List<ConfigColumnQueryKpi> findAllByQueryId(@Param("queryId") Integer queryId);

    @Query(value = " select a.KPI_ID ,b.COLUMN_QUERY, b.COLUMN_DESTINATION , b.DATA_TYPE, b.MAP_KPI_QUERY_ID, b.ID " +
        " from config_map_kpi_query a, config_column_query_kpi  b " +
        " where a.id  = b.MAP_KPI_QUERY_ID and a.QUERY_KPI_ID  = :queryId ", nativeQuery = true)
    List<Object[]> findAllConfigColumnQueryKpiByQueryId(@Param("queryId") Integer queryId);

}
