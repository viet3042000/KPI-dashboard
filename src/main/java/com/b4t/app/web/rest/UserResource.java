package com.b4t.app.web.rest;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.commons.Translator;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.User;
import com.b4t.app.domain.UserCustom;
import com.b4t.app.domain.UserDomain;
import com.b4t.app.repository.UserRepository;
import com.b4t.app.security.AuthoritiesConstants;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.*;
import com.b4t.app.service.dto.*;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.web.rest.errors.EmailAlreadyUsedException;
import com.b4t.app.web.rest.errors.LoginAlreadyUsedException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserDomainService userDomainService;

    private final SysUserRoleService sysUserRoleService;
    private static final String ENTITY_NAME = "user";

    @Autowired
    CaptchaService captchaService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService, UserDomainService userDomainService, SysUserRoleService sysUserRoleService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userDomainService = userDomainService;
        this.sysUserRoleService = sysUserRoleService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            if (!DataUtil.isNullOrEmpty(userDTO.getDomains())) {
                userDomainService.createUserDomain(newUser.getId(), userDTO.getDomains());
            }
            mailService.sendCreationEmail(newUser, userDTO.getPassword());
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        updatedUser.ifPresent(user -> {
            userDomainService.deleteDomain(user.getId());
            if (!DataUtil.isNullOrEmpty(userDTO.getDomains())) {
                userDomainService.createUserDomain(user.getId(), userDTO.getDomains());
            }
        });

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()));
    }

    @PutMapping("/users/updateStatus")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<UserDTO> updateStatus(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);

        Optional<UserDTO> updatedUser = userService.updateStatusUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()));
    }

    @PostMapping("/users/updateStatusMultiple")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<Void> updateStatusMultiple(@Valid @RequestBody List<UserDTO> userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        for (UserDTO us : userDTO) {
            userService.updateStatusUser(us);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/resetPass")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<Void> resetPass(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> user = userService.requestPasswordReset(userDTO.getEmail());
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail '{}'", userDTO.getEmail());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/changePass")
    public ResponseEntity<Void> changePass(@RequestBody ChangePassDTO changePassDTO) throws Exception {
        log.debug("REST request to changePass : {}", changePassDTO);
        boolean captchaVerified = captchaService.verify(changePassDTO.getRecaptchaReactive());
        if (!captchaVerified) {
            log.error(Constants.GOOGLE_RECAPTCHA_FALSE);
            throw new BadRequestAlertException(Translator.toLocale(Constants.GOOGLE_RECAPTCHA_FALSE), "google.recaptcha.false", "google.recaptcha.false");
        }
        userService.changePass(changePassDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/adminChangePass")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<Void> adminChangePass(@RequestBody ChangePassDTO changePassDTO) throws Exception {
        userService.adminChangePass(changePassDTO);
        return ResponseEntity.noContent().build();
    }


    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(String keyword, Pageable pageable) {
        System.out.println("Gia tri tu can tim:" + keyword);
        final Page<UserDTO> page = userService.getAllManagedUsers(keyword, null, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/getAll")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<List<UserDTO>> getAllUsersLst(String keyword, Pageable pageable) throws Exception {
        System.out.println("Gia tri tu can tim:" + keyword);
        PageRequest pageRequest = (PageRequest) pageable;
        Field field =  PageRequest.class.getSuperclass().getDeclaredField("size");
        field.setAccessible(true);
        field.set(pageRequest, Integer.MAX_VALUE);

        final Page<UserDTO> page = userService.getAllManagedUsers(keyword, null, pageRequest);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/users/query")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<List<UserDetailDTO>> queryUser(@RequestBody UserSearchDTO userSearchDTO, Pageable pageable) {
        final Page<UserDetailDTO> page = userService.queryUser(userSearchDTO, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/users/queryUserById")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<List<UserDetailDTO>> queryUserById(@RequestBody String id, Pageable pageable) {
        final Page<UserDetailDTO> page = userService.queryUserById(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasPermission('USER', '*')")
    public List<SysRoleDTO> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        Optional<UserDTO> userDTO = userService.getUserByLogin(login)
            .map(UserDTO::new);
        userDTO.ifPresent(user -> {
            List<String> domains = userDomainService.findAllByUserId(user.getId()).stream()
                .filter(Objects::nonNull).map(UserDomain::getDomainCode).collect(Collectors.toList());
            user.setDomains(new HashSet<>(domains));

            Set<Long> lstRole = sysUserRoleService.findAllByUserId(user.getId()).stream().filter(Objects::nonNull).map(SysUserRoleDTO::getRoleId).collect(Collectors.toSet());
            user.setAuthorities(lstRole);
        });
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasPermission('USER', '*')")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", login)).build();
    }

    @GetMapping("/users/notifications")
    public ResponseEntity<List<UserCustom>> getNotifications(String keyword, Pageable pageable) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        if (!user.isPresent() || !user.get().getActivated()) {
            throw new BadRequestAlertException(Translator.toLocale("error.isnotexisted"), "user", "error.isnotexisted");
        }
        Page<UserCustom> page = userService.getUserWithNotify(keyword, user.get().getId(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/getUserFromDataLog")
    public ResponseEntity<List<UserDTO>> getUserFromDataLog(String time, String tableName) {
        List<UserDTO> lstUserDTO = userService.getUserFromDataLog(Integer.parseInt(time), tableName);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(time));
        return ResponseEntity.ok().headers(headers).body(lstUserDTO);
    }

}
