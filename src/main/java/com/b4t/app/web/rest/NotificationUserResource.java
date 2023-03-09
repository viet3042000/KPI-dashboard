package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.NotificationUser_;
import com.b4t.app.service.NotificationUserService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.NotificationUserDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.b4t.app.domain.NotificationUser}.
 */
@RestController
@RequestMapping("/api")
public class NotificationUserResource {

    private final Logger log = LoggerFactory.getLogger(NotificationUserResource.class);

    private static final String ENTITY_NAME = "notificationUser";
    private static final String ISNOT_EXISTED = "error.isnotexisted";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationUserService notificationUserService;

    public NotificationUserResource(NotificationUserService notificationUserService) {
        this.notificationUserService = notificationUserService;
    }

//    /**
//     * {@code POST  /notification-users} : Create a new notificationUser.
//     *
//     * @param notificationUserDTO the notificationUserDTO to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationUserDTO, or with status {@code 400 (Bad Request)} if the notificationUser has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("/notification-users")
//    public ResponseEntity<NotificationUserDTO> createNotificationUser(@RequestBody NotificationUserDTO notificationUserDTO) throws URISyntaxException {
//        log.debug("REST request to save NotificationUser : {}", notificationUserDTO);
//        if (notificationUserDTO.getId() != null) {
//            throw new BadRequestAlertException(Translator.toLocale("error.idexists"), ENTITY_NAME, "error.idexists");
//        }
//        NotificationUserDTO result = notificationUserService.save(notificationUserDTO);
//        return ResponseEntity.created(new URI("/api/notification-users/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }

//    /**
//     * {@code PUT  /notification-users} : Updates an existing notificationUser.
//     *
//     * @param notificationUserDTO the notificationUserDTO to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationUserDTO,
//     * or with status {@code 400 (Bad Request)} if the notificationUserDTO is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the notificationUserDTO couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/notification-users")
//    public ResponseEntity<NotificationUserDTO> updateNotificationUser(@RequestBody NotificationUserDTO notificationUserDTO) throws URISyntaxException {
//        log.debug("REST request to update NotificationUser : {}", notificationUserDTO);
//        if (notificationUserDTO.getId() == null) {
//            throw new BadRequestAlertException(Translator.toLocale(Constants.VALIDATE.ID_NULL), NotificationUser_.ID, Constants.VALIDATE.ID_NULL);
//        }
//        Optional<NotificationUserDTO> ckOpt = notificationUserService.findOne(notificationUserDTO.getId());
//        if (!ckOpt.isPresent()) {
//            throw new BadRequestAlertException(Translator.toLocale(ISNOT_EXISTED), NotificationUser_.ID, ISNOT_EXISTED);
//        }
//        NotificationUserDTO result = notificationUserService.save(notificationUserDTO);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationUserDTO.getId().toString()))
//            .body(result);
//    }

    @PutMapping("/notification-users/saveAll")
    public ResponseEntity<List<NotificationUserDTO>> updateNotificationUsers(@RequestBody List<NotificationUserDTO> notificationUserDTOs) throws URISyntaxException {
        log.debug("REST request to update NotificationUser : {}", notificationUserDTOs);
        List<NotificationUserDTO> result = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(notificationUserDTOs)) {
//            throw new BadRequestAlertException(Translator.toLocale(Constants.VALIDATE.ID_NULL), NotificationUser_.ID, Constants.VALIDATE.ID_NULL);

            if (notificationUserDTOs.stream().anyMatch(nu -> nu.getId() == null)) {
                throw new BadRequestAlertException(Translator.toLocale(ISNOT_EXISTED), NotificationUser_.ID, ISNOT_EXISTED);
            }
            List<Long> ids = notificationUserDTOs.stream().map(NotificationUserDTO::getId).collect(Collectors.toList());
            List<NotificationUserDTO> ckOpts = notificationUserService.findByIds(ids);
            if (ckOpts.size() != ids.size()) {
                throw new BadRequestAlertException(Translator.toLocale(ISNOT_EXISTED), NotificationUser_.ID, ISNOT_EXISTED);
            }
            result = notificationUserService.readAll(ckOpts);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationUserDTOs.stream().map(NotificationUserDTO::getId).toString()))
            .body(result);
    }

    @PutMapping("/notification-users/delete-notification-user")
    public ResponseEntity<List<NotificationUserDTO>> deleteNotificationUsers(@RequestBody List<NotificationUserDTO> notificationUserDTOs) throws URISyntaxException {
        log.debug("REST request to update NotificationUser : {}", notificationUserDTOs);
        if (DataUtil.isNullOrEmpty(notificationUserDTOs)) {
            throw new BadRequestAlertException(Translator.toLocale(Constants.VALIDATE.ID_NULL), NotificationUser_.ID, Constants.VALIDATE.ID_NULL);
        }
        if (notificationUserDTOs.stream().anyMatch(nu -> nu.getId() == null)) {
            throw new BadRequestAlertException(Translator.toLocale(ISNOT_EXISTED), NotificationUser_.ID, ISNOT_EXISTED);
        }
        List<Long> ids = notificationUserDTOs.stream().map(NotificationUserDTO::getId).collect(Collectors.toList());
        List<NotificationUserDTO> ckOpts = notificationUserService.findByIds(ids);
        if (ckOpts.size() != ids.size()) {
            throw new BadRequestAlertException(Translator.toLocale(ISNOT_EXISTED), NotificationUser_.ID, ISNOT_EXISTED);
        }
        List<NotificationUserDTO> result = notificationUserService.deleteAll(ckOpts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationUserDTOs.stream().map(NotificationUserDTO::getId).toString()))
            .body(result);
    }

//    @PutMapping("/notification-users/by-user-and-notify")
//    public ResponseEntity<List<NotificationUserDTO>> updateNotificationUsersByUserAndNotify(@RequestBody NotificationUserDTO dto) {
//        log.debug("REST request to update NotificationUser : {}", dto);
//        if (dto.getNotifyId() == null || dto.getUserIdRecieved() == null) {
//            if (dto.getNotifyId() == null) {
//                throw new BadRequestAlertException(Translator.toLocale("error.notification.notifyidnull"), NotificationUser_.NOTIFY_ID, "error.notification.notifyidnull");
//            }
//            if (dto.getUserIdRecieved() == null) {
//                throw new BadRequestAlertException(Translator.toLocale("error.notification.receivernull"), NotificationUser_.USER_ID_RECIEVED, "error.notification.receivernull");
//            }
//        }
//
//        if (dto.getIsRead() == null && dto.getIsNew() == null && dto.getIsDeleted() == null) {
//            throw new BadRequestAlertException(Translator.toLocale("error.notification.statusnull"),
//                StringUtils.join(Arrays.asList(NotificationUser_.IS_READ, NotificationUser_.IS_NEW, NotificationUser_.IS_DELETED), ",") , "error.notification.statusnull");
//        }
//        List<NotificationUserDTO> dtos = notificationUserService.findAll(new Long[]{dto.getNotifyId()}, new Long[]{dto.getUserIdRecieved()}, null, null, null, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
//        if (!DataUtil.isNullOrEmpty(dtos)) {
//            dtos = dtos.stream().peek(i -> {
//                if (dto.getIsDeleted() != null)
//                    i.setIsDeleted(dto.getIsDeleted());
//                if (dto.getIsNew() != null)
//                    i.setIsNew(dto.getIsNew());
//                if (dto.getIsRead() != null)
//                    i.setIsRead(dto.getIsRead());
//            }).collect(Collectors.toList());
//        }
//        List<NotificationUserDTO> result = notificationUserService.saveAll(dtos);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.stream().map(NotificationUserDTO::getId).toString()))
//            .body(result);
//    }

//    /**
//     * {@code GET  /notification-users} : get all the notificationUsers.
//     *
//     * @param pageable the pagination information.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationUsers in body.
//     */
//    @GetMapping("/notification-users")
//    public ResponseEntity<List<NotificationUserDTO>> getAllNotificationUsers(Long[] notifyIds, Long[] userIdRecieved, Long isNew, Long isRead, Long isDeleted, Pageable pageable) {
//        log.debug("REST request to get a page of NotificationUsers");
//        Page<NotificationUserDTO> page = notificationUserService.findAll(notifyIds, userIdRecieved, isNew, isRead, isDeleted, pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }
//
//    /**
//     * {@code GET  /notification-users/:id} : get the "id" notificationUser.
//     *
//     * @param id the id of the notificationUserDTO to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationUserDTO, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/notification-users/{id}")
//    public ResponseEntity<NotificationUserDTO> getNotificationUser(@PathVariable Long id) {
//        log.debug("REST request to get NotificationUser : {}", id);
//        Optional<NotificationUserDTO> notificationUserDTO = notificationUserService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(notificationUserDTO);
//    }
//
//    /**
//     * {@code DELETE  /notification-users/:id} : delete the "id" notificationUser.
//     *
//     * @param id the id of the notificationUserDTO to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/notification-users/{id}")
//    public ResponseEntity<Void> deleteNotificationUser(@PathVariable Long id) {
//        log.debug("REST request to delete NotificationUser : {}", id);
//        Optional<NotificationUserDTO> dto = notificationUserService.findOne(id);
//        if (!dto.isPresent() || dto.get().getIsDeleted() == 1L) {
//            throw new BadRequestAlertException(Translator.toLocale(ISNOT_EXISTED), ENTITY_NAME, ISNOT_EXISTED);
//        }
//        notificationUserService.delete(id);
//        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//    }
}
