package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.*;
import com.b4t.app.security.SecurityUtils;
import com.b4t.app.service.FavoriteUrlService;
import com.b4t.app.repository.FavoriteUrlRepository;
import com.b4t.app.service.dto.CatGroupChartDTO;
import com.b4t.app.service.dto.FavoriteUrlDTO;
import com.b4t.app.service.mapper.FavoriteUrlMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link FavoriteUrl}.
 */
@Service
@Transactional
public class FavoriteUrlServiceImpl implements FavoriteUrlService {

    private final Logger log = LoggerFactory.getLogger(FavoriteUrlServiceImpl.class);

    private final FavoriteUrlRepository favoriteUrlRepository;

    private final FavoriteUrlMapper favoriteUrlMapper;

    private final EntityManager entityManager;

    public FavoriteUrlServiceImpl(FavoriteUrlRepository favoriteUrlRepository, FavoriteUrlMapper favoriteUrlMapper, EntityManager entityManager) {
        this.favoriteUrlRepository = favoriteUrlRepository;
        this.favoriteUrlMapper = favoriteUrlMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a favoriteUrl.
     *
     * @param favoriteUrlDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FavoriteUrlDTO save(FavoriteUrlDTO favoriteUrlDTO) {
        log.debug("Request to save FavoriteUrl : {}", favoriteUrlDTO);
        Optional<List<FavoriteUrl>> optional = favoriteUrlRepository.findAllByNameAndUrlLink(favoriteUrlDTO.getName(), favoriteUrlDTO.getUrlLink());
        if (optional.isPresent()) {
            return favoriteUrlMapper.toDto(optional.get().get(0));
        }
        FavoriteUrl favoriteUrl = favoriteUrlMapper.toEntity(favoriteUrlDTO);
        favoriteUrl.setUpdateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        favoriteUrl.setUpdateUser(SecurityUtils.getCurrentUserLogin().get());
        favoriteUrl = favoriteUrlRepository.save(favoriteUrl);
        return favoriteUrlMapper.toDto(favoriteUrl);
    }

    /**
     * Get all the favoriteUrls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteUrlDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FavoriteUrls");
        return favoriteUrlRepository.findAll(pageable)
            .map(favoriteUrlMapper::toDto);
    }

    public Page<FavoriteUrlDTO> findAll(FavoriteUrlDTO favoriteUrlDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FavoriteUrl> criteria = cb.createQuery(FavoriteUrl.class);
        CriteriaQuery<Long> countCrit = cb.createQuery(Long.class);
        Root<FavoriteUrl> root = criteria.from(FavoriteUrl.class);
        Root<FavoriteUrl> rootCount = countCrit.from(FavoriteUrl.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(favoriteUrlDTO.getName())) {
            favoriteUrlDTO.setName(DataUtil.makeLikeParam(favoriteUrlDTO.getName()));
            Predicate keywordWCls = cb.or(
                cb.like(cb.lower(root.get(FavoriteUrl_.NAME)), favoriteUrlDTO.getName(), Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(FavoriteUrl_.URL_LINK)), favoriteUrlDTO.getName(), Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(keywordWCls);
            countPredicates.add(keywordWCls);
        }
        countPredicates.add(cb.equal(root.get(FavoriteUrl_.UPDATE_USER), SecurityUtils.getCurrentUserLogin().get()));
        predicates.add(cb.equal(root.get(FavoriteUrl_.UPDATE_USER), SecurityUtils.getCurrentUserLogin().get()));

        countCrit.where(cb.and(countPredicates.toArray(new Predicate[countPredicates.size()])));
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        countCrit.select(cb.countDistinct(rootCount));
        List<FavoriteUrl> rs = entityManager.createQuery(criteria).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        Long count = entityManager.createQuery(countCrit).getSingleResult();
        List<FavoriteUrlDTO> rsDTOs = favoriteUrlMapper.toDto(rs);
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    @Transactional(readOnly = true)
    public Optional<FavoriteUrlDTO> findByUrlAndUserLogin(FavoriteUrlDTO favoriteUrlDTO) {
        FavoriteUrlDTO favoriteUrl = null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FavoriteUrl> criteria = cb.createQuery(FavoriteUrl.class);
        Root<FavoriteUrl> root = criteria.from(FavoriteUrl.class);
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(favoriteUrlDTO.getUrlLink())) {
            predicates.add(cb.equal(root.get(FavoriteUrl_.URL_LINK), favoriteUrlDTO.getUrlLink()));
            predicates.add(cb.equal(root.get(FavoriteUrl_.UPDATE_USER), SecurityUtils.getCurrentUserLogin().get()));
        }
        criteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        List<FavoriteUrl> rs = entityManager.createQuery(criteria).getResultList();
        if (!DataUtil.isNullOrEmpty(rs)) {
            favoriteUrl = favoriteUrlMapper.toDto(rs.get(0));
        }
        Optional<FavoriteUrlDTO> fOptional = Optional.ofNullable(favoriteUrl);
        return fOptional;
    }

    /**
     * Get one favoriteUrl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteUrlDTO> findOne(Long id) {
        log.debug("Request to get FavoriteUrl : {}", id);
        return favoriteUrlRepository.findById(id)
            .map(favoriteUrlMapper::toDto);
    }

    /**
     * Delete the favoriteUrl by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FavoriteUrl : {}", id);
        favoriteUrlRepository.deleteById(id);
    }
}
