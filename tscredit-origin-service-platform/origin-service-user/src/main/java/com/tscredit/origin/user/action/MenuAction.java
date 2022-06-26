package com.tscredit.origin.user.action;

import com.tscredit.common.response.ActionMessage;
import com.tscredit.origin.user.entity.dao.Resource;
import com.tscredit.origin.user.service.ResourceService;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.origin.user.utils.JwtUtil;
import com.tscredit.origin.user.utils.TreeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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


@Api(tags = {"菜单管理"}, value = "MenuAction")
@RestController
@RequestMapping("/menu")
public class MenuAction {

    private final ResourceService resourceService;
    private final UserService userService;

    public MenuAction(ResourceService resourceService, UserService userService) {
        this.resourceService = resourceService;
        this.userService = userService;
    }

    @ApiOperation("根据Id获取基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "1"),
    })
    @PostMapping("/info")
    public ActionMessage info(String id) {
        return ActionMessage.success().data(resourceService.getById(id));
    }


    @ApiOperation("保存/修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", dataTypeClass = String.class, defaultValue = "1"),
    })
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(Resource resource) {
        return ActionMessage.success().data(resourceService.saveOrUpdate(resource));
    }


    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合(字符串数组)", dataType = "string", dataTypeClass = String.class, required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<String> ids) {
        return ActionMessage.success().data(resourceService.removeByIds(ids));
    }


    @ApiOperation("获取菜单树形图")
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

    @ApiOperation("获取指定用户拥有的菜单")
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
