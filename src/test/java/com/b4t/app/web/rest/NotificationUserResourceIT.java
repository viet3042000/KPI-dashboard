package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.NotificationUser;
import com.b4t.app.repository.NotificationUserRepository;
import com.b4t.app.service.NotificationUserService;
import com.b4t.app.service.dto.NotificationUserDTO;
import com.b4t.app.service.mapper.NotificationUserMapper;
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
 * Integration tests for the {@link NotificationUserResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class NotificationUserResourceIT {

    private static final Long DEFAULT_NOTIFY_ID = 1L;
    private static final Long UPDATED_NOTIFY_ID = 2L;

    private static final Long DEFAULT_USER_ID_RECIEVED = 1L;
    private static final Long UPDATED_USER_ID_RECIEVED = 2L;

    private static final Long DEFAULT_IS_NEW = 1L;
    private static final Long UPDATED_IS_NEW = 2L;

    private static final Long DEFAULT_IS_READ = 1L;
    private static final Long UPDATED_IS_READ = 2L;

    private static final Long DEFAULT_IS_DELETED = 1L;
    private static final Long UPDATED_IS_DELETED = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Autowired
    private NotificationUserMapper notificationUserMapper;

    @Autowired
    private NotificationUserService notificationUserService;

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

    private MockMvc restNotificationUserMockMvc;

    private NotificationUser notificationUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationUserResource notificationUserResource = new NotificationUserResource(notificationUserService);
        this.restNotificationUserMockMvc = MockMvcBuilders.standaloneSetup(notificationUserResource)
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
    public static NotificationUser createEntity(EntityManager em) {
        NotificationUser notificationUser = new NotificationUser()
            .notifyId(DEFAULT_NOTIFY_ID)
            .userIdRecieved(DEFAULT_USER_ID_RECIEVED)
            .isNew(DEFAULT_IS_NEW)
            .isRead(DEFAULT_IS_READ)
            .isDeleted(DEFAULT_IS_DELETED)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return notificationUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationUser createUpdatedEntity(EntityManager em) {
        NotificationUser notificationUser = new NotificationUser()
            .notifyId(UPDATED_NOTIFY_ID)
            .userIdRecieved(UPDATED_USER_ID_RECIEVED)
            .isNew(UPDATED_IS_NEW)
            .isRead(UPDATED_IS_READ)
            .isDeleted(UPDATED_IS_DELETED)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return notificationUser;
    }

    @BeforeEach
    public void initTest() {
        notificationUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationUser() throws Exception {
        int databaseSizeBeforeCreate = notificationUserRepository.findAll().size();

        // Create the NotificationUser
        NotificationUserDTO notificationUserDTO = notificationUserMapper.toDto(notificationUser);
        restNotificationUserMockMvc.perform(post("/api/notification-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationUserDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationUser in the database
        List<NotificationUser> notificationUserList = notificationUserRepository.findAll();
        assertThat(notificationUserList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationUser testNotificationUser = notificationUserList.get(notificationUserList.size() - 1);
        assertThat(testNotificationUser.getNotifyId()).isEqualTo(DEFAULT_NOTIFY_ID);
        assertThat(testNotificationUser.getUserIdRecieved()).isEqualTo(DEFAULT_USER_ID_RECIEVED);
        assertThat(testNotificationUser.getIsNew()).isEqualTo(DEFAULT_IS_NEW);
        assertThat(testNotificationUser.getIsRead()).isEqualTo(DEFAULT_IS_READ);
        assertThat(testNotificationUser.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testNotificationUser.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testNotificationUser.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createNotificationUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationUserRepository.findAll().size();

        // Create the NotificationUser with an existing ID
        notificationUser.setId(1L);
        NotificationUserDTO notificationUserDTO = notificationUserMapper.toDto(notificationUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationUserMockMvc.perform(post("/api/notification-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationUser in the database
        List<NotificationUser> notificationUserList = notificationUserRepository.findAll();
        assertThat(notificationUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotificationUsers() throws Exception {
        // Initialize the database
        notificationUserRepository.saveAndFlush(notificationUser);

        // Get all the notificationUserList
        restNotificationUserMockMvc.perform(get("/api/notification-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].notifyId").value(hasItem(DEFAULT_NOTIFY_ID.intValue())))
            .andExpect(jsonPath("$.[*].userIdRecieved").value(hasItem(DEFAULT_USER_ID_RECIEVED.intValue())))
            .andExpect(jsonPath("$.[*].isNew").value(hasItem(DEFAULT_IS_NEW.intValue())))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ.intValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }
    
    @Test
    @Transactional
    public void getNotificationUser() throws Exception {
        // Initialize the database
        notificationUserRepository.saveAndFlush(notificationUser);

        // Get the notificationUser
        restNotificationUserMockMvc.perform(get("/api/notification-users/{id}", notificationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationUser.getId().intValue()))
            .andExpect(jsonPath("$.notifyId").value(DEFAULT_NOTIFY_ID.intValue()))
            .andExpect(jsonPath("$.userIdRecieved").value(DEFAULT_USER_ID_RECIEVED.intValue()))
            .andExpect(jsonPath("$.isNew").value(DEFAULT_IS_NEW.intValue()))
            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ.intValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationUser() throws Exception {
        // Get the notificationUser
        restNotificationUserMockMvc.perform(get("/api/notification-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationUser() throws Exception {
        // Initialize the database
        notificationUserRepository.saveAndFlush(notificationUser);

        int databaseSizeBeforeUpdate = notificationUserRepository.findAll().size();

        // Update the notificationUser
        NotificationUser updatedNotificationUser = notificationUserRepository.findById(notificationUser.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationUser are not directly saved in db
        em.detach(updatedNotificationUser);
        updatedNotificationUser
            .notifyId(UPDATED_NOTIFY_ID)
            .userIdRecieved(UPDATED_USER_ID_RECIEVED)
            .isNew(UPDATED_IS_NEW)
            .isRead(UPDATED_IS_READ)
            .isDeleted(UPDATED_IS_DELETED)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        NotificationUserDTO notificationUserDTO = notificationUserMapper.toDto(updatedNotificationUser);

        restNotificationUserMockMvc.perform(put("/api/notification-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationUserDTO)))
            .andExpect(status().isOk());

        // Validate the NotificationUser in the database
        List<NotificationUser> notificationUserList = notificationUserRepository.findAll();
        assertThat(notificationUserList).hasSize(databaseSizeBeforeUpdate);
        NotificationUser testNotificationUser = notificationUserList.get(notificationUserList.size() - 1);
        assertThat(testNotificationUser.getNotifyId()).isEqualTo(UPDATED_NOTIFY_ID);
        assertThat(testNotificationUser.getUserIdRecieved()).isEqualTo(UPDATED_USER_ID_RECIEVED);
        assertThat(testNotificationUser.getIsNew()).isEqualTo(UPDATED_IS_NEW);
        assertThat(testNotificationUser.getIsRead()).isEqualTo(UPDATED_IS_READ);
        assertThat(testNotificationUser.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNotificationUser.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testNotificationUser.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationUser() throws Exception {
        int databaseSizeBeforeUpdate = notificationUserRepository.findAll().size();

        // Create the NotificationUser
        NotificationUserDTO notificationUserDTO = notificationUserMapper.toDto(notificationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationUserMockMvc.perform(put("/api/notification-users")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationUser in the database
        List<NotificationUser> notificationUserList = notificationUserRepository.findAll();
        assertThat(notificationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotificationUser() throws Exception {
        // Initialize the database
        notificationUserRepository.saveAndFlush(notificationUser);

        int databaseSizeBeforeDelete = notificationUserRepository.findAll().size();

        // Delete the notificationUser
        restNotificationUserMockMvc.perform(delete("/api/notification-users/{id}", notificationUser.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotificationUser> notificationUserList = notificationUserRepository.findAll();
        assertThat(notificationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
