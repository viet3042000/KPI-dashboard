package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.Notification;
import com.b4t.app.repository.NotificationRepository;
import com.b4t.app.service.NotificationService;
import com.b4t.app.service.NotificationUserService;
import com.b4t.app.service.dto.NotificationDTO;
import com.b4t.app.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class NotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_USER_ID_SENT = 1L;
    private static final Long UPDATED_USER_ID_SENT = 2L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_ATTACH_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_ATTACH_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_ATTACH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_ATTACH_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SCREEN_ID = 1L;
    private static final Long UPDATED_SCREEN_ID = 2L;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_JSON_DATA = "AAAAAAAAAA";
    private static final String UPDATED_JSON_DATA = "BBBBBBBBBB";

    private static final Long DEFAULT_IS_SYSTEM_NOTIFY = 1L;
    private static final Long UPDATED_IS_SYSTEM_NOTIFY = 2L;

    private static final Long DEFAULT_IS_SENT = 1L;
    private static final Long UPDATED_IS_SENT = 2L;

    private static final Instant DEFAULT_UPDATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_USER = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_USER = "BBBBBBBBBB";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationService notificationService;

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

    private MockMvc restNotificationMockMvc;

    private Notification notification;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationResource notificationResource = new NotificationResource(notificationService, notificationUserService);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource)
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
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .title(DEFAULT_TITLE)
            .userIdSent(DEFAULT_USER_ID_SENT)
            .content(DEFAULT_CONTENT)
            .fileAttachPath(DEFAULT_FILE_ATTACH_PATH)
            .fileAttachName(DEFAULT_FILE_ATTACH_NAME)
            .screenId(DEFAULT_SCREEN_ID)
            .createDate(DEFAULT_CREATE_DATE)
            .jsonData(DEFAULT_JSON_DATA)
            .isSystemNotify(DEFAULT_IS_SYSTEM_NOTIFY)
            .isSent(DEFAULT_IS_SENT)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateUser(DEFAULT_UPDATE_USER);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .title(UPDATED_TITLE)
            .userIdSent(UPDATED_USER_ID_SENT)
            .content(UPDATED_CONTENT)
            .fileAttachPath(UPDATED_FILE_ATTACH_PATH)
            .fileAttachName(UPDATED_FILE_ATTACH_NAME)
            .screenId(UPDATED_SCREEN_ID)
            .createDate(UPDATED_CREATE_DATE)
            .jsonData(UPDATED_JSON_DATA)
            .isSystemNotify(UPDATED_IS_SYSTEM_NOTIFY)
            .isSent(UPDATED_IS_SENT)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotification.getUserIdSent()).isEqualTo(DEFAULT_USER_ID_SENT);
        assertThat(testNotification.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNotification.getFileAttachPath()).isEqualTo(DEFAULT_FILE_ATTACH_PATH);
        assertThat(testNotification.getFileAttachName()).isEqualTo(DEFAULT_FILE_ATTACH_NAME);
        assertThat(testNotification.getScreenId()).isEqualTo(DEFAULT_SCREEN_ID);
        assertThat(testNotification.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testNotification.getJsonData()).isEqualTo(DEFAULT_JSON_DATA);
        assertThat(testNotification.getIsSystemNotify()).isEqualTo(DEFAULT_IS_SYSTEM_NOTIFY);
        assertThat(testNotification.getIsSent()).isEqualTo(DEFAULT_IS_SENT);
        assertThat(testNotification.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testNotification.getUpdateUser()).isEqualTo(DEFAULT_UPDATE_USER);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].userIdSent").value(hasItem(DEFAULT_USER_ID_SENT.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].fileAttachPath").value(hasItem(DEFAULT_FILE_ATTACH_PATH)))
            .andExpect(jsonPath("$.[*].fileAttachName").value(hasItem(DEFAULT_FILE_ATTACH_NAME)))
            .andExpect(jsonPath("$.[*].screenId").value(hasItem(DEFAULT_SCREEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].jsonData").value(hasItem(DEFAULT_JSON_DATA)))
            .andExpect(jsonPath("$.[*].isSystemNotify").value(hasItem(DEFAULT_IS_SYSTEM_NOTIFY.intValue())))
            .andExpect(jsonPath("$.[*].isSent").value(hasItem(DEFAULT_IS_SENT.intValue())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateUser").value(hasItem(DEFAULT_UPDATE_USER)));
    }

    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.userIdSent").value(DEFAULT_USER_ID_SENT.intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.fileAttachPath").value(DEFAULT_FILE_ATTACH_PATH))
            .andExpect(jsonPath("$.fileAttachName").value(DEFAULT_FILE_ATTACH_NAME))
            .andExpect(jsonPath("$.screenId").value(DEFAULT_SCREEN_ID.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.jsonData").value(DEFAULT_JSON_DATA))
            .andExpect(jsonPath("$.isSystemNotify").value(DEFAULT_IS_SYSTEM_NOTIFY.intValue()))
            .andExpect(jsonPath("$.isSent").value(DEFAULT_IS_SENT.intValue()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.updateUser").value(DEFAULT_UPDATE_USER));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .title(UPDATED_TITLE)
            .userIdSent(UPDATED_USER_ID_SENT)
            .content(UPDATED_CONTENT)
            .fileAttachPath(UPDATED_FILE_ATTACH_PATH)
            .fileAttachName(UPDATED_FILE_ATTACH_NAME)
            .screenId(UPDATED_SCREEN_ID)
            .createDate(UPDATED_CREATE_DATE)
            .jsonData(UPDATED_JSON_DATA)
            .isSystemNotify(UPDATED_IS_SYSTEM_NOTIFY)
            .isSent(UPDATED_IS_SENT)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateUser(UPDATED_UPDATE_USER);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotification.getUserIdSent()).isEqualTo(UPDATED_USER_ID_SENT);
        assertThat(testNotification.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotification.getFileAttachPath()).isEqualTo(UPDATED_FILE_ATTACH_PATH);
        assertThat(testNotification.getFileAttachName()).isEqualTo(UPDATED_FILE_ATTACH_NAME);
        assertThat(testNotification.getScreenId()).isEqualTo(UPDATED_SCREEN_ID);
        assertThat(testNotification.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testNotification.getJsonData()).isEqualTo(UPDATED_JSON_DATA);
        assertThat(testNotification.getIsSystemNotify()).isEqualTo(UPDATED_IS_SYSTEM_NOTIFY);
        assertThat(testNotification.getIsSent()).isEqualTo(UPDATED_IS_SENT);
        assertThat(testNotification.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testNotification.getUpdateUser()).isEqualTo(UPDATED_UPDATE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
