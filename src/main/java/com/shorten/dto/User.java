package com.shorten.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    private String apiKey;
    private int urlsCount;

    public User() {

    }

    public User(String apiKey, int urlsCount) {
        this.apiKey = apiKey;
        this.urlsCount = urlsCount;
    }

    @Id
    @Column(name = "api_key")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Column(name = "urls_count")
    public int getUrlsCount() {
        return urlsCount;
    }

    public void setUrlsCount(int urlsCount) {
        this.urlsCount = urlsCount;
    }
}
