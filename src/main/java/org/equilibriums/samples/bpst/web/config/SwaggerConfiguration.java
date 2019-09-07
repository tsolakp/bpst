package org.equilibriums.samples.bpst.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket createApi() {  
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("bpst")
            .apiInfo( new ApiInfoBuilder()
                .title("BPST")
                .build() )  
            .select()         
            .apis( RequestHandlerSelectors.withClassAnnotation(RestController.class) )         
            .paths( PathSelectors.ant("/**") )                         
            .build();
    } 
}
