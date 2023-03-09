package com.b4t.app.service;

import com.b4t.app.domain.NotificationOfUser;
import com.b4t.app.domain.UserCustom;
import com.b4t.app.service.dto.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface ChatService {
    List<UserCustom> getListUserChat(Long userLoginId);

    List<UserCustom> getTotalNoticeOfUser();

    List<UserCustom> getUserRecivedAlarm(Long chartId);

    Page<NotificationOfUser> getNoticeOfClient(Long userLoginId, Long userIdConversation, String keySearch, Pageable pageable);

    InputStream getFile(String filePath) throws Exception;

    File getImageByPath(String type, String filePath) throws Exception;
}
