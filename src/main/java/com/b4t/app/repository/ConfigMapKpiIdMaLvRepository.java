package com.b4t.app.repository;

import com.b4t.app.domain.ConfigMapKpiIdMaLv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the CatGraphKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapKpiIdMaLvRepository extends JpaRepository<ConfigMapKpiIdMaLv, Long> {

    Optional<ConfigMapKpiIdMaLv> findByKpiId(Long kpiId);



}
