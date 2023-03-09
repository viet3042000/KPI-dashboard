package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatItem;
import com.b4t.app.domain.CatItemDetail;
import com.b4t.app.domain.CatItem_;
import com.b4t.app.domain.Category;
import com.b4t.app.repository.CatItemCustomRepository;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.dto.CategoryDTO;
import com.b4t.app.service.dto.ComboDTO;
import com.b4t.app.service.mapper.CatItemMapper;
import com.b4t.app.service.mapper.CategoryMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CatItem}.
 */
@Service
@Transactional
public class CatItemServiceImpl implements CatItemService {

    public static final String CAT_ITEM = "catItem";
    private final Logger log = LoggerFactory.getLogger(CatItemServiceImpl.class);

    private final CatItemRepository catItemRepository;
    private final CatItemCustomRepository catItemCustomRepository;

    private final CatItemMapper catItemMapper;

    private final EntityManager entityManager;

    @Autowired
    CategoryMapper categoryMapper;

    public CatItemServiceImpl(CatItemRepository catItemRepository, CatItemCustomRepository catItemCustomRepository, CatItemMapper catItemMapper, EntityManager entityManager) {
        this.catItemRepository = catItemRepository;
        this.catItemCustomRepository = catItemCustomRepository;
        this.catItemMapper = catItemMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a catItem.
     *
     * @param catItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CatItemDTO save(CatItemDTO catItemDTO) {
        log.debug("Request to save CatItem : {}", catItemDTO);

        Optional<Category> category = catItemRepository.findCategoryByCode(catItemDTO.getCategoryCode());
        if (!category.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.catItem.categoryCodeNotExisted"), CAT_ITEM, "catItem.categoryCodeNotExisted");
        }
        Long categoryId = Long.valueOf(category.get().getCategoryId());
        if (!DataUtil.isNullOrEmpty(catItemDTO.getItemId())) {
            //Validate truong hop cap nhat
            List<CatItem> catBO = catItemRepository.getItemByItemCodeAndCategoryIdAndItemIdNotEqual(catItemDTO.getItemCode(), categoryId, catItemDTO.getItemId());
            if (catBO != null && !catBO.isEmpty()) {
                throw new BadRequestAlertException(Translator.toLocale("error.catItem.itemCodeIsExisted"), CAT_ITEM, "catItem.itemCodeIsExisted");
            }
        } else {
            //Validate truong hop them moi
            List<CatItem> catBO = catItemRepository.getItemByItemCodeAndCategoryId(catItemDTO.getItemCode(), categoryId);
            if (catBO != null && !catBO.isEmpty()) {
                throw new BadRequestAlertException(Translator.toLocale("error.catItem.itemCodeIsExisted"), CAT_ITEM, "catItem.itemCodeIsExisted");
            }

        }

        catItemDTO.setCategoryId(categoryId);
        CatItem catItem = catItemMapper.toEntity(catItemDTO);
        catItem.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        catItem.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        catItem = catItemRepository.save(catItem);
        return catItemMapper.toDto(catItem);
    }

    /**
     * Get all the catItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CatItemDTO> findAll(String keyword, String[] categoryCodes, String parentCode, String[] parentCategoryCodes,
                                    String parentValue, Long status, Pageable pageable) {
        log.debug("Request to get all CatItems");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CatItem> criteria = cb.createQuery(CatItem.class);
        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<CatItem> root = criteria.from(CatItem.class);
        Root<CatItem> countRoot = countCrit.from(CatItem.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate keywordWCls = cb.or(
                cb.like(cb.lower(root.get(CatItem_.ITEM_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(CatItem_.ITEM_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(keywordWCls);
            countPredicates.add(keywordWCls);
        }
        if (!DataUtil.isNullOrEmpty(categoryCodes)) {
            predicates.add(root.get(CatItem_.CATEGORY_CODE).in(categoryCodes));
            countPredicates.add(countRoot.get(CatItem_.CATEGORY_CODE).in(categoryCodes));
        }
        if (StringUtils.isNotEmpty(parentCode) || !DataUtil.isNullOrEmpty(parentCategoryCodes) || StringUtils.isNotEmpty(parentValue)) {
            Root<CatItem> parentRoot = criteria.from(CatItem.class);
            predicates.add(cb.equal(root.get(CatItem_.PARENT_ITEM_ID), parentRoot.get(CatItem_.ITEM_ID)));
            predicates.add(cb.equal(parentRoot.get(CatItem_.STATUS), Constants.STATUS_ACTIVE));

            Root<CatItem> parentCountRoot = countCrit.from(CatItem.class);
            countPredicates.add(cb.equal(countRoot.get(CatItem_.PARENT_ITEM_ID), parentCountRoot.get(CatItem_.ITEM_ID)));
            countPredicates.add(cb.equal(parentCountRoot.get(CatItem_.STATUS), Constants.STATUS_ACTIVE));

            if (StringUtils.isNotEmpty(parentCode)) {
                predicates.add(cb.equal(parentRoot.get(CatItem_.ITEM_CODE), parentCode));
                countPredicates.add(cb.equal(parentCountRoot.get(CatItem_.ITEM_CODE), parentCode));
            }

            if (!DataUtil.isNullOrEmpty(parentCategoryCodes)) {
                predicates.add(parentRoot.get(CatItem_.CATEGORY_CODE).in(parentCategoryCodes));
                countPredicates.add(parentCountRoot.get(CatItem_.CATEGORY_CODE).in(parentCategoryCodes));
            }

            if (StringUtils.isNotEmpty(parentValue)) {
                predicates.add(cb.equal(parentRoot.get(CatItem_.ITEM_VALUE), parentValue));
                countPredicates.add(cb.equal(parentCountRoot.get(CatItem_.ITEM_VALUE), parentValue));
            }
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(CatItem_.STATUS), status));
        countPredicates.add(cb.equal(countRoot.get(CatItem_.STATUS), status));

        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        criteria.select(root);

        List<CatItem> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
        countCrit.where(cb.and(countPredicates.toArray(new Predicate[countPredicates.size()])));
        countCrit.select(cb.countDistinct(countRoot));

        Long count = entityManager.createQuery(countCrit).getSingleResult();
        List<CatItemDTO> rsDTOs = catItemMapper.toDto(rs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CatItemDTO> findAll(String keyword, String[] categoryCodes, String parentCode, String[] parentCategoryCodes,
                                    String parentValue, Long status) {
        log.debug("Request to get all CatItems");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CatItem> criteria = cb.createQuery(CatItem.class);
        Root<CatItem> root = criteria.from(CatItem.class);

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate keywordWCls = cb.or(
                cb.like(cb.lower(root.get(CatItem_.ITEM_CODE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(CatItem_.ITEM_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(keywordWCls);
        }
        if (!DataUtil.isNullOrEmpty(categoryCodes)) {
            predicates.add(root.get(CatItem_.CATEGORY_CODE).in(categoryCodes));
        }
        if (StringUtils.isNotEmpty(parentCode) || !DataUtil.isNullOrEmpty(parentCategoryCodes) || StringUtils.isNotEmpty(parentValue)) {
            Root<CatItem> parentRoot = criteria.from(CatItem.class);
            predicates.add(cb.equal(root.get(CatItem_.PARENT_ITEM_ID), parentRoot.get(CatItem_.ITEM_ID)));
            predicates.add(cb.equal(parentRoot.get(CatItem_.STATUS), Constants.STATUS_ACTIVE));

            if (StringUtils.isNotEmpty(parentCode)) {
                predicates.add(cb.equal(parentRoot.get(CatItem_.ITEM_CODE), parentCode));
            }

            if (!DataUtil.isNullOrEmpty(parentCategoryCodes)) {
                predicates.add(parentRoot.get(CatItem_.CATEGORY_CODE).in(parentCategoryCodes));
            }

            if (StringUtils.isNotEmpty(parentValue)) {
                predicates.add(cb.equal(parentRoot.get(CatItem_.ITEM_VALUE), parentValue));
            }
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(CatItem_.STATUS), status));

        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(cb.asc(root.get(CatItem_.ITEM_ID)));
        criteria.select(root);

        List<CatItem> rs = entityManager.createQuery(criteria).getResultList();
        return catItemMapper.toDto(rs);
    }

    /**
     * Get one catItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CatItemDTO> findOne(Long id) {
        log.debug("Request to get CatItem : {}", id);
        return catItemRepository.findById(id)
            .map(catItemMapper::toDto);
    }

    @Override
    public Optional<CatItemDTO> findByCode(String code) {
        CatItem rs = catItemRepository.findFirstByItemCodeAndStatus(code, Constants.STATUS_ACTIVE);
        if (rs == null)
            return Optional.empty();
        return Optional.of(catItemMapper.toDto(rs));
    }

    @Override
    public List<CatItemDTO> findByCodes(List<String> codes) {
        List<CatItem> rs = catItemRepository.findAllByItemCodeInAndStatus(codes, Constants.STATUS_ACTIVE);
        return catItemMapper.toDto(rs);
    }

    /**
     * Delete the catItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CatItem : {}", id);
        Optional<CatItem> catItem = catItemRepository.findByItemId(id);
        if (!catItem.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("error.catItem.itemIdNotExisted"), CAT_ITEM, "catItem.itemIdNotExisted");
        }
        CatItem item = catItem.get();
        item.setStatus(Constants.STATUS_DISABLED);
        item.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        item.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        catItemRepository.save(item);
    }

    @Override
    public List<CatItemDTO> findCatItemByCategoryId(Long catId) {
        List<CatItem> lst = catItemRepository.findCatItemByCategoryIdAndStatusOrderByPositionAsc(catId, Constants.STATUS_ACTIVE);
        return catItemMapper.toDto(lst);
    }

    @Override
    public List<CatItemDTO> findCatItemByDomainCode(String domainCode) {
        List<CatItem> lst = catItemRepository.findCatItemByDomainCodeAndStatus(domainCode);
        return catItemMapper.toDto(lst);
    }

    @Override
    public List<CatItemDTO> findDomainAndTable() {
        List<CatItemDTO> lst = catItemCustomRepository.findDomainAndTable();
        return lst;
    }

    @Override
    public List<CatItemDTO> getTimeTypeMap(String domainCode) {
        List<CatItemDTO> lst = catItemCustomRepository.getTimeTypeMap(domainCode);
        return lst;
    }

    @Override
    public CatItemDTO findDomainByGroupKpiCode(String domainCode) {
        CatItem catItem = catItemRepository.findDomainByGroupKpiCode(domainCode);
        return catItemMapper.toDto(catItem);
    }

    @Override
    public List<CatItemDTO> findTableByDatabase(String database) {
        return catItemRepository.findTableByDatabase(database).stream()
            .map(catItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CatItemDTO> findDomainByUser(String username) {
        return catItemRepository.findDomainByUser(username).stream()
            .map(catItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CatItemDTO> getCatItemByCategoryCode(String categoryCode) {
        return catItemRepository.getCatItemByCategoryCode(categoryCode).stream()
            .map(catItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CatItemDTO getCatItemByItemCodeAndStatus(String itemCode, Long status) {
        CatItem catItem = catItemRepository.findFirstByItemCodeAndStatus(itemCode, status);
        if(DataUtil.isNullOrEmpty(catItem)){
            throw new BadRequestAlertException("Item not exists", CAT_ITEM, "catItem.listItemIsEmpty");
        }
        return catItemMapper.toDto(catItem);
    }

    public CatItemDTO findFirstByCategoryCodeAndItemCodeAndStatus(String categoryCode, String itemCode, Long status) {
        return catItemMapper.toDto(catItemRepository.findFirstByCategoryCodeAndItemCodeAndStatus(categoryCode, itemCode, status));
    }

    @Override
    public List<CategoryDTO> findAllCategory() {
        return catItemRepository.findAllCategory().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CatItemDTO> findListParent() {
        return catItemRepository.findListParent().stream().
            map(e -> {
                CatItemDTO item = new CatItemDTO();
                item.setItemId(DataUtil.safeToLong(e[0]));
                item.setItemName(DataUtil.safeToString(e[1]));
                return item;
            }).collect(Collectors.toList());
    }

    @Override
    public Page<CatItemDetail> query(CatItemDTO catItemDTO, Pageable pageable) {
        return catItemRepository.queryCatItem(catItemDTO.getStatus(),
            catItemDTO.getCategoryCode(), catItemDTO.getParentItemId(),
            DataUtil.makeLikeParam(catItemDTO.getItemName()),
            DataUtil.makeLikeParam(catItemDTO.getItemCode()),
            DataUtil.makeLikeParam(catItemDTO.getItemValue()),
            pageable);
    }

    @Override
    public List<CatItemDTO> findAllByStatus(Long status) {
        return catItemRepository.findAllByStatus(status).stream()
            .map(catItemMapper::toDto).peek(e -> {
                e.setItemName(e.getCategoryCode() + "_" + e.getItemName());
            }).collect(Collectors.toList());
    }

    @Override
    @Async
    public CompletableFuture<List<ComboDTO>> onSearchByCategoryAndName(Long categoryId, String categoryKey, String itemName) {
        List<ComboDTO> result = catItemRepository.onSearchByCategoryAndName(categoryId,
            DataUtil.makeLikeParam(itemName)).stream().map(e -> {
            ComboDTO comboDTO = new ComboDTO(categoryKey + "#" + e.getItemValue(), e.getItemName());
            return comboDTO;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Transactional(rollbackFor = BadRequestAlertException.class)
    public void multipleDelete(List<CatItemDTO> catItemDTOList) {
        if (DataUtil.isNullOrEmpty(catItemDTOList)) {
            throw new BadRequestAlertException(Translator.toLocale("error.catItem.listItemIsEmpty"), CAT_ITEM, "catItem.listItemIsEmpty");
        }
        catItemDTOList.forEach(item -> delete(item.getItemId()));
    }
    public void addSourceTable(String schemaName, String tableName) {
        String table = schemaName + "." + tableName;
        CatItemDTO catItemDTO = new CatItemDTO();
        catItemDTO.setItemName(table);
        catItemDTO.setItemCode(table);
        catItemDTO.setItemValue(table);
        Category category = catItemRepository.findCategoryByCode("TABLE_NAME_SOURCE").orElse(null);
        if(category != null) {
            catItemDTO.setCategoryId(Long.valueOf(category.getCategoryId()));
            catItemDTO.setCategoryCode(category.getCategoryCode());
        }
        catItemDTO.setEditable(1L);
        catItemDTO.setStatus(1L);
        catItemDTO.setUpdateUser(SecurityUtils.getCurrentUserLogin().orElse(null));
        catItemDTO.setUpdateTime(Instant.now());
        catItemRepository.save(catItemMapper.toEntity(catItemDTO));

        Category tableDTO = catItemRepository.findCategoryByCode("TABLE_NAME").orElse(null);
        if (tableDTO != null) {
            CatItemDTO table_ = new CatItemDTO();
            table_.setItemName(tableName);
            table_.setItemCode(tableName);
            table_.setItemValue(tableName);
            table_.setCategoryId(Long.valueOf(tableDTO.getCategoryId()));
            table_.setCategoryCode(tableDTO.getCategoryCode());
            table_.setEditable(1L);
            table_.setStatus(1L);
            catItemRepository.getAllByCategoryCodeAndItemValue("DATABASE_NAME", schemaName).stream().findFirst().ifPresent(e-> {
                table_.setParentItemId(e.getItemId());
            });
            table_.setUpdateUser(SecurityUtils.getCurrentUserLogin().orElse(null));
            table_.setUpdateTime(Instant.now());
            catItemRepository.save(catItemMapper.toEntity(table_));
        }
    }

    @Override
    public void updateAutoIncreaseChartCode(Integer value){
        catItemRepository.updateAutoIncreaseChartCode(value);
    }
}
