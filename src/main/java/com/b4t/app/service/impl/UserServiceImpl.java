package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.DateUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.domain.SysRole;
import com.b4t.app.domain.User;
import com.b4t.app.repository.SysRoleRepository;
import com.b4t.app.repository.SysUserRoleRepository;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.SysRoleService;
import com.b4t.app.service.dto.SysRoleDTO;
import com.b4t.app.service.dto.UserDTO;
import com.b4t.app.service.mapper.SysRoleMapper;
import com.b4t.app.service.mapper.UserMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing {@link SysRole}.
 */
@Service
@Transactional
public class UserServiceImpl {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final SysRoleRepository sysRoleRepository;

    private final SysUserRoleRepository sysUserRoleRepository;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserServiceImpl(SysRoleRepository sysRoleRepository, SysUserRoleRepository sysUserRoleRepository, UserMapper userMapper, UserRepository userRepository) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }
    public List<UserDTO> getUserByLogin(List<String> lstUsername) {
        List<User> lstUser = new ArrayList<>();
        for(String login : lstUsername){
            if(login == null){
                lstUser.add(new User());
            } else {
                User user = userRepository.getUserForDataLog(login);
                lstUser.add(user);
            }
        }
        List<UserDTO> userDTOs = userMapper.usersToUserDTOs(lstUser);
        return userDTOs;
    }

}
