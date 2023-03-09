package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.SysRoleModule;
import com.b4t.app.domain.User;
import com.b4t.app.security.CustomGrantedAuthority;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.security.jwt.JWTFilter;
import com.b4t.app.security.jwt.TokenProvider;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.vm.LoginVM;

import io.github.jhipster.web.util.HeaderUtil;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {
    private static final String ENTITY_NAME = "UserController";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;
    private final TokenDeviceUserService tokenDeviceUserService;

    private final MicReportService micReportService;
    private final SysRoleModuleService sysRoleModuleService;

    @Autowired
    CaptchaService captchaService;

    private static final Logger logger = LoggerFactory.getLogger(UserJWTController.class);

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService, TokenDeviceUserService tokenDeviceUserService, MicReportService micReportService, SysRoleModuleService sysRoleModuleService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.tokenDeviceUserService = tokenDeviceUserService;
        this.micReportService = micReportService;
        this.sysRoleModuleService = sysRoleModuleService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginDTO> authorize(@Valid @RequestBody LoginVM loginVM) {
        boolean captchaVerified = captchaService.verify(loginVM.getRecaptchaReactive());
        if (!captchaVerified) {
            logger.error(Constants.GOOGLE_RECAPTCHA_FALSE);
            throw new ServiceException(Translator.toLocale(Constants.GOOGLE_RECAPTCHA_FALSE));
        }
        Optional<User> userOpn = userService.getUserByLogin(loginVM.getUsername());
        micReportService.login(loginVM);
        List<String> lstPermission = this.sysRoleModuleService.findModuleByUser(userOpn.get().getId()).stream().map(m -> {
            if(!DataUtil.isNullOrEmpty(m.getActionCode())) {
                return m.getModuleCode()+ "_" + m.getActionCode();
            } else {
                return m.getModuleCode() + "_*";
            }
        }).collect(Collectors.toList());
        CustomGrantedAuthority grantedAuthority = new CustomGrantedAuthority();
        grantedAuthority.setPermissions(lstPermission);
        grantedAuthority.setRole("ROLE_USER");

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword(), Arrays.asList(grantedAuthority));
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authenticationToken, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        JWTToken jwtToken = new JWTToken(jwt, userOpn.get().getId());
        List<TreeItemsDTO> listObjects = userService.getRoles(userOpn.get().getId());
        String path_default = userService.getPathDefaultLogin(userOpn.get().getId());
        return new ResponseEntity<>(new LoginDTO(jwtToken,listObjects,path_default), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/authenticationcate")
    public ResponseEntity<Object> authenticationcate(HttpServletRequest request) {
        return new ResponseEntity<>( userService.authenticationcate( request ), HttpStatus.OK );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
//        String userName = SecurityUtils.getCurrentUserLogin().get();
//        if (!DataUtil.isNullOrEmpty(userName)) {
//            Optional<User> userOpn = userService.getUserWithAuthoritiesByLogin(userName);
//            if (userOpn.isPresent()) {
//                Optional<List<TokenDeviceUserDTO>> deviceTokenopt = tokenDeviceUserService.findAllByUserIdInAndTokenDevice(userOpn.get().getId().toString(), deviceToken);
//                if (deviceTokenopt.isPresent()) {
//                    List<TokenDeviceUserDTO> lstDeviceToken = deviceTokenopt.get();
//                    lstDeviceToken.stream().forEach(bean -> {
//                        tokenDeviceUserService.delete(bean.getId());
//                    });
//                }
//            }
//        }

        micReportService.logout();

        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(ENTITY_NAME));
        return ResponseEntity.ok().headers(headers).body("OK");
    }

    private void insertTokenDeviceUser(LoginVM loginVM) {
        if (DataUtil.isNullOrEmpty(loginVM.getTokenDevice())) return;
        Optional<User> userOpn = userService.getUserByLogin(loginVM.getUsername());
        if (userOpn.isPresent()) {
            TokenDeviceUserDTO tokenDeviceUser = new TokenDeviceUserDTO();
            tokenDeviceUser.setUserId(userOpn.get().getId());
            tokenDeviceUser.setTokenDevice(loginVM.getTokenDevice());
            tokenDeviceUser.setDeviceName(loginVM.getDeviceName());
            tokenDeviceUser.setStatus(Constants.STATUS_ACTIVE);
            tokenDeviceUser.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
            tokenDeviceUser.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
            tokenDeviceUserService.save(tokenDeviceUser);
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
//    static class JWTToken {
//
//        private String idToken;
//        private Long idUser;
//        JWTToken(String idToken) {
//            this.idToken = idToken;
//        }
//
//        public JWTToken(String idToken, Long idUser) {
//            this.idToken = idToken;
//            this.idUser = idUser;
//        }
//
//        public Long getIdUser() {
//            return idUser;
//        }
//
//        public void setIdUser(Long idUser) {
//            this.idUser = idUser;
//        }
//
//        @JsonProperty("id_token")
//        String getIdToken() {
//            return idToken;
//        }
//
//        void setIdToken(String idToken) {
//            this.idToken = idToken;
//        }
//    }
}
