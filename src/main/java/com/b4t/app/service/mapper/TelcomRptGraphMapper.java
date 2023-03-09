package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.TelcomRptGraphDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TelcomRptGraph} and its DTO {@link TelcomRptGraphDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TelcomRptGraphMapper extends EntityMapper<TelcomRptGraphDTO, TelcomRptGraph> {



    default TelcomRptGraph fromId(Long id) {
        if (id == null) {
            return null;
        }
        TelcomRptGraph telcomRptGraph = new TelcomRptGraph();
        telcomRptGraph.setId(id);
        return telcomRptGraph;
    }
}
