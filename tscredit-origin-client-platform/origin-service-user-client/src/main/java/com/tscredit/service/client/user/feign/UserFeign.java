package com.tscredit.service.client.user.feign;

import com.aurora.base.entity.response.Result;
import com.tscredit.service.client.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "origin-service-user", contextId = "UserClient", path = "/feign/user")
public interface UserFeign {


    @Operation(summary = "根据Id获取基本信息")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/getById")
    public Result<UserDTO> getById(@RequestParam(value = "id")String id);

    @Operation(summary = "根据账号返回用户")
    @PostMapping(value = "/getByName")
    public Result<UserDTO> getByName(@RequestParam(value = "name") String name);

}