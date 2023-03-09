package com.b4t.app.repository;

import com.b4t.app.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author tamdx
 */
public interface UserDomainRepository extends JpaRepository<UserDomain, Long> {

    List<UserDomain> findAllByUserId(Long userId);

}
