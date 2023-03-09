package com.b4t.app.service;

import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;

import javax.servlet.http.HttpServletRequest;

public interface SyncLGSPService {
    Object syncReport(HttpServletRequest request, SyncLGSPReqDTO req);
}
