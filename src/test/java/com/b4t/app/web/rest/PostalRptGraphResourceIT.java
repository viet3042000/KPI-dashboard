package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.PostalRptGraph;
import com.b4t.app.repository.PostalRptGraphRepository;
import com.b4t.app.service.PostalRptGraphService;
import com.b4t.app.service.dto.PostalRptGraphDTO;
import com.b4t.app.service.mapper.PostalRptGraphMapper;
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
 * Integration tests for the {@link PostalRptGraphResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class PostalRptGraphResourceIT {

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
    private PostalRptGraphRepository postalRptGraphRepository;

    @Autowired
    private PostalRptGraphMapper postalRptGraphMapper;

    @Autowired
    private PostalRptGraphService postalRptGraphService;

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

    private MockMvc restPostalRptGraphMockMvc;

    private PostalRptGraph postalRptGraph;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostalRptGraphResource postalRptGraphResource = new PostalRptGraphResource(postalRptGraphService);
        this.restPostalRptGraphMockMvc = MockMvcBuilders.standaloneSetup(postalRptGraphResource)
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
    public static PostalRptGraph createEntity(EntityManager em) {
        PostalRptGraph postalRptGraph = new PostalRptGraph()
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
        return postalRptGraph;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalRptGraph createUpdatedEntity(EntityManager em) {
        PostalRptGraph postalRptGraph = new PostalRptGraph()
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
        return postalRptGraph;
    }

    @BeforeEach
    public void initTest() {
        postalRptGraph = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostalRptGraph() throws Exception {
        int databaseSizeBeforeCreate = postalRptGraphRepository.findAll().size();

        // Create the PostalRptGraph
        PostalRptGraphDTO postalRptGraphDTO = postalRptGraphMapper.toDto(postalRptGraph);
        restPostalRptGraphMockMvc.perform(post("/api/postal-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalRptGraphDTO)))
            .andExpect(status().isCreated());

        // Validate the PostalRptGraph in the database
        List<PostalRptGraph> postalRptGraphList = postalRptGraphRepository.findAll();
        assertThat(postalRptGraphList).hasSize(databaseSizeBeforeCreate + 1);
        PostalRptGraph testPostalRptGraph = postalRptGraphList.get(postalRptGraphList.size() - 1);
        assertThat(testPostalRptGraph.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
        assertThat(testPostalRptGraph.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testPostalRptGraph.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testPostalRptGraph.getKpiCode()).isEqualTo(DEFAULT_KPI_CODE);
        assertThat(testPostalRptGraph.getKpiName()).isEqualTo(DEFAULT_KPI_NAME);
        assertThat(testPostalRptGraph.getObjCode()).isEqualTo(DEFAULT_OBJ_CODE);
        assertThat(testPostalRptGraph.getObjName()).isEqualTo(DEFAULT_OBJ_NAME);
        assertThat(testPostalRptGraph.getParentCode()).isEqualTo(DEFAULT_PARENT_CODE);
        assertThat(testPostalRptGraph.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testPostalRptGraph.getInputLevel()).isEqualTo(DEFAULT_INPUT_LEVEL);
        assertThat(testPostalRptGraph.getValPlan()).isEqualTo(DEFAULT_VAL_PLAN);
        assertThat(testPostalRptGraph.getValPlanMon()).isEqualTo(DEFAULT_VAL_PLAN_MON);
        assertThat(testPostalRptGraph.getValPlanQuar()).isEqualTo(DEFAULT_VAL_PLAN_QUAR);
        assertThat(testPostalRptGraph.getValPlanYear()).isEqualTo(DEFAULT_VAL_PLAN_YEAR);
        assertThat(testPostalRptGraph.getVal()).isEqualTo(DEFAULT_VAL);
        assertThat(testPostalRptGraph.getValMon()).isEqualTo(DEFAULT_VAL_MON);
        assertThat(testPostalRptGraph.getValQuar()).isEqualTo(DEFAULT_VAL_QUAR);
        assertThat(testPostalRptGraph.getValYear()).isEqualTo(DEFAULT_VAL_YEAR);
        assertThat(testPostalRptGraph.getValLastMon()).isEqualTo(DEFAULT_VAL_LAST_MON);
        assertThat(testPostalRptGraph.getValLastQuar()).isEqualTo(DEFAULT_VAL_LAST_QUAR);
        assertThat(testPostalRptGraph.getValLastYear()).isEqualTo(DEFAULT_VAL_LAST_YEAR);
        assertThat(testPostalRptGraph.getValDelta()).isEqualTo(DEFAULT_VAL_DELTA);
        assertThat(testPostalRptGraph.getValDeltaMon()).isEqualTo(DEFAULT_VAL_DELTA_MON);
        assertThat(testPostalRptGraph.getValDeltaQuar()).isEqualTo(DEFAULT_VAL_DELTA_QUAR);
        assertThat(testPostalRptGraph.getValDeltaYear()).isEqualTo(DEFAULT_VAL_DELTA_YEAR);
        assertThat(testPostalRptGraph.getPercentPlan()).isEqualTo(DEFAULT_PERCENT_PLAN);
        assertThat(testPostalRptGraph.getPercentPlanMon()).isEqualTo(DEFAULT_PERCENT_PLAN_MON);
        assertThat(testPostalRptGraph.getPercentPlanQuar()).isEqualTo(DEFAULT_PERCENT_PLAN_QUAR);
        assertThat(testPostalRptGraph.getPercentPlanYear()).isEqualTo(DEFAULT_PERCENT_PLAN_YEAR);
        assertThat(testPostalRptGraph.getPercentGrow()).isEqualTo(DEFAULT_PERCENT_GROW);
        assertThat(testPostalRptGraph.getPercentGrowMon()).isEqualTo(DEFAULT_PERCENT_GROW_MON);
        assertThat(testPostalRptGraph.getPercentGrowQuar()).isEqualTo(DEFAULT_PERCENT_GROW_QUAR);
        assertThat(testPostalRptGraph.getPercentGrowYear()).isEqualTo(DEFAULT_PERCENT_GROW_YEAR);
        assertThat(testPostalRptGraph.getColorAlarm()).isEqualTo(DEFAULT_COLOR_ALARM);
        assertThat(testPostalRptGraph.getDomainCode()).isEqualTo(DEFAULT_DOMAIN_CODE);
    }

    @Test
    @Transactional
    public void createPostalRptGraphWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postalRptGraphRepository.findAll().size();

        // Create the PostalRptGraph with an existing ID
        postalRptGraph.setId(1L);
        PostalRptGraphDTO postalRptGraphDTO = postalRptGraphMapper.toDto(postalRptGraph);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalRptGraphMockMvc.perform(post("/api/postal-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalRptGraphDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostalRptGraph in the database
        List<PostalRptGraph> postalRptGraphList = postalRptGraphRepository.findAll();
        assertThat(postalRptGraphList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPostalRptGraphs() throws Exception {
        // Initialize the database
        postalRptGraphRepository.saveAndFlush(postalRptGraph);

        // Get all the postalRptGraphList
        restPostalRptGraphMockMvc.perform(get("/api/postal-rpt-graphs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalRptGraph.getId().intValue())))
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
    public void getPostalRptGraph() throws Exception {
        // Initialize the database
        postalRptGraphRepository.saveAndFlush(postalRptGraph);

        // Get the postalRptGraph
        restPostalRptGraphMockMvc.perform(get("/api/postal-rpt-graphs/{id}", postalRptGraph.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postalRptGraph.getId().intValue()))
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
    public void getNonExistingPostalRptGraph() throws Exception {
        // Get the postalRptGraph
        restPostalRptGraphMockMvc.perform(get("/api/postal-rpt-graphs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostalRptGraph() throws Exception {
        // Initialize the database
        postalRptGraphRepository.saveAndFlush(postalRptGraph);

        int databaseSizeBeforeUpdate = postalRptGraphRepository.findAll().size();

        // Update the postalRptGraph
        PostalRptGraph updatedPostalRptGraph = postalRptGraphRepository.findById(postalRptGraph.getId()).get();
        // Disconnect from session so that the updates on updatedPostalRptGraph are not directly saved in db
        em.detach(updatedPostalRptGraph);
        updatedPostalRptGraph
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
        PostalRptGraphDTO postalRptGraphDTO = postalRptGraphMapper.toDto(updatedPostalRptGraph);

        restPostalRptGraphMockMvc.perform(put("/api/postal-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalRptGraphDTO)))
            .andExpect(status().isOk());

        // Validate the PostalRptGraph in the database
        List<PostalRptGraph> postalRptGraphList = postalRptGraphRepository.findAll();
        assertThat(postalRptGraphList).hasSize(databaseSizeBeforeUpdate);
        PostalRptGraph testPostalRptGraph = postalRptGraphList.get(postalRptGraphList.size() - 1);
        assertThat(testPostalRptGraph.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
        assertThat(testPostalRptGraph.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testPostalRptGraph.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testPostalRptGraph.getKpiCode()).isEqualTo(UPDATED_KPI_CODE);
        assertThat(testPostalRptGraph.getKpiName()).isEqualTo(UPDATED_KPI_NAME);
        assertThat(testPostalRptGraph.getObjCode()).isEqualTo(UPDATED_OBJ_CODE);
        assertThat(testPostalRptGraph.getObjName()).isEqualTo(UPDATED_OBJ_NAME);
        assertThat(testPostalRptGraph.getParentCode()).isEqualTo(UPDATED_PARENT_CODE);
        assertThat(testPostalRptGraph.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testPostalRptGraph.getInputLevel()).isEqualTo(UPDATED_INPUT_LEVEL);
        assertThat(testPostalRptGraph.getValPlan()).isEqualTo(UPDATED_VAL_PLAN);
        assertThat(testPostalRptGraph.getValPlanMon()).isEqualTo(UPDATED_VAL_PLAN_MON);
        assertThat(testPostalRptGraph.getValPlanQuar()).isEqualTo(UPDATED_VAL_PLAN_QUAR);
        assertThat(testPostalRptGraph.getValPlanYear()).isEqualTo(UPDATED_VAL_PLAN_YEAR);
        assertThat(testPostalRptGraph.getVal()).isEqualTo(UPDATED_VAL);
        assertThat(testPostalRptGraph.getValMon()).isEqualTo(UPDATED_VAL_MON);
        assertThat(testPostalRptGraph.getValQuar()).isEqualTo(UPDATED_VAL_QUAR);
        assertThat(testPostalRptGraph.getValYear()).isEqualTo(UPDATED_VAL_YEAR);
        assertThat(testPostalRptGraph.getValLastMon()).isEqualTo(UPDATED_VAL_LAST_MON);
        assertThat(testPostalRptGraph.getValLastQuar()).isEqualTo(UPDATED_VAL_LAST_QUAR);
        assertThat(testPostalRptGraph.getValLastYear()).isEqualTo(UPDATED_VAL_LAST_YEAR);
        assertThat(testPostalRptGraph.getValDelta()).isEqualTo(UPDATED_VAL_DELTA);
        assertThat(testPostalRptGraph.getValDeltaMon()).isEqualTo(UPDATED_VAL_DELTA_MON);
        assertThat(testPostalRptGraph.getValDeltaQuar()).isEqualTo(UPDATED_VAL_DELTA_QUAR);
        assertThat(testPostalRptGraph.getValDeltaYear()).isEqualTo(UPDATED_VAL_DELTA_YEAR);
        assertThat(testPostalRptGraph.getPercentPlan()).isEqualTo(UPDATED_PERCENT_PLAN);
        assertThat(testPostalRptGraph.getPercentPlanMon()).isEqualTo(UPDATED_PERCENT_PLAN_MON);
        assertThat(testPostalRptGraph.getPercentPlanQuar()).isEqualTo(UPDATED_PERCENT_PLAN_QUAR);
        assertThat(testPostalRptGraph.getPercentPlanYear()).isEqualTo(UPDATED_PERCENT_PLAN_YEAR);
        assertThat(testPostalRptGraph.getPercentGrow()).isEqualTo(UPDATED_PERCENT_GROW);
        assertThat(testPostalRptGraph.getPercentGrowMon()).isEqualTo(UPDATED_PERCENT_GROW_MON);
        assertThat(testPostalRptGraph.getPercentGrowQuar()).isEqualTo(UPDATED_PERCENT_GROW_QUAR);
        assertThat(testPostalRptGraph.getPercentGrowYear()).isEqualTo(UPDATED_PERCENT_GROW_YEAR);
        assertThat(testPostalRptGraph.getColorAlarm()).isEqualTo(UPDATED_COLOR_ALARM);
        assertThat(testPostalRptGraph.getDomainCode()).isEqualTo(UPDATED_DOMAIN_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingPostalRptGraph() throws Exception {
        int databaseSizeBeforeUpdate = postalRptGraphRepository.findAll().size();

        // Create the PostalRptGraph
        PostalRptGraphDTO postalRptGraphDTO = postalRptGraphMapper.toDto(postalRptGraph);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostalRptGraphMockMvc.perform(put("/api/postal-rpt-graphs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalRptGraphDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostalRptGraph in the database
        List<PostalRptGraph> postalRptGraphList = postalRptGraphRepository.findAll();
        assertThat(postalRptGraphList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePostalRptGraph() throws Exception {
        // Initialize the database
        postalRptGraphRepository.saveAndFlush(postalRptGraph);

        int databaseSizeBeforeDelete = postalRptGraphRepository.findAll().size();

        // Delete the postalRptGraph
        restPostalRptGraphMockMvc.perform(delete("/api/postal-rpt-graphs/{id}", postalRptGraph.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostalRptGraph> postalRptGraphList = postalRptGraphRepository.findAll();
        assertThat(postalRptGraphList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
