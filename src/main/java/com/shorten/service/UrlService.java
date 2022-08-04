package com.shorten.service;

import com.shorten.dto.Url;
import com.shorten.exception.UnknownUrlException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class UrlService {

    @Inject
    EntityManager em;

    @Transactional(Transactional.TxType.REQUIRED)
    public void persistUrl(Url url) {
        em.persist(url);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Url loadUrl(String shortHash) {
        Url url = em.find(Url.class, shortHash);
        if (url == null) {
            throw new UnknownUrlException();
        }
        return url;
    }
}
