package com.example.carrental.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("app")
@Data
@Validated
public class ApplicationConfig {

  @NotNull
  private int billValidityPeriodInMinutes;
  @NotNull
  private long jwtExpirationMs;
  @NotBlank
  private String clientAddress;
  @NotBlank
  private String emailConfirmationLink;
  @NotBlank
  private String emailRestorationLink;
  @NotBlank
  private String jwtSecret;
  @NotBlank
  private String name;
  @NotBlank
  private String rentalEmail;
}
