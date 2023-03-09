package com.b4t.app.repository;
import com.b4t.app.domain.ConfigMapKpiQuery;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ConfigMapKpiQuery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMapKpiQueryRepository extends JpaRepository<ConfigMapKpiQuery, Long> {
    List<ConfigMapKpiQuery> findAllByQueryKpiId(Integer queryId);

}
