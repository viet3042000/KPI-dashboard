package com.b4t.app.web.rest;

import com.b4t.app.commons.Translator;
import com.b4t.app.service.SyncLGSPService;
import com.b4t.app.service.dto.lgsp.BaseSyncResDTO;
import com.b4t.app.service.dto.lgsp.SyncLGSPError;
import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class SyncLGSPResource {
    @Autowired
    SyncLGSPService syncLGSPService;

    @PostMapping("/dashboard-sync-lgsp")
    public ResponseEntity<Object> syncLGSP(HttpServletRequest request, @RequestBody SyncLGSPReqDTO req) {
        Object response = syncLGSPService.syncReport(request, req);
        if (response != null) {
            return ResponseEntity.ok(response);
        }

        BaseSyncResDTO res = new BaseSyncResDTO();
        res.setErrorCode(SyncLGSPError.NOT_FOUND.value());
        res.setErrorMessage(Translator.toLocale("lgsp.error.funcNotFound"));
        return ResponseEntity.ok().body(res);
    }
}
