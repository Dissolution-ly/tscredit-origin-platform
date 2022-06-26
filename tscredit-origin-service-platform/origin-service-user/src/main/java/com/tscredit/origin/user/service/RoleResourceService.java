package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.dao.RoleResource;
import java.util.List;
import java.util.Map;

public interface RoleResourceService extends IService<RoleResource> {

    Page<RoleResource> pageList(Page<RoleResource> page, RoleResource roleResource);

    List<String> selectRoleMenu(String roleCode, String type);

    List<Map<String,Object>> selectRoleMenu(String roleCode);

    void initResourceRoles();

}
