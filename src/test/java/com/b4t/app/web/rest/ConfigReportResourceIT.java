package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigReport;
import com.b4t.app.repository.ConfigReportRepository;
import com.b4t.app.service.ConfigReportService;
import com.b4t.app.service.dto.ConfigReportDTO;
import com.b4t.app.service.mapper.ConfigReportMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConfigReportResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class ConfigReportResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TIME_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DATABASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DATABASE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INPUT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATOR = "AAAAAAAAAA";
    private static final String UPDATED_CREATOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ConfigReportRepository configReportRepository;

    @Autowired
    private ConfigReportMapper configReportMapper;

    @Autowired
    private ConfigReportService configReportService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigReportMockMvc;

    private ConfigReport configReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigReport createEntity(EntityManager em) {
        ConfigReport configReport = new ConfigReport()
            .title(DEFAULT_TITLE)
            .timeType(DEFAULT_TIME_TYPE)
            .domainCode(DEFAULT_DOMAIN_CODE)
            .databaseName(DEFAULT_DATABASE_NAME)
            .tableName(DEFAULT_TABLE_NAME)
            .inputLevel(DEFAULT_INPUT_LEVEL)
            .unit(DEFAULT_UNIT)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .creator(DEFAULT_CREATOR)
            .updateTime(DEFAULT_UPDATE_TIME);
        return configReport;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigReport createUpdatedEntity(EntityManager em) {
        ConfigReport configReport = new ConfigReport()
            .title(UPDATED_TITLE)
            .timeType(UPDATED_TIME_TYPE)
            .domainCode(UPDATED_DOMAIN_CODE)
            .databaseName(UPDATED_DATABASE_NAME)
            .tableName(UPDATED_TABLE_NAME)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .unit(UPDATED_UNIT)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .updateTime(UPDATED_UPDATE_TIME);
        return configReport;
    }

    @BeforeEach
    public void initTest() {
        configReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigReport() throws Exception {
        int databaseSizeBeforeCreate = configReportRepository.findAll().size();

        // Create the ConfigReport
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);
        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigReport in the database
        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigReport testConfigReport = configReportList.get(configReportList.size() - 1);
        assertThat(testConfigReport.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testConfigReport.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
        assertThat(testConfigReport.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
        assertThat(testConfigReport.getDatabaseName()).isEqualTo(DEFAULT_DATABASE_NAME);
        assertThat(testConfigReport.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testConfigReport.getInputLevel()).isEqualTo(DEFAULT_INPUT_LEVEL);
        assertThat(testConfigReport.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testConfigReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigReport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigReport.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testConfigReport.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createConfigReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configReportRepository.findAll().size();

        // Create the ConfigReport with an existing ID
        configReport.setId(1L);
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigReport in the database
        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportRepository.findAll().size();
        // set the field null
        configReport.setTitle(null);

        // Create the ConfigReport, which fails.
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportRepository.findAll().size();
        // set the field null
        configReport.setTimeType(null);

        // Create the ConfigReport, which fails.
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportRepository.findAll().size();
        // set the field null
        configReport.setDomainCode(null);

        // Create the ConfigReport, which fails.
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatabaseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportRepository.findAll().size();
        // set the field null
        configReport.setDatabaseName(null);

        // Create the ConfigReport, which fails.
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportRepository.findAll().size();
        // set the field null
        configReport.setTableName(null);

        // Create the ConfigReport, which fails.
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInputLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportRepository.findAll().size();
        // set the field null
        configReport.setInputLevel(null);

        // Create the ConfigReport, which fails.
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        restConfigReportMockMvc.perform(post("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigReports() throws Exception {
        // Initialize the database
        configReportRepository.saveAndFlush(configReport);

        // Get all the configReportList
        restConfigReportMockMvc.perform(get("/api/config-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].timeType").value(hasItem(DEFAULT_TIME_TYPE)))
            .andExpect(jsonPath("$.[*].domainCode").value(hasItem(DEFAULT_DOMAIN_CODE)))
            .andExpect(jsonPath("$.[*].databaseName").value(hasItem(DEFAULT_DATABASE_NAME)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].inputLevel").value(hasItem(DEFAULT_INPUT_LEVEL)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getConfigReport() throws Exception {
        // Initialize the database
        configReportRepository.saveAndFlush(configReport);

        // Get the configReport
        restConfigReportMockMvc.perform(get("/api/config-reports/{id}", configReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configReport.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.timeType").value(DEFAULT_TIME_TYPE))
            .andExpect(jsonPath("$.domainCode").value(DEFAULT_DOMAIN_CODE))
            .andExpect(jsonPath("$.databaseName").value(DEFAULT_DATABASE_NAME))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.inputLevel").value(DEFAULT_INPUT_LEVEL))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigReport() throws Exception {
        // Get the configReport
        restConfigReportMockMvc.perform(get("/api/config-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigReport() throws Exception {
        // Initialize the database
        configReportRepository.saveAndFlush(configReport);

        int databaseSizeBeforeUpdate = configReportRepository.findAll().size();

        // Update the configReport
        ConfigReport updatedConfigReport = configReportRepository.findById(configReport.getId()).get();
        // Disconnect from session so that the updates on updatedConfigReport are not directly saved in db
        em.detach(updatedConfigReport);
        updatedConfigReport
            .title(UPDATED_TITLE)
            .timeType(UPDATED_TIME_TYPE)
            .domainCode(UPDATED_DOMAIN_CODE)
            .databaseName(UPDATED_DATABASE_NAME)
            .tableName(UPDATED_TABLE_NAME)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .unit(UPDATED_UNIT)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .updateTime(UPDATED_UPDATE_TIME);
        ConfigReportDTO configReportDTO = configReportMapper.toDto(updatedConfigReport);

        restConfigReportMockMvc.perform(put("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigReport in the database
        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeUpdate);
        ConfigReport testConfigReport = configReportList.get(configReportList.size() - 1);
        assertThat(testConfigReport.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testConfigReport.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
        assertThat(testConfigReport.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
        assertThat(testConfigReport.getDatabaseName()).isEqualTo(UPDATED_DATABASE_NAME);
        assertThat(testConfigReport.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testConfigReport.getInputLevel()).isEqualTo(UPDATED_INPUT_LEVEL);
        assertThat(testConfigReport.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testConfigReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigReport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigReport.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testConfigReport.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigReport() throws Exception {
        int databaseSizeBeforeUpdate = configReportRepository.findAll().size();

        // Create the ConfigReport
        ConfigReportDTO configReportDTO = configReportMapper.toDto(configReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigReportMockMvc.perform(put("/api/config-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigReport in the database
        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigReport() throws Exception {
        // Initialize the database
        configReportRepository.saveAndFlush(configReport);

        int databaseSizeBeforeDelete = configReportRepository.findAll().size();

        // Delete the configReport
        restConfigReportMockMvc.perform(delete("/api/config-reports/{id}", configReport.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigReport> configReportList = configReportRepository.findAll();
        assertThat(configReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
