package com.tscredit.origin.user.action.feign;

import com.aurora.base.entity.response.Result;
import com.tscredit.origin.user.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ResourceFeign
 * @Description: ResourceFeign前端控制器
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "ResourceFeign|提供微服务调用接口")
@RefreshScope
public class ResourceFeign {

    private final ResourceService resourceService;

    @GetMapping(value = "/query/max/id")
    @Operation(summary = "查询资源权限表最大的id值", description = "查询资源权限表最大的id值")
    public Result<Long> queryResourceMaxId() {
//        Long idMax = 0L;
//        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        queryWrapper.last("limit 1");
//        List<Resource> resourceList = resourceService.list(queryWrapper);
//        if (!CollectionUtils.isEmpty(resourceList)) {
//            idMax = resourceList.get(0).getId();
//        }
//        return Result.data(idMax);
        return null;
    }
}
