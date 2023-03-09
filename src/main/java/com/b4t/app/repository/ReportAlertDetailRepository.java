package com.b4t.app.repository;

import com.b4t.app.domain.ReportAlertDetail;
import com.b4t.app.service.dto.ReportAlertDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportAlertDetailRepository extends JpaRepository<ReportAlertDetail, Long> {

    @Query(value = "select * from mic_dashboard.report_alert_detail where table_id = :id", nativeQuery = true)
    List<ReportAlertDetail> findByReportId(@Param("id")int id);

//    @Query(value = "delete from mic_dashboard.report_alert_detail where table_id = :id", nativeQuery = true)
    void deleteByTableId(String id);
}
