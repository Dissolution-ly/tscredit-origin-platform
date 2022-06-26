package com.tscredit.origin.user.config.constant;

public class RedisConstants {

    public static String getQuotaRedisKey(String userId) {
        return "quota:" + userId;
    }
}
