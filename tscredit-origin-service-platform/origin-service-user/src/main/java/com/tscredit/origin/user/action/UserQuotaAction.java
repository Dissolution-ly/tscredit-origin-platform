package com.tscredit.origin.user.action;


import com.aurora.base.constant.ErrorMessage;
import com.aurora.base.entity.response.ActionMessage;
import com.aurora.base.exception.LogicException;
import com.aurora.boot.validataed.Flag;
import com.aurora.redis.config.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tscredit.origin.user.config.Constants;
import com.tscredit.origin.user.entity.UserQuota;
import com.tscredit.origin.user.service.UserQuotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @author lixuanyu
 * @since 2021-08-12
 */
@Tag(name ="用户额度管理", description = "UserQuotaAction")
@RestController
@RequestMapping("/userQuota")
public class UserQuotaAction {

    private final UserQuotaService userQuotaService;
    private final RedisUtil redisUtil;

    public UserQuotaAction(UserQuotaService userQuotaService, RedisUtil redisUtil) {
        this.userQuotaService = userQuotaService;
        this.redisUtil = redisUtil;
    }


    @Operation(summary = "额度配置-获取")
    @Parameters({
            @Parameter(name = "userId", description = "userId", required = true),
    })
    @PostMapping("/info")
    public ActionMessage info(String userId) {
        return ActionMessage.success().data(userQuotaService.getByUserId(userId));
    }

    @Operation(summary = "用户额度 设置为跟另一个 用户/角色 相同")
    @Parameters({
            @Parameter(name = "userId", description = "修改额度用户ID", required = true),
            @Parameter(name = "sourceId", description = "额度源用户/角色 id", required = true),
            @Parameter(name = "type", description = "code类型： 1.用户，2.角色", required = true),
    })
    @PostMapping("/setBySourceId")
    public ActionMessage setBySourceId(@RequestParam Integer userId, @RequestParam String sourceId, @Flag(values = {"1", "2"}) @RequestParam Integer type) {
        if (type == 2) {
            sourceId = "ROLE_" + sourceId;
        }
        return ActionMessage.success().data(userQuotaService.setQuotaByRole(userId, sourceId));
    }


    @Operation(summary = "额度配置-修改")
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(@RequestBody List<UserQuota> data) {
        String userId = data.get(0).getUserId();
        if (StringUtils.isBlank(userId)) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        // 检查要修改的额度内容是否合法
        List<String> quotaIds = userQuotaService.getQuotaIds();
        for (UserQuota userQuota : data) {
            if(!quotaIds.contains(userQuota.getQuotaId())){
                throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
            }
        }

        // 查询 redis 中该用户的 额度信息
        Map<Object, Object> quota = redisUtil.hmget(Constants.QUOAT_PREFIX +userId);
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
                        redisUtil.hincr(Constants.QUOAT_PREFIX +userId, userQuota.getQuotaId() + "total", i);
                        redisUtil.hincr(Constants.QUOAT_PREFIX +userId, userQuota.getQuotaId(), i);
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
