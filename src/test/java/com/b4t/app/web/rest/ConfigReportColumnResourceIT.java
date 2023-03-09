package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigReportColumn;
import com.b4t.app.repository.ConfigReportColumnRepository;
import com.b4t.app.service.ConfigReportColumnService;
import com.b4t.app.service.dto.ConfigReportColumnDTO;
import com.b4t.app.service.mapper.ConfigReportColumnMapper;

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
 * Integration tests for the {@link ConfigReportColumnResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class ConfigReportColumnResourceIT {

    private static final String DEFAULT_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_REPORT_ID = 1L;
    private static final Long UPDATED_REPORT_ID = 2L;

    private static final Integer DEFAULT_IS_REQUIRE = 1;
    private static final Integer UPDATED_IS_REQUIRE = 2;

    private static final Integer DEFAULT_MAX_LENGTH = 1;
    private static final Integer UPDATED_MAX_LENGTH = 2;

    private static final String DEFAULT_REGEX_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_REGEX_PATTERN = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_SHOW = 1;
    private static final Integer UPDATED_IS_SHOW = 2;

    private static final Integer DEFAULT_IS_TIME_COLUMN = 1;
    private static final Integer UPDATED_IS_TIME_COLUMN = 2;

    private static final Integer DEFAULT_POS = 1;
    private static final Integer UPDATED_POS = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_CREATOR = "AAAAAAAAAA";
    private static final String UPDATED_CREATOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ConfigReportColumnRepository configReportColumnRepository;

    @Autowired
    private ConfigReportColumnMapper configReportColumnMapper;

    @Autowired
    private ConfigReportColumnService configReportColumnService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigReportColumnMockMvc;

    private ConfigReportColumn configReportColumn;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigReportColumn createEntity(EntityManager em) {
        ConfigReportColumn configReportColumn = new ConfigReportColumn()
            .columnName(DEFAULT_COLUMN_NAME)
            .title(DEFAULT_TITLE)
            .dataType(DEFAULT_DATA_TYPE)
            .reportId(DEFAULT_REPORT_ID)
            .isRequire(DEFAULT_IS_REQUIRE)
            .maxLength(DEFAULT_MAX_LENGTH)
            .regexPattern(DEFAULT_REGEX_PATTERN)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .isShow(DEFAULT_IS_SHOW)
            .isTimeColumn(DEFAULT_IS_TIME_COLUMN)
            .pos(DEFAULT_POS)
            .status(DEFAULT_STATUS)
            .creator(DEFAULT_CREATOR)
            .updateTime(DEFAULT_UPDATE_TIME);
        return configReportColumn;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigReportColumn createUpdatedEntity(EntityManager em) {
        ConfigReportColumn configReportColumn = new ConfigReportColumn()
            .columnName(UPDATED_COLUMN_NAME)
            .title(UPDATED_TITLE)
            .dataType(UPDATED_DATA_TYPE)
            .reportId(UPDATED_REPORT_ID)
            .isRequire(UPDATED_IS_REQUIRE)
            .maxLength(UPDATED_MAX_LENGTH)
            .regexPattern(UPDATED_REGEX_PATTERN)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .isShow(UPDATED_IS_SHOW)
            .isTimeColumn(UPDATED_IS_TIME_COLUMN)
            .pos(UPDATED_POS)
            .status(UPDATED_STATUS)
            .creator(UPDATED_CREATOR)
            .updateTime(UPDATED_UPDATE_TIME);
        return configReportColumn;
    }

    @BeforeEach
    public void initTest() {
        configReportColumn = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigReportColumn() throws Exception {
        int databaseSizeBeforeCreate = configReportColumnRepository.findAll().size();

        // Create the ConfigReportColumn
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);
        restConfigReportColumnMockMvc.perform(post("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigReportColumn in the database
        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigReportColumn testConfigReportColumn = configReportColumnList.get(configReportColumnList.size() - 1);
        assertThat(testConfigReportColumn.getColumnName()).isEqualTo(DEFAULT_COLUMN_NAME);
        assertThat(testConfigReportColumn.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testConfigReportColumn.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testConfigReportColumn.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
        assertThat(testConfigReportColumn.getIsRequire()).isEqualTo(DEFAULT_IS_REQUIRE);
        assertThat(testConfigReportColumn.getMaxLength()).isEqualTo(DEFAULT_MAX_LENGTH);
        assertThat(testConfigReportColumn.getRegexPattern()).isEqualTo(DEFAULT_REGEX_PATTERN);
        assertThat(testConfigReportColumn.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
        assertThat(testConfigReportColumn.getIsShow()).isEqualTo(DEFAULT_IS_SHOW);
        assertThat(testConfigReportColumn.getIsTimeColumn()).isEqualTo(DEFAULT_IS_TIME_COLUMN);
        assertThat(testConfigReportColumn.getPos()).isEqualTo(DEFAULT_POS);
        assertThat(testConfigReportColumn.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigReportColumn.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testConfigReportColumn.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createConfigReportColumnWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configReportColumnRepository.findAll().size();

        // Create the ConfigReportColumn with an existing ID
        configReportColumn.setId(1L);
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigReportColumnMockMvc.perform(post("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigReportColumn in the database
        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkColumnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportColumnRepository.findAll().size();
        // set the field null
        configReportColumn.setColumnName(null);

        // Create the ConfigReportColumn, which fails.
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);

        restConfigReportColumnMockMvc.perform(post("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportColumnRepository.findAll().size();
        // set the field null
        configReportColumn.setTitle(null);

        // Create the ConfigReportColumn, which fails.
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);

        restConfigReportColumnMockMvc.perform(post("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportColumnRepository.findAll().size();
        // set the field null
        configReportColumn.setDataType(null);

        // Create the ConfigReportColumn, which fails.
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);

        restConfigReportColumnMockMvc.perform(post("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReportIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = configReportColumnRepository.findAll().size();
        // set the field null
        configReportColumn.setReportId(null);

        // Create the ConfigReportColumn, which fails.
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);

        restConfigReportColumnMockMvc.perform(post("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isBadRequest());

        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigReportColumns() throws Exception {
        // Initialize the database
        configReportColumnRepository.saveAndFlush(configReportColumn);

        // Get all the configReportColumnList
        restConfigReportColumnMockMvc.perform(get("/api/config-report-columns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configReportColumn.getId().intValue())))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].isRequire").value(hasItem(DEFAULT_IS_REQUIRE)))
            .andExpect(jsonPath("$.[*].maxLength").value(hasItem(DEFAULT_MAX_LENGTH)))
            .andExpect(jsonPath("$.[*].regexPattern").value(hasItem(DEFAULT_REGEX_PATTERN)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].isShow").value(hasItem(DEFAULT_IS_SHOW)))
            .andExpect(jsonPath("$.[*].isTimeColumn").value(hasItem(DEFAULT_IS_TIME_COLUMN)))
            .andExpect(jsonPath("$.[*].pos").value(hasItem(DEFAULT_POS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getConfigReportColumn() throws Exception {
        // Initialize the database
        configReportColumnRepository.saveAndFlush(configReportColumn);

        // Get the configReportColumn
        restConfigReportColumnMockMvc.perform(get("/api/config-report-columns/{id}", configReportColumn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configReportColumn.getId().intValue()))
            .andExpect(jsonPath("$.columnName").value(DEFAULT_COLUMN_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.isRequire").value(DEFAULT_IS_REQUIRE))
            .andExpect(jsonPath("$.maxLength").value(DEFAULT_MAX_LENGTH))
            .andExpect(jsonPath("$.regexPattern").value(DEFAULT_REGEX_PATTERN))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE))
            .andExpect(jsonPath("$.isShow").value(DEFAULT_IS_SHOW))
            .andExpect(jsonPath("$.isTimeColumn").value(DEFAULT_IS_TIME_COLUMN))
            .andExpect(jsonPath("$.pos").value(DEFAULT_POS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigReportColumn() throws Exception {
        // Get the configReportColumn
        restConfigReportColumnMockMvc.perform(get("/api/config-report-columns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigReportColumn() throws Exception {
        // Initialize the database
        configReportColumnRepository.saveAndFlush(configReportColumn);

        int databaseSizeBeforeUpdate = configReportColumnRepository.findAll().size();

        // Update the configReportColumn
        ConfigReportColumn updatedConfigReportColumn = configReportColumnRepository.findById(configReportColumn.getId()).get();
        // Disconnect from session so that the updates on updatedConfigReportColumn are not directly saved in db
        em.detach(updatedConfigReportColumn);
        updatedConfigReportColumn
            .columnName(UPDATED_COLUMN_NAME)
            .title(UPDATED_TITLE)
            .dataType(UPDATED_DATA_TYPE)
            .reportId(UPDATED_REPORT_ID)
            .isRequire(UPDATED_IS_REQUIRE)
            .maxLength(UPDATED_MAX_LENGTH)
            .regexPattern(UPDATED_REGEX_PATTERN)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .isShow(UPDATED_IS_SHOW)
            .isTimeColumn(UPDATED_IS_TIME_COLUMN)
            .pos(UPDATED_POS)
            .status(UPDATED_STATUS)
            .creator(UPDATED_CREATOR)
            .updateTime(UPDATED_UPDATE_TIME);
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(updatedConfigReportColumn);

        restConfigReportColumnMockMvc.perform(put("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigReportColumn in the database
        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeUpdate);
        ConfigReportColumn testConfigReportColumn = configReportColumnList.get(configReportColumnList.size() - 1);
        assertThat(testConfigReportColumn.getColumnName()).isEqualTo(UPDATED_COLUMN_NAME);
        assertThat(testConfigReportColumn.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testConfigReportColumn.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testConfigReportColumn.getReportId()).isEqualTo(UPDATED_REPORT_ID);
        assertThat(testConfigReportColumn.getIsRequire()).isEqualTo(UPDATED_IS_REQUIRE);
        assertThat(testConfigReportColumn.getMaxLength()).isEqualTo(UPDATED_MAX_LENGTH);
        assertThat(testConfigReportColumn.getRegexPattern()).isEqualTo(UPDATED_REGEX_PATTERN);
        assertThat(testConfigReportColumn.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testConfigReportColumn.getIsShow()).isEqualTo(UPDATED_IS_SHOW);
        assertThat(testConfigReportColumn.getIsTimeColumn()).isEqualTo(UPDATED_IS_TIME_COLUMN);
        assertThat(testConfigReportColumn.getPos()).isEqualTo(UPDATED_POS);
        assertThat(testConfigReportColumn.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigReportColumn.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testConfigReportColumn.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigReportColumn() throws Exception {
        int databaseSizeBeforeUpdate = configReportColumnRepository.findAll().size();

        // Create the ConfigReportColumn
        ConfigReportColumnDTO configReportColumnDTO = configReportColumnMapper.toDto(configReportColumn);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigReportColumnMockMvc.perform(put("/api/config-report-columns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configReportColumnDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigReportColumn in the database
        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigReportColumn() throws Exception {
        // Initialize the database
        configReportColumnRepository.saveAndFlush(configReportColumn);

        int databaseSizeBeforeDelete = configReportColumnRepository.findAll().size();

        // Delete the configReportColumn
        restConfigReportColumnMockMvc.perform(delete("/api/config-report-columns/{id}", configReportColumn.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigReportColumn> configReportColumnList = configReportColumnRepository.findAll();
        assertThat(configReportColumnList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
