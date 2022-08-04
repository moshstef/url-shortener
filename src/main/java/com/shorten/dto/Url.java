package com.shorten.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "urls")
public class Url {

    private String shortHash;
    private String realUrl;
    private String apiKey;
    private Date creationDate;

    public Url() {
    }

    public Url(String shortHash, String realUrl, String apiKey, Date creationDate) {
        this.shortHash = shortHash;
        this.realUrl = realUrl;
        this.apiKey = apiKey;
        this.creationDate = creationDate;
    }

    @Id
    @Column(name = "short_hash")
    public String getShortHash() {
        return shortHash;
    }

    public void setShortHash(String shortHash) {
        this.shortHash = shortHash;
    }

    @Column(name = "real_url")
    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    @Column(name = "api_key")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Column(name = "creation_date")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
