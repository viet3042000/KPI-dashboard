package com.b4t.app.service.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.Notification} entity.
 */
public class SaveNotificationDTO extends NotificationDTO implements Serializable {

    public SaveNotificationDTO() {
        super();
    }

    public SaveNotificationDTO(NotificationDTO dto) {
        setId(dto.getId());
        setNotificationUsers(dto.getNotificationUsers());
        setContent(dto.getContent());
        setCreateDate(dto.getCreateDate());
        setFileAttachName(dto.getFileAttachName());
        setFileAttachPath(dto.getFileAttachPath());
        setImagePath(dto.getImagePath());
        setIsSent(dto.getIsSent());
        setIsSystemNotify(dto.getIsSystemNotify());
        setJsonData(dto.getJsonData());
        setScreenId(dto.getScreenId());
        setSendToAll(dto.getSendToAll());
        setTitle(dto.getTitle());
        setUpdateTime(dto.getUpdateTime());
        setUpdateUser(dto.getUpdateUser());
        setUserIdSent(dto.getUserIdSent());
    }

    public NotificationDTO toDto() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(getId());
        dto.setNotificationUsers(getNotificationUsers());
        dto.setContent(StringUtils.isNotEmpty(getContent()) ? getContent().trim() : getContent());
        dto.setCreateDate(getCreateDate());
        dto.setFileAttachName(getFileAttachName());
        dto.setFileAttachPath(getFileAttachPath());
        dto.setImagePath(getImagePath());
        dto.setIsSent(getIsSent());
        dto.setIsSystemNotify(getIsSystemNotify());
        dto.setJsonData(getJsonData());
        dto.setScreenId(getScreenId());
        dto.setTitle(StringUtils.isNotEmpty(getTitle()) ? getTitle().trim() : getTitle());
        dto.setSendToAll(getSendToAll());
        dto.setUpdateTime(getUpdateTime());
        dto.setUpdateUser(getUpdateUser());
        dto.setUserIdSent(getUserIdSent());
        return dto;
    }

    private MultipartFile image;

    private MultipartFile attachment;

    private List<Long> receivedUserIds;

    private Long isSendEmail;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

    public List<Long> getReceivedUserIds() {
        return receivedUserIds;
    }

    public void setReceivedUserIds(List<Long> receivedUserIds) {
        this.receivedUserIds = receivedUserIds;
    }

    public Long getIsSendEmail() {
        return isSendEmail;
    }

    public void setIsSendEmail(Long isSendEmail) {
        this.isSendEmail = isSendEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SaveNotificationDTO notificationDTO = (SaveNotificationDTO) o;
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
