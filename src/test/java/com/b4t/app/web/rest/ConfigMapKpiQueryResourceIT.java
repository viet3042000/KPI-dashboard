package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigMapKpiQuery;
import com.b4t.app.repository.ConfigMapKpiQueryRepository;
import com.b4t.app.service.ConfigMapKpiQueryService;
import com.b4t.app.service.dto.ConfigMapKpiQueryDTO;
import com.b4t.app.service.mapper.ConfigMapKpiQueryMapper;
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
 * Integration tests for the {@link ConfigMapKpiQueryResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigMapKpiQueryResourceIT {

    private static final Integer DEFAULT_QUERY_KPI_ID = 1;
    private static final Integer UPDATED_QUERY_KPI_ID = 2;

    private static final Integer DEFAULT_KPI_ID = 1;
    private static final Integer UPDATED_KPI_ID = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_DESTINATION = "BBBBBBBBBB";

    @Autowired
    private ConfigMapKpiQueryRepository configMapKpiQueryRepository;

    @Autowired
    private ConfigMapKpiQueryMapper configMapKpiQueryMapper;

    @Autowired
    private ConfigMapKpiQueryService configMapKpiQueryService;

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

    private MockMvc restConfigMapKpiQueryMockMvc;

    private ConfigMapKpiQuery configMapKpiQuery;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMapKpiQueryResource configMapKpiQueryResource = new ConfigMapKpiQueryResource(configMapKpiQueryService);
        this.restConfigMapKpiQueryMockMvc = MockMvcBuilders.standaloneSetup(configMapKpiQueryResource)
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
    public static ConfigMapKpiQuery createEntity(EntityManager em) {
        ConfigMapKpiQuery configMapKpiQuery = new ConfigMapKpiQuery()
            .queryKpiId(DEFAULT_QUERY_KPI_ID)
            .kpiId(DEFAULT_KPI_ID)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER)
            .tableDestination(DEFAULT_TABLE_DESTINATION);
        return configMapKpiQuery;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigMapKpiQuery createUpdatedEntity(EntityManager em) {
        ConfigMapKpiQuery configMapKpiQuery = new ConfigMapKpiQuery()
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .kpiId(UPDATED_KPI_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER)
            .tableDestination(UPDATED_TABLE_DESTINATION);
        return configMapKpiQuery;
    }

    @BeforeEach
    public void initTest() {
        configMapKpiQuery = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigMapKpiQuery() throws Exception {
        int databaseSizeBeforeCreate = configMapKpiQueryRepository.findAll().size();

        // Create the ConfigMapKpiQuery
        ConfigMapKpiQueryDTO configMapKpiQueryDTO = configMapKpiQueryMapper.toDto(configMapKpiQuery);
        restConfigMapKpiQueryMockMvc.perform(post("/api/config-map-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapKpiQueryDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigMapKpiQuery in the database
        List<ConfigMapKpiQuery> configMapKpiQueryList = configMapKpiQueryRepository.findAll();
        assertThat(configMapKpiQueryList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMapKpiQuery testConfigMapKpiQuery = configMapKpiQueryList.get(configMapKpiQueryList.size() - 1);
        assertThat(testConfigMapKpiQuery.getQueryKpiId()).isEqualTo(DEFAULT_QUERY_KPI_ID);
        assertThat(testConfigMapKpiQuery.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testConfigMapKpiQuery.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigMapKpiQuery.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigMapKpiQuery.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigMapKpiQuery.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testConfigMapKpiQuery.getTableDestination()).isEqualTo(DEFAULT_TABLE_DESTINATION);
    }

    @Test
    @Transactional
    public void createConfigMapKpiQueryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMapKpiQueryRepository.findAll().size();

        // Create the ConfigMapKpiQuery with an existing ID
        configMapKpiQuery.setId(1L);
        ConfigMapKpiQueryDTO configMapKpiQueryDTO = configMapKpiQueryMapper.toDto(configMapKpiQuery);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMapKpiQueryMockMvc.perform(post("/api/config-map-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapKpiQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapKpiQuery in the database
        List<ConfigMapKpiQuery> configMapKpiQueryList = configMapKpiQueryRepository.findAll();
        assertThat(configMapKpiQueryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigMapKpiQueries() throws Exception {
        // Initialize the database
        configMapKpiQueryRepository.saveAndFlush(configMapKpiQuery);

        // Get all the configMapKpiQueryList
        restConfigMapKpiQueryMockMvc.perform(get("/api/config-map-kpi-queries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMapKpiQuery.getId().intValue())))
            .andExpect(jsonPath("$.[*].queryKpiId").value(hasItem(DEFAULT_QUERY_KPI_ID)))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)))
            .andExpect(jsonPath("$.[*].tableDestination").value(hasItem(DEFAULT_TABLE_DESTINATION)));
    }
    
    @Test
    @Transactional
    public void getConfigMapKpiQuery() throws Exception {
        // Initialize the database
        configMapKpiQueryRepository.saveAndFlush(configMapKpiQuery);

        // Get the configMapKpiQuery
        restConfigMapKpiQueryMockMvc.perform(get("/api/config-map-kpi-queries/{id}", configMapKpiQuery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configMapKpiQuery.getId().intValue()))
            .andExpect(jsonPath("$.queryKpiId").value(DEFAULT_QUERY_KPI_ID))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER))
            .andExpect(jsonPath("$.tableDestination").value(DEFAULT_TABLE_DESTINATION));
    }

    @Test
    @Transactional
    public void getNonExistingConfigMapKpiQuery() throws Exception {
        // Get the configMapKpiQuery
        restConfigMapKpiQueryMockMvc.perform(get("/api/config-map-kpi-queries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigMapKpiQuery() throws Exception {
        // Initialize the database
        configMapKpiQueryRepository.saveAndFlush(configMapKpiQuery);

        int databaseSizeBeforeUpdate = configMapKpiQueryRepository.findAll().size();

        // Update the configMapKpiQuery
        ConfigMapKpiQuery updatedConfigMapKpiQuery = configMapKpiQueryRepository.findById(configMapKpiQuery.getId()).get();
        // Disconnect from session so that the updates on updatedConfigMapKpiQuery are not directly saved in db
        em.detach(updatedConfigMapKpiQuery);
        updatedConfigMapKpiQuery
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .kpiId(UPDATED_KPI_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER)
            .tableDestination(UPDATED_TABLE_DESTINATION);
        ConfigMapKpiQueryDTO configMapKpiQueryDTO = configMapKpiQueryMapper.toDto(updatedConfigMapKpiQuery);

        restConfigMapKpiQueryMockMvc.perform(put("/api/config-map-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapKpiQueryDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigMapKpiQuery in the database
        List<ConfigMapKpiQuery> configMapKpiQueryList = configMapKpiQueryRepository.findAll();
        assertThat(configMapKpiQueryList).hasSize(databaseSizeBeforeUpdate);
        ConfigMapKpiQuery testConfigMapKpiQuery = configMapKpiQueryList.get(configMapKpiQueryList.size() - 1);
        assertThat(testConfigMapKpiQuery.getQueryKpiId()).isEqualTo(UPDATED_QUERY_KPI_ID);
        assertThat(testConfigMapKpiQuery.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testConfigMapKpiQuery.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigMapKpiQuery.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigMapKpiQuery.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigMapKpiQuery.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testConfigMapKpiQuery.getTableDestination()).isEqualTo(UPDATED_TABLE_DESTINATION);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigMapKpiQuery() throws Exception {
        int databaseSizeBeforeUpdate = configMapKpiQueryRepository.findAll().size();

        // Create the ConfigMapKpiQuery
        ConfigMapKpiQueryDTO configMapKpiQueryDTO = configMapKpiQueryMapper.toDto(configMapKpiQuery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMapKpiQueryMockMvc.perform(put("/api/config-map-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapKpiQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapKpiQuery in the database
        List<ConfigMapKpiQuery> configMapKpiQueryList = configMapKpiQueryRepository.findAll();
        assertThat(configMapKpiQueryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigMapKpiQuery() throws Exception {
        // Initialize the database
        configMapKpiQueryRepository.saveAndFlush(configMapKpiQuery);

        int databaseSizeBeforeDelete = configMapKpiQueryRepository.findAll().size();

        // Delete the configMapKpiQuery
        restConfigMapKpiQueryMockMvc.perform(delete("/api/config-map-kpi-queries/{id}", configMapKpiQuery.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigMapKpiQuery> configMapKpiQueryList = configMapKpiQueryRepository.findAll();
        assertThat(configMapKpiQueryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
