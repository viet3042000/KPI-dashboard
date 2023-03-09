package com.b4t.app.service.impl;

import com.b4t.app.security.jwt.JWTFilter;
import com.b4t.app.service.MicReportService;
import com.b4t.app.web.rest.vm.LoginVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class MicReportServiceImpl implements MicReportService {

    private final Logger log = LoggerFactory.getLogger(MicReportServiceImpl.class);

    private Environment env;

    public MicReportServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public void login(LoginVM loginVM) {
        try {
            String url = env.getProperty("mic-report.host.authenticate");
            if (StringUtils.isEmpty(url)) return;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return;
    }

    @Override
    public void logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(JWTFilter.AUTHORIZATION_HEADER).split(" ")[1];
        try {
            String url = env.getProperty("mic-report.host.logout");
            if (StringUtils.isEmpty(url)) return;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + token);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<String>() {
                });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return;
    }
}
