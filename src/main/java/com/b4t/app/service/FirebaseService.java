package com.b4t.app.service;


import com.b4t.app.service.dto.NotificationDTO;
import com.b4t.app.service.dto.PushNotificationRequest;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FirebaseService {
    void initialize() throws Exception;

    void sendMessage(NotificationDTO notification);

}
