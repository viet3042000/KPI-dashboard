package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigMenu;
import com.b4t.app.repository.ConfigMenuRepository;
import com.b4t.app.service.ConfigMenuService;
import com.b4t.app.service.dto.ConfigMenuDTO;
import com.b4t.app.service.mapper.ConfigMenuMapper;
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
 * Integration tests for the {@link ConfigMenuResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigMenuResourceIT {

    private static final String DEFAULT_MENU_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MENU_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigMenuRepository configMenuRepository;

    @Autowired
    private ConfigMenuMapper configMenuMapper;

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

    private MockMvc restConfigMenuMockMvc;

    private ConfigMenu configMenu;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMenuResource configMenuResource = new ConfigMenuResource();
        this.restConfigMenuMockMvc = MockMvcBuilders.standaloneSetup(configMenuResource)
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
    public static ConfigMenu createEntity(EntityManager em) {
        ConfigMenu configMenu = new ConfigMenu()
            .menuCode(DEFAULT_MENU_CODE)
            .menuName(DEFAULT_MENU_NAME)
            .domainCode(DEFAULT_DOMAIN_CODE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configMenu;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigMenu createUpdatedEntity(EntityManager em) {
        ConfigMenu configMenu = new ConfigMenu()
            .menuCode(UPDATED_MENU_CODE)
            .menuName(UPDATED_MENU_NAME)
            .domainCode(UPDATED_DOMAIN_CODE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configMenu;
    }

    @BeforeEach
    public void initTest() {
        configMenu = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigMenu() throws Exception {
        int databaseSizeBeforeCreate = configMenuRepository.findAll().size();

        // Create the ConfigMenu
        ConfigMenuDTO configMenuDTO = configMenuMapper.toDto(configMenu);
        restConfigMenuMockMvc.perform(post("/api/config-menus")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigMenu in the database
        List<ConfigMenu> configMenuList = configMenuRepository.findAll();
        assertThat(configMenuList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMenu testConfigMenu = configMenuList.get(configMenuList.size() - 1);
        assertThat(testConfigMenu.getMenuCode()).isEqualTo(DEFAULT_MENU_CODE);
        assertThat(testConfigMenu.getMenuName()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testConfigMenu.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
        assertThat(testConfigMenu.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigMenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigMenu.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigMenu.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigMenuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMenuRepository.findAll().size();

        // Create the ConfigMenu with an existing ID
        configMenu.setId(1L);
        ConfigMenuDTO configMenuDTO = configMenuMapper.toDto(configMenu);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMenuMockMvc.perform(post("/api/config-menus")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMenu in the database
        List<ConfigMenu> configMenuList = configMenuRepository.findAll();
        assertThat(configMenuList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigMenus() throws Exception {
        // Initialize the database
        configMenuRepository.saveAndFlush(configMenu);

        // Get all the configMenuList
        restConfigMenuMockMvc.perform(get("/api/config-menus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].menuCode").value(hasItem(DEFAULT_MENU_CODE)))
            .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME)))
            .andExpect(jsonPath("$.[*].domainCode").value(hasItem(DEFAULT_DOMAIN_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigMenu() throws Exception {
        // Initialize the database
        configMenuRepository.saveAndFlush(configMenu);

        // Get the configMenu
        restConfigMenuMockMvc.perform(get("/api/config-menus/{id}", configMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configMenu.getId().intValue()))
            .andExpect(jsonPath("$.menuCode").value(DEFAULT_MENU_CODE))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME))
            .andExpect(jsonPath("$.domainCode").value(DEFAULT_DOMAIN_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigMenu() throws Exception {
        // Get the configMenu
        restConfigMenuMockMvc.perform(get("/api/config-menus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigMenu() throws Exception {
        // Initialize the database
        configMenuRepository.saveAndFlush(configMenu);

        int databaseSizeBeforeUpdate = configMenuRepository.findAll().size();

        // Update the configMenu
        ConfigMenu updatedConfigMenu = configMenuRepository.findById(configMenu.getId()).get();
        // Disconnect from session so that the updates on updatedConfigMenu are not directly saved in db
        em.detach(updatedConfigMenu);
        updatedConfigMenu
            .menuCode(UPDATED_MENU_CODE)
            .menuName(UPDATED_MENU_NAME)
            .domainCode(UPDATED_DOMAIN_CODE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigMenuDTO configMenuDTO = configMenuMapper.toDto(updatedConfigMenu);

        restConfigMenuMockMvc.perform(put("/api/config-menus")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigMenu in the database
        List<ConfigMenu> configMenuList = configMenuRepository.findAll();
        assertThat(configMenuList).hasSize(databaseSizeBeforeUpdate);
        ConfigMenu testConfigMenu = configMenuList.get(configMenuList.size() - 1);
        assertThat(testConfigMenu.getMenuCode()).isEqualTo(UPDATED_MENU_CODE);
        assertThat(testConfigMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testConfigMenu.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
        assertThat(testConfigMenu.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigMenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigMenu.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigMenu.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigMenu() throws Exception {
        int databaseSizeBeforeUpdate = configMenuRepository.findAll().size();

        // Create the ConfigMenu
        ConfigMenuDTO configMenuDTO = configMenuMapper.toDto(configMenu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMenuMockMvc.perform(put("/api/config-menus")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMenu in the database
        List<ConfigMenu> configMenuList = configMenuRepository.findAll();
        assertThat(configMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigMenu() throws Exception {
        // Initialize the database
        configMenuRepository.saveAndFlush(configMenu);

        int databaseSizeBeforeDelete = configMenuRepository.findAll().size();

        // Delete the configMenu
        restConfigMenuMockMvc.perform(delete("/api/config-menus/{id}", configMenu.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigMenu> configMenuList = configMenuRepository.findAll();
        assertThat(configMenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
