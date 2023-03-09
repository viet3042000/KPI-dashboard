package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.MonitorQueryKpi;
import com.b4t.app.repository.MonitorQueryKpiRepository;
import com.b4t.app.service.MonitorQueryKpiService;
import com.b4t.app.service.dto.MonitorQueryKpiDTO;
import com.b4t.app.service.mapper.MonitorQueryKpiMapper;
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
 * Integration tests for the {@link MonitorQueryKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class MonitorQueryKpiResourceIT {

    private static final Integer DEFAULT_QUERY_KPI_ID = 1;
    private static final Integer UPDATED_QUERY_KPI_ID = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Instant DEFAULT_RUN_TIME_SUCC = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RUN_TIME_SUCC = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MonitorQueryKpiRepository monitorQueryKpiRepository;

    @Autowired
    private MonitorQueryKpiMapper monitorQueryKpiMapper;

    @Autowired
    private MonitorQueryKpiService monitorQueryKpiService;

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

    private MockMvc restMonitorQueryKpiMockMvc;

    private MonitorQueryKpi monitorQueryKpi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonitorQueryKpiResource monitorQueryKpiResource = new MonitorQueryKpiResource(monitorQueryKpiService);
        this.restMonitorQueryKpiMockMvc = MockMvcBuilders.standaloneSetup(monitorQueryKpiResource)
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
    public static MonitorQueryKpi createEntity(EntityManager em) {
        MonitorQueryKpi monitorQueryKpi = new MonitorQueryKpi()
            .queryKpiId(DEFAULT_QUERY_KPI_ID)
            .status(DEFAULT_STATUS)
            .runTimeSucc(DEFAULT_RUN_TIME_SUCC)
            .updateTime(DEFAULT_UPDATE_TIME);
        return monitorQueryKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitorQueryKpi createUpdatedEntity(EntityManager em) {
        MonitorQueryKpi monitorQueryKpi = new MonitorQueryKpi()
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .status(UPDATED_STATUS)
            .runTimeSucc(UPDATED_RUN_TIME_SUCC)
            .updateTime(UPDATED_UPDATE_TIME);
        return monitorQueryKpi;
    }

    @BeforeEach
    public void initTest() {
        monitorQueryKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createMonitorQueryKpi() throws Exception {
        int databaseSizeBeforeCreate = monitorQueryKpiRepository.findAll().size();

        // Create the MonitorQueryKpi
        MonitorQueryKpiDTO monitorQueryKpiDTO = monitorQueryKpiMapper.toDto(monitorQueryKpi);
        restMonitorQueryKpiMockMvc.perform(post("/api/monitor-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitorQueryKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the MonitorQueryKpi in the database
        List<MonitorQueryKpi> monitorQueryKpiList = monitorQueryKpiRepository.findAll();
        assertThat(monitorQueryKpiList).hasSize(databaseSizeBeforeCreate + 1);
        MonitorQueryKpi testMonitorQueryKpi = monitorQueryKpiList.get(monitorQueryKpiList.size() - 1);
        assertThat(testMonitorQueryKpi.getQueryKpiId()).isEqualTo(DEFAULT_QUERY_KPI_ID);
        assertThat(testMonitorQueryKpi.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMonitorQueryKpi.getRunTimeSucc()).isEqualTo(DEFAULT_RUN_TIME_SUCC);
        assertThat(testMonitorQueryKpi.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createMonitorQueryKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monitorQueryKpiRepository.findAll().size();

        // Create the MonitorQueryKpi with an existing ID
        monitorQueryKpi.setId(1L);
        MonitorQueryKpiDTO monitorQueryKpiDTO = monitorQueryKpiMapper.toDto(monitorQueryKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitorQueryKpiMockMvc.perform(post("/api/monitor-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitorQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitorQueryKpi in the database
        List<MonitorQueryKpi> monitorQueryKpiList = monitorQueryKpiRepository.findAll();
        assertThat(monitorQueryKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMonitorQueryKpis() throws Exception {
        // Initialize the database
        monitorQueryKpiRepository.saveAndFlush(monitorQueryKpi);

        // Get all the monitorQueryKpiList
        restMonitorQueryKpiMockMvc.perform(get("/api/monitor-query-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitorQueryKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].queryKpiId").value(hasItem(DEFAULT_QUERY_KPI_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].runTimeSucc").value(hasItem(DEFAULT_RUN_TIME_SUCC.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getMonitorQueryKpi() throws Exception {
        // Initialize the database
        monitorQueryKpiRepository.saveAndFlush(monitorQueryKpi);

        // Get the monitorQueryKpi
        restMonitorQueryKpiMockMvc.perform(get("/api/monitor-query-kpis/{id}", monitorQueryKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(monitorQueryKpi.getId().intValue()))
            .andExpect(jsonPath("$.queryKpiId").value(DEFAULT_QUERY_KPI_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.runTimeSucc").value(DEFAULT_RUN_TIME_SUCC.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMonitorQueryKpi() throws Exception {
        // Get the monitorQueryKpi
        restMonitorQueryKpiMockMvc.perform(get("/api/monitor-query-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMonitorQueryKpi() throws Exception {
        // Initialize the database
        monitorQueryKpiRepository.saveAndFlush(monitorQueryKpi);

        int databaseSizeBeforeUpdate = monitorQueryKpiRepository.findAll().size();

        // Update the monitorQueryKpi
        MonitorQueryKpi updatedMonitorQueryKpi = monitorQueryKpiRepository.findById(monitorQueryKpi.getId()).get();
        // Disconnect from session so that the updates on updatedMonitorQueryKpi are not directly saved in db
        em.detach(updatedMonitorQueryKpi);
        updatedMonitorQueryKpi
            .queryKpiId(UPDATED_QUERY_KPI_ID)
            .status(UPDATED_STATUS)
            .runTimeSucc(UPDATED_RUN_TIME_SUCC)
            .updateTime(UPDATED_UPDATE_TIME);
        MonitorQueryKpiDTO monitorQueryKpiDTO = monitorQueryKpiMapper.toDto(updatedMonitorQueryKpi);

        restMonitorQueryKpiMockMvc.perform(put("/api/monitor-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitorQueryKpiDTO)))
            .andExpect(status().isOk());

        // Validate the MonitorQueryKpi in the database
        List<MonitorQueryKpi> monitorQueryKpiList = monitorQueryKpiRepository.findAll();
        assertThat(monitorQueryKpiList).hasSize(databaseSizeBeforeUpdate);
        MonitorQueryKpi testMonitorQueryKpi = monitorQueryKpiList.get(monitorQueryKpiList.size() - 1);
        assertThat(testMonitorQueryKpi.getQueryKpiId()).isEqualTo(UPDATED_QUERY_KPI_ID);
        assertThat(testMonitorQueryKpi.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMonitorQueryKpi.getRunTimeSucc()).isEqualTo(UPDATED_RUN_TIME_SUCC);
        assertThat(testMonitorQueryKpi.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingMonitorQueryKpi() throws Exception {
        int databaseSizeBeforeUpdate = monitorQueryKpiRepository.findAll().size();

        // Create the MonitorQueryKpi
        MonitorQueryKpiDTO monitorQueryKpiDTO = monitorQueryKpiMapper.toDto(monitorQueryKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitorQueryKpiMockMvc.perform(put("/api/monitor-query-kpis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monitorQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitorQueryKpi in the database
        List<MonitorQueryKpi> monitorQueryKpiList = monitorQueryKpiRepository.findAll();
        assertThat(monitorQueryKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMonitorQueryKpi() throws Exception {
        // Initialize the database
        monitorQueryKpiRepository.saveAndFlush(monitorQueryKpi);

        int databaseSizeBeforeDelete = monitorQueryKpiRepository.findAll().size();

        // Delete the monitorQueryKpi
        restMonitorQueryKpiMockMvc.perform(delete("/api/monitor-query-kpis/{id}", monitorQueryKpi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MonitorQueryKpi> monitorQueryKpiList = monitorQueryKpiRepository.findAll();
        assertThat(monitorQueryKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
