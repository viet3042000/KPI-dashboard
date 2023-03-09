package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigColumnQueryKpi;
import com.b4t.app.repository.ConfigColumnQueryKpiRepository;
import com.b4t.app.service.ConfigColumnQueryKpiService;
import com.b4t.app.service.dto.ConfigColumnQueryKpiDTO;
import com.b4t.app.service.mapper.ConfigColumnQueryKpiMapper;
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
 * Integration tests for the {@link ConfigColumnQueryKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigColumnQueryKpiResourceIT {

    private static final Long DEFAULT_MAP_KPI_QUERY_ID = 1L;
    private static final Long UPDATED_MAP_KPI_QUERY_ID = 2L;

    private static final String DEFAULT_COLUMN_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMN_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_DESTINATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigColumnQueryKpiRepository configColumnQueryKpiRepository;

    @Autowired
    private ConfigColumnQueryKpiMapper configColumnQueryKpiMapper;

    @Autowired
    private ConfigColumnQueryKpiService configColumnQueryKpiService;

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

    private MockMvc restConfigColumnQueryKpiMockMvc;

    private ConfigColumnQueryKpi configColumnQueryKpi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigColumnQueryKpiResource configColumnQueryKpiResource = new ConfigColumnQueryKpiResource(configColumnQueryKpiService);
        this.restConfigColumnQueryKpiMockMvc = MockMvcBuilders.standaloneSetup(configColumnQueryKpiResource)
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
    public static ConfigColumnQueryKpi createEntity(EntityManager em) {
        ConfigColumnQueryKpi configColumnQueryKpi = new ConfigColumnQueryKpi()
            .mapKpiQueryId(DEFAULT_MAP_KPI_QUERY_ID)
            .columnQuery(DEFAULT_COLUMN_QUERY)
            .dataType(DEFAULT_DATA_TYPE)
            .columnDestination(DEFAULT_COLUMN_DESTINATION)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configColumnQueryKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigColumnQueryKpi createUpdatedEntity(EntityManager em) {
        ConfigColumnQueryKpi configColumnQueryKpi = new ConfigColumnQueryKpi()
            .mapKpiQueryId(UPDATED_MAP_KPI_QUERY_ID)
            .columnQuery(UPDATED_COLUMN_QUERY)
            .dataType(UPDATED_DATA_TYPE)
            .columnDestination(UPDATED_COLUMN_DESTINATION)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configColumnQueryKpi;
    }

    @BeforeEach
    public void initTest() {
        configColumnQueryKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigColumnQueryKpi() throws Exception {
        int databaseSizeBeforeCreate = configColumnQueryKpiRepository.findAll().size();

        // Create the ConfigColumnQueryKpi
        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO = configColumnQueryKpiMapper.toDto(configColumnQueryKpi);
        restConfigColumnQueryKpiMockMvc.perform(post("/api/config-column-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configColumnQueryKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigColumnQueryKpi in the database
        List<ConfigColumnQueryKpi> configColumnQueryKpiList = configColumnQueryKpiRepository.findAll();
        assertThat(configColumnQueryKpiList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigColumnQueryKpi testConfigColumnQueryKpi = configColumnQueryKpiList.get(configColumnQueryKpiList.size() - 1);
        assertThat(testConfigColumnQueryKpi.getMapKpiQueryId()).isEqualTo(DEFAULT_MAP_KPI_QUERY_ID);
        assertThat(testConfigColumnQueryKpi.getColumnQuery()).isEqualTo(DEFAULT_COLUMN_QUERY);
        assertThat(testConfigColumnQueryKpi.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testConfigColumnQueryKpi.getColumnDestination()).isEqualTo(DEFAULT_COLUMN_DESTINATION);
        assertThat(testConfigColumnQueryKpi.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigColumnQueryKpi.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigColumnQueryKpi.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigColumnQueryKpi.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigColumnQueryKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configColumnQueryKpiRepository.findAll().size();

        // Create the ConfigColumnQueryKpi with an existing ID
        configColumnQueryKpi.setId(1L);
        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO = configColumnQueryKpiMapper.toDto(configColumnQueryKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigColumnQueryKpiMockMvc.perform(post("/api/config-column-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configColumnQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigColumnQueryKpi in the database
        List<ConfigColumnQueryKpi> configColumnQueryKpiList = configColumnQueryKpiRepository.findAll();
        assertThat(configColumnQueryKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigColumnQueryKpis() throws Exception {
        // Initialize the database
        configColumnQueryKpiRepository.saveAndFlush(configColumnQueryKpi);

        // Get all the configColumnQueryKpiList
        restConfigColumnQueryKpiMockMvc.perform(get("/api/config-column-query-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configColumnQueryKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].mapKpiQueryId").value(hasItem(DEFAULT_MAP_KPI_QUERY_ID.intValue())))
            .andExpect(jsonPath("$.[*].columnQuery").value(hasItem(DEFAULT_COLUMN_QUERY)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].columnDestination").value(hasItem(DEFAULT_COLUMN_DESTINATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }
    
    @Test
    @Transactional
    public void getConfigColumnQueryKpi() throws Exception {
        // Initialize the database
        configColumnQueryKpiRepository.saveAndFlush(configColumnQueryKpi);

        // Get the configColumnQueryKpi
        restConfigColumnQueryKpiMockMvc.perform(get("/api/config-column-query-kpis/{id}", configColumnQueryKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configColumnQueryKpi.getId().intValue()))
            .andExpect(jsonPath("$.mapKpiQueryId").value(DEFAULT_MAP_KPI_QUERY_ID.intValue()))
            .andExpect(jsonPath("$.columnQuery").value(DEFAULT_COLUMN_QUERY))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.columnDestination").value(DEFAULT_COLUMN_DESTINATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigColumnQueryKpi() throws Exception {
        // Get the configColumnQueryKpi
        restConfigColumnQueryKpiMockMvc.perform(get("/api/config-column-query-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigColumnQueryKpi() throws Exception {
        // Initialize the database
        configColumnQueryKpiRepository.saveAndFlush(configColumnQueryKpi);

        int databaseSizeBeforeUpdate = configColumnQueryKpiRepository.findAll().size();

        // Update the configColumnQueryKpi
        ConfigColumnQueryKpi updatedConfigColumnQueryKpi = configColumnQueryKpiRepository.findById(configColumnQueryKpi.getId()).get();
        // Disconnect from session so that the updates on updatedConfigColumnQueryKpi are not directly saved in db
        em.detach(updatedConfigColumnQueryKpi);
        updatedConfigColumnQueryKpi
            .mapKpiQueryId(UPDATED_MAP_KPI_QUERY_ID)
            .columnQuery(UPDATED_COLUMN_QUERY)
            .dataType(UPDATED_DATA_TYPE)
            .columnDestination(UPDATED_COLUMN_DESTINATION)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO = configColumnQueryKpiMapper.toDto(updatedConfigColumnQueryKpi);

        restConfigColumnQueryKpiMockMvc.perform(put("/api/config-column-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configColumnQueryKpiDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigColumnQueryKpi in the database
        List<ConfigColumnQueryKpi> configColumnQueryKpiList = configColumnQueryKpiRepository.findAll();
        assertThat(configColumnQueryKpiList).hasSize(databaseSizeBeforeUpdate);
        ConfigColumnQueryKpi testConfigColumnQueryKpi = configColumnQueryKpiList.get(configColumnQueryKpiList.size() - 1);
        assertThat(testConfigColumnQueryKpi.getMapKpiQueryId()).isEqualTo(UPDATED_MAP_KPI_QUERY_ID);
        assertThat(testConfigColumnQueryKpi.getColumnQuery()).isEqualTo(UPDATED_COLUMN_QUERY);
        assertThat(testConfigColumnQueryKpi.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testConfigColumnQueryKpi.getColumnDestination()).isEqualTo(UPDATED_COLUMN_DESTINATION);
        assertThat(testConfigColumnQueryKpi.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigColumnQueryKpi.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigColumnQueryKpi.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigColumnQueryKpi.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigColumnQueryKpi() throws Exception {
        int databaseSizeBeforeUpdate = configColumnQueryKpiRepository.findAll().size();

        // Create the ConfigColumnQueryKpi
        ConfigColumnQueryKpiDTO configColumnQueryKpiDTO = configColumnQueryKpiMapper.toDto(configColumnQueryKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigColumnQueryKpiMockMvc.perform(put("/api/config-column-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configColumnQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigColumnQueryKpi in the database
        List<ConfigColumnQueryKpi> configColumnQueryKpiList = configColumnQueryKpiRepository.findAll();
        assertThat(configColumnQueryKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigColumnQueryKpi() throws Exception {
        // Initialize the database
        configColumnQueryKpiRepository.saveAndFlush(configColumnQueryKpi);

        int databaseSizeBeforeDelete = configColumnQueryKpiRepository.findAll().size();

        // Delete the configColumnQueryKpi
        restConfigColumnQueryKpiMockMvc.perform(delete("/api/config-column-query-kpis/{id}", configColumnQueryKpi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigColumnQueryKpi> configColumnQueryKpiList = configColumnQueryKpiRepository.findAll();
        assertThat(configColumnQueryKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
