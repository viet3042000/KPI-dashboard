package com.b4t.app.service.dto;

public class PushNotificationRequest {

    private String title;
    private String message;
    private String topic;
    private String token;
    private String imagePath;

    public PushNotificationRequest() {
    }

    public PushNotificationRequest(String title, String messageBody, String topicName, String imagePath) {
        this.title = title;
        this.message = messageBody;
        this.topic = topicName;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
