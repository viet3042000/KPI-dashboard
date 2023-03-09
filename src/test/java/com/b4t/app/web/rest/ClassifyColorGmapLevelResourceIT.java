package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ClassifyColorGmapLevel;
import com.b4t.app.repository.ClassifyColorGmapLevelRepository;
import com.b4t.app.service.ClassifyColorGmapLevelService;
import com.b4t.app.service.dto.ClassifyColorGmapLevelDTO;
import com.b4t.app.service.mapper.ClassifyColorGmapLevelMapper;

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
 * Integration tests for the {@link ClassifyColorGmapLevelResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClassifyColorGmapLevelResourceIT {

    private static final Long DEFAULT_CLASS_ID = 1L;
    private static final Long UPDATED_CLASS_ID = 2L;

    private static final Long DEFAULT_CLASS_LEVEL = 1L;
    private static final Long UPDATED_CLASS_LEVEL = 2L;

    private static final Double DEFAULT_FROM_VALUE = 1D;
    private static final Double UPDATED_FROM_VALUE = 2D;

    private static final Double DEFAULT_TO_VALUE = 1D;
    private static final Double UPDATED_TO_VALUE = 2D;

    @Autowired
    private ClassifyColorGmapLevelRepository classifyColorGmapLevelRepository;

    @Autowired
    private ClassifyColorGmapLevelMapper classifyColorGmapLevelMapper;

    @Autowired
    private ClassifyColorGmapLevelService classifyColorGmapLevelService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassifyColorGmapLevelMockMvc;

    private ClassifyColorGmapLevel classifyColorGmapLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassifyColorGmapLevel createEntity(EntityManager em) {
        ClassifyColorGmapLevel classifyColorGmapLevel = new ClassifyColorGmapLevel()
            .classId(DEFAULT_CLASS_ID)
            .classLevel(DEFAULT_CLASS_LEVEL)
            .fromValue(DEFAULT_FROM_VALUE)
            .toValue(DEFAULT_TO_VALUE);
        return classifyColorGmapLevel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassifyColorGmapLevel createUpdatedEntity(EntityManager em) {
        ClassifyColorGmapLevel classifyColorGmapLevel = new ClassifyColorGmapLevel()
            .classId(UPDATED_CLASS_ID)
            .classLevel(UPDATED_CLASS_LEVEL)
            .fromValue(UPDATED_FROM_VALUE)
            .toValue(UPDATED_TO_VALUE);
        return classifyColorGmapLevel;
    }

    @BeforeEach
    public void initTest() {
        classifyColorGmapLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassifyColorGmapLevel() throws Exception {
        int databaseSizeBeforeCreate = classifyColorGmapLevelRepository.findAll().size();

        // Create the ClassifyColorGmapLevel
        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO = classifyColorGmapLevelMapper.toDto(classifyColorGmapLevel);
        restClassifyColorGmapLevelMockMvc.perform(post("/api/classify-color-gmap-levels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapLevelDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassifyColorGmapLevel in the database
        List<ClassifyColorGmapLevel> classifyColorGmapLevelList = classifyColorGmapLevelRepository.findAll();
        assertThat(classifyColorGmapLevelList).hasSize(databaseSizeBeforeCreate + 1);
        ClassifyColorGmapLevel testClassifyColorGmapLevel = classifyColorGmapLevelList.get(classifyColorGmapLevelList.size() - 1);
        assertThat(testClassifyColorGmapLevel.getClassId()).isEqualTo(DEFAULT_CLASS_ID);
        assertThat(testClassifyColorGmapLevel.getClassLevel()).isEqualTo(DEFAULT_CLASS_LEVEL);
        assertThat(testClassifyColorGmapLevel.getFromValue()).isEqualTo(DEFAULT_FROM_VALUE);
        assertThat(testClassifyColorGmapLevel.getToValue()).isEqualTo(DEFAULT_TO_VALUE);
    }

    @Test
    @Transactional
    public void createClassifyColorGmapLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classifyColorGmapLevelRepository.findAll().size();

        // Create the ClassifyColorGmapLevel with an existing ID
        classifyColorGmapLevel.setId(1L);
        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO = classifyColorGmapLevelMapper.toDto(classifyColorGmapLevel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassifyColorGmapLevelMockMvc.perform(post("/api/classify-color-gmap-levels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassifyColorGmapLevel in the database
        List<ClassifyColorGmapLevel> classifyColorGmapLevelList = classifyColorGmapLevelRepository.findAll();
        assertThat(classifyColorGmapLevelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClassifyColorGmapLevels() throws Exception {
        // Initialize the database
        classifyColorGmapLevelRepository.saveAndFlush(classifyColorGmapLevel);

        // Get all the classifyColorGmapLevelList
        restClassifyColorGmapLevelMockMvc.perform(get("/api/classify-color-gmap-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classifyColorGmapLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].classId").value(hasItem(DEFAULT_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].classLevel").value(hasItem(DEFAULT_CLASS_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].fromValue").value(hasItem(DEFAULT_FROM_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].toValue").value(hasItem(DEFAULT_TO_VALUE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getClassifyColorGmapLevel() throws Exception {
        // Initialize the database
        classifyColorGmapLevelRepository.saveAndFlush(classifyColorGmapLevel);

        // Get the classifyColorGmapLevel
        restClassifyColorGmapLevelMockMvc.perform(get("/api/classify-color-gmap-levels/{id}", classifyColorGmapLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classifyColorGmapLevel.getId().intValue()))
            .andExpect(jsonPath("$.classId").value(DEFAULT_CLASS_ID.intValue()))
            .andExpect(jsonPath("$.classLevel").value(DEFAULT_CLASS_LEVEL.intValue()))
            .andExpect(jsonPath("$.fromValue").value(DEFAULT_FROM_VALUE.doubleValue()))
            .andExpect(jsonPath("$.toValue").value(DEFAULT_TO_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClassifyColorGmapLevel() throws Exception {
        // Get the classifyColorGmapLevel
        restClassifyColorGmapLevelMockMvc.perform(get("/api/classify-color-gmap-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassifyColorGmapLevel() throws Exception {
        // Initialize the database
        classifyColorGmapLevelRepository.saveAndFlush(classifyColorGmapLevel);

        int databaseSizeBeforeUpdate = classifyColorGmapLevelRepository.findAll().size();

        // Update the classifyColorGmapLevel
        ClassifyColorGmapLevel updatedClassifyColorGmapLevel = classifyColorGmapLevelRepository.findById(classifyColorGmapLevel.getId()).get();
        // Disconnect from session so that the updates on updatedClassifyColorGmapLevel are not directly saved in db
        em.detach(updatedClassifyColorGmapLevel);
        updatedClassifyColorGmapLevel
            .classId(UPDATED_CLASS_ID)
            .classLevel(UPDATED_CLASS_LEVEL)
            .fromValue(UPDATED_FROM_VALUE)
            .toValue(UPDATED_TO_VALUE);
        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO = classifyColorGmapLevelMapper.toDto(updatedClassifyColorGmapLevel);

        restClassifyColorGmapLevelMockMvc.perform(put("/api/classify-color-gmap-levels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapLevelDTO)))
            .andExpect(status().isOk());

        // Validate the ClassifyColorGmapLevel in the database
        List<ClassifyColorGmapLevel> classifyColorGmapLevelList = classifyColorGmapLevelRepository.findAll();
        assertThat(classifyColorGmapLevelList).hasSize(databaseSizeBeforeUpdate);
        ClassifyColorGmapLevel testClassifyColorGmapLevel = classifyColorGmapLevelList.get(classifyColorGmapLevelList.size() - 1);
        assertThat(testClassifyColorGmapLevel.getClassId()).isEqualTo(UPDATED_CLASS_ID);
        assertThat(testClassifyColorGmapLevel.getClassLevel()).isEqualTo(UPDATED_CLASS_LEVEL);
        assertThat(testClassifyColorGmapLevel.getFromValue()).isEqualTo(UPDATED_FROM_VALUE);
        assertThat(testClassifyColorGmapLevel.getToValue()).isEqualTo(UPDATED_TO_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingClassifyColorGmapLevel() throws Exception {
        int databaseSizeBeforeUpdate = classifyColorGmapLevelRepository.findAll().size();

        // Create the ClassifyColorGmapLevel
        ClassifyColorGmapLevelDTO classifyColorGmapLevelDTO = classifyColorGmapLevelMapper.toDto(classifyColorGmapLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassifyColorGmapLevelMockMvc.perform(put("/api/classify-color-gmap-levels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassifyColorGmapLevel in the database
        List<ClassifyColorGmapLevel> classifyColorGmapLevelList = classifyColorGmapLevelRepository.findAll();
        assertThat(classifyColorGmapLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassifyColorGmapLevel() throws Exception {
        // Initialize the database
        classifyColorGmapLevelRepository.saveAndFlush(classifyColorGmapLevel);

        int databaseSizeBeforeDelete = classifyColorGmapLevelRepository.findAll().size();

        // Delete the classifyColorGmapLevel
        restClassifyColorGmapLevelMockMvc.perform(delete("/api/classify-color-gmap-levels/{id}", classifyColorGmapLevel.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassifyColorGmapLevel> classifyColorGmapLevelList = classifyColorGmapLevelRepository.findAll();
        assertThat(classifyColorGmapLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
