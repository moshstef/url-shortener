package com.shorten.service;


import com.shorten.dto.User;
import com.shorten.exception.QuotaReachedException;
import io.quarkus.redis.client.RedisClient;
import io.vertx.redis.client.Response;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.LongStream;

@ApplicationScoped
public class ShortenService {


    private final ConcurrentLinkedDeque<Long> seeds = new ConcurrentLinkedDeque<>();

    private static final int QUOTA = 10000;
    private static final int RANGE_SIZE = 1000;
    private static final String BASE_URL = "http://localhost:8080/";

    @Inject
    ShortenDao dao;

    @Inject
    RedisClient redisClient;

    @PostConstruct
    void onInit() throws SQLException {
        populateSeeds();
    }

    void populateSeeds() throws SQLException {
        Long seed = dao.getSeed();
        ShortenUtils.Range range = ShortenUtils.range(seed, RANGE_SIZE);
        LongStream.range(range.start, range.end).forEach(seeds::add);
    }

    private Long getNextSeed() throws SQLException {
        if (seeds.isEmpty()) {
            populateSeeds();
        }
        return seeds.pop();
    }

    public String generateShortUrl(URL realUrl, String apiKey) throws Exception {
        User user = dao.getUser(apiKey);
        if (user.getUrlsCount() >= QUOTA) {
            throw new QuotaReachedException();
        }
        String urlHash = ShortenUtils.encode(getNextSeed());
        dao.createUserUrl(urlHash, realUrl.toString(), apiKey);
        redisClient.set(Arrays.asList(urlHash, realUrl.toString()));
        return BASE_URL + urlHash;
    }


    public String retrieveRealUrl(String urlHash) throws Exception {
        Response response = redisClient.get(urlHash);
        if (response.toString() != null) {
            return response.toString();
        } else {
            String realUrl = dao.loadRealUrl(urlHash);
            redisClient.set(Arrays.asList(urlHash, realUrl));
            return realUrl;
        }
    }
}
