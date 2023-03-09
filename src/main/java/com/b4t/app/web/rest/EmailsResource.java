package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.service.EmailsService;
import com.b4t.app.service.UserService;
import com.b4t.app.service.dto.UserDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.EmailsDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.Emails}.
 */
@RestController
@RequestMapping("/api")
public class EmailsResource {

    private final Logger log = LoggerFactory.getLogger(EmailsResource.class);
    private static final String VALIDATE_RECEIVE = "error.notification.receivernull";
    private static final String ENTITY_NAME = "emails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailsService emailsService;

    private final UserService userService;

    public EmailsResource(
        EmailsService emailsService,
        UserService userService) {
        this.emailsService = emailsService;
        this.userService = userService;
    }

    /**
     * {@code POST  /emails} : Create a new emails.
     *
     * @param emailsDTO the emailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailsDTO, or with status {@code 400 (Bad Request)} if the emails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emails")
    public ResponseEntity<List<EmailsDTO>> createEmails(@RequestBody EmailsDTO emailsDTO) throws URISyntaxException {
        log.debug("REST request to save Emails : {}", emailsDTO);
        if (emailsDTO.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        if (emailsDTO.getUserId() == null && DataUtil.isNullOrEmpty(emailsDTO.getUserIds())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_RECEIVE), ENTITY_NAME, VALIDATE_RECEIVE);
        }
        if (StringUtils.isEmpty(emailsDTO.getSubject())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.subjectnull"), ENTITY_NAME, "error.notification.subjectnull");
        }
        if (StringUtils.isEmpty(emailsDTO.getContent())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.contentnull"), ENTITY_NAME, "error.notification.contentnull");
        }
        List<EmailsDTO> result;
        List<Long> userIds = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(emailsDTO.getUserIds())) {
            userIds = emailsDTO.getUserIds();
        } else {
            userIds.add(emailsDTO.getId());
        }
        List<UserDTO> users = userService.getByIds(userIds);
        if (DataUtil.isNullOrEmpty(users)) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_RECEIVE), ENTITY_NAME, VALIDATE_RECEIVE);
        }
        List<EmailsDTO> dtos = users.stream().map(i -> {
            EmailsDTO dto = new EmailsDTO();
            dto.setEmail(i.getEmail());
            dto.setContent(StringUtils.isNotEmpty(emailsDTO.getContent()) ? emailsDTO.getContent().trim() : emailsDTO.getContent());
            dto.setIsRepeat(emailsDTO.getIsRepeat());
            dto.setNotifyId(emailsDTO.getNotifyId());
            dto.setSource(emailsDTO.getSource());
            dto.setStatus(0L);
            dto.setSubject(StringUtils.isNotEmpty(emailsDTO.getSubject()) ? emailsDTO.getSubject().trim() : emailsDTO.getSubject());
            dto.setUserId(i.getId());
            return dto;
        }).collect(Collectors.toList());
        result = emailsService.saveAll(dtos);
        Long[] ids = result.stream().map(EmailsDTO::getId).toArray(Long[]::new);
        return ResponseEntity.created(new URI("/api/emails/" + StringUtils.join(ids, ",")))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, StringUtils.join(ids, ",")))
            .body(result);
    }

    /**
     * {@code PUT  /emails} : Updates an existing emails.
     *
     * @param emailsDTO the emailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailsDTO,
     * or with status {@code 400 (Bad Request)} if the emailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emails")
    public ResponseEntity<EmailsDTO> updateEmails(@RequestBody EmailsDTO emailsDTO) {
        log.debug("REST request to update Emails : {}", emailsDTO);
        if (emailsDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), ENTITY_NAME, "error.idnull");
        }
        if (emailsDTO.getUserId() == null && DataUtil.isNullOrEmpty(emailsDTO.getUserIds())) {
            throw new BadRequestAlertException(Translator.toLocale(VALIDATE_RECEIVE), ENTITY_NAME, VALIDATE_RECEIVE);
        }
        EmailsDTO result = emailsService.save(emailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /emails} : get all the emails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emails in body.
     */
    @GetMapping("/emails")
    public ResponseEntity<List<EmailsDTO>> getAllEmails(String keyword, Long[] notifyIds, Long[] userIds, Long status, Long isRepeat, Pageable pageable) {
        log.debug("REST request to get a page of Emails");
        Page<EmailsDTO> page = emailsService.findAll(keyword, notifyIds, userIds, status, isRepeat, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emails/:id} : get the "id" emails.
     *
     * @param id the id of the emailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emails/{id}")
    public ResponseEntity<EmailsDTO> getEmails(@PathVariable Long id) {
        log.debug("REST request to get Emails : {}", id);
        Optional<EmailsDTO> emailsDTO = emailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailsDTO);
    }

    /**
     * {@code DELETE  /emails/:id} : delete the "id" emails.
     *
     * @param id the id of the emailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emails/{id}")
    public ResponseEntity<Void> deleteEmails(@PathVariable Long id) {
        log.debug("REST request to delete Emails : {}", id);
        emailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
