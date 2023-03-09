package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.NotificationUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationUser} and its DTO {@link NotificationUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationUserMapper extends EntityMapper<NotificationUserDTO, NotificationUser> {



    default NotificationUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationUser notificationUser = new NotificationUser();
        notificationUser.setId(id);
        return notificationUser;
    }
}
