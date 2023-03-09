package com.b4t.app.repository;

import com.b4t.app.domain.CatGraphKpi;
import com.b4t.app.domain.CatGraphKpiDetail;
import com.b4t.app.domain.RptGraph;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CatGraphKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatGraphKpiRepository extends JpaRepository<CatGraphKpi, Long> {
    List<CatGraphKpi> findAllByStatus(Long status);

    Optional<CatGraphKpi> findFirstByKpiIdAndStatus(Long kpiId, Long status);

    List<CatGraphKpi> findAllByKpiIdInAndStatus(List<Long> kpiIds, Long status);

    List<CatGraphKpi> findAllByGroupKpiCodeAndDomainCodeAndKpiTypeAndStatus(String groupKpiCode, String domainCode, Long kpiType, Long status);

    Optional<CatGraphKpi> findFirstByKpiCodeAndStatus(String kpiCode, Long status);

    List<CatGraphKpi> findAllByDomainCodeAndStatus(String domainCode, Long status, Sort sort);

    @Query(value = "select new CatGraphKpiDetail(a.id, a.kpiId, a.kpiCode, a.kpiName, a.kpiDisplay, a.unitKpi, b.itemName, a.unitViewCode, c.itemName, " +
        " a.rate, a.status, a.groupKpiCode, g.itemName, a.domainCode, d.itemName, a.source, a.description, a.updateTime, a.updateUser, a.formulaLevel, " +
        " a.formulaQuar, a.formulaYear, a.alarmThresholdType, a.alarmPlanType, a.formulaAcc, a.kpiType, a.synonyms, h.id, h.kpiOriginName) from CatGraphKpi a " +
        " left join CatItem b on a.unitKpi = b.itemCode and b.categoryCode = 'UNIT' and b.status = 1 " +
        " left join CatItem c on a.unitViewCode = c.itemCode and c.categoryCode = 'UNIT' and c.status = 1 " +
        " left join CatItem g on a.groupKpiCode = g.itemCode and g.categoryCode = 'GROUP_KPI' and g.status = 1 " +
        " left join CatItem d on a.domainCode = d.itemCode and d.categoryCode = 'DOMAIN' and d.status = 1 " +
        " left join CatGraphKpiOrigin h on a.kpiOriginId = h.id " +
        " where 1=1 " +
        " and (:keyword is null or lower(a.kpiCode) like %:keyword% escape '&' or lower(a.kpiName) like %:keyword% escape '&') " +
        " and (:domainCode is null or a.domainCode = :domainCode) " +
        " and (:kpiId is null or a.kpiId = :kpiId) " +
        " and (:kpiType is null or a.kpiType = :kpiType) " +
        " and (:groupKpiCode is null or a.groupKpiCode = :groupKpiCode) " +
        " and (:status is null or a.status = :status) " +
        " and (:kpiOriginId is null or a.kpiOriginId = :kpiOriginId) " +
        " and (:isHashTag is null or a.kpiId in (select s.kpiId from CatKpiSynonym s where s.synonym in (:synonym) ) ) ",
        countQuery = "select count(a) from CatGraphKpi a " +
        " left join CatItem b on a.unitKpi = b.itemCode and b.categoryCode = 'UNIT' and b.status = 1 " +
        " left join CatItem c on a.unitViewCode = c.itemCode and c.categoryCode = 'UNIT' and c.status = 1 " +
        " left join CatItem g on a.groupKpiCode = g.itemCode and g.categoryCode = 'GROUP_KPI' and g.status = 1 " +
        " left join CatItem d on a.domainCode = d.itemCode and d.categoryCode = 'DOMAIN' and d.status = 1 " +
        " where 1=1 " +
        " and (:keyword is null or lower(a.kpiCode) like %:keyword% escape '&' or lower(a.kpiName) like %:keyword% escape '&') " +
        " and (:domainCode is null or a.domainCode = :domainCode) " +
        " and (:kpiId is null or a.kpiId = :kpiId) " +
        " and (:kpiType is null or a.kpiType = :kpiType) " +
        " and (:groupKpiCode is null or a.groupKpiCode = :groupKpiCode) " +
        " and (:status is null or a.status = :status) " +
        " and (:kpiOriginId is null or a.kpiOriginId = :kpiOriginId) " +
        " and (:isHashTag is null or a.kpiId in (select s.kpiId from CatKpiSynonym s where s.synonym in (:synonym) ) ) "
        )
    Page<CatGraphKpiDetail> onSearch(@Param("keyword") String keyword,
                                     @Param("domainCode") String domainCode,
                                     @Param("kpiId") Long kpiId,
                                     @Param("kpiType") Long kpiType,
                                     @Param("groupKpiCode") String groupKpiCode,
                                     @Param("status") Long status,
                                     @Param("isHashTag") Long isHashTag,
                                     @Param("synonym") List<String> synonym,
                                     @Param("kpiOriginId") Long kpiOriginId,
                                     Pageable pageable);

    @Query(value = "select new CatGraphKpiDetail(a.id, a.kpiId, a.kpiCode, a.kpiName, a.kpiDisplay, a.unitKpi, b.itemName, a.unitViewCode, c.itemName, " +
        " a.rate, a.status, a.groupKpiCode, g.itemName, a.domainCode, d.itemName, a.source, a.description, a.updateTime, a.updateUser, a.formulaLevel, " +
        " a.formulaQuar, a.formulaYear, a.alarmThresholdType, a.alarmPlanType, a.formulaAcc, a.kpiType, a.synonyms, p.itemName) from CatGraphKpi a " +
        " left join CatItem b on a.unitKpi = b.itemCode and b.categoryCode = 'UNIT' and b.status = 1 " +
        " left join CatItem c on a.unitViewCode = c.itemCode and c.categoryCode = 'UNIT' and c.status = 1 " +
        " left join CatItem g on a.groupKpiCode = g.itemCode and g.categoryCode = 'GROUP_KPI' and g.status = 1 " +
        " left join CatItem d on a.domainCode = d.itemCode and d.categoryCode = 'DOMAIN' and d.status = 1 " +
        " left join CatItem p on a.alarmPlanType = p.itemValue and p.categoryCode = 'ALARM_PLAN_TYPE' and p.status = 1 " +
        " where 1=1 " +
        " and (:keyword is null or lower(a.kpiCode) like %:keyword% escape '&' or lower(a.kpiName) like %:keyword% escape '&') " +
        " and (:domainCode is null or a.domainCode = :domainCode) " +
        " and (:kpiId is null or a.kpiId = :kpiId) " +
        " and (:kpiType is null or a.kpiType = :kpiType) " +
        " and (:groupKpiCode is null or a.groupKpiCode = :groupKpiCode) " +
        " and (:status is null or a.status = :status) " +
        " and (:isHashTag is null or a.kpiId in (select s.kpiId from CatKpiSynonym s where s.synonym in (:synonym) ) ) ")
    List<CatGraphKpiDetail> onExport(@Param("keyword") String keyword,
                                     @Param("domainCode") String domainCode,
                                     @Param("kpiId") Long kpiId,
                                     @Param("kpiType") Long kpiType,
                                     @Param("groupKpiCode") String groupKpiCode,
                                     @Param("status") Long status,
                                     @Param("isHashTag") Long isHashTag,
                                     @Param("synonym") List<String> synonym);

    List<CatGraphKpi> findAllByIdIn(List<Long> lstKpiId);

    @Query(value = "select max(kpi_id)+1 from cat_graph_kpi", nativeQuery = true)
    Object findMaxKpiId();

    /**
     * Set KpiId tu tang theo id
     * @param id
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update cat_graph_kpi cgk set cgk.KPI_ID = cgk.ID + 18100 where cgk.KPI_ID is null", nativeQuery = true)
    void updateKpiIdByIdForCreateKpi(@Param("id") Long id);

}
