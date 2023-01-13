package com.tscredit.origin.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.UserQuota;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-12
 */
public interface UserQuotaService extends IService<UserQuota> {

    Page<UserQuota> pageList(Page<UserQuota> page, UserQuota userQuota);

    List<Map<String, Object>> getByUserId(String userId);

    Map<String,Object> getRedisQuota(String userId, String type);
    int verifyQuota(String userId, String type);

    int verifyQuota(String userId, String quotaId, Integer quota);

    void deductQuota(String userId, String type);

    void deductQuota(String userId, String type, Integer num);

    // 为指定用户 设置和另一个用户或角色相同的 额度配置
    boolean setQuotaByRole(Integer userId, String sourceId);

    int deleteQuotaByUserId(List<String> userId);

    void userOver(List<String> keys);

    List<String> getQuotaIds();

    void clearQuota(String type);

    List<String> getQuotaIdsByType(String type);
//    Map<String, Object> authority(String userId);
//
//    String setAuthority(String authority, String userId);
//
//
//    boolean deleteAuthorityByUserId(List<String> userId);



}
