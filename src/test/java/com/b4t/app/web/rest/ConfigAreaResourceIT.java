package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigArea;
import com.b4t.app.repository.ConfigAreaRepository;
import com.b4t.app.service.ConfigAreaService;
import com.b4t.app.service.dto.ConfigAreaDTO;
import com.b4t.app.service.mapper.ConfigAreaMapper;
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
 * Integration tests for the {@link ConfigAreaResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigAreaResourceIT {

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AREA_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final String DEFAULT_POSITION_JSON = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_JSON = "BBBBBBBBBB";

    private static final Long DEFAULT_SCREEN_ID = 1L;
    private static final Long UPDATED_SCREEN_ID = 2L;

    private static final Long DEFAULT_TIME_REFRESH = 1L;
    private static final Long UPDATED_TIME_REFRESH = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UDPATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UDPATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UDPATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UDPATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigAreaRepository configAreaRepository;

    @Autowired
    private ConfigAreaMapper configAreaMapper;

    @Autowired
    private ConfigAreaService configAreaService;

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

    private MockMvc restConfigAreaMockMvc;

    private ConfigArea configArea;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigAreaResource configAreaResource = new ConfigAreaResource(configAreaService);
        this.restConfigAreaMockMvc = MockMvcBuilders.standaloneSetup(configAreaResource)
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
    public static ConfigArea createEntity(EntityManager em) {
        ConfigArea configArea = new ConfigArea()
            .areaCode(DEFAULT_AREA_CODE)
            .areaName(DEFAULT_AREA_NAME)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .positionJson(DEFAULT_POSITION_JSON)
            .screenId(DEFAULT_SCREEN_ID)
            .timeRefresh(DEFAULT_TIME_REFRESH)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UDPATE_TIME)
            .updateUser(DEFAULT_UDPATE_USER);
        return configArea;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigArea createUpdatedEntity(EntityManager em) {
        ConfigArea configArea = new ConfigArea()
            .areaCode(UPDATED_AREA_CODE)
            .areaName(UPDATED_AREA_NAME)
            .orderIndex(UPDATED_ORDER_INDEX)
            .positionJson(UPDATED_POSITION_JSON)
            .screenId(UPDATED_SCREEN_ID)
            .timeRefresh(UPDATED_TIME_REFRESH)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UDPATE_TIME)
            .updateUser(UPDATED_UDPATE_USER);
        return configArea;
    }

    @BeforeEach
    public void initTest() {
        configArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigArea() throws Exception {
        int databaseSizeBeforeCreate = configAreaRepository.findAll().size();

        // Create the ConfigArea
        ConfigAreaDTO configAreaDTO = configAreaMapper.toDto(configArea);
        restConfigAreaMockMvc.perform(post("/api/config-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigArea in the database
        List<ConfigArea> configAreaList = configAreaRepository.findAll();
        assertThat(configAreaList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigArea testConfigArea = configAreaList.get(configAreaList.size() - 1);
        assertThat(testConfigArea.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testConfigArea.getAreaName()).isEqualTo(DEFAULT_AREA_NAME);
        assertThat(testConfigArea.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigArea.getPositionJson()).isEqualTo(DEFAULT_POSITION_JSON);
        assertThat(testConfigArea.getScreenId()).isEqualTo(DEFAULT_SCREEN_ID);
        assertThat(testConfigArea.getTimeRefresh()).isEqualTo(DEFAULT_TIME_REFRESH);
        assertThat(testConfigArea.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigArea.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigArea.getUpdateTime()).isEqualTo(DEFAULT_UDPATE_TIME);
        assertThat(testConfigArea.getUpdateUser()).isEqualTo(DEFAULT_UDPATE_USER);
    }

    @Test
    @Transactional
    public void createConfigAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configAreaRepository.findAll().size();

        // Create the ConfigArea with an existing ID
        configArea.setId(1L);
        ConfigAreaDTO configAreaDTO = configAreaMapper.toDto(configArea);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigAreaMockMvc.perform(post("/api/config-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigArea in the database
        List<ConfigArea> configAreaList = configAreaRepository.findAll();
        assertThat(configAreaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigAreas() throws Exception {
        // Initialize the database
        configAreaRepository.saveAndFlush(configArea);

        // Get all the configAreaList
        restConfigAreaMockMvc.perform(get("/api/config-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE)))
            .andExpect(jsonPath("$.[*].areaName").value(hasItem(DEFAULT_AREA_NAME)))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].positionJson").value(hasItem(DEFAULT_POSITION_JSON)))
            .andExpect(jsonPath("$.[*].screenId").value(hasItem(DEFAULT_SCREEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].timeRefresh").value(hasItem(DEFAULT_TIME_REFRESH.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UDPATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UDPATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigArea() throws Exception {
        // Initialize the database
        configAreaRepository.saveAndFlush(configArea);

        // Get the configArea
        restConfigAreaMockMvc.perform(get("/api/config-areas/{id}", configArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configArea.getId().intValue()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE))
            .andExpect(jsonPath("$.areaName").value(DEFAULT_AREA_NAME))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.positionJson").value(DEFAULT_POSITION_JSON))
            .andExpect(jsonPath("$.screenId").value(DEFAULT_SCREEN_ID.intValue()))
            .andExpect(jsonPath("$.timeRefresh").value(DEFAULT_TIME_REFRESH.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UDPATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UDPATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigArea() throws Exception {
        // Get the configArea
        restConfigAreaMockMvc.perform(get("/api/config-areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigArea() throws Exception {
        // Initialize the database
        configAreaRepository.saveAndFlush(configArea);

        int databaseSizeBeforeUpdate = configAreaRepository.findAll().size();

        // Update the configArea
        ConfigArea updatedConfigArea = configAreaRepository.findById(configArea.getId()).get();
        // Disconnect from session so that the updates on updatedConfigArea are not directly saved in db
        em.detach(updatedConfigArea);
        updatedConfigArea
            .areaCode(UPDATED_AREA_CODE)
            .areaName(UPDATED_AREA_NAME)
            .orderIndex(UPDATED_ORDER_INDEX)
            .positionJson(UPDATED_POSITION_JSON)
            .screenId(UPDATED_SCREEN_ID)
            .timeRefresh(UPDATED_TIME_REFRESH)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UDPATE_TIME)
            .updateUser(UPDATED_UDPATE_USER);
        ConfigAreaDTO configAreaDTO = configAreaMapper.toDto(updatedConfigArea);

        restConfigAreaMockMvc.perform(put("/api/config-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configAreaDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigArea in the database
        List<ConfigArea> configAreaList = configAreaRepository.findAll();
        assertThat(configAreaList).hasSize(databaseSizeBeforeUpdate);
        ConfigArea testConfigArea = configAreaList.get(configAreaList.size() - 1);
        assertThat(testConfigArea.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testConfigArea.getAreaName()).isEqualTo(UPDATED_AREA_NAME);
        assertThat(testConfigArea.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigArea.getPositionJson()).isEqualTo(UPDATED_POSITION_JSON);
        assertThat(testConfigArea.getScreenId()).isEqualTo(UPDATED_SCREEN_ID);
        assertThat(testConfigArea.getTimeRefresh()).isEqualTo(UPDATED_TIME_REFRESH);
        assertThat(testConfigArea.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigArea.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigArea.getUpdateTime()).isEqualTo(UPDATED_UDPATE_TIME);
        assertThat(testConfigArea.getUpdateUser()).isEqualTo(UPDATED_UDPATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigArea() throws Exception {
        int databaseSizeBeforeUpdate = configAreaRepository.findAll().size();

        // Create the ConfigArea
        ConfigAreaDTO configAreaDTO = configAreaMapper.toDto(configArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigAreaMockMvc.perform(put("/api/config-areas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigArea in the database
        List<ConfigArea> configAreaList = configAreaRepository.findAll();
        assertThat(configAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigArea() throws Exception {
        // Initialize the database
        configAreaRepository.saveAndFlush(configArea);

        int databaseSizeBeforeDelete = configAreaRepository.findAll().size();

        // Delete the configArea
        restConfigAreaMockMvc.perform(delete("/api/config-areas/{id}", configArea.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigArea> configAreaList = configAreaRepository.findAll();
        assertThat(configAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
