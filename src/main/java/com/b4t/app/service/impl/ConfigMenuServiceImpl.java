package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ConfigMapChartMenuItemService;
import com.b4t.app.service.ConfigMenuItemService;
import com.b4t.app.service.ConfigMenuService;
import com.b4t.app.repository.ConfigMenuRepository;
import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;
import com.b4t.app.service.dto.ConfigMenuDTO;
import com.b4t.app.service.dto.ConfigMenuItemDTO;
import com.b4t.app.service.mapper.ConfigMenuItemMapper;
import com.b4t.app.service.mapper.ConfigMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConfigMenu}.
 */
@Service
@Transactional
public class ConfigMenuServiceImpl implements ConfigMenuService {

    private final Logger log = LoggerFactory.getLogger(ConfigMenuServiceImpl.class);

    private final ConfigMenuRepository configMenuRepository;

    private final ConfigMenuMapper configMenuMapper;

    private final EntityManager entityManager;

    private final ConfigMenuItemMapper configMenuItemMapper;

    private final CatItemRepository catItemRepository;
    @Autowired
    private ConfigMenuItemService configMenuItemService;

    @Autowired
    private ConfigMapChartMenuItemService configMapChartMenuItemService;

    public ConfigMenuServiceImpl(ConfigMenuRepository configMenuRepository, ConfigMenuMapper configMenuMapper, EntityManager entityManager, ConfigMenuItemMapper configMenuItemMapper, CatItemRepository catItemRepository) {
        this.configMenuRepository = configMenuRepository;
        this.configMenuMapper = configMenuMapper;
        this.entityManager = entityManager;
        this.configMenuItemMapper = configMenuItemMapper;
        this.catItemRepository = catItemRepository;
    }


    /**
     * Save a configMenu.
     *
     * @param configMenuDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigMenuDTO save(ConfigMenuDTO configMenuDTO) {
        log.debug("Request to save ConfigMenu : {}", configMenuDTO);
        ConfigMenu configMenu = configMenuMapper.toEntity(configMenuDTO);
        configMenu.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        configMenu.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMenu = configMenuRepository.save(configMenu);
        ConfigMenuDTO result = configMenuMapper.toDto(configMenu);
        //save menuitem
        List<ConfigMenuItemDTO> existedMenuItem = configMenuItemService.findAll(null, new Long[]{configMenu.getId()}, null, null);
        List<Long> existedMenuItemIds = existedMenuItem.stream().map(i -> i.getId()).collect(Collectors.toList());

        if (!DataUtil.isNullOrEmpty(configMenuDTO.getItems())) {

            List<ConfigMenuItemDTO> needSaveMenuItem = configMenuDTO.getItems()
                .stream().filter(i -> i.getId() == null || existedMenuItemIds.contains(i.getId())).collect(Collectors.toList());
            List<Long> needUpdateIds = needSaveMenuItem.stream().filter(i -> i.getId() != null).map(i -> i.getId()).collect(Collectors.toList());
            List<ConfigMenuItemDTO> needDeleteMenuItem = existedMenuItem
                .stream().filter(i -> !needUpdateIds.contains(i.getId())).collect(Collectors.toList());

            ConfigMenu finalConfigMenu = configMenu;

            if (!DataUtil.isNullOrEmpty(needDeleteMenuItem)) {
                needDeleteMenuItem = needDeleteMenuItem.stream().peek(i -> {
                    i.setStatus(Constants.STATUS_DISABLED);
                    i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
                    i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
                }).collect(Collectors.toList());
                Long[] deleteMenuItemIds = needDeleteMenuItem.stream().map(i -> i.getId()).toArray(Long[]::new);
                configMapChartMenuItemService.delete(deleteMenuItemIds, null);
                configMenuItemService.delete(needDeleteMenuItem);
            }
            if (!DataUtil.isNullOrEmpty(needSaveMenuItem)) {
                needSaveMenuItem = needSaveMenuItem.stream().peek(i -> {
                    i.setMenuId(finalConfigMenu.getId());
                    i.setStatus(Constants.STATUS_ACTIVE);
                    i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
                    i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
                }).collect(Collectors.toList());

                needSaveMenuItem = configMenuItemService.saveAll(needSaveMenuItem);
            }

            result.setItems(needSaveMenuItem);
        }

        return result;
    }

    /**
     * Get all the configMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigMenuDTO> findAll(String keyword, String domainCode, Long status, Pageable pageable) {
        log.debug("Request to get all ConfigMenus");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConfigMenu> criteria = cb.createQuery(ConfigMenu.class);
        Root<ConfigMenu> root = criteria.from(ConfigMenu.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(
                cb.or(
                    cb.like(cb.lower(root.get(ConfigMenu_.MENU_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                    cb.like(cb.lower(root.get(ConfigMenu_.MENU_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
                )
            );
        }
        if (StringUtils.isNotEmpty(domainCode)) {
            predicates.add(cb.equal(root.get(ConfigMenu_.DOMAIN_CODE), domainCode));
        }

        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(ConfigMenu_.STATUS), status));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<ConfigMenu> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ConfigMenu> rootCount = countQuery.from(ConfigMenu.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<ConfigMenuDTO> rsDTOs = rs.stream().map(configMenuMapper::toDto).collect(Collectors.toList());
        updateConfigMenuInfo(rsDTOs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    private void updateConfigMenuInfo(List<ConfigMenuDTO> menuDTOList) {
        if (DataUtil.isNullOrEmpty(menuDTOList)) return;
        List<String> lstDomainCode = menuDTOList.stream().map(ConfigMenuDTO::getDomainCode).collect(Collectors.toList());
        List<CatItem> lstDomain = catItemRepository.findAllByItemCodeInAndStatus(lstDomainCode, Constants.STATUS_ACTIVE);
        if (DataUtil.isNullOrEmpty(lstDomain)) return;
        Map<String, String> mapDomainCodeAndName = lstDomain.stream().collect(Collectors.toMap(CatItem::getItemCode, CatItem::getItemName, (oldVal, newVal) -> oldVal));
        menuDTOList.forEach(bean -> {
            bean.setDomainName(mapDomainCodeAndName.get(bean.getDomainCode()));
        });
    }

    @Override
    public List<ConfigMenuDTO> findAllByProfileIds(Long[] profileIds) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = cb.createQuery();
        Root<ConfigMenu> menuRoot = criteria.from(ConfigMenu.class);
        Root<ConfigMenuItem> menuItemRoot = criteria.from(ConfigMenuItem.class);
        Root<ConfigScreen> screenRoot = criteria.from(ConfigScreen.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(menuRoot.get(ConfigMenu_.STATUS), Constants.STATUS_ACTIVE));
        predicates.add(cb.equal(menuRoot.get(ConfigMenu_.ID), menuItemRoot.get(ConfigMenuItem_.MENU_ID)));
        predicates.add(cb.equal(menuItemRoot.get(ConfigMenuItem_.STATUS), Constants.STATUS_ACTIVE));
        predicates.add(cb.equal(menuItemRoot.get(ConfigMenuItem_.ID), screenRoot.get(ConfigScreen_.MENU_ITEM_ID)));
        predicates.add(cb.equal(screenRoot.get(ConfigScreen_.STATUS), Constants.STATUS_ACTIVE));
        if (!DataUtil.isNullOrEmpty(profileIds)) {
            Expression<Long> inExpression = screenRoot.get(ConfigScreen_.PROFILE_ID);
            predicates.add(inExpression.in(profileIds));
        }
        criteria.multiselect(menuRoot, menuItemRoot, screenRoot).distinct(true);
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(cb.asc(menuRoot.get(ConfigMenu_.ORDER_INDEX)), cb.asc(menuItemRoot.get(ConfigMenuItem_.ORDER_INDEX)));
        List list = entityManager.createQuery(criteria).getResultList();
        List<ConfigMenuDTO> rs = new ArrayList<>();
        for (Object o : list) {
            Object[] arrObj = (Object[]) o;
            ConfigMenu menu = (ConfigMenu) arrObj[0];
            ConfigMenuItem menuItem = (ConfigMenuItem) arrObj[1];
            ConfigScreen screen = (ConfigScreen) arrObj[2];
            if (menu != null) {
                ConfigMenuDTO dto = configMenuMapper.toDto(menu);
                boolean isExisted = false;
                Optional<ConfigMenuDTO> ckDto = rs.stream().filter(i -> menu.getId().equals(i.getId())).findFirst();
                if (ckDto.isPresent()) {
                    dto = ckDto.get();
                    isExisted = true;
                }
                boolean isItemExisted = false;
                if (menuItem != null && screen != null) {
                    ConfigMenuItemDTO menuItemDto = configMenuItemMapper.toDto(menuItem);
                    Optional<ConfigMenuItemDTO> ckMenuItemDto = dto.getItems().stream().filter(i -> menuItem.getId().equals(i.getId())).findFirst();
                    if (ckMenuItemDto.isPresent()) {
                        menuItemDto = ckMenuItemDto.get();
                        isItemExisted = true;
                    }
                    List<Long> screenIds = menuItemDto.getScreenIds();
                    screenIds.add(screen.getId());
                    menuItemDto.setScreenIds(screenIds);
                    if (!isItemExisted) {
                        List<ConfigMenuItemDTO> items = dto.getItems();
                        items.add(menuItemDto);
                        dto.setItems(items);
                    }
                }
                if (!isExisted) {
                    rs.add(dto);
                }
            }
        }

        return rs;
    }

    /**
     * Get one configMenu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigMenuDTO> findOne(Long id) {
        log.debug("Request to get ConfigMenu : {}", id);
        return configMenuRepository.findById(id)
            .map(configMenuMapper::toDto);
    }

    @Override
    public Optional<ConfigMenuDTO> findByCode(String code) {
        return configMenuRepository
            .findFirstByMenuCodeAndStatus(code, Constants.STATUS_ACTIVE)
            .map(configMenuMapper::toDto);
    }

    /**
     * Delete the configMenu by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigMenu : {}", id);
        Optional<ConfigMenuDTO> menuOpt = findOne(id);
        if (!menuOpt.isPresent()) return;
        ConfigMenuDTO menu = menuOpt.get();
        menu.setStatus(Constants.STATUS_DISABLED);
        menu.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        menu.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        configMenuRepository.save(configMenuMapper.toEntity(menu));

        List<ConfigMenuItemDTO> menuItems = configMenuItemService.findAll(null, new Long[]{id}, null, null);
        if (DataUtil.isNullOrEmpty(menuItems)) return;
        menuItems = menuItems.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
        }).collect(Collectors.toList());
        configMenuItemService.saveAll(menuItems);

        Long[] menuItemIds = menuItems.stream().map(ConfigMenuItemDTO::getId).toArray(Long[]::new);
        List<ConfigMapChartMenuItemDTO> mapCharts = configMapChartMenuItemService.findAll(null, menuItemIds, null, null);
        if (DataUtil.isNullOrEmpty(mapCharts)) return;
        mapCharts = mapCharts.stream().peek(i -> {
            i.setStatus(Constants.STATUS_DISABLED);
        }).collect(Collectors.toList());
        configMapChartMenuItemService.saveAll(mapCharts);

    }
}
