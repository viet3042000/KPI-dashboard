package com.b4t.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataLogCustomRepository {
    Page<Object[]> searchDataLog(String schemaName, String tableName, String fromdate, String todate, String updateBy, Pageable pageable);
}
