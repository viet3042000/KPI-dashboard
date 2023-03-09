package com.b4t.app.service;

import com.b4t.app.domain.CatItemDetail;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.dto.CategoryDTO;
import com.b4t.app.service.dto.ComboDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service Interface for managing {@link com.b4t.app.domain.CatItem}.
 */
public interface CatItemService {

    /**
     * Save a catItem.
     *
     * @param catItemDTO the entity to save.
     * @return the persisted entity.
     */
    CatItemDTO save(CatItemDTO catItemDTO);

    /**
     * Get all the catItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatItemDTO> findAll(String keyword, String[] categoryCodes, String parentCode, String[] parentCategoryCodes,
                             String parentValue, Long status, Pageable pageable);

    List<CatItemDTO> findAll(String keyword, String[] categoryCodes, String parentCode, String[] parentCategoryCodes,
                             String parentValue, Long status);

    /**
     * Get the "id" catItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatItemDTO> findOne(Long id);

    Optional<CatItemDTO> findByCode(String code);

    List<CatItemDTO> findByCodes(List<String> codes);
    /**
     * Delete the "id" catItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CatItemDTO> findCatItemByCategoryId(Long catId);

    List<CatItemDTO> findCatItemByDomainCode(String domainCode);

    CatItemDTO findDomainByGroupKpiCode(String domainCode);

    List<CatItemDTO> findTableByDatabase(String database);

    List<CatItemDTO> findDomainByUser(String username);

    List<CatItemDTO> getCatItemByCategoryCode(String categoryCode);

    CatItemDTO getCatItemByItemCodeAndStatus(String itemCode, Long status);

    CatItemDTO findFirstByCategoryCodeAndItemCodeAndStatus(String categoryCode, String itemCode, Long status);

    List<CategoryDTO> findAllCategory();

    List<CatItemDTO> findListParent();

    Page<CatItemDetail> query(CatItemDTO catItemDTO, Pageable pageable);

    List<CatItemDTO> findAllByStatus(Long status);

    List<CatItemDTO> findDomainAndTable();

    List<CatItemDTO> getTimeTypeMap(String domainCode);

    CompletableFuture<List<ComboDTO>> onSearchByCategoryAndName(Long categoryId, String categoryKey, String itemName);

    void multipleDelete(List<CatItemDTO> catItemDTOList);

    void addSourceTable(String schemaName, String tableName);

    void updateAutoIncreaseChartCode(Integer value);
}
