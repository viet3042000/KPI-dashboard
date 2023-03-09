package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.RawDataFromMicUnit;
import com.b4t.app.repository.RawDataFromMicUnitRepository;
import com.b4t.app.service.RawDataFromMicUnitService;
import com.b4t.app.service.dto.RawDataFromMicUnitDTO;
import com.b4t.app.service.mapper.RawDataFromMicUnitMapper;

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
 * Integration tests for the {@link RawDataFromMicUnitResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class RawDataFromMicUnitResourceIT {

    private static final Long DEFAULT_TIME_TYPE = 1L;
    private static final Long UPDATED_TIME_TYPE = 2L;

    private static final Long DEFAULT_INPUT_LEVEL = 1L;
    private static final Long UPDATED_INPUT_LEVEL = 2L;

    private static final Long DEFAULT_PRD_ID = 1L;
    private static final Long UPDATED_PRD_ID = 2L;

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

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

    private static final Double DEFAULT_VAL = 1D;
    private static final Double UPDATED_VAL = 2D;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RawDataFromMicUnitRepository rawDataFromMicUnitRepository;

    @Autowired
    private RawDataFromMicUnitMapper rawDataFromMicUnitMapper;

    @Autowired
    private RawDataFromMicUnitService rawDataFromMicUnitService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRawDataFromMicUnitMockMvc;

    private RawDataFromMicUnit rawDataFromMicUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RawDataFromMicUnit createEntity(EntityManager em) {
        RawDataFromMicUnit rawDataFromMicUnit = new RawDataFromMicUnit()
            .timeType(DEFAULT_TIME_TYPE)
            .inputLevel(DEFAULT_INPUT_LEVEL)
            .prdId(DEFAULT_PRD_ID)
            .kpiId(DEFAULT_KPI_ID)
            .kpiName(DEFAULT_KPI_NAME)
            .objCode(DEFAULT_OBJ_CODE)
            .objName(DEFAULT_OBJ_NAME)
            .parentCode(DEFAULT_PARENT_CODE)
            .parentName(DEFAULT_PARENT_NAME)
            .val(DEFAULT_VAL)
            .updateTime(DEFAULT_UPDATE_TIME);
        return rawDataFromMicUnit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RawDataFromMicUnit createUpdatedEntity(EntityManager em) {
        RawDataFromMicUnit rawDataFromMicUnit = new RawDataFromMicUnit()
            .timeType(UPDATED_TIME_TYPE)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .prdId(UPDATED_PRD_ID)
            .kpiId(UPDATED_KPI_ID)
            .kpiName(UPDATED_KPI_NAME)
            .objCode(UPDATED_OBJ_CODE)
            .objName(UPDATED_OBJ_NAME)
            .parentCode(UPDATED_PARENT_CODE)
            .parentName(UPDATED_PARENT_NAME)
            .val(UPDATED_VAL)
            .updateTime(UPDATED_UPDATE_TIME);
        return rawDataFromMicUnit;
    }

    @BeforeEach
    public void initTest() {
        rawDataFromMicUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createRawDataFromMicUnit() throws Exception {
        int databaseSizeBeforeCreate = rawDataFromMicUnitRepository.findAll().size();

        // Create the RawDataFromMicUnit
        RawDataFromMicUnitDTO rawDataFromMicUnitDTO = rawDataFromMicUnitMapper.toDto(rawDataFromMicUnit);
        restRawDataFromMicUnitMockMvc.perform(post("/api/raw-data-from-mic-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rawDataFromMicUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the RawDataFromMicUnit in the database
        List<RawDataFromMicUnit> rawDataFromMicUnitList = rawDataFromMicUnitRepository.findAll();
        assertThat(rawDataFromMicUnitList).hasSize(databaseSizeBeforeCreate + 1);
        RawDataFromMicUnit testRawDataFromMicUnit = rawDataFromMicUnitList.get(rawDataFromMicUnitList.size() - 1);
        assertThat(testRawDataFromMicUnit.getTimeType()).isEqualTo(DEFAULT_TIME_TYPE);
        assertThat(testRawDataFromMicUnit.getInputLevel()).isEqualTo(DEFAULT_INPUT_LEVEL);
        assertThat(testRawDataFromMicUnit.getPrdId()).isEqualTo(DEFAULT_PRD_ID);
        assertThat(testRawDataFromMicUnit.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testRawDataFromMicUnit.getKpiName()).isEqualTo(DEFAULT_KPI_NAME);
        assertThat(testRawDataFromMicUnit.getObjCode()).isEqualTo(DEFAULT_OBJ_CODE);
        assertThat(testRawDataFromMicUnit.getObjName()).isEqualTo(DEFAULT_OBJ_NAME);
        assertThat(testRawDataFromMicUnit.getParentCode()).isEqualTo(DEFAULT_PARENT_CODE);
        assertThat(testRawDataFromMicUnit.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testRawDataFromMicUnit.getVal()).isEqualTo(DEFAULT_VAL);
        assertThat(testRawDataFromMicUnit.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createRawDataFromMicUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rawDataFromMicUnitRepository.findAll().size();

        // Create the RawDataFromMicUnit with an existing ID
        rawDataFromMicUnit.setId(1L);
        RawDataFromMicUnitDTO rawDataFromMicUnitDTO = rawDataFromMicUnitMapper.toDto(rawDataFromMicUnit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRawDataFromMicUnitMockMvc.perform(post("/api/raw-data-from-mic-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rawDataFromMicUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RawDataFromMicUnit in the database
        List<RawDataFromMicUnit> rawDataFromMicUnitList = rawDataFromMicUnitRepository.findAll();
        assertThat(rawDataFromMicUnitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRawDataFromMicUnits() throws Exception {
        // Initialize the database
        rawDataFromMicUnitRepository.saveAndFlush(rawDataFromMicUnit);

        // Get all the rawDataFromMicUnitList
        restRawDataFromMicUnitMockMvc.perform(get("/api/raw-data-from-mic-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rawDataFromMicUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeType").value(hasItem(DEFAULT_TIME_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].inputLevel").value(hasItem(DEFAULT_INPUT_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].prdId").value(hasItem(DEFAULT_PRD_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID.intValue())))
            .andExpect(jsonPath("$.[*].kpiName").value(hasItem(DEFAULT_KPI_NAME)))
            .andExpect(jsonPath("$.[*].objCode").value(hasItem(DEFAULT_OBJ_CODE)))
            .andExpect(jsonPath("$.[*].objName").value(hasItem(DEFAULT_OBJ_NAME)))
            .andExpect(jsonPath("$.[*].parentCode").value(hasItem(DEFAULT_PARENT_CODE)))
            .andExpect(jsonPath("$.[*].parentName").value(hasItem(DEFAULT_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].val").value(hasItem(DEFAULT_VAL.doubleValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getRawDataFromMicUnit() throws Exception {
        // Initialize the database
        rawDataFromMicUnitRepository.saveAndFlush(rawDataFromMicUnit);

        // Get the rawDataFromMicUnit
        restRawDataFromMicUnitMockMvc.perform(get("/api/raw-data-from-mic-units/{id}", rawDataFromMicUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rawDataFromMicUnit.getId().intValue()))
            .andExpect(jsonPath("$.timeType").value(DEFAULT_TIME_TYPE.intValue()))
            .andExpect(jsonPath("$.inputLevel").value(DEFAULT_INPUT_LEVEL.intValue()))
            .andExpect(jsonPath("$.prdId").value(DEFAULT_PRD_ID.intValue()))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID.intValue()))
            .andExpect(jsonPath("$.kpiName").value(DEFAULT_KPI_NAME))
            .andExpect(jsonPath("$.objCode").value(DEFAULT_OBJ_CODE))
            .andExpect(jsonPath("$.objName").value(DEFAULT_OBJ_NAME))
            .andExpect(jsonPath("$.parentCode").value(DEFAULT_PARENT_CODE))
            .andExpect(jsonPath("$.parentName").value(DEFAULT_PARENT_NAME))
            .andExpect(jsonPath("$.val").value(DEFAULT_VAL.doubleValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRawDataFromMicUnit() throws Exception {
        // Get the rawDataFromMicUnit
        restRawDataFromMicUnitMockMvc.perform(get("/api/raw-data-from-mic-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRawDataFromMicUnit() throws Exception {
        // Initialize the database
        rawDataFromMicUnitRepository.saveAndFlush(rawDataFromMicUnit);

        int databaseSizeBeforeUpdate = rawDataFromMicUnitRepository.findAll().size();

        // Update the rawDataFromMicUnit
        RawDataFromMicUnit updatedRawDataFromMicUnit = rawDataFromMicUnitRepository.findById(rawDataFromMicUnit.getId()).get();
        // Disconnect from session so that the updates on updatedRawDataFromMicUnit are not directly saved in db
        em.detach(updatedRawDataFromMicUnit);
        updatedRawDataFromMicUnit
            .timeType(UPDATED_TIME_TYPE)
            .inputLevel(UPDATED_INPUT_LEVEL)
            .prdId(UPDATED_PRD_ID)
            .kpiId(UPDATED_KPI_ID)
            .kpiName(UPDATED_KPI_NAME)
            .objCode(UPDATED_OBJ_CODE)
            .objName(UPDATED_OBJ_NAME)
            .parentCode(UPDATED_PARENT_CODE)
            .parentName(UPDATED_PARENT_NAME)
            .val(UPDATED_VAL)
            .updateTime(UPDATED_UPDATE_TIME);
        RawDataFromMicUnitDTO rawDataFromMicUnitDTO = rawDataFromMicUnitMapper.toDto(updatedRawDataFromMicUnit);

        restRawDataFromMicUnitMockMvc.perform(put("/api/raw-data-from-mic-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rawDataFromMicUnitDTO)))
            .andExpect(status().isOk());

        // Validate the RawDataFromMicUnit in the database
        List<RawDataFromMicUnit> rawDataFromMicUnitList = rawDataFromMicUnitRepository.findAll();
        assertThat(rawDataFromMicUnitList).hasSize(databaseSizeBeforeUpdate);
        RawDataFromMicUnit testRawDataFromMicUnit = rawDataFromMicUnitList.get(rawDataFromMicUnitList.size() - 1);
        assertThat(testRawDataFromMicUnit.getTimeType()).isEqualTo(UPDATED_TIME_TYPE);
        assertThat(testRawDataFromMicUnit.getInputLevel()).isEqualTo(UPDATED_INPUT_LEVEL);
        assertThat(testRawDataFromMicUnit.getPrdId()).isEqualTo(UPDATED_PRD_ID);
        assertThat(testRawDataFromMicUnit.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testRawDataFromMicUnit.getKpiName()).isEqualTo(UPDATED_KPI_NAME);
        assertThat(testRawDataFromMicUnit.getObjCode()).isEqualTo(UPDATED_OBJ_CODE);
        assertThat(testRawDataFromMicUnit.getObjName()).isEqualTo(UPDATED_OBJ_NAME);
        assertThat(testRawDataFromMicUnit.getParentCode()).isEqualTo(UPDATED_PARENT_CODE);
        assertThat(testRawDataFromMicUnit.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testRawDataFromMicUnit.getVal()).isEqualTo(UPDATED_VAL);
        assertThat(testRawDataFromMicUnit.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingRawDataFromMicUnit() throws Exception {
        int databaseSizeBeforeUpdate = rawDataFromMicUnitRepository.findAll().size();

        // Create the RawDataFromMicUnit
        RawDataFromMicUnitDTO rawDataFromMicUnitDTO = rawDataFromMicUnitMapper.toDto(rawDataFromMicUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRawDataFromMicUnitMockMvc.perform(put("/api/raw-data-from-mic-units")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rawDataFromMicUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RawDataFromMicUnit in the database
        List<RawDataFromMicUnit> rawDataFromMicUnitList = rawDataFromMicUnitRepository.findAll();
        assertThat(rawDataFromMicUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRawDataFromMicUnit() throws Exception {
        // Initialize the database
        rawDataFromMicUnitRepository.saveAndFlush(rawDataFromMicUnit);

        int databaseSizeBeforeDelete = rawDataFromMicUnitRepository.findAll().size();

        // Delete the rawDataFromMicUnit
        restRawDataFromMicUnitMockMvc.perform(delete("/api/raw-data-from-mic-units/{id}", rawDataFromMicUnit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RawDataFromMicUnit> rawDataFromMicUnitList = rawDataFromMicUnitRepository.findAll();
        assertThat(rawDataFromMicUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
