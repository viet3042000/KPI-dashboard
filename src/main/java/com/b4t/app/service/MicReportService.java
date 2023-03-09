package com.b4t.app.service;

import com.b4t.app.web.rest.vm.LoginVM;
import org.springframework.security.core.Authentication;

public interface MicReportService {
    void login(LoginVM loginVM);

    void logout();
}
