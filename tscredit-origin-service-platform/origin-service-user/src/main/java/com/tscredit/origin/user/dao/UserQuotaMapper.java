package com.tscredit.origin.user.dao;

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

/**
 * @author lixuanyu
 * @since 2021-08-12
 */
@Repository
public interface UserQuotaMapper extends BaseMapper<UserQuota> {

    List<Map<String, Object>> getByUserId(@Param("userId") String userId);

    List<Map<String, Object>> authority(@Param("userId") String userId);

    String setAuthority(@Param("authority") String authority, @Param("userId") String userId);

    boolean setQuotaByRole(@Param("userId") Integer userId, @Param("sourceId") String sourceId);

    boolean deleteAuthorityByUserId(@Param(Constants.WRAPPER) Wrapper wrapper);

    boolean updateQuota(@Param("userId") String userId, @Param("quotaId") String quotaId, @Param("use") Integer use);

    List<String> getDayId();

    List<String> getQuotaIds();

    @Update("UPDATE urm_user_quota SET quota_use = 0 WHERE quota_id in (select id from urm_quota where type = #{type})")
    void clearQuota(@Param("type") String type);

    @Select("select id from urm_quota where type = #{type}")
    List<String> getQuotaIdsByType(@Param("type") String type);
}
