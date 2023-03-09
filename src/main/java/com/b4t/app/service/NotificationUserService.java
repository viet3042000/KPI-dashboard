package com.b4t.app.service;

import com.b4t.app.service.dto.NotificationUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.b4t.app.domain.NotificationUser}.
 */
public interface NotificationUserService {

    /**
     * Save a notificationUser.
     *
     * @param notificationUserDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationUserDTO save(NotificationUserDTO notificationUserDTO);

    List<NotificationUserDTO> saveAll(List<NotificationUserDTO> dtos);

    List<NotificationUserDTO> deleteAll(List<NotificationUserDTO> dtos);

    List<NotificationUserDTO> readAll(List<NotificationUserDTO> dtoList);

    /**
     * Get all the notificationUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificationUserDTO> findAll(Long[] notifyIds, Long[] userIdRecieved, Long isNew, Long isRead, Long isDeleted, Pageable pageable);

    List<NotificationUserDTO> findByNotifyId(Long notifyId);

    List<NotificationUserDTO> findByIds(List<Long> ids);
    /**
     * Get the "id" notificationUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationUserDTO> findOne(Long id);

    /**
     * Delete the "id" notificationUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
