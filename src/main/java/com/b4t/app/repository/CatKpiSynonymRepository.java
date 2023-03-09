package com.b4t.app.repository;

import com.b4t.app.domain.CatKpiSynonym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CatKpiSynonym entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatKpiSynonymRepository extends JpaRepository<CatKpiSynonym, Long> {
    List<CatKpiSynonym> findAllByKpiId(Long kpiId);

    @Query(value = "select distinct synonym from cat_kpi_synonym where synonym like %:keyword% limit 100", nativeQuery = true)
    List<Object> findAllBySynonymLike(@Param("keyword") String keyword);
}
