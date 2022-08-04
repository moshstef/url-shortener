package com.shorten.service;


import com.shorten.dto.Url;
import com.shorten.dto.User;
import com.shorten.exception.QuotaReachedException;
import io.quarkus.redis.client.RedisClient;
import io.vertx.redis.client.Response;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.LongStream;

@ApplicationScoped
public class ShortenService {

    private final ConcurrentLinkedDeque<Long> seeds = new ConcurrentLinkedDeque<>();

    private static final int QUOTA = 10000;
    private static final int RANGE_SIZE = 1000;
    private static final String BASE_URL = "http://localhost:8080/";

    @Inject
    SeedService seedService;

    @Inject
    UserService userService;
    @Inject
    UrlService urlService;

    @Inject
    RedisClient redisClient;

    @PostConstruct
    void onInit() throws SQLException {
        populateSeeds();
    }

    void populateSeeds() throws SQLException {
        Long seed = seedService.getSeed();
        ShortenUtils.Range range = ShortenUtils.range(seed, RANGE_SIZE);
        LongStream.range(range.start, range.end).forEach(seeds::add);
    }

    private Long getNextSeed() throws SQLException {
        if (seeds.isEmpty()) {
            populateSeeds();
        }
        return seeds.pop();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public String generateShortUrl(URL realUrl, String apiKey) throws Exception {
        User user = userService.loadUser(apiKey);
        if (user.getUrlsCount() >= QUOTA) {
            throw new QuotaReachedException();
        }
        String urlHash = ShortenUtils.encode(getNextSeed());
        urlService.persistUrl(new Url(urlHash, realUrl.toString(), apiKey, new Date()));
        userService.incrementUrlsCount(apiKey);
        redisClient.set(Arrays.asList(urlHash, realUrl.toString()));
        return BASE_URL + urlHash;
    }


    public String retrieveRealUrl(String urlHash) {
        Response response = redisClient.get(urlHash);
        if (response != null) {
            return response.toString();
        } else {
            Url url = urlService.loadUrl(urlHash);
            redisClient.set(Arrays.asList(urlHash, url.getRealUrl()));
            return url.getRealUrl();
        }
    }
}
