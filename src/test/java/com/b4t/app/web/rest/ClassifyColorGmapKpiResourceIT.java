package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.ClassifyColorGmapKpi;
import com.b4t.app.repository.ClassifyColorGmapKpiRepository;
import com.b4t.app.service.ClassifyColorGmapKpiService;
import com.b4t.app.service.dto.ClassifyColorGmapKpiDTO;
import com.b4t.app.service.mapper.ClassifyColorGmapKpiMapper;

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
 * Integration tests for the {@link ClassifyColorGmapKpiResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClassifyColorGmapKpiResourceIT {

    private static final String DEFAULT_CLASS_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

    @Autowired
    private ClassifyColorGmapKpiRepository classifyColorGmapKpiRepository;

    @Autowired
    private ClassifyColorGmapKpiMapper classifyColorGmapKpiMapper;

    @Autowired
    private ClassifyColorGmapKpiService classifyColorGmapKpiService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassifyColorGmapKpiMockMvc;

    private ClassifyColorGmapKpi classifyColorGmapKpi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassifyColorGmapKpi createEntity(EntityManager em) {
        ClassifyColorGmapKpi classifyColorGmapKpi = new ClassifyColorGmapKpi()
            .classId(DEFAULT_CLASS_ID)
            .kpiId(DEFAULT_KPI_ID);
        return classifyColorGmapKpi;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassifyColorGmapKpi createUpdatedEntity(EntityManager em) {
        ClassifyColorGmapKpi classifyColorGmapKpi = new ClassifyColorGmapKpi()
            .classId(UPDATED_CLASS_ID)
            .kpiId(UPDATED_KPI_ID);
        return classifyColorGmapKpi;
    }

    @BeforeEach
    public void initTest() {
        classifyColorGmapKpi = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassifyColorGmapKpi() throws Exception {
        int databaseSizeBeforeCreate = classifyColorGmapKpiRepository.findAll().size();

        // Create the ClassifyColorGmapKpi
        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO = classifyColorGmapKpiMapper.toDto(classifyColorGmapKpi);
        restClassifyColorGmapKpiMockMvc.perform(post("/api/classify-color-gmap-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapKpiDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassifyColorGmapKpi in the database
        List<ClassifyColorGmapKpi> classifyColorGmapKpiList = classifyColorGmapKpiRepository.findAll();
        assertThat(classifyColorGmapKpiList).hasSize(databaseSizeBeforeCreate + 1);
        ClassifyColorGmapKpi testClassifyColorGmapKpi = classifyColorGmapKpiList.get(classifyColorGmapKpiList.size() - 1);
        assertThat(testClassifyColorGmapKpi.getClassId()).isEqualTo(DEFAULT_CLASS_ID);
        assertThat(testClassifyColorGmapKpi.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
    }

    @Test
    @Transactional
    public void createClassifyColorGmapKpiWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classifyColorGmapKpiRepository.findAll().size();

        // Create the ClassifyColorGmapKpi with an existing ID
        classifyColorGmapKpi.setId(1L);
        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO = classifyColorGmapKpiMapper.toDto(classifyColorGmapKpi);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassifyColorGmapKpiMockMvc.perform(post("/api/classify-color-gmap-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassifyColorGmapKpi in the database
        List<ClassifyColorGmapKpi> classifyColorGmapKpiList = classifyColorGmapKpiRepository.findAll();
        assertThat(classifyColorGmapKpiList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClassifyColorGmapKpis() throws Exception {
        // Initialize the database
        classifyColorGmapKpiRepository.saveAndFlush(classifyColorGmapKpi);

        // Get all the classifyColorGmapKpiList
        restClassifyColorGmapKpiMockMvc.perform(get("/api/classify-color-gmap-kpis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classifyColorGmapKpi.getId().intValue())))
            .andExpect(jsonPath("$.[*].classId").value(hasItem(DEFAULT_CLASS_ID)))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getClassifyColorGmapKpi() throws Exception {
        // Initialize the database
        classifyColorGmapKpiRepository.saveAndFlush(classifyColorGmapKpi);

        // Get the classifyColorGmapKpi
        restClassifyColorGmapKpiMockMvc.perform(get("/api/classify-color-gmap-kpis/{id}", classifyColorGmapKpi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classifyColorGmapKpi.getId().intValue()))
            .andExpect(jsonPath("$.classId").value(DEFAULT_CLASS_ID))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClassifyColorGmapKpi() throws Exception {
        // Get the classifyColorGmapKpi
        restClassifyColorGmapKpiMockMvc.perform(get("/api/classify-color-gmap-kpis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassifyColorGmapKpi() throws Exception {
        // Initialize the database
        classifyColorGmapKpiRepository.saveAndFlush(classifyColorGmapKpi);

        int databaseSizeBeforeUpdate = classifyColorGmapKpiRepository.findAll().size();

        // Update the classifyColorGmapKpi
        ClassifyColorGmapKpi updatedClassifyColorGmapKpi = classifyColorGmapKpiRepository.findById(classifyColorGmapKpi.getId()).get();
        // Disconnect from session so that the updates on updatedClassifyColorGmapKpi are not directly saved in db
        em.detach(updatedClassifyColorGmapKpi);
        updatedClassifyColorGmapKpi
            .classId(UPDATED_CLASS_ID)
            .kpiId(UPDATED_KPI_ID);
        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO = classifyColorGmapKpiMapper.toDto(updatedClassifyColorGmapKpi);

        restClassifyColorGmapKpiMockMvc.perform(put("/api/classify-color-gmap-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapKpiDTO)))
            .andExpect(status().isOk());

        // Validate the ClassifyColorGmapKpi in the database
        List<ClassifyColorGmapKpi> classifyColorGmapKpiList = classifyColorGmapKpiRepository.findAll();
        assertThat(classifyColorGmapKpiList).hasSize(databaseSizeBeforeUpdate);
        ClassifyColorGmapKpi testClassifyColorGmapKpi = classifyColorGmapKpiList.get(classifyColorGmapKpiList.size() - 1);
        assertThat(testClassifyColorGmapKpi.getClassId()).isEqualTo(UPDATED_CLASS_ID);
        assertThat(testClassifyColorGmapKpi.getKpiId()).isEqualTo(UPDATED_KPI_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingClassifyColorGmapKpi() throws Exception {
        int databaseSizeBeforeUpdate = classifyColorGmapKpiRepository.findAll().size();

        // Create the ClassifyColorGmapKpi
        ClassifyColorGmapKpiDTO classifyColorGmapKpiDTO = classifyColorGmapKpiMapper.toDto(classifyColorGmapKpi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassifyColorGmapKpiMockMvc.perform(put("/api/classify-color-gmap-kpis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classifyColorGmapKpiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassifyColorGmapKpi in the database
        List<ClassifyColorGmapKpi> classifyColorGmapKpiList = classifyColorGmapKpiRepository.findAll();
        assertThat(classifyColorGmapKpiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassifyColorGmapKpi() throws Exception {
        // Initialize the database
        classifyColorGmapKpiRepository.saveAndFlush(classifyColorGmapKpi);

        int databaseSizeBeforeDelete = classifyColorGmapKpiRepository.findAll().size();

        // Delete the classifyColorGmapKpi
        restClassifyColorGmapKpiMockMvc.perform(delete("/api/classify-color-gmap-kpis/{id}", classifyColorGmapKpi.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassifyColorGmapKpi> classifyColorGmapKpiList = classifyColorGmapKpiRepository.findAll();
        assertThat(classifyColorGmapKpiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
