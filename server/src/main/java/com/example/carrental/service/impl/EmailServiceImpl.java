package com.example.carrental.service.impl;

import com.example.carrental.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String email;

  @Override
  @Transactional
  public String sendEmail(String to, String body, String topic) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom(email);
      simpleMailMessage.setTo(to);
      simpleMailMessage.setSubject(topic);
      simpleMailMessage.setText(body);
      mailSender.send(simpleMailMessage);
    } catch (MailSendException e) {
      log.error("Email was not sent to the mail {}", to);
      throw new MailSendException(String.format("Email was not sent to the mail %s", to));
    }
    return "Success";
  }
}
