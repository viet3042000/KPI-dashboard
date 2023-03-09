package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.TokenDeviceUser_;
import com.b4t.app.service.TokenDeviceUserService;
import com.b4t.app.domain.TokenDeviceUser;
import com.b4t.app.repository.TokenDeviceUserRepository;
import com.b4t.app.service.dto.TokenDeviceUserDTO;
import com.b4t.app.service.mapper.TokenDeviceUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TokenDeviceUser}.
 */
@Service
@Transactional
public class TokenDeviceUserServiceImpl implements TokenDeviceUserService {

    private final Logger log = LoggerFactory.getLogger(TokenDeviceUserServiceImpl.class);

    private final TokenDeviceUserRepository tokenDeviceUserRepository;

    private final TokenDeviceUserMapper tokenDeviceUserMapper;

    private final EntityManager entityManager;

    public TokenDeviceUserServiceImpl(
        TokenDeviceUserRepository tokenDeviceUserRepository,
        TokenDeviceUserMapper tokenDeviceUserMapper,
        EntityManager entityManager) {
        this.tokenDeviceUserRepository = tokenDeviceUserRepository;
        this.tokenDeviceUserMapper = tokenDeviceUserMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a tokenDeviceUser.
     *
     * @param tokenDeviceUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TokenDeviceUserDTO save(TokenDeviceUserDTO tokenDeviceUserDTO) {
        log.debug("Request to save TokenDeviceUser : {}", tokenDeviceUserDTO);
        TokenDeviceUser tokenDeviceUser = tokenDeviceUserMapper.toEntity(tokenDeviceUserDTO);
        tokenDeviceUser = tokenDeviceUserRepository.save(tokenDeviceUser);
        return tokenDeviceUserMapper.toDto(tokenDeviceUser);
    }

    /**
     * Get all the tokenDeviceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TokenDeviceUserDTO> findAll(String keyword, Long[] userIds, Long status, Pageable pageable) {
        log.debug("Request to get all TokenDeviceUsers");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TokenDeviceUser> criteria = cb.createQuery(TokenDeviceUser.class);
        Root<TokenDeviceUser> root = criteria.from(TokenDeviceUser.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate likePredicate = cb.or(
                cb.like(cb.lower(root.get(TokenDeviceUser_.DEVICE_NAME)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(TokenDeviceUser_.TOKEN_DEVICE)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(likePredicate);
        }
        if (!DataUtil.isNullOrEmpty(userIds)) {
            predicates.add(root.get(TokenDeviceUser_.USER_ID).in(userIds));
        }
        status = status != null ? status : Constants.STATUS_ACTIVE;
        predicates.add(cb.equal(root.get(TokenDeviceUser_.STATUS), status));
        criteria.select(root).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        List<TokenDeviceUser> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
        List<TokenDeviceUserDTO> rsDTOs = rs.stream().map(tokenDeviceUserMapper::toDto).collect(Collectors.toList());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TokenDeviceUser> rootCount = countQuery.from(TokenDeviceUser.class);
        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(rsDTOs, pageable, count);
    }

    /**
     * Get one tokenDeviceUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TokenDeviceUserDTO> findOne(Long id) {
        log.debug("Request to get TokenDeviceUser : {}", id);
        return tokenDeviceUserRepository.findById(id)
            .map(tokenDeviceUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<TokenDeviceUserDTO>> findAllByUserIdInAndTokenDevice(String userId, String tokenDevice) {
        log.debug("Request to get TokenDeviceUser : {}", userId);
        return tokenDeviceUserRepository.findAllByUserIdAndTokenDevice(userId, tokenDevice)
            .map(tokenDeviceUserMapper::toDto);
    }

    /**
     * Delete the tokenDeviceUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TokenDeviceUser : {}", id);
        tokenDeviceUserRepository.deleteById(id);
    }
}
