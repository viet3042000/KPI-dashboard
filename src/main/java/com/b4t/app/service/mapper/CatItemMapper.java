package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.CatItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatItem} and its DTO {@link CatItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatItemMapper extends EntityMapper<CatItemDTO, CatItem> {


    default CatItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        CatItem catItem = new CatItem();
        catItem.itemId(id);
        return catItem;
    }

}
