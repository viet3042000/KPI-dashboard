package com.b4t.app.service;

import com.b4t.app.domain.UserDomain;
import com.b4t.app.repository.UserDomainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tamdx
 */
@Service
@Transactional
public class UserDomainService {
    private final Logger logger = LoggerFactory.getLogger(UserDomainService.class);

    private final UserDomainRepository userDomainRepository;

    public UserDomainService(UserDomainRepository userDomainRepository) {
        this.userDomainRepository = userDomainRepository;
    }
    public List<UserDomain> findAllByUserId(Long userId){
        return userDomainRepository.findAllByUserId(userId);
    }

    public void deleteDomain(Long userId){
        List<UserDomain> lstUserDomain = this.userDomainRepository.findAllByUserId(userId);
        if(lstUserDomain != null){
            this.userDomainRepository.deleteAll(lstUserDomain);
        }
    }
    public void createUserDomain(Long userId, Set<String> lstDomain){
        if(lstDomain != null && userId != null){
            List<UserDomain> lstUserDomain = lstDomain.stream().map(e->{
                UserDomain userDomain = new UserDomain();
                userDomain.setUserId(userId);
                userDomain.setDomainCode(e);
                return userDomain;
            }).collect(Collectors.toList());
            this.userDomainRepository.saveAll(lstUserDomain);
        }
    }

}
