package com.tscredit.origin.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tscredit.origin.user.entity.UserQuota;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface UserQuotaMapper extends BaseMapper<UserQuota> {

    List<Map<String, Object>> getByUserId(@Param("userId") String userId);

    List<Map<String, Object>> authority(String userId);

    String setAuthority(String authority, String userId);

    boolean setQuotaByRole(Integer userId, String sourceId);

    boolean deleteAuthorityByUserId(@Param(Constants.WRAPPER) Wrapper wrapper);

    boolean updateQuota(String userId, String quotaId, Integer use);

    List<String> getDayId();

    List<String> getQuotaIds();

    @Update("UPDATE urm_user_quota SET quota_use = 0 WHERE quota_id in (select id from urm_quota where type = #{type})")
    void clearQuota(String type);

    @Select("select id from urm_quota where type = #{type}")
    List<String> getQuotaIdsByType(String type);
}
