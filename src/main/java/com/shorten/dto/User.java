package com.shorten.dto;

public class User {
    private String apiKey;
    private int urlsCount;

    public User(String apiKey, int urlsCount) {
        this.apiKey = apiKey;
        this.urlsCount = urlsCount;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getUrlsCount() {
        return urlsCount;
    }

    public void setUrlsCount(int urlsCount) {
        this.urlsCount = urlsCount;
    }
}
