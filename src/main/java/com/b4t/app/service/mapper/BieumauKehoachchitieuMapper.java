package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.BieumauKehoachchitieuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BieumauKehoachchitieu} and its DTO {@link BieumauKehoachchitieuDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BieumauKehoachchitieuMapper extends EntityMapper<BieumauKehoachchitieuDTO, BieumauKehoachchitieu> {



    default BieumauKehoachchitieu fromId(Long id) {
        if (id == null) {
            return null;
        }
        BieumauKehoachchitieu bieumauKehoachchitieu = new BieumauKehoachchitieu();
        bieumauKehoachchitieu.setId(id);
        return bieumauKehoachchitieu;
    }
}
