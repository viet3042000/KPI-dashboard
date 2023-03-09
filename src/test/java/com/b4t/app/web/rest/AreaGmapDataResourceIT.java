package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.AreaGmapData;
import com.b4t.app.repository.AreaGmapDataRepository;
import com.b4t.app.service.AreaGmapDataService;
import com.b4t.app.service.dto.AreaGmapDataDTO;
import com.b4t.app.service.mapper.AreaGmapDataMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AreaGmapDataResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class AreaGmapDataResourceIT {

    private static final String DEFAULT_PARENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_LOCATION_LEVEL = 1L;
    private static final Long UPDATED_LOCATION_LEVEL = 2L;

    private static final String DEFAULT_CENTER_LOC = "AAAAAAAAAA";
    private static final String UPDATED_CENTER_LOC = "BBBBBBBBBB";

    private static final String DEFAULT_SOUTH_LOC = "AAAAAAAAAA";
    private static final String UPDATED_SOUTH_LOC = "BBBBBBBBBB";

    private static final String DEFAULT_NORTH_LOC = "AAAAAAAAAA";
    private static final String UPDATED_NORTH_LOC = "BBBBBBBBBB";

    private static final String DEFAULT_NORTH_POLE = "AAAAAAAAAA";
    private static final String UPDATED_NORTH_POLE = "BBBBBBBBBB";

    private static final String DEFAULT_GEOMETRY = "AAAAAAAAAA";
    private static final String UPDATED_GEOMETRY = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    @Autowired
    private AreaGmapDataRepository areaGmapDataRepository;

    @Autowired
    private AreaGmapDataMapper areaGmapDataMapper;

    @Autowired
    private AreaGmapDataService areaGmapDataService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAreaGmapDataMockMvc;

    private AreaGmapData areaGmapData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaGmapData createEntity(EntityManager em) {
        AreaGmapData areaGmapData = new AreaGmapData()
            .parentCode(DEFAULT_PARENT_CODE)
            .parentName(DEFAULT_PARENT_NAME)
            .objectCode(DEFAULT_OBJECT_CODE)
            .objectName(DEFAULT_OBJECT_NAME)
            .locationLevel(DEFAULT_LOCATION_LEVEL)
            .centerLoc(DEFAULT_CENTER_LOC)
            .southLoc(DEFAULT_SOUTH_LOC)
            .northLoc(DEFAULT_NORTH_LOC)
            .northPole(DEFAULT_NORTH_POLE)
            .geometry(DEFAULT_GEOMETRY)
            .status(DEFAULT_STATUS);
        return areaGmapData;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaGmapData createUpdatedEntity(EntityManager em) {
        AreaGmapData areaGmapData = new AreaGmapData()
            .parentCode(UPDATED_PARENT_CODE)
            .parentName(UPDATED_PARENT_NAME)
            .objectCode(UPDATED_OBJECT_CODE)
            .objectName(UPDATED_OBJECT_NAME)
            .locationLevel(UPDATED_LOCATION_LEVEL)
            .centerLoc(UPDATED_CENTER_LOC)
            .southLoc(UPDATED_SOUTH_LOC)
            .northLoc(UPDATED_NORTH_LOC)
            .northPole(UPDATED_NORTH_POLE)
            .geometry(UPDATED_GEOMETRY)
            .status(UPDATED_STATUS);
        return areaGmapData;
    }

    @BeforeEach
    public void initTest() {
        areaGmapData = createEntity(em);
    }

    @Test
    @Transactional
    public void createAreaGmapData() throws Exception {
        int databaseSizeBeforeCreate = areaGmapDataRepository.findAll().size();

        // Create the AreaGmapData
        AreaGmapDataDTO areaGmapDataDTO = areaGmapDataMapper.toDto(areaGmapData);
        restAreaGmapDataMockMvc.perform(post("/api/area-gmap-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(areaGmapDataDTO)))
            .andExpect(status().isCreated());

        // Validate the AreaGmapData in the database
        List<AreaGmapData> areaGmapDataList = areaGmapDataRepository.findAll();
        assertThat(areaGmapDataList).hasSize(databaseSizeBeforeCreate + 1);
        AreaGmapData testAreaGmapData = areaGmapDataList.get(areaGmapDataList.size() - 1);
        assertThat(testAreaGmapData.getParentCode()).isEqualTo(DEFAULT_PARENT_CODE);
        assertThat(testAreaGmapData.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testAreaGmapData.getObjectCode()).isEqualTo(DEFAULT_OBJECT_CODE);
        assertThat(testAreaGmapData.getObjectName()).isEqualTo(DEFAULT_OBJECT_NAME);
        assertThat(testAreaGmapData.getLocationLevel()).isEqualTo(DEFAULT_LOCATION_LEVEL);
        assertThat(testAreaGmapData.getCenterLoc()).isEqualTo(DEFAULT_CENTER_LOC);
        assertThat(testAreaGmapData.getSouthLoc()).isEqualTo(DEFAULT_SOUTH_LOC);
        assertThat(testAreaGmapData.getNorthLoc()).isEqualTo(DEFAULT_NORTH_LOC);
        assertThat(testAreaGmapData.getNorthPole()).isEqualTo(DEFAULT_NORTH_POLE);
        assertThat(testAreaGmapData.getGeometry()).isEqualTo(DEFAULT_GEOMETRY);
        assertThat(testAreaGmapData.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createAreaGmapDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = areaGmapDataRepository.findAll().size();

        // Create the AreaGmapData with an existing ID
        areaGmapData.setId(1L);
        AreaGmapDataDTO areaGmapDataDTO = areaGmapDataMapper.toDto(areaGmapData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAreaGmapDataMockMvc.perform(post("/api/area-gmap-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(areaGmapDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AreaGmapData in the database
        List<AreaGmapData> areaGmapDataList = areaGmapDataRepository.findAll();
        assertThat(areaGmapDataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAreaGmapData() throws Exception {
        // Initialize the database
        areaGmapDataRepository.saveAndFlush(areaGmapData);

        // Get all the areaGmapDataList
        restAreaGmapDataMockMvc.perform(get("/api/area-gmap-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(areaGmapData.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentCode").value(hasItem(DEFAULT_PARENT_CODE)))
            .andExpect(jsonPath("$.[*].parentName").value(hasItem(DEFAULT_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].objectCode").value(hasItem(DEFAULT_OBJECT_CODE)))
            .andExpect(jsonPath("$.[*].objectName").value(hasItem(DEFAULT_OBJECT_NAME)))
            .andExpect(jsonPath("$.[*].locationLevel").value(hasItem(DEFAULT_LOCATION_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].centerLoc").value(hasItem(DEFAULT_CENTER_LOC)))
            .andExpect(jsonPath("$.[*].southLoc").value(hasItem(DEFAULT_SOUTH_LOC)))
            .andExpect(jsonPath("$.[*].northLoc").value(hasItem(DEFAULT_NORTH_LOC)))
            .andExpect(jsonPath("$.[*].northPole").value(hasItem(DEFAULT_NORTH_POLE)))
            .andExpect(jsonPath("$.[*].geometry").value(hasItem(DEFAULT_GEOMETRY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())));
    }
    
    @Test
    @Transactional
    public void getAreaGmapData() throws Exception {
        // Initialize the database
        areaGmapDataRepository.saveAndFlush(areaGmapData);

        // Get the areaGmapData
        restAreaGmapDataMockMvc.perform(get("/api/area-gmap-data/{id}", areaGmapData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(areaGmapData.getId().intValue()))
            .andExpect(jsonPath("$.parentCode").value(DEFAULT_PARENT_CODE))
            .andExpect(jsonPath("$.parentName").value(DEFAULT_PARENT_NAME))
            .andExpect(jsonPath("$.objectCode").value(DEFAULT_OBJECT_CODE))
            .andExpect(jsonPath("$.objectName").value(DEFAULT_OBJECT_NAME))
            .andExpect(jsonPath("$.locationLevel").value(DEFAULT_LOCATION_LEVEL.intValue()))
            .andExpect(jsonPath("$.centerLoc").value(DEFAULT_CENTER_LOC))
            .andExpect(jsonPath("$.southLoc").value(DEFAULT_SOUTH_LOC))
            .andExpect(jsonPath("$.northLoc").value(DEFAULT_NORTH_LOC))
            .andExpect(jsonPath("$.northPole").value(DEFAULT_NORTH_POLE))
            .andExpect(jsonPath("$.geometry").value(DEFAULT_GEOMETRY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAreaGmapData() throws Exception {
        // Get the areaGmapData
        restAreaGmapDataMockMvc.perform(get("/api/area-gmap-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAreaGmapData() throws Exception {
        // Initialize the database
        areaGmapDataRepository.saveAndFlush(areaGmapData);

        int databaseSizeBeforeUpdate = areaGmapDataRepository.findAll().size();

        // Update the areaGmapData
        AreaGmapData updatedAreaGmapData = areaGmapDataRepository.findById(areaGmapData.getId()).get();
        // Disconnect from session so that the updates on updatedAreaGmapData are not directly saved in db
        em.detach(updatedAreaGmapData);
        updatedAreaGmapData
            .parentCode(UPDATED_PARENT_CODE)
            .parentName(UPDATED_PARENT_NAME)
            .objectCode(UPDATED_OBJECT_CODE)
            .objectName(UPDATED_OBJECT_NAME)
            .locationLevel(UPDATED_LOCATION_LEVEL)
            .centerLoc(UPDATED_CENTER_LOC)
            .southLoc(UPDATED_SOUTH_LOC)
            .northLoc(UPDATED_NORTH_LOC)
            .northPole(UPDATED_NORTH_POLE)
            .geometry(UPDATED_GEOMETRY)
            .status(UPDATED_STATUS);
        AreaGmapDataDTO areaGmapDataDTO = areaGmapDataMapper.toDto(updatedAreaGmapData);

        restAreaGmapDataMockMvc.perform(put("/api/area-gmap-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(areaGmapDataDTO)))
            .andExpect(status().isOk());

        // Validate the AreaGmapData in the database
        List<AreaGmapData> areaGmapDataList = areaGmapDataRepository.findAll();
        assertThat(areaGmapDataList).hasSize(databaseSizeBeforeUpdate);
        AreaGmapData testAreaGmapData = areaGmapDataList.get(areaGmapDataList.size() - 1);
        assertThat(testAreaGmapData.getParentCode()).isEqualTo(UPDATED_PARENT_CODE);
        assertThat(testAreaGmapData.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testAreaGmapData.getObjectCode()).isEqualTo(UPDATED_OBJECT_CODE);
        assertThat(testAreaGmapData.getObjectName()).isEqualTo(UPDATED_OBJECT_NAME);
        assertThat(testAreaGmapData.getLocationLevel()).isEqualTo(UPDATED_LOCATION_LEVEL);
        assertThat(testAreaGmapData.getCenterLoc()).isEqualTo(UPDATED_CENTER_LOC);
        assertThat(testAreaGmapData.getSouthLoc()).isEqualTo(UPDATED_SOUTH_LOC);
        assertThat(testAreaGmapData.getNorthLoc()).isEqualTo(UPDATED_NORTH_LOC);
        assertThat(testAreaGmapData.getNorthPole()).isEqualTo(UPDATED_NORTH_POLE);
        assertThat(testAreaGmapData.getGeometry()).isEqualTo(UPDATED_GEOMETRY);
        assertThat(testAreaGmapData.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingAreaGmapData() throws Exception {
        int databaseSizeBeforeUpdate = areaGmapDataRepository.findAll().size();

        // Create the AreaGmapData
        AreaGmapDataDTO areaGmapDataDTO = areaGmapDataMapper.toDto(areaGmapData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaGmapDataMockMvc.perform(put("/api/area-gmap-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(areaGmapDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AreaGmapData in the database
        List<AreaGmapData> areaGmapDataList = areaGmapDataRepository.findAll();
        assertThat(areaGmapDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAreaGmapData() throws Exception {
        // Initialize the database
        areaGmapDataRepository.saveAndFlush(areaGmapData);

        int databaseSizeBeforeDelete = areaGmapDataRepository.findAll().size();

        // Delete the areaGmapData
        restAreaGmapDataMockMvc.perform(delete("/api/area-gmap-data/{id}", areaGmapData.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AreaGmapData> areaGmapDataList = areaGmapDataRepository.findAll();
        assertThat(areaGmapDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
