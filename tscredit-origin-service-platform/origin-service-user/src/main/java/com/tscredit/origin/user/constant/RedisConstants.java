package com.tscredit.origin.user.constant;

public class RedisConstants {

    public static String getQuotaRedisKey(String userId) {
        return "quota:" + userId;
    }
}
