package com.tscredit.origin.gateway.config.swagger;


import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class SwaggerProvider {

    public static final String API_URI = "/%s/v3/api-docs";

    private final SwaggerUiConfigProperties configProperties;
    private final RouteDefinitionLocator routeLocator;

    public SwaggerProvider(SwaggerUiConfigProperties configProperties, RouteDefinitionLocator routeLocator) {
        this.configProperties = configProperties;
        this.routeLocator = routeLocator;
    }

    @Scheduled(fixedDelay = 5)
    public void apis() {
        // 获取所有可用的服务地址: host
        List<RouteDefinition> definitions = routeLocator.getRouteDefinitions().collectList().block();
        if (CollectionUtils.isEmpty(definitions)) {
            return;
        }
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        definitions.stream().filter(route -> route.getUri().getHost() != null)
                .distinct()
                .forEach(route -> {
                            String name = route.getUri().getHost();
                            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                            swaggerUrl.setName(name);
                            swaggerUrl.setUrl(String.format(API_URI, name));
                            urls.add(swaggerUrl);
                        }
                );

        configProperties.setUrls(urls);
    }

}