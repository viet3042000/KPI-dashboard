package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.IctRptGraph;
import com.b4t.app.repository.IctRptGraphRepository;
import com.b4t.app.service.IctRptGraphService;
import com.b4t.app.service.dto.IctRptGraphDTO;
import com.b4t.app.service.mapper.IctRptGraphMapper;
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
 * Integration tests for the {@link IctRptGraphResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class IctRptGraphResourceIT {

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
    private IctRptGraphRepository ictRptGraphRepository;

    @Autowired
    private IctRptGraphMapper ictRptGraphMapper;

    @Autowired
    private IctRptGraphService ictRptGraphService;

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

    private MockMvc restIctRptGraphMockMvc;

    private IctRptGraph ictRptGraph;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IctRptGraphResource ictRptGraphResource = new IctRptGraphResource(ictRptGraphService);
        this.restIctRptGraphMockMvc = MockMvcBuilders.standaloneSetup(ictRptGraphResource)
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
    public static IctRptGraph createEntity(EntityManager em) {
        IctRptGraph ictRptGraph = new IctRptGraph()
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
        return ictRptGraph;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IctRptGraph createUpdatedEntity(EntityManager em) {
        IctRptGraph ictRptGraph = new IctRptGraph()
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
        return ictRptGraph;
    }

    @BeforeEach
    public void initTest() {
        ictRptGraph = createEntity(em);
    }

    @Test
    @Transactional
    public void createIctRptGraph() throws Exception {
        int databaseSizeBeforeCreate = ictRptGraphRepository.findAll().size();

        // Create the IctRptGraph
        IctRptGraphDTO ictRptGraphDTO = ictRptGraphMapper.toDto(ictRptGraph);
        restIctRptGraphMockMvc.perform(post("/api/ict-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ictRptGraphDTO)))
            .andExpect(status().isCreated());

        // Validate the IctRptGraph in the database
        List<IctRptGraph> ictRptGraphList = ictRptGraphRepository.findAll();
        assertThat(ictRptGraphList).hasSize(databaseSizeBeforeCreate + 1);
        IctRptGraph testIctRptGraph = ictRptGraphList.get(ictRptGraphList.size() - 1);
        assertThat(testIctRptGraph.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
        assertThat(testIctRptGraph.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testIctRptGraph.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testIctRptGraph.getKpiCode()).isEqualTo(DEFAULT_KPI_CODE);
        assertThat(testIctRptGraph.getKpiName()).isEqualTo(DEFAULT_KPI_NAME);
        assertThat(testIctRptGraph.getObjCode()).isEqualTo(DEFAULT_OBJ_CODE);
        assertThat(testIctRptGraph.getObjName()).isEqualTo(DEFAULT_OBJ_NAME);
        assertThat(testIctRptGraph.getParentCode()).isEqualTo(DEFAULT_PARENT_CODE);
        assertThat(testIctRptGraph.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testIctRptGraph.getInputLevel()).isEqualTo(DEFAULT_INPUT_LEVEL);
        assertThat(testIctRptGraph.getValPlan()).isEqualTo(DEFAULT_VAL_PLAN);
        assertThat(testIctRptGraph.getValPlanMon()).isEqualTo(DEFAULT_VAL_PLAN_MON);
        assertThat(testIctRptGraph.getValPlanQuar()).isEqualTo(DEFAULT_VAL_PLAN_QUAR);
        assertThat(testIctRptGraph.getValPlanYear()).isEqualTo(DEFAULT_VAL_PLAN_YEAR);
        assertThat(testIctRptGraph.getVal()).isEqualTo(DEFAULT_VAL);
        assertThat(testIctRptGraph.getValMon()).isEqualTo(DEFAULT_VAL_MON);
        assertThat(testIctRptGraph.getValQuar()).isEqualTo(DEFAULT_VAL_QUAR);
        assertThat(testIctRptGraph.getValYear()).isEqualTo(DEFAULT_VAL_YEAR);
        assertThat(testIctRptGraph.getValLastMon()).isEqualTo(DEFAULT_VAL_LAST_MON);
        assertThat(testIctRptGraph.getValLastQuar()).isEqualTo(DEFAULT_VAL_LAST_QUAR);
        assertThat(testIctRptGraph.getValLastYear()).isEqualTo(DEFAULT_VAL_LAST_YEAR);
        assertThat(testIctRptGraph.getValDelta()).isEqualTo(DEFAULT_VAL_DELTA);
        assertThat(testIctRptGraph.getValDeltaMon()).isEqualTo(DEFAULT_VAL_DELTA_MON);
        assertThat(testIctRptGraph.getValDeltaQuar()).isEqualTo(DEFAULT_VAL_DELTA_QUAR);
        assertThat(testIctRptGraph.getValDeltaYear()).isEqualTo(DEFAULT_VAL_DELTA_YEAR);
        assertThat(testIctRptGraph.getPercentPlan()).isEqualTo(DEFAULT_PERCENT_PLAN);
        assertThat(testIctRptGraph.getPercentPlanMon()).isEqualTo(DEFAULT_PERCENT_PLAN_MON);
        assertThat(testIctRptGraph.getPercentPlanQuar()).isEqualTo(DEFAULT_PERCENT_PLAN_QUAR);
        assertThat(testIctRptGraph.getPercentPlanYear()).isEqualTo(DEFAULT_PERCENT_PLAN_YEAR);
        assertThat(testIctRptGraph.getPercentGrow()).isEqualTo(DEFAULT_PERCENT_GROW);
        assertThat(testIctRptGraph.getPercentGrowMon()).isEqualTo(DEFAULT_PERCENT_GROW_MON);
        assertThat(testIctRptGraph.getPercentGrowQuar()).isEqualTo(DEFAULT_PERCENT_GROW_QUAR);
        assertThat(testIctRptGraph.getPercentGrowYear()).isEqualTo(DEFAULT_PERCENT_GROW_YEAR);
        assertThat(testIctRptGraph.getColorAlarm()).isEqualTo(DEFAULT_COLOR_ALARM);
        assertThat(testIctRptGraph.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
    }

    @Test
    @Transactional
    public void createIctRptGraphWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ictRptGraphRepository.findAll().size();

        // Create the IctRptGraph with an existing ID
        ictRptGraph.setId(1L);
        IctRptGraphDTO ictRptGraphDTO = ictRptGraphMapper.toDto(ictRptGraph);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIctRptGraphMockMvc.perform(post("/api/ict-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ictRptGraphDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IctRptGraph in the database
        List<IctRptGraph> ictRptGraphList = ictRptGraphRepository.findAll();
        assertThat(ictRptGraphList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllIctRptGraphs() throws Exception {
        // Initialize the database
        ictRptGraphRepository.saveAndFlush(ictRptGraph);

        // Get all the ictRptGraphList
        restIctRptGraphMockMvc.perform(get("/api/ict-rpt-graphs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ictRptGraph.getId().intValue())))
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
    public void getIctRptGraph() throws Exception {
        // Initialize the database
        ictRptGraphRepository.saveAndFlush(ictRptGraph);

        // Get the ictRptGraph
        restIctRptGraphMockMvc.perform(get("/api/ict-rpt-graphs/{id}", ictRptGraph.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ictRptGraph.getId().intValue()))
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
    public void getNonExistingIctRptGraph() throws Exception {
        // Get the ictRptGraph
        restIctRptGraphMockMvc.perform(get("/api/ict-rpt-graphs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIctRptGraph() throws Exception {
        // Initialize the database
        ictRptGraphRepository.saveAndFlush(ictRptGraph);

        int databaseSizeBeforeUpdate = ictRptGraphRepository.findAll().size();

        // Update the ictRptGraph
        IctRptGraph updatedIctRptGraph = ictRptGraphRepository.findById(ictRptGraph.getId()).get();
        // Disconnect from session so that the updates on updatedIctRptGraph are not directly saved in db
        em.detach(updatedIctRptGraph);
        updatedIctRptGraph
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
        IctRptGraphDTO ictRptGraphDTO = ictRptGraphMapper.toDto(updatedIctRptGraph);

        restIctRptGraphMockMvc.perform(put("/api/ict-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ictRptGraphDTO)))
            .andExpect(status().isOk());

        // Validate the IctRptGraph in the database
        List<IctRptGraph> ictRptGraphList = ictRptGraphRepository.findAll();
        assertThat(ictRptGraphList).hasSize(databaseSizeBeforeUpdate);
        IctRptGraph testIctRptGraph = ictRptGraphList.get(ictRptGraphList.size() - 1);
        assertThat(testIctRptGraph.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
        assertThat(testIctRptGraph.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testIctRptGraph.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testIctRptGraph.getKpiCode()).isEqualTo(UPDATED_KPI_CODE);
        assertThat(testIctRptGraph.getKpiName()).isEqualTo(UPDATED_KPI_NAME);
        assertThat(testIctRptGraph.getObjCode()).isEqualTo(UPDATED_OBJ_CODE);
        assertThat(testIctRptGraph.getObjName()).isEqualTo(UPDATED_OBJ_NAME);
        assertThat(testIctRptGraph.getParentCode()).isEqualTo(UPDATED_PARENT_CODE);
        assertThat(testIctRptGraph.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testIctRptGraph.getInputLevel()).isEqualTo(UPDATED_INPUT_LEVEL);
        assertThat(testIctRptGraph.getValPlan()).isEqualTo(UPDATED_VAL_PLAN);
        assertThat(testIctRptGraph.getValPlanMon()).isEqualTo(UPDATED_VAL_PLAN_MON);
        assertThat(testIctRptGraph.getValPlanQuar()).isEqualTo(UPDATED_VAL_PLAN_QUAR);
        assertThat(testIctRptGraph.getValPlanYear()).isEqualTo(UPDATED_VAL_PLAN_YEAR);
        assertThat(testIctRptGraph.getVal()).isEqualTo(UPDATED_VAL);
        assertThat(testIctRptGraph.getValMon()).isEqualTo(UPDATED_VAL_MON);
        assertThat(testIctRptGraph.getValQuar()).isEqualTo(UPDATED_VAL_QUAR);
        assertThat(testIctRptGraph.getValYear()).isEqualTo(UPDATED_VAL_YEAR);
        assertThat(testIctRptGraph.getValLastMon()).isEqualTo(UPDATED_VAL_LAST_MON);
        assertThat(testIctRptGraph.getValLastQuar()).isEqualTo(UPDATED_VAL_LAST_QUAR);
        assertThat(testIctRptGraph.getValLastYear()).isEqualTo(UPDATED_VAL_LAST_YEAR);
        assertThat(testIctRptGraph.getValDelta()).isEqualTo(UPDATED_VAL_DELTA);
        assertThat(testIctRptGraph.getValDeltaMon()).isEqualTo(UPDATED_VAL_DELTA_MON);
        assertThat(testIctRptGraph.getValDeltaQuar()).isEqualTo(UPDATED_VAL_DELTA_QUAR);
        assertThat(testIctRptGraph.getValDeltaYear()).isEqualTo(UPDATED_VAL_DELTA_YEAR);
        assertThat(testIctRptGraph.getPercentPlan()).isEqualTo(UPDATED_PERCENT_PLAN);
        assertThat(testIctRptGraph.getPercentPlanMon()).isEqualTo(UPDATED_PERCENT_PLAN_MON);
        assertThat(testIctRptGraph.getPercentPlanQuar()).isEqualTo(UPDATED_PERCENT_PLAN_QUAR);
        assertThat(testIctRptGraph.getPercentPlanYear()).isEqualTo(UPDATED_PERCENT_PLAN_YEAR);
        assertThat(testIctRptGraph.getPercentGrow()).isEqualTo(UPDATED_PERCENT_GROW);
        assertThat(testIctRptGraph.getPercentGrowMon()).isEqualTo(UPDATED_PERCENT_GROW_MON);
        assertThat(testIctRptGraph.getPercentGrowQuar()).isEqualTo(UPDATED_PERCENT_GROW_QUAR);
        assertThat(testIctRptGraph.getPercentGrowYear()).isEqualTo(UPDATED_PERCENT_GROW_YEAR);
        assertThat(testIctRptGraph.getColorAlarm()).isEqualTo(UPDATED_COLOR_ALARM);
        assertThat(testIctRptGraph.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingIctRptGraph() throws Exception {
        int databaseSizeBeforeUpdate = ictRptGraphRepository.findAll().size();

        // Create the IctRptGraph
        IctRptGraphDTO ictRptGraphDTO = ictRptGraphMapper.toDto(ictRptGraph);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIctRptGraphMockMvc.perform(put("/api/ict-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ictRptGraphDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IctRptGraph in the database
        List<IctRptGraph> ictRptGraphList = ictRptGraphRepository.findAll();
        assertThat(ictRptGraphList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIctRptGraph() throws Exception {
        // Initialize the database
        ictRptGraphRepository.saveAndFlush(ictRptGraph);

        int databaseSizeBeforeDelete = ictRptGraphRepository.findAll().size();

        // Delete the ictRptGraph
        restIctRptGraphMockMvc.perform(delete("/api/ict-rpt-graphs/{id}", ictRptGraph.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IctRptGraph> ictRptGraphList = ictRptGraphRepository.findAll();
        assertThat(ictRptGraphList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
