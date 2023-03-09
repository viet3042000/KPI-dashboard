package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigQueryKpi;
import com.b4t.app.repository.ConfigQueryKpiRepository;
import com.b4t.app.service.ConfigQueryKpiService;
import com.b4t.app.service.dto.ConfigQueryKpiDTO;
import com.b4t.app.service.mapper.ConfigQueryKpiMapper;
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
 * Integration tests for the {@link ConfigQueryKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigQueryKpiResourceIT {

    private static final Integer DEFAULT_TIME_TYPE = 1;
    private static final Integer UPDATED_TIME_TYPE = 2;

    private static final Long DEFAULT_INPUT_LEVEL = 1L;
    private static final Long UPDATED_INPUT_LEVEL = 2L;

    private static final String DEFAULT_QUERY_DATA = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY_CHECK_DATA = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_CHECK_DATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    private static final String DEFAULT_LIST_PARENT_INPUT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LIST_PARENT_INPUT_LEVEL = "BBBBBBBBBB";

    @Autowired
    private ConfigQueryKpiRepository configQueryKpiRepository;

    @Autowired
    private ConfigQueryKpiMapper configQueryKpiMapper;

    @Autowired
    private ConfigQueryKpiService configQueryKpiService;

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

    private MockMvc restConfigQueryKpiMockMvc;

    private ConfigQueryKpi configQueryKpi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigQueryKpiResource configQueryKpiResource = new ConfigQueryKpiResource(configQueryKpiService);
        this.restConfigQueryKpiMockMvc = MockMvcBuilders.standaloneSetup(configQueryKpiResource)
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
    public static ConfigQueryKpi createEntity(EntityManager em) {
        ConfigQueryKpi configQueryKpi = new ConfigQueryKpi()
            .timeType(DEFAULT_TIME_TYPE)
            .inputLevel(DEFAULT_INPUT_LEVEL)
            .queryData(DEFAULT_QUERY_DATA)
            .queryCheckData(DEFAULT_QUERY_CHECK_DATA)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER)
            .listParentInputLevel(DEFAULT_LIST_PARENT_INPUT_LEVEL);
        return configQueryKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigQueryKpi createUpdatedEntity(EntityManager em) {
        ConfigQueryKpi configQueryKpi = new ConfigQueryKpi()
            .timeType(UPDATED_TIME_TYPE)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .queryData(UPDATED_QUERY_DATA)
            .queryCheckData(UPDATED_QUERY_CHECK_DATA)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER)
            .listParentInputLevel(UPDATED_LIST_PARENT_INPUT_LEVEL);
        return configQueryKpi;
    }

    @BeforeEach
    public void initTest() {
        configQueryKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigQueryKpi() throws Exception {
        int databaseSizeBeforeCreate = configQueryKpiRepository.findAll().size();

        // Create the ConfigQueryKpi
        ConfigQueryKpiDTO configQueryKpiDTO = configQueryKpiMapper.toDto(configQueryKpi);
        restConfigQueryKpiMockMvc.perform(post("/api/config-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configQueryKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigQueryKpi in the database
        List<ConfigQueryKpi> configQueryKpiList = configQueryKpiRepository.findAll();
        assertThat(configQueryKpiList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigQueryKpi testConfigQueryKpi = configQueryKpiList.get(configQueryKpiList.size() - 1);
        assertThat(testConfigQueryKpi.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
        assertThat(testConfigQueryKpi.getInputLevel()).isEqualTo(DEFAULT_INPUT_LEVEL);
        assertThat(testConfigQueryKpi.getQueryData()).isEqualTo(DEFAULT_QUERY_DATA);
        assertThat(testConfigQueryKpi.getQueryCheckData()).isEqualTo(DEFAULT_QUERY_CHECK_DATA);
        assertThat(testConfigQueryKpi.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigQueryKpi.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigQueryKpi.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigQueryKpi.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testConfigQueryKpi.getListParentInputLevel()).isEqualTo(DEFAULT_LIST_PARENT_INPUT_LEVEL);
    }

    @Test
    @Transactional
    public void createConfigQueryKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configQueryKpiRepository.findAll().size();

        // Create the ConfigQueryKpi with an existing ID
        configQueryKpi.setId(1L);
        ConfigQueryKpiDTO configQueryKpiDTO = configQueryKpiMapper.toDto(configQueryKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigQueryKpiMockMvc.perform(post("/api/config-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigQueryKpi in the database
        List<ConfigQueryKpi> configQueryKpiList = configQueryKpiRepository.findAll();
        assertThat(configQueryKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigQueryKpis() throws Exception {
        // Initialize the database
        configQueryKpiRepository.saveAndFlush(configQueryKpi);

        // Get all the configQueryKpiList
        restConfigQueryKpiMockMvc.perform(get("/api/config-query-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configQueryKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeType").value(hasItem(DEFAULT_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].inputLevel").value(hasItem(DEFAULT_INPUT_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].queryData").value(hasItem(DEFAULT_QUERY_DATA)))
            .andExpect(jsonPath("$.[*].queryCheckData").value(hasItem(DEFAULT_QUERY_CHECK_DATA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)))
            .andExpect(jsonPath("$.[*].listParentInputLevel").value(hasItem(DEFAULT_LIST_PARENT_INPUT_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getConfigQueryKpi() throws Exception {
        // Initialize the database
        configQueryKpiRepository.saveAndFlush(configQueryKpi);

        // Get the configQueryKpi
        restConfigQueryKpiMockMvc.perform(get("/api/config-query-kpis/{id}", configQueryKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configQueryKpi.getId().intValue()))
            .andExpect(jsonPath("$.timeType").value(DEFAULT_TIME_TYPE))
            .andExpect(jsonPath("$.inputLevel").value(DEFAULT_INPUT_LEVEL.intValue()))
            .andExpect(jsonPath("$.queryData").value(DEFAULT_QUERY_DATA))
            .andExpect(jsonPath("$.queryCheckData").value(DEFAULT_QUERY_CHECK_DATA))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER))
            .andExpect(jsonPath("$.listParentInputLevel").value(DEFAULT_LIST_PARENT_INPUT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingConfigQueryKpi() throws Exception {
        // Get the configQueryKpi
        restConfigQueryKpiMockMvc.perform(get("/api/config-query-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigQueryKpi() throws Exception {
        // Initialize the database
        configQueryKpiRepository.saveAndFlush(configQueryKpi);

        int databaseSizeBeforeUpdate = configQueryKpiRepository.findAll().size();

        // Update the configQueryKpi
        ConfigQueryKpi updatedConfigQueryKpi = configQueryKpiRepository.findById(configQueryKpi.getId()).get();
        // Disconnect from session so that the updates on updatedConfigQueryKpi are not directly saved in db
        em.detach(updatedConfigQueryKpi);
        updatedConfigQueryKpi
            .timeType(UPDATED_TIME_TYPE)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .queryData(UPDATED_QUERY_DATA)
            .queryCheckData(UPDATED_QUERY_CHECK_DATA)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER)
            .listParentInputLevel(UPDATED_LIST_PARENT_INPUT_LEVEL);
        ConfigQueryKpiDTO configQueryKpiDTO = configQueryKpiMapper.toDto(updatedConfigQueryKpi);

        restConfigQueryKpiMockMvc.perform(put("/api/config-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configQueryKpiDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigQueryKpi in the database
        List<ConfigQueryKpi> configQueryKpiList = configQueryKpiRepository.findAll();
        assertThat(configQueryKpiList).hasSize(databaseSizeBeforeUpdate);
        ConfigQueryKpi testConfigQueryKpi = configQueryKpiList.get(configQueryKpiList.size() - 1);
        assertThat(testConfigQueryKpi.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
        assertThat(testConfigQueryKpi.getInputLevel()).isEqualTo(UPDATED_INPUT_LEVEL);
        assertThat(testConfigQueryKpi.getQueryData()).isEqualTo(UPDATED_QUERY_DATA);
        assertThat(testConfigQueryKpi.getQueryCheckData()).isEqualTo(UPDATED_QUERY_CHECK_DATA);
        assertThat(testConfigQueryKpi.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigQueryKpi.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigQueryKpi.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigQueryKpi.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testConfigQueryKpi.getListParentInputLevel()).isEqualTo(UPDATED_LIST_PARENT_INPUT_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigQueryKpi() throws Exception {
        int databaseSizeBeforeUpdate = configQueryKpiRepository.findAll().size();

        // Create the ConfigQueryKpi
        ConfigQueryKpiDTO configQueryKpiDTO = configQueryKpiMapper.toDto(configQueryKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigQueryKpiMockMvc.perform(put("/api/config-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigQueryKpi in the database
        List<ConfigQueryKpi> configQueryKpiList = configQueryKpiRepository.findAll();
        assertThat(configQueryKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigQueryKpi() throws Exception {
        // Initialize the database
        configQueryKpiRepository.saveAndFlush(configQueryKpi);

        int databaseSizeBeforeDelete = configQueryKpiRepository.findAll().size();

        // Delete the configQueryKpi
        restConfigQueryKpiMockMvc.perform(delete("/api/config-query-kpis/{id}", configQueryKpi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigQueryKpi> configQueryKpiList = configQueryKpiRepository.findAll();
        assertThat(configQueryKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
