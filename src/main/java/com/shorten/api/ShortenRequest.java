package com.shorten.api;

import java.net.URL;

public class ShortenRequest {

    private URL url;

    public ShortenRequest() {
    }

    public ShortenRequest(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
