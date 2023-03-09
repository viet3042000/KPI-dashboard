package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigDisplayQuery;
import com.b4t.app.repository.ConfigDisplayQueryRepository;
import com.b4t.app.service.ConfigDisplayQueryService;
import com.b4t.app.service.dto.ConfigDisplayQueryDTO;
import com.b4t.app.service.mapper.ConfigDisplayQueryMapper;
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
 * Integration tests for the {@link ConfigDisplayQueryResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigDisplayQueryResourceIT {

    private static final Long DEFAULT_ITEM_CHART_ID = 1L;
    private static final Long UPDATED_ITEM_CHART_ID = 2L;

    private static final String DEFAULT_COLUMN_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMN_CHART = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_CHART = "BBBBBBBBBB";

    private static final Long DEFAULT_IS_REQUIRE = 1L;
    private static final Long UPDATED_IS_REQUIRE = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigDisplayQueryRepository configDisplayQueryRepository;

    @Autowired
    private ConfigDisplayQueryMapper configDisplayQueryMapper;

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

    private MockMvc restConfigDisplayQueryMockMvc;

    private ConfigDisplayQuery configDisplayQuery;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigDisplayQueryResource configDisplayQueryResource = new ConfigDisplayQueryResource(configDisplayQueryService);
        this.restConfigDisplayQueryMockMvc = MockMvcBuilders.standaloneSetup(configDisplayQueryResource)
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
    public static ConfigDisplayQuery createEntity(EntityManager em) {
        ConfigDisplayQuery configDisplayQuery = new ConfigDisplayQuery()
            .itemChartId(DEFAULT_ITEM_CHART_ID)
            .columnQuery(DEFAULT_COLUMN_QUERY)
            .dataType(DEFAULT_DATA_TYPE)
            .columnChart(DEFAULT_COLUMN_CHART)
            .isRequire(DEFAULT_IS_REQUIRE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configDisplayQuery;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigDisplayQuery createUpdatedEntity(EntityManager em) {
        ConfigDisplayQuery configDisplayQuery = new ConfigDisplayQuery()
            .itemChartId(UPDATED_ITEM_CHART_ID)
            .columnQuery(UPDATED_COLUMN_QUERY)
            .dataType(UPDATED_DATA_TYPE)
            .columnChart(UPDATED_COLUMN_CHART)
            .isRequire(UPDATED_IS_REQUIRE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configDisplayQuery;
    }

    @BeforeEach
    public void initTest() {
        configDisplayQuery = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigDisplayQuery() throws Exception {
        int databaseSizeBeforeCreate = configDisplayQueryRepository.findAll().size();

        // Create the ConfigDisplayQuery
        ConfigDisplayQueryDTO configDisplayQueryDTO = configDisplayQueryMapper.toDto(configDisplayQuery);
        restConfigDisplayQueryMockMvc.perform(post("/api/config-display-queries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configDisplayQueryDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigDisplayQuery in the database
        List<ConfigDisplayQuery> configDisplayQueryList = configDisplayQueryRepository.findAll();
        assertThat(configDisplayQueryList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigDisplayQuery testConfigDisplayQuery = configDisplayQueryList.get(configDisplayQueryList.size() - 1);
        assertThat(testConfigDisplayQuery.getItemChartId()).isEqualTo(DEFAULT_ITEM_CHART_ID);
        assertThat(testConfigDisplayQuery.getColumnQuery()).isEqualTo(DEFAULT_COLUMN_QUERY);
        assertThat(testConfigDisplayQuery.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testConfigDisplayQuery.getColumnChart()).isEqualTo(DEFAULT_COLUMN_CHART);
        assertThat(testConfigDisplayQuery.getIsRequire()).isEqualTo(DEFAULT_IS_REQUIRE);
        assertThat(testConfigDisplayQuery.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigDisplayQuery.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigDisplayQuery.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigDisplayQuery.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigDisplayQueryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configDisplayQueryRepository.findAll().size();

        // Create the ConfigDisplayQuery with an existing ID
        configDisplayQuery.setId(1L);
        ConfigDisplayQueryDTO configDisplayQueryDTO = configDisplayQueryMapper.toDto(configDisplayQuery);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigDisplayQueryMockMvc.perform(post("/api/config-display-queries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configDisplayQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigDisplayQuery in the database
        List<ConfigDisplayQuery> configDisplayQueryList = configDisplayQueryRepository.findAll();
        assertThat(configDisplayQueryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigDisplayQueries() throws Exception {
        // Initialize the database
        configDisplayQueryRepository.saveAndFlush(configDisplayQuery);

        // Get all the configDisplayQueryList
        restConfigDisplayQueryMockMvc.perform(get("/api/config-display-queries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configDisplayQuery.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemChartId").value(hasItem(DEFAULT_ITEM_CHART_ID.intValue())))
            .andExpect(jsonPath("$.[*].columnQuery").value(hasItem(DEFAULT_COLUMN_QUERY)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].columnChart").value(hasItem(DEFAULT_COLUMN_CHART)))
            .andExpect(jsonPath("$.[*].isRequire").value(hasItem(DEFAULT_IS_REQUIRE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigDisplayQuery() throws Exception {
        // Initialize the database
        configDisplayQueryRepository.saveAndFlush(configDisplayQuery);

        // Get the configDisplayQuery
        restConfigDisplayQueryMockMvc.perform(get("/api/config-display-queries/{id}", configDisplayQuery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configDisplayQuery.getId().intValue()))
            .andExpect(jsonPath("$.itemChartId").value(DEFAULT_ITEM_CHART_ID.intValue()))
            .andExpect(jsonPath("$.columnQuery").value(DEFAULT_COLUMN_QUERY))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.columnChart").value(DEFAULT_COLUMN_CHART))
            .andExpect(jsonPath("$.isRequire").value(DEFAULT_IS_REQUIRE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigDisplayQuery() throws Exception {
        // Get the configDisplayQuery
        restConfigDisplayQueryMockMvc.perform(get("/api/config-display-queries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigDisplayQuery() throws Exception {
        // Initialize the database
        configDisplayQueryRepository.saveAndFlush(configDisplayQuery);

        int databaseSizeBeforeUpdate = configDisplayQueryRepository.findAll().size();

        // Update the configDisplayQuery
        ConfigDisplayQuery updatedConfigDisplayQuery = configDisplayQueryRepository.findById(configDisplayQuery.getId()).get();
        // Disconnect from session so that the updates on updatedConfigDisplayQuery are not directly saved in db
        em.detach(updatedConfigDisplayQuery);
        updatedConfigDisplayQuery
            .itemChartId(UPDATED_ITEM_CHART_ID)
            .columnQuery(UPDATED_COLUMN_QUERY)
            .dataType(UPDATED_DATA_TYPE)
            .columnChart(UPDATED_COLUMN_CHART)
            .isRequire(UPDATED_IS_REQUIRE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigDisplayQueryDTO configDisplayQueryDTO = configDisplayQueryMapper.toDto(updatedConfigDisplayQuery);

        restConfigDisplayQueryMockMvc.perform(put("/api/config-display-queries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configDisplayQueryDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigDisplayQuery in the database
        List<ConfigDisplayQuery> configDisplayQueryList = configDisplayQueryRepository.findAll();
        assertThat(configDisplayQueryList).hasSize(databaseSizeBeforeUpdate);
        ConfigDisplayQuery testConfigDisplayQuery = configDisplayQueryList.get(configDisplayQueryList.size() - 1);
        assertThat(testConfigDisplayQuery.getItemChartId()).isEqualTo(UPDATED_ITEM_CHART_ID);
        assertThat(testConfigDisplayQuery.getColumnQuery()).isEqualTo(UPDATED_COLUMN_QUERY);
        assertThat(testConfigDisplayQuery.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testConfigDisplayQuery.getColumnChart()).isEqualTo(UPDATED_COLUMN_CHART);
        assertThat(testConfigDisplayQuery.getIsRequire()).isEqualTo(UPDATED_IS_REQUIRE);
        assertThat(testConfigDisplayQuery.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigDisplayQuery.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigDisplayQuery.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigDisplayQuery.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigDisplayQuery() throws Exception {
        int databaseSizeBeforeUpdate = configDisplayQueryRepository.findAll().size();

        // Create the ConfigDisplayQuery
        ConfigDisplayQueryDTO configDisplayQueryDTO = configDisplayQueryMapper.toDto(configDisplayQuery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigDisplayQueryMockMvc.perform(put("/api/config-display-queries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configDisplayQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigDisplayQuery in the database
        List<ConfigDisplayQuery> configDisplayQueryList = configDisplayQueryRepository.findAll();
        assertThat(configDisplayQueryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigDisplayQuery() throws Exception {
        // Initialize the database
        configDisplayQueryRepository.saveAndFlush(configDisplayQuery);

        int databaseSizeBeforeDelete = configDisplayQueryRepository.findAll().size();

        // Delete the configDisplayQuery
        restConfigDisplayQueryMockMvc.perform(delete("/api/config-display-queries/{id}", configDisplayQuery.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigDisplayQuery> configDisplayQueryList = configDisplayQueryRepository.findAll();
        assertThat(configDisplayQueryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
