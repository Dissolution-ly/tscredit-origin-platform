package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.User;


public interface UserService extends IService<User> {

    // 根据用户名称获取用户
    User getUserByName(String loginName, String id);

    /**
     * 分页查询
     */
    Object pageList(Page<User> page, User user);


    void saveWxUserInfo(String openid, String userInfo);

    User getUserByOpenId(String openId, String id);

    public User loadUserByUsername(String username);
}
