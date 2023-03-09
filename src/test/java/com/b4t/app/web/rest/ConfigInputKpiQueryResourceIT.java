package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigInputKpiQuery;
import com.b4t.app.repository.ConfigInputKpiQueryRepository;
import com.b4t.app.service.ConfigInputKpiQueryService;
import com.b4t.app.service.dto.ConfigInputKpiQueryDTO;
import com.b4t.app.service.mapper.ConfigInputKpiQueryMapper;
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
 * Integration tests for the {@link ConfigInputKpiQueryResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigInputKpiQueryResourceIT {

    private static final Integer DEFAULT_QUERY_KPI_ID = 1;
    private static final Integer UPDATED_QUERY_KPI_ID = 2;

    private static final String DEFAULT_KPI_ID = "AAAAAAAAAA";
    private static final String UPDATED_KPI_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ConfigInputKpiQueryRepository configInputKpiQueryRepository;

    @Autowired
    private ConfigInputKpiQueryMapper configInputKpiQueryMapper;

    @Autowired
    private ConfigInputKpiQueryService configInputKpiQueryService;

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

    private MockMvc restConfigInputKpiQueryMockMvc;

    private ConfigInputKpiQuery configInputKpiQuery;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigInputKpiQueryResource configInputKpiQueryResource = new ConfigInputKpiQueryResource(configInputKpiQueryService);
        this.restConfigInputKpiQueryMockMvc = MockMvcBuilders.standaloneSetup(configInputKpiQueryResource)
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
    public static ConfigInputKpiQuery createEntity(EntityManager em) {
        ConfigInputKpiQuery configInputKpiQuery = new ConfigInputKpiQuery()
            .queryKpiId(DEFAULT_QUERY_KPI_ID)
            .kpiId(DEFAULT_KPI_ID)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME);
        return configInputKpiQuery;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigInputKpiQuery createUpdatedEntity(EntityManager em) {
        ConfigInputKpiQuery configInputKpiQuery = new ConfigInputKpiQuery()
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .kpiId(UPDATED_KPI_ID)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME);
        return configInputKpiQuery;
    }

    @BeforeEach
    public void initTest() {
        configInputKpiQuery = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigInputKpiQuery() throws Exception {
        int databaseSizeBeforeCreate = configInputKpiQueryRepository.findAll().size();

        // Create the ConfigInputKpiQuery
        ConfigInputKpiQueryDTO configInputKpiQueryDTO = configInputKpiQueryMapper.toDto(configInputKpiQuery);
        restConfigInputKpiQueryMockMvc.perform(post("/api/config-input-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configInputKpiQueryDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigInputKpiQuery in the database
        List<ConfigInputKpiQuery> configInputKpiQueryList = configInputKpiQueryRepository.findAll();
        assertThat(configInputKpiQueryList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigInputKpiQuery testConfigInputKpiQuery = configInputKpiQueryList.get(configInputKpiQueryList.size() - 1);
        assertThat(testConfigInputKpiQuery.getQueryKpiId()).isEqualTo(DEFAULT_QUERY_KPI_ID);
        assertThat(testConfigInputKpiQuery.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testConfigInputKpiQuery.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigInputKpiQuery.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createConfigInputKpiQueryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configInputKpiQueryRepository.findAll().size();

        // Create the ConfigInputKpiQuery with an existing ID
        configInputKpiQuery.setId(1L);
        ConfigInputKpiQueryDTO configInputKpiQueryDTO = configInputKpiQueryMapper.toDto(configInputKpiQuery);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigInputKpiQueryMockMvc.perform(post("/api/config-input-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configInputKpiQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigInputKpiQuery in the database
        List<ConfigInputKpiQuery> configInputKpiQueryList = configInputKpiQueryRepository.findAll();
        assertThat(configInputKpiQueryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigInputKpiQueries() throws Exception {
        // Initialize the database
        configInputKpiQueryRepository.saveAndFlush(configInputKpiQuery);

        // Get all the configInputKpiQueryList
        restConfigInputKpiQueryMockMvc.perform(get("/api/config-input-kpi-queries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configInputKpiQuery.getId().intValue())))
            .andExpect(jsonPath("$.[*].queryKpiId").value(hasItem(DEFAULT_QUERY_KPI_ID)))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getConfigInputKpiQuery() throws Exception {
        // Initialize the database
        configInputKpiQueryRepository.saveAndFlush(configInputKpiQuery);

        // Get the configInputKpiQuery
        restConfigInputKpiQueryMockMvc.perform(get("/api/config-input-kpi-queries/{id}", configInputKpiQuery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configInputKpiQuery.getId().intValue()))
            .andExpect(jsonPath("$.queryKpiId").value(DEFAULT_QUERY_KPI_ID))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigInputKpiQuery() throws Exception {
        // Get the configInputKpiQuery
        restConfigInputKpiQueryMockMvc.perform(get("/api/config-input-kpi-queries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigInputKpiQuery() throws Exception {
        // Initialize the database
        configInputKpiQueryRepository.saveAndFlush(configInputKpiQuery);

        int databaseSizeBeforeUpdate = configInputKpiQueryRepository.findAll().size();

        // Update the configInputKpiQuery
        ConfigInputKpiQuery updatedConfigInputKpiQuery = configInputKpiQueryRepository.findById(configInputKpiQuery.getId()).get();
        // Disconnect from session so that the updates on updatedConfigInputKpiQuery are not directly saved in db
        em.detach(updatedConfigInputKpiQuery);
        updatedConfigInputKpiQuery
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .kpiId(UPDATED_KPI_ID)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME);
        ConfigInputKpiQueryDTO configInputKpiQueryDTO = configInputKpiQueryMapper.toDto(updatedConfigInputKpiQuery);

        restConfigInputKpiQueryMockMvc.perform(put("/api/config-input-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configInputKpiQueryDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigInputKpiQuery in the database
        List<ConfigInputKpiQuery> configInputKpiQueryList = configInputKpiQueryRepository.findAll();
        assertThat(configInputKpiQueryList).hasSize(databaseSizeBeforeUpdate);
        ConfigInputKpiQuery testConfigInputKpiQuery = configInputKpiQueryList.get(configInputKpiQueryList.size() - 1);
        assertThat(testConfigInputKpiQuery.getQueryKpiId()).isEqualTo(UPDATED_QUERY_KPI_ID);
        assertThat(testConfigInputKpiQuery.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testConfigInputKpiQuery.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigInputKpiQuery.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigInputKpiQuery() throws Exception {
        int databaseSizeBeforeUpdate = configInputKpiQueryRepository.findAll().size();

        // Create the ConfigInputKpiQuery
        ConfigInputKpiQueryDTO configInputKpiQueryDTO = configInputKpiQueryMapper.toDto(configInputKpiQuery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigInputKpiQueryMockMvc.perform(put("/api/config-input-kpi-queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configInputKpiQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigInputKpiQuery in the database
        List<ConfigInputKpiQuery> configInputKpiQueryList = configInputKpiQueryRepository.findAll();
        assertThat(configInputKpiQueryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigInputKpiQuery() throws Exception {
        // Initialize the database
        configInputKpiQueryRepository.saveAndFlush(configInputKpiQuery);

        int databaseSizeBeforeDelete = configInputKpiQueryRepository.findAll().size();

        // Delete the configInputKpiQuery
        restConfigInputKpiQueryMockMvc.perform(delete("/api/config-input-kpi-queries/{id}", configInputKpiQuery.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigInputKpiQuery> configInputKpiQueryList = configInputKpiQueryRepository.findAll();
        assertThat(configInputKpiQueryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
