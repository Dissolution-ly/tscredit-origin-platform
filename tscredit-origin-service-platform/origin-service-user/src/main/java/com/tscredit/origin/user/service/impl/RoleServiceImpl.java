package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.mapper.RoleMapper;
import com.tscredit.origin.user.entity.dao.Role;
import com.tscredit.origin.user.service.RoleService;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;
    public RoleServiceImpl (RoleMapper roleMapper){
        this.roleMapper = roleMapper;
    }

    public QueryWrapper<Role> getWrapper(Role role) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if(role == null) return null;
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
    public Role getRoleByRoleCode(String roleCode) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", roleCode);
        wrapper.eq("status","1");
        return roleMapper.selectOne(wrapper);
    }
}
