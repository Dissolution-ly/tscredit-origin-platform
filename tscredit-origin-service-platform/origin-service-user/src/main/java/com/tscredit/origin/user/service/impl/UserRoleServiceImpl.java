package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.entity.dao.Role;
import com.tscredit.origin.user.entity.dao.UserRole;
import com.tscredit.origin.user.mapper.UserRoleMapper;
import com.tscredit.origin.user.service.RoleService;
import com.tscredit.origin.user.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色相关操作接口实现类
 * @date 2018年5月18日 下午3:22:21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final RoleService roleService;

    @Override
    public UserRole selectByUserId(Long userId) {
        QueryWrapper<UserRole> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        return this.getOne(ew);
    }

    @Override
//    @Cacheable(value = "roles", key = "'user_id_'.concat(#userId)")
    public List<Role> queryRolesByUserId(Long userId) {
        QueryWrapper<UserRole> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        List<UserRole> userRoleList = this.list(ew);
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<Long> roleIds = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                roleIds.add(userRole.getRoleId());
            }
            QueryWrapper<Role> ewRole = new QueryWrapper<>();
            ewRole.in("id", roleIds);
            List<Role> roleList = roleService.list(ewRole);
            return roleList;
        } else {
            return null;
        }
    }
}
