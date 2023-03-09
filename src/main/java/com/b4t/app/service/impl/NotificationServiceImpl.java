package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.FtpUtils;
import com.b4t.app.commons.StringUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.repository.NotificationRepository;
import com.b4t.app.service.dto.NotificationDTO;
import com.b4t.app.service.dto.NotificationUserDTO;
import com.b4t.app.service.dto.SaveNotificationDTO;
import com.b4t.app.service.dto.UserDTO;
import com.b4t.app.service.mapper.NotificationMapper;
import com.b4t.app.service.mapper.NotificationUserMapper;
//import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final NotificationUserMapper notificationUserMapper;

    private final NotificationUserService notificationUserService;

    private final EntityManager entityManager;

    private final FirebaseService firebaseService;

    private final HttpServletRequest request;

    private final Environment env;

    private final MailService mailService;

    private final UserService userService;

    private final FtpUtils fptUtils;


    public NotificationServiceImpl(
        NotificationRepository notificationRepository,
        NotificationMapper notificationMapper,
        NotificationUserMapper notificationUserMapper,
        NotificationUserService notificationUserService,
        EntityManager entityManager,
        FirebaseService firebaseService,
        HttpServletRequest request,
        Environment env,
        MailService mailService,
        UserService userService, FtpUtils fptUtils) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationUserMapper = notificationUserMapper;
        this.notificationUserService = notificationUserService;
        this.entityManager = entityManager;
        this.firebaseService = firebaseService;
        this.request = request;
        this.env = env;
        this.mailService = mailService;
        this.userService = userService;
        this.fptUtils = fptUtils;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);

        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        notification.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    public NotificationDTO create(SaveNotificationDTO saveDto) throws Exception {
        log.debug("Request to save Notification : {}", saveDto);
        NotificationDTO notificationDTO = saveDto.toDto();
        notificationDTO.setId(null);
        UUID uuid;
        if (saveDto.getImage() != null) {
            uuid = UUID.randomUUID();
            String fileName = StringUtils.getSafeFileName(saveDto.getImage().getOriginalFilename());
            String filePath = String.format("image/%s", uuid);
            saveFile(fileName, filePath, saveDto.getImage());
            notificationDTO.setImagePath(filePath+"/"+ fileName);
        }
        if (saveDto.getAttachment() != null) {
            uuid = UUID.randomUUID();
            String fileName = StringUtils.getSafeFileName(saveDto.getAttachment().getOriginalFilename());
            String filePath = String.format("attachment/%s", uuid);
            saveFile(fileName, filePath, saveDto.getAttachment());
            notificationDTO.setFileAttachPath(filePath+"/"+fileName);
            notificationDTO.setFileAttachName(fileName);
        }

        notificationDTO.setCreateDate(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        notificationDTO.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        notificationDTO.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        notificationDTO = notificationMapper.toDto(notification);

        // update status new
        List<NotificationDTO> notifications = findAll(null, saveDto.getUserIdSent(),
            saveDto.getReceivedUserIds().toArray(new Long[saveDto.getReceivedUserIds().size()]),
            1L, null, null, PageRequest.of(0, Integer.MAX_VALUE))
            .getContent();
        List<NotificationUserDTO> needUpdateNotificationUsers = notifications.stream()
            .map(NotificationDTO::getNotificationUsers)
            .flatMap(List::stream)
            .peek(nu -> nu.setIsNew(0L))
            .collect(Collectors.toList());
        notificationUserService.saveAll(needUpdateNotificationUsers);

        if (!DataUtil.isNullOrEmpty(saveDto.getReceivedUserIds())) {
            Notification finalNotification = notification;
            List<NotificationUserDTO> notificationUsers = saveDto.getReceivedUserIds().stream().map(i -> {
                NotificationUserDTO dto = new NotificationUserDTO();
                dto.setIsNew(1L);
                dto.setIsRead(0L);
                dto.setNotifyId(finalNotification.getId());
                dto.setUserIdRecieved(i);
                dto.setIsDeleted(0L);
                return dto;
            }).collect(Collectors.toList());
            notificationDTO.setNotificationUsers(notificationUserService.saveAll(notificationUsers));
        }

//        if (Constants.STATUS_ACTIVE.equals(saveDto.getIsSendEmail())) {
//            sendMail(notificationDTO);
//        }
        return notificationDTO;
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(String keyword, Long sentUserId, Long[] receivedUserIds, Long isNew, Long isRead, Long isDeleted, Pageable pageable) {
        log.debug("Request to get all Notifications");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = cb.createQuery();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<Notification> notifyRoot = criteria.from(Notification.class);
        Root<Notification> notifyRootCount = countQuery.from(Notification.class);
        Root<NotificationUser> notifyUserRoot = criteria.from(NotificationUser.class);
        Root<NotificationUser> notifyUserRootCount = countQuery.from(NotificationUser.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        predicates.add(cb.equal(notifyUserRoot.get(NotificationUser_.NOTIFY_ID), notifyRoot.get(Notification_.ID)));
        countPredicates.add(cb.equal(notifyUserRootCount.get(NotificationUser_.NOTIFY_ID), notifyRoot.get(Notification_.ID)));

        if (!DataUtil.isNullOrEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            predicates.add(cb.or(
                cb.like(cb.lower(notifyRoot.get(Notification_.CONTENT)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            ));
            countPredicates.add(cb.or(
                cb.like(cb.lower(notifyRootCount.get(Notification_.CONTENT)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            ));
        }

        if (sentUserId != null || !DataUtil.isNullOrEmpty(receivedUserIds)) {
            predicates.addAll(createPredicateSendReceiveId(cb, notifyRoot, notifyRootCount, notifyUserRoot, notifyUserRootCount, sentUserId, receivedUserIds, false));
            countPredicates.addAll(createPredicateSendReceiveId(cb, notifyRoot, notifyRootCount, notifyUserRoot, notifyUserRootCount, sentUserId, receivedUserIds, true));
        }

        if (isNew != null) {
            predicates.add(cb.equal(notifyUserRoot.get(NotificationUser_.IS_NEW), isNew));
            countPredicates.add(cb.equal(notifyUserRootCount.get(NotificationUser_.IS_NEW), isNew));
        }

        if (isRead != null) {
            predicates.add(cb.equal(notifyUserRoot.get(NotificationUser_.IS_READ), isRead));
            countPredicates.add(cb.equal(notifyUserRootCount.get(NotificationUser_.IS_READ), isRead));
        }

        if (isDeleted != null) {
            predicates.add(cb.equal(notifyUserRoot.get(NotificationUser_.IS_DELETED), isDeleted));
            countPredicates.add(cb.equal(notifyUserRootCount.get(NotificationUser_.IS_DELETED), isDeleted));
        }

        criteria.select(notifyRoot).distinct(true);
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), notifyRoot, cb));
        List rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        countQuery.select(cb.count(notifyRootCount));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[countPredicates.size()])));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        List<NotificationDTO> rsDTOs = processResult(rs, sentUserId, receivedUserIds, isNew, isRead, isDeleted);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id)
            .map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        Optional<NotificationDTO> dtoOpt = findOne(id);
        if (!dtoOpt.isPresent()) return;
        List<NotificationUserDTO> nus = notificationUserService.findByNotifyId(id);
        nus = nus.stream().peek(i -> i.setIsDeleted(1L)).collect(Collectors.toList());
        notificationUserService.saveAll(nus);
    }

    private void saveFile(String fileName, String filePath, MultipartFile multipartFile) throws IOException {
//        String rootPath = request.getServletContext().getRealPath("/");
//        if (!DataUtil.isNullOrEmpty(env.getProperty("filesystem.rootpath"))) {
//            rootPath = env.getProperty("filesystem.rootpath");
//        }
//        if (!rootPath.endsWith("/") && !filePath.startsWith("/")) rootPath += "/";
//        File file = new File(rootPath + filePath);
//        File folder = file.getParentFile();
//        if (!folder.exists())
//            folder.mkdirs();
//        FileCopyUtils.copy(multipartFile.getBytes(), file);
        try {
            fptUtils.uploadFile(fileName, filePath, multipartFile.getInputStream());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

    }

    private void sendMail(NotificationDTO dto) {
        List<Long> userIds = dto.getNotificationUsers().stream()
            .map(NotificationUserDTO::getUserIdRecieved).collect(Collectors.toList());
        if (DataUtil.isNullOrEmpty(userIds) && (dto.getSendToAll() == null || !dto.getSendToAll())) return;
        List<UserDTO> users;
        if (dto.getSendToAll() != null && dto.getSendToAll()) {
            users = userService.getAllManagedUsers(null, true, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        } else {
            userIds.add(dto.getUserIdSent());
            users = userService.getByIds(userIds);
        }
        if (DataUtil.isNullOrEmpty(users)) return;
        String[] emails = users.stream().filter(i -> !dto.getUserIdSent().equals(i.getId()))
            .map(UserDTO::getEmail).filter(Objects::nonNull).toArray(String[]::new);
        if (DataUtil.isNullOrEmpty(emails)) return;

        Optional<UserDTO> senderOpt = users.stream().filter(i -> i.getId().equals(dto.getUserIdSent())).findFirst();
        String subject = Translator.toLocale("email.notification.title");
        if (senderOpt.isPresent())
            subject = String.format("%s %s(%s)", Translator.toLocale("email.notification.title"), senderOpt.get().getFirstName(), senderOpt.get().getLogin());

        File attachment = null;
        String filename = StringUtils.getSafeFileName(dto.getFileAttachName());
        if (!DataUtil.isNullOrEmpty(dto.getFileAttachPath())) {
            attachment = new File(request.getServletContext().getRealPath(StringUtils.getSafePath(dto.getFileAttachPath())));
            if (!attachment.exists()) attachment = null;
        }

        mailService.sendEmail(emails, subject, dto.getContent(), true, true, filename, attachment);
    }

    private List<NotificationDTO> processResult(List<Notification> rs, Long sentUserId, Long[] receivedUserIds,Long isNew, Long isRead, Long isDeleted) {
        List<NotificationDTO> rsDTOs = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(rs)) return rsDTOs;
        Long[] notifyIds = rs.stream().map(Notification::getId).toArray(Long[]::new);

        List<Long> receivedUserIdsLst = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(receivedUserIds))
            receivedUserIdsLst.addAll(Arrays.asList(receivedUserIds));
        if (sentUserId != null)
            receivedUserIdsLst.add(sentUserId);

        List<NotificationUserDTO> notifyUsers = notificationUserService.findAll(notifyIds, receivedUserIdsLst.toArray(new Long[receivedUserIdsLst.size()]),
            isNew, isRead, isDeleted, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        for (Notification notify : rs) {
            NotificationDTO notifyDto = notificationMapper.toDto(notify);
            List<NotificationUserDTO> notifyUserItems = notifyUsers.stream()
                .filter(i -> notify.getId().equals(i.getNotifyId()))
                .collect(Collectors.toList());
            notifyDto.setNotificationUsers(notifyUserItems);
            rsDTOs.add(notifyDto);
        }
        return rsDTOs;
    }

    private List<Predicate> createPredicateSendReceiveId(
        CriteriaBuilder cb, Root<Notification> notifyRoot, Root<Notification> notifyRootCount,
        Root<NotificationUser> notifyUserRoot, Root<NotificationUser> notifyUserRootCount,
        Long sentUserId, Long[] receivedUserIds, boolean countQuery) {
        List<Predicate> predicates = new ArrayList<>();

        if (sentUserId != null && !DataUtil.isNullOrEmpty(receivedUserIds)) {
            if (receivedUserIds.length == 1) {
                if (countQuery) {
                    predicates.add(cb.or(
                        cb.and(
                            cb.equal(notifyRootCount.get(Notification_.USER_ID_SENT), sentUserId),
                            cb.equal(notifyUserRootCount.get(NotificationUser_.USER_ID_RECIEVED), receivedUserIds[0])
                        ),
                        cb.and(
                            cb.equal(notifyRootCount.get(Notification_.USER_ID_SENT), receivedUserIds[0]),
                            cb.equal(notifyUserRootCount.get(NotificationUser_.USER_ID_RECIEVED), sentUserId)
                        )
                    ));
                } else {
                    predicates.add(cb.or(
                        cb.and(
                            cb.equal(notifyRoot.get(Notification_.USER_ID_SENT), sentUserId),
                            cb.equal(notifyUserRoot.get(NotificationUser_.USER_ID_RECIEVED), receivedUserIds[0])
                        ),
                        cb.and(
                            cb.equal(notifyRoot.get(Notification_.USER_ID_SENT), receivedUserIds[0]),
                            cb.equal(notifyUserRoot.get(NotificationUser_.USER_ID_RECIEVED), sentUserId)
                        )
                    ));
                }
            } else {
                if (countQuery) {
                    predicates.add(cb.or(
                        cb.and(
                            cb.equal(notifyRootCount.get(Notification_.USER_ID_SENT), sentUserId),
                            notifyUserRootCount.get(NotificationUser_.USER_ID_RECIEVED).in(receivedUserIds)
                        ),
                        cb.and(
                            notifyRootCount.get(Notification_.USER_ID_SENT).in(receivedUserIds),
                            cb.equal(notifyUserRootCount.get(NotificationUser_.USER_ID_RECIEVED), sentUserId)
                        )
                    ));
                } else {
                    predicates.add(cb.or(
                        cb.and(
                            cb.equal(notifyRoot.get(Notification_.USER_ID_SENT), sentUserId),
                            notifyUserRoot.get(NotificationUser_.USER_ID_RECIEVED).in(receivedUserIds)
                        ),
                        cb.and(
                            notifyRoot.get(Notification_.USER_ID_SENT).in(receivedUserIds),
                            cb.equal(notifyUserRoot.get(NotificationUser_.USER_ID_RECIEVED), sentUserId)
                        )
                    ));
                }
            }
        } else {
            if (sentUserId != null) {
                if (countQuery) {
                    predicates.add(cb.equal(notifyRootCount.get(Notification_.USER_ID_SENT), sentUserId));
                } else {
                    predicates.add(cb.equal(notifyRoot.get(Notification_.USER_ID_SENT), sentUserId));
                }
            }
            if (!DataUtil.isNullOrEmpty(receivedUserIds)) {
                if (countQuery) {
                    predicates.add(notifyUserRootCount.get(NotificationUser_.USER_ID_RECIEVED).in(receivedUserIds));
                } else {
                    predicates.add(notifyUserRoot.get(NotificationUser_.USER_ID_RECIEVED).in(receivedUserIds));
                }
            }
        }

        return predicates;
    }
}
