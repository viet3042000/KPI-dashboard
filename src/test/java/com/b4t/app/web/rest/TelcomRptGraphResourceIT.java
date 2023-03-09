package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.TelcomRptGraph;
import com.b4t.app.repository.TelcomRptGraphRepository;
import com.b4t.app.service.TelcomRptGraphService;
import com.b4t.app.service.dto.TelcomRptGraphDTO;
import com.b4t.app.service.mapper.TelcomRptGraphMapper;
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
import java.util.List;

import static com.b4t.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TelcomRptGraphResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class TelcomRptGraphResourceIT {

    private static final Long DEFAULT_TIME_TYPE = 1L;
    private static final Long UPDATED_TIME_TYPE = 2L;

    private static final Long DEFAULT_PRD_ID = 1L;
    private static final Long UPDATED_PRD_ID = 2L;

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

    private static final String DEFAULT_KPI_CODE = "AAAAAAAAAA";
    private static final String UPDATED_KPI_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_KPI_NAME = "AAAAAAAAAA";
    private static final String UPDATED_KPI_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OBJ_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OBJ_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJ_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OBJ_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_INPUT_LEVEL = 1L;
    private static final Long UPDATED_INPUT_LEVEL = 2L;

    private static final Long DEFAULT_VAL_PLAN = 1L;
    private static final Long UPDATED_VAL_PLAN = 2L;

    private static final Long DEFAULT_VAL_PLAN_MON = 1L;
    private static final Long UPDATED_VAL_PLAN_MON = 2L;

    private static final Long DEFAULT_VAL_PLAN_QUAR = 1L;
    private static final Long UPDATED_VAL_PLAN_QUAR = 2L;

    private static final Long DEFAULT_VAL_PLAN_YEAR = 1L;
    private static final Long UPDATED_VAL_PLAN_YEAR = 2L;

    private static final Long DEFAULT_VAL = 1L;
    private static final Long UPDATED_VAL = 2L;

    private static final Long DEFAULT_VAL_MON = 1L;
    private static final Long UPDATED_VAL_MON = 2L;

    private static final Long DEFAULT_VAL_QUAR = 1L;
    private static final Long UPDATED_VAL_QUAR = 2L;

    private static final Long DEFAULT_VAL_YEAR = 1L;
    private static final Long UPDATED_VAL_YEAR = 2L;

    private static final Long DEFAULT_VAL_LAST_MON = 1L;
    private static final Long UPDATED_VAL_LAST_MON = 2L;

    private static final Long DEFAULT_VAL_LAST_QUAR = 1L;
    private static final Long UPDATED_VAL_LAST_QUAR = 2L;

    private static final Long DEFAULT_VAL_LAST_YEAR = 1L;
    private static final Long UPDATED_VAL_LAST_YEAR = 2L;

    private static final Long DEFAULT_VAL_DELTA = 1L;
    private static final Long UPDATED_VAL_DELTA = 2L;

    private static final Long DEFAULT_VAL_DELTA_MON = 1L;
    private static final Long UPDATED_VAL_DELTA_MON = 2L;

    private static final Long DEFAULT_VAL_DELTA_QUAR = 1L;
    private static final Long UPDATED_VAL_DELTA_QUAR = 2L;

    private static final Long DEFAULT_VAL_DELTA_YEAR = 1L;
    private static final Long UPDATED_VAL_DELTA_YEAR = 2L;

    private static final Long DEFAULT_PERCENT_PLAN = 1L;
    private static final Long UPDATED_PERCENT_PLAN = 2L;

    private static final Long DEFAULT_PERCENT_PLAN_MON = 1L;
    private static final Long UPDATED_PERCENT_PLAN_MON = 2L;

    private static final Long DEFAULT_PERCENT_PLAN_QUAR = 1L;
    private static final Long UPDATED_PERCENT_PLAN_QUAR = 2L;

    private static final Long DEFAULT_PERCENT_PLAN_YEAR = 1L;
    private static final Long UPDATED_PERCENT_PLAN_YEAR = 2L;

    private static final Long DEFAULT_PERCENT_GROW = 1L;
    private static final Long UPDATED_PERCENT_GROW = 2L;

    private static final Long DEFAULT_PERCENT_GROW_MON = 1L;
    private static final Long UPDATED_PERCENT_GROW_MON = 2L;

    private static final Long DEFAULT_PERCENT_GROW_QUAR = 1L;
    private static final Long UPDATED_PERCENT_GROW_QUAR = 2L;

    private static final Long DEFAULT_PERCENT_GROW_YEAR = 1L;
    private static final Long UPDATED_PERCENT_GROW_YEAR = 2L;

    private static final String DEFAULT_COLOR_ALARM = "AAAAAAAAAA";
    private static final String UPDATED_COLOR_ALARM = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_CODE = "BBBBBBBBBB";

    @Autowired
    private TelcomRptGraphRepository telcomRptGraphRepository;

    @Autowired
    private TelcomRptGraphMapper telcomRptGraphMapper;

    @Autowired
    private TelcomRptGraphService telcomRptGraphService;

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

    private MockMvc restTelcomRptGraphMockMvc;

    private TelcomRptGraph telcomRptGraph;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TelcomRptGraphResource telcomRptGraphResource = new TelcomRptGraphResource(telcomRptGraphService);
        this.restTelcomRptGraphMockMvc = MockMvcBuilders.standaloneSetup(telcomRptGraphResource)
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
    public static TelcomRptGraph createEntity(EntityManager em) {
        TelcomRptGraph telcomRptGraph = new TelcomRptGraph()
            .timeType(DEFAULT_TIME_TYPE)
            .prdId(DEFAULT_PRD_ID)
            .kpiId(DEFAULT_KPI_ID)
            .kpiCode(DEFAULT_KPI_CODE)
            .kpiName(DEFAULT_KPI_NAME)
            .objCode(DEFAULT_OBJ_CODE)
            .objName(DEFAULT_OBJ_NAME)
            .parentCode(DEFAULT_PARENT_CODE)
            .parentName(DEFAULT_PARENT_NAME)
            .inputLevel(DEFAULT_INPUT_LEVEL)
            .valPlan(DEFAULT_VAL_PLAN)
            .valPlanMon(DEFAULT_VAL_PLAN_MON)
            .valPlanQuar(DEFAULT_VAL_PLAN_QUAR)
            .valPlanYear(DEFAULT_VAL_PLAN_YEAR)
            .val(DEFAULT_VAL)
            .valMon(DEFAULT_VAL_MON)
            .valQuar(DEFAULT_VAL_QUAR)
            .valYear(DEFAULT_VAL_YEAR)
            .valLastMon(DEFAULT_VAL_LAST_MON)
            .valLastQuar(DEFAULT_VAL_LAST_QUAR)
            .valLastYear(DEFAULT_VAL_LAST_YEAR)
            .valDelta(DEFAULT_VAL_DELTA)
            .valDeltaMon(DEFAULT_VAL_DELTA_MON)
            .valDeltaQuar(DEFAULT_VAL_DELTA_QUAR)
            .valDeltaYear(DEFAULT_VAL_DELTA_YEAR)
            .percentPlan(DEFAULT_PERCENT_PLAN)
            .percentPlanMon(DEFAULT_PERCENT_PLAN_MON)
            .percentPlanQuar(DEFAULT_PERCENT_PLAN_QUAR)
            .percentPlanYear(DEFAULT_PERCENT_PLAN_YEAR)
            .percentGrow(DEFAULT_PERCENT_GROW)
            .percentGrowMon(DEFAULT_PERCENT_GROW_MON)
            .percentGrowQuar(DEFAULT_PERCENT_GROW_QUAR)
            .percentGrowYear(DEFAULT_PERCENT_GROW_YEAR)
            .colorAlarm(DEFAULT_COLOR_ALARM)
            .domainCode(DEFAULT_DOMAIN_CODE);
        return telcomRptGraph;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelcomRptGraph createUpdatedEntity(EntityManager em) {
        TelcomRptGraph telcomRptGraph = new TelcomRptGraph()
            .timeType(UPDATED_TIME_TYPE)
            .prdId(UPDATED_PRD_ID)
            .kpiId(UPDATED_KPI_ID)
            .kpiCode(UPDATED_KPI_CODE)
            .kpiName(UPDATED_KPI_NAME)
            .objCode(UPDATED_OBJ_CODE)
            .objName(UPDATED_OBJ_NAME)
            .parentCode(UPDATED_PARENT_CODE)
            .parentName(UPDATED_PARENT_NAME)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .valPlan(UPDATED_VAL_PLAN)
            .valPlanMon(UPDATED_VAL_PLAN_MON)
            .valPlanQuar(UPDATED_VAL_PLAN_QUAR)
            .valPlanYear(UPDATED_VAL_PLAN_YEAR)
            .val(UPDATED_VAL)
            .valMon(UPDATED_VAL_MON)
            .valQuar(UPDATED_VAL_QUAR)
            .valYear(UPDATED_VAL_YEAR)
            .valLastMon(UPDATED_VAL_LAST_MON)
            .valLastQuar(UPDATED_VAL_LAST_QUAR)
            .valLastYear(UPDATED_VAL_LAST_YEAR)
            .valDelta(UPDATED_VAL_DELTA)
            .valDeltaMon(UPDATED_VAL_DELTA_MON)
            .valDeltaQuar(UPDATED_VAL_DELTA_QUAR)
            .valDeltaYear(UPDATED_VAL_DELTA_YEAR)
            .percentPlan(UPDATED_PERCENT_PLAN)
            .percentPlanMon(UPDATED_PERCENT_PLAN_MON)
            .percentPlanQuar(UPDATED_PERCENT_PLAN_QUAR)
            .percentPlanYear(UPDATED_PERCENT_PLAN_YEAR)
            .percentGrow(UPDATED_PERCENT_GROW)
            .percentGrowMon(UPDATED_PERCENT_GROW_MON)
            .percentGrowQuar(UPDATED_PERCENT_GROW_QUAR)
            .percentGrowYear(UPDATED_PERCENT_GROW_YEAR)
            .colorAlarm(UPDATED_COLOR_ALARM)
            .domainCode(UPDATED_DOMAIN_CODE);
        return telcomRptGraph;
    }

    @BeforeEach
    public void initTest() {
        telcomRptGraph = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelcomRptGraph() throws Exception {
        int databaseSizeBeforeCreate = telcomRptGraphRepository.findAll().size();

        // Create the TelcomRptGraph
        TelcomRptGraphDTO telcomRptGraphDTO = telcomRptGraphMapper.toDto(telcomRptGraph);
        restTelcomRptGraphMockMvc.perform(post("/api/telcom-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telcomRptGraphDTO)))
            .andExpect(status().isCreated());

        // Validate the TelcomRptGraph in the database
        List<TelcomRptGraph> telcomRptGraphList = telcomRptGraphRepository.findAll();
        assertThat(telcomRptGraphList).hasSize(databaseSizeBeforeCreate + 1);
        TelcomRptGraph testTelcomRptGraph = telcomRptGraphList.get(telcomRptGraphList.size() - 1);
        assertThat(testTelcomRptGraph.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
        assertThat(testTelcomRptGraph.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testTelcomRptGraph.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testTelcomRptGraph.getKpiCode()).isEqualTo(DEFAULT_KPI_CODE);
        assertThat(testTelcomRptGraph.getKpiName()).isEqualTo(DEFAULT_KPI_NAME);
        assertThat(testTelcomRptGraph.getObjCode()).isEqualTo(DEFAULT_OBJ_CODE);
        assertThat(testTelcomRptGraph.getObjName()).isEqualTo(DEFAULT_OBJ_NAME);
        assertThat(testTelcomRptGraph.getParentCode()).isEqualTo(DEFAULT_PARENT_CODE);
        assertThat(testTelcomRptGraph.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testTelcomRptGraph.getInputLevel()).isEqualTo(DEFAULT_INPUT_LEVEL);
        assertThat(testTelcomRptGraph.getValPlan()).isEqualTo(DEFAULT_VAL_PLAN);
        assertThat(testTelcomRptGraph.getValPlanMon()).isEqualTo(DEFAULT_VAL_PLAN_MON);
        assertThat(testTelcomRptGraph.getValPlanQuar()).isEqualTo(DEFAULT_VAL_PLAN_QUAR);
        assertThat(testTelcomRptGraph.getValPlanYear()).isEqualTo(DEFAULT_VAL_PLAN_YEAR);
        assertThat(testTelcomRptGraph.getVal()).isEqualTo(DEFAULT_VAL);
        assertThat(testTelcomRptGraph.getValMon()).isEqualTo(DEFAULT_VAL_MON);
        assertThat(testTelcomRptGraph.getValQuar()).isEqualTo(DEFAULT_VAL_QUAR);
        assertThat(testTelcomRptGraph.getValYear()).isEqualTo(DEFAULT_VAL_YEAR);
        assertThat(testTelcomRptGraph.getValLastMon()).isEqualTo(DEFAULT_VAL_LAST_MON);
        assertThat(testTelcomRptGraph.getValLastQuar()).isEqualTo(DEFAULT_VAL_LAST_QUAR);
        assertThat(testTelcomRptGraph.getValLastYear()).isEqualTo(DEFAULT_VAL_LAST_YEAR);
        assertThat(testTelcomRptGraph.getValDelta()).isEqualTo(DEFAULT_VAL_DELTA);
        assertThat(testTelcomRptGraph.getValDeltaMon()).isEqualTo(DEFAULT_VAL_DELTA_MON);
        assertThat(testTelcomRptGraph.getValDeltaQuar()).isEqualTo(DEFAULT_VAL_DELTA_QUAR);
        assertThat(testTelcomRptGraph.getValDeltaYear()).isEqualTo(DEFAULT_VAL_DELTA_YEAR);
        assertThat(testTelcomRptGraph.getPercentPlan()).isEqualTo(DEFAULT_PERCENT_PLAN);
        assertThat(testTelcomRptGraph.getPercentPlanMon()).isEqualTo(DEFAULT_PERCENT_PLAN_MON);
        assertThat(testTelcomRptGraph.getPercentPlanQuar()).isEqualTo(DEFAULT_PERCENT_PLAN_QUAR);
        assertThat(testTelcomRptGraph.getPercentPlanYear()).isEqualTo(DEFAULT_PERCENT_PLAN_YEAR);
        assertThat(testTelcomRptGraph.getPercentGrow()).isEqualTo(DEFAULT_PERCENT_GROW);
        assertThat(testTelcomRptGraph.getPercentGrowMon()).isEqualTo(DEFAULT_PERCENT_GROW_MON);
        assertThat(testTelcomRptGraph.getPercentGrowQuar()).isEqualTo(DEFAULT_PERCENT_GROW_QUAR);
        assertThat(testTelcomRptGraph.getPercentGrowYear()).isEqualTo(DEFAULT_PERCENT_GROW_YEAR);
        assertThat(testTelcomRptGraph.getColorAlarm()).isEqualTo(DEFAULT_COLOR_ALARM);
        assertThat(testTelcomRptGraph.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
    }

    @Test
    @Transactional
    public void createTelcomRptGraphWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telcomRptGraphRepository.findAll().size();

        // Create the TelcomRptGraph with an existing ID
        telcomRptGraph.setId(1L);
        TelcomRptGraphDTO telcomRptGraphDTO = telcomRptGraphMapper.toDto(telcomRptGraph);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelcomRptGraphMockMvc.perform(post("/api/telcom-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telcomRptGraphDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelcomRptGraph in the database
        List<TelcomRptGraph> telcomRptGraphList = telcomRptGraphRepository.findAll();
        assertThat(telcomRptGraphList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTelcomRptGraphs() throws Exception {
        // Initialize the database
        telcomRptGraphRepository.saveAndFlush(telcomRptGraph);

        // Get all the telcomRptGraphList
        restTelcomRptGraphMockMvc.perform(get("/api/telcom-rpt-graphs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telcomRptGraph.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeType").value(hasItem(DEFAULT_TIME_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].prdId").value(hasItem(DEFAULT_PRD_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiCode").value(hasItem(DEFAULT_KPI_CODE)))
            .andExpect(jsonPath("$.[*].kpiName").value(hasItem(DEFAULT_KPI_NAME)))
            .andExpect(jsonPath("$.[*].objCode").value(hasItem(DEFAULT_OBJ_CODE)))
            .andExpect(jsonPath("$.[*].objName").value(hasItem(DEFAULT_OBJ_NAME)))
            .andExpect(jsonPath("$.[*].parentCode").value(hasItem(DEFAULT_PARENT_CODE)))
            .andExpect(jsonPath("$.[*].parentName").value(hasItem(DEFAULT_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].inputLevel").value(hasItem(DEFAULT_INPUT_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].valPlan").value(hasItem(DEFAULT_VAL_PLAN.intValue())))
            .andExpect(jsonPath("$.[*].valPlanMon").value(hasItem(DEFAULT_VAL_PLAN_MON.intValue())))
            .andExpect(jsonPath("$.[*].valPlanQuar").value(hasItem(DEFAULT_VAL_PLAN_QUAR.intValue())))
            .andExpect(jsonPath("$.[*].valPlanYear").value(hasItem(DEFAULT_VAL_PLAN_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].val").value(hasItem(DEFAULT_VAL.intValue())))
            .andExpect(jsonPath("$.[*].valMon").value(hasItem(DEFAULT_VAL_MON.intValue())))
            .andExpect(jsonPath("$.[*].valQuar").value(hasItem(DEFAULT_VAL_QUAR.intValue())))
            .andExpect(jsonPath("$.[*].valYear").value(hasItem(DEFAULT_VAL_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].valLastMon").value(hasItem(DEFAULT_VAL_LAST_MON.intValue())))
            .andExpect(jsonPath("$.[*].valLastQuar").value(hasItem(DEFAULT_VAL_LAST_QUAR.intValue())))
            .andExpect(jsonPath("$.[*].valLastYear").value(hasItem(DEFAULT_VAL_LAST_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].valDelta").value(hasItem(DEFAULT_VAL_DELTA.intValue())))
            .andExpect(jsonPath("$.[*].valDeltaMon").value(hasItem(DEFAULT_VAL_DELTA_MON.intValue())))
            .andExpect(jsonPath("$.[*].valDeltaQuar").value(hasItem(DEFAULT_VAL_DELTA_QUAR.intValue())))
            .andExpect(jsonPath("$.[*].valDeltaYear").value(hasItem(DEFAULT_VAL_DELTA_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].percentPlan").value(hasItem(DEFAULT_PERCENT_PLAN.intValue())))
            .andExpect(jsonPath("$.[*].percentPlanMon").value(hasItem(DEFAULT_PERCENT_PLAN_MON.intValue())))
            .andExpect(jsonPath("$.[*].percentPlanQuar").value(hasItem(DEFAULT_PERCENT_PLAN_QUAR.intValue())))
            .andExpect(jsonPath("$.[*].percentPlanYear").value(hasItem(DEFAULT_PERCENT_PLAN_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].percentGrow").value(hasItem(DEFAULT_PERCENT_GROW.intValue())))
            .andExpect(jsonPath("$.[*].percentGrowMon").value(hasItem(DEFAULT_PERCENT_GROW_MON.intValue())))
            .andExpect(jsonPath("$.[*].percentGrowQuar").value(hasItem(DEFAULT_PERCENT_GROW_QUAR.intValue())))
            .andExpect(jsonPath("$.[*].percentGrowYear").value(hasItem(DEFAULT_PERCENT_GROW_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].colorAlarm").value(hasItem(DEFAULT_COLOR_ALARM)))
            .andExpect(jsonPath("$.[*].domainCode").value(hasItem(DEFAULT_DOMAIN_CODE)));
    }
    
    @Test
    @Transactional
    public void getTelcomRptGraph() throws Exception {
        // Initialize the database
        telcomRptGraphRepository.saveAndFlush(telcomRptGraph);

        // Get the telcomRptGraph
        restTelcomRptGraphMockMvc.perform(get("/api/telcom-rpt-graphs/{id}", telcomRptGraph.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(telcomRptGraph.getId().intValue()))
            .andExpect(jsonPath("$.timeType").value(DEFAULT_TIME_TYPE.intValue()))
            .andExpect(jsonPath("$.prdId").value(DEFAULT_PRD_ID.intValue()))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID.intValue()))
            .andExpect(jsonPath("$.kpiCode").value(DEFAULT_KPI_CODE))
            .andExpect(jsonPath("$.kpiName").value(DEFAULT_KPI_NAME))
            .andExpect(jsonPath("$.objCode").value(DEFAULT_OBJ_CODE))
            .andExpect(jsonPath("$.objName").value(DEFAULT_OBJ_NAME))
            .andExpect(jsonPath("$.parentCode").value(DEFAULT_PARENT_CODE))
            .andExpect(jsonPath("$.parentName").value(DEFAULT_PARENT_NAME))
            .andExpect(jsonPath("$.inputLevel").value(DEFAULT_INPUT_LEVEL.intValue()))
            .andExpect(jsonPath("$.valPlan").value(DEFAULT_VAL_PLAN.intValue()))
            .andExpect(jsonPath("$.valPlanMon").value(DEFAULT_VAL_PLAN_MON.intValue()))
            .andExpect(jsonPath("$.valPlanQuar").value(DEFAULT_VAL_PLAN_QUAR.intValue()))
            .andExpect(jsonPath("$.valPlanYear").value(DEFAULT_VAL_PLAN_YEAR.intValue()))
            .andExpect(jsonPath("$.val").value(DEFAULT_VAL.intValue()))
            .andExpect(jsonPath("$.valMon").value(DEFAULT_VAL_MON.intValue()))
            .andExpect(jsonPath("$.valQuar").value(DEFAULT_VAL_QUAR.intValue()))
            .andExpect(jsonPath("$.valYear").value(DEFAULT_VAL_YEAR.intValue()))
            .andExpect(jsonPath("$.valLastMon").value(DEFAULT_VAL_LAST_MON.intValue()))
            .andExpect(jsonPath("$.valLastQuar").value(DEFAULT_VAL_LAST_QUAR.intValue()))
            .andExpect(jsonPath("$.valLastYear").value(DEFAULT_VAL_LAST_YEAR.intValue()))
            .andExpect(jsonPath("$.valDelta").value(DEFAULT_VAL_DELTA.intValue()))
            .andExpect(jsonPath("$.valDeltaMon").value(DEFAULT_VAL_DELTA_MON.intValue()))
            .andExpect(jsonPath("$.valDeltaQuar").value(DEFAULT_VAL_DELTA_QUAR.intValue()))
            .andExpect(jsonPath("$.valDeltaYear").value(DEFAULT_VAL_DELTA_YEAR.intValue()))
            .andExpect(jsonPath("$.percentPlan").value(DEFAULT_PERCENT_PLAN.intValue()))
            .andExpect(jsonPath("$.percentPlanMon").value(DEFAULT_PERCENT_PLAN_MON.intValue()))
            .andExpect(jsonPath("$.percentPlanQuar").value(DEFAULT_PERCENT_PLAN_QUAR.intValue()))
            .andExpect(jsonPath("$.percentPlanYear").value(DEFAULT_PERCENT_PLAN_YEAR.intValue()))
            .andExpect(jsonPath("$.percentGrow").value(DEFAULT_PERCENT_GROW.intValue()))
            .andExpect(jsonPath("$.percentGrowMon").value(DEFAULT_PERCENT_GROW_MON.intValue()))
            .andExpect(jsonPath("$.percentGrowQuar").value(DEFAULT_PERCENT_GROW_QUAR.intValue()))
            .andExpect(jsonPath("$.percentGrowYear").value(DEFAULT_PERCENT_GROW_YEAR.intValue()))
            .andExpect(jsonPath("$.colorAlarm").value(DEFAULT_COLOR_ALARM))
            .andExpect(jsonPath("$.domainCode").value(DEFAULT_DOMAIN_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingTelcomRptGraph() throws Exception {
        // Get the telcomRptGraph
        restTelcomRptGraphMockMvc.perform(get("/api/telcom-rpt-graphs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelcomRptGraph() throws Exception {
        // Initialize the database
        telcomRptGraphRepository.saveAndFlush(telcomRptGraph);

        int databaseSizeBeforeUpdate = telcomRptGraphRepository.findAll().size();

        // Update the telcomRptGraph
        TelcomRptGraph updatedTelcomRptGraph = telcomRptGraphRepository.findById(telcomRptGraph.getId()).get();
        // Disconnect from session so that the updates on updatedTelcomRptGraph are not directly saved in db
        em.detach(updatedTelcomRptGraph);
        updatedTelcomRptGraph
            .timeType(UPDATED_TIME_TYPE)
            .prdId(UPDATED_PRD_ID)
            .kpiId(UPDATED_KPI_ID)
            .kpiCode(UPDATED_KPI_CODE)
            .kpiName(UPDATED_KPI_NAME)
            .objCode(UPDATED_OBJ_CODE)
            .objName(UPDATED_OBJ_NAME)
            .parentCode(UPDATED_PARENT_CODE)
            .parentName(UPDATED_PARENT_NAME)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .valPlan(UPDATED_VAL_PLAN)
            .valPlanMon(UPDATED_VAL_PLAN_MON)
            .valPlanQuar(UPDATED_VAL_PLAN_QUAR)
            .valPlanYear(UPDATED_VAL_PLAN_YEAR)
            .val(UPDATED_VAL)
            .valMon(UPDATED_VAL_MON)
            .valQuar(UPDATED_VAL_QUAR)
            .valYear(UPDATED_VAL_YEAR)
            .valLastMon(UPDATED_VAL_LAST_MON)
            .valLastQuar(UPDATED_VAL_LAST_QUAR)
            .valLastYear(UPDATED_VAL_LAST_YEAR)
            .valDelta(UPDATED_VAL_DELTA)
            .valDeltaMon(UPDATED_VAL_DELTA_MON)
            .valDeltaQuar(UPDATED_VAL_DELTA_QUAR)
            .valDeltaYear(UPDATED_VAL_DELTA_YEAR)
            .percentPlan(UPDATED_PERCENT_PLAN)
            .percentPlanMon(UPDATED_PERCENT_PLAN_MON)
            .percentPlanQuar(UPDATED_PERCENT_PLAN_QUAR)
            .percentPlanYear(UPDATED_PERCENT_PLAN_YEAR)
            .percentGrow(UPDATED_PERCENT_GROW)
            .percentGrowMon(UPDATED_PERCENT_GROW_MON)
            .percentGrowQuar(UPDATED_PERCENT_GROW_QUAR)
            .percentGrowYear(UPDATED_PERCENT_GROW_YEAR)
            .colorAlarm(UPDATED_COLOR_ALARM)
            .domainCode(UPDATED_DOMAIN_CODE);
        TelcomRptGraphDTO telcomRptGraphDTO = telcomRptGraphMapper.toDto(updatedTelcomRptGraph);

        restTelcomRptGraphMockMvc.perform(put("/api/telcom-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telcomRptGraphDTO)))
            .andExpect(status().isOk());

        // Validate the TelcomRptGraph in the database
        List<TelcomRptGraph> telcomRptGraphList = telcomRptGraphRepository.findAll();
        assertThat(telcomRptGraphList).hasSize(databaseSizeBeforeUpdate);
        TelcomRptGraph testTelcomRptGraph = telcomRptGraphList.get(telcomRptGraphList.size() - 1);
        assertThat(testTelcomRptGraph.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
        assertThat(testTelcomRptGraph.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testTelcomRptGraph.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testTelcomRptGraph.getKpiCode()).isEqualTo(UPDATED_KPI_CODE);
        assertThat(testTelcomRptGraph.getKpiName()).isEqualTo(UPDATED_KPI_NAME);
        assertThat(testTelcomRptGraph.getObjCode()).isEqualTo(UPDATED_OBJ_CODE);
        assertThat(testTelcomRptGraph.getObjName()).isEqualTo(UPDATED_OBJ_NAME);
        assertThat(testTelcomRptGraph.getParentCode()).isEqualTo(UPDATED_PARENT_CODE);
        assertThat(testTelcomRptGraph.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testTelcomRptGraph.getInputLevel()).isEqualTo(UPDATED_INPUT_LEVEL);
        assertThat(testTelcomRptGraph.getValPlan()).isEqualTo(UPDATED_VAL_PLAN);
        assertThat(testTelcomRptGraph.getValPlanMon()).isEqualTo(UPDATED_VAL_PLAN_MON);
        assertThat(testTelcomRptGraph.getValPlanQuar()).isEqualTo(UPDATED_VAL_PLAN_QUAR);
        assertThat(testTelcomRptGraph.getValPlanYear()).isEqualTo(UPDATED_VAL_PLAN_YEAR);
        assertThat(testTelcomRptGraph.getVal()).isEqualTo(UPDATED_VAL);
        assertThat(testTelcomRptGraph.getValMon()).isEqualTo(UPDATED_VAL_MON);
        assertThat(testTelcomRptGraph.getValQuar()).isEqualTo(UPDATED_VAL_QUAR);
        assertThat(testTelcomRptGraph.getValYear()).isEqualTo(UPDATED_VAL_YEAR);
        assertThat(testTelcomRptGraph.getValLastMon()).isEqualTo(UPDATED_VAL_LAST_MON);
        assertThat(testTelcomRptGraph.getValLastQuar()).isEqualTo(UPDATED_VAL_LAST_QUAR);
        assertThat(testTelcomRptGraph.getValLastYear()).isEqualTo(UPDATED_VAL_LAST_YEAR);
        assertThat(testTelcomRptGraph.getValDelta()).isEqualTo(UPDATED_VAL_DELTA);
        assertThat(testTelcomRptGraph.getValDeltaMon()).isEqualTo(UPDATED_VAL_DELTA_MON);
        assertThat(testTelcomRptGraph.getValDeltaQuar()).isEqualTo(UPDATED_VAL_DELTA_QUAR);
        assertThat(testTelcomRptGraph.getValDeltaYear()).isEqualTo(UPDATED_VAL_DELTA_YEAR);
        assertThat(testTelcomRptGraph.getPercentPlan()).isEqualTo(UPDATED_PERCENT_PLAN);
        assertThat(testTelcomRptGraph.getPercentPlanMon()).isEqualTo(UPDATED_PERCENT_PLAN_MON);
        assertThat(testTelcomRptGraph.getPercentPlanQuar()).isEqualTo(UPDATED_PERCENT_PLAN_QUAR);
        assertThat(testTelcomRptGraph.getPercentPlanYear()).isEqualTo(UPDATED_PERCENT_PLAN_YEAR);
        assertThat(testTelcomRptGraph.getPercentGrow()).isEqualTo(UPDATED_PERCENT_GROW);
        assertThat(testTelcomRptGraph.getPercentGrowMon()).isEqualTo(UPDATED_PERCENT_GROW_MON);
        assertThat(testTelcomRptGraph.getPercentGrowQuar()).isEqualTo(UPDATED_PERCENT_GROW_QUAR);
        assertThat(testTelcomRptGraph.getPercentGrowYear()).isEqualTo(UPDATED_PERCENT_GROW_YEAR);
        assertThat(testTelcomRptGraph.getColorAlarm()).isEqualTo(UPDATED_COLOR_ALARM);
        assertThat(testTelcomRptGraph.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingTelcomRptGraph() throws Exception {
        int databaseSizeBeforeUpdate = telcomRptGraphRepository.findAll().size();

        // Create the TelcomRptGraph
        TelcomRptGraphDTO telcomRptGraphDTO = telcomRptGraphMapper.toDto(telcomRptGraph);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelcomRptGraphMockMvc.perform(put("/api/telcom-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(telcomRptGraphDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TelcomRptGraph in the database
        List<TelcomRptGraph> telcomRptGraphList = telcomRptGraphRepository.findAll();
        assertThat(telcomRptGraphList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelcomRptGraph() throws Exception {
        // Initialize the database
        telcomRptGraphRepository.saveAndFlush(telcomRptGraph);

        int databaseSizeBeforeDelete = telcomRptGraphRepository.findAll().size();

        // Delete the telcomRptGraph
        restTelcomRptGraphMockMvc.perform(delete("/api/telcom-rpt-graphs/{id}", telcomRptGraph.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TelcomRptGraph> telcomRptGraphList = telcomRptGraphRepository.findAll();
        assertThat(telcomRptGraphList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
