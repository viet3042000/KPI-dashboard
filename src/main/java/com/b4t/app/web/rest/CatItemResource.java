package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.CatItemDetail;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.dto.CategoryDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.CatItem}.
 */
@RestController
@RequestMapping("/api")
public class CatItemResource {

    public static final String REST_REQUEST_TO_GET_CAT_ITEM = "REST request to get CatItem : {}";
    public static final String REST_REQUEST_TO_GET_GET_LIST_CATEGORY_ = "REST request to get getListCategory ";
    public static final String REST_REQUEST_TO_FIND_DOMAIN_BY_USER = "REST request to findDomainByUser : {}";
    private final Logger log = LoggerFactory.getLogger(CatItemResource.class);

    private static final String ENTITY_NAME = "catItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatItemService catItemService;

    public CatItemResource(CatItemService catItemService) {
        this.catItemService = catItemService;
    }

    /**
     * {@code POST  /cat-items} : Create a new catItem.
     *
     * @param catItemDTO the catItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catItemDTO, or with status {@code 400 (Bad Request)} if the catItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cat-items")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<CatItemDTO> createCatItem(@RequestBody CatItemDTO catItemDTO) throws URISyntaxException {
        log.debug("REST request to save CatItem : {}", catItemDTO);
        if (catItemDTO.getItemId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        CatItemDTO result = catItemService.save(catItemDTO);
        return ResponseEntity.created(new URI("/api/cat-items/" + result.getItemId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getItemId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cat-items} : Updates an existing catItem.
     *
     * @param catItemDTO the catItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catItemDTO,
     * or with status {@code 400 (Bad Request)} if the catItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cat-items")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<CatItemDTO> updateCatItem(@RequestBody CatItemDTO catItemDTO) throws URISyntaxException {
        log.debug("REST request to update CatItem : {}", catItemDTO);
        if (catItemDTO.getItemId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        CatItemDTO result = catItemService.save(catItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catItemDTO.getItemId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cat-items} : get all the catItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catItems in body.
     */
    @GetMapping("/cat-items")
    public ResponseEntity<List<CatItemDTO>> getAllCatItems(String keyword, String[] categoryCodes, String parentCode,
                                                           String[] parentCategoryCodes, String parentValue,
                                                           Long status, Pageable pageable) {
        log.debug("REST request to get a page of CatItems");
        Page<CatItemDTO> page = catItemService.findAll(keyword, categoryCodes, parentCode, parentCategoryCodes, parentValue, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cat-items/:id} : get the "id" catItem.
     *
     * @param id the id of the catItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cat-items/{id}")
    public ResponseEntity<CatItemDTO> getCatItem(@PathVariable Long id) {
        log.debug("REST get CatItem : {}", id);
        Optional<CatItemDTO> catItemDTO = catItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catItemDTO);
    }

    /**
     * {@code DELETE  /cat-items/:id} : delete the "id" catItem.
     *
     * @param id the id of the catItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cat-items/{id}")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<Void> deleteCatItem(@PathVariable Long id) {
        log.debug("REST request to delete CatItem : {}", id);
        catItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/cat-items/find-by-category/{id}")
    public ResponseEntity<List<CatItemDTO>> findCatItemByCategoryId(@PathVariable Long id) {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, id);
        List<CatItemDTO> catItems = catItemService.findCatItemByCategoryId(id);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }

    @GetMapping("/cat-items/producer-sleep-interval")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<?> getProducerSleepIntervalParam() {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, Constants.PRODUCER_SLEEP_INTERVAL_PARAM);
        CatItemDTO catItem = catItemService.getCatItemByItemCodeAndStatus(Constants.PRODUCER_SLEEP_INTERVAL_PARAM, 1L);
        Long value = Long.valueOf(catItem.getItemValue());
        if(DataUtil.isNullOrEmpty(value)){
            return ResponseEntity.badRequest().body(Constants.PRODUCER_SLEEP_INTERVAL_PARAM + " not exists");
        }
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, Constants.PRODUCER_SLEEP_INTERVAL_PARAM);
        return ResponseEntity.ok().headers(headers).body(value);
    }

    @GetMapping("/cat-items/auto-reload-chart")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<?> getAutoReloadChartParam() {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, Constants.AUTO_RELOAD_CHART_PARAM);
        CatItemDTO catItem = catItemService.getCatItemByItemCodeAndStatus(Constants.AUTO_RELOAD_CHART_PARAM, 1L);
        Long value = Long.valueOf(catItem.getItemValue());
        if(DataUtil.isNullOrEmpty(value)){
            return ResponseEntity.badRequest().body(Constants.AUTO_RELOAD_CHART_PARAM + " not exists");
        }
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, Constants.AUTO_RELOAD_CHART_PARAM);
        return ResponseEntity.ok().headers(headers).body(value);
    }

    @PostMapping("/cat-items/auto-reload-chart")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<?> updateAutoReloadChartParam(@RequestBody CatItemDTO catItemDTO) {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, Constants.AUTO_RELOAD_CHART_PARAM);
        CatItemDTO catItem = catItemService.getCatItemByItemCodeAndStatus(Constants.AUTO_RELOAD_CHART_PARAM, 1L);
        if(DataUtil.isNullOrEmpty(catItemDTO.getItemValue())){
            return ResponseEntity.badRequest().body(Constants.AUTO_RELOAD_CHART_PARAM + " not exists");
        }
        catItem.setItemValue(catItemDTO.getItemValue());
        CatItemDTO catItemDTO1 = catItemService.save(catItem);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, Constants.AUTO_RELOAD_CHART_PARAM);
        return ResponseEntity.ok().headers(headers).body(catItemDTO1.getItemValue());
    }

    @GetMapping("/cat-items/auto-increase-chart-code")
    public ResponseEntity<?> getAutoIncreaseChartCodeParam() {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, Constants.CHART_CODE_AUTO_INCREASE);
        CatItemDTO catItem = catItemService.getCatItemByItemCodeAndStatus(Constants.CHART_CODE_AUTO_INCREASE, 1L);
        if(DataUtil.isNullOrEmpty(catItem.getItemValue())){
            return ResponseEntity.badRequest().body(Constants.CHART_CODE_AUTO_INCREASE + " not exists");
        }
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, Constants.AUTO_RELOAD_CHART_PARAM);
        return ResponseEntity.ok().headers(headers).body(catItem.getItemValue());
    }

    @PostMapping("/cat-items/auto-increase-chart-code")
    public ResponseEntity<?> updateAutoIncreaseChartCodeParam() {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, Constants.CHART_CODE_AUTO_INCREASE);
        CatItemDTO catItem = catItemService.getCatItemByItemCodeAndStatus(Constants.CHART_CODE_AUTO_INCREASE, 1L);
        if(DataUtil.isNullOrEmpty(catItem.getItemValue())){
            return ResponseEntity.badRequest().body(Constants.CHART_CODE_AUTO_INCREASE + " not exists");
        }
        // chartCode += 1
        catItemService.updateAutoIncreaseChartCode(1);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, Constants.AUTO_RELOAD_CHART_PARAM);
        return ResponseEntity.ok().headers(headers).body(catItem.getItemValue());
    }

    @GetMapping("/cat-items/find-by-category-upper/{id}")
    public ResponseEntity<List<CatItemDTO>> findCatItemUpperByCategoryId(@PathVariable Long id) {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, id);
        List<CatItemDTO> catItems = catItemService.findCatItemByCategoryId(id).stream().peek(e->e.setItemValue(e.getItemValue().toUpperCase())).collect(Collectors.toList());
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }
    @GetMapping("/cat-items-get-list-category")
    public ResponseEntity<List<CategoryDTO>> getListCategory() {
        log.debug(REST_REQUEST_TO_GET_GET_LIST_CATEGORY_);
        List<CategoryDTO> catItems = catItemService.findAllCategory();
        return ResponseEntity.ok().body(catItems);
    }
    @GetMapping("/cat-items-get-list-parent")
    public ResponseEntity<List<CatItemDTO>> findListParent() {
        log.debug(REST_REQUEST_TO_GET_GET_LIST_CATEGORY_);
        List<CatItemDTO> catItems = catItemService.findListParent();
        return ResponseEntity.ok().body(catItems);
    }
    @GetMapping("/cat-items-get-all")
    public ResponseEntity<List<CatItemDTO>> findAllItem() {
        log.debug(REST_REQUEST_TO_GET_GET_LIST_CATEGORY_);
        List<CatItemDTO> catItems = catItemService.findAllByStatus(Constants.STATUS_ACTIVE);
        return ResponseEntity.ok().body(catItems);
    }

    @PostMapping("/cat-items/query")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<List<CatItemDetail>> query(@RequestBody CatItemDTO catItemDTO, Pageable pageable) {
        log.debug("REST request to get a page of CatItems");
        Page<CatItemDetail> page = catItemService.query(catItemDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/cat-items/find-group-kpi-by-domain/{domainCode}")
    public ResponseEntity<List<CatItemDTO>> findCatItemByDomainCode(@PathVariable String domainCode) {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, domainCode);
        List<CatItemDTO> catItems = catItemService.findCatItemByDomainCode(domainCode);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(domainCode));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }

    @GetMapping("/cat-items/find-domain-by-kpi/{groupKpiCode}")
    public ResponseEntity<CatItemDTO> findDomainByGroupKpiCode(@PathVariable String groupKpiCode) {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, groupKpiCode);
        CatItemDTO catItem = catItemService.findDomainByGroupKpiCode(groupKpiCode);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(groupKpiCode));
        return ResponseEntity.ok().headers(headers).body(catItem);
    }

    @GetMapping("/cat-items/find-table-by-database/{database}")
    public ResponseEntity<List<CatItemDTO>> findTableByDatabase(@PathVariable String database) {
        log.debug(REST_REQUEST_TO_GET_CAT_ITEM, database);
        List<CatItemDTO> catItems = catItemService.findTableByDatabase(database);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(database));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }
    @GetMapping("/cat-items/find-domain-by-user")
    public ResponseEntity<List<CatItemDTO>> findDomainByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute(Constants.ACTOR);
        log.debug(REST_REQUEST_TO_FIND_DOMAIN_BY_USER, username);
        List<CatItemDTO> catItems = catItemService.findDomainByUser(username);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(username));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }

    @GetMapping("/cat-items/get-catItem-by-categoryCode/{categoryCode}")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<List<CatItemDTO>> getCatItemByCategoryCode(@PathVariable String categoryCode) {
        log.debug(REST_REQUEST_TO_FIND_DOMAIN_BY_USER, categoryCode);
        List<CatItemDTO> catItems = catItemService.getCatItemByCategoryCode(categoryCode);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(categoryCode));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }
  @GetMapping("/cat-items/get-time-type-map/{domainCode}")
    public ResponseEntity<List<CatItemDTO>> getTimeTypeMap(@PathVariable String domainCode) {
        log.debug(REST_REQUEST_TO_FIND_DOMAIN_BY_USER, domainCode);
        List<CatItemDTO> catItems = catItemService.getTimeTypeMap(domainCode);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(domainCode));
        return ResponseEntity.ok().headers(headers).body(catItems);
    }

    @GetMapping("/cat-items/findDomainAndTable")
    public ResponseEntity<List<CatItemDTO>> findDomainAndTable(){
        log.debug(REST_REQUEST_TO_GET_GET_LIST_CATEGORY_);
        List<CatItemDTO> catItems = catItemService.findDomainAndTable();
        return ResponseEntity.ok().body(catItems);
    }

    @PutMapping("/cat-items/multiple-delete")
    @PreAuthorize("hasPermission('CAT-ITEM', '*')")
    public ResponseEntity<Void> deleteCatItemMultiple(@RequestBody List<CatItemDTO> catItems){
        log.debug("REST request to delete multiple item :", catItems.size(), " item");
        catItemService.multipleDelete(catItems);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, catItems.size() + " item")).build();
    }

}
