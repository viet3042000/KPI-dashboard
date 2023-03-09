package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.Notification;
import com.b4t.app.domain.NotificationUser_;
import com.b4t.app.domain.User;
import com.b4t.app.repository.NotificationRepository;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.NotificationUserService;
import com.b4t.app.domain.NotificationUser;
import com.b4t.app.repository.NotificationUserRepository;
import com.b4t.app.service.dto.NotificationUserDTO;
import com.b4t.app.service.mapper.NotificationUserMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link NotificationUser}.
 */
@Service
@Transactional
public class NotificationUserServiceImpl implements NotificationUserService {

    private final Logger log = LoggerFactory.getLogger(NotificationUserServiceImpl.class);

    private final NotificationUserRepository notificationUserRepository;

    private final NotificationUserMapper notificationUserMapper;

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private final EntityManager entityManager;


    public NotificationUserServiceImpl(
        NotificationUserRepository notificationUserRepository,
        NotificationUserMapper notificationUserMapper,
        NotificationRepository notificationRepository, UserRepository userRepository, EntityManager entityManager) {
        this.notificationUserRepository = notificationUserRepository;
        this.notificationUserMapper = notificationUserMapper;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    /**
     * Save a notificationUser.
     *
     * @param notificationUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public NotificationUserDTO save(NotificationUserDTO notificationUserDTO) {
        log.debug("Request to save NotificationUser : {}", notificationUserDTO);
        NotificationUser notificationUser = notificationUserMapper.toEntity(notificationUserDTO);
        notificationUser.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        notificationUser.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        notificationUser = notificationUserRepository.save(notificationUser);
        return notificationUserMapper.toDto(notificationUser);
    }

    @Override
    public List<NotificationUserDTO> saveAll(List<NotificationUserDTO> dtos) {
        if (DataUtil.isNullOrEmpty(dtos)) return new ArrayList<>();
        List<Long> lstNotification = dtos.stream().map(NotificationUserDTO::getNotifyId).distinct().collect(Collectors.toList());
        List<Notification> lstCheck = notificationRepository.findAllByIdIn(lstNotification);
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        if(DataUtil.isNullOrEmpty(lstCheck) || !user.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("notification.notPermissionUpdate"), "notifications", "notification.notPermissionUpdate");
        }

        log.debug("Request to save NotificationUser : {}", dtos);
        List<NotificationUser> entities = notificationUserMapper.toEntity(dtos);
        entities = entities.stream().peek(i -> {
            i.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            i.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        }).collect(Collectors.toList());
        entities = notificationUserRepository.saveAll(entities);
        return notificationUserMapper.toDto(entities);
    }

    @Override
    public List<NotificationUserDTO> deleteAll(List<NotificationUserDTO> lstNotificationUserDelete) {
        if (DataUtil.isNullOrEmpty(lstNotificationUserDelete)) return new ArrayList<>();
        List<Long> lstNotification = lstNotificationUserDelete.stream().map(NotificationUserDTO::getNotifyId).distinct().collect(Collectors.toList());
        List<Notification> lstCheck = notificationRepository.findAllByIdIn(lstNotification);
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        if(DataUtil.isNullOrEmpty(lstCheck) || !user.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("notification.notPermissionUpdate"), "notifications", "notification.notPermissionUpdate");
        }
        lstCheck.stream().forEach(res -> {
            if(res.getUserIdSent() == null || !res.getUserIdSent().equals(user.get().getId())) {
                throw new BadRequestAlertException(Translator.toLocale("notification.notPermissionUpdate"), "notifications", "notification.notPermissionUpdate");
            }
        });
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        List<NotificationUser> lstDel = lstNotificationUserDelete.stream().map(notificationUserMapper::toEntity).peek(res -> {
            res.setUpdateTime(now);
            res.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            res.setIsDeleted(1L);
        }).collect(Collectors.toList());

        lstDel = notificationUserRepository.saveAll(lstDel);
        return notificationUserMapper.toDto(lstDel);
    }

    @Override
    public List<NotificationUserDTO> readAll(List<NotificationUserDTO> dtoList) {
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);

        List<Long> lstNotification = dtoList.stream().map(NotificationUserDTO::getNotifyId).distinct().collect(Collectors.toList());
        Map<Long, Long> mapNotifyUserSend = notificationRepository.findAllByIdIn(lstNotification).stream().collect(Collectors.toMap(Notification::getId, Notification::getUserIdSent, (o1, o2) -> o1));
        dtoList.stream().forEach(e -> {
            if(mapNotifyUserSend != null) {
                e.setUserIdSent(mapNotifyUserSend.get(e.getNotifyId()));
            }
            if(e.getUserIdSent() == null ||
                (!e.getUserIdRecieved().equals(user.get().getId()) && !e.getUserIdSent().equals(user.get().getId()))) {
                throw new BadRequestAlertException(Translator.toLocale("notification.notPermissionUpdate"), "notifications", "notification.notPermissionUpdate");
            }
        });
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        List<NotificationUser> lstView = dtoList.stream().map(notificationUserMapper::toEntity).peek(e -> {
            e.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            e.setUpdateTime(now);
            e.setIsRead(1L);
        }).collect(Collectors.toList());
        lstView = notificationUserRepository.saveAll(lstView);
        return notificationUserMapper.toDto(lstView);
    }

    /**
     * Get all the notificationUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationUserDTO> findAll(Long[] notifyIds, Long[] userIdRecieved, Long isNew, Long isRead, Long isDeleted, Pageable pageable) {
        log.debug("Request to get all NotificationUsers");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotificationUser> criteria = cb.createQuery(NotificationUser.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<NotificationUser> root = criteria.from(NotificationUser.class);
        Root<NotificationUser> rootCount = countQuery.from(NotificationUser.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!DataUtil.isNullOrEmpty(notifyIds)) {
            predicates.add(root.get(NotificationUser_.NOTIFY_ID).in(notifyIds));
        }
        if (!DataUtil.isNullOrEmpty(userIdRecieved)) {
            predicates.add(root.get(NotificationUser_.USER_ID_RECIEVED).in(userIdRecieved));
        }
        if (isRead != null) {
            predicates.add(cb.equal(root.get(NotificationUser_.IS_READ), isRead));
        }
        if (isNew != null) {
            predicates.add(cb.equal(root.get(NotificationUser_.IS_NEW), isNew));
        }
        if (isDeleted != null) {
            predicates.add(cb.equal(root.get(NotificationUser_.IS_DELETED), isDeleted));
        }

        criteria.select(root);
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        List<NotificationUser> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Long cnt = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(notificationUserMapper.toDto(rs), pageable, cnt);
    }

    @Override
    public List<NotificationUserDTO> findByNotifyId(Long notifyId) {
        return notificationUserMapper.toDto(notificationUserRepository.findByNotifyId(notifyId));
    }

    @Override
    public List<NotificationUserDTO> findByIds(List<Long> ids) {
        return notificationUserMapper.toDto(notificationUserRepository.findByIdIn(ids));
    }

    /**
     * Get one notificationUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationUserDTO> findOne(Long id) {
        log.debug("Request to get NotificationUser : {}", id);
        return notificationUserRepository.findById(id)
            .map(notificationUserMapper::toDto);
    }

    /**
     * Delete the notificationUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NotificationUser : {}", id);
        Optional<NotificationUserDTO> dtoOpt = findOne(id);
        if (!dtoOpt.isPresent()) return;
        NotificationUserDTO dto = dtoOpt.get();
        dto.setIsDeleted(1L);
        save(dto);
    }
}
