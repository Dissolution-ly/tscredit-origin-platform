package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.dao.DataPermissionRole;
import com.tscredit.origin.user.entity.dto.CreateDataPermissionRoleDTO;
import com.tscredit.origin.user.entity.dto.DataPermissionRoleDTO;
import com.tscredit.origin.user.entity.dto.QueryDataPermissionRoleDTO;
import com.tscredit.origin.user.entity.dto.UpdateDataPermissionRoleDTO;

import java.util.List;

/**
 * <p> 数据权限配置表 服务类 </p>
 */
public interface DataPermissionRoleService extends IService<DataPermissionRole> {

    /**
     * 分页查询数据权限配置表列表
     */
    Page<DataPermissionRoleDTO> queryDataPermissionRoleList(Page<DataPermissionRoleDTO> page, QueryDataPermissionRoleDTO queryDataPermissionRoleDTO);

    /**
     * 查询数据权限配置表详情
     */
    DataPermissionRoleDTO queryDataPermissionRole(QueryDataPermissionRoleDTO queryDataPermissionRoleDTO);

    /**
     * 创建数据权限配置表
     */
    boolean createDataPermissionRole(CreateDataPermissionRoleDTO dataPermissionRole);

    /**
     * 更新数据权限配置表
     */
    boolean updateDataPermissionRole(UpdateDataPermissionRoleDTO dataPermissionRole);

    /**
     * 删除数据权限配置表
     */
    boolean deleteDataPermissionRole(Long dataPermissionRoleId);

    /**
     * 批量删除数据权限配置表
     */
    boolean batchDeleteDataPermissionRole(List<Long> dataPermissionRoleIds);

    /**
     * 初始化系统数据权限权限
     */
    void initDataRolePermissions();
}
