package com.b4t.app.service.mapper;

import com.b4t.app.domain.RptGraph;
import com.b4t.app.service.dto.RptGraphDTO;
import com.b4t.app.domain.*;
import com.b4t.app.service.dto.ItRptGraphDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RptGraph} and its DTO {@link RptGraphDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RptGraphMapper extends EntityMapper<RptGraphDTO, RptGraph> {



    default RptGraph fromId(Long id) {
        RptGraph rptGraph = new RptGraph();
        rptGraph.setId(id);
        return rptGraph;
    }
}
