package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.TokenDeviceUser;
import com.b4t.app.repository.TokenDeviceUserRepository;
import com.b4t.app.service.TokenDeviceUserService;
import com.b4t.app.service.dto.TokenDeviceUserDTO;
import com.b4t.app.service.mapper.TokenDeviceUserMapper;
import com.b4t.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.b4t.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TokenDeviceUserResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class TokenDeviceUserResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_TOKEN_DEVICE = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_DEVICE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private TokenDeviceUserRepository tokenDeviceUserRepository;

    @Autowired
    private TokenDeviceUserMapper tokenDeviceUserMapper;

    @Autowired
    private TokenDeviceUserService tokenDeviceUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTokenDeviceUserMockMvc;

    private TokenDeviceUser tokenDeviceUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TokenDeviceUserResource tokenDeviceUserResource = new TokenDeviceUserResource(tokenDeviceUserService);
        this.restTokenDeviceUserMockMvc = MockMvcBuilders.standaloneSetup(tokenDeviceUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TokenDeviceUser createEntity(EntityManager em) {
        TokenDeviceUser tokenDeviceUser = new TokenDeviceUser()
            .userId(DEFAULT_USER_ID)
            .tokenDevice(DEFAULT_TOKEN_DEVICE)
            .deviceName(DEFAULT_DEVICE_NAME)
            .status(DEFAULT_STATUS)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return tokenDeviceUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TokenDeviceUser createUpdatedEntity(EntityManager em) {
        TokenDeviceUser tokenDeviceUser = new TokenDeviceUser()
            .userId(UPDATED_USER_ID)
            .tokenDevice(UPDATED_TOKEN_DEVICE)
            .deviceName(UPDATED_DEVICE_NAME)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return tokenDeviceUser;
    }

    @BeforeEach
    public void initTest() {
        tokenDeviceUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createTokenDeviceUser() throws Exception {
        int databaseSizeBeforeCreate = tokenDeviceUserRepository.findAll().size();

        // Create the TokenDeviceUser
        TokenDeviceUserDTO tokenDeviceUserDTO = tokenDeviceUserMapper.toDto(tokenDeviceUser);
        restTokenDeviceUserMockMvc.perform(post("/api/token-device-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tokenDeviceUserDTO)))
            .andExpect(status().isCreated());

        // Validate the TokenDeviceUser in the database
        List<TokenDeviceUser> tokenDeviceUserList = tokenDeviceUserRepository.findAll();
        assertThat(tokenDeviceUserList).hasSize(databaseSizeBeforeCreate + 1);
        TokenDeviceUser testTokenDeviceUser = tokenDeviceUserList.get(tokenDeviceUserList.size() - 1);
        assertThat(testTokenDeviceUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTokenDeviceUser.getTokenDevice()).isEqualTo(DEFAULT_TOKEN_DEVICE);
        assertThat(testTokenDeviceUser.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
        assertThat(testTokenDeviceUser.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTokenDeviceUser.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testTokenDeviceUser.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createTokenDeviceUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tokenDeviceUserRepository.findAll().size();

        // Create the TokenDeviceUser with an existing ID
        tokenDeviceUser.setId(1L);
        TokenDeviceUserDTO tokenDeviceUserDTO = tokenDeviceUserMapper.toDto(tokenDeviceUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTokenDeviceUserMockMvc.perform(post("/api/token-device-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tokenDeviceUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TokenDeviceUser in the database
        List<TokenDeviceUser> tokenDeviceUserList = tokenDeviceUserRepository.findAll();
        assertThat(tokenDeviceUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTokenDeviceUsers() throws Exception {
        // Initialize the database
        tokenDeviceUserRepository.saveAndFlush(tokenDeviceUser);

        // Get all the tokenDeviceUserList
        restTokenDeviceUserMockMvc.perform(get("/api/token-device-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tokenDeviceUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].tokenDevice").value(hasItem(DEFAULT_TOKEN_DEVICE)))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getTokenDeviceUser() throws Exception {
        // Initialize the database
        tokenDeviceUserRepository.saveAndFlush(tokenDeviceUser);

        // Get the tokenDeviceUser
        restTokenDeviceUserMockMvc.perform(get("/api/token-device-users/{id}", tokenDeviceUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tokenDeviceUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.tokenDevice").value(DEFAULT_TOKEN_DEVICE))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingTokenDeviceUser() throws Exception {
        // Get the tokenDeviceUser
        restTokenDeviceUserMockMvc.perform(get("/api/token-device-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTokenDeviceUser() throws Exception {
        // Initialize the database
        tokenDeviceUserRepository.saveAndFlush(tokenDeviceUser);

        int databaseSizeBeforeUpdate = tokenDeviceUserRepository.findAll().size();

        // Update the tokenDeviceUser
        TokenDeviceUser updatedTokenDeviceUser = tokenDeviceUserRepository.findById(tokenDeviceUser.getId()).get();
        // Disconnect from session so that the updates on updatedTokenDeviceUser are not directly saved in db
        em.detach(updatedTokenDeviceUser);
        updatedTokenDeviceUser
            .userId(UPDATED_USER_ID)
            .tokenDevice(UPDATED_TOKEN_DEVICE)
            .deviceName(UPDATED_DEVICE_NAME)
            .status(UPDATED_STATUS)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        TokenDeviceUserDTO tokenDeviceUserDTO = tokenDeviceUserMapper.toDto(updatedTokenDeviceUser);

        restTokenDeviceUserMockMvc.perform(put("/api/token-device-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tokenDeviceUserDTO)))
            .andExpect(status().isOk());

        // Validate the TokenDeviceUser in the database
        List<TokenDeviceUser> tokenDeviceUserList = tokenDeviceUserRepository.findAll();
        assertThat(tokenDeviceUserList).hasSize(databaseSizeBeforeUpdate);
        TokenDeviceUser testTokenDeviceUser = tokenDeviceUserList.get(tokenDeviceUserList.size() - 1);
        assertThat(testTokenDeviceUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTokenDeviceUser.getTokenDevice()).isEqualTo(UPDATED_TOKEN_DEVICE);
        assertThat(testTokenDeviceUser.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testTokenDeviceUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTokenDeviceUser.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testTokenDeviceUser.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingTokenDeviceUser() throws Exception {
        int databaseSizeBeforeUpdate = tokenDeviceUserRepository.findAll().size();

        // Create the TokenDeviceUser
        TokenDeviceUserDTO tokenDeviceUserDTO = tokenDeviceUserMapper.toDto(tokenDeviceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTokenDeviceUserMockMvc.perform(put("/api/token-device-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tokenDeviceUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TokenDeviceUser in the database
        List<TokenDeviceUser> tokenDeviceUserList = tokenDeviceUserRepository.findAll();
        assertThat(tokenDeviceUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTokenDeviceUser() throws Exception {
        // Initialize the database
        tokenDeviceUserRepository.saveAndFlush(tokenDeviceUser);

        int databaseSizeBeforeDelete = tokenDeviceUserRepository.findAll().size();

        // Delete the tokenDeviceUser
        restTokenDeviceUserMockMvc.perform(delete("/api/token-device-users/{id}", tokenDeviceUser.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TokenDeviceUser> tokenDeviceUserList = tokenDeviceUserRepository.findAll();
        assertThat(tokenDeviceUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
