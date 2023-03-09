package com.b4t.app.repository;
import com.b4t.app.domain.ConfigQueryKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ConfigQueryKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigQueryKpiRepository extends JpaRepository<ConfigQueryKpi, Long> {

    @Query(value = "select distinct a.id as query_kpi_id , i1.ITEM_NAME as time_type_name, i2.item_name as input_level_name," +
        " a.QUERY_DATA, a.QUERY_CHECK_DATA, a.list_parent_input_level, a.DESCRIPTION ,a.UPDATE_TIME , a.UPDATE_USER, b.table_destination," +
        " table_source as list_table_source" +
        " from config_query_kpi a" +
        " inner join config_map_kpi_query b on a.id = b.QUERY_KPI_ID " +
        " inner join (select  QUERY_KPI_ID, GROUP_CONCAT(lower(a.TABLE_SOURCE) separator ', ') as table_source from config_input_table_query_kpi a" +
        " where (:status is null or a.STATUS = :status ) group by a.query_kpi_id) c on a.ID = c.QUERY_KPI_ID" +
        " left join config_input_kpi_query d on a.ID = d.QUERY_KPI_ID and d.STATUS = a.status " +
        " inner join cat_item i1 on a.TIME_TYPE = i1.ITEM_VALUE and i1.STATUS = 1 and i1.CATEGORY_CODE ='TIME_TYPE'" +
        " inner join cat_item i2 on a.INPUT_LEVEL = i2.ITEM_VALUE and i2.STATUS = 1 and i2.CATEGORY_CODE ='INPUT_LEVEL'" +
        " inner join cat_item i3 on upper(b.table_destination) = upper(i3.ITEM_VALUE) and i3.STATUS = 1 and i3.CATEGORY_CODE ='DOMAIN_TABLE'" +
        " where 1=1 " +
        " AND (:domainCode is null or i3.ITEM_CODE = :domainCode) " +
        " AND (:kpiId is null or b.KPI_ID = :kpiId )" +
        " AND (:status is null or a.STATUS = :status ) " +
        " AND (:queryId is null or a.id like %:queryId%  ESCAPE '&') " +
        " order by a.UPDATE_TIME desc ",
    countQuery = " select count(*) from (" +
        " select distinct a.id as query_kpi_id , i1.ITEM_NAME as time_type_name, i2.item_name as input_level_name," +
        " a.QUERY_DATA, a.QUERY_CHECK_DATA, a.list_parent_input_level, a.DESCRIPTION ,a.UPDATE_TIME , a.UPDATE_USER, b.table_destination," +
        " table_source as list_table_source" +
        " from config_query_kpi a" +
        " inner join config_map_kpi_query b on a.id = b.QUERY_KPI_ID " +
        " inner join (select  QUERY_KPI_ID, GROUP_CONCAT(lower(a.TABLE_SOURCE) separator ', ') as table_source from config_input_table_query_kpi a" +
        " where (:status is null or a.STATUS = :status ) group by a.query_kpi_id) c on a.ID = c.QUERY_KPI_ID" +
        " left join config_input_kpi_query d on a.ID = d.QUERY_KPI_ID and d.STATUS = a.status " +
        " inner join cat_item i1 on a.TIME_TYPE = i1.ITEM_VALUE and i1.STATUS = 1 and i1.CATEGORY_CODE ='TIME_TYPE'" +
        " inner join cat_item i2 on a.INPUT_LEVEL = i2.ITEM_VALUE and i2.STATUS = 1 and i2.CATEGORY_CODE ='INPUT_LEVEL'" +
        " inner join cat_item i3 on upper(b.table_destination) = upper(i3.ITEM_VALUE) and i3.STATUS = 1 and i3.CATEGORY_CODE ='DOMAIN_TABLE'" +
        " where 1=1 " +
        " AND (:domainCode is null or i3.ITEM_CODE = :domainCode) " +
        " AND (:kpiId is null or b.KPI_ID = :kpiId )" +
        " AND (:status is null or a.STATUS = :status ) " +
        " AND (:queryId is null or a.id like %:queryId%  ESCAPE '&') " +
        " ) tbl"
    , nativeQuery = true)
    Page<Object[]> findAllByDomainAndKpiAndStatus(@Param("domainCode") String domainCode, @Param("kpiId") Long kpiId,
                                                              @Param("status") Long status, @Param("queryId") String queryId, Pageable pageable);


    @Query(value = " select distinct a.KPI_ID, c.KPI_NAME, a.status from config_input_kpi_query a" +
        " inner join cat_graph_kpi c on a.KPI_ID = c.KPI_ID and c.STATUS = 1" +
        " where 1=1 and a.query_kpi_id = :queryKpiId " +
        " order by a.KPI_ID, c.kpi_name",
    countQuery = "select count(tbl.*) from ( select distinct a.KPI_ID, c.KPI_NAME from config_input_kpi_query a" +
        "inner join cat_graph_kpi c on a.KPI_ID = c.KPI_ID and c.STATUS = 1" +
        "where 1=1 and a.query_kpi_id = :queryKpiId ) tbl", nativeQuery = true)
    Page<Object[]> findAllKpiInput(@Param("queryKpiId") String queryKpiId, Pageable pageable);

    @Query(value = " select distinct a.KPI_ID, c.KPI_NAME, a.status from config_map_kpi_query a" +
        " inner join cat_graph_kpi c on a.KPI_ID = c.KPI_ID and c.STATUS = 1" +
        " where 1=1 and a.query_kpi_id = :queryKpiId" +
        " order by a.KPI_ID, c.kpi_name",
    countQuery = " select count(*) from (select distinct a.KPI_ID, c.KPI_NAME from config_map_kpi_query a" +
        " inner join cat_graph_kpi c on a.KPI_ID = c.KPI_ID and c.STATUS = 1" +
        " where 1=1 and a.query_kpi_id = :queryKpiId ) tbl", nativeQuery = true)
    Page<Object[]> findAllKpiOutput(@Param("queryKpiId") String queryKpiId, Pageable pageable);


    @Query(value = " select cgk.KPI_ID, IF(u.item_name is null,concat(cgk.KPI_ID,'-',cgk.KPI_name),concat(cgk.KPI_ID,'-',cgk.KPI_name,'(',u.item_name,')')) as kpi_name" +
        " from cat_graph_kpi cgk " +
        " inner join cat_item d on d.CATEGORY_CODE ='DOMAIN_TABLE' and cgk.DOMAIN_CODE = d.ITEM_CODE and d.status = 1" +
        " left join cat_item u on cgk.UNIT_KPI = u.ITEM_CODE and u.STATUS =1 and u.CATEGORY_CODE ='UNIT'" +
        " where cgk.STATUS = 1" +
        " and upper(d.ITEM_VALUE) in :lstTableSource" +
        " order by cgk.KPI_ID, cgk.KPI_NAME", nativeQuery = true)
    List<Object[]> findAllKpiSourceTable(@Param("lstTableSource") List<String> lstTableSource);

    List<ConfigQueryKpi> findAllByReportId(Long reportId);
}
