package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.repository.ExecuteSqlRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class ExecuteSqlRepositoryImpl implements ExecuteSqlRepository {
    private final EntityManager manager;
    private final ObjectMapper mapper;
    private final Logger log = LoggerFactory.getLogger(ExecuteSqlRepositoryImpl.class);

    public ExecuteSqlRepositoryImpl(EntityManager manager) {
        this.manager = manager;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Integer getMaxPrdId(String sql, Map<String, Object> params) throws JsonProcessingException {
        if (sql.contains(":" + Constants.TABLE_NAME_PARAM)) {
            if (params.containsKey(Constants.TABLE_NAME_PARAM)) {
                sql = sql.replace(":" + Constants.TABLE_NAME_PARAM, (String) params.get(Constants.TABLE_NAME_PARAM));
            } else {
                sql = sql.replace(":" + Constants.TABLE_NAME_PARAM, Constants.DEFAULT_TABLE_NAME);
            }
        }
        Query query = manager.createNativeQuery(sql);
        if (!DataUtil.isNullOrEmpty(params)) {
            for (Parameter<?> queryParam : query.getParameters()) {
                Optional<Object> value = params.entrySet().stream()
                    .filter(i -> queryParam.getName().equals(i.getKey().toUpperCase()))
                    .map(Map.Entry::getValue).findFirst();
                value.ifPresent(o -> query.setParameter(queryParam.getName(), o));
            }
        }
        String defaultParam = mapper.writeValueAsString(params);
        log.info(defaultParam);
        List<?> rs = query.getResultList();
        if (DataUtil.isNullOrEmpty(rs)) return null;
        return rs.get(0) == null ? null : ((Number) rs.get(0)).intValue();
    }

    @Override
    public List<Object> executeSql(String sql, Map<String, Object> params) {
        if (StringUtils.isEmpty(sql)) return new ArrayList<>();
        if (sql.contains(":" + Constants.TABLE_NAME_PARAM) && params.containsKey(Constants.TABLE_NAME_PARAM)) {
            sql = sql.replace(":" + Constants.TABLE_NAME_PARAM, (String) params.get(Constants.TABLE_NAME_PARAM));
        }
        Query q = manager.createNativeQuery(sql);
        if (!DataUtil.isNullOrEmpty(params)) {
            for (Parameter<?> queryParam : q.getParameters()) {
                Optional<Object> value = params.entrySet().stream()
                    .filter(i -> queryParam.getName().equals(i.getKey().toUpperCase()))
                    .map(Map.Entry::getValue)
                    .findFirst();
                value.ifPresent(o -> q.setParameter(queryParam.getName(), o));
            }
        }
        org.hibernate.query.Query hibernateQuery = q.unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return hibernateQuery.list();
    }
}
