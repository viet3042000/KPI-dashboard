package com.b4t.app.service;

import com.b4t.app.service.dto.lgsp.BaseSyncResDTO;
import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;

public interface LgspService {
    BaseSyncResDTO listForm(SyncLGSPReqDTO req);

    BaseSyncResDTO getReportDetail(SyncLGSPReqDTO req);

    BaseSyncResDTO updateReportData(SyncLGSPReqDTO req);
}
