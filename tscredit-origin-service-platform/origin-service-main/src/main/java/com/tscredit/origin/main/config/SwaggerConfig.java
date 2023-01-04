package com.tscredit.origin.main.config;


import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${object.version:1.0.0}")
    private String version;

    @Value("${object.artifactId:项目名称}")
    private String artifactId;

    @Value("${object.description:项目开发者很懒，什么都没有...}")
    private String description;

//    @Bean
//    public OpenAPI springShopOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("SpringShop API")
//                        .description("Spring shop sample application")
//                        .version("v0.0.1")
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
//                .externalDocs(new ExternalDocumentation()
//                        .description("SpringShop Wiki Documentation")
//                        .url("https://springshop.wiki.github.org/docs"));
//    }

    /**
     * 添加分组
     * @return
     */
    @Bean
    public GroupedOpenApi publicApi() {

//               .pathMapping("/")
//                .apiInfo(apiInfo()).enable(true)
//                .enable(env.contains("dev") | env.contains("test") | env.contains("local"))
//                .groupName("WMS-2022接口")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("gr.wms.service.impl.control.controller"))
//                .paths(PathSelectors.any())
//                .build().globalOperationParameters(setHeaderToken());
        return GroupedOpenApi.builder()
                .group(artifactId)
//                .pathsToExclude("/xxx")
                .packagesToScan("gr.wms.service.impl")
                .build();
    }

}
