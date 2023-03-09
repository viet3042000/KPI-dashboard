package com.b4t.app.service.mapper;

import com.b4t.app.domain.CatKpiReport;
import com.b4t.app.service.dto.CatKpiReportDTO;
import com.b4t.app.service.dto.TreeValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CatKpiReportMapper extends EntityMapper<CatKpiReportDTO, CatKpiReport> {
    default TreeValue toTreeValue(CatKpiReport catKpiReport) {
        TreeValue treeDTO = new TreeValue();
        treeDTO.setId(catKpiReport.getKpiId().toString());
        treeDTO.setDes(catKpiReport.getTableName());
        treeDTO.setUnit(catKpiReport.getUnitName());
        return treeDTO;
    }
}
