package com.b4t.app.repository;

import com.b4t.app.domain.CatItem;
import com.b4t.app.domain.CatItemDetail;
import com.b4t.app.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CatItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatItemRepository extends JpaRepository<CatItem, Long> {
    List<CatItem> findCatItemByCategoryIdAndStatus(Long categoryId, Long status);

    List<CatItem> findCatItemByCategoryIdAndStatusOrderByPositionAsc(Long categoryId, Long status);

    CatItem findFirstByItemCodeAndStatus(String code, Long status);

    List<CatItem> findAllByItemCodeInAndStatus(List<String> lstCode, Long status);

    @Query(value = " select gr.* " +
        "from cat_item gr" +
        "         inner join cat_item do on gr.PARENT_ITEM_ID = do.ITEM_ID and do.STATUS = 1" +
        "    and do.CATEGORY_CODE = 'DOMAIN'" +
        " where gr.CATEGORY_CODE = 'GROUP_KPI'" +
        "  and gr.status = 1" +
        "  and do.item_code = ?1 " +
        " order by gr.position, gr.ITEM_NAME ", nativeQuery = true)
    List<CatItem> findCatItemByDomainCodeAndStatus(String domainCode);

    @Query(value = "select * " +
        " from cat_item gr" +
        "         inner join cat_item do on gr.ITEM_ID = do.PARENT_ITEM_ID and do.STATUS = 1" +
        "    and do.CATEGORY_CODE = 'GROUP_KPI'" +
        " where gr.CATEGORY_CODE = 'DOMAIN'" +
        "  and gr.status = 1" +
        "  and do.item_code = ?1" +
        " order by gr.position, gr.ITEM_NAME", nativeQuery = true)
    CatItem findDomainByGroupKpiCode(String groupKpiCode);

    @Query(value = "select * " +
        " from cat_item gr" +
        "         inner join cat_item do on gr.ITEM_ID = do.PARENT_ITEM_ID and do.STATUS = 1" +
        "    and do.CATEGORY_CODE = 'GROUP_KPI'" +
        " where gr.CATEGORY_CODE = 'DOMAIN'" +
        "  and gr.status = 1" +
        "  and do.item_code in (?1)" +
        " order by gr.position, gr.ITEM_NAME", nativeQuery = true)
    List<CatItem> findDomainByInGroupKpiCode(List<String> groupKpiCodes);

    @Query(value = " select * from cat_item a where CATEGORY_ID = 37" +
        " and PARENT_ITEM_ID in (select ITEM_ID from cat_item b where b.ITEM_NAME = ?1 )" +
        " order by a.position asc", nativeQuery = true)
    List<CatItem> findTableByDatabase(String database);

    @Query(value = " select * from cat_item i " +
        " inner join category c on i.CATEGORY_ID = c.CATEGORY_ID and c.CATEGORY_CODE  = 'DOMAIN'" +
        " and exists ( select 1 from jhi_user u inner join jhi_user_domain d on u.id = d.user_id where u.login = ?1 and d.domain_code = i.ITEM_VALUE) " +
        " order by position asc ", nativeQuery = true)
    List<CatItem> findDomainByUser(String username);

    @Query(value = " select ci.*" +
        " from cat_item ci" +
        " where ci.CATEGORY_CODE = ?1" +
        "  and ci.status = 1" +
        " order by ci.position, ci.ITEM_NAME ", nativeQuery = true)
    List<CatItem> getCatItemByCategoryCode(String categoryCode);

    CatItem findFirstByCategoryCodeAndItemCodeAndStatus(String categoryCode, String itemCode, Long status);

    @Query(value = "select a from Category a order by a.categoryName")
    List<Category> findAllCategory();

    @Query(value = " select distinct ci.item_id, concat(ci.CATEGORY_CODE ,'-',ci.ITEM_NAME ) as it" +
        " from cat_item ci " +
        " inner join cat_item chi on ci.ITEM_ID = chi.PARENT_ITEM_ID" +
        " where ci.status = 1" +
        " order by ci.CATEGORY_CODE, ci.position, ci.ITEM_NAME", nativeQuery = true)
    List<Object[]> findListParent();


    @Query(value = " select new CatItemDetail(ci.itemId, ci.itemCode, ci.itemName, ci.itemValue, ci.categoryId, " +
        " ci.categoryCode, ci.position, ci.description, ci.editable, ci.parentItemId, " +
        " ci.status, ci.updateTime, ci.updateUser, pa.itemName) from CatItem ci " +
        " left join CatItem pa on ci.parentItemId = pa.itemId" +
        " where ci.status = :status " +
        " and (:category is null or ci.categoryCode = :category)" +
        " and (:parentItemId is null or ci.parentItemId = :parentItemId)" +
        " and (:itemName is null or lower(ci.itemName) like %:itemName% ESCAPE '&')" +
        " and (:itemCode is null or lower(ci.itemCode) like %:itemCode% ESCAPE '&')" +
        " and (:itemValue is null or lower(ci.itemValue) like %:itemValue% ESCAPE '&')" +
        " order by ci.categoryCode, pa.itemName, ci.position, ci.itemName",
        countQuery = " select count(ci) " +
            " from CatItem ci " +
            " left join CatItem pa on ci.parentItemId = pa.itemId" +
            " where ci.status = :status " +
            " and (:category is null or ci.categoryCode = :category)" +
            " and (:parentItemId is null or ci.parentItemId = :parentItemId)" +
            " and (:itemName is null or lower(ci.itemName) like %:itemName% ESCAPE '&')" +
            " and (:itemCode is null or lower(ci.itemCode) like %:itemCode% ESCAPE '&')" +
            " and (:itemValue is null or lower(ci.itemValue) like %:itemValue% ESCAPE '&')"
    )
    Page<CatItemDetail> queryCatItem(@Param("status") Long status,
                                     @Param("category") String category,
                                     @Param("parentItemId") Long parentItemId,
                                     @Param("itemName") String itemName,
                                     @Param("itemCode") String itemCode, @Param("itemValue") String itemValue,
                                     Pageable pageable);

    @Query(value = "select a from Category a where UPPER(a.categoryCode) = UPPER(:categoryCode)")
    Optional<Category> findCategoryByCode(@Param("categoryCode") String categoryCode);

    @Query(value = "select a from CatItem a where UPPER(a.itemCode) = UPPER(:itemCode) and a.categoryId = :categoryId")
    List<CatItem> getItemByItemCodeAndCategoryId(@Param("itemCode") String itemCode, @Param("categoryId") Long categoryId);

    @Query(value = "select a from CatItem a where UPPER(a.itemCode) = UPPER(:itemCode) and a.categoryId = :categoryId and a.itemId <> :itemId")
    List<CatItem> getItemByItemCodeAndCategoryIdAndItemIdNotEqual(@Param("itemCode") String itemCode, @Param("categoryId") Long categoryId, @Param("itemId") Long itemId);

    Optional<CatItem> findByItemId(Long itemId);

    List<CatItem> findAllByStatus(Long status);

    @Query(value = "Select a.item_id, a.item_value , a.item_name , b.item_value\n" +
        "from cat_item a\n" +
        "         inner join cat_item b on b.item_code = a.item_value and b.category_code ='DOMAIN_TABLE' and b.status = 1\n" +
        "where a.CATEGORY_CODE ='DOMAIN'\n" +
        "  and a.status = 1\n" +
        "order by a.position, a.ITEM_NAME", nativeQuery = true)
    List<CatItem> findDomainAndTable();

    @Query(value = "select a from CatItem a where a.categoryId = :categoryId " +
        " and (:itemName is null or lower( a.itemName) like %:itemName% escape '&') " +
        " and a.status = 1 order by a.position asc ")
    List<CatItem> onSearchByCategoryAndName(@Param("categoryId") Long categoryId,
                                            @Param("itemName") String itemName);

    List<CatItem> getAllByCategoryCodeAndItemValue(String categoryCode, String itemValue);

    @Modifying()
    @Query(value = "update cat_item set item_value = item_value + :value where ITEM_CODE = 'CHART_CODE_AUTO_INCREASE'", nativeQuery = true)
    void updateAutoIncreaseChartCode(@Param("value") Integer value);
}

