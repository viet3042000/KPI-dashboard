package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "user_id_sent")
    private Long userIdSent;

    @Column(name = "content")
    private String content;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "file_attach_path")
    private String fileAttachPath;

    @Column(name = "file_attach_name")
    private String fileAttachName;

    @Column(name = "screen_id")
    private Long screenId;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "json_data")
    private String jsonData;

    @Column(name = "is_system_notify")
    private Long isSystemNotify;

    @Column(name = "is_sent")
    private Long isSent;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "update_user")
    private String updateUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Notification title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserIdSent() {
        return userIdSent;
    }

    public Notification userIdSent(Long userIdSent) {
        this.userIdSent = userIdSent;
        return this;
    }

    public void setUserIdSent(Long userIdSent) {
        this.userIdSent = userIdSent;
    }

    public String getContent() {
        return content;
    }

    public Notification content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileAttachPath() {
        return fileAttachPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Notification fileAttachPath(String fileAttachPath) {
        this.fileAttachPath = fileAttachPath;
        return this;
    }

    public void setFileAttachPath(String fileAttachPath) {
        this.fileAttachPath = fileAttachPath;
    }

    public String getFileAttachName() {
        return fileAttachName;
    }

    public Notification fileAttachName(String fileAttachName) {
        this.fileAttachName = fileAttachName;
        return this;
    }

    public void setFileAttachName(String fileAttachName) {
        this.fileAttachName = fileAttachName;
    }

    public Long getScreenId() {
        return screenId;
    }

    public Notification screenId(Long screenId) {
        this.screenId = screenId;
        return this;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Notification createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getJsonData() {
        return jsonData;
    }

    public Notification jsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Long getIsSystemNotify() {
        return isSystemNotify;
    }

    public Notification isSystemNotify(Long isSystemNotify) {
        this.isSystemNotify = isSystemNotify;
        return this;
    }

    public void setIsSystemNotify(Long isSystemNotify) {
        this.isSystemNotify = isSystemNotify;
    }

    public Long getIsSent() {
        return isSent;
    }

    public Notification isSent(Long isSent) {
        this.isSent = isSent;
        return this;
    }

    public void setIsSent(Long isSent) {
        this.isSent = isSent;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public Notification updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public Notification updateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Notification{" +
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
