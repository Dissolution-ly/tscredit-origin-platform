package com.tscredit.origin.user.action;

import com.aurora.base.entity.response.Result;
import com.aurora.base.exception.SystemException;
import com.tscredit.origin.user.entity.dto.SystemDTO;
import com.tscredit.origin.user.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "system")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "系统服务", description = "CodeTableAction")
@RefreshScope
public class SystemController {

    private final SystemService systemService;

    private final RedissonClient redisson;

    private final RedisTemplate<String, String> template;

    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping(value = "list")
    @Operation(summary = "system list接口")
    public Object list() {
        return systemService.list();
    }


    @GetMapping(value = "page")
    @Operation(summary = "system page接口")
    public Object page() {
        return systemService.page();
    }

    @GetMapping(value = "exception")
    @Operation(summary = "自定义异常及返回测试接口")
    public Result<String> exception() {
        return Result.success(systemService.exception());
    }

    @PostMapping(value = "valid")
    @Operation(summary = "参数校验测试接口")
    public Result<SystemDTO> valid(@Valid @RequestBody SystemDTO systemDTO) {
        return Result.success(systemDTO);
    }

    @PostMapping(value = "nacos")
    @Operation(summary = "Nacos读取配置文件测试接口")
    public Result<String> nacos() {
        return Result.success(timeZone);
    }

    @GetMapping(value = "api/by/id")
    @Operation(summary = "Fegin Get调用测试接口")
    public Result<Object> feginById(@RequestParam("id") String id) {
        return Result.success(systemService.list());
    }

    @PostMapping(value = "api/by/dto")
    @Operation(summary = "Fegin Post调用测试接口")
    public Result<Object> feginByDto(@Valid @RequestBody SystemDTO systemDTO) {
        return Result.success(systemDTO);
    }

    @GetMapping("/api/ribbon")
    @Operation(summary = "Ribbon调用测试接口")
    public Result<String> testRibbon() {
        return Result.success("现在访问的服务端口是:" + serverPort);
    }

    @Operation(summary = "限流测试")
    @GetMapping(value = "sentinel/protected")
    public Result<String> sentinelProtected() {
        return Result.success("访问的是限流测试接口");
    }

    @Operation(summary = "慢调用比例熔断策略")
    @GetMapping(value = "sentinel/slow/request/ratio")
    public Result<String> sentinelRR() {
        try {
            double randomNumber;
            randomNumber = Math.random();
            if (randomNumber >= 0 && randomNumber <= 0.80) {
                Thread.sleep(300L);
            } else if (randomNumber >= 0.80 && randomNumber <= 0.80 + 0.10) {
                Thread.sleep(10L);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success("慢调用比例熔断策略");
    }

    @Operation(summary = "异常比例/异常数量熔断策略")
    @GetMapping(value = "sentinel/error/ratio")
    public Result sentinelRatio() {
        double randomNumber;
        randomNumber = Math.random();
        if (randomNumber >= 0 && randomNumber <= 0.80) {
            throw new SystemException("系统异常");
        }
        return Result.success("异常比例/异常数量熔断策略");
    }

    @Operation(summary = "Gateway路由转发测试")
    @GetMapping(value = "gateway/forward")
    public Result gatewayForward() {
        return Result.success("origin-service-user 测试数据");
    }

    @Operation(summary = "缓存测试设置值")
    @GetMapping(value = "redis/set")
    public Result redisSet(@RequestParam("id") String id) {
        RMap<String, String> m = redisson.getMap("test", StringCodec.INSTANCE);
        m.put("1", id);
        return Result.success("设置成功");
    }

    @Operation(summary = "缓存测试获取值")
    @GetMapping(value = "redis/get")
    public Result redisGet() {
        BoundHashOperations<String, String, String> hash = template.boundHashOps("test");
        String t = hash.get("1");
        return Result.success(t);
    }

}
