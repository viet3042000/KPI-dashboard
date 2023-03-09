package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A NotificationUser.
 */
@Entity
@Table(name = "notification_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NotificationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notify_id")
    private Long notifyId;

    @Column(name = "user_id_recieved")
    private Long userIdRecieved;

    @Column(name = "is_new")
    private Long isNew;

    @Column(name = "is_read")
    private Long isRead;

    @Column(name = "is_deleted")
    private Long isDeleted;

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

    public Long getNotifyId() {
        return notifyId;
    }

    public NotificationUser notifyId(Long notifyId) {
        this.notifyId = notifyId;
        return this;
    }

    public void setNotifyId(Long notifyId) {
        this.notifyId = notifyId;
    }

    public Long getUserIdRecieved() {
        return userIdRecieved;
    }

    public NotificationUser userIdRecieved(Long userIdRecieved) {
        this.userIdRecieved = userIdRecieved;
        return this;
    }

    public void setUserIdRecieved(Long userIdRecieved) {
        this.userIdRecieved = userIdRecieved;
    }

    public Long getIsNew() {
        return isNew;
    }

    public NotificationUser isNew(Long isNew) {
        this.isNew = isNew;
        return this;
    }

    public void setIsNew(Long isNew) {
        this.isNew = isNew;
    }

    public Long getIsRead() {
        return isRead;
    }

    public NotificationUser isRead(Long isRead) {
        this.isRead = isRead;
        return this;
    }

    public void setIsRead(Long isRead) {
        this.isRead = isRead;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public NotificationUser isDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public NotificationUser updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public NotificationUser updateUser(String updateUser) {
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
        if (!(o instanceof NotificationUser)) {
            return false;
        }
        return id != null && id.equals(((NotificationUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NotificationUser{" +
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
