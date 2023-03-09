package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigProfile;
import com.b4t.app.repository.ConfigProfileRepository;
import com.b4t.app.service.ConfigProfileService;
import com.b4t.app.service.dto.ConfigProfileDTO;
import com.b4t.app.service.mapper.ConfigProfileMapper;
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
 * Integration tests for the {@link ConfigProfileResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigProfileResourceIT {

    private static final String DEFAULT_PROFILE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_IS_DEFAULT = 1L;
    private static final Long UPDATED_IS_DEFAULT = 2L;

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final String DEFAULT_ROLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigProfileRepository configProfileRepository;

    @Autowired
    private ConfigProfileMapper configProfileMapper;

    @Autowired
    private ConfigProfileService configProfileService;

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

    private MockMvc restConfigProfileMockMvc;

    private ConfigProfile configProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigProfileResource configProfileResource = new ConfigProfileResource(configProfileService);
        this.restConfigProfileMockMvc = MockMvcBuilders.standaloneSetup(configProfileResource)
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
    public static ConfigProfile createEntity(EntityManager em) {
        ConfigProfile configProfile = new ConfigProfile()
            .profileCode(DEFAULT_PROFILE_CODE)
            .profileName(DEFAULT_PROFILE_NAME)
            .isDefault(DEFAULT_IS_DEFAULT)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .roleCode(DEFAULT_ROLE_CODE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigProfile createUpdatedEntity(EntityManager em) {
        ConfigProfile configProfile = new ConfigProfile()
            .profileCode(UPDATED_PROFILE_CODE)
            .profileName(UPDATED_PROFILE_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .orderIndex(UPDATED_ORDER_INDEX)
            .roleCode(UPDATED_ROLE_CODE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configProfile;
    }

    @BeforeEach
    public void initTest() {
        configProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigProfile() throws Exception {
        int databaseSizeBeforeCreate = configProfileRepository.findAll().size();

        // Create the ConfigProfile
        ConfigProfileDTO configProfileDTO = configProfileMapper.toDto(configProfile);
        restConfigProfileMockMvc.perform(post("/api/config-profiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigProfile in the database
        List<ConfigProfile> configProfileList = configProfileRepository.findAll();
        assertThat(configProfileList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigProfile testConfigProfile = configProfileList.get(configProfileList.size() - 1);
        assertThat(testConfigProfile.getProfileCode()).isEqualTo(DEFAULT_PROFILE_CODE);
        assertThat(testConfigProfile.getProfileName()).isEqualTo(DEFAULT_PROFILE_NAME);
        assertThat(testConfigProfile.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testConfigProfile.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigProfile.getRoleCode()).isEqualTo(DEFAULT_ROLE_CODE);
        assertThat(testConfigProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigProfile.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigProfile.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configProfileRepository.findAll().size();

        // Create the ConfigProfile with an existing ID
        configProfile.setId(1L);
        ConfigProfileDTO configProfileDTO = configProfileMapper.toDto(configProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigProfileMockMvc.perform(post("/api/config-profiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigProfile in the database
        List<ConfigProfile> configProfileList = configProfileRepository.findAll();
        assertThat(configProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigProfiles() throws Exception {
        // Initialize the database
        configProfileRepository.saveAndFlush(configProfile);

        // Get all the configProfileList
        restConfigProfileMockMvc.perform(get("/api/config-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileCode").value(hasItem(DEFAULT_PROFILE_CODE)))
            .andExpect(jsonPath("$.[*].profileName").value(hasItem(DEFAULT_PROFILE_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].roleCode").value(hasItem(DEFAULT_ROLE_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigProfile() throws Exception {
        // Initialize the database
        configProfileRepository.saveAndFlush(configProfile);

        // Get the configProfile
        restConfigProfileMockMvc.perform(get("/api/config-profiles/{id}", configProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configProfile.getId().intValue()))
            .andExpect(jsonPath("$.profileCode").value(DEFAULT_PROFILE_CODE))
            .andExpect(jsonPath("$.profileName").value(DEFAULT_PROFILE_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.roleCode").value(DEFAULT_ROLE_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigProfile() throws Exception {
        // Get the configProfile
        restConfigProfileMockMvc.perform(get("/api/config-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigProfile() throws Exception {
        // Initialize the database
        configProfileRepository.saveAndFlush(configProfile);

        int databaseSizeBeforeUpdate = configProfileRepository.findAll().size();

        // Update the configProfile
        ConfigProfile updatedConfigProfile = configProfileRepository.findById(configProfile.getId()).get();
        // Disconnect from session so that the updates on updatedConfigProfile are not directly saved in db
        em.detach(updatedConfigProfile);
        updatedConfigProfile
            .profileCode(UPDATED_PROFILE_CODE)
            .profileName(UPDATED_PROFILE_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .orderIndex(UPDATED_ORDER_INDEX)
            .roleCode(UPDATED_ROLE_CODE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigProfileDTO configProfileDTO = configProfileMapper.toDto(updatedConfigProfile);

        restConfigProfileMockMvc.perform(put("/api/config-profiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configProfileDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigProfile in the database
        List<ConfigProfile> configProfileList = configProfileRepository.findAll();
        assertThat(configProfileList).hasSize(databaseSizeBeforeUpdate);
        ConfigProfile testConfigProfile = configProfileList.get(configProfileList.size() - 1);
        assertThat(testConfigProfile.getProfileCode()).isEqualTo(UPDATED_PROFILE_CODE);
        assertThat(testConfigProfile.getProfileName()).isEqualTo(UPDATED_PROFILE_NAME);
        assertThat(testConfigProfile.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testConfigProfile.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigProfile.getRoleCode()).isEqualTo(UPDATED_ROLE_CODE);
        assertThat(testConfigProfile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigProfile.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigProfile.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigProfile() throws Exception {
        int databaseSizeBeforeUpdate = configProfileRepository.findAll().size();

        // Create the ConfigProfile
        ConfigProfileDTO configProfileDTO = configProfileMapper.toDto(configProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigProfileMockMvc.perform(put("/api/config-profiles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigProfile in the database
        List<ConfigProfile> configProfileList = configProfileRepository.findAll();
        assertThat(configProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigProfile() throws Exception {
        // Initialize the database
        configProfileRepository.saveAndFlush(configProfile);

        int databaseSizeBeforeDelete = configProfileRepository.findAll().size();

        // Delete the configProfile
        restConfigProfileMockMvc.perform(delete("/api/config-profiles/{id}", configProfile.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigProfile> configProfileList = configProfileRepository.findAll();
        assertThat(configProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
