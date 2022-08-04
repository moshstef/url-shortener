package com.shorten.service;

import com.shorten.dto.User;
import com.shorten.exception.UserNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    EntityManager em;


    @Transactional(Transactional.TxType.SUPPORTS)
    public User loadUser(String apiKey) {
        User user = em.find(User.class, apiKey);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void incrementUrlsCount(String apiKey) {
        em.createQuery("UPDATE User set urlsCount = urlsCount+1 where apiKey = :apiKey").setParameter("apiKey", apiKey).executeUpdate();
    }
}
