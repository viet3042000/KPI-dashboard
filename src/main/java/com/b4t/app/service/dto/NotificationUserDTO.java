package com.b4t.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.NotificationUser} entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationUserDTO implements Serializable {

    private Long id;

    private Long notifyId;

    private Long userIdRecieved;

    private Long userIdSent;

    private Long isNew;

    private Long isRead;

    private Long isDeleted;

    private Instant updateTime;

    private String updateUser;

    public Long getUserIdSent() {
        return userIdSent;
    }

    public void setUserIdSent(Long userIdSent) {
        this.userIdSent = userIdSent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Long notifyId) {
        this.notifyId = notifyId;
    }

    public Long getUserIdRecieved() {
        return userIdRecieved;
    }

    public void setUserIdRecieved(Long userIdRecieved) {
        this.userIdRecieved = userIdRecieved;
    }

    public Long getIsNew() {
        return isNew;
    }

    public void setIsNew(Long isNew) {
        this.isNew = isNew;
    }

    public Long getIsRead() {
        return isRead;
    }

    public void setIsRead(Long isRead) {
        this.isRead = isRead;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationUserDTO notificationUserDTO = (NotificationUserDTO) o;
        if (notificationUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationUserDTO{" +
            "id=" + getId() +
            ", notifyId=" + getNotifyId() +
            ", userIdRecieved=" + getUserIdRecieved() +
            ", isNew=" + getIsNew() +
            ", isRead=" + getIsRead() +
            ", isDeleted=" + getIsDeleted() +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateUser='" + getUpdateUser() + "'" +
            "}";
    }
}
