package com.b4t.app.repository;

import com.b4t.app.domain.CatGraphKpiOrigin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CatGraphKpiOrigin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatGraphKpiOriginRepository extends JpaRepository<CatGraphKpiOrigin, Long> {
    @Query(value = " select a from CatGraphKpiOrigin a " +
        " where 1=1 " +
        " and (:name is null or lower(a.kpiOriginName) like %:name%) " +
        " and (:code is null or lower(a.kpiOriginCode) like %:code%) " +
        " and (:domainCode is null or a.domainCode = :domainCode) " +
        " and (:timeType is null or a.timeType = :timeType) " +
        " and (:status is null or a.status = :status) ",
    countQuery = " select count(a) from CatGraphKpiOrigin a " +
        " where 1=1 " +
        " and (:name is null or lower(a.kpiOriginName) like %:name%) " +
        " and (:code is null or lower(a.kpiOriginCode) like %:code%) " +
        " and (:domainCode is null or a.domainCode = :domainCode) " +
        " and (:timeType is null or a.timeType = :timeType) " +
        " and (:status is null or a.status = :status) ")
    Page<CatGraphKpiOrigin> onSearch(@Param("name") String kpiOriginName,
                                     @Param("code") String kpiOriginCode,
                                     @Param("domainCode") String domainCode,
                                     @Param("timeType") Integer timeType,
                                     @Param("status") Integer status,
                                     Pageable pageable);
}
