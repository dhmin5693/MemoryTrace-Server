package com.memorytrace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        System.out.println("******************************Configuring swagger resource handler");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addRedirectViewController("/api/v2/api-docs", "/v2/api-docs");
//        registry.addRedirectViewController("/api/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
//        registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources");
//    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) //기본정보
                .select()
                .apis(RequestHandlerSelectors.any())  //주소접근 옵션
                .apis(RequestHandlerSelectors.basePackage("com.memorytrace.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     *  Swagger API INFO
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("기록이 최고야 API with Swagger")
                .description("스웨거 테스트")
                .version("2.0")
                .build();
    }
}
