package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigMapGroupChartArea;
import com.b4t.app.repository.ConfigMapGroupChartAreaRepository;
import com.b4t.app.service.ConfigMapGroupChartAreaService;
import com.b4t.app.service.dto.ConfigMapGroupChartAreaDTO;
import com.b4t.app.service.mapper.ConfigMapGroupChartAreaMapper;
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
 * Integration tests for the {@link ConfigMapGroupChartAreaResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigMapGroupChartAreaResourceIT {

    private static final Long DEFAULT_GROUP_CHART_ID = 1L;
    private static final Long UPDATED_GROUP_CHART_ID = 2L;

    private static final Long DEFAULT_AREA_ID = 1L;
    private static final Long UPDATED_AREA_ID = 2L;

    private static final String DEFAULT_POSITION_JSON = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_JSON = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigMapGroupChartAreaRepository configMapGroupChartAreaRepository;

    @Autowired
    private ConfigMapGroupChartAreaMapper configMapGroupChartAreaMapper;

    @Autowired
    private ConfigMapGroupChartAreaService configMapGroupChartAreaService;

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

    private MockMvc restConfigMapGroupChartAreaMockMvc;

    private ConfigMapGroupChartArea configMapGroupChartArea;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMapGroupChartAreaResource configMapGroupChartAreaResource = new ConfigMapGroupChartAreaResource(configMapGroupChartAreaService);
        this.restConfigMapGroupChartAreaMockMvc = MockMvcBuilders.standaloneSetup(configMapGroupChartAreaResource)
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
    public static ConfigMapGroupChartArea createEntity(EntityManager em) {
        ConfigMapGroupChartArea configMapGroupChartArea = new ConfigMapGroupChartArea()
            .groupChartId(DEFAULT_GROUP_CHART_ID)
            .areaId(DEFAULT_AREA_ID)
            .positionJson(DEFAULT_POSITION_JSON)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configMapGroupChartArea;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigMapGroupChartArea createUpdatedEntity(EntityManager em) {
        ConfigMapGroupChartArea configMapGroupChartArea = new ConfigMapGroupChartArea()
            .groupChartId(UPDATED_GROUP_CHART_ID)
            .areaId(UPDATED_AREA_ID)
            .positionJson(UPDATED_POSITION_JSON)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configMapGroupChartArea;
    }

    @BeforeEach
    public void initTest() {
        configMapGroupChartArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigMapGroupChartArea() throws Exception {
        int databaseSizeBeforeCreate = configMapGroupChartAreaRepository.findAll().size();

        // Create the ConfigMapGroupChartArea
        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO = configMapGroupChartAreaMapper.toDto(configMapGroupChartArea);
        restConfigMapGroupChartAreaMockMvc.perform(post("/api/config-map-group-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapGroupChartAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigMapGroupChartArea in the database
        List<ConfigMapGroupChartArea> configMapGroupChartAreaList = configMapGroupChartAreaRepository.findAll();
        assertThat(configMapGroupChartAreaList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMapGroupChartArea testConfigMapGroupChartArea = configMapGroupChartAreaList.get(configMapGroupChartAreaList.size() - 1);
        assertThat(testConfigMapGroupChartArea.getGroupChartId()).isEqualTo(DEFAULT_GROUP_CHART_ID);
        assertThat(testConfigMapGroupChartArea.getAreaId()).isEqualTo(DEFAULT_AREA_ID);
        assertThat(testConfigMapGroupChartArea.getPositionJson()).isEqualTo(DEFAULT_POSITION_JSON);
        assertThat(testConfigMapGroupChartArea.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigMapGroupChartArea.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigMapGroupChartArea.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigMapGroupChartAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMapGroupChartAreaRepository.findAll().size();

        // Create the ConfigMapGroupChartArea with an existing ID
        configMapGroupChartArea.setId(1L);
        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO = configMapGroupChartAreaMapper.toDto(configMapGroupChartArea);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMapGroupChartAreaMockMvc.perform(post("/api/config-map-group-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapGroupChartAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapGroupChartArea in the database
        List<ConfigMapGroupChartArea> configMapGroupChartAreaList = configMapGroupChartAreaRepository.findAll();
        assertThat(configMapGroupChartAreaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigMapGroupChartAreas() throws Exception {
        // Initialize the database
        configMapGroupChartAreaRepository.saveAndFlush(configMapGroupChartArea);

        // Get all the configMapGroupChartAreaList
        restConfigMapGroupChartAreaMockMvc.perform(get("/api/config-map-group-chart-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMapGroupChartArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupChartId").value(hasItem(DEFAULT_GROUP_CHART_ID.intValue())))
            .andExpect(jsonPath("$.[*].areaId").value(hasItem(DEFAULT_AREA_ID.intValue())))
            .andExpect(jsonPath("$.[*].positionJson").value(hasItem(DEFAULT_POSITION_JSON)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigMapGroupChartArea() throws Exception {
        // Initialize the database
        configMapGroupChartAreaRepository.saveAndFlush(configMapGroupChartArea);

        // Get the configMapGroupChartArea
        restConfigMapGroupChartAreaMockMvc.perform(get("/api/config-map-group-chart-areas/{id}", configMapGroupChartArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configMapGroupChartArea.getId().intValue()))
            .andExpect(jsonPath("$.groupChartId").value(DEFAULT_GROUP_CHART_ID.intValue()))
            .andExpect(jsonPath("$.areaId").value(DEFAULT_AREA_ID.intValue()))
            .andExpect(jsonPath("$.positionJson").value(DEFAULT_POSITION_JSON))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigMapGroupChartArea() throws Exception {
        // Get the configMapGroupChartArea
        restConfigMapGroupChartAreaMockMvc.perform(get("/api/config-map-group-chart-areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigMapGroupChartArea() throws Exception {
        // Initialize the database
        configMapGroupChartAreaRepository.saveAndFlush(configMapGroupChartArea);

        int databaseSizeBeforeUpdate = configMapGroupChartAreaRepository.findAll().size();

        // Update the configMapGroupChartArea
        ConfigMapGroupChartArea updatedConfigMapGroupChartArea = configMapGroupChartAreaRepository.findById(configMapGroupChartArea.getId()).get();
        // Disconnect from session so that the updates on updatedConfigMapGroupChartArea are not directly saved in db
        em.detach(updatedConfigMapGroupChartArea);
        updatedConfigMapGroupChartArea
            .groupChartId(UPDATED_GROUP_CHART_ID)
            .areaId(UPDATED_AREA_ID)
            .positionJson(UPDATED_POSITION_JSON)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO = configMapGroupChartAreaMapper.toDto(updatedConfigMapGroupChartArea);

        restConfigMapGroupChartAreaMockMvc.perform(put("/api/config-map-group-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapGroupChartAreaDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigMapGroupChartArea in the database
        List<ConfigMapGroupChartArea> configMapGroupChartAreaList = configMapGroupChartAreaRepository.findAll();
        assertThat(configMapGroupChartAreaList).hasSize(databaseSizeBeforeUpdate);
        ConfigMapGroupChartArea testConfigMapGroupChartArea = configMapGroupChartAreaList.get(configMapGroupChartAreaList.size() - 1);
        assertThat(testConfigMapGroupChartArea.getGroupChartId()).isEqualTo(UPDATED_GROUP_CHART_ID);
        assertThat(testConfigMapGroupChartArea.getAreaId()).isEqualTo(UPDATED_AREA_ID);
        assertThat(testConfigMapGroupChartArea.getPositionJson()).isEqualTo(UPDATED_POSITION_JSON);
        assertThat(testConfigMapGroupChartArea.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigMapGroupChartArea.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigMapGroupChartArea.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigMapGroupChartArea() throws Exception {
        int databaseSizeBeforeUpdate = configMapGroupChartAreaRepository.findAll().size();

        // Create the ConfigMapGroupChartArea
        ConfigMapGroupChartAreaDTO configMapGroupChartAreaDTO = configMapGroupChartAreaMapper.toDto(configMapGroupChartArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMapGroupChartAreaMockMvc.perform(put("/api/config-map-group-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapGroupChartAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapGroupChartArea in the database
        List<ConfigMapGroupChartArea> configMapGroupChartAreaList = configMapGroupChartAreaRepository.findAll();
        assertThat(configMapGroupChartAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigMapGroupChartArea() throws Exception {
        // Initialize the database
        configMapGroupChartAreaRepository.saveAndFlush(configMapGroupChartArea);

        int databaseSizeBeforeDelete = configMapGroupChartAreaRepository.findAll().size();

        // Delete the configMapGroupChartArea
        restConfigMapGroupChartAreaMockMvc.perform(delete("/api/config-map-group-chart-areas/{id}", configMapGroupChartArea.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigMapGroupChartArea> configMapGroupChartAreaList = configMapGroupChartAreaRepository.findAll();
        assertThat(configMapGroupChartAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
