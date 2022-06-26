package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.tscredit.common.response.ErrorMessage;
import com.tscredit.common.response.LogicException;
import com.tscredit.origin.user.config.constant.RedisConstants;
import com.tscredit.origin.user.mapper.UserQuotaMapper;
import com.tscredit.origin.user.entity.UserQuota;
import com.tscredit.origin.user.service.UserQuotaService;
import com.tscredit.platform.redis.config.RedisUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserQuotaServiceImpl extends ServiceImpl<UserQuotaMapper, UserQuota> implements UserQuotaService {

    private final UserQuotaMapper userQuotaMapper;

    private final RedisUtil redisUtil;

    public UserQuotaServiceImpl(UserQuotaMapper userQuotaMapper, RedisUtil redisUtil) {
        this.userQuotaMapper = userQuotaMapper;
        this.redisUtil = redisUtil;
    }

    public QueryWrapper<UserQuota> getWrapper(UserQuota userQuota) {
        QueryWrapper<UserQuota> wrapper = new QueryWrapper<>();
        if (userQuota == null) {
            return null;
        }

        //
        if (userQuota.getId() != null) {
            wrapper.eq("id", userQuota.getId());
        }
        //用户/角色 id
        if (userQuota.getUserId() != null) {
            wrapper.eq("user_id", userQuota.getUserId());
        }
        //额度id
        if (userQuota.getQuotaId() != null) {
            wrapper.eq("quota_id", userQuota.getQuotaId());
        }
        //总额度
        if (userQuota.getQuotaTotal() != null) {
            wrapper.eq("quota_total", userQuota.getQuotaTotal());
        }
        //已用额度
        if (userQuota.getQuotaUse() != null) {
            wrapper.eq("quota_use", userQuota.getQuotaUse());
        }

        return wrapper;
    }


    @Override
    public Page<UserQuota> pageList(Page<UserQuota> page, UserQuota userQuota) {
        return page(page, getWrapper(userQuota));
    }

    @Override
    public List<Map<String, Object>> getByUserId(String userId) {
        // 同步对应用户 redis数据 到 数据库
        this.userOver(Lists.newArrayList(RedisConstants.getQuotaRedisKey(userId)));
        // 查询 用户额度
        return userQuotaMapper.getByUserId(userId);
    }

    @Override
    public Map<String, Object> getRedisQuota(String userId, String quotaId) {
        Integer surplusQuota = (Integer) redisUtil.hget(RedisConstants.getQuotaRedisKey(userId), quotaId);
        Integer totalQuota = (Integer) redisUtil.hget(RedisConstants.getQuotaRedisKey(userId), quotaId + "@total");
        if (surplusQuota == null) {
            // 数据库 获取总额度 和 剩余额度
            UserQuota userQuota = null;
            List<Map<String, Object>> userAllQuota = userQuotaMapper.getByUserId(userId);
            for (Map<String, Object> map : userAllQuota) {
                if (MapUtils.getString(map, "quotaId", "").equals(quotaId)) {
                    userQuota = new UserQuota();
                    userQuota.setUserId(userId);
                    userQuota.setQuotaId(quotaId);
                    userQuota.setId(MapUtils.getString(map, "id"));
                    userQuota.setQuotaTotal(MapUtils.getInteger(map, "quotaTotal"));
                    userQuota.setQuotaUse(MapUtils.getInteger(map, "quotaUse"));
                }
            }
            if (userQuota == null) {
                throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR).setCode("6379").setErrorMsg("要获取的额度数据不存在");
            }

            // 将信息保存到 redis TODO 需要原子操作
            totalQuota = userQuota.getQuotaTotal() == null ? 0 : userQuota.getQuotaTotal();
            int useQuota = userQuota.getQuotaUse() == null ? 0 : userQuota.getQuotaUse();
            surplusQuota = totalQuota - useQuota;
            redisUtil.hset(RedisConstants.getQuotaRedisKey(userId), quotaId, surplusQuota);
            redisUtil.hset(RedisConstants.getQuotaRedisKey(userId), quotaId + "@total", totalQuota);
            redisUtil.hset(RedisConstants.getQuotaRedisKey(userId), quotaId + "@id", userQuota.getId());
        }

        Map<String, Object> result = new HashMap<>(8);
        result.put("surplusQuota", surplusQuota);
        result.put("totalQuota", totalQuota);
        return result;
    }

    @Override
    public int verifyQuota(String userId, String quotaId, Integer quota) {
        Map<String, Object> redisQuota = getRedisQuota(userId, quotaId);
        Integer surplusQuota = MapUtils.getInteger(redisQuota, "surplusQuota");
        Integer totalQuota = MapUtils.getInteger(redisQuota, "totalQuota");
        if (totalQuota != -1 && surplusQuota < quota) {
            throw LogicException.errorMessage(ErrorMessage.USER_QUOTA);
        }
        return surplusQuota;
    }

    @Override
    public int verifyQuota(String userId, String quotaId) {
        return verifyQuota(userId, quotaId, 1);
    }


    @Override
    public void deductQuota(String userId, String quotaId) {
        deductQuota(userId, quotaId, -1);
    }

    @Override
    public void deductQuota(String userId, String quotaId, Integer num) {
        String key = RedisConstants.getQuotaRedisKey(userId);

        // 获取并更新额度
        int count = 1;
        while (true) {
            long i = redisUtil.executeLua("redis/updateUserOver.lua", Lists.newArrayList(key, quotaId, "QUOTA:changeContent"), num);
            if (i == 0) {
                // 成功则跳出循环
                break;
            } else if (i == 2000 || i == 1500 || i == 1100) {
                // 接口额度 达到预警值，1000、500、100，考虑发送企业微信或邮件等操作
                break;
            } else if (i == 11) {
                if (count++ > 3) {
                    // 防止死循环
                    throw LogicException.errorMessage(ErrorMessage.USER_QUOTA);
                }
                // 查询不到总额度或天额度，则重新查询
                getRedisQuota(userId, quotaId);
            } else if (i == 12) {
                // 额度不足
                throw LogicException.errorMessage(ErrorMessage.USER_QUOTA);
            } else if (i == -1) {
                // num 为空
                throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
            }
        }
    }

    @Override
    public void clearQuota(String type) {
        userQuotaMapper.clearQuota(type);
    }

    @Override
    public List<String> getQuotaIdsByType(String type) {
        return userQuotaMapper.getQuotaIdsByType(type);
    }


    @Override
    public boolean setQuotaByRole(Integer userId, String sourceId) {
        return userQuotaMapper.setQuotaByRole(userId, sourceId);
    }

    @Override
    public int deleteQuotaByUserId(List<String> userIds) {
        QueryWrapper<UserQuota> wrapper = new QueryWrapper<>();
        wrapper.in("user_id", userIds);
        return userQuotaMapper.delete(wrapper);
    }

    /**
     * redis 信息同步到数据库
     *
     * @param keys redis的Key
     */
    public void userOver(List<String> keys) {
        try {
            for (String key : keys) {
                String userId = key.replace("quota:", "");
                Map<Object, Object> quota = redisUtil.hmget(key);
                // 根据类型分组
                Map<String, List<Object>> total = quota.keySet().stream()
                        .collect(Collectors.groupingBy(s -> String.valueOf(s).replace("@total", "").replace("@id", "")));
                for (String quotaId : total.keySet()) {
                    Integer surplusQuota = MapUtils.getInteger(quota, quotaId);
                    Integer totalQuota = MapUtils.getInteger(quota, quotaId + "@total");
                    String userQuotaId = MapUtils.getString(quota, quotaId + "@id");
                    // 更新数据库额度
                    UserQuota userQuota = new UserQuota();
                    userQuota.setId(userQuotaId);
                    userQuota.setUserId(userId);
                    userQuota.setQuotaId(quotaId);
                    userQuota.setQuotaUse(totalQuota - surplusQuota);
                    userQuota.setQuotaTotal(totalQuota);
                    this.saveOrUpdate(userQuota);
                    // redis 第一次获取该值，数据库中还没有该条数据(上一句执行的新增操作)，则将数据库 id 回填到 redis
                    if(StringUtils.isEmpty(userQuotaId)){
                        redisUtil.hset(key, quotaId + "@id", userQuota.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("额度数据执行异常(每五分钟一次)：");
            log.error(getTrace(e));
        }
    }

    @Override
    public List<String> getQuotaIds() {
        return userQuotaMapper.getQuotaIds();
    }


    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }


}
