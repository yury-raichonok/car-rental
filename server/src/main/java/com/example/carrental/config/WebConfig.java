package com.example.carrental.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configurations.
 *
 * @author Yury Raichonak
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final ApplicationPropertiesConfig applicationPropertiesConfig;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedMethods(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name())
        .allowedOrigins(applicationPropertiesConfig.getClientAddress())
        .allowCredentials(true);
  }
}
