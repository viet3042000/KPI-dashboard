package com.b4t.app.service.dto;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.Notification} entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    private String title;

    private Long userIdSent;

    private String content;

    private String imagePath;

    private String fileAttachPath;

    private String fileAttachName;

    private Long screenId;

    private Boolean sendToAll;

    private Instant createDate;

    private String jsonData;

    private Long isSystemNotify;

    private Long isSent;

    private Instant updateTime;

    private String updateUser;

    public NotificationDTO() {
        notificationUsers = new ArrayList<>();
        isSystemNotify = 0L;
        createDate = Instant.now();
        title = StringUtils.EMPTY;
        content = StringUtils.EMPTY;
    }

    private List<NotificationUserDTO> notificationUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserIdSent() {
        return userIdSent;
    }

    public void setUserIdSent(Long userIdSent) {
        this.userIdSent = userIdSent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFileAttachPath() {
        return fileAttachPath;
    }

    public void setFileAttachPath(String fileAttachPath) {
        this.fileAttachPath = fileAttachPath;
    }

    public String getFileAttachName() {
        return fileAttachName;
    }

    public void setFileAttachName(String fileAttachName) {
        this.fileAttachName = fileAttachName;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Boolean getSendToAll() {
        return sendToAll;
    }

    public void setSendToAll(Boolean sendToAll) {
        this.sendToAll = sendToAll;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Long getIsSystemNotify() {
        return isSystemNotify;
    }

    public void setIsSystemNotify(Long isSystemNotify) {
        this.isSystemNotify = isSystemNotify;
    }

    public Long getIsSent() {
        return isSent;
    }

    public void setIsSent(Long isSent) {
        this.isSent = isSent;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public List<NotificationUserDTO> getNotificationUsers() {
        return notificationUsers;
    }

    public void setNotificationUsers(List<NotificationUserDTO> notificationUsers) {
        this.notificationUsers = notificationUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", userIdSent=" + getUserIdSent() +
            ", content='" + getContent() + "'" +
            ", fileAttachPath='" + getFileAttachPath() + "'" +
            ", fileAttachName='" + getFileAttachName() + "'" +
            ", screenId=" + getScreenId() +
            ", createDate='" + getCreateDate() + "'" +
            ", jsonData='" + getJsonData() + "'" +
            ", isSystemNotify=" + getIsSystemNotify() +
            ", isSent=" + getIsSent() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
