package com.b4t.app.security;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DateUtil;
import com.b4t.app.config.CustomUserDetails;
import com.b4t.app.domain.User;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.jwt.TokenProvider;
import com.b4t.app.service.dto.SysRoleDTO;
import com.b4t.app.service.dto.UserDTO;
import com.b4t.app.service.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private TokenProvider tokenProvider;

    public DomainUserDetailsService(TokenProvider tokenProvider, UserRepository userRepository, UserMapper userMapper) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
//        Optional<User> user = userRepository.findOneByLogin(lowercaseLogin);
        return userRepository.findOneByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            //.map(this::createSpringSecurityUser)
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<SysRoleDTO> lstRoleDTO = userRepository.getRoleName(user.getId()).stream().map(obj -> {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            sysRoleDTO.setId(DataUtil.safeToLong(obj[0]));
            sysRoleDTO.setName(DataUtil.safeToString(obj[1]));
            sysRoleDTO.setCode(DataUtil.safeToString(obj[2]));
            return sysRoleDTO;
        }).collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthorities = lstRoleDTO.stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getCode()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
            user.getPassword(),
            grantedAuthorities);
    }

//    private CustomUserDetails createSpringSecurityUser(User user) {
//        if (!user.getActivated())
//            throw new UserNotActivatedException("User " + user.getLogin() + " was not activated");
//        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
//            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//            .collect(Collectors.toList());
//        UserDTO dto = userMapper.userToUserDTO(user);
//        return new CustomUserDetails(dto, null, grantedAuthorities);
//    }

    public boolean initContext(HttpServletRequest request, String token) {
        if (StringUtils.isNotEmpty(token)) {
            try {
                String username = tokenProvider.getAuthenticationByToken(token);
                if (StringUtils.isNotEmpty(username)) {
                    UserDetails userDetails = loadUserByUsername(username);
                    if (tokenProvider.validateToken(token)) {
                        String refreshToken = tokenProvider.generateToken(userDetails);
                        Authentication authentication = this.tokenProvider.getAuthentication(refreshToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        return false;
                    }
                }

            } catch (Exception e) {
                log.info(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }
}
