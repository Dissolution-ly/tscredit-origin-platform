package com.tscredit.origin.user.component;

import com.tscredit.origin.user.service.DataPermissionRoleService;
import com.tscredit.origin.user.service.RoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * 容器启动完成加载资源权限数据到缓存
 */

@Slf4j
//@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InitSystemCacheRunner implements CommandLineRunner {

    private final RoleResourceService roleResourceService;

    private final DataPermissionRoleService dataPermissionRoleService;

    @Override
    public void run(String... args) {

        log.info("InitResourceRolesCacheRunner running");

        // 初始化系统权限和角色的关系缓存
        roleResourceService.initResourceRoles();

        // 初始化数据权限缓存
        dataPermissionRoleService.initDataRolePermissions();

    }
}

