package com.b4t.app.repository.impl;

import com.b4t.app.commons.BuildParamAndQueryUtils;
import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DbUtils;
import com.b4t.app.config.CatGrapKpiQuery;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.RptGraph;
import com.b4t.app.repository.CatGraphKpiCustomRepository;
import com.b4t.app.service.dto.CatGrapKpiExtendDTO;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.dto.ConfigReportDetailDTO;
import com.b4t.app.service.dto.SaveKpiInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional
public class CatGraphKpiCustomRepositoryImpl implements CatGraphKpiCustomRepository {

    private static final Logger logger = LoggerFactory.getLogger(CatGraphKpiCustomRepositoryImpl.class);
    private final EntityManager entityManager;

    public CatGraphKpiCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<CatGraphKpiDTO> findAll(String keyword, List<Long> kpiIds, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select " +
            " cgk.ID " +
            " ,cgk.KPI_CODE " +
            " ,cgk.KPI_ID " +
            " ,cgk.KPI_DISPLAY " +
            " ,cgk.RATE " +
            " ,cgk.UNIT_KPI " +
            " ,cgk.UNIT_VIEW_CODE " +
            " ,cgk.DESCRIPTION " +
            " ,cgk.SOURCE " +
            " ,cgk.DOMAIN_CODE " +
            " ,d.ITEM_NAME domain_name" +
            " ,d.ITEM_VALUE table_name" +
            " ,cgk.GROUP_KPI_CODE " +
            " ,grpKpi.ITEM_NAME group_kpi_name" +
            " ,IF(u.ITEM_NAME is null, cgk.KPI_DISPLAY,concat(cgk.KPI_DISPLAY,' (',u.ITEM_NAME,')')) as kpi_name " +
            " ,u.ITEM_NAME unit_name " +
            " from cat_graph_kpi cgk  " +
            " inner join cat_item d on d.CATEGORY_CODE ='DOMAIN_TABLE' and cgk.DOMAIN_CODE = d.ITEM_CODE and d.STATUS = 1 " +
            " inner join cat_item grpKpi on cgk.GROUP_KPI_CODE = grpKpi.ITEM_CODE and grpKpi.STATUS = 1 and grpKpi.CATEGORY_CODE = 'GROUP_KPI' " +
            " left join cat_item u on cgk.UNIT_VIEW_CODE = u.ITEM_CODE and u.STATUS = 1 and u.CATEGORY_CODE = 'UNIT' " +
            " where cgk.STATUS = 1 ");
        if (StringUtils.isNotEmpty(keyword)) {
            sql.append(" and (cgk.KPI_CODE like :keyword escape :escapeChr or cgk.KPI_NAME like :keyword escape :escapeChr) ");
        }
        if (!DataUtil.isNullOrEmpty(kpiIds)) {
            sql.append(" and cgk.KPI_ID IN (:kpiIds) ");
        }
        sql.append(" order by d.POSITION asc, grpKpi.POSITION asc, cgk.KPI_NAME asc" +
            " limit :offset , :pageSize ");

        Query query = entityManager.createNativeQuery(sql.toString());
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = StringUtils.isNotEmpty(keyword) ? DataUtil.makeLikeParam(keyword) : StringUtils.EMPTY;
            query.setParameter("keyword", keyword);
            query.setParameter("escapeChr", Constants.DEFAULT_ESCAPE_CHAR);
        }
        if (!DataUtil.isNullOrEmpty(kpiIds)) {
            query.setParameter("kpiIds", kpiIds);
        }
        query.setParameter("offset", pageable.getOffset());
        query.setParameter("pageSize", pageable.getPageSize());
        org.hibernate.query.Query hibernateQuery = query.unwrap(org.hibernate.query.Query.class);
        ObjectMapper mapper = new ObjectMapper();
        hibernateQuery.setResultTransformer(new ResultTransformer() {
            @Override
            public CatGraphKpiDTO transformTuple(Object[] rowData, String[] aliasNames) {
                return DbUtils.transformTuple(mapper, rowData, aliasNames, CatGraphKpiDTO.class);
            }

            @Override
            public List<CatGraphKpiDTO> transformList(List list) {
                return DataUtil.isNullOrEmpty(list)
                    ? new ArrayList<>()
                    : (List<CatGraphKpiDTO>) list.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            }
        });
        List<CatGraphKpiDTO> list = hibernateQuery.list();
        long cnt = countAll(keyword, kpiIds);
        return new PageImpl<>(list, pageable, cnt);
    }

    private long countAll(String keyword, List<Long> kpiIds) {
        StringBuilder countSql = new StringBuilder("select count(*) " +
            " from cat_graph_kpi cgk  " +
            " inner join cat_item d on d.CATEGORY_CODE ='DOMAIN_TABLE' and cgk.DOMAIN_CODE = d.ITEM_CODE and d.STATUS = 1 " +
            " inner join cat_item grpKpi on cgk.GROUP_KPI_CODE = grpKpi.ITEM_CODE and grpKpi.STATUS = 1 and grpKpi.CATEGORY_CODE = 'GROUP_KPI' " +
            " left join cat_item u on cgk.UNIT_VIEW_CODE = u.ITEM_CODE and u.STATUS = 1 and u.CATEGORY_CODE = 'UNIT' " +
            " where cgk.STATUS = 1 ");
        if (StringUtils.isNotEmpty(keyword)) {
            countSql.append(" and (cgk.KPI_CODE like :keyword escape :escapeChr or cgk.KPI_NAME like :keyword escape :escapeChr)");
        }
        if (!DataUtil.isNullOrEmpty(kpiIds)) {
            countSql.append(" and cgk.KPI_ID IN (:kpiIds) ");
        }
        countSql.append(" order by d.POSITION asc, grpKpi.POSITION asc, cgk.KPI_NAME asc");
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        if (StringUtils.isNotEmpty(keyword)) {
            countQuery.setParameter("keyword", keyword);
            countQuery.setParameter("escapeChr", Constants.DEFAULT_ESCAPE_CHAR);
        }
        if (!DataUtil.isNullOrEmpty(kpiIds)) {
            countQuery.setParameter("kpiIds", kpiIds);
        }
        return ((Number) countQuery.getSingleResult()).longValue();
    }

    public List<CatGraphKpiDTO> getListCatGraphKpi(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType) {
        Map<String, Object> parram = new HashMap<>();
        StringBuilder sql = new StringBuilder(" " +
            " select a.*, '' as table_name, '' as alarm_threshold_type_name, '' as kpi_type_name, \n" +
            "       catUnit.ITEM_NAME as unit_name,\n" +
            "       catUnitView.ITEM_NAME as unit_view_name,\n" +
            "       catDomain.ITEM_NAME as domain_name,\n" +
            "       catAlarmPlanType.ITEM_NAME as alarm_plan_type_name,\n" +
            "       catGroupKpi.ITEM_NAME as group_kpi_name\n" +
            "from cat_graph_kpi a\n" +
            "    left join (select * from cat_item where CATEGORY_CODE = 'UNIT') catUnit on a.UNIT_KPI = catUnit.ITEM_CODE\n" +
            "    left join (select * from cat_item where CATEGORY_CODE = 'UNIT') catUnitView on a.UNIT_VIEW_CODE = catUnitView.ITEM_CODE\n" +
            "    left join (select * from cat_item where CATEGORY_CODE = 'DOMAIN') catDomain on a.DOMAIN_CODE = catDomain.ITEM_CODE\n" +
            "    left join (select * from cat_item where CATEGORY_CODE = 'GROUP_KPI') catGroupKpi on a.GROUP_KPI_CODE = catGroupKpi.ITEM_CODE\n" +
            "    left join (select * from cat_item where CATEGORY_CODE = 'ALARM_PLAN_TYPE') catAlarmPlanType on a.ALARM_PLAN_TYPE = catAlarmPlanType.ITEM_VALUE\n" +
            "where a.STATUS = 1    \n ");
//        parram.put("escapeChr", Constants.DEFAULT_ESCAPE_CHAR);
        if (!DataUtil.isNullOrEmpty(keyword)) {
            sql.append(" and ((lower(a.KPI_CODE) like (:keyword) escape '&') or (lower(a.kpi_name) like (:keyword) escape '&'))  ");
            parram.put("keyword", "%" + keyword.toLowerCase() + "%");
        }
        if (kpiId != null) {
            sql.append(" and a.kpi_id = :kpiId ");
            parram.put("kpiId", kpiId);
        }
        if (kpiType != null) {
            sql.append(" and a.kpi_type = :kpiType ");
            parram.put("kpiType", kpiType);
        }
        if (!DataUtil.isNullOrEmpty(domainCode)) {
            sql.append(" and a.domain_code = :domainCode ");
            parram.put("domainCode", domainCode.toLowerCase());
        }
        if (!DataUtil.isNullOrEmpty(groupKpiCode)) {
            sql.append(" and a.GROUP_KPI_CODE = :groupKpiCode ");
            parram.put("groupKpiCode", groupKpiCode);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), CatGraphKpiDTO.class);
        DbUtils.setParramToQuery(query, parram);
        List<CatGraphKpiDTO> results = query.getResultList();
        return results;
    }

    public List<CatGraphKpiDTO> getListKpiForMaps(CatGrapKpiExtendDTO catGrapKpiExtendDTO) {
        Query query = entityManager.createNativeQuery(CatGrapKpiQuery.KPIS_ON_MAP);
        query.setParameter("DOMAIN_CODE", catGrapKpiExtendDTO.getDomainCode());
        query.setParameter("TIMETYPE", catGrapKpiExtendDTO.getTimeType());
        List<Object[]> lst = query.getResultList();
        List<CatGraphKpiDTO> results = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(lst)) {
            CatGraphKpiDTO catGraphKpiDTO;
            for (Object[] obj : lst) {
                catGraphKpiDTO = new CatGraphKpiDTO(
                    DataUtil.safeToLong(obj[0]),
                    DataUtil.safeToString(obj[1]),
                    DataUtil.safeToString(obj[2])
                );
                results.add(catGraphKpiDTO);
            }
        }
        return results;
    }

    @Override
    public Page<RptGraph> getDstDataByCatGraphKpi(CatGraphKpiDTO catGraphKpiDTO, Pageable pageable) {
        Long kpiId = catGraphKpiDTO.getKpiId();
        StringBuilder sql = new StringBuilder("select * from " + catGraphKpiDTO.getTableName() +
            "\n where kpi_id = :kpiId" +
            "\n order by prd_id, time_type asc");

        StringBuilder sqlCount = new StringBuilder(" SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        sqlCount.append(" record_count");
        sql.append(" LIMIT :startIndex , :pageSize");

        Query query = entityManager.createNativeQuery(sql.toString(), RptGraph.class);
        Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
        query.setParameter("kpiId", kpiId);
        queryCount.setParameter("kpiId", kpiId);
        query.setParameter("startIndex", pageable.getOffset());
        query.setParameter("pageSize", pageable.getPageSize());

        List<RptGraph> results = new ArrayList<>();
        Integer totalCount = 0;
        try {
            results = query.getResultList();
            totalCount = ((BigInteger) queryCount.getSingleResult()).intValue();
        } catch (Exception e) {
            logger.error("Error: ", e);
        }
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRptGraphById(Long id , String tableName) {
        StringBuffer sb = new StringBuffer("");
        sb.append("delete from `").append(DataUtil.removeSchemaName(tableName)).append("`");
        sb.append("\n where id = :id");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRptGraphByKpiId(Long kpiId , String tableName) {
        StringBuffer sb = new StringBuffer("");
        sb.append("delete from `").append(DataUtil.removeSchemaName(tableName)).append("`");
        sb.append("\n where kpi_id = :kpiId");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("kpiId", kpiId);
        query.executeUpdate();
    }
}
