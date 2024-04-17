package com.example.cocktails.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration class for Swagger API documentation.
 */
@Configuration
public class SwaggerConfiguration {

  /**
   * Configures and returns a Docket instance for Swagger API documentation.
   *
   * @return a configured Docket instance
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.cocktails.controller"))
        .paths(PathSelectors.any())
        .build();
  }
}
