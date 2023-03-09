package com.b4t.app.repository;

import com.b4t.app.domain.BieumauKehoachchitieu;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the BieumauKehoachchitieu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BieumauKehoachchitieuRepository extends JpaRepository<BieumauKehoachchitieu, Long> {
    Optional<List<BieumauKehoachchitieu>> findBieumauKehoachchitieuByPrdIdAndKpiId(Long prdId, Long kpiId);
    Optional<List<BieumauKehoachchitieu>> findBieumauKehoachchitieuByPrdIdAndKpiCode(Long prdId, String kpiCode);

    @Query(value="select a.* from bieumau_kehoachchitieu a " +
        " left outer join bieumau_kehoachchitieu b on a.kpi_id = b.kpi_id and a.prd_id < b.prd_id" +
        " where b.kpi_id is null and a.kpi_id in :kpiIds"
        , nativeQuery = true
    )
    List<BieumauKehoachchitieu> findLastestPlan(@Param("kpiIds") List<Long> kpiIds);
}
