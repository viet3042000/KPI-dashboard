package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.PostalRptGraphDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostalRptGraph} and its DTO {@link PostalRptGraphDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostalRptGraphMapper extends EntityMapper<PostalRptGraphDTO, PostalRptGraph> {



    default PostalRptGraph fromId(Long id) {
        if (id == null) {
            return null;
        }
        PostalRptGraph postalRptGraph = new PostalRptGraph();
        postalRptGraph.setId(id);
        return postalRptGraph;
    }
}
