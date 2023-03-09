package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.FavoriteUrlDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FavoriteUrl} and its DTO {@link FavoriteUrlDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FavoriteUrlMapper extends EntityMapper<FavoriteUrlDTO, FavoriteUrl> {



    default FavoriteUrl fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavoriteUrl favoriteUrl = new FavoriteUrl();
        favoriteUrl.setId(id);
        return favoriteUrl;
    }
}
