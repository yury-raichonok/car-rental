package com.example.carrental.config;

import com.twilio.Twilio;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("twilio")
@Data
@Validated
public class TwilioConfig {

  @NotBlank
  private String accountSid;
  @NotBlank
  private String authToken;
  @NotBlank
  private String trialNumber;

  @Bean
  public void initTwilio() {
    Twilio.init(accountSid, authToken);
  }
}
