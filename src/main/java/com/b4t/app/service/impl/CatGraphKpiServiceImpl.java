package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.CatGraphKpiCustomRepository;
import com.b4t.app.repository.CatGraphKpiRepository;
import com.b4t.app.repository.CatKpiSynonymRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.CatGraphKpiService;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.CatGraphKpiDetailMapper;
import com.b4t.app.service.mapper.CatGraphKpiMapper;
import com.b4t.app.service.mapper.RptGraphMapper;
import io.undertow.util.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CatGraphKpi}.
 */
@Service
@Transactional
public class CatGraphKpiServiceImpl implements CatGraphKpiService {

    private final Logger log = LoggerFactory.getLogger(CatGraphKpiServiceImpl.class);

    private final CatGraphKpiRepository catGraphKpiRepository;

    private final CatGraphKpiMapper catGraphKpiMapper;

    private final CatItemService catItemService;

    private final CatGraphKpiCustomRepository catGraphKpiCustomRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    CatKpiSynonymRepository catKpiSynonymRepository;

    @Autowired
    CatGraphKpiDetailMapper catGraphKpiDetailMapper;

    @Autowired
    RptGraphMapper rptGraphMapper;

    public CatGraphKpiServiceImpl(
        CatGraphKpiRepository catGraphKpiRepository,
        CatGraphKpiMapper catGraphKpiMapper,
        CatItemService catItemService,
        CatGraphKpiCustomRepository catGraphKpiCustomRepository) {
        this.catGraphKpiRepository = catGraphKpiRepository;
        this.catGraphKpiMapper = catGraphKpiMapper;
        this.catItemService = catItemService;
        this.catGraphKpiCustomRepository = catGraphKpiCustomRepository;
    }

    /**
     * Save a catGraphKpi.
     *
     * @param catGraphKpiDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CatGraphKpiDTO save(CatGraphKpiDTO catGraphKpiDTO) {
        log.debug("Request to save CatGraphKpi : {}", catGraphKpiDTO);
        CatGraphKpi catGraphKpi = catGraphKpiMapper.toEntity(catGraphKpiDTO);

        List<String> lstInsert;
        if (!DataUtil.isNullOrEmpty(catGraphKpiDTO.getId())) {
            List<CatKpiSynonym> lstSynonym = catKpiSynonymRepository.findAllByKpiId(catGraphKpiDTO.getKpiId());
            List<CatKpiSynonym> lstDelete = lstSynonym.stream().filter(e -> !catGraphKpiDTO.getSynonym().contains(e.getSynonym())).collect(Collectors.toList());
            List<String> oldSyn = lstSynonym.stream().map(CatKpiSynonym::getSynonym).collect(Collectors.toList());
            lstInsert = catGraphKpiDTO.getSynonym().stream().filter(e -> !oldSyn.contains(e)).collect(Collectors.toList());
            catKpiSynonymRepository.deleteAll(lstDelete);
        } else {
            lstInsert = catGraphKpiDTO.getSynonym();
        }

        if (!DataUtil.isNullOrEmpty(lstInsert)) {
            List<CatKpiSynonym> lstInsertSyn = lstInsert.stream().map(e -> {
                CatKpiSynonym catKpiSynonym = new CatKpiSynonym();
                catKpiSynonym.setKpiId(catGraphKpiDTO.getKpiId());
                catKpiSynonym.setSynonym(e);
                return catKpiSynonym;
            }).collect(Collectors.toList());
            catKpiSynonymRepository.saveAll(lstInsertSyn);
        }
        String synonyms = null;
        if (!DataUtil.isNullOrEmpty(catGraphKpiDTO.getSynonym())) {
            synonyms = StringUtils.join(catGraphKpiDTO.getSynonym(), ", ");
        }
        catGraphKpi.setSynonyms(synonyms);

        catGraphKpi.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        catGraphKpi.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        catGraphKpi = catGraphKpiRepository.save(catGraphKpi);
        return catGraphKpiMapper.toDto(catGraphKpi);
    }

    public List<CatGraphKpiDTO> getKpiMain(String groupKpiCode, String domainCode) {
        List<CatGraphKpi> catGraphKpis = catGraphKpiRepository.findAllByGroupKpiCodeAndDomainCodeAndKpiTypeAndStatus(groupKpiCode, domainCode, Constants.STATUS_DISABLED, Constants.STATUS_ACTIVE);
        List<CatGraphKpiDTO> rsDTOs = catGraphKpiMapper.toDto(catGraphKpis);
        return rsDTOs;
    }

    /**
     * Get all the catGraphKpis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CatGraphKpiDTO> findAll(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType, Integer showOnMap, Long status, Pageable pageable) {
        log.debug("Request to get all CatGraphKpis");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CatGraphKpi> criteria = cb.createQuery(CatGraphKpi.class);
        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<CatGraphKpi> root = criteria.from(CatGraphKpi.class);
        Root<CatGraphKpi> rootCount = countCrit.from(CatGraphKpi.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate keywordWCls = cb.or(
                cb.like(cb.lower(root.get(CatGraphKpi_.KPI_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(CatGraphKpi_.KPI_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(keywordWCls);
            countPredicates.add(keywordWCls);
        }
        if (StringUtils.isNotEmpty(domainCode)) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.DOMAIN_CODE), domainCode));
            countPredicates.add(cb.equal(root.get(CatGraphKpi_.DOMAIN_CODE), domainCode));
        }
        if (kpiId != null) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.kpiId), kpiId));
            countPredicates.add(cb.equal(root.get(CatGraphKpi_.kpiId), kpiId));
        }
        if (kpiType != null) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.kpiType), kpiType));
            countPredicates.add(cb.equal(root.get(CatGraphKpi_.kpiType), kpiType));
        }
        if (StringUtils.isNotEmpty(groupKpiCode)) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.GROUP_KPI_CODE), groupKpiCode));
            countPredicates.add(cb.equal(root.get(CatGraphKpi_.GROUP_KPI_CODE), groupKpiCode));
        }
        if (showOnMap != null) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.SHOW_ON_MAP), showOnMap));
            countPredicates.add(cb.equal(root.get(CatGraphKpi_.SHOW_ON_MAP), showOnMap));
        }

        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(CatGraphKpi_.STATUS), status));
        countPredicates.add(cb.equal(rootCount.get(CatGraphKpi_.STATUS), status));

        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<CatGraphKpi> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        countCrit.where(cb.and(countPredicates.toArray(new Predicate[countPredicates.size()])));
        countCrit.select(cb.countDistinct(rootCount));
        Long count = entityManager.createQuery(countCrit).getSingleResult();
        List<CatGraphKpiDTO> rsDTOs = catGraphKpiMapper.toDto(rs);
        updateInfo(rsDTOs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    private void updateInfo(List<CatGraphKpiDTO> rsDTOs) {
        if (DataUtil.isNullOrEmpty(rsDTOs)) return;
        rsDTOs.forEach(bean -> {
            CatItemDTO catUnit = catItemService.findFirstByCategoryCodeAndItemCodeAndStatus(Constants.CATEGORY.UNIT_CATEOGRY, bean.getUnitKpi(), Constants.STATUS_ACTIVE);
            if (catUnit != null) bean.setUnitName(catUnit.getItemName());
            CatItemDTO catUnitView = catItemService.findFirstByCategoryCodeAndItemCodeAndStatus(Constants.CATEGORY.UNIT_CATEOGRY, bean.getUnitViewCode(), Constants.STATUS_ACTIVE);
            if (catUnitView != null) bean.setUnitViewName(catUnitView.getItemName());
            CatItemDTO catGroupKpi = catItemService.findFirstByCategoryCodeAndItemCodeAndStatus(Constants.CATEGORY.GROUP_KPI, bean.getGroupKpiCode(), Constants.STATUS_ACTIVE);
            if (catGroupKpi != null) bean.setGroupKpiName(catGroupKpi.getItemName());
            CatItemDTO domain = catItemService.findFirstByCategoryCodeAndItemCodeAndStatus(Constants.CATEGORY.DOMAIN_CATEOGRY, bean.getDomainCode(), Constants.STATUS_ACTIVE);
            if (domain != null) bean.setDomainName(domain.getItemName());
        });
    }

    /**
     * Get all the catGraphKpis.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CatGraphKpiDTO> findAll(String keyword, Long kpiId, String domainCode, String groupKpiCode, Long kpiType, Long status) {
        /*log.debug("Request to get all CatGraphKpis");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CatGraphKpi> criteria = cb.createQuery(CatGraphKpi.class);
        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<CatGraphKpi> root = criteria.from(CatGraphKpi.class);
        Root<CatGraphKpi> rootCount = countCrit.from(CatGraphKpi.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate keywordWCls = cb.or(
                cb.like(cb.lower(root.get(CatGraphKpi_.KPI_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(CatGraphKpi_.KPI_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(keywordWCls);
        }
        if (StringUtils.isNotEmpty(domainCode)) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.DOMAIN_CODE), domainCode));
        }
        if (kpiId != null) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.kpiId), kpiId));
        }
        if (kpiType != null) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.kpiType), kpiType));
        }
        if (StringUtils.isNotEmpty(groupKpiCode)) {
            predicates.add(cb.equal(root.get(CatGraphKpi_.GROUP_KPI_CODE), groupKpiCode));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(CatGraphKpi_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        List<CatGraphKpi> rs = entityManager.createQuery(criteria).getResultList();
        List<CatGraphKpiDTO> rsDTOs = catGraphKpiMapper.toDto(rs);
        updateInfo(rsDTOs);*/
        List<CatGraphKpiDTO> rsDTOs = catGraphKpiCustomRepository.getListCatGraphKpi(keyword, kpiId, domainCode, groupKpiCode, kpiType);
        return rsDTOs;
    }

    public List<CatGraphKpiDTO> getListKpiForMaps(CatGrapKpiExtendDTO catGrapKpiExtendDTO) {
        return catGraphKpiCustomRepository.getListKpiForMaps(catGrapKpiExtendDTO);
    }

    @Override
    public Page<CatGraphKpiDTO> findAll(String keyword, List<Long> kpiIds, Pageable pageable) {
        return catGraphKpiCustomRepository.findAll(keyword, kpiIds, pageable);
    }

    @Override
    public Optional<CatGraphKpiDTO> findByCode(String code) {
        log.debug("Request to get CatGroupChart : {}", code);
        return catGraphKpiRepository.findFirstByKpiCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(catGraphKpiMapper::toDto);
    }

    /**
     * Get one catGraphKpi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CatGraphKpiDTO> findOne(Long id) {
        log.debug("Request to get CatGraphKpi : {}", id);
        return catGraphKpiRepository.findById(id).map(catGraphKpiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CatGraphKpiDTO> findByKpiId(Long kpiId) {
        log.debug("Request to get CatGraphKpi with KPI_ID : {}", kpiId);
        Optional<CatGraphKpi> cgk = catGraphKpiRepository.findFirstByKpiIdAndStatus(kpiId, Constants.STATUS_ACTIVE);
        if (cgk.isPresent()) {
            Optional<CatItemDTO> catItem = catItemService.findByCode(cgk.get().getUnitViewCode());
            CatGraphKpiDTO dto = catGraphKpiMapper.toDto(cgk.get());
            catItem.ifPresent(catItemDTO -> dto.setUnitName(catItemDTO.getItemName()));
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatGraphKpiDTO> findByKpiIds(List<Long> kpiIds) {
        log.debug("Request to get CatGraphKpi with KPI_ID : {}", kpiIds);
        List<CatGraphKpi> entities = catGraphKpiRepository.findAllByKpiIdInAndStatus(kpiIds, Constants.STATUS_ACTIVE);
        List<CatGraphKpiDTO> rs = catGraphKpiMapper.toDto(entities);
        if (!DataUtil.isNullOrEmpty(rs)) {
            List<String> codes = rs.stream().map(CatGraphKpiDTO::getUnitViewCode).filter(Objects::nonNull).collect(Collectors.toList());
            List<CatItemDTO> items = catItemService.findByCodes(codes);
            if (!DataUtil.isNullOrEmpty(items)) {
                rs = rs.stream().peek(k -> {
                    Optional<CatItemDTO> item = items.stream().filter(i -> StringUtils.isNotEmpty(k.getUnitViewCode())
                        && k.getUnitViewCode().equals(i.getItemCode())).findFirst();
                    item.ifPresent(catItemDTO -> k.setUnitName(catItemDTO.getItemName()));
                }).collect(Collectors.toList());
            }
        }
        return rs;
    }
    /**
     * Delete the catGraphKpi by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatGraphKpi : {}", id);
        Optional<CatGraphKpi> entityOption = catGraphKpiRepository.findById(id);
        if (entityOption.isPresent()) {
            CatGraphKpi entity = entityOption.get();
            entity.setStatus(Constants.STATUS_DISABLED);
            entity.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            entity.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            catGraphKpiRepository.save(entity);
        }
    }

    @Override
    public List<CatGraphKpiDTO> getGraphKpiByDomain(String domainCode) {
        return catGraphKpiRepository.findAllByDomainCodeAndStatus(domainCode, Constants.STATUS_ACTIVE, Sort.by(Sort.Direction.ASC, "kpiId", "kpiName")).stream()
            .map(catGraphKpiMapper::toDto).peek(e -> {
                e.setKpiName(e.getKpiId() + "_" + e.getKpiName());
                e.setKpiNameCombo(e.getKpiName());
            }).collect(Collectors.toList());
    }

    @Override
    public List<ComboDTO> onSearchHashTag(String keyword) {
        return catKpiSynonymRepository.findAllBySynonymLike(keyword).stream().
            map(e -> {
                ComboDTO comboDTO = new ComboDTO();
                comboDTO.setLabel(e.toString());
                comboDTO.setValue(e);
                return comboDTO;
            }).collect(Collectors.toList());
    }

    @Override
    public Page<CatGraphKpiDetailDTO> onSearch(CatGraphKpiDetailDTO dto, Pageable pageable) {
        return catGraphKpiRepository.onSearch(DataUtil.makeLikeParam(dto.getKeySearch()),
            dto.getDomainCode(), dto.getKpiId(), dto.getKpiType(), dto.getGroupKpiCode(), dto.getStatus(),
            DataUtil.isNullOrEmpty(dto.getSynonym()) ? null : 1L, dto.getSynonym(), dto.getKpiOriginId(), pageable).
            map(catGraphKpiDetailMapper::toDto);
    }

    @Override
    public List<CatGraphKpiDetailDTO> onExport(CatGraphKpiDetailDTO dto) {
        return catGraphKpiRepository.onExport(DataUtil.makeLikeParam(dto.getKeySearch()),
            dto.getDomainCode(), dto.getKpiId(), dto.getKpiType(), dto.getGroupKpiCode(), dto.getStatus(),
            DataUtil.isNullOrEmpty(dto.getSynonym()) ? null : 1L, dto.getSynonym()).stream().
            map(catGraphKpiDetailMapper::toDto).peek(bean -> {
            bean.setKpiTypeName(Constants.KPI_TYPE.ORIGINAL.equals(bean.getKpiType()) ? Translator.toLocale("catGraphKpi.label.original") : Translator.toLocale("catGraphKpi.label.synthetic"));
            bean.setAlarmThresholdTypeName(Constants.THRESHOLDTYPE.POSITIVE.equals(bean.getAlarmThresholdType()) ? Translator.toLocale("catGraphKpi.column.positive") : Translator.toLocale("catGraphKpi.column.negative"));
        }).collect(Collectors.toList());
    }

    @Override
    public List<CatGraphKpiDTO> getAllCatGraphKpi() {
        return catGraphKpiRepository.findAllByStatus(Constants.STATUS_ACTIVE).stream()
            .map(catGraphKpiMapper::toDto).peek(e-> e.setKpiNameCombo(e.getKpiId() + "_" + e.getKpiName())).collect(Collectors.toList());
    }

    @Override
    public Long getMaxKpiId() {
        return Long.valueOf(catGraphKpiRepository.findMaxKpiId().toString());
    }

    @Override
    public Page<RptGraphDTO> getDstDataByCatGraphKpi(CatGraphKpiDTO catGraphKpiDTO, Pageable pageable) {
        return catGraphKpiCustomRepository.getDstDataByCatGraphKpi(catGraphKpiDTO, pageable).map(rptGraphMapper:: toDto);
    }

    @Override
    public void setKpiIdForNewKpi(Long id) {
        catGraphKpiRepository.updateKpiIdByIdForCreateKpi(id);
    }

    @Override
    public void deleteRptGraphById(Long id, String tableName) {
        catGraphKpiCustomRepository.deleteRptGraphById(id, tableName);
    }

    @Override
    public void deleteRptGraphByKpiId(Long kpiId, String tableName) {
        catGraphKpiCustomRepository.deleteRptGraphByKpiId(kpiId, tableName);
    }
}
