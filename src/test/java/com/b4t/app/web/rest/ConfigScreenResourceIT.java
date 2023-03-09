package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigScreen;
import com.b4t.app.repository.ConfigScreenRepository;
import com.b4t.app.service.ConfigAreaService;
import com.b4t.app.service.ConfigMenuItemService;
import com.b4t.app.service.ConfigScreenService;
import com.b4t.app.service.dto.ConfigScreenDTO;
import com.b4t.app.service.mapper.ConfigScreenMapper;
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
 * Integration tests for the {@link ConfigScreenResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigScreenResourceIT {

    private static final String DEFAULT_SCREEN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SCREEN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SCREEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCREEN_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_IS_DEFAULT = 1L;
    private static final Long UPDATED_IS_DEFAULT = 2L;

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final Long DEFAULT_PROFILE_ID = 1L;
    private static final Long UPDATED_PROFILE_ID = 2L;

    private static final Long DEFAULT_MENU_ITEM_ID = 1L;
    private static final Long UPDATED_MENU_ITEM_ID = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigScreenRepository configScreenRepository;

    @Autowired
    private ConfigScreenMapper configScreenMapper;

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

    private MockMvc restConfigScreenMockMvc;

    private ConfigScreen configScreen;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigScreenResource configScreenResource = new ConfigScreenResource();
        this.restConfigScreenMockMvc = MockMvcBuilders.standaloneSetup(configScreenResource)
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
    public static ConfigScreen createEntity(EntityManager em) {
        ConfigScreen configScreen = new ConfigScreen()
            .screenCode(DEFAULT_SCREEN_CODE)
            .screenName(DEFAULT_SCREEN_NAME)
            .isDefault(DEFAULT_IS_DEFAULT)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .profileId(DEFAULT_PROFILE_ID)
            .menuItemId(DEFAULT_MENU_ITEM_ID)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configScreen;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigScreen createUpdatedEntity(EntityManager em) {
        ConfigScreen configScreen = new ConfigScreen()
            .screenCode(UPDATED_SCREEN_CODE)
            .screenName(UPDATED_SCREEN_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .orderIndex(UPDATED_ORDER_INDEX)
            .profileId(UPDATED_PROFILE_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configScreen;
    }

    @BeforeEach
    public void initTest() {
        configScreen = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigScreen() throws Exception {
        int databaseSizeBeforeCreate = configScreenRepository.findAll().size();

        // Create the ConfigScreen
        ConfigScreenDTO configScreenDTO = configScreenMapper.toDto(configScreen);
        restConfigScreenMockMvc.perform(post("/api/config-screens")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configScreenDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigScreen in the database
        List<ConfigScreen> configScreenList = configScreenRepository.findAll();
        assertThat(configScreenList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigScreen testConfigScreen = configScreenList.get(configScreenList.size() - 1);
        assertThat(testConfigScreen.getScreenCode()).isEqualTo(DEFAULT_SCREEN_CODE);
        assertThat(testConfigScreen.getScreenName()).isEqualTo(DEFAULT_SCREEN_NAME);
        assertThat(testConfigScreen.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testConfigScreen.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigScreen.getProfileId()).isEqualTo(DEFAULT_PROFILE_ID);
        assertThat(testConfigScreen.getMenuItemId()).isEqualTo(DEFAULT_MENU_ITEM_ID);
        assertThat(testConfigScreen.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigScreen.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigScreen.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigScreen.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigScreenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configScreenRepository.findAll().size();

        // Create the ConfigScreen with an existing ID
        configScreen.setId(1L);
        ConfigScreenDTO configScreenDTO = configScreenMapper.toDto(configScreen);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigScreenMockMvc.perform(post("/api/config-screens")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configScreenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigScreen in the database
        List<ConfigScreen> configScreenList = configScreenRepository.findAll();
        assertThat(configScreenList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigScreens() throws Exception {
        // Initialize the database
        configScreenRepository.saveAndFlush(configScreen);

        // Get all the configScreenList
        restConfigScreenMockMvc.perform(get("/api/config-screens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configScreen.getId().intValue())))
            .andExpect(jsonPath("$.[*].screenCode").value(hasItem(DEFAULT_SCREEN_CODE)))
            .andExpect(jsonPath("$.[*].screenName").value(hasItem(DEFAULT_SCREEN_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].profileId").value(hasItem(DEFAULT_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuItemId").value(hasItem(DEFAULT_MENU_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigScreen() throws Exception {
        // Initialize the database
        configScreenRepository.saveAndFlush(configScreen);

        // Get the configScreen
        restConfigScreenMockMvc.perform(get("/api/config-screens/{id}", configScreen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configScreen.getId().intValue()))
            .andExpect(jsonPath("$.screenCode").value(DEFAULT_SCREEN_CODE))
            .andExpect(jsonPath("$.screenName").value(DEFAULT_SCREEN_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.profileId").value(DEFAULT_PROFILE_ID.intValue()))
            .andExpect(jsonPath("$.menuItemId").value(DEFAULT_MENU_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigScreen() throws Exception {
        // Get the configScreen
        restConfigScreenMockMvc.perform(get("/api/config-screens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigScreen() throws Exception {
        // Initialize the database
        configScreenRepository.saveAndFlush(configScreen);

        int databaseSizeBeforeUpdate = configScreenRepository.findAll().size();

        // Update the configScreen
        ConfigScreen updatedConfigScreen = configScreenRepository.findById(configScreen.getId()).get();
        // Disconnect from session so that the updates on updatedConfigScreen are not directly saved in db
        em.detach(updatedConfigScreen);
        updatedConfigScreen
            .screenCode(UPDATED_SCREEN_CODE)
            .screenName(UPDATED_SCREEN_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .orderIndex(UPDATED_ORDER_INDEX)
            .profileId(UPDATED_PROFILE_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigScreenDTO configScreenDTO = configScreenMapper.toDto(updatedConfigScreen);

        restConfigScreenMockMvc.perform(put("/api/config-screens")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configScreenDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigScreen in the database
        List<ConfigScreen> configScreenList = configScreenRepository.findAll();
        assertThat(configScreenList).hasSize(databaseSizeBeforeUpdate);
        ConfigScreen testConfigScreen = configScreenList.get(configScreenList.size() - 1);
        assertThat(testConfigScreen.getScreenCode()).isEqualTo(UPDATED_SCREEN_CODE);
        assertThat(testConfigScreen.getScreenName()).isEqualTo(UPDATED_SCREEN_NAME);
        assertThat(testConfigScreen.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testConfigScreen.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigScreen.getProfileId()).isEqualTo(UPDATED_PROFILE_ID);
        assertThat(testConfigScreen.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testConfigScreen.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigScreen.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigScreen.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigScreen.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigScreen() throws Exception {
        int databaseSizeBeforeUpdate = configScreenRepository.findAll().size();

        // Create the ConfigScreen
        ConfigScreenDTO configScreenDTO = configScreenMapper.toDto(configScreen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigScreenMockMvc.perform(put("/api/config-screens")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configScreenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigScreen in the database
        List<ConfigScreen> configScreenList = configScreenRepository.findAll();
        assertThat(configScreenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigScreen() throws Exception {
        // Initialize the database
        configScreenRepository.saveAndFlush(configScreen);

        int databaseSizeBeforeDelete = configScreenRepository.findAll().size();

        // Delete the configScreen
        restConfigScreenMockMvc.perform(delete("/api/config-screens/{id}", configScreen.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigScreen> configScreenList = configScreenRepository.findAll();
        assertThat(configScreenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
