package com.example.carrental.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Common application configurations.
 * <p>
 * This class provides common Rental data.
 * </p>
 * @author Yury Raichonak
 */
@Component
@ConfigurationProperties("app")
@Data
@Validated
public class ApplicationPropertiesConfig {

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
