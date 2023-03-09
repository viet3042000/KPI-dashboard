package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigChart;
import com.b4t.app.repository.CatGraphKpiRepository;
import com.b4t.app.repository.CatItemRepository;
import com.b4t.app.repository.ConfigChartRepository;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.ConfigChartDTO;
import com.b4t.app.service.mapper.ConfigChartMapper;
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
 * Integration tests for the {@link ConfigChartResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigChartResourceIT {

    private static final String DEFAULT_CHART_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHART_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHART_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHART_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_CHART = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_CHART = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_CHART = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CHART = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME_TYPE_DEFAULT = 1;
    private static final Integer UPDATED_TIME_TYPE_DEFAULT = 2;

    private static final Integer DEFAULT_RELATIVE_TIME = 1;
    private static final Integer UPDATED_RELATIVE_TIME = 2;

    private static final String DEFAULT_CHART_URL = "AAAAAAAAAA";
    private static final String UPDATED_CHART_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_GROUP_CHART_ID = 1L;
    private static final Long UPDATED_GROUP_CHART_ID = 2L;

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final String DEFAULT_GROUP_KPI_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_KPI_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigChartRepository configChartRepository;

    @Autowired
    private ConfigChartMapper configChartMapper;

    @Autowired
    private ConfigChartService configChartService;

    @Autowired
    private CatGraphKpiRepository catGraphKpiRepository;

    @Autowired
    private CatItemRepository catItemRepository;

    @Autowired
    private BuildChartService buildChartService;

    @Autowired
    private ConfigChartItemService configChartItemService;

    @Autowired
    private ConfigQueryChartService configQueryChartService;

    @Autowired
    private ConfigDisplayQueryService configDisplayQueryService;

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

    private MockMvc restConfigChartMockMvc;

    private ConfigChart configChart;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigChartResource configChartResource = new ConfigChartResource(configChartService, configChartItemService, buildChartService, configQueryChartService, configDisplayQueryService, catGraphKpiRepository, catItemRepository);
        this.restConfigChartMockMvc = MockMvcBuilders.standaloneSetup(configChartResource)
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
    public static ConfigChart createEntity(EntityManager em) {
        ConfigChart configChart = new ConfigChart()
            .chartCode(DEFAULT_CHART_CODE)
            .chartName(DEFAULT_CHART_NAME)
            .titleChart(DEFAULT_TITLE_CHART)
            .typeChart(DEFAULT_TYPE_CHART)
            .timeTypeDefault(DEFAULT_TIME_TYPE_DEFAULT)
            .relativeTime(DEFAULT_RELATIVE_TIME)
            .chartUrl(DEFAULT_CHART_URL)
            .groupChartId(DEFAULT_GROUP_CHART_ID)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .groupKpiCode(DEFAULT_GROUP_KPI_CODE)
            .domainCode(DEFAULT_DOMAIN_CODE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configChart;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigChart createUpdatedEntity(EntityManager em) {
        ConfigChart configChart = new ConfigChart()
            .chartCode(UPDATED_CHART_CODE)
            .chartName(UPDATED_CHART_NAME)
            .titleChart(UPDATED_TITLE_CHART)
            .typeChart(UPDATED_TYPE_CHART)
            .timeTypeDefault(UPDATED_TIME_TYPE_DEFAULT)
            .relativeTime(UPDATED_RELATIVE_TIME)
            .chartUrl(UPDATED_CHART_URL)
            .groupChartId(UPDATED_GROUP_CHART_ID)
            .orderIndex(UPDATED_ORDER_INDEX)
            .groupKpiCode(UPDATED_GROUP_KPI_CODE)
            .domainCode(UPDATED_DOMAIN_CODE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configChart;
    }

    @BeforeEach
    public void initTest() {
        configChart = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigChart() throws Exception {
        int databaseSizeBeforeCreate = configChartRepository.findAll().size();

        // Create the ConfigChart
        ConfigChartDTO configChartDTO = configChartMapper.toDto(configChart);
        restConfigChartMockMvc.perform(post("/api/config-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigChart in the database
        List<ConfigChart> configChartList = configChartRepository.findAll();
        assertThat(configChartList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigChart testConfigChart = configChartList.get(configChartList.size() - 1);
        assertThat(testConfigChart.getChartCode()).isEqualTo(DEFAULT_CHART_CODE);
        assertThat(testConfigChart.getChartName()).isEqualTo(DEFAULT_CHART_NAME);
        assertThat(testConfigChart.getTitleChart()).isEqualTo(DEFAULT_TITLE_CHART);
        assertThat(testConfigChart.getTypeChart()).isEqualTo(DEFAULT_TYPE_CHART);
        assertThat(testConfigChart.getTimeTypeDefault()).isEqualTo(DEFAULT_TIME_TYPE_DEFAULT);
        assertThat(testConfigChart.getRelativeTime()).isEqualTo(DEFAULT_RELATIVE_TIME);
        assertThat(testConfigChart.getChartUrl()).isEqualTo(DEFAULT_CHART_URL);
        assertThat(testConfigChart.getGroupChartId()).isEqualTo(DEFAULT_GROUP_CHART_ID);
        assertThat(testConfigChart.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigChart.getGroupKpiCode()).isEqualTo(DEFAULT_GROUP_KPI_CODE);
        assertThat(testConfigChart.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
        assertThat(testConfigChart.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigChart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigChart.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigChart.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigChartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configChartRepository.findAll().size();

        // Create the ConfigChart with an existing ID
        configChart.setId(1L);
        ConfigChartDTO configChartDTO = configChartMapper.toDto(configChart);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigChartMockMvc.perform(post("/api/config-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigChart in the database
        List<ConfigChart> configChartList = configChartRepository.findAll();
        assertThat(configChartList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigCharts() throws Exception {
        // Initialize the database
        configChartRepository.saveAndFlush(configChart);

        // Get all the configChartList
        restConfigChartMockMvc.perform(get("/api/config-charts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configChart.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartCode").value(hasItem(DEFAULT_CHART_CODE)))
            .andExpect(jsonPath("$.[*].chartName").value(hasItem(DEFAULT_CHART_NAME)))
            .andExpect(jsonPath("$.[*].titleChart").value(hasItem(DEFAULT_TITLE_CHART)))
            .andExpect(jsonPath("$.[*].typeChart").value(hasItem(DEFAULT_TYPE_CHART)))
            .andExpect(jsonPath("$.[*].timeTypeDefault").value(hasItem(DEFAULT_TIME_TYPE_DEFAULT.intValue())))
            .andExpect(jsonPath("$.[*].relativeTime").value(hasItem(DEFAULT_RELATIVE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].chartUrl").value(hasItem(DEFAULT_CHART_URL)))
            .andExpect(jsonPath("$.[*].groupChartId").value(hasItem(DEFAULT_GROUP_CHART_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].groupKpiCode").value(hasItem(DEFAULT_GROUP_KPI_CODE)))
            .andExpect(jsonPath("$.[*].domainCode").value(hasItem(DEFAULT_DOMAIN_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigChart() throws Exception {
        // Initialize the database
        configChartRepository.saveAndFlush(configChart);

        // Get the configChart
        restConfigChartMockMvc.perform(get("/api/config-charts/{id}", configChart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configChart.getId().intValue()))
            .andExpect(jsonPath("$.chartCode").value(DEFAULT_CHART_CODE))
            .andExpect(jsonPath("$.chartName").value(DEFAULT_CHART_NAME))
            .andExpect(jsonPath("$.titleChart").value(DEFAULT_TITLE_CHART))
            .andExpect(jsonPath("$.typeChart").value(DEFAULT_TYPE_CHART))
            .andExpect(jsonPath("$.timeTypeDefault").value(DEFAULT_TIME_TYPE_DEFAULT.intValue()))
            .andExpect(jsonPath("$.relativeTime").value(DEFAULT_RELATIVE_TIME.intValue()))
            .andExpect(jsonPath("$.chartUrl").value(DEFAULT_CHART_URL))
            .andExpect(jsonPath("$.groupChartId").value(DEFAULT_GROUP_CHART_ID.intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.groupKpiCode").value(DEFAULT_GROUP_KPI_CODE))
            .andExpect(jsonPath("$.domainCode").value(DEFAULT_DOMAIN_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigChart() throws Exception {
        // Get the configChart
        restConfigChartMockMvc.perform(get("/api/config-charts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigChart() throws Exception {
        // Initialize the database
        configChartRepository.saveAndFlush(configChart);

        int databaseSizeBeforeUpdate = configChartRepository.findAll().size();

        // Update the configChart
        ConfigChart updatedConfigChart = configChartRepository.findById(configChart.getId()).get();
        // Disconnect from session so that the updates on updatedConfigChart are not directly saved in db
        em.detach(updatedConfigChart);
        updatedConfigChart
            .chartCode(UPDATED_CHART_CODE)
            .chartName(UPDATED_CHART_NAME)
            .titleChart(UPDATED_TITLE_CHART)
            .typeChart(UPDATED_TYPE_CHART)
            .timeTypeDefault(UPDATED_TIME_TYPE_DEFAULT)
            .relativeTime(UPDATED_RELATIVE_TIME)
            .chartUrl(UPDATED_CHART_URL)
            .groupChartId(UPDATED_GROUP_CHART_ID)
            .orderIndex(UPDATED_ORDER_INDEX)
            .groupKpiCode(UPDATED_GROUP_KPI_CODE)
            .domainCode(UPDATED_DOMAIN_CODE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigChartDTO configChartDTO = configChartMapper.toDto(updatedConfigChart);

        restConfigChartMockMvc.perform(put("/api/config-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigChart in the database
        List<ConfigChart> configChartList = configChartRepository.findAll();
        assertThat(configChartList).hasSize(databaseSizeBeforeUpdate);
        ConfigChart testConfigChart = configChartList.get(configChartList.size() - 1);
        assertThat(testConfigChart.getChartCode()).isEqualTo(UPDATED_CHART_CODE);
        assertThat(testConfigChart.getChartName()).isEqualTo(UPDATED_CHART_NAME);
        assertThat(testConfigChart.getTitleChart()).isEqualTo(UPDATED_TITLE_CHART);
        assertThat(testConfigChart.getTypeChart()).isEqualTo(UPDATED_TYPE_CHART);
        assertThat(testConfigChart.getTimeTypeDefault()).isEqualTo(UPDATED_TIME_TYPE_DEFAULT);
        assertThat(testConfigChart.getRelativeTime()).isEqualTo(UPDATED_RELATIVE_TIME);
        assertThat(testConfigChart.getChartUrl()).isEqualTo(UPDATED_CHART_URL);
        assertThat(testConfigChart.getGroupChartId()).isEqualTo(UPDATED_GROUP_CHART_ID);
        assertThat(testConfigChart.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigChart.getGroupKpiCode()).isEqualTo(UPDATED_GROUP_KPI_CODE);
        assertThat(testConfigChart.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
        assertThat(testConfigChart.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigChart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigChart.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigChart.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigChart() throws Exception {
        int databaseSizeBeforeUpdate = configChartRepository.findAll().size();

        // Create the ConfigChart
        ConfigChartDTO configChartDTO = configChartMapper.toDto(configChart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigChartMockMvc.perform(put("/api/config-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configChartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigChart in the database
        List<ConfigChart> configChartList = configChartRepository.findAll();
        assertThat(configChartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigChart() throws Exception {
        // Initialize the database
        configChartRepository.saveAndFlush(configChart);

        int databaseSizeBeforeDelete = configChartRepository.findAll().size();

        // Delete the configChart
        restConfigChartMockMvc.perform(delete("/api/config-charts/{id}", configChart.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigChart> configChartList = configChartRepository.findAll();
        assertThat(configChartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
