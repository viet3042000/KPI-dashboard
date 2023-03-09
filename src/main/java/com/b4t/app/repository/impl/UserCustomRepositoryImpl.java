package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.config.ChatQueryConstant;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.NotificationOfUser;
import com.b4t.app.domain.UserCustom;
import com.b4t.app.repository.UserCustomRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserCustomRepositoryImpl implements UserCustomRepository {
    private final Logger log = LoggerFactory.getLogger(UserCustomRepositoryImpl.class);
    @PersistenceContext
    private EntityManager manager;


    public List<UserCustom> getListUserChat(Long userLoginId) {
        String queryStr = ChatQueryConstant.QUERY_GET_LIST_USER_CHART.replace("#idUserLogin", userLoginId.toString());
        Query query = manager.createNativeQuery(queryStr, UserCustom.class);
        query.setParameter("idUserLogin", userLoginId);
        return query.getResultList();
    }

    public List<UserCustom> getTotalNoticeOfUser(Long userLoginId) {
        String queryStr = ChatQueryConstant.COUNT_NUM_NOTICE_UN_READ.replace("#idUserLogin", userLoginId.toString());
        Query query = manager.createNativeQuery(queryStr);
        query.setParameter("idUserLogin", userLoginId);
        List<Object[]> queryResults = query.getResultList();
        if (DataUtil.isNullOrEmpty(queryResults)) return null;
        List<UserCustom> results = new ArrayList<>();
        UserCustom userCustom;
        for (Object[] bean : queryResults) {
            userCustom = new UserCustom(DataUtil.safeToLong(bean[0]), DataUtil.safeToLong(bean[1]));
            results.add(userCustom);
        }
        return results;
    }

    public List<UserCustom> getUserRecivedAlarm(Long chartId) {
        Query query = manager.createNativeQuery(ChatQueryConstant.QUERY_GET_USER_RECIEVED_ALARM);
        query.setParameter("chartId", chartId);
        List<Object[]> queryResults = query.getResultList();
        List<UserCustom> results = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(queryResults)) return results;
        UserCustom userCustom;
        for (Object[] bean : queryResults) {
            userCustom = new UserCustom(DataUtil.safeToLong(bean[0]), DataUtil.safeToString(bean[1]));
            results.add(userCustom);
        }
        return results;
    }

    public Long countNumNoticeOfUser(Long userLoginId, Long userIdConversation, String keySearch) {
        Map<String, Object> parram = new HashMap<>();
        String querySearch = ChatQueryConstant.GET_NOTICE_OF_CLIENT_COUNT;
        buildQueryNoticeOfClient(userLoginId, userIdConversation, keySearch, parram, querySearch);
        Query query = manager.createNativeQuery(querySearch);
        DbUtils.setParramToQuery(query, parram);
        return DataUtil.safeToLong(query.getSingleResult());

    }

    public List<NotificationOfUser> getNoticeOfClient(Long userLoginId, Long userIdConversation, String keySearch, Long offSet, int pageSize) {
        Map<String, Object> parram = new HashMap<>();
        String querySearch = ChatQueryConstant.GET_NOTICE_OF_CLIENT;
        buildQueryNoticeOfClient(userLoginId, userIdConversation, keySearch, parram, querySearch);
        Query query = manager.createNativeQuery(querySearch, NotificationOfUser.class);
        DbUtils.setParramToQuery(query, parram);
        if (offSet != null && pageSize > 0) {
            query.setFirstResult(offSet.intValue());
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    @Override
    public Page<UserCustom> getUserWithNotify(String keyword, Long userId, Pageable pageable) {
        StringBuilder sql = new StringBuilder(ChatQueryConstant.QUERY_USER_NOTICE);
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (a.login like :keyword escape :escapeChr");
            sql.append(" or a.first_name like :keyword escape :escapeChr");
            sql.append(" or a.last_name like :keyword escape :escapeChr");
            sql.append(" or a.email like :keyword escape :escapeChr )");
        }
        sql.append(" order by noder.time_newest desc, a.login" +
            " limit :offset , :size");

        Query query = manager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            query.setParameter("keyword", keyword);
            query.setParameter("escapeChr", Constants.DEFAULT_ESCAPE_CHAR);
        }
        query.setParameter("offset", pageable.getOffset());
        query.setParameter("size", pageable.getPageSize());
        org.hibernate.query.Query hibernateQuery = query.unwrap(org.hibernate.query.Query.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        hibernateQuery.setResultTransformer(new ResultTransformer() {
            @Override
            public UserCustom transformTuple(Object[] rowData, String[] aliasNames) {
                return DbUtils.transformTuple(mapper, rowData, aliasNames, UserCustom.class);
            }

            @Override
            public List<UserCustom> transformList(List list) {
                return DataUtil.isNullOrEmpty(list)
                    ? new ArrayList<>()
                    : (List<UserCustom>) list.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            }
        });
        List<UserCustom> list = hibernateQuery.list();

        StringBuilder countSql = new StringBuilder(ChatQueryConstant.COUNT_NOTICE_USER);
        if (StringUtils.isNotEmpty(keyword)) {
            countSql.append(" and (a.login like :keyword escape :escapeChr");
            countSql.append(" or a.first_name like :keyword escape :escapeChr");
            countSql.append(" or a.last_name like :keyword escape :escapeChr");
            countSql.append(" or a.email like :keyword escape :escapeChr )");
        }
        countSql.append(" order by noder.time_newest desc, a.login");

        Query cntQuery = manager.createNativeQuery(countSql.toString());
        cntQuery.setParameter("userId", userId);
        if (StringUtils.isNotEmpty(keyword)) {
            cntQuery.setParameter("keyword", keyword);
            cntQuery.setParameter("escapeChr", Constants.DEFAULT_ESCAPE_CHAR);
        }
        long cnt = ((Number) cntQuery.getSingleResult()).longValue();
        return new PageImpl<>(list, pageable, cnt);
    }

    private String buildQueryNoticeOfClient(Long userLoginId, Long userIdConversation, String keySearch, Map<String, Object> parram, String querySearch) {
        parram.put("userIdConversation", userIdConversation);
        parram.put("userLoginId", userLoginId);
        if (!DataUtil.isNullOrEmpty(keySearch)) {
            parram.put("keySearch", keySearch);
            querySearch = querySearch.replace("#extendCondition", " And n.content like '%:keySearch% ");
        } else {
            querySearch = querySearch.replace("#extendCondition", " ");
        }
        return querySearch;
    }
}
