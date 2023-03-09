package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.ConfigChart_;
import com.b4t.app.security.SecurityUtils;
//import com.b4t.app.service.BuildChartService;
import com.b4t.app.service.*;
import com.b4t.app.domain.ConfigChart;
import com.b4t.app.repository.ConfigChartRepository;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.mapper.ConfigChartDetailMapper;
import com.b4t.app.service.mapper.ConfigChartMapper;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigChart}.
 */
@Service
@Transactional
public class ConfigChartServiceImpl implements ConfigChartService {

    private final Logger log = LoggerFactory.getLogger(ConfigChartServiceImpl.class);

    private final ConfigChartRepository configChartRepository;

    private final ConfigChartMapper configChartMapper;

    private final EntityManager entityManager;

    private final ConfigChartDetailMapper configChartDetailMapper;

    private final ConfigChartItemService configChartItemService;

    private final ConfigQueryChartService configQueryChartService;

    private final ConfigDisplayQueryService configDisplayQueryService;

    private final CommonService commonService;

    private final CatItemService catItemService;

    public ConfigChartServiceImpl(
        ConfigChartRepository configChartRepository,
        ConfigChartMapper configChartMapper,
        EntityManager entityManager,
        ConfigChartDetailMapper configChartDetailMapper,
        ConfigChartItemService configChartItemService,
        ConfigQueryChartService configQueryChartService,
        ConfigDisplayQueryService configDisplayQueryService,
        CommonService commonService, CatItemService catItemService) {
        this.configChartRepository = configChartRepository;
        this.configChartMapper = configChartMapper;
        this.entityManager = entityManager;
        this.configChartDetailMapper = configChartDetailMapper;
        this.configChartItemService = configChartItemService;
        this.configQueryChartService = configQueryChartService;
        this.configDisplayQueryService = configDisplayQueryService;
        this.commonService = commonService;
        this.catItemService = catItemService;
    }

    /**
     * Save a configChart.
     *
     * @param configChartDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigChartDTO save(ConfigChartDTO configChartDTO) {
        log.debug("Request to save ConfigChart : {}", configChartDTO);
        ConfigChart configChart = configChartMapper.toEntity(configChartDTO);
        configChart.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configChart.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configChart = configChartRepository.save(configChart);
        return configChartMapper.toDto(configChart);
    }

    @Override
    public SaveChartDTO clone(Long id) {
        Optional<ConfigChartDTO> configChartDtoOpt = findOne(id);
        if (!configChartDtoOpt.isPresent()) return null;
        ConfigChartDTO configChartDto = configChartDtoOpt.get();
        configChartDto.setId(null);
        String copyCodePrefix = configChartDto.getChartCode() + Constants.COPY_SUFFIX_CODE;
        Integer copyIdx = commonService.getMaxCode("config_chart", "chart_code", copyCodePrefix);
        copyIdx += 1;
        configChartDto.setChartCode(copyCodePrefix + copyIdx);
        configChartDto.setChartName(String.format(Constants.COPY_FORMAT_NAME, configChartDto.getChartName(), copyIdx));
        configChartDto.setTitleChart(String.format(Constants.COPY_FORMAT_NAME, configChartDto.getTitleChart(), copyIdx));
        configChartDto = save(configChartDto);

        SaveChartDTO rs = new SaveChartDTO(configChartDto);

        //get items
        List<ConfigChartItemDTO> chartItems = configChartItemService.findByChartId(id);
        if (!DataUtil.isNullOrEmpty(chartItems)) {
            List<Long> itemIds = chartItems.stream().map(ConfigChartItemDTO::getId).collect(Collectors.toList());
            Map<Long, Long> mapOldNewChartItemIds = new HashMap<>();
            List<Long> queryIds = chartItems.stream().map(ConfigChartItemDTO::getQueryId).collect(Collectors.toList());
            List<ConfigQueryChartDTO> queries = configQueryChartService.findByIds(queryIds);
            Map<Long, Long> mapOldNewQueryIds = new HashMap<>();
            for (ConfigQueryChartDTO q : queries) {
                Long oldId = q.getId();
                q.setId(null);
                q = configQueryChartService.save(q);
                mapOldNewQueryIds.put(oldId, q.getId());
            }
            for (ConfigChartItemDTO i : chartItems) {
                Long oldId = i.getId();
                i.setId(null);
                i.setChartId(configChartDto.getId());
                i.setQueryId(mapOldNewQueryIds.get(i.getQueryId()));
                i = configChartItemService.save(i);
                mapOldNewChartItemIds.put(oldId, i.getId());
            }
            List<ConfigDisplayQueryDTO> displayConfigs = configDisplayQueryService.findByChartItemIds(itemIds);
            displayConfigs = displayConfigs.stream().peek(c -> {
                c.setId(null);
                c.setItemChartId(mapOldNewChartItemIds.get(c.getItemChartId()));
            }).collect(Collectors.toList());
            configDisplayQueryService.saveAll(displayConfigs);
        }
        return rs;
    }

    /**
     * Get all the configCharts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigChartDTO> findAll(String keyword, String typeChart, Long[] groupChartIds, String[] domainCodes, String[] groupKpiCodes, Integer timeTypeDefault, Long status,
                                        Integer childChart, Long[] listChartNotIn, Long[] listChartIn,  Pageable pageable) {
        log.debug("Request to get all ConfigCharts");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigChart> criteria = cb.createQuery(ConfigChart.class);
        Root<ConfigChart> root = criteria.from(ConfigChart.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(
                cb.or(
                    cb.like(cb.lower(root.get(ConfigChart_.CHART_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get(ConfigChart_.CHART_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get(ConfigChart_.TITLE_CHART)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
                )
            );
        }
        if (StringUtils.isNotEmpty(typeChart)) {
            predicates.add(cb.equal(root.get(ConfigChart_.TYPE_CHART), typeChart));
        }
        if (timeTypeDefault != null) {
            predicates.add(cb.equal(root.get(ConfigChart_.TIME_TYPE_DEFAULT), timeTypeDefault));
        }
        if (!DataUtil.isNullOrEmpty(groupChartIds)) {
            predicates.add(root.get(ConfigChart_.GROUP_CHART_ID).in(groupChartIds));
        }
        if (!DataUtil.isNullOrEmpty(domainCodes)) {
            predicates.add(root.get(ConfigChart_.DOMAIN_CODE).in(domainCodes));
        }
        if (!DataUtil.isNullOrEmpty(childChart)) {
            predicates.add(cb.equal(root.get(ConfigChart_.CHILD_CHART), childChart));
        }
        if (!DataUtil.isNullOrEmpty(listChartNotIn)) {
            predicates.add(root.get(ConfigChart_.ID).in(listChartNotIn).not());
        }
        if (!DataUtil.isNullOrEmpty(listChartIn)) {
            predicates.add(root.get(ConfigChart_.ID).in(listChartIn));
        }
        if (!DataUtil.isNullOrEmpty(groupKpiCodes)) {
            predicates.add(root.get(ConfigChart_.GROUP_KPI_CODE).in(groupKpiCodes));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigChart_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<ConfigChart> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigChart> rootCount = countQuery.from(ConfigChart.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigChartDTO> rsDTOs = rs.stream().map(configChartMapper::toDto).collect(Collectors.toList());

        List<CatItemDTO> chartTypes = catItemService.findAll(null, new String[]{Constants.TYPE_CHART_CATITEM}, null, null, null, null);
        rsDTOs = rsDTOs.stream().peek(i -> {
            Optional<CatItemDTO> chartType = chartTypes.stream()
                .filter(t -> StringUtils.isNotEmpty(t.getItemValue()) && t.getItemValue().equals(i.getTypeChart()))
                .findFirst();
//            chartType.ifPresent(catItemDTO -> i.setTypeChart(catItemDTO.getDescription()));
            chartType.ifPresent(catItemDTO -> i.setTypeChart(catItemDTO.getItemValue()));
        }).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    public List<ConfigChartDTO> findConfigChartByIds(List<Long> chartIds) {
        return configChartRepository.findAllById(chartIds).stream().map(configChartMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one configChart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigChartDTO> findOne(Long id) {
        log.debug("Request to get ConfigChart : {}", id);
        return configChartRepository.findById(id)
            .map(configChartMapper::toDto);
    }


    @Override
    public Optional<ConfigChartDTO> findByCode(String code) {
        return configChartRepository.findFirstByChartCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(configChartMapper::toDto);
    }

    /**
     * Delete the configChart by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigChart : {}", id);
        Optional<ConfigChart> configChartOpt = configChartRepository.findById(id);
        if (!configChartOpt.isPresent()) return;
        ConfigChart configChart = configChartOpt.get();
        configChart.setStatus(Constants.STATUS_DISABLED);
        configChart.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configChart.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configChartRepository.save(configChart);
        List<ConfigChartItemDTO> chartItems = configChartItemService.findByChartId(id);
        if (!DataUtil.isNullOrEmpty(chartItems)) {
            configChartItemService.delete(chartItems);
            List<Long> chartItemIds = chartItems.stream().map(ConfigChartItemDTO::getId).collect(Collectors.toList());
            List<Long> queryIds = chartItems.stream().map(ConfigChartItemDTO::getQueryId).collect(Collectors.toList());
            configQueryChartService.delete(queryIds);
            List<ConfigDisplayQueryDTO> displayQueries = configDisplayQueryService.findByChartItemIds(chartItemIds);
            configDisplayQueryService.delete(displayQueries);
        }
    }

    @Override
    public void delete(List<ConfigChartDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return;
        List<ConfigChart> entities = dtos.stream().map(d -> {
            ConfigChart e = configChartMapper.toEntity(d);
            e.setStatus(Constants.STATUS_DISABLED);
            e.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            e.setUpdateUser(SecurityUtils.getCurrentUserLogin().orElse(null));
            return e;
        }).collect(Collectors.toList());
        configChartRepository.saveAll(entities);
        List<Long> chartIds = dtos.stream().map(ConfigChartDTO::getId).collect(Collectors.toList());
        List<ConfigChartItemDTO> chartItems = configChartItemService.findByChartIds(chartIds);
        if (!DataUtil.isNullOrEmpty(chartItems)) {
            configChartItemService.delete(chartItems);
            List<Long> queryIds = chartItems.stream().map(ConfigChartItemDTO::getQueryId).collect(Collectors.toList());
            configQueryChartService.delete(queryIds);
            List<Long> chartItemIds = chartItems.stream().map(ConfigChartItemDTO::getId).collect(Collectors.toList());
            List<ConfigDisplayQueryDTO> displayQueries = configDisplayQueryService.findByChartItemIds(chartItemIds);
            configDisplayQueryService.delete(displayQueries);
        }
    }

    /**
     * Get infor of chartId
     *
     * @param chartId
     * @return
     */
    @Override
    public List<ConfigChartDetailDTO> findByChartId(Long chartId) {
        return configChartRepository.findByChartId(chartId).stream().map(configChartDetailMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ConfigChartDTO> findByIds(List<Long> ids) {
        return configChartMapper.toDto(configChartRepository.findByIdInAndStatus(ids, Constants.STATUS_ACTIVE));
    }

    @Override
    public List<ComboDTO> onSearchChart(Long profileId, String keyword) {
        return configChartRepository.onSearchChartByProfile(profileId, keyword).stream()
            .filter(e -> !DataUtil.isNullOrEmpty(e.getDomainName())).map(e -> {
                ComboDTO comboDTO = new ComboDTO();
                comboDTO.setValue(e);
                comboDTO.setLabel(e.getChartName() + " (" + e.getDomainName() + ")");
                return comboDTO;
            }).collect(Collectors.toList());
    }
}
