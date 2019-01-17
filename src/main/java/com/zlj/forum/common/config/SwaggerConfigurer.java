package com.zlj.forum.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author tori
 * @description
 * @date 2018/8/25 下午9:33
 */

@Configuration
@EnableSwagger2
public class SwaggerConfigurer {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zlj.forum.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Spring Boot中使用swagger2")
                .description("rest api 文档构建")
                .termsOfServiceUrl("http://localhost:8080/swag")
                .contact(new Contact("zlj", "", "949941475@qq.com"))
                .license("license")
                .licenseUrl("http://licenseUrl")
                .version("V1.0.0")
                .build();

    }
}
