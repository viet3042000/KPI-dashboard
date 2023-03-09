package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.BieumauKehoachchitieu;
import com.b4t.app.repository.BieumauKehoachchitieuRepository;
import com.b4t.app.service.BieumauKehoachchitieuService;
import com.b4t.app.service.dto.BieumauKehoachchitieuDTO;
import com.b4t.app.service.mapper.BieumauKehoachchitieuMapper;

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
 * Integration tests for the {@link BieumauKehoachchitieuResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class BieumauKehoachchitieuResourceIT {

    private static final Long DEFAULT_PRD_ID = 1L;
    private static final Long UPDATED_PRD_ID = 2L;

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

    private static final String DEFAULT_KPI_CODE = "AAAAAAAAAA";
    private static final String UPDATED_KPI_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_KPI_NAME = "AAAAAAAAAA";
    private static final String UPDATED_KPI_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_VAL_PLAN = 1D;
    private static final Double UPDATED_VAL_PLAN = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_RANK = 1D;
    private static final Double UPDATED_TOTAL_RANK = 2D;

    @Autowired
    private BieumauKehoachchitieuRepository bieumauKehoachchitieuRepository;

    @Autowired
    private BieumauKehoachchitieuMapper bieumauKehoachchitieuMapper;

    @Autowired
    private BieumauKehoachchitieuService bieumauKehoachchitieuService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBieumauKehoachchitieuMockMvc;

    private BieumauKehoachchitieu bieumauKehoachchitieu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BieumauKehoachchitieu createEntity(EntityManager em) {
        BieumauKehoachchitieu bieumauKehoachchitieu = new BieumauKehoachchitieu()
            .prdId(DEFAULT_PRD_ID)
            .kpiId(DEFAULT_KPI_ID)
            .kpiCode(DEFAULT_KPI_CODE)
            .kpiName(DEFAULT_KPI_NAME)
            .valPlan(DEFAULT_VAL_PLAN)
            .description(DEFAULT_DESCRIPTION)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER)
            .totalRank(DEFAULT_TOTAL_RANK);
        return bieumauKehoachchitieu;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BieumauKehoachchitieu createUpdatedEntity(EntityManager em) {
        BieumauKehoachchitieu bieumauKehoachchitieu = new BieumauKehoachchitieu()
            .prdId(UPDATED_PRD_ID)
            .kpiId(UPDATED_KPI_ID)
            .kpiCode(UPDATED_KPI_CODE)
            .kpiName(UPDATED_KPI_NAME)
            .valPlan(UPDATED_VAL_PLAN)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER)
            .totalRank(UPDATED_TOTAL_RANK);
        return bieumauKehoachchitieu;
    }

    @BeforeEach
    public void initTest() {
        bieumauKehoachchitieu = createEntity(em);
    }

    @Test
    @Transactional
    public void createBieumauKehoachchitieu() throws Exception {
        int databaseSizeBeforeCreate = bieumauKehoachchitieuRepository.findAll().size();

        // Create the BieumauKehoachchitieu
        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = bieumauKehoachchitieuMapper.toDto(bieumauKehoachchitieu);
        restBieumauKehoachchitieuMockMvc.perform(post("/api/bieumau-kehoachchitieus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bieumauKehoachchitieuDTO)))
            .andExpect(status().isCreated());

        // Validate the BieumauKehoachchitieu in the database
        List<BieumauKehoachchitieu> bieumauKehoachchitieuList = bieumauKehoachchitieuRepository.findAll();
        assertThat(bieumauKehoachchitieuList).hasSize(databaseSizeBeforeCreate + 1);
        BieumauKehoachchitieu testBieumauKehoachchitieu = bieumauKehoachchitieuList.get(bieumauKehoachchitieuList.size() - 1);
        assertThat(testBieumauKehoachchitieu.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testBieumauKehoachchitieu.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testBieumauKehoachchitieu.getKpiCode()).isEqualTo(DEFAULT_KPI_CODE);
        assertThat(testBieumauKehoachchitieu.getKpiName()).isEqualTo(DEFAULT_KPI_NAME);
        assertThat(testBieumauKehoachchitieu.getValPlan()).isEqualTo(DEFAULT_VAL_PLAN);
        assertThat(testBieumauKehoachchitieu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBieumauKehoachchitieu.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testBieumauKehoachchitieu.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
        assertThat(testBieumauKehoachchitieu.getTotalRank()).isEqualTo(DEFAULT_TOTAL_RANK);
    }

    @Test
    @Transactional
    public void createBieumauKehoachchitieuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bieumauKehoachchitieuRepository.findAll().size();

        // Create the BieumauKehoachchitieu with an existing ID
        bieumauKehoachchitieu.setId(1L);
        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = bieumauKehoachchitieuMapper.toDto(bieumauKehoachchitieu);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBieumauKehoachchitieuMockMvc.perform(post("/api/bieumau-kehoachchitieus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bieumauKehoachchitieuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BieumauKehoachchitieu in the database
        List<BieumauKehoachchitieu> bieumauKehoachchitieuList = bieumauKehoachchitieuRepository.findAll();
        assertThat(bieumauKehoachchitieuList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBieumauKehoachchitieus() throws Exception {
        // Initialize the database
        bieumauKehoachchitieuRepository.saveAndFlush(bieumauKehoachchitieu);

        // Get all the bieumauKehoachchitieuList
        restBieumauKehoachchitieuMockMvc.perform(get("/api/bieumau-kehoachchitieus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bieumauKehoachchitieu.getId().intValue())))
            .andExpect(jsonPath("$.[*].prdId").value(hasItem(DEFAULT_PRD_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiCode").value(hasItem(DEFAULT_KPI_CODE)))
            .andExpect(jsonPath("$.[*].kpiName").value(hasItem(DEFAULT_KPI_NAME)))
            .andExpect(jsonPath("$.[*].valPlan").value(hasItem(DEFAULT_VAL_PLAN.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)))
            .andExpect(jsonPath("$.[*].totalRank").value(hasItem(DEFAULT_TOTAL_RANK.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getBieumauKehoachchitieu() throws Exception {
        // Initialize the database
        bieumauKehoachchitieuRepository.saveAndFlush(bieumauKehoachchitieu);

        // Get the bieumauKehoachchitieu
        restBieumauKehoachchitieuMockMvc.perform(get("/api/bieumau-kehoachchitieus/{id}", bieumauKehoachchitieu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bieumauKehoachchitieu.getId().intValue()))
            .andExpect(jsonPath("$.prdId").value(DEFAULT_PRD_ID.intValue()))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID.intValue()))
            .andExpect(jsonPath("$.kpiCode").value(DEFAULT_KPI_CODE))
            .andExpect(jsonPath("$.kpiName").value(DEFAULT_KPI_NAME))
            .andExpect(jsonPath("$.valPlan").value(DEFAULT_VAL_PLAN.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER))
            .andExpect(jsonPath("$.totalRank").value(DEFAULT_TOTAL_RANK.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBieumauKehoachchitieu() throws Exception {
        // Get the bieumauKehoachchitieu
        restBieumauKehoachchitieuMockMvc.perform(get("/api/bieumau-kehoachchitieus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBieumauKehoachchitieu() throws Exception {
        // Initialize the database
        bieumauKehoachchitieuRepository.saveAndFlush(bieumauKehoachchitieu);

        int databaseSizeBeforeUpdate = bieumauKehoachchitieuRepository.findAll().size();

        // Update the bieumauKehoachchitieu
        BieumauKehoachchitieu updatedBieumauKehoachchitieu = bieumauKehoachchitieuRepository.findById(bieumauKehoachchitieu.getId()).get();
        // Disconnect from session so that the updates on updatedBieumauKehoachchitieu are not directly saved in db
        em.detach(updatedBieumauKehoachchitieu);
        updatedBieumauKehoachchitieu
            .prdId(UPDATED_PRD_ID)
            .kpiId(UPDATED_KPI_ID)
            .kpiCode(UPDATED_KPI_CODE)
            .kpiName(UPDATED_KPI_NAME)
            .valPlan(UPDATED_VAL_PLAN)
            .description(UPDATED_DESCRIPTION)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER)
            .totalRank(UPDATED_TOTAL_RANK);
        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = bieumauKehoachchitieuMapper.toDto(updatedBieumauKehoachchitieu);

        restBieumauKehoachchitieuMockMvc.perform(put("/api/bieumau-kehoachchitieus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bieumauKehoachchitieuDTO)))
            .andExpect(status().isOk());

        // Validate the BieumauKehoachchitieu in the database
        List<BieumauKehoachchitieu> bieumauKehoachchitieuList = bieumauKehoachchitieuRepository.findAll();
        assertThat(bieumauKehoachchitieuList).hasSize(databaseSizeBeforeUpdate);
        BieumauKehoachchitieu testBieumauKehoachchitieu = bieumauKehoachchitieuList.get(bieumauKehoachchitieuList.size() - 1);
        assertThat(testBieumauKehoachchitieu.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testBieumauKehoachchitieu.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testBieumauKehoachchitieu.getKpiCode()).isEqualTo(UPDATED_KPI_CODE);
        assertThat(testBieumauKehoachchitieu.getKpiName()).isEqualTo(UPDATED_KPI_NAME);
        assertThat(testBieumauKehoachchitieu.getValPlan()).isEqualTo(UPDATED_VAL_PLAN);
        assertThat(testBieumauKehoachchitieu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBieumauKehoachchitieu.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testBieumauKehoachchitieu.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
        assertThat(testBieumauKehoachchitieu.getTotalRank()).isEqualTo(UPDATED_TOTAL_RANK);
    }

    @Test
    @Transactional
    public void updateNonExistingBieumauKehoachchitieu() throws Exception {
        int databaseSizeBeforeUpdate = bieumauKehoachchitieuRepository.findAll().size();

        // Create the BieumauKehoachchitieu
        BieumauKehoachchitieuDTO bieumauKehoachchitieuDTO = bieumauKehoachchitieuMapper.toDto(bieumauKehoachchitieu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBieumauKehoachchitieuMockMvc.perform(put("/api/bieumau-kehoachchitieus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bieumauKehoachchitieuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BieumauKehoachchitieu in the database
        List<BieumauKehoachchitieu> bieumauKehoachchitieuList = bieumauKehoachchitieuRepository.findAll();
        assertThat(bieumauKehoachchitieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBieumauKehoachchitieu() throws Exception {
        // Initialize the database
        bieumauKehoachchitieuRepository.saveAndFlush(bieumauKehoachchitieu);

        int databaseSizeBeforeDelete = bieumauKehoachchitieuRepository.findAll().size();

        // Delete the bieumauKehoachchitieu
        restBieumauKehoachchitieuMockMvc.perform(delete("/api/bieumau-kehoachchitieus/{id}", bieumauKehoachchitieu.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BieumauKehoachchitieu> bieumauKehoachchitieuList = bieumauKehoachchitieuRepository.findAll();
        assertThat(bieumauKehoachchitieuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
