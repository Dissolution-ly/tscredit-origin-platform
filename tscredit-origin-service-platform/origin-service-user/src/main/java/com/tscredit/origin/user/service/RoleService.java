package com.tscredit.origin.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.Role;


import java.util.List;
import java.util.Map;


public interface RoleService extends IService<Role> {

    Page<Role> pageList(Page<Role> page, Role role);

    Map<String, String> loadResourceDefine();

    Role getRoleByRoleCode(String roleCode);


    List<Map<String, Object>> getUserRoleByRoleId(List<String> ids);
}
