package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ItRptGraphDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItRptGraph} and its DTO {@link ItRptGraphDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItRptGraphMapper extends EntityMapper<ItRptGraphDTO, ItRptGraph> {



    default ItRptGraph fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItRptGraph itRptGraph = new ItRptGraph();
        itRptGraph.setId(id);
        return itRptGraph;
    }
}
