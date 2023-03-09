package com.b4t.app.service.mapper;

import com.b4t.app.domain.ConfigReportColumn;
import com.b4t.app.domain.ReportAlertDetail;
import com.b4t.app.service.dto.ReportAlertDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ReportAlertDetailMapper extends EntityMapper<ReportAlertDetailDTO, ReportAlertDetail> {
    default ReportAlertDetail fromId(Integer id) {
        if (id == null) {
            return null;
        }
        ReportAlertDetail reportAlertDetail = new ReportAlertDetail();
        reportAlertDetail.setId(id);
        return reportAlertDetail;
    }
}
