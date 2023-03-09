package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.Notification_;
import com.b4t.app.domain.User;
import com.b4t.app.service.NotificationService;
import com.b4t.app.service.NotificationUserService;
import com.b4t.app.service.UserService;
import com.b4t.app.service.dto.NotificationUserDTO;
import com.b4t.app.service.dto.SaveNotificationDTO;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.NotificationDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.Notification}.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationService notificationService;

    private final NotificationUserService notificationUserService;
    @Autowired
    UserService userService;

    public NotificationResource(
        NotificationService notificationService,
        NotificationUserService notificationUserService) {
        this.notificationService = notificationService;
        this.notificationUserService = notificationUserService;
    }

    /**
     * {@code POST  /notifications} : Create a new notification.
     *
     * @param saveDto the notificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationDTO, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(value = "/notifications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NotificationDTO> createNotification(@ModelAttribute SaveNotificationDTO saveDto) throws Exception {
        validateNotificationPermission(saveDto.getUserIdSent());
        log.debug("REST request to save Notification : {}", saveDto);
        if (saveDto.getId() != null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
        }
        if (DataUtil.isNullOrEmpty(saveDto.getReceivedUserIds())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.receivernull"), ENTITY_NAME, "error.notification.receivernull");
        }
        if (saveDto.getUserIdSent() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.sendernull"), ENTITY_NAME, "error.notification.sendernull");
        }
        if (StringUtils.isEmpty(saveDto.getContent()) && saveDto.getImage() == null && saveDto.getAttachment() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.contentnull"), ENTITY_NAME, "error.notification.contentnull");
        }
        if (saveDto.getIsSent() == null)
            saveDto.setIsSent(1L);
        NotificationDTO result = notificationService.create(saveDto);
        HttpHeaders headers = HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(headers)
            .body(result);
    }

    /**
     * {@code PUT  /notifications} : Updates an existing notification.
     *
     * @param notificationDTO the notificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationDTO couldn't be updated.
     */
    @PutMapping("/notifications")
    public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO) {
        validateNotificationPermission(notificationDTO.getUserIdSent());
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.getId() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.idnull"), Notification_.ID, "error.idnull");
        }
        Optional<NotificationDTO> ckOpt = notificationService.findOne(notificationDTO.getId());
        if (ckOpt.isPresent() && !ckOpt.get().getUpdateTime().equals(notificationDTO.getUpdateTime())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notlastestdata"), ENTITY_NAME, "error.notlastestdata");
        }
        if (DataUtil.isNullOrEmpty(notificationDTO.getNotificationUsers())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.receivernull"), ENTITY_NAME, "error.notification.receivernull");
        }
        if (notificationDTO.getUserIdSent() == null) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.sendernull"), ENTITY_NAME, "error.notification.sendernull");
        }
        if (StringUtils.isEmpty(notificationDTO.getContent())) {
            throw new BadRequestAlertException(Translator.toLocale("error.notification.contentnull"), ENTITY_NAME, "error.notification.contentnull");
        }
        NotificationDTO result = notificationService.save(notificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notifications} : get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(String keyword, Long sentUserId, Long[] receivedUserIds,
                                                                     Long isNew, Long isRead, Pageable pageable) {
        validateNotificationPermission(sentUserId);
        log.debug("REST request to get a page of Notifications");
        Page<NotificationDTO> page = notificationService.findAll(keyword, sentUserId, receivedUserIds, isNew, isRead, 0L, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

//    /**
//     * {@code GET  /notifications/:id} : get the "id" notification.
//     *
//     * @param id the id of the notificationDTO to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationDTO, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/notifications/{id}")
//    public ResponseEntity<NotificationDTO> getNotification(@PathVariable Long id) {
//        log.debug("REST request to get Notification : {}", id);
//        Optional<NotificationDTO> dtoOpt = notificationService.findOne(id);
//        if (!dtoOpt.isPresent()) return ResponseUtil.wrapOrNotFound(dtoOpt);
//        NotificationDTO dto = dtoOpt.get();
//        List<NotificationUserDTO> notificationUsers = notificationUserService.findByNotifyId(dto.getId());
//        dto.setNotificationUsers(notificationUsers);
//        return ResponseUtil.wrapOrNotFound(Optional.of(dto));
//    }

//    /**
//     * {@code DELETE  /notifications/:id} : delete the "id" notification.
//     *
//     * @param id the id of the notificationDTO to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/notifications/{id}")
//    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
//        log.debug("REST request to delete Notification : {}", id);
//        Optional<NotificationDTO> dto = notificationService.findOne(id);
//        if (!dto.isPresent()) {
//            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), ENTITY_NAME, "error.isnotexisted");
//        }
//        notificationService.delete(id);
//        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//    }

    private void validateNotificationPermission(Long sentUserId) {
        User user = userService.getUserWithAuthorities().orElse(null);
        if (user == null || !user.getId().equals(sentUserId)) {
            throw new BadRequestAlertException(Translator.toLocale("notification.notPermission"), "notifications", "notification.notPermission");
        }
    }
}
