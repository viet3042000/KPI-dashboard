package com.b4t.app.service.impl;

import com.b4t.app.commons.DataUtil;
import com.b4t.app.config.Constants;
import com.b4t.app.domain.Emails_;
import com.b4t.app.service.EmailsService;
import com.b4t.app.domain.Emails;
import com.b4t.app.repository.EmailsRepository;
import com.b4t.app.service.dto.EmailsDTO;
import com.b4t.app.service.mapper.EmailsMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Emails}.
 */
@Service
@Transactional
public class EmailsServiceImpl implements EmailsService {

    private final Logger log = LoggerFactory.getLogger(EmailsServiceImpl.class);

    private final EmailsRepository emailsRepository;

    private final EmailsMapper emailsMapper;

    private final EntityManager entityManager;

    public EmailsServiceImpl(
        EmailsRepository emailsRepository,
        EmailsMapper emailsMapper,
        EntityManager entityManager) {
        this.emailsRepository = emailsRepository;
        this.emailsMapper = emailsMapper;
        this.entityManager = entityManager;
    }

    /**
     * Save a emails.
     *
     * @param emailsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EmailsDTO save(EmailsDTO emailsDTO) {
        log.debug("Request to save Emails : {}", emailsDTO);
        Emails emails = emailsMapper.toEntity(emailsDTO);
        emails.setCreateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS));
        emails = emailsRepository.save(emails);
        return emailsMapper.toDto(emails);
    }

    @Override
    public List<EmailsDTO> saveAll(List<EmailsDTO> dtos) {
        log.debug("Request to save Emails : {}", dtos);
        if (DataUtil.isNullOrEmpty(dtos)) return new ArrayList<>();
        List<Emails> emails = emailsMapper.toEntity(dtos);
        emails = emails.stream()
            .peek(i -> i.setCreateTime(Instant.now().truncatedTo(ChronoUnit.MILLIS)))
            .collect(Collectors.toList());
        emails = emailsRepository.saveAll(emails);
        return emailsMapper.toDto(emails);
    }

    /**
     * Get all the emails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmailsDTO> findAll(String keyword, Long[] notifyIds, Long[] userIds, Long status, Long isRepeat, Pageable pageable) {
        log.debug("Request to get all Emails");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Emails> criteria = cb.createQuery(Emails.class);
        Root<Emails> root = criteria.from(Emails.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Emails> rootCount = countQuery.from(Emails.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotEmpty(keyword)) {
            keyword = DataUtil.makeLikeParam(keyword);
            Predicate likePredicate = cb.or(
                cb.like(cb.lower(root.get(Emails_.EMAIL)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(Emails_.SUBJECT)), keyword, Constants.DEFAULT_ESCAPE_CHAR),
                cb.like(cb.lower(root.get(Emails_.CONTENT)), keyword, Constants.DEFAULT_ESCAPE_CHAR)
            );
            predicates.add(likePredicate);
        }
        if (!DataUtil.isNullOrEmpty(notifyIds)) {
            predicates.add(root.get(Emails_.NOTIFY_ID).in(notifyIds));
        }
        if (!DataUtil.isNullOrEmpty(userIds)) {
            predicates.add(root.get(Emails_.USER_ID).in(userIds));
        }
        if (isRepeat != null) {
            predicates.add(cb.equal(root.get(Emails_.IS_REPEAT), isRepeat));
        }
        if (status != null) {
            predicates.add(cb.equal(root.get(Emails_.STATUS), status));
        }
        criteria.select(root).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        List<Emails> rs = entityManager.createQuery(criteria)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        countQuery.select(cb.count(rootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = entityManager.createQuery(countQuery).getSingleResult();
        List<EmailsDTO> rsDTOs = rs.stream().map(emailsMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, count);
    }

    /**
     * Get one emails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmailsDTO> findOne(Long id) {
        log.debug("Request to get Emails : {}", id);
        return emailsRepository.findById(id)
            .map(emailsMapper::toDto);
    }

    /**
     * Delete the emails by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Emails : {}", id);
        emailsRepository.deleteById(id);
    }
}
