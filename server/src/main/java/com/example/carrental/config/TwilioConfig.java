package com.example.carrental.config;

import com.twilio.Twilio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TwilioConfig {

  @Value("${twilio.account_sid}")
  private String accountSid;

  @Value("${twilio.auth_token}")
  private String authToken;

  @Bean
  public void initTwilio() {
    Twilio.init(accountSid, authToken);
  }
}
