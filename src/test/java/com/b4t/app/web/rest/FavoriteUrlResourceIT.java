package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.FavoriteUrl;
import com.b4t.app.repository.FavoriteUrlRepository;
import com.b4t.app.service.FavoriteUrlService;
import com.b4t.app.service.dto.FavoriteUrlDTO;
import com.b4t.app.service.mapper.FavoriteUrlMapper;

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
 * Integration tests for the {@link FavoriteUrlResource} REST controller.
 */
@SpringBootTest(classes = Application.class)

@AutoConfigureMockMvc
@WithMockUser
public class FavoriteUrlResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL_LINK = "AAAAAAAAAA";
    private static final String UPDATED_URL_LINK = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private FavoriteUrlRepository favoriteUrlRepository;

    @Autowired
    private FavoriteUrlMapper favoriteUrlMapper;

    @Autowired
    private FavoriteUrlService favoriteUrlService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavoriteUrlMockMvc;

    private FavoriteUrl favoriteUrl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavoriteUrl createEntity(EntityManager em) {
        FavoriteUrl favoriteUrl = new FavoriteUrl()
            .name(DEFAULT_NAME)
            .urlLink(DEFAULT_URL_LINK)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return favoriteUrl;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavoriteUrl createUpdatedEntity(EntityManager em) {
        FavoriteUrl favoriteUrl = new FavoriteUrl()
            .name(UPDATED_NAME)
            .urlLink(UPDATED_URL_LINK)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return favoriteUrl;
    }

    @BeforeEach
    public void initTest() {
        favoriteUrl = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavoriteUrl() throws Exception {
        int databaseSizeBeforeCreate = favoriteUrlRepository.findAll().size();

        // Create the FavoriteUrl
        FavoriteUrlDTO favoriteUrlDTO = favoriteUrlMapper.toDto(favoriteUrl);
        restFavoriteUrlMockMvc.perform(post("/api/favorite-urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(favoriteUrlDTO)))
            .andExpect(status().isCreated());

        // Validate the FavoriteUrl in the database
        List<FavoriteUrl> favoriteUrlList = favoriteUrlRepository.findAll();
        assertThat(favoriteUrlList).hasSize(databaseSizeBeforeCreate + 1);
        FavoriteUrl testFavoriteUrl = favoriteUrlList.get(favoriteUrlList.size() - 1);
        assertThat(testFavoriteUrl.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFavoriteUrl.getUrlLink()).isEqualTo(DEFAULT_URL_LINK);
        assertThat(testFavoriteUrl.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testFavoriteUrl.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createFavoriteUrlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favoriteUrlRepository.findAll().size();

        // Create the FavoriteUrl with an existing ID
        favoriteUrl.setId(1L);
        FavoriteUrlDTO favoriteUrlDTO = favoriteUrlMapper.toDto(favoriteUrl);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriteUrlMockMvc.perform(post("/api/favorite-urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(favoriteUrlDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavoriteUrl in the database
        List<FavoriteUrl> favoriteUrlList = favoriteUrlRepository.findAll();
        assertThat(favoriteUrlList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFavoriteUrls() throws Exception {
        // Initialize the database
        favoriteUrlRepository.saveAndFlush(favoriteUrl);

        // Get all the favoriteUrlList
        restFavoriteUrlMockMvc.perform(get("/api/favorite-urls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favoriteUrl.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].urlLink").value(hasItem(DEFAULT_URL_LINK)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }
    
    @Test
    @Transactional
    public void getFavoriteUrl() throws Exception {
        // Initialize the database
        favoriteUrlRepository.saveAndFlush(favoriteUrl);

        // Get the favoriteUrl
        restFavoriteUrlMockMvc.perform(get("/api/favorite-urls/{id}", favoriteUrl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favoriteUrl.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.urlLink").value(DEFAULT_URL_LINK))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingFavoriteUrl() throws Exception {
        // Get the favoriteUrl
        restFavoriteUrlMockMvc.perform(get("/api/favorite-urls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavoriteUrl() throws Exception {
        // Initialize the database
        favoriteUrlRepository.saveAndFlush(favoriteUrl);

        int databaseSizeBeforeUpdate = favoriteUrlRepository.findAll().size();

        // Update the favoriteUrl
        FavoriteUrl updatedFavoriteUrl = favoriteUrlRepository.findById(favoriteUrl.getId()).get();
        // Disconnect from session so that the updates on updatedFavoriteUrl are not directly saved in db
        em.detach(updatedFavoriteUrl);
        updatedFavoriteUrl
            .name(UPDATED_NAME)
            .urlLink(UPDATED_URL_LINK)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        FavoriteUrlDTO favoriteUrlDTO = favoriteUrlMapper.toDto(updatedFavoriteUrl);

        restFavoriteUrlMockMvc.perform(put("/api/favorite-urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(favoriteUrlDTO)))
            .andExpect(status().isOk());

        // Validate the FavoriteUrl in the database
        List<FavoriteUrl> favoriteUrlList = favoriteUrlRepository.findAll();
        assertThat(favoriteUrlList).hasSize(databaseSizeBeforeUpdate);
        FavoriteUrl testFavoriteUrl = favoriteUrlList.get(favoriteUrlList.size() - 1);
        assertThat(testFavoriteUrl.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFavoriteUrl.getUrlLink()).isEqualTo(UPDATED_URL_LINK);
        assertThat(testFavoriteUrl.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testFavoriteUrl.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingFavoriteUrl() throws Exception {
        int databaseSizeBeforeUpdate = favoriteUrlRepository.findAll().size();

        // Create the FavoriteUrl
        FavoriteUrlDTO favoriteUrlDTO = favoriteUrlMapper.toDto(favoriteUrl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteUrlMockMvc.perform(put("/api/favorite-urls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(favoriteUrlDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavoriteUrl in the database
        List<FavoriteUrl> favoriteUrlList = favoriteUrlRepository.findAll();
        assertThat(favoriteUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFavoriteUrl() throws Exception {
        // Initialize the database
        favoriteUrlRepository.saveAndFlush(favoriteUrl);

        int databaseSizeBeforeDelete = favoriteUrlRepository.findAll().size();

        // Delete the favoriteUrl
        restFavoriteUrlMockMvc.perform(delete("/api/favorite-urls/{id}", favoriteUrl.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FavoriteUrl> favoriteUrlList = favoriteUrlRepository.findAll();
        assertThat(favoriteUrlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
