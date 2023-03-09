package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigQueryChart;
import com.b4t.app.repository.ConfigQueryChartRepository;
import com.b4t.app.service.ConfigQueryChartService;
import com.b4t.app.service.dto.ConfigQueryChartDTO;
import com.b4t.app.service.mapper.ConfigQueryChartMapper;
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
 * Integration tests for the {@link ConfigQueryChartResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigQueryChartResourceIT {

    private static final String DEFAULT_QUERY_DATA = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UP_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UP_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UP_DATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UP_DATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigQueryChartRepository configQueryChartRepository;

    @Autowired
    private ConfigQueryChartMapper configQueryChartMapper;

    @Autowired
    private ConfigQueryChartService configQueryChartService;

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

    private MockMvc restConfigQueryChartMockMvc;

    private ConfigQueryChart configQueryChart;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigQueryChartResource configQueryChartResource = new ConfigQueryChartResource(configQueryChartService);
        this.restConfigQueryChartMockMvc = MockMvcBuilders.standaloneSetup(configQueryChartResource)
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
    public static ConfigQueryChart createEntity(EntityManager em) {
        ConfigQueryChart configQueryChart = new ConfigQueryChart()
            .queryData(DEFAULT_QUERY_DATA)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UP_DATE_TIME)
            .updateUser(DEFAULT_UP_DATE_USER);
        return configQueryChart;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigQueryChart createUpdatedEntity(EntityManager em) {
        ConfigQueryChart configQueryChart = new ConfigQueryChart()
            .queryData(UPDATED_QUERY_DATA)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UP_DATE_TIME)
            .updateUser(UPDATED_UP_DATE_USER);
        return configQueryChart;
    }

    @BeforeEach
    public void initTest() {
        configQueryChart = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigQueryChart() throws Exception {
        int databaseSizeBeforeCreate = configQueryChartRepository.findAll().size();

        // Create the ConfigQueryChart
        ConfigQueryChartDTO configQueryChartDTO = configQueryChartMapper.toDto(configQueryChart);
        restConfigQueryChartMockMvc.perform(post("/api/config-query-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configQueryChartDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigQueryChart in the database
        List<ConfigQueryChart> configQueryChartList = configQueryChartRepository.findAll();
        assertThat(configQueryChartList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigQueryChart testConfigQueryChart = configQueryChartList.get(configQueryChartList.size() - 1);
        assertThat(testConfigQueryChart.getQueryData()).isEqualTo(DEFAULT_QUERY_DATA);
        assertThat(testConfigQueryChart.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
        assertThat(testConfigQueryChart.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigQueryChart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigQueryChart.getUpdateTime()).isEqualTo(DEFAULT_UP_DATE_TIME);
        assertThat(testConfigQueryChart.getUpdateUser()).isEqualTo(DEFAULT_UP_DATE_USER);
    }

    @Test
    @Transactional
    public void createConfigQueryChartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configQueryChartRepository.findAll().size();

        // Create the ConfigQueryChart with an existing ID
        configQueryChart.setId(1L);
        ConfigQueryChartDTO configQueryChartDTO = configQueryChartMapper.toDto(configQueryChart);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigQueryChartMockMvc.perform(post("/api/config-query-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configQueryChartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigQueryChart in the database
        List<ConfigQueryChart> configQueryChartList = configQueryChartRepository.findAll();
        assertThat(configQueryChartList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigQueryCharts() throws Exception {
        // Initialize the database
        configQueryChartRepository.saveAndFlush(configQueryChart);

        // Get all the configQueryChartList
        restConfigQueryChartMockMvc.perform(get("/api/config-query-charts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configQueryChart.getId().intValue())))
            .andExpect(jsonPath("$.[*].queryData").value(hasItem(DEFAULT_QUERY_DATA)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].upDateTime").value(hasItem(DEFAULT_UP_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].upDateUser").value(hasItem(DEFAULT_UP_DATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigQueryChart() throws Exception {
        // Initialize the database
        configQueryChartRepository.saveAndFlush(configQueryChart);

        // Get the configQueryChart
        restConfigQueryChartMockMvc.perform(get("/api/config-query-charts/{id}", configQueryChart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configQueryChart.getId().intValue()))
            .andExpect(jsonPath("$.queryData").value(DEFAULT_QUERY_DATA))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.upDateTime").value(DEFAULT_UP_DATE_TIME.toString()))
            .andExpect(jsonPath("$.upDateUser").value(DEFAULT_UP_DATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigQueryChart() throws Exception {
        // Get the configQueryChart
        restConfigQueryChartMockMvc.perform(get("/api/config-query-charts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigQueryChart() throws Exception {
        // Initialize the database
        configQueryChartRepository.saveAndFlush(configQueryChart);

        int databaseSizeBeforeUpdate = configQueryChartRepository.findAll().size();

        // Update the configQueryChart
        ConfigQueryChart updatedConfigQueryChart = configQueryChartRepository.findById(configQueryChart.getId()).get();
        // Disconnect from session so that the updates on updatedConfigQueryChart are not directly saved in db
        em.detach(updatedConfigQueryChart);
        updatedConfigQueryChart
            .queryData(UPDATED_QUERY_DATA)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UP_DATE_TIME)
            .updateUser(UPDATED_UP_DATE_USER);
        ConfigQueryChartDTO configQueryChartDTO = configQueryChartMapper.toDto(updatedConfigQueryChart);

        restConfigQueryChartMockMvc.perform(put("/api/config-query-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configQueryChartDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigQueryChart in the database
        List<ConfigQueryChart> configQueryChartList = configQueryChartRepository.findAll();
        assertThat(configQueryChartList).hasSize(databaseSizeBeforeUpdate);
        ConfigQueryChart testConfigQueryChart = configQueryChartList.get(configQueryChartList.size() - 1);
        assertThat(testConfigQueryChart.getQueryData()).isEqualTo(UPDATED_QUERY_DATA);
        assertThat(testConfigQueryChart.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testConfigQueryChart.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigQueryChart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigQueryChart.getUpdateTime()).isEqualTo(UPDATED_UP_DATE_TIME);
        assertThat(testConfigQueryChart.getUpdateUser()).isEqualTo(UPDATED_UP_DATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigQueryChart() throws Exception {
        int databaseSizeBeforeUpdate = configQueryChartRepository.findAll().size();

        // Create the ConfigQueryChart
        ConfigQueryChartDTO configQueryChartDTO = configQueryChartMapper.toDto(configQueryChart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigQueryChartMockMvc.perform(put("/api/config-query-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configQueryChartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigQueryChart in the database
        List<ConfigQueryChart> configQueryChartList = configQueryChartRepository.findAll();
        assertThat(configQueryChartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigQueryChart() throws Exception {
        // Initialize the database
        configQueryChartRepository.saveAndFlush(configQueryChart);

        int databaseSizeBeforeDelete = configQueryChartRepository.findAll().size();

        // Delete the configQueryChart
        restConfigQueryChartMockMvc.perform(delete("/api/config-query-charts/{id}", configQueryChart.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigQueryChart> configQueryChartList = configQueryChartRepository.findAll();
        assertThat(configQueryChartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
