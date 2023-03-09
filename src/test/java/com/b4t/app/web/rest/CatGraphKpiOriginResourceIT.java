package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.CatGraphKpiOrigin;
import com.b4t.app.repository.CatGraphKpiOriginRepository;
import com.b4t.app.service.CatGraphKpiOriginService;
import com.b4t.app.service.dto.CatGraphKpiOriginDTO;
import com.b4t.app.service.mapper.CatGraphKpiOriginMapper;

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
 * Integration tests for the {@link CatGraphKpiOriginResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@WithMockUser
public class CatGraphKpiOriginResourceIT {

    @Autowired
    private CatGraphKpiOriginRepository catGraphKpiOriginRepository;

    @Autowired
    private CatGraphKpiOriginMapper catGraphKpiOriginMapper;

    @Autowired
    private CatGraphKpiOriginService catGraphKpiOriginService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatGraphKpiOriginMockMvc;

    private CatGraphKpiOrigin catGraphKpiOrigin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatGraphKpiOrigin createEntity(EntityManager em) {
        CatGraphKpiOrigin catGraphKpiOrigin = new CatGraphKpiOrigin();
        return catGraphKpiOrigin;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatGraphKpiOrigin createUpdatedEntity(EntityManager em) {
        CatGraphKpiOrigin catGraphKpiOrigin = new CatGraphKpiOrigin();
        return catGraphKpiOrigin;
    }

    @BeforeEach
    public void initTest() {
        catGraphKpiOrigin = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatGraphKpiOrigin() throws Exception {
        int databaseSizeBeforeCreate = catGraphKpiOriginRepository.findAll().size();
        // Create the CatGraphKpiOrigin
        CatGraphKpiOriginDTO catGraphKpiOriginDTO = catGraphKpiOriginMapper.toDto(catGraphKpiOrigin);
        restCatGraphKpiOriginMockMvc.perform(post("/api/cat-graph-kpi-origins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiOriginDTO)))
            .andExpect(status().isCreated());

        // Validate the CatGraphKpiOrigin in the database
        List<CatGraphKpiOrigin> catGraphKpiOriginList = catGraphKpiOriginRepository.findAll();
        assertThat(catGraphKpiOriginList).hasSize(databaseSizeBeforeCreate + 1);
        CatGraphKpiOrigin testCatGraphKpiOrigin = catGraphKpiOriginList.get(catGraphKpiOriginList.size() - 1);
    }

    @Test
    @Transactional
    public void createCatGraphKpiOriginWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catGraphKpiOriginRepository.findAll().size();

        // Create the CatGraphKpiOrigin with an existing ID
        catGraphKpiOrigin.setId(1L);
        CatGraphKpiOriginDTO catGraphKpiOriginDTO = catGraphKpiOriginMapper.toDto(catGraphKpiOrigin);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatGraphKpiOriginMockMvc.perform(post("/api/cat-graph-kpi-origins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiOriginDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatGraphKpiOrigin in the database
        List<CatGraphKpiOrigin> catGraphKpiOriginList = catGraphKpiOriginRepository.findAll();
        assertThat(catGraphKpiOriginList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCatGraphKpiOrigins() throws Exception {
        // Initialize the database
        catGraphKpiOriginRepository.saveAndFlush(catGraphKpiOrigin);

        // Get all the catGraphKpiOriginList
        restCatGraphKpiOriginMockMvc.perform(get("/api/cat-graph-kpi-origins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catGraphKpiOrigin.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getCatGraphKpiOrigin() throws Exception {
        // Initialize the database
        catGraphKpiOriginRepository.saveAndFlush(catGraphKpiOrigin);

        // Get the catGraphKpiOrigin
        restCatGraphKpiOriginMockMvc.perform(get("/api/cat-graph-kpi-origins/{id}", catGraphKpiOrigin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catGraphKpiOrigin.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCatGraphKpiOrigin() throws Exception {
        // Get the catGraphKpiOrigin
        restCatGraphKpiOriginMockMvc.perform(get("/api/cat-graph-kpi-origins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatGraphKpiOrigin() throws Exception {
        // Initialize the database
        catGraphKpiOriginRepository.saveAndFlush(catGraphKpiOrigin);

        int databaseSizeBeforeUpdate = catGraphKpiOriginRepository.findAll().size();

        // Update the catGraphKpiOrigin
        CatGraphKpiOrigin updatedCatGraphKpiOrigin = catGraphKpiOriginRepository.findById(catGraphKpiOrigin.getId()).get();
        // Disconnect from session so that the updates on updatedCatGraphKpiOrigin are not directly saved in db
        em.detach(updatedCatGraphKpiOrigin);
        CatGraphKpiOriginDTO catGraphKpiOriginDTO = catGraphKpiOriginMapper.toDto(updatedCatGraphKpiOrigin);

        restCatGraphKpiOriginMockMvc.perform(put("/api/cat-graph-kpi-origins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiOriginDTO)))
            .andExpect(status().isOk());

        // Validate the CatGraphKpiOrigin in the database
        List<CatGraphKpiOrigin> catGraphKpiOriginList = catGraphKpiOriginRepository.findAll();
        assertThat(catGraphKpiOriginList).hasSize(databaseSizeBeforeUpdate);
        CatGraphKpiOrigin testCatGraphKpiOrigin = catGraphKpiOriginList.get(catGraphKpiOriginList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingCatGraphKpiOrigin() throws Exception {
        int databaseSizeBeforeUpdate = catGraphKpiOriginRepository.findAll().size();

        // Create the CatGraphKpiOrigin
        CatGraphKpiOriginDTO catGraphKpiOriginDTO = catGraphKpiOriginMapper.toDto(catGraphKpiOrigin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatGraphKpiOriginMockMvc.perform(put("/api/cat-graph-kpi-origins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catGraphKpiOriginDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatGraphKpiOrigin in the database
        List<CatGraphKpiOrigin> catGraphKpiOriginList = catGraphKpiOriginRepository.findAll();
        assertThat(catGraphKpiOriginList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCatGraphKpiOrigin() throws Exception {
        // Initialize the database
        catGraphKpiOriginRepository.saveAndFlush(catGraphKpiOrigin);

        int databaseSizeBeforeDelete = catGraphKpiOriginRepository.findAll().size();

        // Delete the catGraphKpiOrigin
        restCatGraphKpiOriginMockMvc.perform(delete("/api/cat-graph-kpi-origins/{id}", catGraphKpiOrigin.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatGraphKpiOrigin> catGraphKpiOriginList = catGraphKpiOriginRepository.findAll();
        assertThat(catGraphKpiOriginList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
