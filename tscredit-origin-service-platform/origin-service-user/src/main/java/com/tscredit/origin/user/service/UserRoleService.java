package com.tscredit.origin.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.dao.Role;
import com.tscredit.origin.user.entity.dao.UserRole;

import java.util.List;

/**
 * @ClassName: IUserRoleService
 * @Description: 用户角色相关操接口
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID查询人员角色
     * 
     * @param userId
     *            用户ID
     * @return 结果
     */
    UserRole selectByUserId(Long userId);

    /**
     * queryRolesByUserId
     * 
     * @Title: queryRolesByUserId
     * @Description: 查询用户的角色
     * @param userId
     * @return List<Role>
     */
    List<Role> queryRolesByUserId(Long userId);
}
