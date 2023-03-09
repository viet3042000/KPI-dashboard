package com.b4t.app.web.rest;

import com.b4t.app.Application;
import com.b4t.app.domain.Emails;
import com.b4t.app.repository.EmailsRepository;
import com.b4t.app.service.EmailsService;
import com.b4t.app.service.UserService;
import com.b4t.app.service.dto.EmailsDTO;
import com.b4t.app.service.mapper.EmailsMapper;
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
 * Integration tests for the {@link EmailsResource} REST controller.
 */
@SpringBootTest(classes = Application.class)
public class EmailsResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_STATUS = 1L;
    private static final Long UPDATED_STATUS = 2L;

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SEND_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEND_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_IS_REPEAT = 1L;
    private static final Long UPDATED_IS_REPEAT = 2L;

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final Long DEFAULT_NOTIFY_ID = 1L;
    private static final Long UPDATED_NOTIFY_ID = 2L;

    @Autowired
    private EmailsRepository emailsRepository;

    @Autowired
    private EmailsMapper emailsMapper;

    @Autowired
    private EmailsService emailsService;

    @Autowired
    private UserService userService;

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

    private MockMvc restEmailsMockMvc;

    private Emails emails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailsResource emailsResource = new EmailsResource(emailsService, userService);
        this.restEmailsMockMvc = MockMvcBuilders.standaloneSetup(emailsResource)
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
    public static Emails createEntity(EntityManager em) {
        Emails emails = new Emails()
            .subject(DEFAULT_SUBJECT)
            .email(DEFAULT_EMAIL)
            .content(DEFAULT_CONTENT)
            .status(DEFAULT_STATUS)
            .createTime(DEFAULT_CREATE_TIME)
            .sendTime(DEFAULT_SEND_TIME)
            .userId(DEFAULT_USER_ID)
            .isRepeat(DEFAULT_IS_REPEAT)
            .source(DEFAULT_SOURCE)
            .notifyId(DEFAULT_NOTIFY_ID);
        return emails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emails createUpdatedEntity(EntityManager em) {
        Emails emails = new Emails()
            .subject(UPDATED_SUBJECT)
            .email(UPDATED_EMAIL)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS)
            .createTime(UPDATED_CREATE_TIME)
            .sendTime(UPDATED_SEND_TIME)
            .userId(UPDATED_USER_ID)
            .isRepeat(UPDATED_IS_REPEAT)
            .source(UPDATED_SOURCE)
            .notifyId(UPDATED_NOTIFY_ID);
        return emails;
    }

    @BeforeEach
    public void initTest() {
        emails = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmails() throws Exception {
        int databaseSizeBeforeCreate = emailsRepository.findAll().size();

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);
        restEmailsMockMvc.perform(post("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailsDTO)))
            .andExpect(status().isCreated());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeCreate + 1);
        Emails testEmails = emailsList.get(emailsList.size() - 1);
        assertThat(testEmails.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testEmails.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmails.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEmails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmails.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testEmails.getSentTime()).isEqualTo(DEFAULT_SEND_TIME);
        assertThat(testEmails.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testEmails.getIsRepeat()).isEqualTo(DEFAULT_IS_REPEAT);
        assertThat(testEmails.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testEmails.getNotifyId()).isEqualTo(DEFAULT_NOTIFY_ID);
    }

    @Test
    @Transactional
    public void createEmailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailsRepository.findAll().size();

        // Create the Emails with an existing ID
        emails.setId(1L);
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailsMockMvc.perform(post("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        // Get all the emailsList
        restEmailsMockMvc.perform(get("/api/emails?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emails.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(DEFAULT_SEND_TIME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].isRepeat").value(hasItem(DEFAULT_IS_REPEAT.intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].notifyId").value(hasItem(DEFAULT_NOTIFY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        // Get the emails
        restEmailsMockMvc.perform(get("/api/emails/{id}", emails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emails.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.sendTime").value(DEFAULT_SEND_TIME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.isRepeat").value(DEFAULT_IS_REPEAT.intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.notifyId").value(DEFAULT_NOTIFY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmails() throws Exception {
        // Get the emails
        restEmailsMockMvc.perform(get("/api/emails/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();

        // Update the emails
        Emails updatedEmails = emailsRepository.findById(emails.getId()).get();
        // Disconnect from session so that the updates on updatedEmails are not directly saved in db
        em.detach(updatedEmails);
        updatedEmails
            .subject(UPDATED_SUBJECT)
            .email(UPDATED_EMAIL)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS)
            .createTime(UPDATED_CREATE_TIME)
            .sendTime(UPDATED_SEND_TIME)
            .userId(UPDATED_USER_ID)
            .isRepeat(UPDATED_IS_REPEAT)
            .source(UPDATED_SOURCE)
            .notifyId(UPDATED_NOTIFY_ID);
        EmailsDTO emailsDTO = emailsMapper.toDto(updatedEmails);

        restEmailsMockMvc.perform(put("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailsDTO)))
            .andExpect(status().isOk());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
        Emails testEmails = emailsList.get(emailsList.size() - 1);
        assertThat(testEmails.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testEmails.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmails.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEmails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmails.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testEmails.getSentTime()).isEqualTo(UPDATED_SEND_TIME);
        assertThat(testEmails.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEmails.getIsRepeat()).isEqualTo(UPDATED_IS_REPEAT);
        assertThat(testEmails.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testEmails.getNotifyId()).isEqualTo(UPDATED_NOTIFY_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailsMockMvc.perform(put("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        int databaseSizeBeforeDelete = emailsRepository.findAll().size();

        // Delete the emails
        restEmailsMockMvc.perform(delete("/api/emails/{id}", emails.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
