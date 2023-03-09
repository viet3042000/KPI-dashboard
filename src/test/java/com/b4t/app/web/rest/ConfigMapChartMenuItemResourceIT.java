package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigMapChartMenuItem;
import com.b4t.app.repository.ConfigMapChartMenuItemRepository;
import com.b4t.app.service.ConfigMapChartMenuItemService;
import com.b4t.app.service.dto.ConfigMapChartMenuItemDTO;
import com.b4t.app.service.mapper.ConfigMapChartMenuItemMapper;
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
 * Integration tests for the {@link ConfigMapChartMenuItemResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigMapChartMenuItemResourceIT {

    private static final Long DEFAULT_CHART_ID = 1L;
    private static final Long UPDATED_CHART_ID = 2L;

    private static final Long DEFAULT_MENU_ITEM_ID = 1L;
    private static final Long UPDATED_MENU_ITEM_ID = 2L;

    private static final Long DEFAULT_IS_MAIN = 1L;
    private static final Long UPDATED_IS_MAIN = 2L;

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigMapChartMenuItemRepository configMapChartMenuItemRepository;

    @Autowired
    private ConfigMapChartMenuItemMapper configMapChartMenuItemMapper;

    @Autowired
    private ConfigMapChartMenuItemService configMapChartMenuItemService;

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

    private MockMvc restConfigMapChartMenuItemMockMvc;

    private ConfigMapChartMenuItem configMapChartMenuItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMapChartMenuItemResource configMapChartMenuItemResource = new ConfigMapChartMenuItemResource(configMapChartMenuItemService);
        this.restConfigMapChartMenuItemMockMvc = MockMvcBuilders.standaloneSetup(configMapChartMenuItemResource)
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
    public static ConfigMapChartMenuItem createEntity(EntityManager em) {
        ConfigMapChartMenuItem configMapChartMenuItem = new ConfigMapChartMenuItem()
            .chartId(DEFAULT_CHART_ID)
            .menuItemId(DEFAULT_MENU_ITEM_ID)
            .isMain(DEFAULT_IS_MAIN)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configMapChartMenuItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigMapChartMenuItem createUpdatedEntity(EntityManager em) {
        ConfigMapChartMenuItem configMapChartMenuItem = new ConfigMapChartMenuItem()
            .chartId(UPDATED_CHART_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .isMain(UPDATED_IS_MAIN)
            .orderIndex(UPDATED_ORDER_INDEX)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configMapChartMenuItem;
    }

    @BeforeEach
    public void initTest() {
        configMapChartMenuItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigMapChartMenuItem() throws Exception {
        int databaseSizeBeforeCreate = configMapChartMenuItemRepository.findAll().size();

        // Create the ConfigMapChartMenuItem
        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO = configMapChartMenuItemMapper.toDto(configMapChartMenuItem);
        restConfigMapChartMenuItemMockMvc.perform(post("/api/config-map-chart-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartMenuItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigMapChartMenuItem in the database
        List<ConfigMapChartMenuItem> configMapChartMenuItemList = configMapChartMenuItemRepository.findAll();
        assertThat(configMapChartMenuItemList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMapChartMenuItem testConfigMapChartMenuItem = configMapChartMenuItemList.get(configMapChartMenuItemList.size() - 1);
        assertThat(testConfigMapChartMenuItem.getChartId()).isEqualTo(DEFAULT_CHART_ID);
        assertThat(testConfigMapChartMenuItem.getMenuItemId()).isEqualTo(DEFAULT_MENU_ITEM_ID);
        assertThat(testConfigMapChartMenuItem.getIsMain()).isEqualTo(DEFAULT_IS_MAIN);
        assertThat(testConfigMapChartMenuItem.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigMapChartMenuItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigMapChartMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigMapChartMenuItem.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigMapChartMenuItem.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigMapChartMenuItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMapChartMenuItemRepository.findAll().size();

        // Create the ConfigMapChartMenuItem with an existing ID
        configMapChartMenuItem.setId(1L);
        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO = configMapChartMenuItemMapper.toDto(configMapChartMenuItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMapChartMenuItemMockMvc.perform(post("/api/config-map-chart-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartMenuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapChartMenuItem in the database
        List<ConfigMapChartMenuItem> configMapChartMenuItemList = configMapChartMenuItemRepository.findAll();
        assertThat(configMapChartMenuItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigMapChartMenuItems() throws Exception {
        // Initialize the database
        configMapChartMenuItemRepository.saveAndFlush(configMapChartMenuItem);

        // Get all the configMapChartMenuItemList
        restConfigMapChartMenuItemMockMvc.perform(get("/api/config-map-chart-menu-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMapChartMenuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartId").value(hasItem(DEFAULT_CHART_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuItemId").value(hasItem(DEFAULT_MENU_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].isMain").value(hasItem(DEFAULT_IS_MAIN.intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigMapChartMenuItem() throws Exception {
        // Initialize the database
        configMapChartMenuItemRepository.saveAndFlush(configMapChartMenuItem);

        // Get the configMapChartMenuItem
        restConfigMapChartMenuItemMockMvc.perform(get("/api/config-map-chart-menu-items/{id}", configMapChartMenuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configMapChartMenuItem.getId().intValue()))
            .andExpect(jsonPath("$.chartId").value(DEFAULT_CHART_ID.intValue()))
            .andExpect(jsonPath("$.menuItemId").value(DEFAULT_MENU_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.isMain").value(DEFAULT_IS_MAIN.intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigMapChartMenuItem() throws Exception {
        // Get the configMapChartMenuItem
        restConfigMapChartMenuItemMockMvc.perform(get("/api/config-map-chart-menu-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigMapChartMenuItem() throws Exception {
        // Initialize the database
        configMapChartMenuItemRepository.saveAndFlush(configMapChartMenuItem);

        int databaseSizeBeforeUpdate = configMapChartMenuItemRepository.findAll().size();

        // Update the configMapChartMenuItem
        ConfigMapChartMenuItem updatedConfigMapChartMenuItem = configMapChartMenuItemRepository.findById(configMapChartMenuItem.getId()).get();
        // Disconnect from session so that the updates on updatedConfigMapChartMenuItem are not directly saved in db
        em.detach(updatedConfigMapChartMenuItem);
        updatedConfigMapChartMenuItem
            .chartId(UPDATED_CHART_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .isMain(UPDATED_IS_MAIN)
            .orderIndex(UPDATED_ORDER_INDEX)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO = configMapChartMenuItemMapper.toDto(updatedConfigMapChartMenuItem);

        restConfigMapChartMenuItemMockMvc.perform(put("/api/config-map-chart-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartMenuItemDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigMapChartMenuItem in the database
        List<ConfigMapChartMenuItem> configMapChartMenuItemList = configMapChartMenuItemRepository.findAll();
        assertThat(configMapChartMenuItemList).hasSize(databaseSizeBeforeUpdate);
        ConfigMapChartMenuItem testConfigMapChartMenuItem = configMapChartMenuItemList.get(configMapChartMenuItemList.size() - 1);
        assertThat(testConfigMapChartMenuItem.getChartId()).isEqualTo(UPDATED_CHART_ID);
        assertThat(testConfigMapChartMenuItem.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testConfigMapChartMenuItem.getIsMain()).isEqualTo(UPDATED_IS_MAIN);
        assertThat(testConfigMapChartMenuItem.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigMapChartMenuItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigMapChartMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigMapChartMenuItem.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigMapChartMenuItem.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigMapChartMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = configMapChartMenuItemRepository.findAll().size();

        // Create the ConfigMapChartMenuItem
        ConfigMapChartMenuItemDTO configMapChartMenuItemDTO = configMapChartMenuItemMapper.toDto(configMapChartMenuItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMapChartMenuItemMockMvc.perform(put("/api/config-map-chart-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartMenuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapChartMenuItem in the database
        List<ConfigMapChartMenuItem> configMapChartMenuItemList = configMapChartMenuItemRepository.findAll();
        assertThat(configMapChartMenuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigMapChartMenuItem() throws Exception {
        // Initialize the database
        configMapChartMenuItemRepository.saveAndFlush(configMapChartMenuItem);

        int databaseSizeBeforeDelete = configMapChartMenuItemRepository.findAll().size();

        // Delete the configMapChartMenuItem
        restConfigMapChartMenuItemMockMvc.perform(delete("/api/config-map-chart-menu-items/{id}", configMapChartMenuItem.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigMapChartMenuItem> configMapChartMenuItemList = configMapChartMenuItemRepository.findAll();
        assertThat(configMapChartMenuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
