package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.repository.NotificationRepository;
import com.b4t.app.service.CatItemService;
import com.b4t.app.service.FirebaseService;
import com.b4t.app.service.TokenDeviceUserService;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.dto.NotificationDTO;
import com.b4t.app.service.dto.NotificationUserDTO;
import com.b4t.app.service.dto.TokenDeviceUserDTO;
import com.b4t.app.service.mapper.NotificationMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    private final Logger logger = LoggerFactory.getLogger(FirebaseServiceImpl.class);

    private final Environment env;

    private final TokenDeviceUserService tokenDeviceUserService;

    private final CatItemService catItemService;

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public FirebaseServiceImpl(
        Environment env,
        TokenDeviceUserService tokenDeviceUserService,
        CatItemService catItemService,
        NotificationRepository notificationRepository,
        NotificationMapper notificationMapper) {
        this.env = env;
        this.tokenDeviceUserService = tokenDeviceUserService;
        this.catItemService = catItemService;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void initialize() throws Exception {
        String firebaseConfigPath = env.getProperty("firebase.adminsdk-file");
        ClassPathResource file = new ClassPathResource(firebaseConfigPath);
        if (!file.exists())
            throw new Exception(Translator.toLocale("error.firebase.missconfig"));
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(file.getInputStream()))
            .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Override
    public void sendMessage(NotificationDTO notification) {
        if (notification.getSendToAll() != null && notification.getSendToAll()) {
            sendToAll(notification);
            return;
        }
        if (DataUtil.isNullOrEmpty(notification.getNotificationUsers())) {
            throw new BadRequestAlertException(Translator.toLocale("error.receivernull"), "notification", "error.receivernull");
        }
        sendMessageToTokens(notification);
    }

    private void sendToAll(NotificationDTO notification) {
        AndroidConfig androidConfig = getAndroidConfig();
        ApnsConfig apnsConfig = getApnsConfig();
        String topic = Constants.FIREBASE_TOPIC_DEFAULT;
        Optional<CatItemDTO> catItem = catItemService.findByCode(Constants.TOPIC_DEFAULT_FIREBASE_CATITEM);
        if (catItem.isPresent())
            topic = catItem.get().getItemValue();
        Message.Builder messageBuilder = Message.builder()
            .setApnsConfig(apnsConfig)
            .setAndroidConfig(androidConfig)
            .setTopic(topic)
            .setNotification(getPreConfigNotificationBuilder(notification).build());
        if (StringUtils.isNotEmpty(notification.getJsonData())) {
            messageBuilder = messageBuilder.putData("jsonData", notification.getJsonData());
        }
        if (notification.getScreenId() != null) {
            messageBuilder = messageBuilder.putData("screenId", notification.getScreenId().toString());
        }
        try {
            String response = FirebaseMessaging.getInstance().sendAsync(messageBuilder.build()).get();
            logger.info("Sent message:. " + response);
            notification.setIsSent(1L);
            notificationRepository.save(notificationMapper.toEntity(notification));
        } catch (InterruptedException |ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sendMessageToTokens(NotificationDTO notification) {
        Long[] receiverIds = notification.getNotificationUsers().stream()
            .map(NotificationUserDTO::getUserIdRecieved).toArray(Long[]::new);

        Page<TokenDeviceUserDTO> tokenDeviceUsers = tokenDeviceUserService.findAll(null, receiverIds, null, PageRequest.of(0, Integer.MAX_VALUE));
        if (DataUtil.isNullOrEmpty(tokenDeviceUsers.getContent())) return;
        List<String> tokens = tokenDeviceUsers.stream()
            .map(TokenDeviceUserDTO::getTokenDevice)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        if (DataUtil.isNullOrEmpty(tokens)) return;

        AndroidConfig androidConfig = getAndroidConfig();
        ApnsConfig apnsConfig = getApnsConfig();
        MulticastMessage.Builder messageBuilder = MulticastMessage.builder()
            .setApnsConfig(apnsConfig)
            .setAndroidConfig(androidConfig)
            .setNotification(getPreConfigNotificationBuilder(notification).build());
        if (StringUtils.isNotEmpty(notification.getJsonData())) {
            messageBuilder = messageBuilder.putData("jsonData", notification.getJsonData());
        }
        if (notification.getScreenId() != null) {
            messageBuilder = messageBuilder.putData("screenId", notification.getScreenId().toString());
        }
        messageBuilder = messageBuilder.addAllTokens(tokens);
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticastAsync(messageBuilder.build()).get();
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(tokens.get(i));
                    }
                }
                logger.error("List of tokens that caused failures: " + failedTokens);
            } else {
                notification.setIsSent(1L);
                notificationRepository.save(notificationMapper.toEntity(notification));
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private ApnsConfig getApnsConfig() {
        return ApnsConfig.builder().setAps(Aps.builder().build()).build();
    }

    private AndroidConfig getAndroidConfig() {
        return AndroidConfig.builder()
            .setTtl(Duration.ofMinutes(2).toMillis())
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(AndroidNotification.builder().setSound("default")
                .setColor("#FFFF00").build()).build();
    }

    private Notification.Builder getPreConfigNotificationBuilder(NotificationDTO notification) {
        Notification.Builder builder = Notification.builder();
        if (StringUtils.isNotEmpty(notification.getTitle())) {
            builder = builder.setTitle(notification.getTitle());
        }
        if (StringUtils.isNotEmpty(notification.getContent())) {
            builder = builder.setBody(notification.getContent());
        }
        if (StringUtils.isNotEmpty(notification.getImagePath())) {
            builder = builder.setImage(DataUtil.getExternalUrl(env) + notification.getImagePath());
        }
        return builder;
    }

}
