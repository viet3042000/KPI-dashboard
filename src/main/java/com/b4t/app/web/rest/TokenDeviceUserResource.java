package com.b4t.app.web.rest;

import com.b4t.app.service.TokenDeviceUserService;
import com.b4t.app.web.rest.errors.BadRequestAlertException;
import com.b4t.app.service.dto.TokenDeviceUserDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.b4t.app.domain.TokenDeviceUser}.
 */
@RestController
@RequestMapping("/api")
public class TokenDeviceUserResource {

    private final Logger log = LoggerFactory.getLogger(TokenDeviceUserResource.class);

    private static final String ENTITY_NAME = "tokenDeviceUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TokenDeviceUserService tokenDeviceUserService;

    public TokenDeviceUserResource(TokenDeviceUserService tokenDeviceUserService) {
        this.tokenDeviceUserService = tokenDeviceUserService;
    }

    /**
     * {@code POST  /token-device-users} : Create a new tokenDeviceUser.
     *
     * @param tokenDeviceUserDTO the tokenDeviceUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tokenDeviceUserDTO, or with status {@code 400 (Bad Request)} if the tokenDeviceUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/token-device-users")
    public ResponseEntity<TokenDeviceUserDTO> createTokenDeviceUser(@RequestBody TokenDeviceUserDTO tokenDeviceUserDTO) throws URISyntaxException {
        log.debug("REST request to save TokenDeviceUser : {}", tokenDeviceUserDTO);
        if (tokenDeviceUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new tokenDeviceUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TokenDeviceUserDTO result = tokenDeviceUserService.save(tokenDeviceUserDTO);
        return ResponseEntity.created(new URI("/api/token-device-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /token-device-users} : Updates an existing tokenDeviceUser.
     *
     * @param tokenDeviceUserDTO the tokenDeviceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tokenDeviceUserDTO,
     * or with status {@code 400 (Bad Request)} if the tokenDeviceUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tokenDeviceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/token-device-users")
    public ResponseEntity<TokenDeviceUserDTO> updateTokenDeviceUser(@RequestBody TokenDeviceUserDTO tokenDeviceUserDTO) throws URISyntaxException {
        log.debug("REST request to update TokenDeviceUser : {}", tokenDeviceUserDTO);
        if (tokenDeviceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TokenDeviceUserDTO result = tokenDeviceUserService.save(tokenDeviceUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tokenDeviceUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /token-device-users} : get all the tokenDeviceUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tokenDeviceUsers in body.
     */
    @GetMapping("/token-device-users")
    public ResponseEntity<List<TokenDeviceUserDTO>> getAllTokenDeviceUsers(String keyword, Long[] userIds, Long status, Pageable pageable) {
        log.debug("REST request to get a page of TokenDeviceUsers");
        Page<TokenDeviceUserDTO> page = tokenDeviceUserService.findAll(keyword, userIds, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /token-device-users/:id} : get the "id" tokenDeviceUser.
     *
     * @param id the id of the tokenDeviceUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tokenDeviceUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/token-device-users/{id}")
    public ResponseEntity<TokenDeviceUserDTO> getTokenDeviceUser(@PathVariable Long id) {
        log.debug("REST request to get TokenDeviceUser : {}", id);
        Optional<TokenDeviceUserDTO> tokenDeviceUserDTO = tokenDeviceUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tokenDeviceUserDTO);
    }

    /**
     * {@code DELETE  /token-device-users/:id} : delete the "id" tokenDeviceUser.
     *
     * @param id the id of the tokenDeviceUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/token-device-users/{id}")
    public ResponseEntity<Void> deleteTokenDeviceUser(@PathVariable Long id) {
        log.debug("REST request to delete TokenDeviceUser : {}", id);
        tokenDeviceUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
