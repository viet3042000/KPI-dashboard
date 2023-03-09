package com.b4t.app.repository;

import com.b4t.app.domain.ConfigReportColumn;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ConfigReportColumn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigReportColumnRepository extends JpaRepository<ConfigReportColumn, Long> {
    List<ConfigReportColumn> findAllByReportIdEquals(Long reportId);

    List<ConfigReportColumn> findAllByIsTimeColumnEqualsAndReportIdEquals(Integer isTimeColumn, Long reportId);
}
