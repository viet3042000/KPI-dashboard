package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.repository.DataLogCustomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DataLogCustomRepositoryImpl implements DataLogCustomRepository {

    private final Logger log = LoggerFactory.getLogger(DataLogCustomRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Object[]> searchDataLog(String schemaName, String tableName, String fromdate, String todate, String updateBy, Pageable pageable) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT * ").append(" FROM ").append(schemaName).append(".").append("data_log").append(" a\n");
        queryBuilder.append(" WHERE 1=1 \n");
        if (!DataUtil.isNullOrEmpty(tableName)) {
            queryBuilder.append(" AND (a.table_name = :tableName ) \n");
            params.put("tableName", tableName);
        }
        if(!DataUtil.isNullOrEmpty(updateBy)){
            if(updateBy.equals("NULL")){
                queryBuilder.append(" AND (a.modified_by is null or a.modified_by = '') \n");
            } else {
                queryBuilder.append(" AND a.modified_by = (:updateBy) \n");
                params.put("updateBy", updateBy);
            }
        }
        if(!DataUtil.isNullOrEmpty(fromdate) && !DataUtil.isNullOrEmpty(todate)){
            queryBuilder.append("AND (a.modified_time >= :fromdate AND a.modified_time < :todate )");
            params.put("fromdate", fromdate);
            params.put("todate", todate);
        }
        String countQuery = "SELECT count(*) FROM (" + queryBuilder.toString() + " ) q";

        Query query = manager.createNativeQuery(queryBuilder.toString());
        Query totalQuery = manager.createNativeQuery(countQuery);
        if (pageable.getPageSize() > 0) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        DbUtils.setParramToQuery(query, params);
        DbUtils.setParramToQuery(totalQuery, params);
        List<Object[]> lst = query.getResultList();
        Integer total = DataUtil.safeToInt(totalQuery.getSingleResult());

        return new PageImpl<>(lst, pageable, total);
    }
}
