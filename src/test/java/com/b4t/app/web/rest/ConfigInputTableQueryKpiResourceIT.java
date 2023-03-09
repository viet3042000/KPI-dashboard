package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigInputTableQueryKpi;
import com.b4t.app.repository.ConfigInputTableQueryKpiRepository;
import com.b4t.app.service.ConfigInputTableQueryKpiService;
import com.b4t.app.service.dto.ConfigInputTableQueryKpiDTO;
import com.b4t.app.service.mapper.ConfigInputTableQueryKpiMapper;
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
 * Integration tests for the {@link ConfigInputTableQueryKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigInputTableQueryKpiResourceIT {

    private static final Integer DEFAULT_QUERY_KPI_ID = 1;
    private static final Integer UPDATED_QUERY_KPI_ID = 2;

    private static final String DEFAULT_TABLE_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_SOURCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ConfigInputTableQueryKpiRepository configInputTableQueryKpiRepository;

    @Autowired
    private ConfigInputTableQueryKpiMapper configInputTableQueryKpiMapper;

    @Autowired
    private ConfigInputTableQueryKpiService configInputTableQueryKpiService;

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

    private MockMvc restConfigInputTableQueryKpiMockMvc;

    private ConfigInputTableQueryKpi configInputTableQueryKpi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigInputTableQueryKpiResource configInputTableQueryKpiResource = new ConfigInputTableQueryKpiResource(configInputTableQueryKpiService);
        this.restConfigInputTableQueryKpiMockMvc = MockMvcBuilders.standaloneSetup(configInputTableQueryKpiResource)
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
    public static ConfigInputTableQueryKpi createEntity(EntityManager em) {
        ConfigInputTableQueryKpi configInputTableQueryKpi = new ConfigInputTableQueryKpi()
            .queryKpiId(DEFAULT_QUERY_KPI_ID)
            .tableSource(DEFAULT_TABLE_SOURCE)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME);
        return configInputTableQueryKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigInputTableQueryKpi createUpdatedEntity(EntityManager em) {
        ConfigInputTableQueryKpi configInputTableQueryKpi = new ConfigInputTableQueryKpi()
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .tableSource(UPDATED_TABLE_SOURCE)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME);
        return configInputTableQueryKpi;
    }

    @BeforeEach
    public void initTest() {
        configInputTableQueryKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigInputTableQueryKpi() throws Exception {
        int databaseSizeBeforeCreate = configInputTableQueryKpiRepository.findAll().size();

        // Create the ConfigInputTableQueryKpi
        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO = configInputTableQueryKpiMapper.toDto(configInputTableQueryKpi);
        restConfigInputTableQueryKpiMockMvc.perform(post("/api/config-input-table-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configInputTableQueryKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigInputTableQueryKpi in the database
        List<ConfigInputTableQueryKpi> configInputTableQueryKpiList = configInputTableQueryKpiRepository.findAll();
        assertThat(configInputTableQueryKpiList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigInputTableQueryKpi testConfigInputTableQueryKpi = configInputTableQueryKpiList.get(configInputTableQueryKpiList.size() - 1);
        assertThat(testConfigInputTableQueryKpi.getQueryKpiId()).isEqualTo(DEFAULT_QUERY_KPI_ID);
        assertThat(testConfigInputTableQueryKpi.getTableSource()).isEqualTo(DEFAULT_TABLE_SOURCE);
        assertThat(testConfigInputTableQueryKpi.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigInputTableQueryKpi.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createConfigInputTableQueryKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configInputTableQueryKpiRepository.findAll().size();

        // Create the ConfigInputTableQueryKpi with an existing ID
        configInputTableQueryKpi.setId(1L);
        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO = configInputTableQueryKpiMapper.toDto(configInputTableQueryKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigInputTableQueryKpiMockMvc.perform(post("/api/config-input-table-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configInputTableQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigInputTableQueryKpi in the database
        List<ConfigInputTableQueryKpi> configInputTableQueryKpiList = configInputTableQueryKpiRepository.findAll();
        assertThat(configInputTableQueryKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigInputTableQueryKpis() throws Exception {
        // Initialize the database
        configInputTableQueryKpiRepository.saveAndFlush(configInputTableQueryKpi);

        // Get all the configInputTableQueryKpiList
        restConfigInputTableQueryKpiMockMvc.perform(get("/api/config-input-table-query-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configInputTableQueryKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].queryKpiId").value(hasItem(DEFAULT_QUERY_KPI_ID)))
            .andExpect(jsonPath("$.[*].tableSource").value(hasItem(DEFAULT_TABLE_SOURCE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getConfigInputTableQueryKpi() throws Exception {
        // Initialize the database
        configInputTableQueryKpiRepository.saveAndFlush(configInputTableQueryKpi);

        // Get the configInputTableQueryKpi
        restConfigInputTableQueryKpiMockMvc.perform(get("/api/config-input-table-query-kpis/{id}", configInputTableQueryKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configInputTableQueryKpi.getId().intValue()))
            .andExpect(jsonPath("$.queryKpiId").value(DEFAULT_QUERY_KPI_ID))
            .andExpect(jsonPath("$.tableSource").value(DEFAULT_TABLE_SOURCE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigInputTableQueryKpi() throws Exception {
        // Get the configInputTableQueryKpi
        restConfigInputTableQueryKpiMockMvc.perform(get("/api/config-input-table-query-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigInputTableQueryKpi() throws Exception {
        // Initialize the database
        configInputTableQueryKpiRepository.saveAndFlush(configInputTableQueryKpi);

        int databaseSizeBeforeUpdate = configInputTableQueryKpiRepository.findAll().size();

        // Update the configInputTableQueryKpi
        ConfigInputTableQueryKpi updatedConfigInputTableQueryKpi = configInputTableQueryKpiRepository.findById(configInputTableQueryKpi.getId()).get();
        // Disconnect from session so that the updates on updatedConfigInputTableQueryKpi are not directly saved in db
        em.detach(updatedConfigInputTableQueryKpi);
        updatedConfigInputTableQueryKpi
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .tableSource(UPDATED_TABLE_SOURCE)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME);
        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO = configInputTableQueryKpiMapper.toDto(updatedConfigInputTableQueryKpi);

        restConfigInputTableQueryKpiMockMvc.perform(put("/api/config-input-table-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configInputTableQueryKpiDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigInputTableQueryKpi in the database
        List<ConfigInputTableQueryKpi> configInputTableQueryKpiList = configInputTableQueryKpiRepository.findAll();
        assertThat(configInputTableQueryKpiList).hasSize(databaseSizeBeforeUpdate);
        ConfigInputTableQueryKpi testConfigInputTableQueryKpi = configInputTableQueryKpiList.get(configInputTableQueryKpiList.size() - 1);
        assertThat(testConfigInputTableQueryKpi.getQueryKpiId()).isEqualTo(UPDATED_QUERY_KPI_ID);
        assertThat(testConfigInputTableQueryKpi.getTableSource()).isEqualTo(UPDATED_TABLE_SOURCE);
        assertThat(testConfigInputTableQueryKpi.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigInputTableQueryKpi.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigInputTableQueryKpi() throws Exception {
        int databaseSizeBeforeUpdate = configInputTableQueryKpiRepository.findAll().size();

        // Create the ConfigInputTableQueryKpi
        ConfigInputTableQueryKpiDTO configInputTableQueryKpiDTO = configInputTableQueryKpiMapper.toDto(configInputTableQueryKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigInputTableQueryKpiMockMvc.perform(put("/api/config-input-table-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configInputTableQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigInputTableQueryKpi in the database
        List<ConfigInputTableQueryKpi> configInputTableQueryKpiList = configInputTableQueryKpiRepository.findAll();
        assertThat(configInputTableQueryKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigInputTableQueryKpi() throws Exception {
        // Initialize the database
        configInputTableQueryKpiRepository.saveAndFlush(configInputTableQueryKpi);

        int databaseSizeBeforeDelete = configInputTableQueryKpiRepository.findAll().size();

        // Delete the configInputTableQueryKpi
        restConfigInputTableQueryKpiMockMvc.perform(delete("/api/config-input-table-query-kpis/{id}", configInputTableQueryKpi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigInputTableQueryKpi> configInputTableQueryKpiList = configInputTableQueryKpiRepository.findAll();
        assertThat(configInputTableQueryKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
