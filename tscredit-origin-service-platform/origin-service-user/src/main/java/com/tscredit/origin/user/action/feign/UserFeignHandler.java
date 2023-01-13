package com.tscredit.origin.user.action.feign;


import com.aurora.base.entity.response.Result;
import com.tscredit.origin.user.entity.User;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.service.client.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/feign/user")
public class UserFeignHandler {

    private final UserService userService;

    public UserFeignHandler(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "根据Id获取基本信息")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/getById")
    public Result<User> getById(String id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "根据账号返回用户")
    @PostMapping(value = "/getByName")
    public Result<User> getByName(@RequestParam(value = "name") String name) {
        return Result.success(userService.loadUserByUsername(name));
    }

}



