package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ClassifyColorGmap;
import com.b4t.app.repository.ClassifyColorGmapRepository;
import com.b4t.app.service.ClassifyColorGmapService;
import com.b4t.app.service.dto.ClassifyColorGmapDTO;
import com.b4t.app.service.mapper.ClassifyColorGmapMapper;

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
 * Integration tests for the {@link ClassifyColorGmapResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClassifyColorGmapResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_LEVEL = 1D;
    private static final Double UPDATED_TOTAL_LEVEL = 2D;

    private static final String DEFAULT_COLOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COLOR_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private ClassifyColorGmapRepository classifyColorGmapRepository;

    @Autowired
    private ClassifyColorGmapMapper classifyColorGmapMapper;

    @Autowired
    private ClassifyColorGmapService classifyColorGmapService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassifyColorGmapMockMvc;

    private ClassifyColorGmap classifyColorGmap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassifyColorGmap createEntity(EntityManager em) {
        ClassifyColorGmap classifyColorGmap = new ClassifyColorGmap()
            .name(DEFAULT_NAME)
            .totalLevel(DEFAULT_TOTAL_LEVEL)
            .colorCode(DEFAULT_COLOR_CODE)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return classifyColorGmap;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassifyColorGmap createUpdatedEntity(EntityManager em) {
        ClassifyColorGmap classifyColorGmap = new ClassifyColorGmap()
            .name(UPDATED_NAME)
            .totalLevel(UPDATED_TOTAL_LEVEL)
            .colorCode(UPDATED_COLOR_CODE)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return classifyColorGmap;
    }

    @BeforeEach
    public void initTest() {
        classifyColorGmap = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassifyColorGmap() throws Exception {
        int databaseSizeBeforeCreate = classifyColorGmapRepository.findAll().size();

        // Create the ClassifyColorGmap
        ClassifyColorGmapDTO classifyColorGmapDTO = classifyColorGmapMapper.toDto(classifyColorGmap);
        restClassifyColorGmapMockMvc.perform(post("/api/classify-color-gmaps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassifyColorGmap in the database
        List<ClassifyColorGmap> classifyColorGmapList = classifyColorGmapRepository.findAll();
        assertThat(classifyColorGmapList).hasSize(databaseSizeBeforeCreate + 1);
        ClassifyColorGmap testClassifyColorGmap = classifyColorGmapList.get(classifyColorGmapList.size() - 1);
        assertThat(testClassifyColorGmap.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassifyColorGmap.getTotalLevel()).isEqualTo(DEFAULT_TOTAL_LEVEL);
        assertThat(testClassifyColorGmap.getColorCode()).isEqualTo(DEFAULT_COLOR_CODE);
        assertThat(testClassifyColorGmap.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testClassifyColorGmap.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testClassifyColorGmap.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createClassifyColorGmapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classifyColorGmapRepository.findAll().size();

        // Create the ClassifyColorGmap with an existing ID
        classifyColorGmap.setId(1L);
        ClassifyColorGmapDTO classifyColorGmapDTO = classifyColorGmapMapper.toDto(classifyColorGmap);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassifyColorGmapMockMvc.perform(post("/api/classify-color-gmaps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassifyColorGmap in the database
        List<ClassifyColorGmap> classifyColorGmapList = classifyColorGmapRepository.findAll();
        assertThat(classifyColorGmapList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClassifyColorGmaps() throws Exception {
        // Initialize the database
        classifyColorGmapRepository.saveAndFlush(classifyColorGmap);

        // Get all the classifyColorGmapList
        restClassifyColorGmapMockMvc.perform(get("/api/classify-color-gmaps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classifyColorGmap.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }
    
    @Test
    @Transactional
    public void getClassifyColorGmap() throws Exception {
        // Initialize the database
        classifyColorGmapRepository.saveAndFlush(classifyColorGmap);

        // Get the classifyColorGmap
        restClassifyColorGmapMockMvc.perform(get("/api/classify-color-gmaps/{id}", classifyColorGmap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classifyColorGmap.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.totalLevel").value(DEFAULT_TOTAL_LEVEL.doubleValue()))
            .andExpect(jsonPath("$.colorCode").value(DEFAULT_COLOR_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingClassifyColorGmap() throws Exception {
        // Get the classifyColorGmap
        restClassifyColorGmapMockMvc.perform(get("/api/classify-color-gmaps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassifyColorGmap() throws Exception {
        // Initialize the database
        classifyColorGmapRepository.saveAndFlush(classifyColorGmap);

        int databaseSizeBeforeUpdate = classifyColorGmapRepository.findAll().size();

        // Update the classifyColorGmap
        ClassifyColorGmap updatedClassifyColorGmap = classifyColorGmapRepository.findById(classifyColorGmap.getId()).get();
        // Disconnect from session so that the updates on updatedClassifyColorGmap are not directly saved in db
        em.detach(updatedClassifyColorGmap);
        updatedClassifyColorGmap
            .name(UPDATED_NAME)
            .totalLevel(UPDATED_TOTAL_LEVEL)
            .colorCode(UPDATED_COLOR_CODE)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        ClassifyColorGmapDTO classifyColorGmapDTO = classifyColorGmapMapper.toDto(updatedClassifyColorGmap);

        restClassifyColorGmapMockMvc.perform(put("/api/classify-color-gmaps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapDTO)))
            .andExpect(status().isOk());

        // Validate the ClassifyColorGmap in the database
        List<ClassifyColorGmap> classifyColorGmapList = classifyColorGmapRepository.findAll();
        assertThat(classifyColorGmapList).hasSize(databaseSizeBeforeUpdate);
        ClassifyColorGmap testClassifyColorGmap = classifyColorGmapList.get(classifyColorGmapList.size() - 1);
        assertThat(testClassifyColorGmap.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassifyColorGmap.getTotalLevel()).isEqualTo(UPDATED_TOTAL_LEVEL);
        assertThat(testClassifyColorGmap.getColorCode()).isEqualTo(UPDATED_COLOR_CODE);
        assertThat(testClassifyColorGmap.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testClassifyColorGmap.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testClassifyColorGmap.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingClassifyColorGmap() throws Exception {
        int databaseSizeBeforeUpdate = classifyColorGmapRepository.findAll().size();

        // Create the ClassifyColorGmap
        ClassifyColorGmapDTO classifyColorGmapDTO = classifyColorGmapMapper.toDto(classifyColorGmap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassifyColorGmapMockMvc.perform(put("/api/classify-color-gmaps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassifyColorGmap in the database
        List<ClassifyColorGmap> classifyColorGmapList = classifyColorGmapRepository.findAll();
        assertThat(classifyColorGmapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassifyColorGmap() throws Exception {
        // Initialize the database
        classifyColorGmapRepository.saveAndFlush(classifyColorGmap);

        int databaseSizeBeforeDelete = classifyColorGmapRepository.findAll().size();

        // Delete the classifyColorGmap
        restClassifyColorGmapMockMvc.perform(delete("/api/classify-color-gmaps/{id}", classifyColorGmap.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassifyColorGmap> classifyColorGmapList = classifyColorGmapRepository.findAll();
        assertThat(classifyColorGmapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
