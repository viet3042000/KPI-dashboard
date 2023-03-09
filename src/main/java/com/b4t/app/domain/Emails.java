package com.b4t.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Emails.
 */
@Entity
@Table(name = "emails")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Emails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "email")
    private String email;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private Long status;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "sent_time")
    private Instant sentTime;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_repeat")
    private Long isRepeat;

    @Column(name = "source")
    private String source;

    @Column(name = "notify_id")
    private Long notifyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Emails subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public Emails email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public Emails content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getStatus() {
        return status;
    }

    public Emails status(Long status) {
        this.status = status;
        return this;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Emails createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getSentTime() {
        return sentTime;
    }

    public Emails sendTime(Instant sentTime) {
        this.sentTime = sentTime;
        return this;
    }

    public void setSentTime(Instant sentTime) {
        this.sentTime = sentTime;
    }

    public Long getUserId() {
        return userId;
    }

    public Emails userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIsRepeat() {
        return isRepeat;
    }

    public Emails isRepeat(Long isRepeat) {
        this.isRepeat = isRepeat;
        return this;
    }

    public void setIsRepeat(Long isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getSource() {
        return source;
    }

    public Emails source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getNotifyId() {
        return notifyId;
    }

    public Emails notifyId(Long notifyId) {
        this.notifyId = notifyId;
        return this;
    }

    public void setNotifyId(Long notifyId) {
        this.notifyId = notifyId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Emails)) {
            return false;
        }
        return id != null && id.equals(((Emails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Emails{" +
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
