package com.b4t.app.repository;

import com.b4t.app.domain.MapReportDataToDashboard;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MapReportDataToDashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MapReportDataToDashboardRepository extends JpaRepository<MapReportDataToDashboard, Long> {
    List<MapReportDataToDashboard> findAllByRpInputGrantIdAndReportId(Long rpInputGrantId, Long reportId);

    List<MapReportDataToDashboard> deleteAllByRpInputGrantIdAndReportIdAndPrdId(Long rpInputGrantId, Long reportId, Long prdId);

    List<MapReportDataToDashboard> deleteAllByRpInputGrantIdAndReportIdAndPrdIdAndColumnIdAndRowId(Long rpInputGrantId, Long reportId, Long prdId, Long columnId, Long rowId);
}
