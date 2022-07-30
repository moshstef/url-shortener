package com.shorten.service;

import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

public class ShortenUtilsTest {

    @Test
    public void encodeTest() {
        Long rangeSize = 100000L;
        Long start = 100000L;

        Set<String> results = new HashSet<>();
        LongStream.range(start, start + rangeSize)
                .mapToObj(ShortenUtils::encode)
                .peek(s -> Assert.assertTrue(s.length() <= 7))
                .forEach(results::add);
        Assert.assertTrue(results.size() == rangeSize);

    }


    @Test
    public void testRange() {

        ShortenUtils.Range range = ShortenUtils.range(1L, 1000);
        Assert.assertTrue(range.start == 1000L);
        Assert.assertTrue(range.end == 1999L);

        ShortenUtils.Range range2 = ShortenUtils.range(2L, 1000);
        Assert.assertTrue(range2.start == 2000L);
        Assert.assertTrue(range2.end == 2999L);
    }
}
