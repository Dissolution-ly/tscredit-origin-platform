package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.tscredit.common.response.ErrorMessage;
import com.tscredit.common.response.LogicException;
import com.tscredit.origin.user.entity.UserInfo;
import com.tscredit.origin.user.entity.dao.User;
import com.tscredit.origin.user.entity.dao.UserRole;
import com.tscredit.origin.user.entity.dto.CreateUserDTO;
import com.tscredit.origin.user.entity.dto.QueryUserDTO;
import com.tscredit.origin.user.entity.dto.UpdateUserDTO;
import com.tscredit.origin.user.mapper.UserMapper;
import com.tscredit.origin.user.service.UserRoleService;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.platform.base.enums.ResultCodeEnum;
import com.tscredit.platform.base.exception.BusinessException;
import com.tscredit.platform.base.util.BeanCopierUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleService userRoleService;

    public QueryWrapper<User> getWrapper(QueryUserDTO user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        //用户id
        if (!StringUtils.isEmpty(user.getId())) {
            wrapper.eq("urm_user.fu_id", user.getId());
        }
        //用户名称
        if (!StringUtils.isEmpty(user.getLoginName())) {
            wrapper.like("urm_user.fu_loginname", user.getLoginName());
        }
        //密码
        if (!StringUtils.isEmpty(user.getPassword())) {
            wrapper.eq("urm_user.fu_password", user.getPassword());
        }
        // 企业名称
        if (!StringUtils.isEmpty(user.getEntName())) {
            wrapper.like("urm_user.fu_ent_name", user.getEntName());
        }
        //手机号
        if (!StringUtils.isEmpty(user.getMobile())) {
            wrapper.like("urm_user.fu_mobile", user.getMobile());
        }
        //用户状态
        if (!StringUtils.isEmpty(user.getUserStatus())) {
            wrapper.eq("urm_user.fu_userstatus", user.getUserStatus());
        }


        //开始时间
        if (!StringUtils.isEmpty(user.getOpenTime())) {
            wrapper.eq("urm_user.fu_opentime", user.getOpenTime());
        }
        //过期时间
        if (!StringUtils.isEmpty(user.getExpiredTime())) {
            wrapper.eq("urm_user.fu_expiredtime", user.getExpiredTime());
        }
        //联系人
        if (!StringUtils.isEmpty(user.getCnName())) {
            wrapper.like("urm_user.fu_cnname", user.getCnName());
        }
        //用户角色
        if (!StringUtils.isEmpty(user.getRoleId())) {
            // TODO
            wrapper.eq("role_code", user.getRoleId());
        }
        // 逻辑删除
        wrapper.eq("urm_user.fu_status", 1);
        return wrapper;
    }

    /**
     * 根据 用户登录信息 返回用户信息(userId 不为null,则排除 指定用户Id的用户)
     */
    @Override
    public User getUserByLoginInfo(User user) {
        // 获取用户 账号、名称、手机号、邮箱 与现有字段进行交叉对比
        List<String> loginInfo = Lists.newArrayList(user.getLoginName(), user.getEmail(), user.getMobile(), user.getUserName());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("urm_user.fu_status", "1");
        wrapper.and(e -> e.in("urm_user.fu_loginname", loginInfo).or()
                .in("urm_user.fu_email", loginInfo).or()
                .in("urm_user.fu_mobile", loginInfo).or()
                .in("urm_user.fu_name", loginInfo));
        if (!StringUtils.isEmpty(user.getId())) {
            wrapper.ne("urm_user.fu_id", user.getId());
        }

        List<User> users = userMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public Page<UserInfo> pageList(Page<UserInfo> page, QueryUserDTO user) {
        QueryWrapper<User> wrapper = getWrapper(user);
        wrapper.orderByDesc("urm_user.udt");
        wrapper.groupBy("urm_user.fu_id");
        return userMapper.getUserPage(page, wrapper);
    }

    @Override
    @Transactional()
    public boolean createUser(CreateUserDTO createUserDTO) {
        User user = BeanCopierUtils.copyByClass(createUserDTO, User.class);

        // 验证 用户登录信息 是否重复
        if (this.getUserByLoginInfo(user) != null) {
            throw LogicException.errorMessage(ErrorMessage.USER_REPEAT);
        }

        // TODO 允许 用户组织、、 为空

        // 若设置角色为空，赋默认角色
        List<Long> roleIds = createUserDTO.getRoleIds();
        if (CollectionUtils.isEmpty(roleIds)) {
// TODO             createUserDTO.setRoleIds(Lists.newArrayList(defaultRoleId));
        }

        // 若密码为空，则赋值默认值
        if (StringUtils.isEmpty(user.getPassword())) {
            // 默认密码，配置文件配置
            // TODO user.setPassword(defaultPwd);
            // 初次登录需要修改密码
            user.setUserStatus("2");
        }

        // 密码加密
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodePwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePwd);
        createUserDTO.setPassword(encodePwd);

        // 设置 地区类型 默认为 地区
        if (StringUtils.isEmpty(user.getDistrictType())) {
            user.setDistrictType("1");
        }

        //
        boolean result = this.save(user);
        if (result) {
            createUserDTO.setId(user.getId());

            // 保存用户角色信息
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            for (Long role : roleIds) {
                userRole.setRoleId(role);
                result = userRoleService.save(userRole);
            }

            // 保存用户和组织机构的关系
//            Long organizationId = user.getOrganizationId();
//            OrganizationUser orgUser = new OrganizationUser();
//            orgUser.setUserId(userEntity.getId());
//            orgUser.setOrganizationId(organizationId);
//            organizationUserService.save(orgUser);

            // 默认增加用户所在机构数据权限值，但是否有操作权限还是会根据资源权限判断
//            DataPermissionUser dataPermission = new DataPermissionUser();
//            dataPermission.setUserId(userEntity.getId());
//            dataPermission.setOrganizationId(organizationId);
//            dataPermissionUserService.save(dataPermission);
        }
        return result;
    }

    @Override
    public boolean updateUser(UpdateUserDTO updateUserDTO) {
        User user = BeanCopierUtils.copyByClass(updateUserDTO, User.class);

        // 验证 用户登录信息 是否重复
        if (this.getUserByLoginInfo(user) != null) {
            throw LogicException.errorMessage(ErrorMessage.USER_REPEAT);
        }

        // 为输入的密码加密
        String pwd = user.getPassword();
        if (!StringUtils.isEmpty(pwd)) {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String cryptPwd = passwordEncoder.encode(pwd);
            user.setPassword(cryptPwd);
        }

        boolean result = updateById(user);

        // 修改后更新缓存
//        User oldInfo = this.getById(user.getId());
//        redisTemplate.delete("users:account_" + oldInfo.getLoginName());
//        if (!StringUtils.isEmpty(oldInfo.getEmail())) {
//            redisTemplate.delete("users:account_" + oldInfo.getEmail());
//        }
//        if (!StringUtils.isEmpty(oldInfo.getMobile())) {
//            redisTemplate.delete("users:account_" + oldInfo.getMobile());
//        }

//        机构数据
//        Long organizationId = user.getOrganizationId();
//        QueryWrapper<OrganizationUser> organizationUserWrapper = new QueryWrapper<>();
//        organizationUserWrapper.eq("user_id", userEntity.getId()).eq("organization_id", organizationId);
//        OrganizationUser orgUserOld = organizationUserService.getOne(organizationUserWrapper);
//        if (null == orgUserOld && null != organizationId) {
//            QueryWrapper<OrganizationUser> organizationUserRemoveWrapper = new QueryWrapper<>();
//            organizationUserRemoveWrapper.eq("user_id", userEntity.getId());
//            OrganizationUser orgUserRemove = organizationUserService.getOne(organizationUserRemoveWrapper);
//            QueryWrapper<DataPermissionUser> dataPermissionRemoveWrapper = new QueryWrapper<>();
//            dataPermissionRemoveWrapper.eq("user_id", userEntity.getId()).eq("organization_id", orgUserRemove.getOrganizationId());
//            //删除旧机构的数据权限
//            dataPermissionUserService.remove(dataPermissionRemoveWrapper);
//            //删除旧机构的组织机构关系
//            organizationUserService.remove(organizationUserRemoveWrapper);
//            //保存用户和组织机构的关系
//            OrganizationUser orgUser = new OrganizationUser();
//            orgUser.setUserId(userEntity.getId());
//            orgUser.setOrganizationId(organizationId);
//            organizationUserService.save(orgUser);
//            //默认增加用户所在机构数据权限值，但是否有操作权限还是会根据资源权限判断
//            DataPermissionUser dataPermissionUser = new DataPermissionUser();
//            dataPermissionUser.setUserId(userEntity.getId());
//            dataPermissionUser.setOrganizationId(organizationId);
//            dataPermissionUserService.save(dataPermissionUser);
//        }


        List<Long> roleIds = updateUserDTO.getRoleIds();
        if (result && !CollectionUtils.isEmpty(roleIds)) {
            if (!CollectionUtils.isEmpty(roleIds)) {
                // 删除不存在的权限
                QueryWrapper<UserRole> wp = new QueryWrapper<>();
                wp.eq("user_id", user.getId());
                List<UserRole> urList = userRoleService.list(wp);
                if (!CollectionUtils.isEmpty(urList)) {
                    for (UserRole role : urList) {
                        // 如果这个权限不存在，则删除
                        if (!roleIds.contains(role.getRoleId())) {
                            QueryWrapper<UserRole> wpd = new QueryWrapper<>();
                            wpd.eq("user_id", user.getId()).eq("role_id", role.getRoleId());
                            userRoleService.remove(wpd);
                        }
                    }
                }

                // 新增库里不存在的权限
                for (Long role : roleIds) {
                    QueryWrapper<UserRole> oldWp = new QueryWrapper<>();
                    oldWp.eq("user_id", user.getId()).eq("role_id", role);
                    UserRole oldUserRole = userRoleService.getOne(oldWp);
                    //查询出库中存在的角色列表，如果更新中的存在则不操作，如果不存在则新增
                    if (null == oldUserRole) {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getId());
                        userRole.setRoleId(role);
                        result = userRoleService.save(userRole);
                    }
                }
            }

            //
//            redisTemplate.delete("roles:user_id_" + userEntity.getId());
//            redisTemplate.delete("resources:user_id_" + userEntity.getId());
//            redisTemplate.delete("resources:all_user_id_" + userEntity.getId());
        }
        return result;
    }

    @Override
    public boolean batchDeleteUser(List<Long> userIds) {
        List<User> userList = listByIds(userIds);
//        for (User oldInfo : userList) {
//            redisTemplate.delete("users:account_" + oldInfo.getAccount());
//            if (!StringUtils.isEmpty(oldInfo.getEmail())) {
//                redisTemplate.delete("users:account_" + oldInfo.getEmail());
//            }
//            if (!StringUtils.isEmpty(oldInfo.getMobile())) {
//                redisTemplate.delete("users:account_" + oldInfo.getMobile());
//            }
//            redisTemplate.delete("users:id_" + oldInfo.getId());
//            redisTemplate.delete("roles:user_id_" + oldInfo.getId());
//        }
        return removeByIds(userIds);
    }

    @Override
    public UserInfo queryUserInfo(QueryUserDTO queryUserDTO) {
        QueryWrapper<User> wrapper = getWrapper(queryUserDTO);
        wrapper.groupBy("urm_user.fu_id");
        UserInfo userInfo = userMapper.queryUserInfo(wrapper);

        if (null == userInfo) {
            throw new BusinessException(ResultCodeEnum.INVALID_USERNAME.getMsg());
        }


        // 将 roleIds ',' 分割为集合，用于 OAuth2 和 Gateway鉴权
        String roleIds = userInfo.getRoleIds();
        if (!StringUtils.isEmpty(roleIds)) {
            String[] roleIdsArray = roleIds.split(",");
            userInfo.setRoleIdList(Arrays.asList(roleIdsArray));
        }

        // 将 roleKeys ',' 分割为集合，用于前端页面鉴权
        String roleKeys = userInfo.getRoleKeys();
        if (!StringUtils.isEmpty(roleKeys)) {
            String[] roleKeysArray = roleKeys.split(",");
            userInfo.setRoleKeyList(Arrays.asList(roleKeysArray));
        }


        // 获取用户的角色数据权限级别
//        String dataPermissionTypes = userInfo.getDataPermissionTypes();
//        if (!StringUtils.isEmpty(dataPermissionTypes)) {
//            String[] dataPermissionTypeArray = dataPermissionTypes.split(",");
//            userInfo.setDataPermissionTypeList(Arrays.asList(dataPermissionTypeArray));
//        }


        // 获取用户机构数据权限列表
//        String organizationIds = userInfo.getOrganizationIds();
//        if (!StringUtils.isEmpty(organizationIds)) {
//            String[] organizationIdArray = organizationIds.split(",");
//            userInfo.setOrganizationIdList(Arrays.asList(organizationIdArray));
//        }

//        QueryUserResourceDTO queryUserResourceDTO = new QueryUserResourceDTO();
//        queryUserResourceDTO.setUserId(userInfo.getId());
//        List<Resource> resourceList = resourceService.queryResourceListByUserId(queryUserResourceDTO);
//
//        // 查询用户权限列表 key，用于前端页面鉴权
//        List<String> menuList = resourceList.stream().map(Resource::getResourceKey).collect(Collectors.toList());
//        userInfo.setResourceKeyList(menuList);
//
//        // 查询用户资源列表，用于 SpringSecurity 鉴权
//        List<String> resourceUrlList = resourceList.stream().filter(s -> !ResourceEnum.MODULE.getCode().equals(s.getResourceType()) && !ResourceEnum.MENU.getCode().equals(s.getResourceType())).map(Resource::getResourceUrl).collect(Collectors.toList());
//        userInfo.setResourceUrlList(resourceUrlList);
//
//        // 查询用户菜单树，用于页面展示
//        List<Resource> menuTree = resourceService.queryMenuTreeByUserId(userInfo.getId());
//        userInfo.setMenuTree(menuTree);

        return userInfo;
    }
}
