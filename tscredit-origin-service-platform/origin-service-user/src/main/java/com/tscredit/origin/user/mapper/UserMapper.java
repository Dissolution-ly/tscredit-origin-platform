package com.tscredit.origin.user.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.origin.user.entity.UserInfo;
import com.tscredit.origin.user.entity.dao.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<User> {

    Page<UserInfo> getUserPage(@Param("page") Page<UserInfo> page, @Param(Constants.WRAPPER) QueryWrapper<User> wrapper);



    UserInfo queryUserInfo(@Param(Constants.WRAPPER) QueryWrapper<User> wrapper);
}