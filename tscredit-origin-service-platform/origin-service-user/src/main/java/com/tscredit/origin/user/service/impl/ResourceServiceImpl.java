package com.tscredit.origin.user.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.mapper.ResourceMapper;
import com.tscredit.origin.user.entity.dao.Resource;
import com.tscredit.origin.user.entity.dao.Role;
import com.tscredit.origin.user.service.ResourceService;
import com.tscredit.origin.user.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final RoleService roleService;

    public ResourceServiceImpl(ResourceMapper resourceMapper, RoleService roleService){
        this.resourceMapper = resourceMapper;
        this.roleService = roleService;
    }

    @Override
    public List<Map<String, Object>> getListMap(String name) {
        return resourceMapper.getListMap(name);
    }


    @Override
    public List<Map<String, Object>> getUserMenu(String roleCode) {
        Role role = roleService.getRoleByRoleCode(roleCode);
        return resourceMapper.getUserMenu(role.getId());
    }

}
