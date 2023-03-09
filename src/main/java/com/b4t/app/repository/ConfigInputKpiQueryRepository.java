package com.b4t.app.repository;

import com.b4t.app.domain.ConfigInputKpiQuery;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ConfigInputKpiQuery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigInputKpiQueryRepository extends JpaRepository<ConfigInputKpiQuery, Long> {
    List<ConfigInputKpiQuery> findAllByQueryKpiId(Integer queryKpiId);

}
