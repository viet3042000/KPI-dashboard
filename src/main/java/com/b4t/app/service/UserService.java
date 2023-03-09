package com.b4t.app.service;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.RandomUtils;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.config.CustomUserDetails;
import com.b4t.app.domain.*;
import com.b4t.app.repository.*;
import com.b4t.app.repository.impl.DataLogRepositoryImpl;
import com.b4t.app.security.AuthoritiesConstants;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.security.jwt.TokenProvider;
import com.b4t.app.service.dto.*;
import com.b4t.app.service.impl.UserServiceImpl;
import com.b4t.app.service.mapper.UserMapper;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.security.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

//    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final EntityManager entityManager;

    private final UserMapper userMapper;

    private final UserCustomRepository userCustomRepository;

    private final DataLogRepositoryImpl dataLogRepositoryImpl;

    private final UserServiceImpl userServiceImpl;

    @Autowired
    SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    TokenProvider tokenProvider;


    @Value("${user.pass-default}")
    String defaultPass;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       CacheManager cacheManager, SysRoleService sysRoleService, EntityManager entityManager, UserMapper userMapper, UserCustomRepository userCustomRepository,
                       DataLogRepositoryImpl dataLogRepositoryImpl, UserServiceImpl userServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.userCustomRepository = userCustomRepository;
        this.sysRoleService = sysRoleService;
        this.dataLogRepositoryImpl = dataLogRepositoryImpl;
        this.userServiceImpl = userServiceImpl;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtils.generateOtp());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
//        Set<Authority> authorities = new HashSet<>();
//        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
//        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword;
        String md5Password;
        if (!StringUtils.isEmpty(userDTO.getPassword())) {
            encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            md5Password = DataUtil.md5Encode(userDTO.getPassword());
        } else {
            encryptedPassword = passwordEncoder.encode(Constants.DEFAULT_SECRET_KEY);
            md5Password = DataUtil.md5Encode(Constants.DEFAULT_SECRET_KEY);
        }
        user.setPassword(encryptedPassword);
        user.setPasswordMd5(md5Password);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(userDTO.isActivated());
        user.setAlarmLeader(userDTO.isAlarmLeader());
        user.setPhone(userDTO.getPhone());
        user.setTenantId(SecurityUtils.getCurrentUserTenantId(userRepository).orElse(null));
        if (userDTO.getAuthorities() != null) {
//            Set<Authority> authorities = userDTO.getAuthorities().stream()
//                .map(authorityRepository::findById)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());
//            user.setAuthorities(authorities);

        }

        user = userRepository.save(user);
        Long userId = user.getId();
        if (userDTO.getAuthorities() != null) {
            List<SysUserRole> lstSysUserRole = userDTO.getAuthorities().stream().map(e-> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(e);
                sysUserRole.setUserId(userId);
                sysUserRole.setUpdateTime(Instant.now());
                return sysUserRole;
            }).collect(Collectors.toList());
            sysUserRoleRepository.saveAll(lstSysUserRole);
        }
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        Optional<UserDTO> userOpt= Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setAlarmLeader(userDTO.isAlarmLeader());
                user.setLangKey(userDTO.getLangKey());
                user.setPhone(userDTO.getPhone());
                user.setDeptPermissionCode(userDTO.getDeptPermissionCode());
                user.setUnitLeader(userDTO.getUnitLeader());
//                Set<Authority> managedAuthorities = user.getAuthorities();
//                managedAuthorities.clear();
//                userDTO.getAuthorities().stream()
//                    .map(authorityRepository::findById)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);

        List<SysUserRole> lstUserRole = sysUserRoleRepository.findAllByUserId(userDTO.getId()).stream().filter(Objects::nonNull).collect(Collectors.toList());
        Set<Long> roles = userDTO.getAuthorities().stream().map(e-> Long.valueOf(e)).collect(Collectors.toSet());
        Map<Long, Long> mapRoleId = lstUserRole.stream().filter(Objects::nonNull).collect(Collectors.toMap(SysUserRole::getRoleId, SysUserRole::getId, (o1,o2) -> o1));
        List<SysUserRole> lstDelete  = lstUserRole.stream().filter(e-> !roles.contains(e.getRoleId())).collect(Collectors.toList());

        List<SysUserRole> lstUpdate = userDTO.getAuthorities().stream().map(e -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userDTO.getId());
            sysUserRole.setRoleId(Long.valueOf(e));
            sysUserRole.setUpdateTime(Instant.now());
            return sysUserRole;
        }).peek(e -> {
            if(mapRoleId.containsKey(e.getRoleId())) {
                e.setId(mapRoleId.get(e.getRoleId()));
            }
        }).collect(Collectors.toList());
        sysUserRoleRepository.deleteInBatch(lstDelete);
        sysUserRoleRepository.saveAll(lstUpdate);

        return userOpt;
    }

    public Optional<UserDTO> updateStatusUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setActivated(userDTO.isActivated());
                return user;
            })
            .map(UserDTO::new);
    }
    public Optional<UserDTO> resetPassByAdmin(String userLogin) {
        log.debug("Admin Reset user password for user {}", userLogin);
        return userRepository.findOneByLogin(userLogin)
            .map(user -> {
                user.setPassword(passwordEncoder.encode(this.defaultPass));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                userRepository.save(user);
                return user;
            }).map(UserDTO::new);
    }

    public void changePass(ChangePassDTO changePassDTO) throws Exception{
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(changePassDTO.getCurrentPassword(), currentEncryptedPassword)) {
                    throw new BadRequestAlertException(Translator.toLocale("error.changePass.passwordIncorrect"),"auth","changePass.passwordIncorrect");
                }
                String encryptedPassword = passwordEncoder.encode(changePassDTO.getPassword());
                user.setPassword(encryptedPassword);
                user.setPasswordMd5(DataUtil.md5Encode(changePassDTO.getPassword()));
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    public void adminChangePass(ChangePassDTO changePassDTO) throws Exception{
        userRepository.findOneByLogin(changePassDTO.getCurrentPassword()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(changePassDTO.getPassword());
            user.setPassword(encryptedPassword);
            this.clearUserCaches(user);
            log.debug("Changed password for User: {}", user);
        });
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            sysUserRoleRepository.deleteAllByUserId(user.getId());
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                user.setPasswordMd5(DataUtil.md5Encode(newPassword));
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(String keyword, Boolean activated, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> root = criteria.from(User.class);

        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<User> rootCnt = countCrit.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesCount = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate predicate = cb.or(
                cb.like(cb.lower(root.get(User_.LOGIN)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(User_.EMAIL)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(User_.PHONE)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(User_.FIRST_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(User_.LAST_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(predicate);
            predicatesCount.add(predicate);
        }
        predicates.add(cb.notEqual(root.get(User_.LOGIN), Constants.ANONYMOUS_USER));
        predicatesCount.add(cb.notEqual(root.get(User_.LOGIN), Constants.ANONYMOUS_USER));
        if (activated != null) {
            predicates.add(cb.equal(root.get(User_.ACTIVATED), activated));
            predicatesCount.add(cb.notEqual(root.get(User_.ACTIVATED), activated));
        }
        criteria.select(root);
        countCrit.select(cb.count(rootCnt));

        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        List<User> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        countCrit.where(cb.and(predicatesCount.toArray(new Predicate[predicatesCount.size()])));
        Long count = entityManager.createQuery(countCrit).getSingleResult();

        List<UserDTO> rsDTOs = rs.stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

//    @Transactional(readOnly = true)
//    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
//        return userRepository.findOneWithAuthoritiesByLogin(login);
//    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

//    @Transactional(readOnly = true)
//    public Optional<User> getUserWithAuthorities(Long id) {
//        return userRepository.findOneWithAuthoritiesById(id);
//    }

//    @Transactional(readOnly = true)
//    public Optional<User> getUserWithAuthorities() {
//        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
//    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
//    public List<String> getAuthorities() {
//        List<String> a= authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
//        return a;
//    }

        public List<SysRoleDTO> getAuthorities() {
        List<SysRoleDTO> a= sysRoleService.getAllRole();
        return a;
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public Page<UserCustom> getUserWithNotify(String keyword, Long userId, Pageable pageable) {
        return userCustomRepository.getUserWithNotify(keyword, userId, pageable);
    }

    public UserDTO getByLogin(String login){
        return userMapper.userToUserDTO(userRepository.findOneByLogin(login).orElse(new User()));
    }

    public UserDTO getById(Long id){
        return userMapper.userToUserDTO(userRepository.findOneById(id).orElse(new User()));
    }

    public List<UserDTO> getByLogins(List<String> logins) {
        return userMapper.usersToUserDTOs(userRepository.findByLoginIn(logins));
    }

    public List<UserDTO> getByIds(List<Long> ids) {
        return userMapper.usersToUserDTOs(userRepository.findByIdIn(ids));
    }

    public List<UserDTO> getUserFromDataLog(int time, String tableName) {
        List<String> lstUsername = dataLogRepositoryImpl.getModifiedBy(time, tableName);
        List<UserDTO> lstUser= lstUsername.stream().map(e -> new UserDTO(e)).collect(Collectors.toList());
        return lstUser;
    }

    public Page<UserDetailDTO> queryUser(UserSearchDTO userSearchDTO, Pageable pageable) {
        return userRepository.queryUser(userSearchDTO.getAuthorities(),
            userSearchDTO.getDomains(),
            DataUtil.makeLikeParam(userSearchDTO.getKeyword()),
            pageable).map(obj -> {
            UserDetailDTO user = new UserDetailDTO();
            user.setId(DataUtil.safeToLong(obj[0]));
            user.setLogin(DataUtil.safeToString(obj[1]));
            user.setEmail(DataUtil.safeToString(obj[2]));
            user.setActivated(DataUtil.safeToBoolean(obj[3]));
            user.setPhone(DataUtil.safeToString(obj[4]));
            if(obj[5] != null) {
                user.setCreatedDate(((Timestamp) obj[5]).toInstant());
            }
            user.setAuthoritiesName(DataUtil.safeToString(obj[6]));
            user.setDomainCode(DataUtil.safeToString(obj[7]));
            return user;
        });
    }

    public Page<UserDetailDTO> queryUserById(String id, Pageable pageable) {
        return userRepository.queryUserById(
            id,
            pageable).map(obj -> {
            UserDetailDTO user = new UserDetailDTO();
            user.setId(DataUtil.safeToLong(obj[0]));
            user.setLogin(DataUtil.safeToString(obj[1]));
            user.setEmail(DataUtil.safeToString(obj[2]));
            user.setActivated(DataUtil.safeToBoolean(obj[3]));
            user.setPhone(DataUtil.safeToString(obj[4]));
            if(obj[5] != null) {
                user.setCreatedDate(((Timestamp) obj[5]).toInstant());
            }
            user.setAuthoritiesName(DataUtil.safeToString(obj[6]));
            user.setDomainCode(DataUtil.safeToString(obj[7]));
            return user;
        });
    }

    public List<TreeItemsDTO> getRoles(Long userId){
        List<SysModuleDTO> lstObject = userRepository.getRoleAction( userId )
            .stream().map( e -> {
                SysModuleDTO dto = new SysModuleDTO();
                dto.setCodeAction( DataUtil.safeToString( e[0] ) );
                dto.setId( DataUtil.safeToLong( e[1] ) );
                dto.setCode( DataUtil.safeToString( e[2] ) );
                dto.setName( DataUtil.safeToString( e[3] ) );
                dto.setTenantId( DataUtil.safeToLong( e[4] ) );
                dto.setDescription( DataUtil.safeToString( e[5] ) );
                dto.setStatus( DataUtil.safeToInt( e[6] ) );
                dto.setParentId( DataUtil.safeToLong( e[8] ) );
                return dto;
            } ).collect( Collectors.toList() );
        List<SysModuleDTO> lstSysModuleDTO = userRepository.getRole( userId )
            .stream().map( e -> {
                SysModuleDTO dto = new SysModuleDTO();
                dto.setId( DataUtil.safeToLong( e[0] ) );
                dto.setCode( DataUtil.safeToString( e[1] ) );
                dto.setName( DataUtil.safeToString( e[2] ) );
                dto.setTenantId( DataUtil.safeToLong( e[3] ) );
                dto.setDescription( DataUtil.safeToString( e[4] ) );
                dto.setStatus( DataUtil.safeToInt( e[5] ) );
                dto.setParentId( DataUtil.safeToLong( e[7] ) );
                dto.setIcon( DataUtil.safeToString( e[8] ) );
                dto.setPathUrl( DataUtil.safeToString( e[9] ) );
                return dto;
            } ).collect( Collectors.toList() );
        List<ListActionDTO> listActionDTO = new ArrayList<>();
        List<TreeItemsDTO> listTreeDTO = new ArrayList<>();
        if (lstObject.size() > 0) {
            List<RoleActionDTO> listRoleActionDTO  = new ArrayList<>();
            for (int i = 0; i < lstObject.size() - 1; i++) {
                RoleActionDTO acitonDTO = new RoleActionDTO();
                if (lstObject.get( i ).getId().equals( lstObject.get( i + 1 ).getId() )) {
                    acitonDTO.setCodeAction( lstObject.get( i ).getCodeAction() );
                    listRoleActionDTO.add( acitonDTO );
                } else if (!lstObject.get( i ).getId().equals( lstObject.get( i + 1 ).getId() )) {
                    acitonDTO.setCodeAction( lstObject.get( i ).getCodeAction() );
                    listRoleActionDTO.add( acitonDTO );
                    ListActionDTO lsActionDTO = new ListActionDTO();
                    lsActionDTO.setId( lstObject.get( i ).getId() );
                    lsActionDTO.setListAction( listRoleActionDTO );
                    listActionDTO.add( lsActionDTO );
                    listRoleActionDTO = new ArrayList<>();
                }
                if (lstObject.size() == (i + 2)) {
                    RoleActionDTO acitonDTOs = new RoleActionDTO();
                    acitonDTOs.setCodeAction( lstObject.get( i + 1 ).getCodeAction() );
                    listRoleActionDTO.add( acitonDTOs );
                    ListActionDTO lstActionDTO = new ListActionDTO();
                    lstActionDTO.setId( lstObject.get( i + 1 ).getId() );
                    lstActionDTO.setListAction( listRoleActionDTO );
                    listActionDTO.add( lstActionDTO );
                }
            }
        }
        for (int i = 0; i < lstSysModuleDTO.size(); i++) {
            for (int j = 0; j < listActionDTO.size(); j++) {
                if (lstSysModuleDTO.get( i ).getId().equals( listActionDTO.get( j ).getId() )) {
                    lstSysModuleDTO.get( i ).setRole( listActionDTO.get( j ).getListAction() );
                }
            }
            listTreeDTO.add( lstSysModuleDTO.get( i ).toTree() );
        }
        return listTreeDTO;
    }

    public LoginDTO authenticationcate(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(Constants.AUTHORIZATION);
        LoginDTO login = new LoginDTO();

        String username = null;
        String jwtToken = null;
        if (StringUtils.isNotEmpty(requestTokenHeader) && requestTokenHeader.startsWith(Constants.BEARER)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = tokenProvider.getAuthenticationByToken(jwtToken);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
        User rs = userRepository.findOneByLogin(username).get();
        UserDTO usersDTO = userMapper.userToUserDTO(rs);
        if (usersDTO != null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenProvider.validateToken(jwtToken)) {
                login.setListObjects(getRoles(usersDTO.getId()));
            }
        } else {
            log.debug("error {}", Constants.LOGIN_NAME_FALSE);
            throw new ServiceException(Translator.toLocale(Constants.LOGIN_NAME_FALSE));

        }
        return login;
    }


    public String getPathDefaultLogin(Long userId){
        return userRepository.getPathDefaultLogin(userId);
    }


}
