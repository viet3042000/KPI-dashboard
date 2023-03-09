package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigMapChartArea;
import com.b4t.app.repository.ConfigMapChartAreaRepository;
import com.b4t.app.service.ConfigMapChartAreaService;
import com.b4t.app.service.dto.ConfigMapChartAreaDTO;
import com.b4t.app.service.mapper.ConfigMapChartAreaMapper;
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
 * Integration tests for the {@link ConfigMapChartAreaResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigMapChartAreaResourceIT {

    private static final Long DEFAULT_CHART_ID = 1L;
    private static final Long UPDATED_CHART_ID = 2L;

    private static final Long DEFAULT_AREA_ID = 1L;
    private static final Long UPDATED_AREA_ID = 2L;

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final Long DEFAULT_SCREEN_ID_NEXTTO = 1L;
    private static final Long UPDATED_SCREEN_ID_NEXTTO = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigMapChartAreaRepository configMapChartAreaRepository;

    @Autowired
    private ConfigMapChartAreaMapper configMapChartAreaMapper;

    @Autowired
    private ConfigMapChartAreaService configMapChartAreaService;

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

    private MockMvc restConfigMapChartAreaMockMvc;

    private ConfigMapChartArea configMapChartArea;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMapChartAreaResource configMapChartAreaResource = new ConfigMapChartAreaResource(configMapChartAreaService);
        this.restConfigMapChartAreaMockMvc = MockMvcBuilders.standaloneSetup(configMapChartAreaResource)
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
    public static ConfigMapChartArea createEntity(EntityManager em) {
        ConfigMapChartArea configMapChartArea = new ConfigMapChartArea()
            .chartId(DEFAULT_CHART_ID)
            .areaId(DEFAULT_AREA_ID)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .screenIdNextto(DEFAULT_SCREEN_ID_NEXTTO)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configMapChartArea;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigMapChartArea createUpdatedEntity(EntityManager em) {
        ConfigMapChartArea configMapChartArea = new ConfigMapChartArea()
            .chartId(UPDATED_CHART_ID)
            .areaId(UPDATED_AREA_ID)
            .orderIndex(UPDATED_ORDER_INDEX)
            .screenIdNextto(UPDATED_SCREEN_ID_NEXTTO)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configMapChartArea;
    }

    @BeforeEach
    public void initTest() {
        configMapChartArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigMapChartArea() throws Exception {
        int databaseSizeBeforeCreate = configMapChartAreaRepository.findAll().size();

        // Create the ConfigMapChartArea
        ConfigMapChartAreaDTO configMapChartAreaDTO = configMapChartAreaMapper.toDto(configMapChartArea);
        restConfigMapChartAreaMockMvc.perform(post("/api/config-map-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigMapChartArea in the database
        List<ConfigMapChartArea> configMapChartAreaList = configMapChartAreaRepository.findAll();
        assertThat(configMapChartAreaList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMapChartArea testConfigMapChartArea = configMapChartAreaList.get(configMapChartAreaList.size() - 1);
        assertThat(testConfigMapChartArea.getChartId()).isEqualTo(DEFAULT_CHART_ID);
        assertThat(testConfigMapChartArea.getAreaId()).isEqualTo(DEFAULT_AREA_ID);
        assertThat(testConfigMapChartArea.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigMapChartArea.getScreenIdNextto()).isEqualTo(DEFAULT_SCREEN_ID_NEXTTO);
        assertThat(testConfigMapChartArea.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigMapChartArea.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigMapChartArea.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigMapChartAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMapChartAreaRepository.findAll().size();

        // Create the ConfigMapChartArea with an existing ID
        configMapChartArea.setId(1L);
        ConfigMapChartAreaDTO configMapChartAreaDTO = configMapChartAreaMapper.toDto(configMapChartArea);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMapChartAreaMockMvc.perform(post("/api/config-map-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapChartArea in the database
        List<ConfigMapChartArea> configMapChartAreaList = configMapChartAreaRepository.findAll();
        assertThat(configMapChartAreaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigMapChartAreas() throws Exception {
        // Initialize the database
        configMapChartAreaRepository.saveAndFlush(configMapChartArea);

        // Get all the configMapChartAreaList
        restConfigMapChartAreaMockMvc.perform(get("/api/config-map-chart-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMapChartArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].chartId").value(hasItem(DEFAULT_CHART_ID.intValue())))
            .andExpect(jsonPath("$.[*].areaId").value(hasItem(DEFAULT_AREA_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].screenIdNextto").value(hasItem(DEFAULT_SCREEN_ID_NEXTTO.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigMapChartArea() throws Exception {
        // Initialize the database
        configMapChartAreaRepository.saveAndFlush(configMapChartArea);

        // Get the configMapChartArea
        restConfigMapChartAreaMockMvc.perform(get("/api/config-map-chart-areas/{id}", configMapChartArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configMapChartArea.getId().intValue()))
            .andExpect(jsonPath("$.chartId").value(DEFAULT_CHART_ID.intValue()))
            .andExpect(jsonPath("$.areaId").value(DEFAULT_AREA_ID.intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.screenIdNextto").value(DEFAULT_SCREEN_ID_NEXTTO.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigMapChartArea() throws Exception {
        // Get the configMapChartArea
        restConfigMapChartAreaMockMvc.perform(get("/api/config-map-chart-areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigMapChartArea() throws Exception {
        // Initialize the database
        configMapChartAreaRepository.saveAndFlush(configMapChartArea);

        int databaseSizeBeforeUpdate = configMapChartAreaRepository.findAll().size();

        // Update the configMapChartArea
        ConfigMapChartArea updatedConfigMapChartArea = configMapChartAreaRepository.findById(configMapChartArea.getId()).get();
        // Disconnect from session so that the updates on updatedConfigMapChartArea are not directly saved in db
        em.detach(updatedConfigMapChartArea);
        updatedConfigMapChartArea
            .chartId(UPDATED_CHART_ID)
            .areaId(UPDATED_AREA_ID)
            .orderIndex(UPDATED_ORDER_INDEX)
            .screenIdNextto(UPDATED_SCREEN_ID_NEXTTO)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigMapChartAreaDTO configMapChartAreaDTO = configMapChartAreaMapper.toDto(updatedConfigMapChartArea);

        restConfigMapChartAreaMockMvc.perform(put("/api/config-map-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartAreaDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigMapChartArea in the database
        List<ConfigMapChartArea> configMapChartAreaList = configMapChartAreaRepository.findAll();
        assertThat(configMapChartAreaList).hasSize(databaseSizeBeforeUpdate);
        ConfigMapChartArea testConfigMapChartArea = configMapChartAreaList.get(configMapChartAreaList.size() - 1);
        assertThat(testConfigMapChartArea.getChartId()).isEqualTo(UPDATED_CHART_ID);
        assertThat(testConfigMapChartArea.getAreaId()).isEqualTo(UPDATED_AREA_ID);
        assertThat(testConfigMapChartArea.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigMapChartArea.getScreenIdNextto()).isEqualTo(UPDATED_SCREEN_ID_NEXTTO);
        assertThat(testConfigMapChartArea.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigMapChartArea.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigMapChartArea.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigMapChartArea() throws Exception {
        int databaseSizeBeforeUpdate = configMapChartAreaRepository.findAll().size();

        // Create the ConfigMapChartArea
        ConfigMapChartAreaDTO configMapChartAreaDTO = configMapChartAreaMapper.toDto(configMapChartArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMapChartAreaMockMvc.perform(put("/api/config-map-chart-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMapChartAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapChartArea in the database
        List<ConfigMapChartArea> configMapChartAreaList = configMapChartAreaRepository.findAll();
        assertThat(configMapChartAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigMapChartArea() throws Exception {
        // Initialize the database
        configMapChartAreaRepository.saveAndFlush(configMapChartArea);

        int databaseSizeBeforeDelete = configMapChartAreaRepository.findAll().size();

        // Delete the configMapChartArea
        restConfigMapChartAreaMockMvc.perform(delete("/api/config-map-chart-areas/{id}", configMapChartArea.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigMapChartArea> configMapChartAreaList = configMapChartAreaRepository.findAll();
        assertThat(configMapChartAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
