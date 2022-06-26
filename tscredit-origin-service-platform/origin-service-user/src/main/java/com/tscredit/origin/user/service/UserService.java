package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.UserInfo;
import com.tscredit.origin.user.entity.dao.User;
import com.tscredit.origin.user.entity.dto.CreateUserDTO;
import com.tscredit.origin.user.entity.dto.QueryUserDTO;
import com.tscredit.origin.user.entity.dto.UpdateUserDTO;

import java.util.List;


public interface UserService extends IService<User> {

    boolean createUser(CreateUserDTO user);

    boolean updateUser(UpdateUserDTO updateUserDTO);

    boolean batchDeleteUser(List<Long> userIds);

    User getUserByLoginInfo(User user);

    Page<UserInfo> pageList(Page<UserInfo> page, QueryUserDTO user);

    UserInfo queryUserInfo(QueryUserDTO queryUserDTO);

}
