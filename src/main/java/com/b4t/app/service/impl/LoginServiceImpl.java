package com.b4t.app.service.impl;


import com.b4t.app.commons.Translator;
import com.b4t.app.domain.User;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.jwt.TokenProvider;
import com.b4t.app.service.LoginService;
import com.b4t.app.service.dto.lgsp.LoginResDTO;
import com.b4t.app.service.dto.lgsp.SyncLGSPError;
import com.b4t.app.service.dto.lgsp.SyncLGSPReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Override
    public LoginResDTO loginLGSP(SyncLGSPReqDTO req) {
        LoginResDTO res = new LoginResDTO();
        if (StringUtils.isEmpty(req.getUser()) || StringUtils.isEmpty(req.getPassword())) {
            res.setErrorCode(SyncLGSPError.UNAUTHORIZED.value());
            res.setErrorMessage(Translator.toLocale("lgsp.login.userPassRequired"));
            return res;
        }

        Optional<User> optUser = userRepository.findOneByLogin(req.getUser());
        if (!optUser.isPresent()) {
            res.setErrorCode(SyncLGSPError.UNAUTHORIZED.value());
            res.setErrorMessage(Translator.toLocale("lgsp.login.userNotFound"));
            return res;
        }
        User user = optUser.get();
        if (!user.isActivated()) {
            res.setErrorCode(SyncLGSPError.UNAUTHORIZED.value());
            res.setErrorMessage(Translator.toLocale("lgsp.error.userIsLocked"));
            return res;
        }

        if (!req.getPassword().equals(user.getPasswordMd5())) {
            res.setErrorCode(SyncLGSPError.UNAUTHORIZED.value());
            res.setErrorMessage(Translator.toLocale("lgsp.login.wrongPassword"));
            return res;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createTokenLGSP(authentication);
        res.setAccessToken(token);
        return res;
    }
}
