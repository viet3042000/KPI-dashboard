package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.SecurRptGraphDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SecurRptGraph} and its DTO {@link SecurRptGraphDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SecurRptGraphMapper extends EntityMapper<SecurRptGraphDTO, SecurRptGraph> {



    default SecurRptGraph fromId(Long id) {
        if (id == null) {
            return null;
        }
        SecurRptGraph securRptGraph = new SecurRptGraph();
        securRptGraph.setId(id);
        return securRptGraph;
    }
}
