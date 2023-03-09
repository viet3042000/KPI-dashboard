package com.b4t.app.repository;

import com.b4t.app.domain.NotificationOfUser;
import com.b4t.app.domain.UserCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCustomRepository {
    List<UserCustom> getListUserChat(Long userLoginId);

    List<UserCustom> getTotalNoticeOfUser(Long userLoginId);

    List<UserCustom> getUserRecivedAlarm(Long chartId);

    Long countNumNoticeOfUser(Long userLoginId, Long userIdConversation, String keySearch);

    List<NotificationOfUser> getNoticeOfClient(Long userLoginId, Long userIdConversation, String keySearch, Long offSet, int pageSize);

    Page<UserCustom> getUserWithNotify(String keyword, Long userId, Pageable pageable);
}
