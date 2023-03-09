package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.CatGroupChart;
import com.b4t.app.repository.CatGroupChartRepository;
import com.b4t.app.service.CatGroupChartService;
import com.b4t.app.service.dto.CatGroupChartDTO;
import com.b4t.app.service.mapper.CatGroupChartMapper;
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
 * Integration tests for the {@link CatGroupChartResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class CatGroupChartResourceIT {

    private static final String DEFAULT_GROUP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_KPI_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_KPI_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private CatGroupChartRepository catGroupChartRepository;

    @Autowired
    private CatGroupChartMapper catGroupChartMapper;

    @Autowired
    private CatGroupChartService catGroupChartService;

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

    private MockMvc restCatGroupChartMockMvc;

    private CatGroupChart catGroupChart;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CatGroupChartResource catGroupChartResource = new CatGroupChartResource(catGroupChartService);
        this.restCatGroupChartMockMvc = MockMvcBuilders.standaloneSetup(catGroupChartResource)
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
    public static CatGroupChart createEntity(EntityManager em) {
        CatGroupChart catGroupChart = new CatGroupChart()
            .groupCode(DEFAULT_GROUP_CODE)
            .groupName(DEFAULT_GROUP_NAME)
            .groupKpiCode(DEFAULT_GROUP_KPI_CODE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return catGroupChart;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatGroupChart createUpdatedEntity(EntityManager em) {
        CatGroupChart catGroupChart = new CatGroupChart()
            .groupCode(UPDATED_GROUP_CODE)
            .groupName(UPDATED_GROUP_NAME)
            .groupKpiCode(UPDATED_GROUP_KPI_CODE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return catGroupChart;
    }

    @BeforeEach
    public void initTest() {
        catGroupChart = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatGroupChart() throws Exception {
        int databaseSizeBeforeCreate = catGroupChartRepository.findAll().size();

        // Create the CatGroupChart
        CatGroupChartDTO catGroupChartDTO = catGroupChartMapper.toDto(catGroupChart);
        restCatGroupChartMockMvc.perform(post("/api/cat-group-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGroupChartDTO)))
            .andExpect(status().isCreated());

        // Validate the CatGroupChart in the database
        List<CatGroupChart> catGroupChartList = catGroupChartRepository.findAll();
        assertThat(catGroupChartList).hasSize(databaseSizeBeforeCreate + 1);
        CatGroupChart testCatGroupChart = catGroupChartList.get(catGroupChartList.size() - 1);
        assertThat(testCatGroupChart.getGroupCode()).isEqualTo(DEFAULT_GROUP_CODE);
        assertThat(testCatGroupChart.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testCatGroupChart.getGroupKpiCode()).isEqualTo(DEFAULT_GROUP_KPI_CODE);
        assertThat(testCatGroupChart.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatGroupChart.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCatGroupChart.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testCatGroupChart.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createCatGroupChartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catGroupChartRepository.findAll().size();

        // Create the CatGroupChart with an existing ID
        catGroupChart.setId(1L);
        CatGroupChartDTO catGroupChartDTO = catGroupChartMapper.toDto(catGroupChart);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatGroupChartMockMvc.perform(post("/api/cat-group-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGroupChartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatGroupChart in the database
        List<CatGroupChart> catGroupChartList = catGroupChartRepository.findAll();
        assertThat(catGroupChartList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCatGroupCharts() throws Exception {
        // Initialize the database
        catGroupChartRepository.saveAndFlush(catGroupChart);

        // Get all the catGroupChartList
        restCatGroupChartMockMvc.perform(get("/api/cat-group-charts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catGroupChart.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupCode").value(hasItem(DEFAULT_GROUP_CODE)))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].groupKpiCode").value(hasItem(DEFAULT_GROUP_KPI_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getCatGroupChart() throws Exception {
        // Initialize the database
        catGroupChartRepository.saveAndFlush(catGroupChart);

        // Get the catGroupChart
        restCatGroupChartMockMvc.perform(get("/api/cat-group-charts/{id}", catGroupChart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catGroupChart.getId().intValue()))
            .andExpect(jsonPath("$.groupCode").value(DEFAULT_GROUP_CODE))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.groupKpiCode").value(DEFAULT_GROUP_KPI_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingCatGroupChart() throws Exception {
        // Get the catGroupChart
        restCatGroupChartMockMvc.perform(get("/api/cat-group-charts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatGroupChart() throws Exception {
        // Initialize the database
        catGroupChartRepository.saveAndFlush(catGroupChart);

        int databaseSizeBeforeUpdate = catGroupChartRepository.findAll().size();

        // Update the catGroupChart
        CatGroupChart updatedCatGroupChart = catGroupChartRepository.findById(catGroupChart.getId()).get();
        // Disconnect from session so that the updates on updatedCatGroupChart are not directly saved in db
        em.detach(updatedCatGroupChart);
        updatedCatGroupChart
            .groupCode(UPDATED_GROUP_CODE)
            .groupName(UPDATED_GROUP_NAME)
            .groupKpiCode(UPDATED_GROUP_KPI_CODE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        CatGroupChartDTO catGroupChartDTO = catGroupChartMapper.toDto(updatedCatGroupChart);

        restCatGroupChartMockMvc.perform(put("/api/cat-group-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGroupChartDTO)))
            .andExpect(status().isOk());

        // Validate the CatGroupChart in the database
        List<CatGroupChart> catGroupChartList = catGroupChartRepository.findAll();
        assertThat(catGroupChartList).hasSize(databaseSizeBeforeUpdate);
        CatGroupChart testCatGroupChart = catGroupChartList.get(catGroupChartList.size() - 1);
        assertThat(testCatGroupChart.getGroupCode()).isEqualTo(UPDATED_GROUP_CODE);
        assertThat(testCatGroupChart.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testCatGroupChart.getGroupKpiCode()).isEqualTo(UPDATED_GROUP_KPI_CODE);
        assertThat(testCatGroupChart.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatGroupChart.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCatGroupChart.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testCatGroupChart.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingCatGroupChart() throws Exception {
        int databaseSizeBeforeUpdate = catGroupChartRepository.findAll().size();

        // Create the CatGroupChart
        CatGroupChartDTO catGroupChartDTO = catGroupChartMapper.toDto(catGroupChart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatGroupChartMockMvc.perform(put("/api/cat-group-charts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGroupChartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatGroupChart in the database
        List<CatGroupChart> catGroupChartList = catGroupChartRepository.findAll();
        assertThat(catGroupChartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCatGroupChart() throws Exception {
        // Initialize the database
        catGroupChartRepository.saveAndFlush(catGroupChart);

        int databaseSizeBeforeDelete = catGroupChartRepository.findAll().size();

        // Delete the catGroupChart
        restCatGroupChartMockMvc.perform(delete("/api/cat-group-charts/{id}", catGroupChart.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatGroupChart> catGroupChartList = catGroupChartRepository.findAll();
        assertThat(catGroupChartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
