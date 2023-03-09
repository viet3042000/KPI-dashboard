package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.CatKpiSynonym;
import com.b4t.app.repository.CatKpiSynonymRepository;
import com.b4t.app.service.CatKpiSynonymService;
import com.b4t.app.service.dto.CatKpiSynonymDTO;
import com.b4t.app.service.mapper.CatKpiSynonymMapper;

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
 * Integration tests for the {@link CatKpiSynonymResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WithMockUser
public class CatKpiSynonymResourceIT {

    private static final Long DEFAULT_KPI_ID = 1L;
    private static final Long UPDATED_KPI_ID = 2L;

    private static final String DEFAULT_SYNONYM = "AAAAAAAAAA";
    private static final String UPDATED_SYNONYM = "BBBBBBBBBB";

    @Autowired
    private CatKpiSynonymRepository catKpiSynonymRepository;

    @Autowired
    private CatKpiSynonymMapper catKpiSynonymMapper;

    @Autowired
    private CatKpiSynonymService catKpiSynonymService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatKpiSynonymMockMvc;

    private CatKpiSynonym catKpiSynonym;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatKpiSynonym createEntity(EntityManager em) {
        CatKpiSynonym catKpiSynonym = new CatKpiSynonym()
            .kpiId(DEFAULT_KPI_ID)
            .synonym(DEFAULT_SYNONYM);
        return catKpiSynonym;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatKpiSynonym createUpdatedEntity(EntityManager em) {
        CatKpiSynonym catKpiSynonym = new CatKpiSynonym()
            .kpiId(UPDATED_KPI_ID)
            .synonym(UPDATED_SYNONYM);
        return catKpiSynonym;
    }

    @BeforeEach
    public void initTest() {
        catKpiSynonym = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatKpiSynonym() throws Exception {
        int databaseSizeBeforeCreate = catKpiSynonymRepository.findAll().size();
        // Create the CatKpiSynonym
        CatKpiSynonymDTO catKpiSynonymDTO = catKpiSynonymMapper.toDto(catKpiSynonym);
        restCatKpiSynonymMockMvc.perform(post("/api/cat-kpi-synonyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catKpiSynonymDTO)))
            .andExpect(status().isCreated());

        // Validate the CatKpiSynonym in the database
        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeCreate + 1);
        CatKpiSynonym testCatKpiSynonym = catKpiSynonymList.get(catKpiSynonymList.size() - 1);
        assertThat(testCatKpiSynonym.getKpiId()).isEqualTo(DEFAULT_KPI_ID);
        assertThat(testCatKpiSynonym.getSynonym()).isEqualTo(DEFAULT_SYNONYM);
    }

    @Test
    @Transactional
    public void createCatKpiSynonymWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catKpiSynonymRepository.findAll().size();

        // Create the CatKpiSynonym with an existing ID
        catKpiSynonym.setId(1L);
        CatKpiSynonymDTO catKpiSynonymDTO = catKpiSynonymMapper.toDto(catKpiSynonym);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatKpiSynonymMockMvc.perform(post("/api/cat-kpi-synonyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catKpiSynonymDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatKpiSynonym in the database
        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkKpiIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = catKpiSynonymRepository.findAll().size();
        // set the field null
        catKpiSynonym.setKpiId(null);

        // Create the CatKpiSynonym, which fails.
        CatKpiSynonymDTO catKpiSynonymDTO = catKpiSynonymMapper.toDto(catKpiSynonym);


        restCatKpiSynonymMockMvc.perform(post("/api/cat-kpi-synonyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catKpiSynonymDTO)))
            .andExpect(status().isBadRequest());

        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSynonymIsRequired() throws Exception {
        int databaseSizeBeforeTest = catKpiSynonymRepository.findAll().size();
        // set the field null
        catKpiSynonym.setSynonym(null);

        // Create the CatKpiSynonym, which fails.
        CatKpiSynonymDTO catKpiSynonymDTO = catKpiSynonymMapper.toDto(catKpiSynonym);


        restCatKpiSynonymMockMvc.perform(post("/api/cat-kpi-synonyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catKpiSynonymDTO)))
            .andExpect(status().isBadRequest());

        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCatKpiSynonyms() throws Exception {
        // Initialize the database
        catKpiSynonymRepository.saveAndFlush(catKpiSynonym);

        // Get all the catKpiSynonymList
        restCatKpiSynonymMockMvc.perform(get("/api/cat-kpi-synonyms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catKpiSynonym.getId().intValue())))
            .andExpect(jsonPath("$.[*].kpiId").value(hasItem(DEFAULT_KPI_ID)))
            .andExpect(jsonPath("$.[*].synonym").value(hasItem(DEFAULT_SYNONYM)));
    }

    @Test
    @Transactional
    public void getCatKpiSynonym() throws Exception {
        // Initialize the database
        catKpiSynonymRepository.saveAndFlush(catKpiSynonym);

        // Get the catKpiSynonym
        restCatKpiSynonymMockMvc.perform(get("/api/cat-kpi-synonyms/{id}", catKpiSynonym.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catKpiSynonym.getId().intValue()))
            .andExpect(jsonPath("$.kpiId").value(DEFAULT_KPI_ID))
            .andExpect(jsonPath("$.synonym").value(DEFAULT_SYNONYM));
    }
    @Test
    @Transactional
    public void getNonExistingCatKpiSynonym() throws Exception {
        // Get the catKpiSynonym
        restCatKpiSynonymMockMvc.perform(get("/api/cat-kpi-synonyms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatKpiSynonym() throws Exception {
        // Initialize the database
        catKpiSynonymRepository.saveAndFlush(catKpiSynonym);

        int databaseSizeBeforeUpdate = catKpiSynonymRepository.findAll().size();

        // Update the catKpiSynonym
        CatKpiSynonym updatedCatKpiSynonym = catKpiSynonymRepository.findById(catKpiSynonym.getId()).get();
        // Disconnect from session so that the updates on updatedCatKpiSynonym are not directly saved in db
        em.detach(updatedCatKpiSynonym);
        updatedCatKpiSynonym
            .kpiId(UPDATED_KPI_ID)
            .synonym(UPDATED_SYNONYM);
        CatKpiSynonymDTO catKpiSynonymDTO = catKpiSynonymMapper.toDto(updatedCatKpiSynonym);

        restCatKpiSynonymMockMvc.perform(put("/api/cat-kpi-synonyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catKpiSynonymDTO)))
            .andExpect(status().isOk());

        // Validate the CatKpiSynonym in the database
        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeUpdate);
        CatKpiSynonym testCatKpiSynonym = catKpiSynonymList.get(catKpiSynonymList.size() - 1);
        assertThat(testCatKpiSynonym.getKpiId()).isEqualTo(UPDATED_KPI_ID);
        assertThat(testCatKpiSynonym.getSynonym()).isEqualTo(UPDATED_SYNONYM);
    }

    @Test
    @Transactional
    public void updateNonExistingCatKpiSynonym() throws Exception {
        int databaseSizeBeforeUpdate = catKpiSynonymRepository.findAll().size();

        // Create the CatKpiSynonym
        CatKpiSynonymDTO catKpiSynonymDTO = catKpiSynonymMapper.toDto(catKpiSynonym);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatKpiSynonymMockMvc.perform(put("/api/cat-kpi-synonyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catKpiSynonymDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatKpiSynonym in the database
        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCatKpiSynonym() throws Exception {
        // Initialize the database
        catKpiSynonymRepository.saveAndFlush(catKpiSynonym);

        int databaseSizeBeforeDelete = catKpiSynonymRepository.findAll().size();

        // Delete the catKpiSynonym
        restCatKpiSynonymMockMvc.perform(delete("/api/cat-kpi-synonyms/{id}", catKpiSynonym.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatKpiSynonym> catKpiSynonymList = catKpiSynonymRepository.findAll();
        assertThat(catKpiSynonymList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
