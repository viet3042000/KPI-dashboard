package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.EmailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emails} and its DTO {@link EmailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmailsMapper extends EntityMapper<EmailsDTO, Emails> {



    default Emails fromId(Long id) {
        if (id == null) {
            return null;
        }
        Emails emails = new Emails();
        emails.setId(id);
        return emails;
    }
}
