package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.domain.NotificationOfUser;
import com.b4t.app.domain.UserCustom;
import com.b4t.app.service.ChatService;
import com.b4t.app.service.dto.File;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatResource {
    private final Logger log = LoggerFactory.getLogger(ChatResource.class);

    private static final String ENTITY_NAME = "catItem";
    private static final String REQUIRE_ID = "require.userLoginId";
    private static final String REQUIRE_FILE_TYPE = "require.file.type";
    private static final String REQUIRE_FILE_PATH = "require.file.path";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatService chatService;

    public ChatResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/notification/list-user/{userLoginId}")
    public ResponseEntity<List<UserCustom>> getListUserChat(@PathVariable Long userLoginId) {
        log.debug("REST request to get a page of userLoginId");
        if (userLoginId == null) {
            throw new BadRequestAlertException(Translator.toLocale(REQUIRE_ID), ENTITY_NAME, REQUIRE_ID);
        }
        List<UserCustom> lstUser = chatService.getListUserChat(userLoginId);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(userLoginId));
        return ResponseEntity.ok().headers(headers).body(lstUser);
    }

    @GetMapping("/notification/count-notice-of-user")
    public ResponseEntity<List<UserCustom>> getTotalNoticeOfUser() {
        log.debug("REST request to get a page of userLoginId");

        List<UserCustom> lstUser = chatService.getTotalNoticeOfUser();
        return ResponseEntity.ok().body(lstUser);
    }

    @GetMapping("/notification/user-recived-alarm/{chartId}")
    public ResponseEntity<List<UserCustom>> getUserRecivedAlarm(@PathVariable Long chartId) {
        log.debug("REST request to get a page of chartId");
        if (chartId == null) {
            throw new BadRequestAlertException(Translator.toLocale("require.chartId"), ENTITY_NAME, "require.chartId");
        }
        List<UserCustom> lstUser = chatService.getUserRecivedAlarm(chartId);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(chartId));
        return ResponseEntity.ok().headers(headers).body(lstUser);
    }

    @GetMapping("/notification/get-notice-of-client")
    public ResponseEntity<Page<NotificationOfUser>> getNoticeOfClient(Long userLoginId, Long userIdConversation, String keySearch, Pageable pageable) {
        log.debug("REST request to get a page of chartId");
        if (userLoginId == null) {
            throw new BadRequestAlertException(Translator.toLocale(REQUIRE_ID), ENTITY_NAME, REQUIRE_ID);
        }
        if (userIdConversation == null) {
            throw new BadRequestAlertException(Translator.toLocale(REQUIRE_ID), ENTITY_NAME, "require.userIdConversation");
        }
        Page<NotificationOfUser> lstUser = chatService.getNoticeOfClient(userLoginId, userIdConversation, keySearch, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), lstUser);
        return ResponseEntity.ok().headers(headers).body(lstUser);
    }

    @GetMapping(
        value = "/notification/show-image"
        , produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] showImage(String path) throws Exception {
        return IOUtils.toByteArray(chatService.getFile(path));
    }

    @GetMapping("/notification/get-file")
    public ResponseEntity<File> getFile(String type, String path) throws Exception {
        log.debug("REST request to get a page of chartId {}", path);
        if (type == null) {
            throw new BadRequestAlertException(Translator.toLocale(REQUIRE_FILE_TYPE), ENTITY_NAME, REQUIRE_FILE_TYPE);
        }
        if (path == null) {
            throw new BadRequestAlertException(Translator.toLocale(REQUIRE_FILE_PATH), ENTITY_NAME, REQUIRE_FILE_PATH);
        }
        File file = chatService.getImageByPath(type, path);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, file.getFileName()))
            .body(file);
    }
}
