package com.actionworks.flashsale.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfo("高并发秒杀系统", "方便的API调用", ApiInfo.DEFAULT.getVersion(),
                ApiInfo.DEFAULT.getTermsOfServiceUrl(), ApiInfo.DEFAULT.getContact(), ApiInfo.DEFAULT.getLicense(),
                ApiInfo.DEFAULT.getLicenseUrl(), ApiInfo.DEFAULT.getVendorExtensions());

        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com.actionworks.flashsale.controller.resource")).build();
    }
}
