package com.example.carrental.service.impl;

import com.example.carrental.config.ApplicationPropertiesConfig;
import com.example.carrental.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final ApplicationPropertiesConfig applicationPropertiesConfig;
  private final JavaMailSender mailSender;

  @Override
  @Transactional
  public void sendEmail(String to, String body, String topic) {
    try {
      var simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom(applicationPropertiesConfig.getRentalEmail());
      simpleMailMessage.setTo(to);
      simpleMailMessage.setSubject(topic);
      simpleMailMessage.setText(body);
      mailSender.send(simpleMailMessage);
    } catch (MailSendException e) {
      log.error("Email was not sent to the mail {}. Exception: {}", to, e.getMessage());
    }
  }
}
