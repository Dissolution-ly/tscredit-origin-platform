package com.tscredit.service.system.client.feign;


import com.tscredit.platform.base.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "origin-service-user", contextId = "ResourceClient")
public interface IResourceFeign {

    /**
     * 查询Resource最大id
     *
     * @return
     */
    @GetMapping("/feign/resource/query/max/id")
    Result<Object> queryResourceMaxId();

}
