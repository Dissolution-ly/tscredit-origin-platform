package com.tscredit.origin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.UserAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface UserAuthorityMapper extends BaseMapper<UserAuthority> {



    List<String> authority(String userId, String authorityId);


    List<Map<String, Object>> userAuthority(String userId);

    List<String> selectAllAuthority();
}
