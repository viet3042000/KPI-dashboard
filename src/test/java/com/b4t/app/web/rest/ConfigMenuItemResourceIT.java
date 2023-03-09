package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ConfigMenuItem;
import com.b4t.app.repository.ConfigMenuItemRepository;
import com.b4t.app.service.ConfigMenuItemService;
import com.b4t.app.service.dto.ConfigMenuItemDTO;
import com.b4t.app.service.mapper.ConfigMenuItemMapper;
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
 * Integration tests for the {@link ConfigMenuItemResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class ConfigMenuItemResourceIT {

    private static final String DEFAULT_MENU_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MENU_ITEM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_ITEM_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_IS_DEFAULT = 1L;
    private static final Long UPDATED_IS_DEFAULT = 2L;

    private static final Long DEFAULT_ORDER_INDEX = 1L;
    private static final Long UPDATED_ORDER_INDEX = 2L;

    private static final Long DEFAULT_MENU_ID = 1L;
    private static final Long UPDATED_MENU_ID = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ConfigMenuItemRepository configMenuItemRepository;

    @Autowired
    private ConfigMenuItemMapper configMenuItemMapper;

    @Autowired
    private ConfigMenuItemService configMenuItemService;

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

    private MockMvc restConfigMenuItemMockMvc;

    private ConfigMenuItem configMenuItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMenuItemResource configMenuItemResource = new ConfigMenuItemResource(configMenuItemService);
        this.restConfigMenuItemMockMvc = MockMvcBuilders.standaloneSetup(configMenuItemResource)
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
    public static ConfigMenuItem createEntity(EntityManager em) {
        ConfigMenuItem configMenuItem = new ConfigMenuItem()
            .menuItemCode(DEFAULT_MENU_ITEM_CODE)
            .menuItemName(DEFAULT_MENU_ITEM_NAME)
            .isDefault(DEFAULT_IS_DEFAULT)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .menuId(DEFAULT_MENU_ID)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return configMenuItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigMenuItem createUpdatedEntity(EntityManager em) {
        ConfigMenuItem configMenuItem = new ConfigMenuItem()
            .menuItemCode(UPDATED_MENU_ITEM_CODE)
            .menuItemName(UPDATED_MENU_ITEM_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .orderIndex(UPDATED_ORDER_INDEX)
            .menuId(UPDATED_MENU_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return configMenuItem;
    }

    @BeforeEach
    public void initTest() {
        configMenuItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigMenuItem() throws Exception {
        int databaseSizeBeforeCreate = configMenuItemRepository.findAll().size();

        // Create the ConfigMenuItem
        ConfigMenuItemDTO configMenuItemDTO = configMenuItemMapper.toDto(configMenuItem);
        restConfigMenuItemMockMvc.perform(post("/api/config-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfigMenuItem in the database
        List<ConfigMenuItem> configMenuItemList = configMenuItemRepository.findAll();
        assertThat(configMenuItemList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMenuItem testConfigMenuItem = configMenuItemList.get(configMenuItemList.size() - 1);
        assertThat(testConfigMenuItem.getMenuItemCode()).isEqualTo(DEFAULT_MENU_ITEM_CODE);
        assertThat(testConfigMenuItem.getMenuItemName()).isEqualTo(DEFAULT_MENU_ITEM_NAME);
        assertThat(testConfigMenuItem.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testConfigMenuItem.getOrderIndex()).isEqualTo(DEFAULT_ORDER_INDEX);
        assertThat(testConfigMenuItem.getMenuId()).isEqualTo(DEFAULT_MENU_ID);
        assertThat(testConfigMenuItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testConfigMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfigMenuItem.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testConfigMenuItem.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createConfigMenuItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMenuItemRepository.findAll().size();

        // Create the ConfigMenuItem with an existing ID
        configMenuItem.setId(1L);
        ConfigMenuItemDTO configMenuItemDTO = configMenuItemMapper.toDto(configMenuItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMenuItemMockMvc.perform(post("/api/config-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMenuItem in the database
        List<ConfigMenuItem> configMenuItemList = configMenuItemRepository.findAll();
        assertThat(configMenuItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfigMenuItems() throws Exception {
        // Initialize the database
        configMenuItemRepository.saveAndFlush(configMenuItem);

        // Get all the configMenuItemList
        restConfigMenuItemMockMvc.perform(get("/api/config-menu-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMenuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].menuItemCode").value(hasItem(DEFAULT_MENU_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].menuItemName").value(hasItem(DEFAULT_MENU_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].menuId").value(hasItem(DEFAULT_MENU_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getConfigMenuItem() throws Exception {
        // Initialize the database
        configMenuItemRepository.saveAndFlush(configMenuItem);

        // Get the configMenuItem
        restConfigMenuItemMockMvc.perform(get("/api/config-menu-items/{id}", configMenuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configMenuItem.getId().intValue()))
            .andExpect(jsonPath("$.menuItemCode").value(DEFAULT_MENU_ITEM_CODE))
            .andExpect(jsonPath("$.menuItemName").value(DEFAULT_MENU_ITEM_NAME))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX.intValue()))
            .andExpect(jsonPath("$.menuId").value(DEFAULT_MENU_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigMenuItem() throws Exception {
        // Get the configMenuItem
        restConfigMenuItemMockMvc.perform(get("/api/config-menu-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigMenuItem() throws Exception {
        // Initialize the database
        configMenuItemRepository.saveAndFlush(configMenuItem);

        int databaseSizeBeforeUpdate = configMenuItemRepository.findAll().size();

        // Update the configMenuItem
        ConfigMenuItem updatedConfigMenuItem = configMenuItemRepository.findById(configMenuItem.getId()).get();
        // Disconnect from session so that the updates on updatedConfigMenuItem are not directly saved in db
        em.detach(updatedConfigMenuItem);
        updatedConfigMenuItem
            .menuItemCode(UPDATED_MENU_ITEM_CODE)
            .menuItemName(UPDATED_MENU_ITEM_NAME)
            .isDefault(UPDATED_IS_DEFAULT)
            .orderIndex(UPDATED_ORDER_INDEX)
            .menuId(UPDATED_MENU_ID)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ConfigMenuItemDTO configMenuItemDTO = configMenuItemMapper.toDto(updatedConfigMenuItem);

        restConfigMenuItemMockMvc.perform(put("/api/config-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuItemDTO)))
            .andExpect(status().isOk());

        // Validate the ConfigMenuItem in the database
        List<ConfigMenuItem> configMenuItemList = configMenuItemRepository.findAll();
        assertThat(configMenuItemList).hasSize(databaseSizeBeforeUpdate);
        ConfigMenuItem testConfigMenuItem = configMenuItemList.get(configMenuItemList.size() - 1);
        assertThat(testConfigMenuItem.getMenuItemCode()).isEqualTo(UPDATED_MENU_ITEM_CODE);
        assertThat(testConfigMenuItem.getMenuItemName()).isEqualTo(UPDATED_MENU_ITEM_NAME);
        assertThat(testConfigMenuItem.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testConfigMenuItem.getOrderIndex()).isEqualTo(UPDATED_ORDER_INDEX);
        assertThat(testConfigMenuItem.getMenuId()).isEqualTo(UPDATED_MENU_ID);
        assertThat(testConfigMenuItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testConfigMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfigMenuItem.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testConfigMenuItem.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = configMenuItemRepository.findAll().size();

        // Create the ConfigMenuItem
        ConfigMenuItemDTO configMenuItemDTO = configMenuItemMapper.toDto(configMenuItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMenuItemMockMvc.perform(put("/api/config-menu-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(configMenuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMenuItem in the database
        List<ConfigMenuItem> configMenuItemList = configMenuItemRepository.findAll();
        assertThat(configMenuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfigMenuItem() throws Exception {
        // Initialize the database
        configMenuItemRepository.saveAndFlush(configMenuItem);

        int databaseSizeBeforeDelete = configMenuItemRepository.findAll().size();

        // Delete the configMenuItem
        restConfigMenuItemMockMvc.perform(delete("/api/config-menu-items/{id}", configMenuItem.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigMenuItem> configMenuItemList = configMenuItemRepository.findAll();
        assertThat(configMenuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
