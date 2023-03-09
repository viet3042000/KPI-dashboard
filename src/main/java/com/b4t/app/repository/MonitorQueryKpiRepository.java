package com.b4t.app.repository;
import com.b4t.app.domain.MonitorQueryKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the MonitorQueryKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitorQueryKpiRepository extends JpaRepository<MonitorQueryKpi, Long> {

    @Query(value = "select a.QUERY_KPI_ID, c.ITEM_NAME as time_type, a.STATUS , a.RUN_TIME_SUCC , a.update_time, c.item_value  as time_type_id, a.id" +
        " from monitor_query_kpi a  " +
        " inner join config_query_kpi b on a.QUERY_KPI_ID = b.ID and b.STATUS =1 " +
        " inner join cat_item c on b.TIME_TYPE =c.ITEM_VALUE and c.STATUS  = 1 and c.CATEGORY_CODE ='TIME_TYPE' " +
        " where 1=1 "+
        " and (:keyword is null or a.QUERY_KPI_ID like %:keyword% or c.item_name like %:keyword%) "+
        " and (:tableSource is null or a.QUERY_KPI_ID in (select d.query_kpi_id from config_input_table_query_kpi d where d.table_source = :tableSource)) "+
        " and (:kpiId is null or a.QUERY_KPI_ID in (select q.query_kpi_id from config_map_kpi_query q where q.kpi_id = :kpiId) )" +
        " and (:status is null or a.status = :status ) "+
        " order by a.status desc, a.QUERY_KPI_ID",
        countQuery = "select count(a.QUERY_KPI_ID)  " +
            " from monitor_query_kpi a  " +
            " inner join config_query_kpi b on a.QUERY_KPI_ID = b.ID and b.STATUS =1 " +
            " inner join cat_item c on b.TIME_TYPE =c.ITEM_VALUE and c.STATUS  = 1 and c.CATEGORY_CODE ='TIME_TYPE'"+
            " WHERE 1=1 " +
            " and (:keyword is null or a.QUERY_KPI_ID like %:keyword% or c.item_name like %:keyword%) " +
            " and (:tableSource is null or a.QUERY_KPI_ID in (select d.query_kpi_id from config_input_table_query_kpi d where d.table_source = :tableSource)) "+
            " and (:kpiId is null or a.QUERY_KPI_ID in (select q.query_kpi_id from config_map_kpi_query q where q.kpi_id = :kpiId))" +
            " and (:status is null or a.status = :status ) ",
        nativeQuery = true)
    Page<Object[]> findMonitorQueryKpiByKey(@Param("keyword") String keyword, @Param("tableSource") String tableSource, @Param("kpiId") Long kpiId, @Param("status") Long status, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "update monitor_query_kpi set RUN_TIME_SUCC = :runTime, priority = 1 where query_kpi_id = :queryId", nativeQuery = true)
    void updateMonitorQueryKpi(@Param("runTime") String runTime, @Param("queryId") Integer queryId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update monitor_query_kpi set RUN_TIME_REPORT = :runTime, priority = 1 where query_kpi_id = :queryId", nativeQuery = true)
    void updateRunTimeReport(@Param("runTime") String runTime, @Param("queryId") Integer queryId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update monitor_query_kpi set RUN_TIME_REPORT = null where query_kpi_id = :queryId", nativeQuery = true)
    void clearRunTimeReport(@Param("queryId") Integer queryId);

    Optional<MonitorQueryKpi> findFirstByQueryKpiIdEquals(Integer queryId);

    @Query(value = "select distinct a.table_source as value, b.TABLE_NAME as name\n" +
        " from config_input_table_query_kpi a\n" +
        " inner join INFORMATION_SCHEMA.TABLES b on a.TABLE_SOURCE = upper(concat(b.TABLE_SCHEMA,'.', b.TABLE_NAME)) \n" +
        " where a.status = 1\n" +
        " order by b.table_name\n", nativeQuery = true)
    List<Object[]> getMonitorTemplate();


    @Query(value = "select distinct a.KPI_ID, concat(a.KPI_ID, '_' ,c.KPI_NAME) KPI_NAME from config_map_kpi_query a\n" +
        " inner join config_input_table_query_kpi b on a.QUERY_KPI_ID = b.QUERY_KPI_ID and a.STATUS =1 and b.STATUS =1\n" +
        " inner join cat_graph_kpi c on a.KPI_ID = c.KPI_ID and c.STATUS = 1\n" +
        " where b.TABLE_SOURCE = :template \n" +
        " order by c.kpi_name ", nativeQuery = true)
    List<Object[]> getKpiByTemplate(@Param("template") String template);


    Optional<MonitorQueryKpi> findFirstByQueryKpiId(Integer queryId);
}
