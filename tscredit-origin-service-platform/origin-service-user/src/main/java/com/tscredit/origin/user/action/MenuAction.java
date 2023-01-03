package com.tscredit.origin.user.action;


import com.aurora.base.entity.response.ActionMessage;
import com.tscredit.origin.user.entity.dao.Resource;
import com.tscredit.origin.user.service.ResourceService;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.origin.user.utils.JwtUtil;
import com.tscredit.origin.user.utils.TreeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Tag(name = "菜单管理", description = "MenuAction")
@RestController
@RequestMapping("/menu")
public class MenuAction {

    private final ResourceService resourceService;
    private final UserService userService;

    public MenuAction(ResourceService resourceService, UserService userService) {
        this.resourceService = resourceService;
        this.userService = userService;
    }

    @Operation(summary = "根据Id获取基本信息")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/info")
    public ActionMessage info(String id) {
        return ActionMessage.success().data(resourceService.getById(id));
    }


    @Operation(summary = "保存/修改")
    @Parameters({
            @Parameter(name = "id", description = "id"),
    })
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(Resource resource) {
        return ActionMessage.success().data(resourceService.saveOrUpdate(resource));
    }


    @Operation(summary = "删除")
    @Parameters({
            @Parameter(name = "ids", description = "id集合(字符串数组)", required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<String> ids) {
        return ActionMessage.success().data(resourceService.removeByIds(ids));
    }


    @Operation(summary = "获取菜单树形图")
    @PostMapping(value = "/menuTree")

    public ActionMessage menuTree(String name) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        List<Map<String, Object>> result = resourceService.getListMap(name);
        List<String> idList = result.stream().map(map -> MapUtils.getString(map, "id")).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(result)) {
            result = TreeUtils.buildTree(result, new TreeUtils.FindChild<Map<String, Object>>() {
                @Override
                public Map<String, Object> findRoot(Map<String, Object> map) {
                    if (!idList.contains(MapUtils.getString(map, "pId"))) {
                        return map;
                    }
                    return null;
                }

                @Override
                public List<Map<String, Object>> findChildren(Map<String, Object> rootMap, List<Map<String, Object>> list) {
                    List<Map<String, Object>> children = new ArrayList<>();
                    for (Map<String, Object> map : list) {
                        if (MapUtils.getString(rootMap, "id").equalsIgnoreCase(MapUtils.getString(map, "pId"))) {
                            children.add(map);
                        }
                    }
                    return children;
                }

                @Override
                public void buildExtendProperty(Map<String, Object> property, Map<String, Object> map, List<Map<String, Object>> children) {

                }

                @Override
                public Map<String, Object> buildObjectToMap(Map<String, Object> map) {
                    return map;
                }
            });
        }

        return ActionMessage.success().data(result);
    }

    @Operation(summary = "获取指定用户拥有的菜单")
    @PostMapping(value = "/getUserMenu")
    public ActionMessage getUserMenu(HttpServletRequest request, String userId) {
        String roleCode = null;
        if (StringUtils.isEmpty(userId)) {
            Map<String, Object> user = JwtUtil.token(request);
            roleCode = MapUtils.getString(user, "roleCode");
        } else {
//            TODO roleCode
//            User user = userService.getById(userId);
//            roleCode ;
        }
        return ActionMessage.success().data(resourceService.getUserMenu(roleCode));
    }


}
