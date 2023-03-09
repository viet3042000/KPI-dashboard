package com.b4t.app.service;

import com.b4t.app.service.dto.lgsp.LoginResDTO;
import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;

public interface LoginService {

    LoginResDTO loginLGSP(SyncLGSPReqDTO req);
}
