package com.tscredit.origin.user.service.impl;


import com.aurora.base.constant.AuthConstant;
import com.aurora.redis.config.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.entity.dao.Resource;
import com.tscredit.origin.user.entity.dao.RoleResource;
import com.tscredit.origin.user.mapper.RoleMenuMapper;
import com.tscredit.origin.user.service.ResourceService;
import com.tscredit.origin.user.service.RoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleResourceServiceImpl extends ServiceImpl<RoleMenuMapper, RoleResource> implements RoleResourceService {

    private final RoleMenuMapper roleMenuMapper;
    private final ResourceService resourceService;
    private final RedisUtil redisUtil;

    @Value(("${tenant.enable}"))
    private Boolean enable;



    public QueryWrapper<RoleResource> getWrapper(RoleResource roleResource) {
        QueryWrapper<RoleResource> wrapper = new QueryWrapper<>();
        if (roleResource == null) return null;

        //
        if (roleResource.getId() != null) {
            wrapper.eq("id", roleResource.getId());
        }
        //角色编码
        if (roleResource.getRoleCode() != null) {
            wrapper.eq("role_code", roleResource.getRoleCode());
        }
        //菜单编码
        if (roleResource.getMenuCode() != null) {
            wrapper.eq("menu_code", roleResource.getMenuCode());
        }

        return wrapper;
    }


    @Override
    public Page<RoleResource> pageList(Page<RoleResource> page, RoleResource roleResource) {
        return page(page, getWrapper(roleResource));
    }

    @Override
    public List<String> selectRoleMenu(String roleCode, String type) {
        return roleMenuMapper.selectRoleMenu(roleCode, type);
    }

    @Override
    public List<Map<String, Object>> selectRoleMenu(String roleCode) {
        return roleMenuMapper.selectRoleMenuList(roleCode);
    }

    @Override
    public void initResourceRoles() {
        // 查询系统角色和权限的关系
        QueryWrapper<Resource> wrapper = new QueryWrapper<>();
        wrapper.in("resource_type", 1, 2);
        wrapper.eq("status", "1");
        List<Resource> resourceList = resourceService.list(wrapper);
        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            Map<String, List<Resource>> resourceListMap =
                    resourceList.stream().collect(Collectors.groupingBy(Resource::getTenantId));
            resourceListMap.forEach((key, value) -> {
                String redisKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY + key;
                redisUtil.del(redisKey);
                addRoleResource(redisKey, value);
            });
        } else {
            redisUtil.del(AuthConstant.RESOURCE_ROLES_KEY);
            addRoleResource(AuthConstant.RESOURCE_ROLES_KEY, resourceList);
        }
    }

    private void addRoleResource(String key, List<Resource> resourceList) {
//        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
//        Optional.ofNullable(resourceList).orElse(new ArrayList<>()).forEach(resource -> {
//            // roleKey -> ROLE_{roleKey}
//            List<String> roleKeys = Optional.ofNullable(resource.getRoles()).orElse(new ArrayList<>()).stream().map(Role::getRoleKey)
//                    .distinct().map(roleKey -> AuthConstant.AUTHORITY_PREFIX + roleKey).collect(Collectors.toList());
//            if (CollectionUtil.isNotEmpty(roleKeys)) {
//                resourceRolesMap.put(resource.getResourceUrl(), roleKeys);
//            }
//        });
//        redisTemplate.opsForHash().putAll(key, resourceRolesMap);
    }

}
