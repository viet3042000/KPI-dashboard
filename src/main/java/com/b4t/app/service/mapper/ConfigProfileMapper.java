package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ConfigProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigProfile} and its DTO {@link ConfigProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigProfileMapper extends EntityMapper<ConfigProfileDTO, ConfigProfile> {



    default ConfigProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigProfile configProfile = new ConfigProfile();
        configProfile.setId(id);
        return configProfile;
    }
}
