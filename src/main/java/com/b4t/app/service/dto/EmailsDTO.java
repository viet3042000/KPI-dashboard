package com.b4t.app.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.Emails} entity.
 */
public class EmailsDTO implements Serializable {

    private Long id;

    private String subject;

    private String email;

    private String content;

    private Long status;

    private Instant createTime;

    private Instant sentTime;

    private Long userId;

    private Long isRepeat;

    private String source;

    private Long notifyId;

    private List<Long> userIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getSentTime() {
        return sentTime;
    }

    public void setSentTime(Instant sentTime) {
        this.sentTime = sentTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Long isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Long notifyId) {
        this.notifyId = notifyId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailsDTO emailsDTO = (EmailsDTO) o;
        if (emailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailsDTO{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", email='" + getEmail() + "'" +
            ", content='" + getContent() + "'" +
            ", status=" + getStatus() +
            ", createTime='" + getCreateTime() + "'" +
            ", sendTime='" + getSentTime() + "'" +
            ", userId=" + getUserId() +
            ", isRepeat=" + getIsRepeat() +
            ", source='" + getSource() + "'" +
            ", notifyId=" + getNotifyId() +
            "}";
    }
}
