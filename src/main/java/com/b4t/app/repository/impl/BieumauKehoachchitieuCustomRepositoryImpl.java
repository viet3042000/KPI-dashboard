package com.b4t.app.repository.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.repository.BieumauKehoachchitieuCustomRepository;
import com.b4t.app.repository.DbWork;
import com.b4t.app.service.dto.BieumauKehoachchitieuDTO;
import com.b4t.app.service.dto.SearchBieuMauParram;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class BieumauKehoachchitieuCustomRepositoryImpl implements BieumauKehoachchitieuCustomRepository {
    private final EntityManager entityManager;
    private static final String QUERY_SEARCH = " select a.*,\n" +
        "       b.DOMAIN_CODE as DOMAIN_CODE,\n" +
        "       d.item_name as DOMAIN_NAME,\n" +
        "       c.ITEM_CODE as group_kpi_code,\n" +
        "       c.ITEM_NAME as group_kpi_name,\n" +
        "       '' as alarm_plan_type,\n" +
        "       e.item_name as unit_name\n" +
        " from bieumau_kehoachchitieu a\n" +
        "         inner join cat_graph_kpi b on a.KPI_ID = b.KPI_ID and b.STATUS = 1\n" +
        "         inner join cat_item c on b.GROUP_KPI_CODE = c.ITEM_VALUE and c.CATEGORY_CODE = 'GROUP_KPI' and c.STATUS = 1\n" +
        "         inner join cat_item d on b.DOMAIN_CODE = d.ITEM_CODE and d.CATEGORY_CODE = 'DOMAIN' and d.STATUS = 1\n" +
        "         left  join cat_item e on b.UNIT_KPI  = e.ITEM_CODE and e.CATEGORY_CODE = 'UNIT' and e.STATUS = 1 \n" +
        " where 1 = 1 ";
    private static final String QUERY_COUNT = " select count(1) " +
        " from bieumau_kehoachchitieu a\n" +
        "         inner join cat_graph_kpi b on a.KPI_ID = b.KPI_ID and b.STATUS = 1\n" +
        "         inner join cat_item c on b.GROUP_KPI_CODE = c.ITEM_VALUE and c.CATEGORY_CODE = 'GROUP_KPI' and c.STATUS = 1\n" +
        "         inner join cat_item d on b.DOMAIN_CODE = d.ITEM_CODE and d.CATEGORY_CODE = 'DOMAIN' and d.STATUS = 1\n" +
        "         left  join cat_item e on b.UNIT_KPI  = e.ITEM_CODE and e.CATEGORY_CODE = 'UNIT' and e.STATUS = 1 \n" +
        " where 1 = 1 ";

    public BieumauKehoachchitieuCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<BieumauKehoachchitieuDTO> query(SearchBieuMauParram searchBieuMauParram, Pageable pageable) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder strQuery = new StringBuilder(QUERY_SEARCH);
        createQueryCondition(searchBieuMauParram, param, strQuery);
        strQuery.append(" order by b.DOMAIN_CODE, c.ITEM_NAME, b.KPI_ID ");
        Query query = entityManager.createNativeQuery(strQuery.toString(), BieumauKehoachchitieuDTO.class);
        DbUtils.setParramToQuery(query, param);
        if (pageable.getPageSize() > 0) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        List<BieumauKehoachchitieuDTO> lst = query.getResultList();

        Integer countResult = countResult(searchBieuMauParram);
        return new PageImpl<>(lst, pageable, countResult);
    }

    private Integer countResult(SearchBieuMauParram searchBieuMauParram) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder strQuery = new StringBuilder(QUERY_COUNT);
        createQueryCondition(searchBieuMauParram, param, strQuery);
        Query query = entityManager.createNativeQuery(strQuery.toString());
        DbUtils.setParramToQuery(query, param);
        return DataUtil.safeToInt(query.getSingleResult());
    }

    private void createQueryCondition(SearchBieuMauParram bieumauKehoachchitieuDTO, Map<String, Object> param, StringBuilder strQuery) {
        if (!DataUtil.isNullOrEmpty(bieumauKehoachchitieuDTO.getDomainCode())) {
            strQuery.append(" and b.DOMAIN_CODE = :domainCode ");
            param.put("domainCode", bieumauKehoachchitieuDTO.getDomainCode());
        }
        if (!DataUtil.isNullOrEmpty(bieumauKehoachchitieuDTO.getGroupKpiCode())) {
            strQuery.append(" and b.GROUP_KPI_CODE = :groupKpiCode ");
            param.put("groupKpiCode", bieumauKehoachchitieuDTO.getGroupKpiCode());
        }
        if (bieumauKehoachchitieuDTO.getKpiId() != null) {
            strQuery.append(" and a.kpi_id = :kpiId ");
            param.put("kpiId", bieumauKehoachchitieuDTO.getKpiId());
        }
        if (bieumauKehoachchitieuDTO.getStatus() != null) {
            strQuery.append(" and a.status = :status ");
            param.put("status", bieumauKehoachchitieuDTO.getStatus());
        }
        if(bieumauKehoachchitieuDTO.getPrdId() != null){
            strQuery.append(" and  a.prd_id = :prdId ");
            param.put("prdId", bieumauKehoachchitieuDTO.getPrdId());

        }

    }
}
