package com.b4t.app.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExecuteSqlRepository {
    Integer getMaxPrdId(String sql, Map<String, Object> params) throws JsonProcessingException;

    List<Object> executeSql(String sql, Map<String, Object> params);
}
