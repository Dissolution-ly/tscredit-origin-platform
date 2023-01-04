package com.tscredit.origin.main.action;


import com.aurora.base.entity.response.ActionMessage;
import com.aurora.redis.config.RedisUtil;
import com.tscredit.service.user.client.feign.IUserFeign;
import com.tscredit.tsinterfaces.access.HttpQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Tag(name = "通用")
@Slf4j
@RestController
@RequestMapping("/common")
@Validated
public class CommonAction {

    private final HttpQuery httpQuery;
    private final RedisUtil redisUtil;
    private final IUserFeign userFeign;

    public CommonAction(HttpQuery httpQuery, RedisUtil redisUtil, IUserFeign userFeign) {
        this.httpQuery = httpQuery;
        this.redisUtil = redisUtil;
        this.userFeign = userFeign;
    }

    @Operation(summary ="CCCC")
    @PostMapping(value = "/tttt")
    public ActionMessage entSearch() {
        return ActionMessage.success().data(userFeign.queryUserByOpenId("3"));
    }

    @Operation(summary ="企业名称检索")
    @Parameters({
            @Parameter(name = "entName", description = "企业名称"),
            @Parameter(name = "type", description = "0.企业名称检索"),
    })
    @PostMapping(value = "/entSearch")
    public ActionMessage entSearch(String entName, @RequestParam(defaultValue = "0") Integer type) {
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", entName);
        param.put("type", type);
        List<Map<String, Object>> data = (List<Map<String, Object>>) httpQuery.queryRoute("getEntByKeyword", param, "ts").get("RESULTDATA");
        List<String> entNames = data.stream().map(map -> MapUtils.getString(map, "ENTNAME")).collect(Collectors.toList());
        return ActionMessage.success().data(data);
    }

    @Operation(summary = "获取标签接口参数信息", description = "获取标签接口参数信息")
    @Parameters({
            @Parameter(name = "code", description = "父级code(为空,返回第一级)"),
            @Parameter(name = "tagName", description = "模糊标签名称"),
    })
    @PostMapping(value = "/getTagInfo")
    public ActionMessage getTagInfo(String code, String tagName) {
        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(code)) {
            paramMap.put("code", code);
        }
        if (StringUtils.isNotBlank(tagName)) {
            paramMap.put("tagName", tagName);
        }
        Map<String, Object> map = httpQuery.queryRoute("getTagInfo", paramMap, "ts");
        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("RESULTDATA");
        return ActionMessage.success().data(data);
    }
}
