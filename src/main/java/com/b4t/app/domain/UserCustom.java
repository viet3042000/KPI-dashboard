package com.b4t.app.domain;

import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity

@SqlResultSetMapping(
    name = "UserCustom",
    entities = {
        @EntityResult(
            entityClass = UserCustom.class,
            fields = {
                @FieldResult(name = "userId", column = "id"),
                @FieldResult(name = "userLoginId", column = "user_login_id"),
                @FieldResult(name = "username", column = "login"),
                @FieldResult(name = "avatar", column = "image_url"),
                @FieldResult(name = "content", column = "content"),
                @FieldResult(name = "timeNewest", column = "time_newest"),
                @FieldResult(name = "isAlarmLeader", column = "is_alarm_leader"),
                @FieldResult(name = "numNotifyUnread", column = "num_notify_unread")
            })}
)
public class UserCustom implements Serializable {
    @Id
    private Long userId;
    private Long userLoginId;
    private String username;
//    private String email;
    private String avatar;
    private Instant timeNewest;
    private Long numNotifyUnread;
    private Long isAlarmLeader;
    private String content;

    public UserCustom() {
    }

    public UserCustom(Long id) {
        this.userId = id;
    }

    public UserCustom(Long id, String userName) {
        this.userId = id;
        this.username = userName;
    }
//    public UserCustom(Long id, String userName, String email) {
//        this.userId = id;
//        this.username = userName;
//        this.email = email;
//    }

    public UserCustom(Long userLoginId, Long numNotifyUnread) {
        this.userLoginId = userLoginId;
        this.numNotifyUnread = numNotifyUnread;
    }

//    public UserCustom(Long id, Long userLoginId, String userName, String email, String avatar, Instant timeNewest, Long numNotifyUnread, Long isAlarmLeader) {
//        this.userId = id;
//        this.userLoginId = userLoginId;
//        this.username = userName;
//        this.email = email;
//        this.avatar = avatar;
//        this.timeNewest = timeNewest;
//        this.numNotifyUnread = numNotifyUnread;
//        this.isAlarmLeader = isAlarmLeader;
//    }

    public Instant getTimeNewest() {
        return timeNewest;
    }

    public void setTimeNewest(Instant timeNewest) {
        this.timeNewest = timeNewest;
    }

    @JsonSetter
    private void setTimeNewest(long time) {
        this.timeNewest = time == 0 ? null : Instant.ofEpochMilli(time);
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(Long userLoginId) {
        this.userLoginId = userLoginId;
    }

    public Long getNumNotifyUnread() {
        return numNotifyUnread;
    }

    public void setNumNotifyUnread(Long numNotifyUnread) {
        this.numNotifyUnread = numNotifyUnread;
    }

    public Long getIsAlarmLeader() {
        return isAlarmLeader;
    }

    public void setIsAlarmLeader(Long isAlarmLeader) {
        this.isAlarmLeader = isAlarmLeader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
