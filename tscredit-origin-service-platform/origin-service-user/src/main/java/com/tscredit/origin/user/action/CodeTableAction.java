package com.tscredit.origin.user.action;


import com.aurora.base.constant.ErrorMessage;
import com.aurora.base.entity.response.ActionMessage;
import com.aurora.base.exception.LogicException;
import com.tscredit.tsinterfaces.access.HttpQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lixuanyu
 * @since 2021-08-12
 */
@Tag(name = "码表查询", description = "CodeTableAction")
@RestController
@RequestMapping("/codeTable")
public class CodeTableAction {

    private final HttpQuery query;

    public CodeTableAction(HttpQuery query) {
        this.query = query;
    }

    @Operation(summary = "权限配置-地区列表")
    @Parameters({
            @Parameter(name = "type", description = "查询类型：1.园区,2.地区", required = true),
    })
    @PostMapping("/area")
    public ActionMessage area(@RequestParam Integer type, String keyWord) {
        Object result;
        Map<String, Object> param = new HashMap<>(16);

        if (type == 1) {
            // 地区查询
            param.put("keyWord", keyWord);
            result = query.queryRoute("getInputtipsInfo", param, "es").get("RESULTDATA");
        } else if (type == 2) {
            // 园区查询
            result = query.queryRoute("getParkInfo", param, "es");
        } else {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }

        return ActionMessage.success().data(result);
    }


    @Operation(summary = "查询产业链列表")
    @PostMapping("/industryLink")
    public ActionMessage industryLink() {
        Map<String, Object> map = query.queryRoute("getCylListInfo", null, "es");

        return ActionMessage.success().data(map);
    }
}
