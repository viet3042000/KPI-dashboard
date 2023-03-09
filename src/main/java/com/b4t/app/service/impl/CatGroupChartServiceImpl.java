package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.CatGraphKpiRepository;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.CatGroupChartService;
import com.b4t.app.repository.CatGroupChartRepository;
import com.b4t.app.service.dto.CatGroupChartDTO;
import com.b4t.app.service.mapper.CatGroupChartMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CatGroupChart}.
 */
@Service
@Transactional
public class CatGroupChartServiceImpl implements CatGroupChartService {

    private final Logger log = LoggerFactory.getLogger(CatGroupChartServiceImpl.class);

    private final CatGroupChartRepository catGroupChartRepository;

    private final CatItemRepository catItemRepository;
    private final CatGraphKpiRepository catGraphKpiRepository;
    private final CatGroupChartMapper catGroupChartMapper;

    private final EntityManager entityManager;


    public CatGroupChartServiceImpl(CatGroupChartRepository catGroupChartRepository, CatItemRepository catItemRepository, CatGraphKpiRepository catGraphKpiRepository, CatGroupChartMapper catGroupChartMapper, EntityManager entityManager) {
        this.catGroupChartRepository = catGroupChartRepository;
        this.catItemRepository = catItemRepository;
        this.catGraphKpiRepository = catGraphKpiRepository;
        this.catGroupChartMapper = catGroupChartMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a catGroupChart.
     *
     * @param catGroupChartDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CatGroupChartDTO save(CatGroupChartDTO catGroupChartDTO) {
        log.debug("Request to save CatGroupChart : {}", catGroupChartDTO);
        CatGroupChart catGroupChart = catGroupChartMapper.toEntity(catGroupChartDTO);
        catGroupChart.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        catGroupChart.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        catGroupChart = catGroupChartRepository.save(catGroupChart);
        return catGroupChartMapper.toDto(catGroupChart);
    }

    /**
     * Get all the catGroupCharts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CatGroupChartDTO> findAll(String keyword, String[] groupKpiCodes, Long[] screenIds, Long status, Pageable pageable) {
        log.debug("Request to get all CatGroupCharts");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CatGroupChart> criteria = cb.createQuery(CatGroupChart.class);
        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<CatGroupChart> root = criteria.from(CatGroupChart.class);
        Root<CatGroupChart> rootCount = countCrit.from(CatGroupChart.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate keywordWCls = cb.or(
                cb.like(cb.lower(root.get(CatGroupChart_.GROUP_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(CatGroupChart_.GROUP_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(keywordWCls);
            countPredicates.add(keywordWCls);
        }
        if (!DataUtil.isNullOrEmpty(groupKpiCodes)) {
            Expression<String> inExpression = root.get(CatGroupChart_.GROUP_KPI_CODE);
            predicates.add(inExpression.in(groupKpiCodes));
            countPredicates.add(inExpression.in(groupKpiCodes));
        }
        if (!DataUtil.isNullOrEmpty(screenIds)) {
            Root<ConfigChart> chart = criteria.from(ConfigChart.class);
            Root<ConfigMapChartArea> mapChartArea = criteria.from(ConfigMapChartArea.class);
            Root<ConfigArea> area = criteria.from(ConfigArea.class);
            predicates.add(cb.equal(root.get(CatGraphKpi_.ID), chart.get(ConfigChart_.GROUP_CHART_ID)));
            predicates.add(cb.equal(chart.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));

            predicates.add(cb.equal(chart.get(CatGraphKpi_.ID), mapChartArea.get(ConfigMapChartArea_.CHART_ID)));
            predicates.add(cb.equal(mapChartArea.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));

            predicates.add(cb.equal(area.get(CatGraphKpi_.ID), mapChartArea.get(ConfigMapChartArea_.AREA_ID)));
            predicates.add(cb.equal(area.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));

            Expression<String> inExpression = area.get(ConfigArea_.SCREEN_ID);
            predicates.add(inExpression.in(screenIds));

            chart = countCrit.from(ConfigChart.class);
            mapChartArea = countCrit.from(ConfigMapChartArea.class);
            area = countCrit.from(ConfigArea.class);
            countPredicates.add(cb.equal(rootCount.get(CatGraphKpi_.ID), chart.get(ConfigChart_.GROUP_CHART_ID)));
            countPredicates.add(cb.equal(chart.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));

            countPredicates.add(cb.equal(chart.get(CatGraphKpi_.ID), mapChartArea.get(ConfigMapChartArea_.CHART_ID)));
            countPredicates.add(cb.equal(mapChartArea.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));

            countPredicates.add(cb.equal(area.get(CatGraphKpi_.ID), mapChartArea.get(ConfigMapChartArea_.AREA_ID)));
            countPredicates.add(cb.equal(area.get(ConfigChart_.STATUS), Constants.STATUS_ACTIVE));

            inExpression = area.get(ConfigArea_.SCREEN_ID);
            countPredicates.add(inExpression.in(screenIds));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(CatGroupChart_.STATUS), status));
        countPredicates.add(cb.equal(rootCount.get(CatGroupChart_.STATUS), status));

        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        criteria.select(root).distinct(true);

        List<CatGroupChart> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        countCrit.where(cb.and(countPredicates.toArray(new Predicate[countPredicates.size()])));
        countCrit.select(cb.countDistinct(rootCount));
        Long count = entityManager.createQuery(countCrit).getSingleResult();
        List<CatGroupChartDTO> rsDTOs = catGroupChartMapper.toDto(rs);
        updateCatGroupChart(rsDTOs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    private void updateCatGroupChart(List<CatGroupChartDTO> rsDTOs) {
        if (DataUtil.isNullOrEmpty(rsDTOs)) return;
        rsDTOs.forEach(bean -> {
            CatItem catItem = catItemRepository.findDomainByGroupKpiCode(bean.getGroupKpiCode());
            CatItem catItemGroupKpiCode = catItemRepository.findFirstByItemCodeAndStatus(bean.getGroupKpiCode(), Constants.STATUS_ACTIVE);
            if (bean.getKpiIdMain() != null) {
                Optional<CatGraphKpi> catGrapOpn = catGraphKpiRepository.findFirstByKpiIdAndStatus(bean.getKpiIdMain(), Constants.STATUS_ACTIVE);
                if (catGrapOpn.isPresent()) {
                    bean.setKpiMainName(catGrapOpn.get().getKpiName());
                }
            }
            if (catItem != null) {
                bean.setDomainName(catItem.getItemName());
            }
            if (catItemGroupKpiCode != null) {
                bean.setGroupKpiName(catItemGroupKpiCode.getItemName());
            }
        });
    }

    /**
     * Get one catGroupChart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CatGroupChartDTO> findOne(Long id) {
        log.debug("Request to get CatGroupChart : {}", id);
        return catGroupChartRepository.findById(id)
            .map(catGroupChartMapper::toDto);
    }

    @Override
    public Optional<CatGroupChartDTO> findByCode(String code) {
        log.debug("Request to get CatGroupChart : {}", code);
        return catGroupChartRepository.findFirstByGroupCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(catGroupChartMapper::toDto);
    }

    /**
     * Delete the catGroupChart by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatGroupChart : {}", id);
        Optional<CatGroupChart> entityOption = catGroupChartRepository.findById(id);
        if (entityOption.isPresent()) {
            CatGroupChart entity = entityOption.get();
            entity.setStatus(Constants.STATUS_DISABLED);
            entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            catGroupChartRepository.save(entity);
        }
    }
}
