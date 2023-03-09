package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.RpReport;
import com.b4t.app.repository.RpReportRepository;
import com.b4t.app.service.RpReportService;
import com.b4t.app.service.dto.RpReportDTO;
import com.b4t.app.service.mapper.RpReportMapper;

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
 * Integration tests for the {@link RpReportResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WithMockUser
public class RpReportResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_PRD_ID = 1;
    private static final Integer UPDATED_PRD_ID = 2;

    private static final Integer DEFAULT_TIME_TYPE = 1;
    private static final Integer UPDATED_TIME_TYPE = 2;

    @Autowired
    private RpReportRepository rpReportRepository;

    @Autowired
    private RpReportMapper rpReportMapper;

    @Autowired
    private RpReportService rpReportService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRpReportMockMvc;

    private RpReport rpReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpReport createEntity(EntityManager em) {
        RpReport rpReport = new RpReport()
            .reportName(DEFAULT_REPORT_NAME)
            .reportCode(DEFAULT_REPORT_CODE)
            .updateTime(DEFAULT_UPDATE_TIME)
            .prdId(DEFAULT_PRD_ID)
            .timeType(DEFAULT_TIME_TYPE);
        return rpReport;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RpReport createUpdatedEntity(EntityManager em) {
        RpReport rpReport = new RpReport()
            .reportName(UPDATED_REPORT_NAME)
            .reportCode(UPDATED_REPORT_CODE)
            .updateTime(UPDATED_UPDATE_TIME)
            .prdId(UPDATED_PRD_ID)
            .timeType(UPDATED_TIME_TYPE);
        return rpReport;
    }

    @BeforeEach
    public void initTest() {
        rpReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createRpReport() throws Exception {
        int databaseSizeBeforeCreate = rpReportRepository.findAll().size();
        // Create the RpReport
        RpReportDTO rpReportDTO = rpReportMapper.toDto(rpReport);
        restRpReportMockMvc.perform(post("/api/rp-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rpReportDTO)))
            .andExpect(status().isCreated());

        // Validate the RpReport in the database
        List<RpReport> rpReportList = rpReportRepository.findAll();
        assertThat(rpReportList).hasSize(databaseSizeBeforeCreate + 1);
        RpReport testRpReport = rpReportList.get(rpReportList.size() - 1);
        assertThat(testRpReport.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testRpReport.getReportCode()).isEqualTo(DEFAULT_REPORT_CODE);
        assertThat(testRpReport.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testRpReport.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testRpReport.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
    }

    @Test
    @Transactional
    public void createRpReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rpReportRepository.findAll().size();

        // Create the RpReport with an existing ID
        rpReport.setId(1L);
        RpReportDTO rpReportDTO = rpReportMapper.toDto(rpReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRpReportMockMvc.perform(post("/api/rp-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rpReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpReport in the database
        List<RpReport> rpReportList = rpReportRepository.findAll();
        assertThat(rpReportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRpReports() throws Exception {
        // Initialize the database
        rpReportRepository.saveAndFlush(rpReport);

        // Get all the rpReportList
        restRpReportMockMvc.perform(get("/api/rp-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rpReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportCode").value(hasItem(DEFAULT_REPORT_CODE)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].prdId").value(hasItem(DEFAULT_PRD_ID)))
            .andExpect(jsonPath("$.[*].timeType").value(hasItem(DEFAULT_TIME_TYPE)));
    }
    
    @Test
    @Transactional
    public void getRpReport() throws Exception {
        // Initialize the database
        rpReportRepository.saveAndFlush(rpReport);

        // Get the rpReport
        restRpReportMockMvc.perform(get("/api/rp-reports/{id}", rpReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rpReport.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportCode").value(DEFAULT_REPORT_CODE))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.prdId").value(DEFAULT_PRD_ID))
            .andExpect(jsonPath("$.timeType").value(DEFAULT_TIME_TYPE));
    }
    @Test
    @Transactional
    public void getNonExistingRpReport() throws Exception {
        // Get the rpReport
        restRpReportMockMvc.perform(get("/api/rp-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRpReport() throws Exception {
        // Initialize the database
        rpReportRepository.saveAndFlush(rpReport);

        int databaseSizeBeforeUpdate = rpReportRepository.findAll().size();

        // Update the rpReport
        RpReport updatedRpReport = rpReportRepository.findById(rpReport.getId()).get();
        // Disconnect from session so that the updates on updatedRpReport are not directly saved in db
        em.detach(updatedRpReport);
        updatedRpReport
            .reportName(UPDATED_REPORT_NAME)
            .reportCode(UPDATED_REPORT_CODE)
            .updateTime(UPDATED_UPDATE_TIME)
            .prdId(UPDATED_PRD_ID)
            .timeType(UPDATED_TIME_TYPE);
        RpReportDTO rpReportDTO = rpReportMapper.toDto(updatedRpReport);

        restRpReportMockMvc.perform(put("/api/rp-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rpReportDTO)))
            .andExpect(status().isOk());

        // Validate the RpReport in the database
        List<RpReport> rpReportList = rpReportRepository.findAll();
        assertThat(rpReportList).hasSize(databaseSizeBeforeUpdate);
        RpReport testRpReport = rpReportList.get(rpReportList.size() - 1);
        assertThat(testRpReport.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testRpReport.getReportCode()).isEqualTo(UPDATED_REPORT_CODE);
        assertThat(testRpReport.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testRpReport.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testRpReport.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRpReport() throws Exception {
        int databaseSizeBeforeUpdate = rpReportRepository.findAll().size();

        // Create the RpReport
        RpReportDTO rpReportDTO = rpReportMapper.toDto(rpReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRpReportMockMvc.perform(put("/api/rp-reports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rpReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RpReport in the database
        List<RpReport> rpReportList = rpReportRepository.findAll();
        assertThat(rpReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRpReport() throws Exception {
        // Initialize the database
        rpReportRepository.saveAndFlush(rpReport);

        int databaseSizeBeforeDelete = rpReportRepository.findAll().size();

        // Delete the rpReport
        restRpReportMockMvc.perform(delete("/api/rp-reports/{id}", rpReport.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RpReport> rpReportList = rpReportRepository.findAll();
        assertThat(rpReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
