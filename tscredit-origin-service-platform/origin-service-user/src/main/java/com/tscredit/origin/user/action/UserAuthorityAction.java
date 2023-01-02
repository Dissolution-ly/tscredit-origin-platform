package com.tscredit.origin.user.action;


import com.aurora.base.entity.response.ActionMessage;
import com.aurora.base.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.tscredit.origin.user.entity.UserAuthority;
import com.tscredit.origin.user.service.UserAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Api(tags = {"用户管理"}, value = "UserQuotaAction")
@RestController
@RequestMapping("/userAuthortiy")
public class UserAuthorityAction {

    private final UserAuthorityService userAuthorityService;

    public UserAuthorityAction(UserAuthorityService userAuthorityService) {
        this.userAuthorityService = userAuthorityService;
    }

    @ApiOperation("权限设置-获取")
    @PostMapping("/allAuthority")
    public ActionMessage allAuthority(@RequestParam String userId) {
        Map<Object, Object> data = userAuthorityService.userAuthority(userId);

        // 处理格式
        for (Map.Entry<Object, Object> map : data.entrySet()) {
            // 每个 value 从 Map 转换为 List [{"name":"","value":""}]
            Map<String, Object> temp = JsonUtils.convertJson2ObjectLogicException(map.getValue().toString(), Map.class);
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map.Entry<String, Object> entry : temp.entrySet()) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("name", entry.getKey());
                tempMap.put("value", entry.getValue());
                result.add(tempMap);
            }
            data.put(map.getKey(), result);
        }

        return ActionMessage.success().data(data);
    }


    @ApiOperation("权限设置-修改")
    @PostMapping("/setAuthority")
    @Transactional(rollbackFor = Exception.class)
    public ActionMessage setAuthority(@RequestBody Map<String, List<Map<String, Object>>> authority, @RequestParam String userId) throws JsonProcessingException {
        // 规范请求参数 : 验证 key 是否都为数据库已有权限 ， 若有数据库中不存在的权限 则直接删除
        List<String> authoritys = userAuthorityService.selectAllAuthority();
        ArrayList<String> temp = new ArrayList<>(authority.keySet());
        temp.removeAll(authoritys);
        temp.forEach(authority::remove);

        // 删除用户权限信息
        userAuthorityService.deleteAuthorityByUserIds(Lists.newArrayList(userId));

        // 将要添加的信息转换为数据库格式
        List<UserAuthority> userAuthorities = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : authority.entrySet()) {
            Map<Object, Object> authorityContent = entry.getValue().stream()
                    .collect(Collectors.toMap(map -> map.get("name"), map -> map.get("value"), (key1, key2) -> key2));

            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setAuthorityId(entry.getKey());
            userAuthority.setUserId(userId);
            userAuthority.setAuthorityContent(JsonUtils.convertObject2Json(authorityContent));
            userAuthorities.add(userAuthority);
        }

        // 保存该用户的权限信息
        return ActionMessage.success().data(userAuthorityService.saveBatch(userAuthorities));
    }
}
