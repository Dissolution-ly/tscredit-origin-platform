package com.tscredit.origin.user.task;

import com.aurora.redis.config.RedisUtil;
import com.google.common.collect.Lists;
import com.tscredit.origin.user.config.Constants;
import com.tscredit.origin.user.service.UserQuotaService;
import com.tscredit.origin.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty("user.quota.check.enable")
public class UserOverTask {

    private final RedisUtil redisUtil;
    private final UserQuotaService userQuotaService;
    private final UserService userService;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final DefaultRedisScript<List> removeAll;

    public UserOverTask(RedisUtil redisUtil, UserQuotaService userQuotaService, UserService userService, DefaultRedisScript<List> removeAll) {
        this.redisUtil = redisUtil;
        this.userQuotaService = userQuotaService;
        this.userService = userService;
        this.removeAll = removeAll;
    }

    /**
     * 每五分钟执行一次 (更新数据库额度)
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void userOverUpdateTask() {
        synchronized (this) {
            log.info("更新数据库额度定时任务时间: " + simpleDateFormat.format(new Date()));
            List<String> keys = redisUtil.execute(removeAll, Lists.newArrayList(Constants.QUOAT_PREFIX + "changeContent"));
            if (CollectionUtils.isNotEmpty(keys)) {
                userQuotaService.userOver(keys);
            }
        }

    }

    /**
     * 每天执行一次 (清空天使用额度)
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void clearDayQuota() {
        // 任何操作 redis 额度相关数据的 同步操作，都不应该与此方法同步执行
        synchronized (this) {
            try {
                String format = simpleDateFormat.format(new Date());
                log.info("更新数据库天使用量定时任务时间: " + format);

                // STEP1：redis数据库置0
                // 此操作需要原子操作，同时，该操作也是时间分割线，该操作之前的数据认为是前一天的，之后的数据认为是新一天的
                List<String> userIds = userService.list().stream().map(user -> user.getId().toString()).collect(Collectors.toList());
                List<String> quotaIds = userQuotaService.getQuotaIdsByType("day");
                // Object execute = redisUtil.execute(resetZero, userIds, quotaIds);
                for (String userId : userIds) {
                    for (String quotaId : quotaIds) {
                        Integer max = (Integer) redisUtil.hget(Constants.QUOAT_PREFIX + userId, quotaId + "@total");
                        if (max != null) {
                            redisUtil.hset(Constants.QUOAT_PREFIX + userId, quotaId, max);
                        }
                    }
                }
                // STEP2 ：数据库中按天统计的额度 全部置0
                userQuotaService.clearQuota("day");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
