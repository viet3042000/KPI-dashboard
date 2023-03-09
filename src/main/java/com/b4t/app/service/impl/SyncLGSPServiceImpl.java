package com.b4t.app.service.impl;

import com.b4t.app.commons.Translator;
import com.b4t.app.security.DomainUserDetailsService;
import com.b4t.app.service.LgspService;
import com.b4t.app.service.LoginService;
import com.b4t.app.service.SyncLGSPService;
import com.b4t.app.service.dto.lgsp.BaseSyncResDTO;
import com.b4t.app.service.dto.lgsp.LoginResDTO;
import com.b4t.app.service.dto.lgsp.SyncLGSPError;
import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SyncLGSPServiceImpl implements SyncLGSPService {

    private final DomainUserDetailsService userDetailsService;
    private final LoginService loginService;
    private final LgspService lgspService;

    public SyncLGSPServiceImpl(DomainUserDetailsService userDetailsService, LoginService loginService, LgspService lgspService) {
        this.userDetailsService = userDetailsService;
        this.loginService = loginService;
        this.lgspService = lgspService;
    }

    @Override
    public Object syncReport(HttpServletRequest request, SyncLGSPReqDTO req) {
        if (!"login".equals(req.getFunc()) && StringUtils.isEmpty(req.getAccessToken())) {
            BaseSyncResDTO res = new BaseSyncResDTO();
            res.setErrorCode(SyncLGSPError.UNAUTHORIZED.value());
            res.setErrorMessage(Translator.toLocale("lgsp.error.unauthorized"));
            return res;
        }

        if (!userDetailsService.initContext(request, req.getAccessToken())) {
            BaseSyncResDTO res = new BaseSyncResDTO();
            res.setErrorCode(SyncLGSPError.UNAUTHORIZED.value());
            res.setErrorMessage(Translator.toLocale("lgsp.error.unauthorized"));
            return res;
        }

        if ("login".equals(req.getFunc())) {
            LoginResDTO res = loginService.loginLGSP(req);
            return res;
        }

        if ("lstForm".equals(req.getFunc())) {
            BaseSyncResDTO res = lgspService.listForm(req);
            return res;
        }

        if ("getReportDetail".equals(req.getFunc())) {
            BaseSyncResDTO res = lgspService.getReportDetail(req);
            return res;
        }

        if ("updateReportData".equals(req.getFunc())) {
            BaseSyncResDTO res = lgspService.updateReportData(req);
            return res;
        }

        return null;
    }
}
