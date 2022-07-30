package com.shorten.service;

public class ShortenUtils {

    public final static String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(long number) {
        StringBuilder stringBuilder = new StringBuilder(1);
        do {
            stringBuilder.insert(0, BASE62_CHARS.charAt((int) (number % 62)));
            number /= 62;
        } while (number > 0);
        return stringBuilder.toString();
    }

    static class Range {
        Long start;
        Long end;

        public Range(Long start, Long end) {
            this.start = start;
            this.end = end;
        }

        public Long getStart() {
            return start;
        }

        public Long getEnd() {
            return end;
        }
    }

    public static Range range(Long seed, int rangeSize) {
        return new Range(seed * rangeSize, seed * rangeSize + rangeSize - 1);
    }
}
