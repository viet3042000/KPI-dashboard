package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.FlagRunQueryKpi;
import com.b4t.app.repository.FlagRunQueryKpiRepository;
import com.b4t.app.service.FlagRunQueryKpiService;
import com.b4t.app.service.dto.FlagRunQueryKpiDTO;
import com.b4t.app.service.mapper.FlagRunQueryKpiMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FlagRunQueryKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class FlagRunQueryKpiResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

    private static final Long DEFAULT_PRD_ID = 1L;
    private static final Long UPDATED_PRD_ID = 2L;

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FlagRunQueryKpiRepository flagRunQueryKpiRepository;

    @Autowired
    private FlagRunQueryKpiMapper flagRunQueryKpiMapper;

    @Autowired
    private FlagRunQueryKpiService flagRunQueryKpiService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlagRunQueryKpiMockMvc;

    private FlagRunQueryKpi flagRunQueryKpi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlagRunQueryKpi createEntity(EntityManager em) {
        FlagRunQueryKpi flagRunQueryKpi = new FlagRunQueryKpi()
            .tableName(DEFAULT_TABLE_NAME)
            .kpiId(DEFAULT_KPI_ID)
            .prdId(DEFAULT_PRD_ID)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME);
        return flagRunQueryKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlagRunQueryKpi createUpdatedEntity(EntityManager em) {
        FlagRunQueryKpi flagRunQueryKpi = new FlagRunQueryKpi()
            .tableName(UPDATED_TABLE_NAME)
            .kpiId(UPDATED_KPI_ID)
            .prdId(UPDATED_PRD_ID)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME);
        return flagRunQueryKpi;
    }

    @BeforeEach
    public void initTest() {
        flagRunQueryKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlagRunQueryKpi() throws Exception {
        int databaseSizeBeforeCreate = flagRunQueryKpiRepository.findAll().size();

        // Create the FlagRunQueryKpi
        FlagRunQueryKpiDTO flagRunQueryKpiDTO = flagRunQueryKpiMapper.toDto(flagRunQueryKpi);
        restFlagRunQueryKpiMockMvc.perform(post("/api/flag-run-query-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flagRunQueryKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the FlagRunQueryKpi in the database
        List<FlagRunQueryKpi> flagRunQueryKpiList = flagRunQueryKpiRepository.findAll();
        assertThat(flagRunQueryKpiList).hasSize(databaseSizeBeforeCreate + 1);
        FlagRunQueryKpi testFlagRunQueryKpi = flagRunQueryKpiList.get(flagRunQueryKpiList.size() - 1);
        assertThat(testFlagRunQueryKpi.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testFlagRunQueryKpi.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testFlagRunQueryKpi.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testFlagRunQueryKpi.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFlagRunQueryKpi.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createFlagRunQueryKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flagRunQueryKpiRepository.findAll().size();

        // Create the FlagRunQueryKpi with an existing ID
        flagRunQueryKpi.setId(1L);
        FlagRunQueryKpiDTO flagRunQueryKpiDTO = flagRunQueryKpiMapper.toDto(flagRunQueryKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlagRunQueryKpiMockMvc.perform(post("/api/flag-run-query-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flagRunQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FlagRunQueryKpi in the database
        List<FlagRunQueryKpi> flagRunQueryKpiList = flagRunQueryKpiRepository.findAll();
        assertThat(flagRunQueryKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFlagRunQueryKpis() throws Exception {
        // Initialize the database
        flagRunQueryKpiRepository.saveAndFlush(flagRunQueryKpi);

        // Get all the flagRunQueryKpiList
        restFlagRunQueryKpiMockMvc.perform(get("/api/flag-run-query-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flagRunQueryKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID.intValue())))
            .andExpect(jsonPath("$.[*].prdId").value(hasItem(DEFAULT_PRD_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getFlagRunQueryKpi() throws Exception {
        // Initialize the database
        flagRunQueryKpiRepository.saveAndFlush(flagRunQueryKpi);

        // Get the flagRunQueryKpi
        restFlagRunQueryKpiMockMvc.perform(get("/api/flag-run-query-kpis/{id}", flagRunQueryKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flagRunQueryKpi.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID.intValue()))
            .andExpect(jsonPath("$.prdId").value(DEFAULT_PRD_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFlagRunQueryKpi() throws Exception {
        // Get the flagRunQueryKpi
        restFlagRunQueryKpiMockMvc.perform(get("/api/flag-run-query-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlagRunQueryKpi() throws Exception {
        // Initialize the database
        flagRunQueryKpiRepository.saveAndFlush(flagRunQueryKpi);

        int databaseSizeBeforeUpdate = flagRunQueryKpiRepository.findAll().size();

        // Update the flagRunQueryKpi
        FlagRunQueryKpi updatedFlagRunQueryKpi = flagRunQueryKpiRepository.findById(flagRunQueryKpi.getId()).get();
        // Disconnect from session so that the updates on updatedFlagRunQueryKpi are not directly saved in db
        em.detach(updatedFlagRunQueryKpi);
        updatedFlagRunQueryKpi
            .tableName(UPDATED_TABLE_NAME)
            .kpiId(UPDATED_KPI_ID)
            .prdId(UPDATED_PRD_ID)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME);
        FlagRunQueryKpiDTO flagRunQueryKpiDTO = flagRunQueryKpiMapper.toDto(updatedFlagRunQueryKpi);

        restFlagRunQueryKpiMockMvc.perform(put("/api/flag-run-query-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flagRunQueryKpiDTO)))
            .andExpect(status().isOk());

        // Validate the FlagRunQueryKpi in the database
        List<FlagRunQueryKpi> flagRunQueryKpiList = flagRunQueryKpiRepository.findAll();
        assertThat(flagRunQueryKpiList).hasSize(databaseSizeBeforeUpdate);
        FlagRunQueryKpi testFlagRunQueryKpi = flagRunQueryKpiList.get(flagRunQueryKpiList.size() - 1);
        assertThat(testFlagRunQueryKpi.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testFlagRunQueryKpi.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testFlagRunQueryKpi.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testFlagRunQueryKpi.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFlagRunQueryKpi.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingFlagRunQueryKpi() throws Exception {
        int databaseSizeBeforeUpdate = flagRunQueryKpiRepository.findAll().size();

        // Create the FlagRunQueryKpi
        FlagRunQueryKpiDTO flagRunQueryKpiDTO = flagRunQueryKpiMapper.toDto(flagRunQueryKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlagRunQueryKpiMockMvc.perform(put("/api/flag-run-query-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flagRunQueryKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FlagRunQueryKpi in the database
        List<FlagRunQueryKpi> flagRunQueryKpiList = flagRunQueryKpiRepository.findAll();
        assertThat(flagRunQueryKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFlagRunQueryKpi() throws Exception {
        // Initialize the database
        flagRunQueryKpiRepository.saveAndFlush(flagRunQueryKpi);

        int databaseSizeBeforeDelete = flagRunQueryKpiRepository.findAll().size();

        // Delete the flagRunQueryKpi
        restFlagRunQueryKpiMockMvc.perform(delete("/api/flag-run-query-kpis/{id}", flagRunQueryKpi.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlagRunQueryKpi> flagRunQueryKpiList = flagRunQueryKpiRepository.findAll();
        assertThat(flagRunQueryKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
