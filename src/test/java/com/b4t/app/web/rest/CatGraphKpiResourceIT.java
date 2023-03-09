package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.CatGraphKpi;
import com.b4t.app.repository.CatGraphKpiRepository;
import com.b4t.app.service.CatGraphKpiService;
import com.b4t.app.service.dto.CatGraphKpiDTO;
import com.b4t.app.service.mapper.CatGraphKpiMapper;
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
 * Integration tests for the {@link CatGraphKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class CatGraphKpiResourceIT {

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

    private static final String DEFAULT_KPI_CODE = "AAAAAAAAAA";
    private static final String UPDATED_KPI_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_KPI_NAME = "AAAAAAAAAA";
    private static final String UPDATED_KPI_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KPI_DISPLAY = "AAAAAAAAAA";
    private static final String UPDATED_KPI_DISPLAY = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_KPI = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_KPI = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_VIEW_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_VIEW_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1.0;
    private static final Double UPDATED_RATE = 2.2;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final String DEFAULT_GROUP_KPI_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_KPI_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private CatGraphKpiRepository catGraphKpiRepository;

    @Autowired
    private CatGraphKpiMapper catGraphKpiMapper;

    @Autowired
    private CatGraphKpiService catGraphKpiService;

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

    private MockMvc restCatGraphKpiMockMvc;

    private CatGraphKpi catGraphKpi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CatGraphKpiResource catGraphKpiResource = new CatGraphKpiResource(catGraphKpiService);
        this.restCatGraphKpiMockMvc = MockMvcBuilders.standaloneSetup(catGraphKpiResource)
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
    public static CatGraphKpi createEntity(EntityManager em) {
        CatGraphKpi catGraphKpi = new CatGraphKpi()
            .kpiId(DEFAULT_KPI_ID)
            .kpiCode(DEFAULT_KPI_CODE)
            .kpiName(DEFAULT_KPI_NAME)
            .kpiDisplay(DEFAULT_KPI_DISPLAY)
            .unitKpi(DEFAULT_UNIT_KPI)
            .unitViewCode(DEFAULT_UNIT_VIEW_CODE)
            .rate(DEFAULT_RATE)
            .status(DEFAULT_STATUS)
            .groupKpiCode(DEFAULT_GROUP_KPI_CODE)
            .domainCode(DEFAULT_DOMAIN_CODE)
            .source(DEFAULT_SOURCE)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return catGraphKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatGraphKpi createUpdatedEntity(EntityManager em) {
        CatGraphKpi catGraphKpi = new CatGraphKpi()
            .kpiId(UPDATED_KPI_ID)
            .kpiCode(UPDATED_KPI_CODE)
            .kpiName(UPDATED_KPI_NAME)
            .kpiDisplay(UPDATED_KPI_DISPLAY)
            .unitKpi(UPDATED_UNIT_KPI)
            .unitViewCode(UPDATED_UNIT_VIEW_CODE)
            .rate(UPDATED_RATE)
            .status(UPDATED_STATUS)
            .groupKpiCode(UPDATED_GROUP_KPI_CODE)
            .domainCode(UPDATED_DOMAIN_CODE)
            .source(UPDATED_SOURCE)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return catGraphKpi;
    }

    @BeforeEach
    public void initTest() {
        catGraphKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatGraphKpi() throws Exception {
        int databaseSizeBeforeCreate = catGraphKpiRepository.findAll().size();

        // Create the CatGraphKpi
        CatGraphKpiDTO catGraphKpiDTO = catGraphKpiMapper.toDto(catGraphKpi);
        restCatGraphKpiMockMvc.perform(post("/api/cat-graph-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the CatGraphKpi in the database
        List<CatGraphKpi> catGraphKpiList = catGraphKpiRepository.findAll();
        assertThat(catGraphKpiList).hasSize(databaseSizeBeforeCreate + 1);
        CatGraphKpi testCatGraphKpi = catGraphKpiList.get(catGraphKpiList.size() - 1);
        assertThat(testCatGraphKpi.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testCatGraphKpi.getKpiCode()).isEqualTo(DEFAULT_KPI_CODE);
        assertThat(testCatGraphKpi.getKpiName()).isEqualTo(DEFAULT_KPI_NAME);
        assertThat(testCatGraphKpi.getKpiDisplay()).isEqualTo(DEFAULT_KPI_DISPLAY);
        assertThat(testCatGraphKpi.getUnitKpi()).isEqualTo(DEFAULT_UNIT_KPI);
        assertThat(testCatGraphKpi.getUnitViewCode()).isEqualTo(DEFAULT_UNIT_VIEW_CODE);
        assertThat(testCatGraphKpi.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testCatGraphKpi.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCatGraphKpi.getGroupKpiCode()).isEqualTo(DEFAULT_GROUP_KPI_CODE);
        assertThat(testCatGraphKpi.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
        assertThat(testCatGraphKpi.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testCatGraphKpi.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatGraphKpi.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testCatGraphKpi.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createCatGraphKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catGraphKpiRepository.findAll().size();

        // Create the CatGraphKpi with an existing ID
        catGraphKpi.setId(1L);
        CatGraphKpiDTO catGraphKpiDTO = catGraphKpiMapper.toDto(catGraphKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatGraphKpiMockMvc.perform(post("/api/cat-graph-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatGraphKpi in the database
        List<CatGraphKpi> catGraphKpiList = catGraphKpiRepository.findAll();
        assertThat(catGraphKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCatGraphKpis() throws Exception {
        // Initialize the database
        catGraphKpiRepository.saveAndFlush(catGraphKpi);

        // Get all the catGraphKpiList
        restCatGraphKpiMockMvc.perform(get("/api/cat-graph-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catGraphKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiCode").value(hasItem(DEFAULT_KPI_CODE)))
            .andExpect(jsonPath("$.[*].kpiName").value(hasItem(DEFAULT_KPI_NAME)))
            .andExpect(jsonPath("$.[*].kpiDisplay").value(hasItem(DEFAULT_KPI_DISPLAY)))
            .andExpect(jsonPath("$.[*].unitKpi").value(hasItem(DEFAULT_UNIT_KPI)))
            .andExpect(jsonPath("$.[*].unitViewCode").value(hasItem(DEFAULT_UNIT_VIEW_CODE)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].groupKpiCode").value(hasItem(DEFAULT_GROUP_KPI_CODE)))
            .andExpect(jsonPath("$.[*].domainCode").value(hasItem(DEFAULT_DOMAIN_CODE)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getCatGraphKpi() throws Exception {
        // Initialize the database
        catGraphKpiRepository.saveAndFlush(catGraphKpi);

        // Get the catGraphKpi
        restCatGraphKpiMockMvc.perform(get("/api/cat-graph-kpis/{id}", catGraphKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catGraphKpi.getId().intValue()))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID.intValue()))
            .andExpect(jsonPath("$.kpiCode").value(DEFAULT_KPI_CODE))
            .andExpect(jsonPath("$.kpiName").value(DEFAULT_KPI_NAME))
            .andExpect(jsonPath("$.kpiDisplay").value(DEFAULT_KPI_DISPLAY))
            .andExpect(jsonPath("$.unitKpi").value(DEFAULT_UNIT_KPI))
            .andExpect(jsonPath("$.unitViewCode").value(DEFAULT_UNIT_VIEW_CODE))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.groupKpiCode").value(DEFAULT_GROUP_KPI_CODE))
            .andExpect(jsonPath("$.domainCode").value(DEFAULT_DOMAIN_CODE))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingCatGraphKpi() throws Exception {
        // Get the catGraphKpi
        restCatGraphKpiMockMvc.perform(get("/api/cat-graph-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatGraphKpi() throws Exception {
        // Initialize the database
        catGraphKpiRepository.saveAndFlush(catGraphKpi);

        int databaseSizeBeforeUpdate = catGraphKpiRepository.findAll().size();

        // Update the catGraphKpi
        CatGraphKpi updatedCatGraphKpi = catGraphKpiRepository.findById(catGraphKpi.getId()).get();
        // Disconnect from session so that the updates on updatedCatGraphKpi are not directly saved in db
        em.detach(updatedCatGraphKpi);
        updatedCatGraphKpi
            .kpiId(UPDATED_KPI_ID)
            .kpiCode(UPDATED_KPI_CODE)
            .kpiName(UPDATED_KPI_NAME)
            .kpiDisplay(UPDATED_KPI_DISPLAY)
            .unitKpi(UPDATED_UNIT_KPI)
            .unitViewCode(UPDATED_UNIT_VIEW_CODE)
            .rate(UPDATED_RATE)
            .status(UPDATED_STATUS)
            .groupKpiCode(UPDATED_GROUP_KPI_CODE)
            .domainCode(UPDATED_DOMAIN_CODE)
            .source(UPDATED_SOURCE)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        CatGraphKpiDTO catGraphKpiDTO = catGraphKpiMapper.toDto(updatedCatGraphKpi);

        restCatGraphKpiMockMvc.perform(put("/api/cat-graph-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiDTO)))
            .andExpect(status().isOk());

        // Validate the CatGraphKpi in the database
        List<CatGraphKpi> catGraphKpiList = catGraphKpiRepository.findAll();
        assertThat(catGraphKpiList).hasSize(databaseSizeBeforeUpdate);
        CatGraphKpi testCatGraphKpi = catGraphKpiList.get(catGraphKpiList.size() - 1);
        assertThat(testCatGraphKpi.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testCatGraphKpi.getKpiCode()).isEqualTo(UPDATED_KPI_CODE);
        assertThat(testCatGraphKpi.getKpiName()).isEqualTo(UPDATED_KPI_NAME);
        assertThat(testCatGraphKpi.getKpiDisplay()).isEqualTo(UPDATED_KPI_DISPLAY);
        assertThat(testCatGraphKpi.getUnitKpi()).isEqualTo(UPDATED_UNIT_KPI);
        assertThat(testCatGraphKpi.getUnitViewCode()).isEqualTo(UPDATED_UNIT_VIEW_CODE);
        assertThat(testCatGraphKpi.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testCatGraphKpi.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCatGraphKpi.getGroupKpiCode()).isEqualTo(UPDATED_GROUP_KPI_CODE);
        assertThat(testCatGraphKpi.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
        assertThat(testCatGraphKpi.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testCatGraphKpi.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatGraphKpi.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testCatGraphKpi.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingCatGraphKpi() throws Exception {
        int databaseSizeBeforeUpdate = catGraphKpiRepository.findAll().size();

        // Create the CatGraphKpi
        CatGraphKpiDTO catGraphKpiDTO = catGraphKpiMapper.toDto(catGraphKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatGraphKpiMockMvc.perform(put("/api/cat-graph-kpis")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatGraphKpi in the database
        List<CatGraphKpi> catGraphKpiList = catGraphKpiRepository.findAll();
        assertThat(catGraphKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCatGraphKpi() throws Exception {
        // Initialize the database
        catGraphKpiRepository.saveAndFlush(catGraphKpi);

        int databaseSizeBeforeDelete = catGraphKpiRepository.findAll().size();

        // Delete the catGraphKpi
        restCatGraphKpiMockMvc.perform(delete("/api/cat-graph-kpis/{id}", catGraphKpi.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatGraphKpi> catGraphKpiList = catGraphKpiRepository.findAll();
        assertThat(catGraphKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
