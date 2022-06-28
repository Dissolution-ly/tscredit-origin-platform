package com.tscredit.origin.oauth.securtiy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    /**
     * 允许请求的域名
     */
    private List<String> allowedOrigins = new ArrayList<>();
 
    /**
     * 允许请求http的方法
     */
    private List<String> allowedMethods = new ArrayList<String>();
 
    /**
     * 允许请求http头信息
     */
    private List<String> allowedHeaders = new ArrayList<String>();
 
    /**
     * 排除请求http头信息
     */
    private List<String> exposedHeaders = new ArrayList<String>();
 
    /**
     * Options请求的最大缓存时间
     */
    private long maxAge = 60;
 
    /**
     * 是否允许请求时携带Cookie
     */
    private Boolean allowCredentials = false;

}
