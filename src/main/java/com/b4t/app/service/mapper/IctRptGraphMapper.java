package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.dto.IctRptGraphDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link IctRptGraph} and its DTO {@link IctRptGraphDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IctRptGraphMapper extends EntityMapper<IctRptGraphDTO, IctRptGraph> {



    default IctRptGraph fromId(Long id) {
        if (id == null) {
            return null;
        }
        IctRptGraph ictRptGraph = new IctRptGraph();
        ictRptGraph.setId(id);
        return ictRptGraph;
    }
}
