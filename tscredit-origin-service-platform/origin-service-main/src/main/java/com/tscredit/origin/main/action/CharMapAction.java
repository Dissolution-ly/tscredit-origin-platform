package com.tscredit.origin.main.action;

import com.aurora.base.entity.response.ActionMessage;
import com.tscredit.origin.main.entity.CharMap;
import com.tscredit.origin.main.service.CharMapService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


/**
 * (CharMap)表控制层
 *
 * @author lixuanyu
 * @since 2022-11-20 12:13:09
 */

@Tag(name = "码表", description = "CharMapAction")
@RestController
@RequestMapping("charMap")
public class CharMapAction {

    private final CharMapService charMapService;

    public CharMapAction(CharMapService charMapService) {
        this.charMapService = charMapService;
    }

    @Operation(summary = "分页列表查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "页数", required = true),
            @Parameter(name = "pageSize", description = "每页条数", required = true)
    })
    @PostMapping("/list")
    public ActionMessage list(@RequestParam Integer pageNum, @RequestParam Integer pageSize, CharMap charMap) {
        return ActionMessage.success().data(charMapService.pageList(new Page<>(pageNum, pageSize), charMap));
    }

    @Operation(summary ="根据Id获取基本信息")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/info")
    public ActionMessage info(@RequestParam String id) {
        return ActionMessage.success().data(charMapService.getById(id));
    }

}

