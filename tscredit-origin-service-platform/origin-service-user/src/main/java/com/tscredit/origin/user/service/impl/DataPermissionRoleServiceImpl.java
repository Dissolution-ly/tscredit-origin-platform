package com.tscredit.origin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.entity.dao.DataPermissionRole;
import com.tscredit.origin.user.entity.dto.CreateDataPermissionRoleDTO;
import com.tscredit.origin.user.entity.dto.DataPermissionRoleDTO;
import com.tscredit.origin.user.entity.dto.QueryDataPermissionRoleDTO;
import com.tscredit.origin.user.entity.dto.UpdateDataPermissionRoleDTO;
import com.tscredit.origin.user.mapper.DataPermissionRoleMapper;
import com.tscredit.origin.user.service.DataPermissionRoleService;
import com.tscredit.platform.base.util.BeanCopierUtils;
import com.tscredit.platform.mybatis.constant.DataPermissionConstant;
import com.tscredit.platform.mybatis.entity.DataPermissionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据权限配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataPermissionRoleServiceImpl extends ServiceImpl<DataPermissionRoleMapper, DataPermissionRole> implements DataPermissionRoleService {

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    private final DataPermissionRoleMapper dataPermissionRoleMapper;

    private final RedisTemplate redisTemplate;

    /**
    * 分页查询数据权限配置表列表
    * @param page
    * @param queryDataPermissionRoleDTO
    * @return
    */
    @Override
    public Page<DataPermissionRoleDTO> queryDataPermissionRoleList(Page<DataPermissionRoleDTO> page, QueryDataPermissionRoleDTO queryDataPermissionRoleDTO) {
        Page<DataPermissionRoleDTO> dataPermissionRoleInfoList = dataPermissionRoleMapper.queryDataPermissionRoleList(page, queryDataPermissionRoleDTO);
        return dataPermissionRoleInfoList;
    }

    /**
    * 查询数据权限配置表详情
    * @param queryDataPermissionRoleDTO
    * @return
    */
    @Override
    public DataPermissionRoleDTO queryDataPermissionRole(QueryDataPermissionRoleDTO queryDataPermissionRoleDTO) {
        DataPermissionRoleDTO dataPermissionRoleDTO = dataPermissionRoleMapper.queryDataPermissionRole(queryDataPermissionRoleDTO);
        return dataPermissionRoleDTO;
    }

    /**
    * 创建数据权限配置表
    * @param dataPermissionRole
    * @return
    */
    @Override
    public boolean createDataPermissionRole(CreateDataPermissionRoleDTO dataPermissionRole) {
        DataPermissionRole dataPermissionRoleEntity = BeanCopierUtils.copyByClass(dataPermissionRole, DataPermissionRole.class);
        boolean result = this.save(dataPermissionRoleEntity);
        return result;
    }

    /**
    * 更新数据权限配置表
    * @param dataPermissionRole
    * @return
    */
    @Override
    public boolean updateDataPermissionRole(UpdateDataPermissionRoleDTO dataPermissionRole) {
        DataPermissionRole dataPermissionRoleEntity = BeanCopierUtils.copyByClass(dataPermissionRole, DataPermissionRole.class);
        QueryWrapper<DataPermissionRole> wrapper = new QueryWrapper<>();
        wrapper.eq("id", dataPermissionRoleEntity.getId());
        boolean result = this.update(dataPermissionRoleEntity, wrapper);
        return result;
    }

    /**
    * 删除数据权限配置表
    * @param dataPermissionRoleId
    * @return
    */
    @Override
    public boolean deleteDataPermissionRole(Long dataPermissionRoleId) {
        boolean result = this.removeById(dataPermissionRoleId);
        return result;
    }

    /**
    * 批量删除数据权限配置表
    * @param dataPermissionRoleIds
    * @return
    */
    @Override
    public boolean batchDeleteDataPermissionRole(List<Long> dataPermissionRoleIds) {
        boolean result = this.removeByIds(dataPermissionRoleIds);
        return result;
    }

    @Override
    public void initDataRolePermissions() {
//        List<DataPermissionRoleDTO> dataPermissionRoleList = dataPermissionRoleMapper.list();
//        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
//        if (enable) {
//            Map<Long, List<DataPermissionRoleDTO>> dataPermissionRoleListMap =
//                    dataPermissionRoleList.stream().collect(Collectors.groupingBy(DataPermissionRoleDTO::getTenantId));
//            dataPermissionRoleListMap.forEach((key, value) -> {
//                // auth:tenant:data:permission:0
//                String redisKey = DataPermissionConstant.TENANT_DATA_PERMISSION_KEY + key;
//                redisTemplate.delete(redisKey);
//                addDataRolePermissions(redisKey, value);
//            });
//        } else {
//            redisTemplate.delete(DataPermissionConstant.DATA_PERMISSION_KEY);
//            // auth:data:permission
//            addDataRolePermissions(DataPermissionConstant.DATA_PERMISSION_KEY, dataPermissionRoleList);
//        }
    }

    private void addDataRolePermissions(String key, List<DataPermissionRoleDTO> dataPermissionRoleList) {

        Map<String, DataPermissionEntity> dataPermissionMap = new TreeMap<>();
        for (DataPermissionRoleDTO dataPermissionRole : dataPermissionRoleList) {
            String dataRolePermissionCache = DataPermissionConstant.DATA_PERMISSION_KEY_MAPPER +
                    dataPermissionRole.getDataMapperFunction() + DataPermissionConstant.DATA_PERMISSION_KEY_TYPE +
                    dataPermissionRole.getDataPermissionType();
            DataPermissionEntity dataPermissionEntity = BeanCopierUtils.copyByClass(dataPermissionRole, DataPermissionEntity.class);
            dataPermissionMap.put(dataRolePermissionCache, dataPermissionEntity);
        }
        redisTemplate.boundHashOps(key).putAll(dataPermissionMap);
    }
}
