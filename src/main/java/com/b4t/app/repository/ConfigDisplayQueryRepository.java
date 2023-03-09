package com.b4t.app.repository;

import com.b4t.app.domain.ConfigDisplayQuery;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigDisplayQuery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigDisplayQueryRepository extends JpaRepository<ConfigDisplayQuery, Long> {

    List<ConfigDisplayQuery> findAllByItemChartIdInAndStatus(List<Long> chartItemIds, Long status);
}
