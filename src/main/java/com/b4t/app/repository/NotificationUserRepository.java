package com.b4t.app.repository;

import com.b4t.app.domain.NotificationUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the NotificationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {

    List<NotificationUser> findByNotifyId(Long notifyId);

    List<NotificationUser> findByIdIn(List<Long> ids);

}
