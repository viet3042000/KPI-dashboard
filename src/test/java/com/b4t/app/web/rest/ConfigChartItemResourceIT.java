package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigChartItem;
import com.b4t.app.repository.ConfigChartItemRepository;
import com.b4t.app.service.ConfigChartItemService;
import com.b4t.app.service.dto.ConfigChartItemDTO;
import com.b4t.app.service.mapper.ConfigChartItemMapper;
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
 * Integration tests for the {@link ConfigChartItemResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigChartItemResourceIT {

    private static final Long DEFAULT_CHART_ID = 1L;
    private static final Long UPDATED_CHART_ID = 2L;

    private static final String DEFAULT_TYPE_CHART = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CHART = "BBBBBBBBBB";

    private static final Long DEFAULT_HAS_AVG_LINE = 1L;
    private static final Long UPDATED_HAS_AVG_LINE = 2L;

    private static final String DEFAULT_LIST_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_LIST_COLOR = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final String DEFAULT_QUERY_ID = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_1 = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_2 = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_3 = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_3 = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_4 = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_4 = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_5 = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_5 = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigChartItemRepository configChartItemRepository;

    @Autowired
    private ConfigChartItemMapper configChartItemMapper;

    @Autowired
    private ConfigChartItemService configChartItemService;

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

    private MockMvc restConfigChartItemMockMvc;

    private ConfigChartItem configChartItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigChartItemResource configChartItemResource = new ConfigChartItemResource(configChartItemService);
        this.restConfigChartItemMockMvc = MockMvcBuilders.standaloneSetup(configChartItemResource)
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
    public static ConfigChartItem createEntity(EntityManager em) {
        ConfigChartItem configChartItem = new ConfigChartItem()
            .chartId(DEFAULT_CHART_ID)
            .typeChart(DEFAULT_TYPE_CHART)
            .hasAvgLine(DEFAULT_HAS_AVG_LINE)
            .listColor(DEFAULT_LIST_COLOR)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .queryId(DEFAULT_QUERY_ID)
            .condition1(DEFAULT_CONDITION_1)
            .condition2(DEFAULT_CONDITION_2)
            .condition3(DEFAULT_CONDITION_3)
            .condition4(DEFAULT_CONDITION_4)
            .condition5(DEFAULT_CONDITION_5)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configChartItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigChartItem createUpdatedEntity(EntityManager em) {
        ConfigChartItem configChartItem = new ConfigChartItem()
            .chartId(UPDATED_CHART_ID)
            .typeChart(UPDATED_TYPE_CHART)
            .hasAvgLine(UPDATED_HAS_AVG_LINE)
            .listColor(UPDATED_LIST_COLOR)
            .orderIndex(UPDATED_ORDER_INDEX)
            .queryId(UPDATED_QUERY_ID)
            .condition1(UPDATED_CONDITION_1)
            .condition2(UPDATED_CONDITION_2)
            .condition3(UPDATED_CONDITION_3)
            .condition4(UPDATED_CONDITION_4)
            .condition5(UPDATED_CONDITION_5)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configChartItem;
    }

    @BeforeEach
    public void initTest() {
        configChartItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigChartItem() throws Exception {
        int databaseSizeBeforeCreate = configChartItemRepository.findAll().size();

        // Create the ConfigChartItem
        ConfigChartItemDTO configChartItemDTO = configChartItemMapper.toDto(configChartItem);
        restConfigChartItemMockMvc.perform(post("/api/config-chart-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigChartItem in the database
        List<ConfigChartItem> configChartItemList = configChartItemRepository.findAll();
        assertThat(configChartItemList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigChartItem testConfigChartItem = configChartItemList.get(configChartItemList.size() - 1);
        assertThat(testConfigChartItem.getChartId()).isEqualTo(DEFAULT_CHART_ID);
        assertThat(testConfigChartItem.getTypeChart()).isEqualTo(DEFAULT_TYPE_CHART);
        assertThat(testConfigChartItem.getHasAvgLine()).isEqualTo(DEFAULT_HAS_AVG_LINE);
        assertThat(testConfigChartItem.getListColor()).isEqualTo(DEFAULT_LIST_COLOR);
        assertThat(testConfigChartItem.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigChartItem.getQueryId()).isEqualTo(DEFAULT_QUERY_ID);
        assertThat(testConfigChartItem.getCondition1()).isEqualTo(DEFAULT_CONDITION_1);
        assertThat(testConfigChartItem.getCondition2()).isEqualTo(DEFAULT_CONDITION_2);
        assertThat(testConfigChartItem.getCondition3()).isEqualTo(DEFAULT_CONDITION_3);
        assertThat(testConfigChartItem.getCondition4()).isEqualTo(DEFAULT_CONDITION_4);
        assertThat(testConfigChartItem.getCondition5()).isEqualTo(DEFAULT_CONDITION_5);
        assertThat(testConfigChartItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigChartItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigChartItem.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigChartItem.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigChartItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configChartItemRepository.findAll().size();

        // Create the ConfigChartItem with an existing ID
        configChartItem.setId(1L);
        ConfigChartItemDTO configChartItemDTO = configChartItemMapper.toDto(configChartItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigChartItemMockMvc.perform(post("/api/config-chart-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigChartItem in the database
        List<ConfigChartItem> configChartItemList = configChartItemRepository.findAll();
        assertThat(configChartItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigChartItems() throws Exception {
        // Initialize the database
        configChartItemRepository.saveAndFlush(configChartItem);

        // Get all the configChartItemList
        restConfigChartItemMockMvc.perform(get("/api/config-chart-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configChartItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartId").value(hasItem(DEFAULT_CHART_ID.intValue())))
            .andExpect(jsonPath("$.[*].typeChart").value(hasItem(DEFAULT_TYPE_CHART)))
            .andExpect(jsonPath("$.[*].hasAvgLine").value(hasItem(DEFAULT_HAS_AVG_LINE.intValue())))
            .andExpect(jsonPath("$.[*].listColor").value(hasItem(DEFAULT_LIST_COLOR)))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].queryId").value(hasItem(DEFAULT_QUERY_ID)))
            .andExpect(jsonPath("$.[*].condition1").value(hasItem(DEFAULT_CONDITION_1)))
            .andExpect(jsonPath("$.[*].condition2").value(hasItem(DEFAULT_CONDITION_2)))
            .andExpect(jsonPath("$.[*].condition3").value(hasItem(DEFAULT_CONDITION_3)))
            .andExpect(jsonPath("$.[*].condition4").value(hasItem(DEFAULT_CONDITION_4)))
            .andExpect(jsonPath("$.[*].condition5").value(hasItem(DEFAULT_CONDITION_5)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigChartItem() throws Exception {
        // Initialize the database
        configChartItemRepository.saveAndFlush(configChartItem);

        // Get the configChartItem
        restConfigChartItemMockMvc.perform(get("/api/config-chart-items/{id}", configChartItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configChartItem.getId().intValue()))
            .andExpect(jsonPath("$.chartId").value(DEFAULT_CHART_ID.intValue()))
            .andExpect(jsonPath("$.typeChart").value(DEFAULT_TYPE_CHART))
            .andExpect(jsonPath("$.hasAvgLine").value(DEFAULT_HAS_AVG_LINE.intValue()))
            .andExpect(jsonPath("$.listColor").value(DEFAULT_LIST_COLOR))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.queryId").value(DEFAULT_QUERY_ID))
            .andExpect(jsonPath("$.condition1").value(DEFAULT_CONDITION_1))
            .andExpect(jsonPath("$.condition2").value(DEFAULT_CONDITION_2))
            .andExpect(jsonPath("$.condition3").value(DEFAULT_CONDITION_3))
            .andExpect(jsonPath("$.condition4").value(DEFAULT_CONDITION_4))
            .andExpect(jsonPath("$.condition5").value(DEFAULT_CONDITION_5))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigChartItem() throws Exception {
        // Get the configChartItem
        restConfigChartItemMockMvc.perform(get("/api/config-chart-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigChartItem() throws Exception {
        // Initialize the database
        configChartItemRepository.saveAndFlush(configChartItem);

        int databaseSizeBeforeUpdate = configChartItemRepository.findAll().size();

        // Update the configChartItem
        ConfigChartItem updatedConfigChartItem = configChartItemRepository.findById(configChartItem.getId()).get();
        // Disconnect from session so that the updates on updatedConfigChartItem are not directly saved in db
        em.detach(updatedConfigChartItem);
        updatedConfigChartItem
            .chartId(UPDATED_CHART_ID)
            .typeChart(UPDATED_TYPE_CHART)
            .hasAvgLine(UPDATED_HAS_AVG_LINE)
            .listColor(UPDATED_LIST_COLOR)
            .orderIndex(UPDATED_ORDER_INDEX)
            .queryId(UPDATED_QUERY_ID)
            .condition1(UPDATED_CONDITION_1)
            .condition2(UPDATED_CONDITION_2)
            .condition3(UPDATED_CONDITION_3)
            .condition4(UPDATED_CONDITION_4)
            .condition5(UPDATED_CONDITION_5)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigChartItemDTO configChartItemDTO = configChartItemMapper.toDto(updatedConfigChartItem);

        restConfigChartItemMockMvc.perform(put("/api/config-chart-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartItemDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigChartItem in the database
        List<ConfigChartItem> configChartItemList = configChartItemRepository.findAll();
        assertThat(configChartItemList).hasSize(databaseSizeBeforeUpdate);
        ConfigChartItem testConfigChartItem = configChartItemList.get(configChartItemList.size() - 1);
        assertThat(testConfigChartItem.getChartId()).isEqualTo(UPDATED_CHART_ID);
        assertThat(testConfigChartItem.getTypeChart()).isEqualTo(UPDATED_TYPE_CHART);
        assertThat(testConfigChartItem.getHasAvgLine()).isEqualTo(UPDATED_HAS_AVG_LINE);
        assertThat(testConfigChartItem.getListColor()).isEqualTo(UPDATED_LIST_COLOR);
        assertThat(testConfigChartItem.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigChartItem.getQueryId()).isEqualTo(UPDATED_QUERY_ID);
        assertThat(testConfigChartItem.getCondition1()).isEqualTo(UPDATED_CONDITION_1);
        assertThat(testConfigChartItem.getCondition2()).isEqualTo(UPDATED_CONDITION_2);
        assertThat(testConfigChartItem.getCondition3()).isEqualTo(UPDATED_CONDITION_3);
        assertThat(testConfigChartItem.getCondition4()).isEqualTo(UPDATED_CONDITION_4);
        assertThat(testConfigChartItem.getCondition5()).isEqualTo(UPDATED_CONDITION_5);
        assertThat(testConfigChartItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigChartItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigChartItem.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigChartItem.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigChartItem() throws Exception {
        int databaseSizeBeforeUpdate = configChartItemRepository.findAll().size();

        // Create the ConfigChartItem
        ConfigChartItemDTO configChartItemDTO = configChartItemMapper.toDto(configChartItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigChartItemMockMvc.perform(put("/api/config-chart-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigChartItem in the database
        List<ConfigChartItem> configChartItemList = configChartItemRepository.findAll();
        assertThat(configChartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigChartItem() throws Exception {
        // Initialize the database
        configChartItemRepository.saveAndFlush(configChartItem);

        int databaseSizeBeforeDelete = configChartItemRepository.findAll().size();

        // Delete the configChartItem
        restConfigChartItemMockMvc.perform(delete("/api/config-chart-items/{id}", configChartItem.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigChartItem> configChartItemList = configChartItemRepository.findAll();
        assertThat(configChartItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
