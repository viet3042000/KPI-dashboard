package com.b4t.app.domain;

import javax.persistence.*;
import java.time.Instant;

@SqlResultSetMapping(
    name = "NotificationOfUser",
    entities = {
        @EntityResult(
            entityClass = NotificationOfUser.class,
            fields = {
                @FieldResult(name = "id", column = "id"),
                @FieldResult(name = "title", column = "title"),
                @FieldResult(name = "userIdSent", column = "user_id_sent"),
                @FieldResult(name = "content", column = "content"),
                @FieldResult(name = "imagePath", column = "image_path"),
                @FieldResult(name = "fileAttachPath", column = "file_attach_path"),
                @FieldResult(name = "createDate", column = "create_date"),
                @FieldResult(name = "userIdRecieved", column = "user_id_recieved"),
                @FieldResult(name = "isRead", column = "is_read"),
                @FieldResult(name = "isDeleted", column = "is_deleted"),
            })}
)
@Entity
public class NotificationOfUser {
    @Id
    private Long id;

    private String title;

    private Long userIdSent;

    private String content;

    private String imagePath;

    private String fileAttachPath;

    private String fileAttachName;

    private Instant createDate;

    private Long userIdRecieved;

    private Long isRead;

    private Long isDeleted;

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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Long getUserIdRecieved() {
        return userIdRecieved;
    }

    public void setUserIdRecieved(Long userIdRecieved) {
        this.userIdRecieved = userIdRecieved;
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
}
