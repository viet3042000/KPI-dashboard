package com.b4t.app.repository;

import com.b4t.app.domain.ConfigInputTableQueryKpi;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigInputTableQueryKpi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigInputTableQueryKpiRepository extends JpaRepository<ConfigInputTableQueryKpi, Long> {

    List<ConfigInputTableQueryKpi> findAllByQueryKpiId(Integer queryKpiId);

}
