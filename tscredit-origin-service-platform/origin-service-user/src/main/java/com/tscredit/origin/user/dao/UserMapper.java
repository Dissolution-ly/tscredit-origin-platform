package com.tscredit.origin.user.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tscredit.origin.user.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<User> {

    void saveWxUserInfo(@Param("openid") String openid, @Param("userInfo") String userInfo);

    User loadUserByUsername(@Param(Constants.WRAPPER) QueryWrapper<User> wrapper);
}