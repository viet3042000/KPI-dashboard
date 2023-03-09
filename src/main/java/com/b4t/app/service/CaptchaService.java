package com.b4t.app.service;

import com.b4t.app.service.dto.RecaptchaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaService {

    private final Logger log = LoggerFactory.getLogger( CaptchaService.class );
    @Value("${google.captcha.secretKey}")
    private String secretKey;
    @Value("${google.captcha.verifyUrl}")
    private String verifyUrl;
    @Value("${google.captcha.enable}")
    private boolean enable;

    private final RestTemplate restTemplate;

    public CaptchaService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public boolean verify(String response) {
        if(!enable) {
            return true;
        }
        MultiValueMap param = new LinkedMultiValueMap<>();
        param.add( "secret", secretKey );
        param.add( "response", response );

        RecaptchaDTO recaptchaResponse;
        try {
            recaptchaResponse = this.restTemplate.postForObject( verifyUrl, param, RecaptchaDTO.class );
            if(recaptchaResponse != null) {
                return recaptchaResponse.isSuccess();
            }
        } catch (RestClientException e) {
            log.error( e.getMessage(), e );
        }
        return false;
    }

}
