package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.dao.UserMapper;
import com.tscredit.origin.user.entity.User;
import com.tscredit.origin.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public QueryWrapper<User> getWrapper(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        // 角色ID
        if (!StringUtils.isEmpty(user.getRoleId())) {
            wrapper.eq("urm_user.role_id", user.getRoleId());
        }
        //用户id
        if (!StringUtils.isEmpty(user.getId())) {
            wrapper.eq("urm_user.fu_id", user.getId());
        }
        //用户名称
        if (!StringUtils.isEmpty(user.getLoginName())) {
            wrapper.like("urm_user.fu_loginname", user.getLoginName());
        }
        //密码
        if (!StringUtils.isEmpty(user.getPassword())) {
            wrapper.eq("urm_user.fu_password", user.getPassword());
        }
        // 企业名称
        if (!StringUtils.isEmpty(user.getEntName())) {
            wrapper.like("urm_user.fu_ent_name", user.getEntName());
        }
        //手机号
        if (!StringUtils.isEmpty(user.getMobile())) {
            wrapper.like("urm_user.fu_mobile", user.getMobile());
        }
        //用户状态
        if (!StringUtils.isEmpty(user.getUserstatus())) {
            wrapper.eq("urm_user.fu_userstatus", user.getUserstatus());
        }
        //是否停用
        if (!StringUtils.isEmpty(user.getStatus())) {
            wrapper.eq("urm_user.fu_status", user.getStatus());
        }
        //开始时间
        if (!StringUtils.isEmpty(user.getOpentime())) {
            wrapper.apply("to_days(urm_user.fu_opentime) = to_days({0})", user.getOpentime());
        }
        //过期时间
        if (!StringUtils.isEmpty(user.getExpiredtime())) {
            wrapper.apply("to_days(urm_user.fu_expiredtime) = to_days({0})", user.getExpiredtime());
        }
        //联系人
        if (!StringUtils.isEmpty(user.getCnname())) {
            wrapper.like("urm_user.fu_cnname", user.getCnname());
        }
        //用户角色
        if (!StringUtils.isEmpty(user.getRoleCode())) {
            wrapper.eq("role_code", user.getRoleCode());
        }
        wrapper.eq("urm_user.fu_status", 1);
        return wrapper;
    }

    /**
     * 根据用户名称返回用户信息(userId 不为null,则排除 指定用户Id的用户)
     */
    @Override
    public User getUserByName(String userName, String id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("urm_user.fu_loginname", userName);
        wrapper.eq("urm_user.fu_status", "1");
        if (!StringUtils.isEmpty(id)) {
            wrapper.ne("urm_user.fu_id", id);
        }

        return this.getOne(wrapper, false);
    }

    @Override
    public User getUserByOpenId(String openId, String id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("urm_user.fu_open_id", openId);
        wrapper.eq("urm_user.fu_status", "1");
        if (!StringUtils.isEmpty(id)) {
            wrapper.ne("fu_id", id);
        }

        return this.getOne(wrapper, false);
    }

    @Override
    public Object pageList(Page<User> page, User user) {
        QueryWrapper<User> wrapper = getWrapper(user);
        wrapper.orderByDesc("urm_user.fu_opentime");
        return userMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveWxUserInfo(String openid, String userInfo) {
        userMapper.saveWxUserInfo(openid, userInfo);
    }

    @Override
    public User loadUserByUsername(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("urm_user.fu_loginname", userName);
        wrapper.eq("urm_user.fu_status", "1");
        return userMapper.loadUserByUsername(wrapper);
    }
}
