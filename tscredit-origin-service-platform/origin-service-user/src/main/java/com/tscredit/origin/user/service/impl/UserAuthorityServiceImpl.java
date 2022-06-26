package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.common.util.JsonUtils;
import com.tscredit.origin.user.mapper.UserAuthorityMapper;
import com.tscredit.origin.user.entity.UserAuthority;
import com.tscredit.origin.user.service.UserAuthorityService;
import com.tscredit.platform.redis.config.RedisUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityMapper, UserAuthority> implements UserAuthorityService {

    private final UserAuthorityMapper userAuthorityMapper;
    private final RedisUtil redisUtil;

    public UserAuthorityServiceImpl(UserAuthorityMapper userAuthorityMapper, RedisUtil redisUtil) {
        this.userAuthorityMapper = userAuthorityMapper;
        this.redisUtil = redisUtil;
    }

    public QueryWrapper<UserAuthority> getWrapper(UserAuthority userAuthority) {
        QueryWrapper<UserAuthority> wrapper = new QueryWrapper<>();
        if (userAuthority == null) return null;

        //用户Id
        if (StringUtils.isNotBlank(userAuthority.getUserId())) {
            wrapper.eq("user_id", userAuthority.getUserId());
        }
        //权限Id
        if (StringUtils.isNotBlank(userAuthority.getAuthorityId())) {
            wrapper.eq("authority_id", userAuthority.getAuthorityId());
        }
        //权限内容
        if (StringUtils.isNotBlank(userAuthority.getAuthorityContent())) {
            wrapper.eq("authority_content", userAuthority.getAuthorityContent());
        }

        return wrapper;
    }


    @Override
    public Page<UserAuthority> pageList(Page<UserAuthority> page, UserAuthority userAuthority) {
        return page(page, getWrapper(userAuthority));
    }


    @Override
    public Map<Object, Object> userAuthority(String userId) {
        String rediskey = "authority:" + userId;

        Map<Object, Object> hmget = redisUtil.hmget(rediskey);
        // redis 中 获取不到，则查询数据库
        if (MapUtils.isEmpty(hmget)) {
            List<Map<String, Object>> authority = userAuthorityMapper.userAuthority(userId);
            // authorityId 为 key，authorityContent 为 value 的 Map
            // TODO map 的值是Map类型还是String类型
            hmget = authority.stream()
                    .collect(Collectors.toMap(map -> map.get("authorityId"), map -> map.get("authorityContent"), (key1, key2) -> key2));
            redisUtil.hmset(rediskey, hmget);
        }

        return hmget;
    }

    @Override
    public List<String> selectAllAuthority() {
        return userAuthorityMapper.selectAllAuthority();
    }

    @Override
    public boolean deleteAuthorityByUserIds(List<String> userIds) {
        // 删除数据库中数据
        QueryWrapper<UserAuthority> wrapper = new QueryWrapper<>();
        wrapper.in("user_id", userIds);
        boolean dbRemove = super.remove(wrapper);
        // 删除 redis 数据
        for (String userId : userIds) {
            String rediskey = "authority:" + userId;
            redisUtil.del(rediskey);
        }
        return dbRemove;
    }


    @Override
    public <T> Map<String, T> userAuthority(String userId, String authorityId) {
        Map<Object, Object> data = userAuthority(userId);
        String authority = MapUtils.getString(data, authorityId);
        Map<String, T> result = new HashMap<>();
        if (StringUtils.isNotBlank(authority)) {
            result = JsonUtils.convertJson2ObjectLogicException(authority, Map.class);
        }
        return result;
    }
}
