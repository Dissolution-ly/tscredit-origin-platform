package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.dao.RoleMapper;
import com.tscredit.origin.user.entity.Role;
import com.tscredit.origin.user.service.RoleService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public QueryWrapper<Role> getWrapper(Role role) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (role == null) {
            return null;
        }
        //角色ID
        if (role.getId() != null) {
            wrapper.eq("id", role.getId());
        }
        //角色名称
        if (role.getName() != null) {
            wrapper.like("role_name", role.getName());
        }
        return wrapper;
    }

    @Override
    public Page<Role> pageList(Page<Role> page, Role role) {
        return page(page, getWrapper(role));
    }

    @Override
    public Map<String, String> loadResourceDefine() {
        // 角色表、菜单表、角色菜单关联表 - 获取路径所需要的角色，格式大致如下：
        //          "/user/*" : [AUTH_USER,AUTH_ADMIN_SUPER]
        Map<String, String> resourceDefineTemp = roleMapper.loadResourceDefine(null).stream().collect(
                Collectors.toMap(map -> map.get("roleCode"), map -> {
                    String authority = MapUtils.getString(map, "authority", "");
                    return StringUtils.isBlank(authority) ? "!当前路径禁止访问!" : authority;
                }, (key1, key2) -> key2)
        );

//        for (String key : resourceDefineTemp.keySet()) {
//            String value = resourceDefineTemp.remove(key);
//            value =  StringUtils.isBlank(value) ? "!当前路径禁止访问!" : value;
//            for (String s : key.split(",")) {
//                resourceDefineTemp.put(s, value);
//            }
//        }

        return resourceDefineTemp;
    }


    @Override
    public Role getRoleByRoleCode(String roleCode) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", roleCode);
        wrapper.eq("status", "1");
        return roleMapper.selectOne(wrapper);
    }

    @Override
    public List<Map<String, Object>> getUserRoleByRoleId(List<String> ids) {
        return roleMapper.getUserRoleByRoleId(ids);
    }
}
