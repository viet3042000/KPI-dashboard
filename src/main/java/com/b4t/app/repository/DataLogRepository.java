package com.b4t.app.repository;

import com.b4t.app.domain.ConfigReport;
import com.b4t.app.domain.DataLog;
import com.b4t.app.domain.SysAction;
import com.b4t.app.domain.User;
import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.dto.ConfigReportDTO;
import com.b4t.app.service.dto.DataLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface DataLogRepository extends JpaRepository<DataLog, Long>{

    @Query(value = " select * from data_log a \n" +
        " where 1=1\n" +
        " and (a.table_name = (:tableName) ) " +
//        " and (a.modified_time = (:timeType) ) " +
        " and (:updateBy is null or a.modified_by = (:updateBy) ) " +
        "",
        countQuery = " select count(*) from data_log a \n" +
            " where 1=1\n" +
            " and (a.table_name = (:tableName) ) " +
//            " and (a.modified_time = (:timeType) ) " +
            " and (:updateBy is null or a.modified_by = (:updateBy) ) " +
            "",
        nativeQuery = true
    )
    Page<Object[]> searchDataLog(@Param("tableName") String tableName, @Param("updateBy") String updateBy,  Pageable pageable);

    @Query(value = " select * from data_log a \n" +
        " where 1=1\n" +
        " and (a.table_name = (:tableName) ) " +
        " and (:updateBy is null or a.modified_by = (:updateBy) ) " +
        " and (a.modified_time >= :fromdate and a.modified_time < :todate ) " +
        "",
        countQuery = " select count(*) from data_log a \n" +
            " where 1=1\n" +
            " and (a.table_name = (:tableName) ) " +
            " and (:updateBy is null or a.modified_by = (:updateBy) ) " +
            " and (a.modified_time >= :fromdate and a.modified_time < :todate ) " +
            "",
        nativeQuery = true
    )
    Page<Object[]> searchDataLog(@Param("tableName") String tableName, @Param("fromdate") String fromdate, @Param("todate") String todate, @Param("updateBy") String updateBy, Pageable pageable);

}

