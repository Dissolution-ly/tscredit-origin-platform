package com.tscredit.origin.user.action;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tscredit.common.response.ActionMessage;
import com.tscredit.common.response.ErrorMessage;
import com.tscredit.common.response.LogicException;
import com.tscredit.common.validataed.Flag;
import com.tscredit.origin.user.constant.RedisConstants;
import com.tscredit.origin.user.entity.UserQuota;
import com.tscredit.origin.user.service.UserQuotaService;
import com.tscredit.platform.redis.config.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Api(tags = {"用户额度管理"}, value = "UserQuotaAction")
@RestController
@RequestMapping("/userQuota")
public class UserQuotaAction {

    private final UserQuotaService userQuotaService;
    private final RedisUtil redisUtil;

    public UserQuotaAction(UserQuotaService userQuotaService, RedisUtil redisUtil) {
        this.userQuotaService = userQuotaService;
        this.redisUtil = redisUtil;
    }


    @ApiOperation("额度配置-获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "userId", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "1"),
    })
    @PostMapping("/info")
    public ActionMessage info(String userId) {
        return ActionMessage.success().data(userQuotaService.getByUserId(userId));
    }

    @ApiOperation("用户额度 设置为跟另一个 用户/角色 相同")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "修改额度用户ID", required = true),
            @ApiImplicitParam(name = "sourceId", value = "额度源用户/角色 id", required = true),
            @ApiImplicitParam(name = "type", value = "code类型： 1.用户，2.角色", required = true),
    })
    @PostMapping("/setBySourceId")
    public ActionMessage setBySourceId(@RequestParam Integer userId, @RequestParam String sourceId, @Flag(values = {"1", "2"}) @RequestParam Integer type) {
        if (type == 2) {
            sourceId = "ROLE_" + sourceId;
        }
        return ActionMessage.success().data(userQuotaService.setQuotaByRole(userId, sourceId));
    }


    @ApiOperation("额度配置-修改")
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(@RequestBody List<UserQuota> data) {
        String userId = data.get(0).getUserId();
        if (StringUtils.isBlank(userId)) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        // 检查要修改的额度内容是否合法
        List<String> quotaIds = userQuotaService.getQuotaIds();
        for (UserQuota userQuota : data) {
            if (!quotaIds.contains(userQuota.getQuotaId())) {
                throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
            }
        }

        // 查询 redis 中该用户的 额度信息
        Map<Object, Object> quota = redisUtil.hmget(RedisConstants.getQuotaRedisKey(userId));
        for (UserQuota userQuota : data) {
            // 禁止用户修改 已使用 额度
            userQuota.setQuotaUse(null);

            // 修改 redis 中的 用户额度信息
            Integer oldTotal = MapUtils.getInteger(quota, userQuota.getQuotaId() + "total");
            if (quota.containsKey(userQuota.getQuotaId()) && oldTotal != null) {
                try {
                    // 计算变更数，对数据进行自增或自减
                    int i = userQuota.getQuotaTotal() - oldTotal;
                    if (i != 0) {
                        redisUtil.hincr(RedisConstants.getQuotaRedisKey(userId), userQuota.getQuotaId() + "total", i);
                        redisUtil.hincr(RedisConstants.getQuotaRedisKey(userId), userQuota.getQuotaId(), i);
                    }
                } catch (Exception ignore) {
                    // 此处不应该捕获到异常！！！ newTotal 为前端传过来的应该不会错，oldTotal 也应该在存储 redis时同步存储！
                }
            }
        }

        // 批量保存到数据库
        if (!userQuotaService.saveOrUpdateBatch(data)) {
            throw LogicException.errorMessage(ErrorMessage.SYS_ERROR);
        }
        return ActionMessage.success().data(true);
    }

}
