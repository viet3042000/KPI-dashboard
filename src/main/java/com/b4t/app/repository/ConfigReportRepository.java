package com.b4t.app.repository;

import com.b4t.app.domain.ConfigReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ConfigReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigReportRepository extends JpaRepository<ConfigReport, Long> {
    Optional<ConfigReport> findFirstByDatabaseNameIgnoreCaseAndTableNameIgnoreCase(String databaseName, String tableName);
}
