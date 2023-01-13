package com.tscredit.origin.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.UserAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-16
 */
@Repository
public interface UserAuthorityMapper extends BaseMapper<UserAuthority> {


    List<String> authority(@Param("userId") String userId, @Param("authorityId") String authorityId);


    List<Map<String, Object>> userAuthority(@Param("userId") String userId);

    List<String> selectAllAuthority();
}
