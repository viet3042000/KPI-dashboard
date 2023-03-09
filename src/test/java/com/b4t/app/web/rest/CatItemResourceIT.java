package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.CatItem;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.mapper.CatItemMapper;
import com.b4t.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.b4t.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CatItemResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class CatItemResourceIT {

    private static final Long DEFAULT_ITEM_ID = 1L;
    private static final Long UPDATED_ITEM_ID = 2L;

    private static final String DEFAULT_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final String DEFAULT_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_POSITION = 1L;
    private static final Long UPDATED_POSITION = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_EDITABLE = 1L;
    private static final Long UPDATED_EDITABLE = 2L;

    private static final Long DEFAULT_PARENT_ITEM_ID = 1L;
    private static final Long UPDATED_PARENT_ITEM_ID = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private CatItemRepository catItemRepository;

    @Autowired
    private CatItemMapper catItemMapper;

    @Autowired
    private CatItemService catItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCatItemMockMvc;

    private CatItem catItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CatItemResource catItemResource = new CatItemResource(catItemService);
        this.restCatItemMockMvc = MockMvcBuilders.standaloneSetup(catItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatItem createEntity(EntityManager em) {
        CatItem catItem = new CatItem()
            .itemId(DEFAULT_ITEM_ID)
            .itemCode(DEFAULT_ITEM_CODE)
            .itemName(DEFAULT_ITEM_NAME)
            .itemValue(DEFAULT_ITEM_VALUE)
            .categoryId(DEFAULT_CATEGORY_ID)
            .categoryCode(DEFAULT_CATEGORY_CODE)
            .position(DEFAULT_POSITION)
            .description(DEFAULT_DESCRIPTION)
            .editable(DEFAULT_EDITABLE)
            .parentItemId(DEFAULT_PARENT_ITEM_ID)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return catItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatItem createUpdatedEntity(EntityManager em) {
        CatItem catItem = new CatItem()
            .itemId(UPDATED_ITEM_ID)
            .itemCode(UPDATED_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .itemValue(UPDATED_ITEM_VALUE)
            .categoryId(UPDATED_CATEGORY_ID)
            .categoryCode(UPDATED_CATEGORY_CODE)
            .position(UPDATED_POSITION)
            .description(UPDATED_DESCRIPTION)
            .editable(UPDATED_EDITABLE)
            .parentItemId(UPDATED_PARENT_ITEM_ID)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return catItem;
    }

    @BeforeEach
    public void initTest() {
        catItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatItem() throws Exception {
        int databaseSizeBeforeCreate = catItemRepository.findAll().size();

        // Create the CatItem
        CatItemDTO catItemDTO = catItemMapper.toDto(catItem);
        restCatItemMockMvc.perform(post("/api/cat-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catItemDTO)))
            .andExpect(status().isCreated());

        // Validate the CatItem in the database
        List<CatItem> catItemList = catItemRepository.findAll();
        assertThat(catItemList).hasSize(databaseSizeBeforeCreate + 1);
        CatItem testCatItem = catItemList.get(catItemList.size() - 1);
        assertThat(testCatItem.getItemId()).isEqualTo(DEFAULT_ITEM_ID);
        assertThat(testCatItem.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testCatItem.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testCatItem.getItemValue()).isEqualTo(DEFAULT_ITEM_VALUE);
        assertThat(testCatItem.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testCatItem.getCategoryCode()).isEqualTo(DEFAULT_CATEGORY_CODE);
        assertThat(testCatItem.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testCatItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatItem.getEditable()).isEqualTo(DEFAULT_EDITABLE);
        assertThat(testCatItem.getParentItemId()).isEqualTo(DEFAULT_PARENT_ITEM_ID);
        assertThat(testCatItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCatItem.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testCatItem.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createCatItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catItemRepository.findAll().size();

        // Create the CatItem with an existing ID
        catItem.setItemId(1L);
        CatItemDTO catItemDTO = catItemMapper.toDto(catItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatItemMockMvc.perform(post("/api/cat-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatItem in the database
        List<CatItem> catItemList = catItemRepository.findAll();
        assertThat(catItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCatItems() throws Exception {
        // Initialize the database
        catItemRepository.saveAndFlush(catItem);

        // Get all the catItemList
        restCatItemMockMvc.perform(get("/api/cat-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catItem.getItemId().intValue())))
            .andExpect(jsonPath("$.[*].itemId").value(hasItem(DEFAULT_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].itemValue").value(hasItem(DEFAULT_ITEM_VALUE)))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].editable").value(hasItem(DEFAULT_EDITABLE.intValue())))
            .andExpect(jsonPath("$.[*].parentItemId").value(hasItem(DEFAULT_PARENT_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getCatItem() throws Exception {
        // Initialize the database
        catItemRepository.saveAndFlush(catItem);

        // Get the catItem
        restCatItemMockMvc.perform(get("/api/cat-items/{id}", catItem.getItemId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catItem.getItemId().intValue()))
            .andExpect(jsonPath("$.itemId").value(DEFAULT_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.itemValue").value(DEFAULT_ITEM_VALUE))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.categoryCode").value(DEFAULT_CATEGORY_CODE))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.editable").value(DEFAULT_EDITABLE.intValue()))
            .andExpect(jsonPath("$.parentItemId").value(DEFAULT_PARENT_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingCatItem() throws Exception {
        // Get the catItem
        restCatItemMockMvc.perform(get("/api/cat-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatItem() throws Exception {
        // Initialize the database
        catItemRepository.saveAndFlush(catItem);

        int databaseSizeBeforeUpdate = catItemRepository.findAll().size();

        // Update the catItem
        CatItem updatedCatItem = catItemRepository.findById(catItem.getItemId()).get();
        // Disconnect from session so that the updates on updatedCatItem are not directly saved in db
        em.detach(updatedCatItem);
        updatedCatItem
            .itemId(UPDATED_ITEM_ID)
            .itemCode(UPDATED_ITEM_CODE)
            .itemName(UPDATED_ITEM_NAME)
            .itemValue(UPDATED_ITEM_VALUE)
            .categoryId(UPDATED_CATEGORY_ID)
            .categoryCode(UPDATED_CATEGORY_CODE)
            .position(UPDATED_POSITION)
            .description(UPDATED_DESCRIPTION)
            .editable(UPDATED_EDITABLE)
            .parentItemId(UPDATED_PARENT_ITEM_ID)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        CatItemDTO catItemDTO = catItemMapper.toDto(updatedCatItem);

        restCatItemMockMvc.perform(put("/api/cat-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catItemDTO)))
            .andExpect(status().isOk());

        // Validate the CatItem in the database
        List<CatItem> catItemList = catItemRepository.findAll();
        assertThat(catItemList).hasSize(databaseSizeBeforeUpdate);
        CatItem testCatItem = catItemList.get(catItemList.size() - 1);
        assertThat(testCatItem.getItemId()).isEqualTo(UPDATED_ITEM_ID);
        assertThat(testCatItem.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testCatItem.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testCatItem.getItemValue()).isEqualTo(UPDATED_ITEM_VALUE);
        assertThat(testCatItem.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testCatItem.getCategoryCode()).isEqualTo(UPDATED_CATEGORY_CODE);
        assertThat(testCatItem.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testCatItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatItem.getEditable()).isEqualTo(UPDATED_EDITABLE);
        assertThat(testCatItem.getParentItemId()).isEqualTo(UPDATED_PARENT_ITEM_ID);
        assertThat(testCatItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCatItem.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testCatItem.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingCatItem() throws Exception {
        int databaseSizeBeforeUpdate = catItemRepository.findAll().size();

        // Create the CatItem
        CatItemDTO catItemDTO = catItemMapper.toDto(catItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatItemMockMvc.perform(put("/api/cat-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatItem in the database
        List<CatItem> catItemList = catItemRepository.findAll();
        assertThat(catItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCatItem() throws Exception {
        // Initialize the database
        catItemRepository.saveAndFlush(catItem);

        int databaseSizeBeforeDelete = catItemRepository.findAll().size();

        // Delete the catItem
        restCatItemMockMvc.perform(delete("/api/cat-items/{id}", catItem.getItemId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatItem> catItemList = catItemRepository.findAll();
        assertThat(catItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
