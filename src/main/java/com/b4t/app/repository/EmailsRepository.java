package com.b4t.app.repository;

import com.b4t.app.domain.Emails;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Emails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailsRepository extends JpaRepository<Emails, Long> {

}
