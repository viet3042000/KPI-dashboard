package com.b4t.app.service.impl;

import com.b4t.app.commons.FtpUtils;
import com.b4t.app.commons.StringUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.NotificationOfUser;
import com.b4t.app.domain.User;
import com.b4t.app.domain.UserCustom;
import com.b4t.app.repository.UserCustomRepository;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.ChatService;
import com.b4t.app.service.dto.File;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
    private final Logger log = LoggerFactory.getLogger(CatItemServiceImpl.class);
    private final UserCustomRepository userCustomRepository;
    private final Environment env;
    private final FtpUtils fptUtils;
    private final UserRepository userRepository;

    public ChatServiceImpl(UserCustomRepository userCustomRepository, Environment env, FtpUtils fptUtils, UserRepository userRepository) {
        this.userCustomRepository = userCustomRepository;
        this.env = env;
        this.fptUtils = fptUtils;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserCustom> getListUserChat(Long userLoginId) {
        try {
            return userCustomRepository.getListUserChat(userLoginId);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public List<UserCustom> getTotalNoticeOfUser() {
        Optional<User> optUser = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        if(!optUser.isPresent()) {
            throw new BadRequestAlertException(Translator.toLocale("notification.notPermission"), "notifications", "notification.notPermission");
        }
        try {
            return userCustomRepository.getTotalNoticeOfUser(optUser.get().getId());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public List<UserCustom> getUserRecivedAlarm(Long chartId) {
        try {
            return userCustomRepository.getUserRecivedAlarm(chartId);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return new ArrayList<>();
    }

    @Override
    public Page<NotificationOfUser> getNoticeOfClient(Long userLoginId, Long userIdConversation, String keySearch, Pageable pageable) {
        try {
            List<NotificationOfUser> lstNotices = userCustomRepository.getNoticeOfClient(userLoginId, userIdConversation, keySearch, pageable.getOffset(), pageable.getPageSize());
            Long count = userCustomRepository.countNumNoticeOfUser(userLoginId, userIdConversation, keySearch);
            return new PageImpl<>(lstNotices, pageable, count);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public File getImageByPath(String type, String filePath) throws Exception {
        return encoder(filePath);
    }

    public InputStream getFile(String filePath) throws Exception {
        try {
            String safePath = StringUtils.getSafePath(filePath);
//            java.io.File file = new java.io.File(env.getProperty("filesystem.rootpath") + "/" + safePath);
//            InputStream targetStream = new FileInputStream(file);
//            return targetStream;
            String fileName = safePath.substring(safePath.lastIndexOf("/") + 1);
            String folder = safePath.substring(0, safePath.lastIndexOf("/"));
            return new ByteArrayInputStream(fptUtils.downloadFile(folder, fileName));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestAlertException(Translator.toLocale("notification.fileNotFound"), "notification", "notification.fileNotFound");
        }
    }

    private File encoder(String filePath) throws Exception {
        String safePath = StringUtils.getSafePath(filePath);
        File result = new File();
//        String base64File = "";
//        java.io.File file = new java.io.File(env.getProperty("filesystem.rootpath") + "/" + safePath);
//        if (!file.isFile()) {
//            throw new Exception(Translator.toLocale("validate.path"));
//        }
//        String safePath = StringUtils.getSafePath(filePath);
//            java.io.File file = new java.io.File(env.getProperty("filesystem.rootpath") + "/" + safePath);
//            InputStream targetStream = new FileInputStream(file);
//            return targetStream;
        String fileName = safePath.substring(safePath.lastIndexOf("/") + 1);
        String folder = safePath.substring(0, safePath.lastIndexOf("/"));

        String base64File = Base64.getEncoder().encodeToString(fptUtils.downloadFile(folder, fileName));

        result.setFileName(fileName);
        result.setFileContent(base64File);
        return result;
    }
}
