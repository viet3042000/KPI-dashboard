package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.TokenDeviceUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TokenDeviceUser} and its DTO {@link TokenDeviceUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TokenDeviceUserMapper extends EntityMapper<TokenDeviceUserDTO, TokenDeviceUser> {



    default TokenDeviceUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TokenDeviceUser tokenDeviceUser = new TokenDeviceUser();
        tokenDeviceUser.setId(id);
        return tokenDeviceUser;
    }
}
