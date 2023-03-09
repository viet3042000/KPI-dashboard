package com.b4t.app.service.mapper;


import com.b4t.app.domain.CatItem;
import com.b4t.app.domain.Category;
import com.b4t.app.service.dto.CatItemDTO;
import com.b4t.app.service.dto.CategoryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link CatItem} and its DTO {@link CatItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

}
